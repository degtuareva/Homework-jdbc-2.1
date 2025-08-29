package com.product.star.account.manager;

import com.product.star.account.manager.config.ApplicationConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Main {

    public static void main(String[] args) {
        var applicationContext = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);

        var namedJdbcAccountDao = applicationContext.getBean(NamedJdbcAccountDao.class);
        var transactionTemplate = applicationContext.getBean(TransactionTemplate.class);
        var accountService = applicationContext.getBean(AccountService.class);

        namedJdbcAccountDao.deleteAll();

        namedJdbcAccountDao.addAccount(1, 1000);
        namedJdbcAccountDao.addAccount(2, 2000);

        try {
            accountService.transfer(1, 2, 500);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(namedJdbcAccountDao.getAllAccounts());


    }
}
