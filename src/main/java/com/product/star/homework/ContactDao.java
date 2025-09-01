package com.product.star.homework;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ContactDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ContactDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Contact> getAllContacts() {
        var sql = "SELECT id, name, surname, phone_number, email FROM contact";
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new Contact(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("phone_number"),
                        rs.getString("email")
                ));
    }

    public void saveAll(Collection<Contact> contacts) {
        var sql = "INSERT INTO contact (name, surname, phone_number, email) VALUES (:name, :surname, :phone, :email) " +
                "ON CONFLICT (email) DO UPDATE SET " +
                "name = EXCLUDED.name, surname = EXCLUDED.surname, phone_number = EXCLUDED.phone_number";
        var batchValues = contacts.stream()
                .map(contact -> new MapSqlParameterSource()
                        .addValue("name", contact.getName())
                        .addValue("surname", contact.getSurname())
                        .addValue("phone", contact.getPhone())
                        .addValue("email", contact.getEmail()))
                .collect(Collectors.toList());
        jdbcTemplate.batchUpdate(sql, batchValues.toArray(new MapSqlParameterSource[0]));
    }
}
