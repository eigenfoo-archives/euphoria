package edu.cooper.ece366.euphoria;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.RequestContext;
import com.typesafe.config.Config;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PostingHandlersTest {

    @Mock
    ObjectMapper objectMapper;
    @Mock
    Config config;
    @Mock
    RequestContext rc;
    @Mock
    PreparedStatement ps;
    @Mock
    ResultSet rs;

    private PostingHandlers testClass;

    @Before
    public void setup() {
        testClass = new PostingHandlers(objectMapper, config);
    }

    @Test
    public void getPostingAndAllPostings() throws SQLException {
        // Setup variables
        Posting expected = new PostingBuilder()
                .postingId(1)
                .jobTitle("Underwater Basket Weaver")
                .description("What it sounds like.")
                .location(Location.valueOf("NEWYORK"))
                .industry(Industry.valueOf("FINANCE"))
                .skillLevel(SkillLevel.valueOf("INTERNSHIP"))
                .build();

        // Mock dependencies and inputs
        when(rc.pathArgs()).thenReturn(Collections.singletonMap("postingId", "1"));
        when(rs.getString("postingId")).thenReturn(expected.postingId().toString());
        when(rs.getString("jobTitle")).thenReturn(expected.jobTitle());
        when(rs.getString("description")).thenReturn(expected.description());
        when(rs.getString("location")).thenReturn(expected.location().toString());
        when(rs.getString("industry")).thenReturn(expected.industry().toString());
        when(rs.getString("skillLevel")).thenReturn(expected.skillLevel().toString());
        when(ps.executeQuery()).thenReturn(rs);

        // Call test class
        Posting actualSingle = testClass.getPosting(rc).get(0);
        Posting actualAll = testClass.getAllPostings(rc).get(0);

        // Assert and verify
        assertEquals(expected, actualSingle);
        assertEquals(expected, actualAll);

        verifyZeroInteractions(objectMapper);
    }

    @Test
    public void searchPostings() throws SQLException {
        // Setup variables
        Posting expected = new PostingBuilder()
                .postingId(1)
                .jobTitle("Underwater Basket Weaver")
                .description("What it sounds like.")
                .location(Location.valueOf("NEWYORK"))
                .industry(Industry.valueOf("FINANCE"))
                .skillLevel(SkillLevel.valueOf("INTERNSHIP"))
                .build();

        // Mock dependencies and inputs
        Map<String, String> rcMap = new HashMap<>();
        rcMap.put("location", "NEWYORK");
        rcMap.put("industry", "FINANCE");
        rcMap.put("skillLevel", "INTERNSHIP");
        when(rc.pathArgs()).thenReturn(rcMap);
        when(rs.getString("postingId")).thenReturn(expected.postingId().toString());
        when(rs.getString("jobTitle")).thenReturn(expected.jobTitle());
        when(rs.getString("description")).thenReturn(expected.description());
        when(rs.getString("location")).thenReturn(expected.location().toString());
        when(rs.getString("industry")).thenReturn(expected.industry().toString());
        when(rs.getString("skillLevel")).thenReturn(expected.skillLevel().toString());
        when(ps.executeQuery()).thenReturn(rs);

        // Call test class
        Posting actualSingle = testClass.searchPostings(rc).get(0);

        // Assert and verify
        assertEquals(expected, actualSingle);

        verifyZeroInteractions(objectMapper);
    }

    @Test
    public void createAndEditPosting() throws SQLException {
        // Setup variables
        List<Posting> expected = Collections.emptyList();

        // Mock dependencies and inputs
        when(rs.getString("jobTitle")).thenReturn("Underwater Basket Weaver");
        when(rs.getString("description")).thenReturn("What it sounds like.");
        when(rs.getString("location")).thenReturn("NEWYORK");
        when(rs.getString("industry")).thenReturn("FINANCE");
        when(rs.getString("skillLevel")).thenReturn("INTERNSHIP");

        // Call test class
        List<Posting> actualCreated = testClass.createPosting(rc);
        List<Posting> actualEdited = testClass.editPosting(rc);

        // Assert and verify
        assertEquals(expected, actualCreated);
        assertEquals(expected, actualEdited);

        verifyZeroInteractions(objectMapper);
    }

    @Test
    public void deletePosting() throws SQLException {
        // Setup variables
        List<Posting> expected = Collections.emptyList();

        // Mock dependencies and inputs
        when(rs.getString("postingId")).thenReturn("1");

        // Call test class
        List<Posting> actual = testClass.deletePosting(rc);

        // Assert and verify
        assertEquals(expected, actual);

        verifyZeroInteractions(objectMapper);
    }
}
