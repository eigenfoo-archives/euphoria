package edu.cooper.ece366.euphoria;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.route.*;
import okio.ByteString;

import java.sql.*;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class CompanyHandlers implements RouteProvider {
    private static final String dbUrl = "jdbc:mysql://localhost:3306/euphoria";
    private static final String dbUsername = "euphoria";
    private static final String dbPassword = "euphoria";
    private final ObjectMapper objectMapper;

    public CompanyHandlers(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
        return Stream.of(
                Route.sync("GET", "/company/<companyId>", this::getCompany),
                Route.sync("POST", "/company/<name>/<website>/<description>",
                        this::createCompany)
        ).map(r -> r.withMiddleware(jsonMiddleware()));
    }

    private List<Company> getCompany(final RequestContext rc) {
        Company company = null;

        try {
            Integer companyId = Integer.valueOf(rc.pathArgs().get("companyId"));
            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
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

    private List<Company> createCompany(final RequestContext rc) {
        try {
            String name = rc.pathArgs().get("name");
            String website = rc.pathArgs().get("website");
            String description = rc.pathArgs().get("description");

            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            String sqlQuery = "INSERT INTO companies (name, website, description, " +
                    "dateCreated) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setString(1, name);
            ps.setString(2, website);
            ps.setString(3, description);
            Date date = new Date();
            ps.setObject(4, date.toInstant().atZone(ZoneId.of("UTC")).toLocalDate());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return Collections.emptyList();
    }

    private <T> Middleware<AsyncHandler<T>, AsyncHandler<Response<ByteString>>> jsonMiddleware() {
        return JsonSerializerMiddlewares.<T>jsonSerialize(objectMapper.writer())
                .and(Middlewares::httpPayloadSemantics);
    }
}
