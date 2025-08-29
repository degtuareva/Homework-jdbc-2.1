package com.product.star.account.manager;

import java.util.Collection;
import java.util.List;

public interface AccountDao {

    void deleteAll();
    Account addAccount(long id, long amount);
    Account addAccount(long amount);
    List<Account> addAccounts(List<Account> accounts);
    Account getAccount(long  accountId);
    Account lockAccount(long accountId);
    List<Account> getAccounts(Collection<Long> accountsIds);
    void setAmount(long accountId, long amount);
    void updateAccounts(Collection<Account> accounts);
    List<Account> getAllAccounts();
    AccountStats getTotal();
}
