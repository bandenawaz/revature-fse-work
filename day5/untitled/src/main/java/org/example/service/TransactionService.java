package org.example.service;


import org.example.model.Account;
import org.example.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Production-ready transaction processing service.
 * Thread-safe, high-performance, with comprehensive error handling.
 */
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    // 1. DUPLICATE DETECTION - Fast O(1) lookup
    // ConcurrentHashMap.newKeySet() creates thread-safe Set
    private final Set<String> processedTransactionIds = ConcurrentHashMap.newKeySet();

    // 2. ACCOUNT CACHE - Fast account lookups
    // ConcurrentHashMap for thread-safe concurrent access
    private final Map<String, Account> accountCache;

    // 3. PROCESSING QUEUE - Priority-based processing
    // PriorityBlockingQueue is thread-safe PriorityQueue
    private final PriorityBlockingQueue<Transaction> processingQueue;

    // 4. TRANSACTION HISTORY - Per-account transaction list
    // ConcurrentHashMap with ArrayList values
    // ReadWriteLock protects list modifications
    private final Map<String, List<Transaction>> transactionHistory;
    private final ReadWriteLock historyLock = new ReentrantReadWriteLock();

    // 5. RETRY QUEUE - Failed transactions with scheduled retry
    private final PriorityBlockingQueue<RetryTask> retryQueue;

    // 6. AUDIT LOG - Ordered operations log
    // LinkedHashMap maintains insertion order
    // Size-limited to prevent memory leak
    private final Map<String, AuditEntry> auditLog;
    private static final int MAX_AUDIT_ENTRIES = 10000;

    // 7. DAILY STATISTICS - Aggregated by date
    // TreeMap keeps dates sorted
    private final TreeMap<LocalDate, DailyStatistics> dailyStats;
    private final ReadWriteLock statsLock = new ReentrantReadWriteLock();

    // 8. FRAUD DETECTION - Track transaction count per account
    private final Map<String, Deque<LocalDateTime>> accountActivityTracking;
    private static final int FRAUD_THRESHOLD = 10;  // 10 transactions in 5 minutes
    private static final int FRAUD_WINDOW_MINUTES = 5;

    public TransactionService(int initialAccountCapacity) {
        // Pre-size maps for known capacity - avoids resizing
        this.accountCache = new ConcurrentHashMap<>(initialAccountCapacity);
        this.transactionHistory = new ConcurrentHashMap<>(initialAccountCapacity);
        this.accountActivityTracking = new ConcurrentHashMap<>(initialAccountCapacity);

        // Priority comparator: VIP + priority + amount
        this.processingQueue = new PriorityBlockingQueue<>(1000,
                new TransactionPriorityComparator());

        // Retry queue ordered by next retry time
        this.retryQueue = new PriorityBlockingQueue<>(100,
                Comparator.comparing(RetryTask::getNextRetryTime));

        // LinkedHashMap with access-order for LRU behavior
        this.auditLog = Collections.synchronizedMap(
                new LinkedHashMap<String, AuditEntry>(MAX_AUDIT_ENTRIES, 0.75f, true) {
                    @Override
                    protected boolean removeEldestEntry(Map.Entry<String, AuditEntry> eldest) {
                        return size() > MAX_AUDIT_ENTRIES;
                    }
                }
        );

        this.dailyStats = new TreeMap<>();  // TreeMap for sorted dates

        logger.info("TransactionService initialized with capacity: {}", initialAccountCapacity);
    }

    /**
     * Submit a transaction for processing.
     * Performs duplicate detection, fraud check, and queues for processing.
     */
    public SubmissionResult submitTransaction(Transaction transaction) {
        logger.debug("Submitting transaction: {}", transaction.getTransactionId());

        try {
            // STEP 1: Duplicate detection using Set
            if (!processedTransactionIds.add(transaction.getTransactionId())) {
                logger.warn("Duplicate transaction detected: {}", transaction.getTransactionId());
                recordAudit(transaction.getTransactionId(), "DUPLICATE_REJECTED", null);
                return SubmissionResult.duplicate(transaction.getTransactionId());
            }

            // STEP 2: Fraud detection using Deque for time-window tracking
            if (isFraudulent(transaction)) {
                logger.warn("Fraudulent pattern detected for account: {}", transaction.getAccountId());
                processedTransactionIds.remove(transaction.getTransactionId());  // Allow retry later
                recordAudit(transaction.getTransactionId(), "FRAUD_REJECTED", null);
                return SubmissionResult.fraudulent(transaction.getTransactionId());
            }

            // STEP 3: Validate account exists in cache
            Account account = accountCache.get(transaction.getAccountId());
            if (account == null) {
                logger.error("Account not found: {}", transaction.getAccountId());
                processedTransactionIds.remove(transaction.getTransactionId());
                recordAudit(transaction.getTransactionId(), "ACCOUNT_NOT_FOUND", null);
                return SubmissionResult.accountNotFound(transaction.getAccountId());
            }

            // STEP 4: Add to processing queue (PriorityQueue handles ordering)
            processingQueue.offer(transaction);

            // STEP 5: Track activity for fraud detection (Deque for efficient sliding window)
            trackActivity(transaction);

            // STEP 6: Record audit
            recordAudit(transaction.getTransactionId(), "SUBMITTED", null);

            logger.info("Transaction submitted successfully: {}", transaction.getTransactionId());
            return SubmissionResult.success(transaction.getTransactionId());

        } catch (Exception e) {
            logger.error("Error submitting transaction: {}", transaction.getTransactionId(), e);
            processedTransactionIds.remove(transaction.getTransactionId());
            return SubmissionResult.error(transaction.getTransactionId(), e.getMessage());
        }
    }

    /**
     * Process the next transaction from the priority queue.
     * Called by worker threads.
     */
    public void processNextTransaction() {
        Transaction transaction = processingQueue.poll();
        if (transaction == null) {
            return;  // Queue empty
        }

        logger.debug("Processing transaction: {}", transaction.getTransactionId());

        try {
            // STEP 1: Get account from cache (O(1) lookup)
            Account account = accountCache.get(transaction.getAccountId());
            if (account == null) {
                logger.error("Account not found during processing: {}", transaction.getAccountId());
                scheduleRetry(transaction, "Account not found", 1);
                return;
            }

            // STEP 2: Validate sufficient balance
            BigDecimal requiredAmount = getRequiredAmount(transaction);
            if (account.getBalance().compareTo(requiredAmount) < 0) {
                logger.warn("Insufficient balance for transaction: {}", transaction.getTransactionId());
                recordAudit(transaction.getTransactionId(), "INSUFFICIENT_BALANCE", null);
                return;  // Don't retry - business rule
            }

            // STEP 3: Execute transaction (simulate)
            executeTransaction(account, transaction);

            // STEP 4: Add to transaction history (Map + List)
            addToHistory(transaction);

            // STEP 5: Update daily statistics (TreeMap for sorted dates)
            updateDailyStatistics(transaction);

            // STEP 6: Record success
            recordAudit(transaction.getTransactionId(), "PROCESSED", null);

            logger.info("Transaction processed successfully: {}", transaction.getTransactionId());

        } catch (Exception e) {
            logger.error("Error processing transaction: {}", transaction.getTransactionId(), e);
            scheduleRetry(transaction, e.getMessage(), 1);
        }
    }

    /**
     * Fraud detection using Deque for sliding window pattern.
     * Tracks transactions in last N minutes.
     */
    private boolean isFraudulent(Transaction transaction) {
        String accountId = transaction.getAccountId();

        // Get or create activity tracking deque for this account
        Deque<LocalDateTime> activities = accountActivityTracking.computeIfAbsent(
                accountId, k -> new ArrayDeque<>()
        );

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime windowStart = now.minusMinutes(FRAUD_WINDOW_MINUTES);

        // Remove old activities outside the time window (from front of deque)
        synchronized (activities) {
            while (!activities.isEmpty() && activities.peekFirst().isBefore(windowStart)) {
                activities.pollFirst();
            }

            // Check if threshold exceeded
            if (activities.size() >= FRAUD_THRESHOLD) {
                logger.warn("Fraud threshold exceeded for account {}: {} transactions in {} minutes",
                        accountId, activities.size(), FRAUD_WINDOW_MINUTES);
                return true;
            }
        }

        return false;
    }

    /**
     * Track transaction activity for fraud detection.
     */
    private void trackActivity(Transaction transaction) {
        Deque<LocalDateTime> activities = accountActivityTracking.get(transaction.getAccountId());
        if (activities != null) {
            synchronized (activities) {
                activities.offerLast(transaction.getTimestamp());
            }
        }
    }

    /**
     * Add transaction to history using Map<String, List<Transaction>>.
     * Uses ReadWriteLock for thread safety with minimal contention.
     */
    private void addToHistory(Transaction transaction) {
        String accountId = transaction.getAccountId();

        // Read lock to check if list exists
        historyLock.readLock().lock();
        try {
            List<Transaction> history = transactionHistory.get(accountId);
            if (history != null) {
                synchronized (history) {
                    history.add(transaction);
                }
                return;
            }
        } finally {
            historyLock.readLock().unlock();
        }

        // Write lock to create new list
        historyLock.writeLock().lock();
        try {
            // Double-check after acquiring write lock
            List<Transaction> history = transactionHistory.get(accountId);
            if (history == null) {
                history = new ArrayList<>();
                transactionHistory.put(accountId, history);
            }
            synchronized (history) {
                history.add(transaction);
            }
        } finally {
            historyLock.writeLock().unlock();
        }
    }

    /**
     * Update daily statistics using TreeMap for sorted dates.
     */
    private void updateDailyStatistics(Transaction transaction) {
        LocalDate date = transaction.getTimestamp().toLocalDate();

        statsLock.writeLock().lock();
        try {
            DailyStatistics stats = dailyStats.computeIfAbsent(date, k -> new DailyStatistics(date));
            stats.addTransaction(transaction);
        } finally {
            statsLock.writeLock().unlock();
        }
    }

    /**
     * Schedule failed transaction for retry using PriorityQueue.
     * Implements exponential backoff.
     */
    private void scheduleRetry(Transaction transaction, String reason, int attemptNumber) {
        if (attemptNumber > 5) {
            logger.error("Max retry attempts exceeded for transaction: {}", transaction.getTransactionId());
            recordAudit(transaction.getTransactionId(), "MAX_RETRIES_EXCEEDED", reason);
            return;
        }

        // Exponential backoff: 2^attemptNumber seconds
        long delaySeconds = (long) Math.pow(2, attemptNumber);
        LocalDateTime nextRetry = LocalDateTime.now().plusSeconds(delaySeconds);

        RetryTask retryTask = new RetryTask(transaction, nextRetry, attemptNumber, reason);
        retryQueue.offer(retryTask);

        logger.info("Scheduled retry for transaction {} at {} (attempt {})",
                transaction.getTransactionId(), nextRetry, attemptNumber);

        recordAudit(transaction.getTransactionId(), "RETRY_SCHEDULED",
                String.format("Attempt %d at %s", attemptNumber, nextRetry));
    }

    /**
     * Process retry queue - called periodically by scheduler.
     */
    public void processRetries() {
        LocalDateTime now = LocalDateTime.now();

        while (!retryQueue.isEmpty()) {
            RetryTask task = retryQueue.peek();

            if (task.getNextRetryTime().isAfter(now)) {
                break;  // Not ready yet (queue is sorted by time)
            }

            retryQueue.poll();
            logger.info("Retrying transaction: {} (attempt {})",
                    task.getTransaction().getTransactionId(), task.getAttemptNumber());

            // Re-submit to processing queue
            processingQueue.offer(task.getTransaction());
        }
    }

    /**
     * Get transaction history for an account.
     * Returns defensive copy to prevent external modification.
     */
    public List<Transaction> getTransactionHistory(String accountId) {
        historyLock.readLock().lock();
        try {
            List<Transaction> history = transactionHistory.get(accountId);
            if (history == null) {
                return Collections.emptyList();
            }

            synchronized (history) {
                // Return unmodifiable copy
                return Collections.unmodifiableList(new ArrayList<>(history));
            }
        } finally {
            historyLock.readLock().unlock();
        }
    }

    /**
     * Get daily statistics for a date range.
     * Uses TreeMap.subMap for efficient range query.
     */
    public Map<LocalDate, DailyStatistics> getDailyStatistics(LocalDate startDate, LocalDate endDate) {
        statsLock.readLock().lock();
        try {
            // TreeMap.subMap returns sorted view
            return new TreeMap<>(dailyStats.subMap(startDate, true, endDate, true));
        } finally {
            statsLock.readLock().unlock();
        }
    }

    /**
     * Get account balance.
     */
    public BigDecimal getAccountBalance(String accountId) {
        Account account = accountCache.get(accountId);
        return account != null ? account.getBalance() : null;
    }

    /**
     * Register a new account in the cache.
     */
    public void registerAccount(Account account) {
        accountCache.put(account.getAccountId(), account);
        logger.info("Account registered: {}", account.getAccountId());
    }

    /**
     * Get processing queue size.
     */
    public int getQueueSize() {
        return processingQueue.size();
    }

    /**
     * Get retry queue size.
     */
    public int getRetryQueueSize() {
        return retryQueue.size();
    }

    /**
     * Get audit trail (last N entries).
     */
    public List<AuditEntry> getRecentAuditEntries(int count) {
        synchronized (auditLog) {
            return auditLog.values().stream()
                    .skip(Math.max(0, auditLog.size() - count))
                    .toList();
        }
    }

    /**
     * Record audit entry.
     */
    private void recordAudit(String transactionId, String status, String details) {
        AuditEntry entry = new AuditEntry(
                transactionId,
                status,
                LocalDateTime.now(),
                details
        );
        auditLog.put(transactionId + "_" + System.nanoTime(), entry);
    }

    /**
     * Simulate transaction execution.
     */
    private void executeTransaction(Account account, Transaction transaction) {
        BigDecimal amount = getRequiredAmount(transaction);
        account.updateBalance(amount.negate());  // Deduct from balance
        logger.debug("Executed transaction {} for account {}",
                transaction.getTransactionId(), account.getAccountId());
    }

    /**
     * Get required amount based on transaction type.
     */
    private BigDecimal getRequiredAmount(Transaction transaction) {
        return switch (transaction.getType()) {
            case PAYMENT, WITHDRAWAL, TRANSFER -> transaction.getAmount();
            case DEPOSIT -> transaction.getAmount().negate();  // Deposits add to balance
        };
    }

    // ============================================
    // SUPPORTING CLASSES
    // ============================================

    /**
     * Comparator for transaction priority.
     * Order: VIP customers > priority > amount
     */
    private static class TransactionPriorityComparator implements Comparator<Transaction> {
        @Override
        public int compare(Transaction t1, Transaction t2) {
            // VIP customers first (need Account lookup - simplified here)
            // In production, would check account.isVipCustomer()

            // Higher priority first
            int priorityCompare = Integer.compare(t2.getPriority(), t1.getPriority());
            if (priorityCompare != 0) {
                return priorityCompare;
            }

            // Larger amounts first
            int amountCompare = t2.getAmount().compareTo(t1.getAmount());
            if (amountCompare != 0) {
                return amountCompare;
            }

            // Earlier timestamp first (FIFO for same priority/amount)
            return t1.getTimestamp().compareTo(t2.getTimestamp());
        }
    }

    /**
     * Audit log entry.
     */
    public class AuditEntry {
        private final String transactionId;
        private final String status;
        private final LocalDateTime timestamp;
        private final String details;

        public AuditEntry(String transactionId, String status,
                          LocalDateTime timestamp, String details) {
            this.transactionId = transactionId;
            this.status = status;
            this.timestamp = timestamp;
            this.details = details;
        }

        public String getTransactionId() { return transactionId; }
        public String getStatus() { return status; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public String getDetails() { return details; }

        @Override
        public String toString() {
            return String.format("[%s] %s - %s: %s",
                    timestamp, transactionId, status, details);
        }
    }


}