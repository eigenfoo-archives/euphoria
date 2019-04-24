package club.euphoria_recruiting.store.jdbc;

import com.typesafe.config.Config;
import club.euphoria_recruiting.model.Application;
import club.euphoria_recruiting.model.ApplicationBuilder;
import club.euphoria_recruiting.store.model.ApplicationStore;
import org.apache.commons.dbutils.DbUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public class ApplicationStoreJdbc implements ApplicationStore {

    private static final String GET_APPLICATION_STATEMENT = "SELECT * FROM applications WHERE applicationId = ?";
    private static final String GET_APPLICATIONS_FOR_POSTING_STATEMENT = "SELECT * FROM applications WHERE postingId = ?";
    private static final String CREATE_APPLICATION_STATEMENT = "INSERT INTO applications (postingId, userId) VALUES (?, ?)";
    private static final String ADD_FILE_LOCATIONS_STATEMENT = "UPDATE applications SET resumeLocation = ?,  coverLetterLocation = ? WHERE applicationId = ? ";

    private final DataSource dataSource;
    private final String fileStoragePath;

    public ApplicationStoreJdbc(final DataSource dataSource, final Config config) {
        this.dataSource = dataSource;
        fileStoragePath = config.getString("FileStoragePath").replaceFirst("^~", System.getProperty("user.home"));
    }

    @Override
    public Application getApplication(final String applicationId) {
        File fileRes = new File(fileStoragePath + "app_" + applicationId + "/resume_" + applicationId + ".pdf");
        byte[] bufferRes = new byte[(int) fileRes.length()];
        File fileCov = new File(fileStoragePath + "app_" + applicationId + "/cover_" + applicationId + ".pdf");
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

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn= dataSource.getConnection();
            ps = conn.prepareStatement(GET_APPLICATION_STATEMENT);
            ps.setInt(1, Integer.parseInt(applicationId));
            rs = ps.executeQuery();

            if (rs.first()) {
                return new ApplicationBuilder()
                        .applicationId(rs.getInt("applicationId"))
                        .postingId(rs.getInt("postingId"))
                        .userId(rs.getInt("userId"))
                        .resume(bufferRes)        //returns BASE64
                        .coverLetter(bufferCov)   //returns BASE64
                        .dateCreated(rs.getString("dateCreated"))
                        .build();
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("error fetching application", e);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }

    }

    @Override
    public List<Application> getApplicationsForPosting(final String postingId) {
        ArrayList<Application> applicationList = new ArrayList<Application>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn= dataSource.getConnection();
            ps = conn.prepareStatement(GET_APPLICATIONS_FOR_POSTING_STATEMENT);
            ps.setInt(1, Integer.parseInt(postingId));
            rs = ps.executeQuery();

            while (rs.next()) {
                Integer applicationId = rs.getInt("applicationId");
                File fileRes = new File(fileStoragePath + "app_" + applicationId + "/resume_" + applicationId + ".pdf");
                byte[] bufferRes = new byte[(int) fileRes.length()];
                File fileCov = new File(fileStoragePath + "app_" + applicationId + "/cover_" + applicationId + ".pdf");
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
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(rs);
        }

        return applicationList;
    }

    @Override
    public List<Application> createApplication(final String postingId, final String userId, final String resume, final String coverLetter) {
        Integer applicationId = null;
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn= dataSource.getConnection();
            ps = conn.prepareStatement(CREATE_APPLICATION_STATEMENT, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, Integer.parseInt(postingId));
            ps.setInt(2, Integer.parseInt(userId));
            //timestamped automatically in UTC by mysql database
            int rowsAffected = ps.executeUpdate();

            //get ApplicationId
            if (rowsAffected == 0) {
                throw new SQLException("Creating new application failed, no rows affected.");
            }
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    applicationId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating new application failed, no ID obtained.");
                }
            }
            //write to file system
            byte[] decodedRes = Base64.getDecoder().decode(resume);
            byte[] decodedCov = Base64.getDecoder().decode(coverLetter);

            try {
                File newDir = new File(fileStoragePath + "app_" + applicationId);
                newDir.mkdir();
                FileOutputStream output1 = new FileOutputStream(fileStoragePath + "app_" + applicationId + "/resume_" + applicationId + ".pdf");
                output1.write(decodedRes);
                FileOutputStream output2 = new FileOutputStream(fileStoragePath + "app_" + applicationId + "/cover_" + applicationId + ".pdf");
                output2.write(decodedCov);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            ps = conn.prepareStatement(ADD_FILE_LOCATIONS_STATEMENT);
            ps.setString(1, "file://" + fileStoragePath + "app_" + applicationId + "/resume_" + applicationId + ".pdf");
            ps.setString(2, "file://" + fileStoragePath + "app_" + applicationId + "/cover_" + applicationId + ".pdf");
            ps.setInt(3, applicationId);
            rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Inserting application file paths failed, no rows affected.");
            }

            return Collections.emptyList(); //if everything successful, return empty list

        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(ps);
        }

        return null; //if not successful, return null
    }
}
