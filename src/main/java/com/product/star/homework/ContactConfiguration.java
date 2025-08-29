package com.product.star.homework;


import com.product.star.account.manager.config.JdbcConfig;
import com.product.star.account.manager.config.PropertiesConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


@Configuration
@Import({JdbcConfig.class, PropertiesConfiguration.class})
public class ContactConfiguration {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ContactConfiguration(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Bean
    public ContactDao contactDao() {
        return new ContactDao(jdbcTemplate);
    }

    @Bean
    public ContactReader contactReader() {
        return new ContactReader();
    }

    @Bean
    public ContactService contactService() {
        return new ContactService(contactDao(), contactReader());
    }
}
