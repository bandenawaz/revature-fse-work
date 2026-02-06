package org.example.service;

import org.example.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;

/**
     * Daily statistics aggregation.
     */
    public class DailyStatistics {
        private final LocalDate date;
        private int transactionCount;
        private BigDecimal totalAmount;
        private final Map<Transaction.TransactionType, Integer> countByType;

        public DailyStatistics(LocalDate date) {
            this.date = date;
            this.transactionCount = 0;
            this.totalAmount = BigDecimal.ZERO;
            this.countByType = new EnumMap<>(Transaction.TransactionType.class);
        }

        public synchronized void addTransaction(Transaction transaction) {
            transactionCount++;
            totalAmount = totalAmount.add(transaction.getAmount());
            countByType.merge(transaction.getType(), 1, Integer::sum);
        }

        public LocalDate getDate() { return date; }
        public int getTransactionCount() { return transactionCount; }
        public BigDecimal getTotalAmount() { return totalAmount; }
        public Map<Transaction.TransactionType, Integer> getCountByType() {
            return new EnumMap<>(countByType);
        }

        @Override
        public String toString() {
            return String.format("DailyStats{date=%s, count=%d, total=%s, byType=%s}",
                    date, transactionCount, totalAmount, countByType);
        }
    }