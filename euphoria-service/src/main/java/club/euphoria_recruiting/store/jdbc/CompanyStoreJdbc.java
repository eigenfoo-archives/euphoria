package club.euphoria_recruiting.store.jdbc;

import club.euphoria_recruiting.model.Company;
import club.euphoria_recruiting.model.CompanyBuilder;
import club.euphoria_recruiting.store.model.CompanyStore;
import org.apache.commons.dbutils.DbUtils;

import java.sql.*;

public class CompanyStoreJdbc implements CompanyStore {

    private static final String GET_COMPANY_STATEMENT = "SELECT * FROM companies WHERE companyId = ?";
    private static final String CREATE_COMPANY_STATEMENT = "INSERT INTO companies (name, website, description) VALUES (?, ?, ?)";

    private final DataSource dataSource;

    public CompanyStoreJdbc(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Company getCompany(final String companyId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn= dataSource.getConnection();
            ps = conn.prepareStatement(GET_COMPANY_STATEMENT);
            ps.setInt(1, Integer.parseInt(companyId));
            rs = ps.executeQuery();

            if (rs.first()) {
                return new CompanyBuilder()
                        .companyId(rs.getInt("companyId"))
                        .name(rs.getString("name"))
                        .website(rs.getString("website"))
                        .description(rs.getString("description"))
                        .build();
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("error fetching company", e);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }
    }


    @Override
    public Company createCompany(final String name, final String website, final String description) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn= dataSource.getConnection();
            ps = conn.prepareStatement(CREATE_COMPANY_STATEMENT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, website);
            ps.setString(3, description);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Creating new company failed, no rows affected.");
            }
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.first()) {
                return new CompanyBuilder()
                        .companyId(generatedKeys.getInt(1))
                        //only want to send the Id, but don't know how to return just an integer alone without the builder, so putting placeholder values below
                        .name("NA")
                        .website("NA")
                        .description("NA")
                        .build();
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("error creating company", e);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
        }
    }
}
