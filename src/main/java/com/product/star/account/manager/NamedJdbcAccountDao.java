package com.product.star.account.manager;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
@Primary
public class NamedJdbcAccountDao implements AccountDao {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public NamedJdbcAccountDao(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }


    @Override
    public Account addAccount(long id, long amount) {
        namedJdbcTemplate.update(
                "INSERT INTO ACCOUNT(ID, AMOUNT) VALUES(:id, :amount)",
                new MapSqlParameterSource()
                    .addValue("id", id)
                    .addValue("amount", amount)
        );
        return new Account(id, amount);
    }

    @Override
    public Account addAccount(long amount) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedJdbcTemplate.update(
                "INSERT INTO ACCOUNT(AMOUNT) VALUES(:amount)",
                new MapSqlParameterSource("amount", amount),
                keyHolder,
                new String[] { "id" }
        );

        var accountId = keyHolder.getKey().longValue();
        return new Account(accountId, amount);
    }

    @Override
    public void deleteAll() {
        namedJdbcTemplate.update("DELETE FROM ACCOUNT", EmptySqlParameterSource.INSTANCE);
    }

    @Override
    public List<Account> addAccounts(List<Account> accounts) {
        var args = accounts.stream()
                .map(account -> new MapSqlParameterSource()
                        .addValue("id", account.getId())
                        .addValue("amount", account.getAmount()))
                .toArray(MapSqlParameterSource[]::new);
        namedJdbcTemplate.batchUpdate(
                "INSERT INTO ACCOUNT(ID, AMOUNT) VALUES(:id, :amount)",
                args
        );
        return accounts;
    }


    @Override
    public Account getAccount(long accountId) {
        return namedJdbcTemplate.queryForObject(
                "SELECT ID, AMOUNT FROM ACCOUNT WHERE ID = :id",
                new MapSqlParameterSource("id", accountId),
                (rs, i) -> new Account(rs.getLong("ID"), rs.getLong("AMOUNT"))
        );
    }

    @Override
    public Account lockAccount(long accountId) {
        return namedJdbcTemplate.queryForObject(
                "SELECT ID, AMOUNT FROM ACCOUNT WHERE ID = :id FOR UPDATE",
                new MapSqlParameterSource("id", accountId),
                (rs, i) -> new Account(rs.getLong("ID"), rs.getLong("AMOUNT"))
        );
    }

    @Override
    public List<Account> getAccounts(Collection<Long> accountsIds) {
        return namedJdbcTemplate.query(
                "SELECT ID, AMOUNT FROM ACCOUNT WHERE ID IN (:ids)",
                new MapSqlParameterSource("ids", accountsIds),
                (rs, i) -> new Account(rs.getLong("ID"), rs.getLong("AMOUNT"))
        );
    }

    @Override
    public void setAmount(long accountId, long amount) {
        namedJdbcTemplate.update(
                "UPDATE ACCOUNT SET AMOUNT = :amount WHERE ID = :id",
                new MapSqlParameterSource()
                        .addValue("id", accountId)
                        .addValue("amount", amount)
        );
    }

    @Override
    public void updateAccounts(Collection<Account> accounts) {
        var args = accounts.stream()
                .map(account -> new MapSqlParameterSource()
                        .addValue("id", account.getId())
                        .addValue("amount", account.getAmount()))
                .toArray(MapSqlParameterSource[]::new);
        namedJdbcTemplate.batchUpdate(
                "UPDATE ACCOUNT SET AMOUNT = :amount WHERE ID = :id",
                args
        );
    }

    @Override
    public List<Account> getAllAccounts() {
        return namedJdbcTemplate.query(
                "SELECT ID, AMOUNT FROM ACCOUNT",
                (rs, i) -> new Account(rs.getLong("ID"), rs.getLong("AMOUNT"))
        );
    }

    @Override
    public AccountStats getTotal() {
        return namedJdbcTemplate.queryForObject(
                "SELECT " +
                "COUNT(ID) as ID_COUNT, " +
                "SUM(AMOUNT) as TOTAL_SUM " +
                "FROM ACCOUNT",
                EmptySqlParameterSource.INSTANCE,
                (rs, i) -> new AccountStats(rs.getLong("ID_COUNT"), rs.getLong("TOTAL_SUM"))
        );
    }
}
