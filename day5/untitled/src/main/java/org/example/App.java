package org.example;

import org.example.model.Account;
import org.example.model.Transaction;
import org.example.service.DailyStatistics;
import org.example.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class App 
{
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws InterruptedException {

        // Initialize service with expected capacity
        TransactionService service = new TransactionService(1000);

        // Register some accounts
        setupAccounts(service);

        // Submit various transactions
        submitTransactions(service);

        // Process transactions with multiple worker threads
        processTransactionsInParallel(service);

        // Wait for processing to complete
        Thread.sleep(2000);

        // Process any retries
        service.processRetries();

        // Generate reports
        generateReports(service);

        logger.info("Demo completed");
    }

    private static void setupAccounts(TransactionService service) {
        logger.info("=== Setting up accounts ===");

        service.registerAccount(new Account(
                "ACC001", "CUST001", Account.AccountType.CHECKING,
                new BigDecimal("10000.00"), true  // VIP customer
        ));

        service.registerAccount(new Account(
                "ACC002", "CUST002", Account.AccountType.CHECKING,
                new BigDecimal("5000.00"), false
        ));

        service.registerAccount(new Account(
                "ACC003", "CUST003", Account.AccountType.SAVINGS,
                new BigDecimal("50000.00"), false
        ));

        logger.info("Registered 3 accounts");
    }

    private static void submitTransactions(TransactionService service) {
        logger.info("=== Submitting transactions ===");

        // Normal transaction
        Transaction txn1 = new Transaction.Builder()
                .transactionId("TXN001")
                .accountId("ACC001")
                .type(Transaction.TransactionType.PAYMENT)
                .amount(new BigDecimal("100.00"))
                .timestamp(LocalDateTime.now())
                .priority(3)
                .customerId("CUST001")
                .build();
        service.submitTransaction(txn1);

        // High priority transaction
        Transaction txn2 = new Transaction.Builder()
                .transactionId("TXN002")
                .accountId("ACC002")
                .type(Transaction.TransactionType.WITHDRAWAL)
                .amount(new BigDecimal("500.00"))
                .timestamp(LocalDateTime.now())
                .priority(5)  // High priority
                .customerId("CUST002")
                .build();
        service.submitTransaction(txn2);

        // Large amount transaction
        Transaction txn3 = new Transaction.Builder()
                .transactionId("TXN003")
                .accountId("ACC003")
                .type(Transaction.TransactionType.TRANSFER)
                .amount(new BigDecimal("5000.00"))
                .timestamp(LocalDateTime.now())
                .priority(3)
                .customerId("CUST003")
                .build();
        service.submitTransaction(txn3);

        // Duplicate transaction (will be rejected)
        service.submitTransaction(txn1);

        // Multiple transactions from same account (fraud detection)
        for (int i = 0; i < 12; i++) {
            Transaction txn = new Transaction.Builder()
                    .transactionId("TXN_FRAUD_" + i)
                    .accountId("ACC001")
                    .type(Transaction.TransactionType.PAYMENT)
                    .amount(new BigDecimal("10.00"))
                    .timestamp(LocalDateTime.now())
                    .priority(2)
                    .customerId("CUST001")
                    .build();
            service.submitTransaction(txn);
        }

        logger.info("Submitted transactions. Queue size: {}", service.getQueueSize());
    }

    private static void processTransactionsInParallel(TransactionService service) {
        logger.info("=== Processing transactions with 3 worker threads ===");

        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Start 3 worker threads
        for (int i = 0; i < 3; i++) {
            final int workerId = i;
            executor.submit(() -> {
                logger.info("Worker {} started", workerId);
                while (service.getQueueSize() > 0) {
                    service.processNextTransaction();
                }
                logger.info("Worker {} finished", workerId);
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("Interrupted while waiting for workers", e);
        }
    }

    private static void generateReports(TransactionService service) {
        logger.info("=== Generating reports ===");

        // Account balances
        logger.info("Account balances:");
        logger.info("  ACC001: {}", service.getAccountBalance("ACC001"));
        logger.info("  ACC002: {}", service.getAccountBalance("ACC002"));
        logger.info("  ACC003: {}", service.getAccountBalance("ACC003"));

        // Transaction history
        logger.info("\nTransaction history for ACC001:");
        List<Transaction> history = service.getTransactionHistory("ACC001");
        history.forEach(txn -> logger.info("  {}", txn));

        // Daily statistics
        logger.info("\nDaily statistics:");
        LocalDate today = LocalDate.now();
        Map<LocalDate, DailyStatistics> stats =
                service.getDailyStatistics(today.minusDays(7), today);
        stats.forEach((date, stat) -> logger.info("  {}", stat));

        // Audit trail
        logger.info("\nRecent audit entries:");
        List<TransactionService.AuditEntry> auditEntries = service.getRecentAuditEntries(10);
        auditEntries.forEach(entry -> logger.info("  {}", entry));

        // Queue status
        logger.info("\nQueue status:");
        logger.info("  Processing queue: {}", service.getQueueSize());
        logger.info("  Retry queue: {}", service.getRetryQueueSize());
    }
}