package edu.cooper.ece366.euphoria;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.route.*;
import com.typesafe.config.Config;
import okio.ByteString;

import java.io.IOException;
import java.sql.*;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class CompanyHandlers implements RouteProvider {
    private final ObjectMapper objectMapper;
    private final Config config;

    public CompanyHandlers(final ObjectMapper objectMapper, final Config config) {
        this.objectMapper = objectMapper;
        this.config = config;
    }

    @Override
    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
        return Stream.of(
                Route.sync("GET", "/api/company/<companyId>", this::getCompany),
                Route.sync("POST", "/api/company", this::createCompany)
        ).map(r -> r.withMiddleware(jsonMiddleware()));
    }

    @VisibleForTesting
    public List<Company> getCompany(final RequestContext rc) {
        Company company = null;

        try {
            Integer companyId = Integer.valueOf(rc.pathArgs().get("companyId"));
            Connection conn = DriverManager.getConnection(
                    config.getString("mysql.jdbc"),
                    config.getString("mysql.user"),
                    config.getString("mysql.password"));
            String sqlQuery = "SELECT * FROM companies WHERE companyId = ?";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setInt(1, companyId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {  //FIXME Only read the first result. There should only be one, after all...
                company = new CompanyBuilder()
                        .companyId(rs.getInt("companyId"))
                        .name(rs.getString("name"))
                        .website(rs.getString("website"))
                        .description(rs.getString("description"))
                        .build();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return Collections.singletonList(company);
    }

    @VisibleForTesting
    public List<Company> createCompany(final RequestContext rc) {
        Company company = null;
        try {
            company = objectMapper.readValue(rc.request().payload().get().toByteArray(), Company.class);
            String name = company.name();
            String website = company.website();
            String description = company.description();

            Connection conn = DriverManager.getConnection(
                    config.getString("mysql.jdbc"),
                    config.getString("mysql.user"),
                    config.getString("mysql.password"));
            String sqlQuery = "INSERT INTO companies (name, website, description) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, website);
            ps.setString(3, description);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Creating new company failed, no rows affected.");
            }
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    company = new CompanyBuilder()
                            .companyId(generatedKeys.getInt(1))
                            //only want to send the Id, but don't know how to return just an integer alone without the builder, so putting placeholder values below
                            .name("namefield")
                            .website("websitefield")
                            .description("descriptonfield")
                            .build();
                } else {
                    throw new SQLException("Creating new company failed, no ID obtained.");
                }
            }
        } catch (SQLException | IOException ex) {
            System.out.println(ex);
        }

        return Collections.singletonList(company);
    }

    private <T> Middleware<AsyncHandler<T>, AsyncHandler<Response<ByteString>>> jsonMiddleware() {
        return JsonSerializerMiddlewares.<T>jsonSerialize(objectMapper.writer())
                .and(Middlewares::httpPayloadSemantics)
                .and(responseAsyncHandler -> requestContext ->
                        responseAsyncHandler.invoke(requestContext)
                                .thenApply(response -> response.withHeader("Access-Control-Allow-Origin", "*")));
    }
}
