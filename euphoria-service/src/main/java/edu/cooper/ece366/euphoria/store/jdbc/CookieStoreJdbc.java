package edu.cooper.ece366.euphoria.store.jdbc;

import edu.cooper.ece366.euphoria.model.Cookie;
import edu.cooper.ece366.euphoria.model.CookieBuilder;
import edu.cooper.ece366.euphoria.store.model.CookieStore;
import org.apache.commons.dbutils.DbUtils;

import java.sql.*;
import java.util.UUID;

public class CookieStoreJdbc implements CookieStore {

    private static final String GET_COOKIE_STATEMENT = "SELECT * FROM cookies WHERE (cookie) IN ((?))";
    private static final String CREATE_COOKIE_STATEMENT = "INSERT INTO cookies (id, isUser, cookie) VALUES (?, ?, ?)";
    private static final String AUTHENTICATE_LOGIN_STATEMENT = "SELECT * FROM authentications WHERE (username, passwordHash) IN ((?, ?))";

    private final DataSource dataSource;

    public CookieStoreJdbc(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Cookie getCookie(final String cookieCheck) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn= dataSource.getConnection();
            ps = conn.prepareStatement(GET_COOKIE_STATEMENT);
            ps.setString(1, cookieCheck);
            rs = ps.executeQuery();

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
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
    }


    @Override
    public Cookie createCookie(final String username, final String passwordHash) {
        Integer id;
        Boolean isUser;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn= dataSource.getConnection();
            ps = conn.prepareStatement(AUTHENTICATE_LOGIN_STATEMENT);
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            rs = ps.executeQuery();

            if (rs.first()) {
                id = rs.getInt("id");
                isUser = rs.getBoolean("isUser");

                String cookieNew = UUID.randomUUID().toString();
                ps = conn.prepareStatement(CREATE_COOKIE_STATEMENT);
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
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
    }
}
