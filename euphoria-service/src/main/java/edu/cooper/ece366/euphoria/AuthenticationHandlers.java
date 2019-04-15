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
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class AuthenticationHandlers implements RouteProvider {
    private ObjectMapper objectMapper;
    private Config config;

    public AuthenticationHandlers(ObjectMapper objectMapper, Config config) {
        this.objectMapper = objectMapper;
        this.config = config;
    }

    @Override
    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
        return Stream.of(
                Route.sync("GET", "/api/authentication/<username>/<passwordHash>", this::getAuthentication),
                Route.sync("POST", "/api/authentication", this::createAuthentication)
        ).map(r -> r.withMiddleware(jsonMiddleware()));
    }

    @VisibleForTesting
    public List<Authentication> getAuthentication(RequestContext rc) {
        Authentication authentication = null;

        try {
            String username = rc.pathArgs().get("username");
            String passwordHash = rc.pathArgs().get("passwordHash");

            Connection conn = DriverManager.getConnection(
                    config.getString("mysql.jdbc"),
                    config.getString("mysql.user"),
                    config.getString("mysql.password"));
            String sqlQuery = "SELECT * FROM authentications WHERE (username, passwordHash) IN ((?, ?))";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {  //FIXME Only read the first result. There should only be one, after all...
                authentication = new AuthenticationBuilder()
                        .id(rs.getInt("id"))
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
    public List<Authentication> createAuthentication(RequestContext rc) {
        try {
            byte[] requestBytes = rc.request().payload().get().toByteArray();
            Authentication authentication = objectMapper.readValue(requestBytes, Authentication.class);
            Integer id = authentication.id();
            String username = authentication.username();
            String passwordHash = authentication.passwordHash();
            Boolean isUser = authentication.isUser();

            Connection conn = DriverManager.getConnection(
                    config.getString("mysql.jdbc"),
                    config.getString("mysql.user"),
                    config.getString("mysql.password"));
            String sqlQuery = "INSERT INTO authentications (id, username, passwordHash, isUser)" +
                    "VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setInt(1, id);
            ps.setString(2, username);
            ps.setString(3, passwordHash);
            ps.setBoolean(4, isUser);
            ps.executeUpdate();
        } catch (SQLException | IOException ex) {
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
