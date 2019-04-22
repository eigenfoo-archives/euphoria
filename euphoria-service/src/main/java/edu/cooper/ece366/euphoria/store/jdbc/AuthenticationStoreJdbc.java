package edu.cooper.ece366.euphoria.store.jdbc;

import edu.cooper.ece366.euphoria.model.Authentication;
import edu.cooper.ece366.euphoria.model.AuthenticationBuilder;
import edu.cooper.ece366.euphoria.store.model.AuthenticationStore;

import java.sql.*;
import java.util.Collections;
import java.util.List;

public class AuthenticationStoreJdbc implements AuthenticationStore {

    private static final String GET_AUTHENTICATION_STATEMENT = "SELECT * FROM authentications WHERE (username, passwordHash) IN ((?, ?))";
    private static final String CREATE_AUTHENTICATION_STATEMENT = "INSERT INTO authentications (id, username, passwordHash, isUser)" +
            "VALUES (?, ?, ?, ?)";

    public AuthenticationStoreJdbc() {}

    @Override
    public Authentication getAuthentication(final String username, final String passwordHash) {
        try {
            Connection connection = DataSource.getConnection();

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
        try {
            Connection connection = DataSource.getConnection();

            PreparedStatement ps = connection.prepareStatement(CREATE_AUTHENTICATION_STATEMENT);
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
        }

        return null; //if not successful, return null
    }
}
