package edu.cooper.ece366.euphoria;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.route.*;
import okio.ByteString;

import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class AuthenticationHandlers implements RouteProvider {
    private static final String dbUrl = "jdbc:mysql://localhost:3306/euphoria";
    private static final String dbUsername = "euphoria";
    private static final String dbPassword = "euphoria";
    private final ObjectMapper objectMapper;

    public AuthenticationHandlers(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
        return Stream.of(
                Route.sync("GET", "/authentication/<username>/<passwordHash>", this::getAuthentication),
                Route.sync("POST",
                        "/authentication/<username>/<passwordHash>/<isUser>",
                        this::createAuthentication)
        ).map(r -> r.withMiddleware(jsonMiddleware()));
    }

    @VisibleForTesting
    public List<Authentication> getAuthentication(final RequestContext rc) {
        Authentication authentication = null;

        try {
            String username = rc.pathArgs().get("username");
            String passwordHash = rc.pathArgs().get("passwordHash");

            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            String sqlQuery = "SELECT * FROM authentications WHERE (username, passwordHash) IN ((?, ?))";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {  //FIXME Only read the first result. There should only be one, after all...
                authentication = new AuthenticationBuilder()
                        .username(rs.getString("username"))
                        .passwordHash(rs.getString("passwordHash"))
                        .isUser(rs.getBoolean("isUser"))
                        .build();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return Collections.singletonList(authentication);
    }

    @VisibleForTesting
    public List<Authentication> createAuthentication(final RequestContext rc) {
        try {
            String username = rc.pathArgs().get("username");
            String passwordHash = rc.pathArgs().get("passwordHash");
            Boolean isUser = Boolean.valueOf(rc.pathArgs().get("isUser"));

            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            String sqlQuery = "INSERT INTO authentications (username, passwordHash, isUser)" +
                    "VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            ps.setBoolean(3, isUser);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return Collections.emptyList();
    }


    private <T> Middleware<AsyncHandler<T>, AsyncHandler<Response<ByteString>>> jsonMiddleware() {
        return JsonSerializerMiddlewares.<T>jsonSerialize(objectMapper.writer())
                .and(Middlewares::httpPayloadSemantics)
                .and(responseAsyncHandler -> requestContext ->
                        responseAsyncHandler.invoke(requestContext)
                                .thenApply(response -> response.withHeader("Access-Control-Allow-Origin", "*")));
    }
}
