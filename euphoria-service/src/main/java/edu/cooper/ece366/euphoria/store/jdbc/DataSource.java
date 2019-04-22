package edu.cooper.ece366.euphoria.store.jdbc;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

// DataSource class adapted from https://stackoverflow.com/a/7592081
public final class DataSource {
    private static final BasicDataSource ds = new BasicDataSource();

    static {
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUsername("euphoria");
        ds.setPassword("euphoria");
        ds.setUrl("jdbc:mysql://localhost:3306/euphoria");
    }

    private DataSource() {}

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}