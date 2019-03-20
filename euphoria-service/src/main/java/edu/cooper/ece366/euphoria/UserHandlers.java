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

public class UserHandlers implements RouteProvider {
    private static final String dbUrl = "jdbc:mysql://localhost:3306/euphoria";
    private static final String dbUsername = "euphoria";
    private static final String dbPassword = "euphoria";
    private final ObjectMapper objectMapper;

    public UserHandlers(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
        return Stream.of(
                Route.sync("GET", "/user/<userId>", this::getUser),
                Route.sync("POST",
                        "/user/<name>/<email>/<phoneNumber>/<educationLevel>/<description>",
                        this::createUser)
        ).map(r -> r.withMiddleware(jsonMiddleware()));
    }

    private List<User> getUser(final RequestContext rc) {
        User user = null;

        try {
            Integer userId = Integer.valueOf(rc.pathArgs().get("userId"));
            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
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

    private List<User> createUser(final RequestContext rc) {
        try {
            String name = rc.pathArgs().get("name");
            String email = rc.pathArgs().get("email");
            String phoneNumber = rc.pathArgs().get("phoneNumber");
            EducationLevel educationLevel = EducationLevel.valueOf(rc.pathArgs().get("educationLevel"));
            String description = rc.pathArgs().get("description");

            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            String sqlQuery = "INSERT INTO users (name, email, phoneNumber, " +
                    "educationLevel, description, dateCreated) " +
                    "(?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phoneNumber);
            ps.setString(4, educationLevel.toString());
            ps.setString(5, description);
            Date date = new Date();
            ps.setObject(6, date.toInstant().atZone(ZoneId.of("UTC")).toLocalDate());
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
