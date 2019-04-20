package edu.cooper.ece366.euphoria.store.jdbc;

import com.typesafe.config.Config;
import edu.cooper.ece366.euphoria.model.*;
import edu.cooper.ece366.euphoria.store.model.AuthenticationStore;

import java.sql.*;
import java.util.Collections;
import java.util.List;

public class AuthenticationStoreJdbc implements AuthenticationStore {

    private static final String GET_AUTHENTICATION_STATEMENT = "SELECT * FROM authentications WHERE (username, passwordHash) IN ((?, ?))";
    private static final String CREATE_AUTHENTICATION_STATEMENT = "INSERT INTO authentications (id, username, passwordHash, isUser)" +
                                                                  "VALUES (?, ?, ?, ?)";
    private final Config config;

    public AuthenticationStoreJdbc(final Config config) {
        this.config = config;
    }

    @Override
    public Authentication getAuthentication(final String username, final String passwordHash) {
        Connection connection;
        try {
            connection =
                    DriverManager.getConnection(
                            config.getString("mysql.jdbc"),
                            config.getString("mysql.user"),
                            config.getString("mysql.password"));

            PreparedStatement ps = connection.prepareStatement(GET_AUTHENTICATION_STATEMENT);
            ps.setString(1, username);
            ps.setString(2, passwordHash);

            ResultSet rs = ps.executeQuery();

            if (rs.first()) {
                return new AuthenticationBuilder()
                        .id(rs.getInt("id"))
                        .username(rs.getString("username"))
                        .passwordHash(rs.getString("passwordHash"))
                        .isUser(rs.getBoolean("isUser"))
                        .build();
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("error fetching authentication", e);
        }
    }


    @Override
    public List<Authentication> createAuthentication(final Integer id, final String username, final String passwordHash, final Boolean isUser) {
        Connection connection;
        try {
            connection =
                    DriverManager.getConnection(
                            config.getString("mysql.jdbc"),
                            config.getString("mysql.user"),
                            config.getString("mysql.password"));
            PreparedStatement ps = connection.prepareStatement(CREATE_AUTHENTICATION_STATEMENT);
            ps.setInt(1, id);
            ps.setString(2, username);
            ps.setString(3, passwordHash);
            ps.setBoolean(4, isUser);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating new authentication failed, no rows affected.");
            }
            return Collections.emptyList();
        } catch (SQLException e) {
            throw new RuntimeException("error creating authentication", e);
        }
    }
}
