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
import java.util.stream.Stream;

public class UserHandlers implements RouteProvider {
    private final ObjectMapper objectMapper;
    private final Config config;

    public UserHandlers(final ObjectMapper objectMapper, final Config config) {
        this.objectMapper = objectMapper;
        this.config = config;
    }

    @Override
    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
        return Stream.of(
                Route.sync("GET", "/api/user/<userId>", this::getUser),
                Route.sync("POST", "/api/user", this::createUser)
        ).map(r -> r.withMiddleware(jsonMiddleware()));
    }

    @VisibleForTesting
    public List<User> getUser(final RequestContext rc) {
        User user = null;

        try {
            Integer userId = Integer.valueOf(rc.pathArgs().get("userId"));
            Connection conn = DriverManager.getConnection(
                    config.getString("mysql.jdbc"),
                    config.getString("mysql.user"),
                    config.getString("mysql.password"));
            String sqlQuery = "SELECT * FROM users WHERE userId = ?";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {  //FIXME Only read the first result. There should only be one, after all...
                user = new UserBuilder()
                        .userId(rs.getInt("userId"))
                        .name(rs.getString("name"))
                        .email(rs.getString("email"))
                        .phoneNumber(rs.getString("phoneNumber"))
                        .educationLevel(EducationLevel.valueOf(rs.getString("educationLevel")))
                        .description(rs.getString("description"))
                        .build();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return Collections.singletonList(user);
    }

    @VisibleForTesting
    public List<User> createUser(final RequestContext rc) {
        User user = null;
        try {
            byte[] requestBytes = rc.request().payload().get().toByteArray();
            Map jsonMap = objectMapper.readValue(requestBytes, Map.class);
            String name = jsonMap.get("name").toString();
            String email = jsonMap.get("email").toString();
            String phoneNumber = jsonMap.get("phoneNumber").toString();
            EducationLevel educationLevel = EducationLevel.valueOf(jsonMap.get("educationLevel").toString());
            String description = jsonMap.get("description").toString();

            Connection conn = DriverManager.getConnection(
                    config.getString("mysql.jdbc"),
                    config.getString("mysql.user"),
                    config.getString("mysql.password"));
            String sqlQuery = "INSERT INTO users (name, email, phoneNumber, " +
                    "educationLevel, description) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phoneNumber);
            ps.setString(4, educationLevel.toString());
            ps.setString(5, description);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Creating new user failed, no rows affected.");
            }
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    System.out.println("the generated key was" + generatedKeys.getLong(1));
                    user = new UserBuilder()
                            .userId(generatedKeys.getInt(1))
                            //only want to send the Id, but don't know how to return just an integer alone without the builder, so putting placeholder values below
                            .name("namefield")
                            .email("emailfield")
                            .phoneNumber("phoneNumfield")
                            .educationLevel(EducationLevel.valueOf("PHD"))
                            .description("descriptonfield")
                            .build();
                } else {
                    throw new SQLException("Creating new user failed, no ID obtained.");
                }
            }
        } catch (SQLException | IOException ex) {
            System.out.println(ex);
        }

        return Collections.singletonList(user);
    }

    private <T> Middleware<AsyncHandler<T>, AsyncHandler<Response<ByteString>>> jsonMiddleware() {
        return JsonSerializerMiddlewares.<T>jsonSerialize(objectMapper.writer())
                .and(Middlewares::httpPayloadSemantics)
                .and(responseAsyncHandler -> requestContext ->
                        responseAsyncHandler.invoke(requestContext)
                                .thenApply(response -> response.withHeader("Access-Control-Allow-Origin", "*")));
    }
}
