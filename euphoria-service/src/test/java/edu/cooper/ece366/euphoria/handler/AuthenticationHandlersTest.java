package edu.cooper.ece366.euphoria;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.Request;
import com.spotify.apollo.RequestContext;
import com.typesafe.config.Config;
import okio.ByteString;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

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
    Request request;
    @Mock
    ByteString requestPayloadByteString;

    private AuthenticationHandlers testClass;

    @Before
    public void setup() {
        testClass = new AuthenticationHandlers(objectMapper, config);
    }

    @Test
    public void getAuthentication() {
        // Setup variables
        Authentication expected = new AuthenticationBuilder()
                .id(1)
                .username("johnnyappleseed")
                .passwordHash("hash")
                .isUser(true)
                .build();

        Map<String, String> map = new HashMap<>();
        map.put("username", "johnnyappleseed");
        map.put("passwordHash", "hash");

        // Mock dependencies and inputs
        when(config.getString("mysql.jdbc")).thenReturn("jdbc:mysql://localhost:3306/euphoria");
        when(config.getString("mysql.user")).thenReturn("euphoria");
        when(config.getString("mysql.password")).thenReturn("euphoria");
        when(rc.pathArgs()).thenReturn(map);

        // Call test class
        Authentication actual = testClass.getAuthentication(rc).get(0);

        // Assert and verify
        assertEquals(expected, actual);

        verifyZeroInteractions(objectMapper);
    }

    @Test
    public void createAuthentication() throws IOException {
        // Setup variables
        List<Application> expected = Collections.emptyList();
        byte[] byteArray = new byte[0];

        Authentication authentication = new AuthenticationBuilder()
                .id(1)
                .username("johnnyappleseed")
                .passwordHash("hash")
                .isUser(true)
                .build();

        when(rc.request()).thenReturn(request);
        when(request.payload()).thenReturn(Optional.of(requestPayloadByteString));
        when(requestPayloadByteString.toByteArray()).thenReturn(byteArray);
        when(objectMapper.readValue(byteArray, Authentication.class)).thenReturn(authentication);

        // Call test class
        List<Authentication> actual = testClass.createAuthentication(rc);

        // Assert and verify
        assertEquals(expected, actual);
    }
}
