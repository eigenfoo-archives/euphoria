package edu.cooper.ece366.euphoria;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.route.*;
import okio.ByteString;

import java.sql.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class PostingHandles {
    private final ObjectMapper objectMapper;
    private static final String dbUrl = "jdbg:mysql://localhost:3306/euphoria";
    private static final String dbUsername = "euphoria";
    private static final String dbPassword = "euphoria";

    public PostingHandles(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
        return Stream.of(
                Route.sync("GET", "/posting/<postingId>", rc -> getPosting(rc.pathArgs().get("postingId"))),
                Route.sync("POST", "/posting", this::createPosting)
        ).map(r -> r.withMiddleware(jsonMiddleware()));
    }

    private List<Posting> getPosting(String postingId) {
        Connection conn = null;
        Posting posting = null;

        try {
            DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        } catch (SQLException ex) {
            System.out.println("SQL exception on connection: " + ex.getMessage());
        }

        try {
            String sqlQuery = "SELECT * from postings WHERE postingId == ?";
            PreparedStatement stmt = conn.prepareStatement(sqlQuery);
            stmt.setInt(1, Integer.valueOf(postingId));
            ResultSet resultSet = stmt.executeQuery(sqlQuery);

            posting = new PostingBuilder()
                    .postingId(resultSet.getInt("postingId"))
                    .jobTitle(resultSet.getString("jobTitle"))
                    .description(resultSet.getString("description"))
                    .location(Location.valueOf(resultSet.getString("description")))
                    .skillLevel(SkillLevel.valueOf(resultSet.getString("skillLevel")))
                    .industry(Industry.valueOf(resultSet.getString("industry")))
                    .build();
        } catch (SQLException ex) {
            System.out.println("SQL exception on query: " + ex.getMessage());
        }

        return Collections.singletonList(posting);
    }

    private List<Posting> createPosting(final RequestContext requestContext) {
        Integer postingId = null;
        String jobTitle = null;
        String description = null;
        Location location = null;
        SkillLevel skillLevel = null;
        Industry industry = null;
        Date dateCreated = null;

        try {
            postingId = Integer.valueOf(requestContext.pathArgs().get("postingId"));
            jobTitle = requestContext.pathArgs().get("jobTitle");
            description = requestContext.pathArgs().get("description");
            location = Location.valueOf(requestContext.pathArgs().get("location"));
            skillLevel = SkillLevel.valueOf(requestContext.pathArgs().get("skillLevel"));
            industry = Industry.valueOf(requestContext.pathArgs().get("industry"));
            dateCreated = new Date(requestContext.pathArgs().get("dateCreated"));
        } catch (Exception ex) {
            System.out.println("Malformed POST request: " + ex.getMessage());
        }

        Connection conn = null;
        Posting posting = null;

        try {
            DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        } catch (SQLException ex) {
            System.out.println("SQL exception on connection: " + ex.getMessage());
        }

        try {
            String statement = "INSERT INTO postings (companyId, jobTitle, " +
                               "description, location, industry, skillLevel, " +
                               "dateCreated) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement query = conn.prepareStatement(statement);
            query.setInt(1, postingId);
            query.setString(2, jobTitle);
            query.setString(3, description);
            query.setString(4, location.toString());
            query.setString(5, skillLevel.toString());
            query.setString(6, industry.toString());
            query.setDate(7, (java.sql.Date) dateCreated);
            query.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("SQL exception on query: " + ex.getMessage());
        }

        return Collections.emptyList();
    }

    private <T> Middleware<AsyncHandler<T>, AsyncHandler<Response<ByteString>>> jsonMiddleware() {
        return JsonSerializerMiddlewares.<T>jsonSerialize(objectMapper.writer())
                .and(Middlewares::httpPayloadSemantics);
    }
}
