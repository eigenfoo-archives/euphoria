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
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public class CookieHandlers implements RouteProvider {
    private final ObjectMapper objectMapper;
    private final Config config;

    public CookieHandlers(final ObjectMapper objectMapper, final Config config) {
        this.objectMapper = objectMapper;
        this.config = config;
    }

    @Override
    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
        return Stream.of(
                Route.sync("GET", "/api/cookie/<cookieCheck>", this::getCookie),
                Route.sync("POST", "/api/cookie", this::createCookie)
        ).map(r -> r.withMiddleware(jsonMiddleware()));
    }

    @VisibleForTesting
    public List<Cookie> getCookie(final RequestContext rc) {
        Cookie cookie = null;

        try {
            String cookieCheck = rc.pathArgs().get("cookieCheck");
            Connection conn = DriverManager.getConnection(
                    config.getString("mysql.jdbc"),
                    config.getString("mysql.user"),
                    config.getString("mysql.password"));
            String sqlQuery = "SELECT * FROM cookies WHERE (cookie) IN ((?))";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setString(1, cookieCheck);
            ResultSet rs = ps.executeQuery();

            if (rs.first()) {
                cookie = new CookieBuilder()
                        .id(rs.getInt("id"))
                        .isUser(rs.getBoolean("isUser"))
                        .cookie(rs.getString("cookie"))
                        .build();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return Collections.singletonList(cookie);
    }

    @VisibleForTesting
    public List<Cookie> createCookie(final RequestContext rc) {
        Cookie cookie = null;
        try {
            Map jsonMap = objectMapper.readValue(rc.request().payload().get().toByteArray(), Map.class);
            String username = jsonMap.get("username").toString();
            String passwordHash = jsonMap.get("passwordHash").toString();
            Integer id;
            Boolean isUser;

            Connection conn = DriverManager.getConnection(
                    config.getString("mysql.jdbc"),
                    config.getString("mysql.user"),
                    config.getString("mysql.password"));
            String sqlQuery = "SELECT * FROM authentications WHERE (username, passwordHash) IN ((?, ?))";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            ResultSet rs = ps.executeQuery();

            if (rs.first()) {
                id = rs.getInt("id");
                isUser = rs.getBoolean("isUser");
                if (rs != null) {
                    String cookieNew = UUID.randomUUID().toString();
                    String sqlQueryIns = "INSERT INTO cookies (id, isUser, cookie) VALUES (?, ?, ?)";
                    PreparedStatement psIns = conn.prepareStatement(sqlQueryIns);
                    psIns.setInt(1, id); // Either userId or companyId
                    psIns.setBoolean(2, isUser);
                    psIns.setString(3, cookieNew);
                    psIns.executeUpdate();
                    // Send back to front-end
                    cookie = new CookieBuilder()
                            .cookie(cookieNew)
                            .build();
                }
            }
        } catch (SQLException | IOException ex) {
            System.out.println(ex);
        }

        return Collections.singletonList(cookie);
    }


    private <T> Middleware<AsyncHandler<T>, AsyncHandler<Response<ByteString>>> jsonMiddleware() {
        return JsonSerializerMiddlewares.<T>jsonSerialize(objectMapper.writer())
                .and(Middlewares::httpPayloadSemantics)
                .and(responseAsyncHandler -> requestContext ->
                        responseAsyncHandler.invoke(requestContext)
                                .thenApply(response -> response.withHeader("Access-Control-Allow-Origin", "*")));
    }
}
