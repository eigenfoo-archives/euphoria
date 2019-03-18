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

public class PostingHandles implements RouteProvider {
    private static final String dbUrl = "jdbc:mysql://localhost:3306/euphoria";
    private static final String dbUsername = "euphoria";
    private static final String dbPassword = "euphoria";
    private final ObjectMapper objectMapper;

    public PostingHandles(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
        return Stream.of(
                Route.sync("GET", "/posting/<postingId>", this::getPosting),
                Route.sync("POST",
                        "/posting/<companyId>/<jobTitle>/<description>/<location>/<skillLevel>/<industry>",
                        this::createPosting)
        ).map(r -> r.withMiddleware(jsonMiddleware()));
    }

    private List<Posting> getPosting(final RequestContext rc) {
        Posting posting = null;

        try {
            Integer postingId = Integer.valueOf(rc.pathArgs().get("postingId"));
            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            String sqlQuery = "SELECT * FROM postings WHERE postingId = ?";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setInt(1, postingId);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {  //FIXME Only read the first result. There should only be one, after all...
                posting = new PostingBuilder()
                        .postingId(resultSet.getInt("postingId"))
                        .jobTitle(resultSet.getString("jobTitle"))
                        .description(resultSet.getString("description"))
                        .location(Location.valueOf(resultSet.getString("location")))
                        .skillLevel(SkillLevel.valueOf(resultSet.getString("skillLevel")))
                        .industry(Industry.valueOf(resultSet.getString("industry")))
                        .build();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return Collections.singletonList(posting);
    }

    private List<Posting> createPosting(final RequestContext rc) {
        try {
            Integer companyId = Integer.valueOf(rc.pathArgs().get("companyId"));
            String jobTitle = rc.pathArgs().get("jobTitle");
            String description = rc.pathArgs().get("description");
            Location location = Location.valueOf(rc.pathArgs().get("location"));
            SkillLevel skillLevel = SkillLevel.valueOf(rc.pathArgs().get("skillLevel"));
            Industry industry = Industry.valueOf(rc.pathArgs().get("industry"));

            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            String sqlQuery = "INSERT INTO postings (companyId, jobTitle, " +
                    "description, location, industry, skillLevel, " +
                    "dateCreated) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setInt(1, companyId);
            ps.setString(2, jobTitle);
            ps.setString(3, description);
            ps.setString(4, location.toString());
            ps.setString(5, skillLevel.toString());
            ps.setString(6, industry.toString());
            Date date = new Date();
            ps.setObject(7, date.toInstant().atZone(ZoneId.of("UTC")).toLocalDate());
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
