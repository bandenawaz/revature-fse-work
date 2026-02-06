package org.example.service;

/**
     * Result of transaction submission.
     */
    public class SubmissionResult {
        private final String transactionId;
        private final boolean success;
        private final String reason;

        private SubmissionResult(String transactionId, boolean success, String reason) {
            this.transactionId = transactionId;
            this.success = success;
            this.reason = reason;
        }

        public static SubmissionResult success(String transactionId) {
            return new SubmissionResult(transactionId, true, null);
        }

        public static SubmissionResult duplicate(String transactionId) {
            return new SubmissionResult(transactionId, false, "Duplicate transaction");
        }

        public static SubmissionResult fraudulent(String transactionId) {
            return new SubmissionResult(transactionId, false, "Fraudulent pattern detected");
        }

        public static SubmissionResult accountNotFound(String accountId) {
            return new SubmissionResult(null, false, "Account not found: " + accountId);
        }

        public static SubmissionResult error(String transactionId, String errorMessage) {
            return new SubmissionResult(transactionId, false, errorMessage);
        }

        public boolean isSuccess() { return success; }
        public String getTransactionId() { return transactionId; }
        public String getReason() { return reason; }

}