package edu.cooper.ece366.euphoria.store.jdbc;

import com.typesafe.config.Config;
import edu.cooper.ece366.euphoria.model.Cookie;
import edu.cooper.ece366.euphoria.model.CookieBuilder;
import edu.cooper.ece366.euphoria.store.model.CookieStore;

import java.sql.*;
import java.util.UUID;

public class CookieStoreJdbc implements CookieStore {

    private static final String GET_COOKIE_STATEMENT = "SELECT * FROM cookies WHERE (cookie) IN ((?))";
    private static final String CREATE_COOKIE_STATEMENT = "INSERT INTO cookies (id, isUser, cookie) VALUES (?, ?, ?)";
    private static final String AUTHENTICATE_LOGIN_STATEMENT = "SELECT * FROM authentications WHERE (username, passwordHash) IN ((?, ?))";
    private final Config config;

    public CookieStoreJdbc(final Config config) {
        this.config = config;
    }

    @Override
    public Cookie getCookie(final String cookieCheck) {
        Connection connection;
        try {
            connection =
                    DriverManager.getConnection(
                            config.getString("mysql.jdbc"),
                            config.getString("mysql.user"),
                            config.getString("mysql.password"));

            PreparedStatement ps = connection.prepareStatement(GET_COOKIE_STATEMENT);
            ps.setString(1, cookieCheck);
            ResultSet rs = ps.executeQuery();

            if (rs.first()) {
                return new CookieBuilder()
                        .id(rs.getInt("id"))
                        .isUser(rs.getBoolean("isUser"))
                        .cookie(rs.getString("cookie"))
                        .build();
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("error fetching cookie", e);
        }
    }


    @Override
    public Cookie createCookie(final String username, final String passwordHash) {
        Integer id;
        Boolean isUser;

        Connection connection;
        try {
            connection =
                    DriverManager.getConnection(
                            config.getString("mysql.jdbc"),
                            config.getString("mysql.user"),
                            config.getString("mysql.password"));
            PreparedStatement ps = connection.prepareStatement(AUTHENTICATE_LOGIN_STATEMENT);
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            ResultSet rs = ps.executeQuery();

            if (rs.first()) {
                id = rs.getInt("id");
                isUser = rs.getBoolean("isUser");

                String cookieNew = UUID.randomUUID().toString();
                ps = connection.prepareStatement(CREATE_COOKIE_STATEMENT);
                ps.setInt(1, id); // Either userId or companyId
                ps.setBoolean(2, isUser);
                ps.setString(3, cookieNew);
                ps.executeUpdate();
                // Send back to front-end
                return new CookieBuilder()
                        .id(id)
                        .isUser(isUser)
                        .cookie(cookieNew)
                        .build();
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("error creating cookie", e);
        }
    }
}
