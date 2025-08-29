package com.product.star.account.manager;

public class AccountStats {

    private final long accountsNumber;
    private final long totalAmount;

    public AccountStats(long accountsNumber, long totalAmount) {
        this.accountsNumber = accountsNumber;
        this.totalAmount = totalAmount;
    }

    public long getAccountsNumber() {
        return accountsNumber;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    @Override
    public String toString() {
        return "AccountStats{" +
                "accountsNumber=" + accountsNumber +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
