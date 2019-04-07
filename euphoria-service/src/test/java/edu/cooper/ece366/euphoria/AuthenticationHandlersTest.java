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
public class AuthenticationHandlersTest {

    @Mock
    ObjectMapper objectMapper;
    @Mock
    RequestContext rc;
    @Mock
    PreparedStatement ps;
    @Mock
    ResultSet rs;

    private AuthenticationHandlers testClass;

    @Before
    public void setup() {
        testClass = new AuthenticationHandlers(objectMapper);
    }

    @Test
    public void getAuthentication() throws SQLException {
        // Setup variables
        Authentication expected = new AuthenticationBuilder()
                .username("johnsmith")
                .passwordHash("hash")
                .isUser(true)
                .build();

        // Mock dependencies and inputs
        when(rs.getString("username")).thenReturn(expected.username());
        when(rs.getString("passwordHash")).thenReturn(expected.passwordHash());
        when(rs.getBoolean("isUser")).thenReturn(expected.isUser());
        when(ps.executeQuery()).thenReturn(rs);

        // Call test class
        Authentication actual = testClass.getAuthentication(rc).get(0);

        // Assert and verify
        assertEquals(expected, actual);

        verifyZeroInteractions(objectMapper);
    }

    @Test
    public void createAuthentication() throws SQLException {
        // Setup variables
        List<Authentication> expected = Collections.emptyList();

        // Mock dependencies and inputs
        when(rs.getString("username")).thenReturn("johnsmith");
        when(rs.getString("passwordHash")).thenReturn("hash");
        when(rs.getBoolean("isUser")).thenReturn(true);
        when(ps.executeQuery()).thenReturn(rs);

        // Call test class
        List<Authentication> actual = testClass.createAuthentication(rc);

        // Assert and verify
        assertEquals(expected, actual);

        verifyZeroInteractions(objectMapper);
    }
}
