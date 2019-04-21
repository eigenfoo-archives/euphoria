package edu.cooper.ece366.euphoria.store.jdbc;

import com.typesafe.config.Config;
import edu.cooper.ece366.euphoria.model.*;
import edu.cooper.ece366.euphoria.store.model.PostingStore;
import edu.cooper.ece366.euphoria.utils.Industry;
import edu.cooper.ece366.euphoria.utils.Location;
import edu.cooper.ece366.euphoria.utils.SkillLevel;

import java.io.File;
import java.nio.file.NoSuchFileException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PostingStoreJdbc implements PostingStore {

    private static final String GET_POSTING_STATEMENT = "SELECT * FROM postings WHERE postingId = ?";

    private static final String SEARCH_POSTINGS_STATEMENT = "SELECT * FROM postings WHERE location LIKE ? " +
                                                                "AND industry LIKE ? AND skillLevel LIKE ?";;

    private static final String GET_ALL_POSTINGS_STATEMENT =         "SELECT * FROM postings";

    private static final String GET_RANDOM_POSTINGS_STATEMENT =      "SELECT * FROM postings ORDER BY RAND() LIMIT 10";

    private static final String GET_POSTINGS_FOR_COMPANY_STATEMENT = "SELECT * FROM postings WHERE companyId = ?";

    private static final String CREATE_POSTING_STATEMENT = "INSERT INTO postings (companyId, jobTitle, " +
                                                                "description, location, industry, skillLevel) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String EDIT_POSTING_STATEMENT =   "UPDATE postings SET jobTitle = ?, " +
                                                                "description = ?, location = ?, industry = ?, skillLevel = ? WHERE postingId = ?";

    private static final String DELETE_POSTING_STATEMENT =    "DELETE FROM postings WHERE postingId = ?";

    private static final String GET_ASSOC_APPS_STATEMENT =    "SELECT applicationId FROM applications WHERE postingId = ?";

    private static final String DELETE_ASSOC_APPS_STATEMENT = "DELETE FROM applications WHERE postingId = ?";

    private final Config config;
    private final String FileStoragePath;

    public PostingStoreJdbc(final Config config) {
        this.config = config;
        FileStoragePath = config.getString("FileStoragePath");
    }

    @Override
    public Posting getPosting(final String postingId) {
        Connection connection;
        try {
            connection =
                    DriverManager.getConnection(
                            config.getString("mysql.jdbc"),
                            config.getString("mysql.user"),
                            config.getString("mysql.password"));

            PreparedStatement ps = connection.prepareStatement(GET_POSTING_STATEMENT);
            ps.setInt(1, Integer.parseInt(postingId));

            ResultSet rs = ps.executeQuery();

            if (rs.first()) {
                return new PostingBuilder()
                        .postingId(rs.getInt("postingId"))
                        .companyId(rs.getInt("companyId"))
                        .jobTitle(rs.getString("jobTitle"))
                        .description(rs.getString("description"))
                        .location(Location.valueOf(rs.getString("location")))
                        .industry(Industry.valueOf(rs.getString("industry")))
                        .skillLevel(SkillLevel.valueOf(rs.getString("skillLevel")))
                        .dateCreated(rs.getString("dateCreated"))
                        .build();
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("error fetching user", e);
        }
    }

    @Override
    public List<Posting> searchPostings(final String location, final String industry, final String skillLevel) {
        ArrayList<Posting> postingList = new ArrayList<Posting>();

        Connection connection;
        try {
            connection =
                    DriverManager.getConnection(
                            config.getString("mysql.jdbc"),
                            config.getString("mysql.user"),
                            config.getString("mysql.password"));

            PreparedStatement ps = connection.prepareStatement(SEARCH_POSTINGS_STATEMENT);
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

    @Override
    public List<Posting> getAllPostings() {
        ArrayList<Posting> postingList = new ArrayList<Posting>();

        Connection connection;
        try {
            connection =
                    DriverManager.getConnection(
                            config.getString("mysql.jdbc"),
                            config.getString("mysql.user"),
                            config.getString("mysql.password"));
            PreparedStatement ps = connection.prepareStatement(GET_ALL_POSTINGS_STATEMENT);
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

    @Override
    public List<Posting> getRandomPostings() {
        ArrayList<Posting> postingList = new ArrayList<Posting>();

        Connection connection;
        try {
            connection =
                    DriverManager.getConnection(
                            config.getString("mysql.jdbc"),
                            config.getString("mysql.user"),
                            config.getString("mysql.password"));
            PreparedStatement ps = connection.prepareStatement(GET_RANDOM_POSTINGS_STATEMENT);
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

    @Override
    public List<Posting> getPostingsForCompany(final String companyId) {
        ArrayList<Posting> postingList = new ArrayList<Posting>();

        Connection connection;
        try {
            connection =
                    DriverManager.getConnection(
                            config.getString("mysql.jdbc"),
                            config.getString("mysql.user"),
                            config.getString("mysql.password"));
            PreparedStatement ps = connection.prepareStatement(GET_POSTINGS_FOR_COMPANY_STATEMENT);
            ps.setInt(1,  Integer.parseInt(companyId));
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

    @Override
    public List<Posting> createPosting(final String companyId, final String jobTitle, final String description, final Location location, final Industry industry, final SkillLevel skillLevel) {
        Connection connection;
        try {
            connection =
                    DriverManager.getConnection(
                            config.getString("mysql.jdbc"),
                            config.getString("mysql.user"),
                            config.getString("mysql.password"));
            PreparedStatement ps = connection.prepareStatement(CREATE_POSTING_STATEMENT);
            ps.setInt(1, Integer.parseInt(companyId));
            ps.setString(2, jobTitle);
            ps.setString(3, description);
            ps.setString(4, location.toString());
            ps.setString(5, industry.toString());
            ps.setString(6, skillLevel.toString());
            //timestamped automatically in UTC by mysql database
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating new authentication failed, no rows affected.");
            }
            return Collections.emptyList(); //if everything successful, return empty list
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return null; //if not successful, return null
    }

    @Override
    public List<Posting> editPosting(final String postingId, final String jobTitle, final String description, final Location location, final Industry industry, final SkillLevel skillLevel) {
        Connection connection;
        try {
            connection =
                    DriverManager.getConnection(
                            config.getString("mysql.jdbc"),
                            config.getString("mysql.user"),
                            config.getString("mysql.password"));
            PreparedStatement ps = connection.prepareStatement(EDIT_POSTING_STATEMENT);
            ps.setString(1, jobTitle);
            ps.setString(2, description);
            ps.setString(3, location.toString());
            ps.setString(4, industry.toString());
            ps.setString(5, skillLevel.toString());
            ps.setInt(6, Integer.parseInt(postingId));
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("editing posting failed, no rows affected.");
            }
            return Collections.emptyList(); //if everything successful, return empty list
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return null; //if not successful, return null
    }

    @Override
    public List<Posting> deletePosting(final String postingId) {
        Connection connection;
        boolean empty = true;
        try {
            connection =
                    DriverManager.getConnection(
                            config.getString("mysql.jdbc"),
                            config.getString("mysql.user"),
                            config.getString("mysql.password"));

            PreparedStatement ps = connection.prepareStatement(DELETE_POSTING_STATEMENT);
            ps.setInt(1, Integer.parseInt(postingId));
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("delete posting failed, no rows affected.");
            }

            //remove associated resumes and cover letters from file system
            ps = connection.prepareStatement(GET_ASSOC_APPS_STATEMENT);
            ps.setInt(1, Integer.parseInt(postingId));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                empty = false;
                Integer applicationId = rs.getInt("applicationId");
                try {
                    File fileRes = new File(FileStoragePath + "app_" + applicationId + "/resume_" + applicationId + ".pdf");
                    File fileCov = new File(FileStoragePath + "app_" + applicationId + "/cover_" + applicationId + ".pdf");
                    File fileDir = new File(FileStoragePath + "app_" + applicationId);
                    fileRes.delete();
                    fileCov.delete();
                    fileDir.delete();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            if  (!empty) {
                ps = connection.prepareStatement(DELETE_ASSOC_APPS_STATEMENT);
                ps.setInt(1, Integer.parseInt(postingId));
                rowsAffected = ps.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("error removing associated applications from applications table, no rows affected.");
                }
            }

            return Collections.emptyList(); //if everything successful, return empty list

        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return null; //if not successful, return null
    }
}