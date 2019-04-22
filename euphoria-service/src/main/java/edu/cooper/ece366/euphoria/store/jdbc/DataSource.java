package edu.cooper.ece366.euphoria.store.jdbc;

import com.typesafe.config.Config;
import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

// DataSource class adapted from https://stackoverflow.com/a/7592081
public class DataSource {
    private static final BasicDataSource dataSource = new BasicDataSource();

    private DataSource(final Config config) {
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername(config.getString("mysql.user"));
        dataSource.setPassword(config.getString("mysql.password")))
        dataSource.setUrl(config.getString("mysql.jdbc"));
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}