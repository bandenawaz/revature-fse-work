package org.example.service;

import org.example.model.Transaction;

import java.time.LocalDateTime;

/**
     * Retry task for failed transactions.
     */
    public class RetryTask {
        private final Transaction transaction;
        private final LocalDateTime nextRetryTime;
        private final int attemptNumber;
        private final String failureReason;

        public RetryTask(Transaction transaction, LocalDateTime nextRetryTime,
                         int attemptNumber, String failureReason) {
            this.transaction = transaction;
            this.nextRetryTime = nextRetryTime;
            this.attemptNumber = attemptNumber;
            this.failureReason = failureReason;
        }

        public Transaction getTransaction() { return transaction; }
        public LocalDateTime getNextRetryTime() { return nextRetryTime; }
        public int getAttemptNumber() { return attemptNumber; }
        public String getFailureReason() { return failureReason; }
    }
