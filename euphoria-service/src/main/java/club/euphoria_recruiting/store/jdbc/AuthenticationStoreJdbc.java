package club.euphoria_recruiting.store.jdbc;

import club.euphoria_recruiting.model.Authentication;
import club.euphoria_recruiting.model.AuthenticationBuilder;
import club.euphoria_recruiting.store.model.AuthenticationStore;
import org.apache.commons.dbutils.DbUtils;

import java.sql.*;
import java.util.Collections;
import java.util.List;

public class AuthenticationStoreJdbc implements AuthenticationStore {

    private static final String GET_AUTHENTICATION_STATEMENT = "SELECT * FROM authentications WHERE (username, passwordHash) IN ((?, ?))";
    private static final String CREATE_AUTHENTICATION_STATEMENT = "INSERT INTO authentications (id, username, passwordHash, isUser)" +
            "VALUES (?, ?, ?, ?)";

    private final DataSource dataSource;

    public AuthenticationStoreJdbc(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Authentication getAuthentication(final String username, final String passwordHash) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn= dataSource.getConnection();
            ps = conn.prepareStatement(GET_AUTHENTICATION_STATEMENT);
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            rs = ps.executeQuery();

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
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
    }


    @Override
    public List<Authentication> createAuthentication(final Integer id, final String username, final String passwordHash, final Boolean isUser) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn= dataSource.getConnection();
            ps = conn.prepareStatement(CREATE_AUTHENTICATION_STATEMENT);
            ps.setInt(1, id);
            ps.setString(2, username);
            ps.setString(3, passwordHash);
            ps.setBoolean(4, isUser);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Creating new authentication failed, no rows affected.");
            }

            return Collections.emptyList(); //if everything successful, return empty list

        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
        }

        return null; //if not successful, return null
    }
}
