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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class PostingHandlers implements RouteProvider {
    private final ObjectMapper objectMapper;
    private final Config config;

    public PostingHandlers(final ObjectMapper objectMapper, final Config config) {
        this.objectMapper = objectMapper;
        this.config = config;
    }

    @Override
    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
        return Stream.of(
                Route.sync("GET", "/api/posting/<postingId>", this::getPosting),
                Route.sync("GET", "/api/posting/all", this::getAllPostings),
                Route.sync("GET", "/api/posting/random", this::getRandomPostings),
                // FIXME this http request should use query parameters instead of path arguments
                Route.sync("GET", "/api/posting/<location>/<industry>/<skillLevel>", this::searchPostings),
                Route.sync("POST", "/api/posting/", this::createPosting),
                Route.sync("PUT", "/api/posting/", this::editPosting),
                Route.sync("DELETE", "/api/posting/<postingId>", this::deletePosting)
        ).map(r -> r.withMiddleware(jsonMiddleware()));
    }

    @VisibleForTesting
    public List<Posting> getPosting(final RequestContext rc) {
        Posting posting = null;

        try {
            Integer postingId = Integer.valueOf(rc.pathArgs().get("postingId"));
            Connection conn = DriverManager.getConnection(
                    config.getString("mysql.jdbc"),
                    config.getString("mysql.user"),
                    config.getString("mysql.password"));
            String sqlQuery = "SELECT * FROM postings WHERE postingId = ?";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setInt(1, postingId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {  //FIXME Only read the first result. There should only be one, after all...
                posting = new PostingBuilder()
                        .postingId(rs.getInt("postingId"))
                        .companyId(rs.getInt("companyId"))
                        .jobTitle(rs.getString("jobTitle"))
                        .description(rs.getString("description"))
                        .location(Location.valueOf(rs.getString("location")))
                        .industry(Industry.valueOf(rs.getString("industry")))
                        .skillLevel(SkillLevel.valueOf(rs.getString("skillLevel")))
                        .dateCreated(rs.getString("dateCreated"))
                        .build();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return Collections.singletonList(posting);
    }

    @VisibleForTesting
    public List<Posting> searchPostings(final RequestContext rc) {
        ArrayList<Posting> postingList = new ArrayList<Posting>();
        String location = "%";
        String industry = "%";
        String skillLevel = "%";

        try {
            location = Location.valueOf(rc.pathArgs().get("location")).toString();
        } catch (Exception ex) {
            System.out.println(ex);
        }

        try {
            industry = Industry.valueOf(rc.pathArgs().get("industry")).toString();
        } catch (Exception ex) {
            System.out.println(ex);
        }

        try {
            skillLevel = SkillLevel.valueOf(rc.pathArgs().get("skillLevel")).toString();
        } catch (Exception ex) {
            System.out.println(ex);
        }

        try {
            Connection conn = DriverManager.getConnection(
                    config.getString("mysql.jdbc"),
                    config.getString("mysql.user"),
                    config.getString("mysql.password"));
            String sqlQuery = "SELECT * FROM postings WHERE location LIKE ? " +
                    "AND industry LIKE ? AND skillLevel LIKE ?";
            System.out.println(sqlQuery);
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setString(1, location);
            ps.setString(2, industry);
            ps.setString(3, skillLevel);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Posting posting = new PostingBuilder()
                        .postingId(rs.getInt("postingId"))
                        .companyId(rs.getInt("companyId"))
                        .jobTitle(rs.getString("jobTitle"))
                        .description(rs.getString("description"))
                        .location(Location.valueOf(rs.getString("location")))
                        .industry(Industry.valueOf(rs.getString("industry")))
                        .skillLevel(SkillLevel.valueOf(rs.getString("skillLevel")))
                        .dateCreated(rs.getString("dateCreated"))
                        .build();

                postingList.add(posting);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return postingList;
    }

    @VisibleForTesting
    public List<Posting> getAllPostings(final RequestContext rc) {
        ArrayList<Posting> postingList = new ArrayList<Posting>();

        try {
            Connection conn = DriverManager.getConnection(
                    config.getString("mysql.jdbc"),
                    config.getString("mysql.user"),
                    config.getString("mysql.password"));
            String sqlQuery = "SELECT * FROM postings";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Posting posting = new PostingBuilder()
                        .postingId(rs.getInt("postingId"))
                        .companyId(rs.getInt("companyId"))
                        .jobTitle(rs.getString("jobTitle"))
                        .description(rs.getString("description"))
                        .location(Location.valueOf(rs.getString("location")))
                        .industry(Industry.valueOf(rs.getString("industry")))
                        .skillLevel(SkillLevel.valueOf(rs.getString("skillLevel")))
                        .dateCreated(rs.getString("dateCreated"))
                        .build();

                postingList.add(posting);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return postingList;
    }

    @VisibleForTesting
    public List<Posting> getRandomPostings(final RequestContext rc) {
        ArrayList<Posting> postingList = new ArrayList<Posting>();

        try {
            Connection conn = DriverManager.getConnection(
                    config.getString("mysql.jdbc"),
                    config.getString("mysql.user"),
                    config.getString("mysql.password"));
            String sqlQuery = "SELECT * FROM postings ORDER BY RAND() LIMIT 10";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Posting posting = new PostingBuilder()
                        .postingId(rs.getInt("postingId"))
                        .companyId(rs.getInt("companyId"))
                        .jobTitle(rs.getString("jobTitle"))
                        .description(rs.getString("description"))
                        .location(Location.valueOf(rs.getString("location")))
                        .industry(Industry.valueOf(rs.getString("industry")))
                        .skillLevel(SkillLevel.valueOf(rs.getString("skillLevel")))
                        .dateCreated(rs.getString("dateCreated"))
                        .build();

                postingList.add(posting);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return postingList;
    }

    @VisibleForTesting
    public List<Posting> createPosting(final RequestContext rc) {
        try {
            byte[] requestBytes = rc.request().payload().get().toByteArray();
            Map jsonMap = objectMapper.readValue(requestBytes, Map.class);
            Integer companyId = Integer.parseInt(jsonMap.get("companyId").toString());
            String jobTitle = jsonMap.get("jobTitle").toString();
            String description = jsonMap.get("description").toString();
            Location location = Location.valueOf(jsonMap.get("location").toString());
            Industry industry = Industry.valueOf(jsonMap.get("industry").toString());
            SkillLevel skillLevel = SkillLevel.valueOf(jsonMap.get("skillLevel").toString());

            Connection conn = DriverManager.getConnection(
                    config.getString("mysql.jdbc"),
                    config.getString("mysql.user"),
                    config.getString("mysql.password"));
            String sqlQuery = "INSERT INTO postings (companyId, jobTitle, " +
                    "description, location, industry, skillLevel) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setInt(1, companyId);
            ps.setString(2, jobTitle);
            ps.setString(3, description);
            ps.setString(4, location.toString());
            ps.setString(5, industry.toString());
            ps.setString(6, skillLevel.toString());
            //timestamped automatically in UTC by mysql database
            ps.executeUpdate();
        } catch (SQLException | IOException ex) {
            System.out.println(ex);
        }

        return Collections.emptyList();
    }

    @VisibleForTesting
    public List<Posting> editPosting(final RequestContext rc) {
        try {
            Map jsonMap = objectMapper.readValue(rc.request().payload().get().toByteArray(), Map.class);
            Integer postingId = Integer.parseInt(jsonMap.get("postingId").toString());
            String jobTitle = jsonMap.get("jobTitle").toString();
            String description = jsonMap.get("description").toString();
            Location location = Location.valueOf(jsonMap.get("location").toString());
            Industry industry = Industry.valueOf(jsonMap.get("industry").toString());
            SkillLevel skillLevel = SkillLevel.valueOf(jsonMap.get("skillLevel").toString());

            Connection conn = DriverManager.getConnection(
                    config.getString("mysql.jdbc"),
                    config.getString("mysql.user"),
                    config.getString("mysql.password"));
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
        } catch (SQLException | IOException ex) {
            System.out.println(ex);
        }

        return Collections.emptyList();
    }

    @VisibleForTesting
    public List<Posting> deletePosting(final RequestContext rc) {
        try {
            Integer postingId = Integer.valueOf(rc.pathArgs().get("postingId"));

            Connection conn = DriverManager.getConnection(
                    config.getString("mysql.jdbc"),
                    config.getString("mysql.user"),
                    config.getString("mysql.password"));
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
                .and(Middlewares::httpPayloadSemantics)
                .and(responseAsyncHandler -> requestContext ->
                        responseAsyncHandler.invoke(requestContext)
                                .thenApply(response -> response.withHeader("Access-Control-Allow-Origin", "*")));
    }
}
