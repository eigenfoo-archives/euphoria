package edu.cooper.ece366.euphoria;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.route.*;
import okio.ByteString;

import javax.sql.rowset.serial.SerialBlob;
import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.sql.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class ApplicationHandlers implements RouteProvider {
    private static final String dbUrl = "jdbc:mysql://localhost:3306/euphoria";
    private static final String dbUsername = "euphoria";
    private static final String dbPassword = "euphoria";
    private final ObjectMapper objectMapper;

    public ApplicationHandlers(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
        return Stream.of(
                Route.sync("GET", "/application/<applicationId>", this::getApplication),
                Route.sync("GET", "/application/matchPosting/<postingId>", this::getApplicationsForPosting),
                Route.sync("POST",
                        "/application/<postingId>/<userId>/<resume>/<coverLetter>",
                        this::createApplication)
        ).map(r -> r.withMiddleware(jsonMiddleware()));
    }

    private List<Application> getApplication(final RequestContext rc) {
        Application application = null;

        try {
            Integer applicationId = Integer.valueOf(rc.pathArgs().get("applicationId"));
            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            String sqlQuery = "SELECT * FROM applications WHERE applicationId = ?";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setInt(1, applicationId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {  //FIXME Only read the first result. There should only be one, after all...
                application = new ApplicationBuilder()
                        .applicationId(rs.getInt("applicationId"))
                        .postingId(rs.getInt("postingId"))
                        .userId(rs.getInt("userId"))
                        .resume (rs.getBytes("resume"))            //returns BASE64
                        .coverLetter(rs.getBytes("coverLetter"))   //returns BASE64
                        .build();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return Collections.singletonList(application);
    }

    private List<Application> getApplicationsForPosting(final RequestContext rc) {
        ArrayList<Application> applicationList = new ArrayList<Application>();

        try {
            Integer postingId = Integer.valueOf(rc.pathArgs().get("postingId"));
            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            String sqlQuery = "SELECT * FROM applications WHERE postingId = ?";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setInt(1, postingId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Application application = new ApplicationBuilder()
                        .applicationId(rs.getInt("applicationId"))
                        .postingId(rs.getInt("postingId"))
                        .userId(rs.getInt("userId"))
                        .resume (rs.getBytes("resume"))            //returns BASE64
                        .coverLetter(rs.getBytes("coverLetter"))   //returns BASE64
                        .build();

                applicationList.add(application);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return applicationList;
    }


    private List<Application> createApplication(final RequestContext rc) {
        try {
            Integer postingId = Integer.valueOf(rc.pathArgs().get("postingId"));
            Integer userId = Integer.valueOf(rc.pathArgs().get("userId"));
            byte[]resume = DatatypeConverter.parseHexBinary(rc.pathArgs().get("resume"));              //HTTP req body
            byte[] coverLetter = DatatypeConverter.parseHexBinary(rc.pathArgs().get("coverLetter"));  //HTTP req body

            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            String sqlQuery = "INSERT INTO applications (postingId, userId, " +
                    "resume, coverLetter, dateCreated) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setInt(1, postingId);
            ps.setInt(2, userId);
            ps.setBytes(3, resume);
            ps.setBytes(4, coverLetter);
            Date date = new Date();
            ps.setObject(5, date.toInstant().atZone(ZoneId.of("UTC")).toLocalDate());
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