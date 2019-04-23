package edu.cooper.ece366.euphoria.store.jdbc;

import edu.cooper.ece366.euphoria.model.User;
import edu.cooper.ece366.euphoria.model.UserBuilder;
import edu.cooper.ece366.euphoria.store.model.UserStore;
import edu.cooper.ece366.euphoria.utils.EducationLevel;
import org.apache.commons.dbutils.DbUtils;

import java.sql.*;

public class UserStoreJdbc implements UserStore {

    private static final String GET_USER_STATEMENT = "SELECT * FROM users WHERE userId = ?";
    private static final String CREATE_USER_STATEMENT = "INSERT INTO users (name, email, phoneNumber, educationLevel, description)" +
            " VALUES (?, ?, ?, ?, ?)";

    private final DataSource dataSource;

    public UserStoreJdbc(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public User getUser(final String userId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DataSource.getConnection();
            ps = conn.prepareStatement(GET_USER_STATEMENT);
            ps.setInt(1, Integer.parseInt(userId));
            rs = ps.executeQuery();

            if (rs.first()) {
                return new UserBuilder()
                        .userId(rs.getInt("userId"))
                        .name(rs.getString("name"))
                        .email(rs.getString("email"))
                        .phoneNumber(rs.getString("phoneNumber"))
                        .educationLevel(EducationLevel.valueOf(rs.getString("educationLevel")))
                        .description(rs.getString("description"))
                        .build();
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("error fetching user", e);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
    }


    @Override
    public User createUser(final String name, final String email, final String phoneNumber, final EducationLevel educationLevel, final String description) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DataSource.getConnection();
            ps = conn.prepareStatement(CREATE_USER_STATEMENT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phoneNumber);
            ps.setString(4, educationLevel.toString());
            ps.setString(5, description);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Creating new user failed, no rows affected.");
            }
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.first()) {
                return new UserBuilder()
                        .userId(generatedKeys.getInt(1))
                        //only want to send the Id, but don't know how to return just an integer alone without the builder, so putting placeholder values below
                        .name("NA")
                        .email("NA")
                        .phoneNumber("NA")
                        .educationLevel(EducationLevel.NOHIGHSCHOOL)
                        .description("NA")
                        .build();
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("error creating user", e);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
        }
    }
}