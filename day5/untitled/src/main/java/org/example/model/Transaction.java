package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/*
* Represents a financial transaction in the system
* Immutable for thread safety
 */
public final class Transaction {

    private final String transactionId;
    private final String accountId;
    private final TransactionType type;
    private final BigDecimal amount;
    private final LocalDateTime timestamp;
    private final int priority;  // 1-5 , where 5 is the highest
    private final String customerId;

    public enum TransactionType {
        PAYMENT, TRANSFER, WITHDRAWAL, DEPOSIT
    }

    //Builder with non null validation
    private Transaction(Builder builder) {
        this.transactionId = Objects.requireNonNull(builder.transactionId, "transactionId is required");
        this.accountId = Objects.requireNonNull(builder.accountId, "accountId is required");
        this.type = Objects.requireNonNull(builder.type, "type is required");
        this.amount = Objects.requireNonNull(builder.amount, "amount is required");
        this.timestamp = Objects.requireNonNull(builder.timestamp, "timestamp is required");
        this.priority = builder.priority;
        this.customerId = builder.customerId;

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("amount must be greater than zero");
        }
        if (priority < 1  || priority > 5) {
            throw new IllegalArgumentException("priority must be between 1 and 5");
        }

    }

    //Getters
    public String getTransactionId() {
        return transactionId;
    }
    public String getAccountId() {
        return accountId;
    }
    public TransactionType getType() {
        return type;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public int getPriority() {
        return priority;
    }
    public String getCustomerId() {
        return customerId;
    }


    //Builder Pattern for clean object construction
    public static class Builder {
        private String transactionId;
        private String accountId;
        private TransactionType type;
        private BigDecimal amount;
        private LocalDateTime timestamp;
        private int priority = 3;
        private String customerId;

        public Builder transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public Builder accountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder type(TransactionType type) {
            this.type = type;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public Builder customerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }
    }

    //equals, hashcode, toString


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(transactionId);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", accountId='" + accountId + '\'' +
                ", type=" + type +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                ", priority=" + priority +
                ", customerId='" + customerId + '\'' +
                '}';
    }
}
