package org.example.model;

/*
* Represents a bank account
* Mutable balance fields protected by Synchronization
 */

import java.math.BigDecimal;
import java.util.Objects;

public class Account {
    private final String accountId;
    private final String customerId;
    private final AccountType type;
    private final boolean vipCustomer;
    private BigDecimal balance;


    //Enum for Account type
    public enum AccountType {
        CHECKING,
        SAVINGS,
        CREDIT
    }

    public Account(String accountId, String customerId, AccountType type, BigDecimal initialBalance,
                   boolean vipCustomer) {
        this.accountId = Objects.requireNonNull(accountId);
        this.customerId = Objects.requireNonNull(customerId);
        this.type = Objects.requireNonNull(type);
        this.balance = Objects.requireNonNull(initialBalance);
        this.vipCustomer = vipCustomer;

    }

    public String getAccountId() {
        return accountId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public AccountType getType() {
        return type;
    }

    public boolean isVipCustomer() {
        return vipCustomer;
    }

    public synchronized BigDecimal getBalance() {
        return balance;
    }

    public synchronized void updateBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    //equals/hashcode based on accountid only

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountId, account.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(accountId);
    }

    @Override
    public String toString() {
        return String.format(
                "Account{id = '%s',Customer='%s, balance=%s, VIP=%s}", accountId, customerId, balance, vipCustomer
        );
    }
}
