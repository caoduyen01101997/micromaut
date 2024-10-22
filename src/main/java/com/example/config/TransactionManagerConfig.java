package com.example.config;

import io.micronaut.context.annotation.Factory;
import io.micronaut.transaction.jdbc.DataSourceTransactionManager;
import jakarta.inject.Singleton;

import javax.sql.DataSource;

@Factory
public class TransactionManagerConfig {
    @Singleton
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
