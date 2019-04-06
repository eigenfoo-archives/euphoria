package edu.cooper.ece366.euphoria;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.RequestContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PostingHandlersTest {

    @Mock
    ObjectMapper objectMapper;
    @Mock
    RequestContext rc;
    @Mock
    PreparedStatement ps;
    @Mock
    ResultSet rs;

    private PostingHandlers testClass;

    @Before
    public void setup() {
        testClass = new PostingHandlers(objectMapper);
    }

    @Test
    public void getPosting() throws SQLException {
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
        when(rc.pathArgs()).thenReturn(Collections.singletonMap("id", "1"));
        when(rs.getString("postingId")).thenReturn(expected.postingId().toString());
        when(rs.getString("jobTitle")).thenReturn(expected.jobTitle());
        when(rs.getString("description")).thenReturn(expected.description());
        when(rs.getString("location")).thenReturn(expected.location().toString());
        when(rs.getString("industry")).thenReturn(expected.industry().toString());
        when(rs.getString("skillLevel")).thenReturn(expected.skillLevel().toString());
        when(ps.executeQuery()).thenReturn(rs);

        // Call test class
        Posting actual = testClass.getPosting(rc).get(0);

        // Assert and verify
        assertEquals(expected, actual);

        verifyZeroInteractions(objectMapper);
    }

    @Test
    public void getAllPostings() throws SQLException {
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
        when(rs.getString("postingId")).thenReturn(expected.postingId().toString());
        when(rs.getString("jobTitle")).thenReturn(expected.jobTitle());
        when(rs.getString("description")).thenReturn(expected.description());
        when(rs.getString("location")).thenReturn(expected.location().toString());
        when(rs.getString("industry")).thenReturn(expected.industry().toString());
        when(rs.getString("skillLevel")).thenReturn(expected.skillLevel().toString());
        when(ps.executeQuery()).thenReturn(rs);

        // Call test class
        Posting actual = testClass.getAllPostings(rc).get(0);

        // Assert and verify
        assertEquals(expected, actual);

        verifyZeroInteractions(objectMapper);
    }
}
