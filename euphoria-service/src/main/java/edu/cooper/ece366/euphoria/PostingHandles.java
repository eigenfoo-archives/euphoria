package edu.cooper.ece366.euphoria;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.route.*;
import okio.ByteString;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class PostingHandles {
    private final ObjectMapper objectMapper;

    public PostingHandles(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
        return Stream.of(
                Route.sync("GET", "/posting/<postingId>", rc -> getPosting(rc.pathArgs().get("postingId"))),
                Route.sync("POST", "/posting", this::createPosting)
        ).map(r -> r.withMiddleware(jsonMiddleware()));
    }

    private List<edu.cooper.ece366.euphoria.Posting> getPosting(String postingId) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/euphoria?" + "user=euphoria&password=euphoria");
        } catch (Exception ex) {
            System.out.println("Exception connecting to db: " + ex.getMessage());
        }

        if (conn != null) {
            try {
                //FIXME: we should use PreparedStatements, not Statements
                Statement statement = conn.createStatement();
                String foo = "SELECT * from postings WHERE postingId == " + postingId;
                statement.executeUpdate(foo);
            } catch (Exception ex) {
                System.out.println("Exception reading from db: " + ex.getMessage());
            }
        }

        return Collections.emptyList();
    }

    private List<edu.cooper.ece366.euphoria.Posting> createPosting(final RequestContext requestContext) {
        try {
            Integer postingId = Integer.valueOf(requestContext.pathArgs().get("postingId"));
            String jobTitle = requestContext.pathArgs().get("jobTitle");
            String description = requestContext.pathArgs().get("description");
            Location location = Location.valueOf(requestContext.pathArgs().get("location"));
            SkillLevel skillLevel = SkillLevel.valueOf(requestContext.pathArgs().get("skillLevel"));
            Industry industry = Industry.valueOf(requestContext.pathArgs().get("industry"));
        } catch (Exception ex) {
            System.out.println("Malformed POST request: " + ex.getMessage());
        }

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/euphoria?" + "user=euphoria&password=euphoria");
        } catch (Exception ex) {
            System.out.println("Exception connecting to db: " + ex.getMessage());
        }

        if (conn != null) {
            try {
                Statement statement = conn.createStatement();
                String foo = "INSERT INTO postings (postingId, jobTitle, description, location, industry, skillLevel, dateCreated) VALUES" + "";
                statement.executeUpdate(foo);
            } catch (Exception ex) {
                System.out.println("Exception writing to db: " + ex.getMessage());
            }
        }

        return Collections.emptyList();
    }

    private <T> Middleware<AsyncHandler<T>, AsyncHandler<Response<ByteString>>> jsonMiddleware() {
        return JsonSerializerMiddlewares.<T>jsonSerialize(objectMapper.writer())
                .and(Middlewares::httpPayloadSemantics);
    }
}