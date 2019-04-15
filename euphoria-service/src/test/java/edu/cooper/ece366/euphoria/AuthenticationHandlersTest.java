package edu.cooper.ece366.euphoria;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.RequestContext;
import com.typesafe.config.Config;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
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
public class AuthenticationHandlersTest {

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
    @Mock
    byte[] requestBytes;

    private AuthenticationHandlers testClass;

    @Before
    public void setup() {
        testClass = new AuthenticationHandlers(objectMapper, config);
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
    public void createAuthentication() throws SQLException, IOException {
        // Setup variables
        List<Authentication> expected = Collections.emptyList();
        byte[] foo = new byte[0];

        // Mock dependencies and inputs
        Map<String, Object> request = new HashMap<String, Object>();
        request.put("username", "johnsmith");
        request.put("passwordHash", "hash");
        request.put("isUser", true);
        when(rc.request().payload().get().toByteArray()).thenReturn(foo);
        when(objectMapper.readValue(requestBytes, Map.class)).thenReturn(request);

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
