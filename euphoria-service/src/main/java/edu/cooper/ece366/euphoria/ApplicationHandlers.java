package edu.cooper.ece366.euphoria;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.route.*;
import com.typesafe.config.Config;
import okio.ByteString;
import java.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.ArrayList;

public class ApplicationHandlers implements RouteProvider {
    private final ObjectMapper objectMapper;
    private final Config config;
    private final String FileStoragePath;

    public ApplicationHandlers(final ObjectMapper objectMapper, final Config config) {
        this.objectMapper = objectMapper;
        this.config = config;
        FileStoragePath = config.getString("FileStoragePath");
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

        Integer applicationId = Integer.valueOf(rc.pathArgs().get("applicationId"));

        File fileRes = new File(FileStoragePath + "resume" + "_" + applicationId);
        byte[] bufferRes = new byte[(int) fileRes.length()];
        File fileCov = new File(FileStoragePath + "cover" + "_" + applicationId);
        byte[] bufferCov = new byte[(int) fileCov.length()];
            try {
                FileInputStream input1 = new FileInputStream(fileRes);
                input1.read(bufferRes);
                input1.close();
                FileInputStream input2 = new FileInputStream(fileCov);
                input2.read(bufferCov);
                input2.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        try {
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
                        .resume(bufferRes)        //returns BASE64
                        .coverLetter(bufferCov)   //returns BASE64
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
                Integer applicationId = rs.getInt("applicationId");
                File fileRes = new File(FileStoragePath + "resume" + "_" + applicationId);
                byte[] bufferRes = new byte[(int) fileRes.length()];
                File fileCov = new File(FileStoragePath + "cover" + "_" + applicationId);
                byte[] bufferCov = new byte[(int) fileCov.length()];
                try {
                    FileInputStream input1 = new FileInputStream(fileRes);
                    input1.read(bufferRes);
                    input1.close();
                    FileInputStream input2 = new FileInputStream(fileCov);
                    input2.read(bufferCov);
                    input2.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }

                Application application = new ApplicationBuilder()
                        .applicationId(applicationId)
                        .postingId(rs.getInt("postingId"))
                        .userId(rs.getInt("userId"))
                        .resume(bufferRes)        //returns BASE64
                        .coverLetter(bufferCov)   //returns BASE64
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
        Integer applicationId;
        try {
            byte[] requestBytes = rc.request().payload().get().toByteArray();
            Map jsonMap = objectMapper.readValue(requestBytes, Map.class);
            Integer postingId = Integer.valueOf(jsonMap.get("postingId").toString());
            Integer userId = Integer.valueOf(jsonMap.get("userId").toString());
            String resume = jsonMap.get("resume").toString();
            String coverLetter = jsonMap.get("coverLetter").toString();

            Connection conn = DriverManager.getConnection(
                    config.getString("mysql.jdbc"),
                    config.getString("mysql.user"),
                    config.getString("mysql.password"));
            String sqlQuery = "INSERT INTO applications (postingId, userId) " +
                              "VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, postingId);
            ps.setInt(2, userId);
            //timestamped automatically in UTC by mysql database
            int rowsAffected = ps.executeUpdate();

            //get ApplicationId
            if (rowsAffected == 0) {
                throw new SQLException("Creating new user failed, no rows affected.");
            }
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                     applicationId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating new user failed, no ID obtained.");
                }
            }
            //write to file system
            byte[] decodedRes = Base64.getDecoder().decode(resume);
            byte[] decodedCov = Base64.getDecoder().decode(coverLetter);

            try {
                FileOutputStream output1 = new FileOutputStream(FileStoragePath + "resume" + "_" + applicationId);
                output1.write(decodedRes);
                FileOutputStream output2 = new FileOutputStream(FileStoragePath + "cover" + "_" + applicationId);
                output2.write(decodedCov);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
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