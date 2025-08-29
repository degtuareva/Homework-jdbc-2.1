package com.product.star.account.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class JdbcAccountService implements AccountService {

    private final AccountDao accountDao;
    private final TransactionTemplate transactionTemplate;

    @Autowired
    public JdbcAccountService(AccountDao accountDao, TransactionTemplate transactionTemplate) {
        this.accountDao = accountDao;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public void transfer(long from, long to, long amount) {
        transactionTemplate.executeWithoutResult(
                st -> {
                    Account accountFrom;
                    Account accountTo;
                    var accounts = Stream.of(from, to)
                            .sorted()
                            .map(accountDao::lockAccount)
                            .collect(Collectors.toList());
                    if (accounts.get(0).getId() == from) {
                        accountFrom = accounts.get(0);
                        accountTo = accounts.get(1);
                    } else {
                        accountFrom = accounts.get(1);
                        accountTo = accounts.get(0);
                    }
                    if (accountFrom.getAmount() < amount) {
                        throw new IllegalStateException("Account: " + accountFrom + "" +
                                "does not have enough money to transfer");
                    }

                    var newAmountFrom = accountFrom.getAmount() - amount;
                    var newAmountTo = accountTo.getAmount() + amount;

                    accountDao.setAmount(from, newAmountFrom);
                    accountDao.setAmount(to, newAmountTo);
                }
        );
    }
}
