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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationHandlersTest {

    @Mock
    ObjectMapper objectMapper;
    @Mock
    RequestContext rc;
    @Mock
    PreparedStatement ps;
    @Mock
    ResultSet rs;

    private ApplicationHandlers testClass;

    @Before
    public void setup() {
        testClass = new ApplicationHandlers(objectMapper);
    }

    @Test
    public void getApplication() throws SQLException {
        byte[] emptyArray = new byte[0];

        // Setup variables
        Application expected = new ApplicationBuilder()
                .applicationId(1)
                .postingId(1)
                .userId(1)
                .resume(emptyArray)
                .coverLetter(emptyArray)
                .build();

        // Mock dependencies and inputs
        when(rc.pathArgs()).thenReturn(Collections.singletonMap("applicationId", "1"));
        when(rs.getString("applicationId")).thenReturn(expected.postingId().toString());
        when(rs.getString("postingId")).thenReturn(expected.postingId().toString());
        when(rs.getString("userId")).thenReturn(expected.userId().toString());
        when(rs.getBytes("resume")).thenReturn(emptyArray);
        when(rs.getBytes("coverLetter")).thenReturn(emptyArray);
        when(ps.executeQuery()).thenReturn(rs);

        // Call test class
        Application actual = testClass.getApplication(rc).get(0);

        // Assert and verify
        assertEquals(expected, actual);

        verifyZeroInteractions(objectMapper);
    }

    @Test
    public void getApplicationsForPosting() throws SQLException {
        byte[] emptyArray = new byte[0];

        // Setup variables
        Application expected = new ApplicationBuilder()
                .applicationId(1)
                .postingId(1)
                .userId(1)
                .resume(emptyArray)
                .coverLetter(emptyArray)
                .build();

        // Mock dependencies and inputs
        when(rc.pathArgs()).thenReturn(Collections.singletonMap("postingId", "1"));
        when(rs.getString("applicationId")).thenReturn(expected.postingId().toString());
        when(rs.getString("postingId")).thenReturn(expected.postingId().toString());
        when(rs.getString("userId")).thenReturn(expected.userId().toString());
        when(rs.getBytes("resume")).thenReturn(emptyArray);
        when(rs.getBytes("coverLetter")).thenReturn(emptyArray);
        when(ps.executeQuery()).thenReturn(rs);

        // Call test class
        Application actual = testClass.getApplicationsForPosting(rc).get(0);

        // Assert and verify
        assertEquals(expected, actual);

        verifyZeroInteractions(objectMapper);
    }

    @Test
    public void createApplication() throws SQLException {
        byte[] emptyArray = new byte[0];

        // Setup variables
        List<Application> expected = Collections.emptyList();

        // Mock dependencies and inputs
        when(rs.getString("applicationId")).thenReturn("1");
        when(rs.getString("postingId")).thenReturn("1");
        when(rs.getString("userId")).thenReturn("1");
        when(rs.getBytes("resume")).thenReturn(emptyArray);
        when(rs.getBytes("coverLetter")).thenReturn(emptyArray);
        when(ps.executeQuery()).thenReturn(rs);

        // Call test class
        List<Application> actual = testClass.createApplication(rc);

        // Assert and verify
        assertEquals(expected, actual);

        verifyZeroInteractions(objectMapper);
    }
}
