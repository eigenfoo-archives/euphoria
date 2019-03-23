package edu.cooper.ece366.euphoria;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.route.*;
import okio.ByteString;

import java.sql.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class PostingHandlers implements RouteProvider {
    private static final String dbUrl = "jdbc:mysql://localhost:3306/euphoria";
    private static final String dbUsername = "euphoria";
    private static final String dbPassword = "euphoria";
    private final ObjectMapper objectMapper;

    public PostingHandlers(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
        return Stream.of(
                Route.sync("GET", "/posting/<postingId>", this::getPosting),
                Route.sync("GET", "/posting/getAll", this::getAllPostings),
                Route.sync("POST",
                        "/posting/<companyId>/<jobTitle>/<description>/<location>/<industry>/<skillLevel>",
                        this::createPosting),
                Route.sync("PUT",
                        "/posting/<postingId>/<jobTitle>/<description>/<location>/<industry>/<skillLevel>",
                        this::editPosting),
                Route.sync("DELETE", "/posting/<postingId>", this::deletePosting)
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
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {  //FIXME Only read the first result. There should only be one, after all...
                posting = new PostingBuilder()
                        .postingId(rs.getInt("postingId"))
                        .jobTitle(rs.getString("jobTitle"))
                        .description(rs.getString("description"))
                        .location(Location.valueOf(rs.getString("location")))
                        .industry(Industry.valueOf(rs.getString("industry")))
                        .skillLevel(SkillLevel.valueOf(rs.getString("skillLevel")))
                        .build();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return Collections.singletonList(posting);
    }

    private List<Posting> getAllPostings(final RequestContext rc) {
        ArrayList<Posting> postingList = new ArrayList<Posting>();

        try {
            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            String sqlQuery = "SELECT * FROM postings";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Posting posting = new PostingBuilder()
                        .postingId(rs.getInt("postingId"))
                        .jobTitle(rs.getString("jobTitle"))
                        .description(rs.getString("description"))
                        .location(Location.valueOf(rs.getString("location")))
                        .industry(Industry.valueOf(rs.getString("industry")))
                        .skillLevel(SkillLevel.valueOf(rs.getString("skillLevel")))
                        .build();

                postingList.add(posting);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return postingList;
    }

    private List<Posting> createPosting(final RequestContext rc) {
        try {
            Integer companyId = Integer.valueOf(rc.pathArgs().get("companyId"));
            String jobTitle = rc.pathArgs().get("jobTitle");
            String description = rc.pathArgs().get("description");
            Location location = Location.valueOf(rc.pathArgs().get("location"));
            Industry industry = Industry.valueOf(rc.pathArgs().get("industry"));
            SkillLevel skillLevel = SkillLevel.valueOf(rc.pathArgs().get("skillLevel"));

            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            String sqlQuery = "INSERT INTO postings (companyId, jobTitle, " +
                    "description, location, industry, skillLevel, " +
                    "dateCreated) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setInt(1, companyId);
            ps.setString(2, jobTitle);
            ps.setString(3, description);
            ps.setString(4, location.toString());
            ps.setString(5, industry.toString());
            ps.setString(6, skillLevel.toString());
            Date date = new Date();
            ps.setObject(7, date.toInstant().atZone(ZoneId.of("UTC")).toLocalDate());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return Collections.emptyList();
    }

    private List<Posting> editPosting(final RequestContext rc) {
        try {
            Integer postingId = Integer.valueOf(rc.pathArgs().get("postingId"));
            String jobTitle = rc.pathArgs().get("jobTitle");
            String description = rc.pathArgs().get("description");
            Location location = Location.valueOf(rc.pathArgs().get("location"));
            Industry industry = Industry.valueOf(rc.pathArgs().get("industry"));
            SkillLevel skillLevel = SkillLevel.valueOf(rc.pathArgs().get("skillLevel"));

            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            String sqlQuery = "UPDATE postings SET jobTitle = ?, description = ?, " +
                    "location = ?, industry = ?, skillLevel = ? WHERE postingId = ?";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setString(1, jobTitle);
            ps.setString(2, description);
            ps.setString(3, location.toString());
            ps.setString(4, industry.toString());
            ps.setString(5, skillLevel.toString());
            ps.setInt(6, postingId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return Collections.emptyList();
    }

    private List<Posting> deletePosting(final RequestContext rc) {
        try {
            Integer postingId = Integer.valueOf(rc.pathArgs().get("postingId"));

            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            String sqlQuery = "DELETE FROM postings WHERE postingId = ?";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setInt(1, postingId);
            ps.executeUpdate();

            sqlQuery = "DELETE FROM applications WHERE postingId = ?";
            ps = conn.prepareStatement(sqlQuery);
            ps.setInt(1, postingId);
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
