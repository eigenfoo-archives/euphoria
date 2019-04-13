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

public class ApplicationHandlers implements RouteProvider {
    private final ObjectMapper objectMapper;
    private final Config config;

    public ApplicationHandlers(final ObjectMapper objectMapper, final Config config) {
        this.objectMapper = objectMapper;
        this.config = config;
    }

    @Override
    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
        return Stream.of(
                Route.sync("GET", "/api/application/<applicationId>", this::getApplication),
                Route.sync("GET", "/api/application/posting/<postingId>", this::getApplicationsForPosting),
                Route.sync("POST", "/api/application", this::createApplication)
        ).map(r -> r.withMiddleware(jsonMiddleware()));
    }

    @VisibleForTesting
    public List<Application> getApplication(final RequestContext rc) {
        Application application = null;

        try {
            Integer applicationId = Integer.valueOf(rc.pathArgs().get("applicationId"));
            Connection conn = DriverManager.getConnection(
                    config.getString("mysql.jdbc"),
                    config.getString("mysql.user"),
                    config.getString("mysql.password"));
            String sqlQuery = "SELECT * FROM applications WHERE applicationId = ?";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setInt(1, applicationId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {  //FIXME Only read the first result. There should only be one, after all...
                application = new ApplicationBuilder()
                        .applicationId(rs.getInt("applicationId"))
                        .postingId(rs.getInt("postingId"))
                        .userId(rs.getInt("userId"))
                        .resume(rs.getBytes("resume"))            //returns BASE64
                        .coverLetter(rs.getBytes("coverLetter"))   //returns BASE64
                        .dateCreated(rs.getString("dateCreated"))
                        .build();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return Collections.singletonList(application);
    }

    @VisibleForTesting
    public List<Application> getApplicationsForPosting(final RequestContext rc) {
        ArrayList<Application> applicationList = new ArrayList<Application>();

        try {
            Integer postingId = Integer.valueOf(rc.pathArgs().get("postingId"));
            Connection conn = DriverManager.getConnection(
                    config.getString("mysql.jdbc"),
                    config.getString("mysql.user"),
                    config.getString("mysql.password"));
            String sqlQuery = "SELECT * FROM applications WHERE postingId = ?";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setInt(1, postingId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Application application = new ApplicationBuilder()
                        .applicationId(rs.getInt("applicationId"))
                        .postingId(rs.getInt("postingId"))
                        .userId(rs.getInt("userId"))
                        .resume(rs.getBytes("resume"))            //returns BASE64
                        .coverLetter(rs.getBytes("coverLetter"))   //returns BASE64
                        .dateCreated(rs.getString("dateCreated"))
                        .build();

                applicationList.add(application);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return applicationList;
    }

    @VisibleForTesting
    public List<Application> createApplication(final RequestContext rc) {
        try {
            Map jsonMap = objectMapper.readValue(rc.request().payload().get().toByteArray(), Map.class);
            Integer postingId = Integer.valueOf(jsonMap.get("postingId").toString());
            Integer userId = Integer.valueOf(jsonMap.get("userId").toString());
            byte[] resume = jsonMap.get("resume").toString().getBytes();
            byte[] coverLetter = jsonMap.get("coverLetter").toString().getBytes();

            Connection conn = DriverManager.getConnection(
                    config.getString("mysql.jdbc"),
                    config.getString("mysql.user"),
                    config.getString("mysql.password"));
            String sqlQuery = "INSERT INTO applications (postingId, userId, " +
                    "resume, coverLetter) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setInt(1, postingId);
            ps.setInt(2, userId);
            ps.setBytes(3, resume);
            ps.setBytes(4, coverLetter);
            //timestamped automatically in UTC by mysql database
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