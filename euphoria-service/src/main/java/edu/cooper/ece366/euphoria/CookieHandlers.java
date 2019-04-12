package edu.cooper.ece366.euphoria;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.route.*;
import com.typesafe.config.Config;
import okio.ByteString;

import java.sql.*;
import java.util.Collections;
import java.util.List;
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
                Route.sync("GET", "/cookie/<cookieCheck>", this::getCookie),
                Route.sync("POST",
                        "/cookie/<username>/<passwordHash>",
                        this::createCookie)
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
            String sqlQuery = "SELECT * FROM coookies WHERE (cookie) IN ((?))";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setString(1, cookieCheck);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {  //FIXME Only read the first result. There should only be one, after all...
                cookie = new CookieBuilder()
                        //.Id(rs.getInt("Id"))
                        //.isUser(rs.getBoolean("isUser"))
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
            String username = rc.pathArgs().get("username");
            String passwordHash = rc.pathArgs().get("passwordHash");
            Integer Id;
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
            if (rs.next()) {  //FIXME Only read the first result. There should only be one, after all...
                Id = rs.getInt("Id");
                isUser = rs.getBoolean("isUser");
                if (rs != null) {
                    String cookieNew = "abc" + username + "1234" + passwordHash.substring(0, 3); //TODO: make a better cookie hashing function
                    String sqlQueryIns = "INSERT INTO cookies (Id, isUser, cookie) VALUES (?, ?, ?)";
                    PreparedStatement psIns = conn.prepareStatement(sqlQueryIns);
                    psIns.setInt(1, Id);
                    psIns.setBoolean(2, isUser);
                    psIns.setString(3, cookieNew);
                    psIns.executeUpdate();
                    //send back to front-end
                    cookie = new CookieBuilder()
                            .cookie(cookieNew)
                            .build();
                    }
                }
        } catch (SQLException ex) {
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
