package club.euphoria_recruiting.store.jdbc;

import com.typesafe.config.Config;
import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

// DataSource class adapted from https://stackoverflow.com/a/7592081
public final class DataSource {
    private static final BasicDataSource ds = new BasicDataSource();

    public DataSource(final Config config) {
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUsername(config.getString("mysql.user"));
        ds.setPassword(config.getString("mysql.password"));
        ds.setUrl(config.getString("mysql.jdbc"));
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}