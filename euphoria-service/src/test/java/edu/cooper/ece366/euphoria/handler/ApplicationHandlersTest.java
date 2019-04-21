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
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationHandlersTest {

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

    private ApplicationHandlers testClass;

    @Before
    public void setup() {
        testClass = new ApplicationHandlers(objectMapper, config);
    }

    @Test
    public void getApplication() {
        byte[] emptyArray = new byte[0];

        // Setup variables
        Application expected = new ApplicationBuilder()
                .applicationId(1)
                .postingId(1)
                .userId(1)
                .resume(emptyArray)
                .coverLetter(emptyArray)
                .dateCreated("2018-07-11 05:30:00")
                .build();

        // Mock dependencies and inputs
        when(config.getString("mysql.jdbc")).thenReturn("jdbc:mysql://localhost:3306/euphoria");
        when(config.getString("mysql.user")).thenReturn("euphoria");
        when(config.getString("mysql.password")).thenReturn("euphoria");
        when(rc.pathArgs()).thenReturn(Collections.singletonMap("applicationId", "1"));

        // Call test class
        Application actual = testClass.getApplication(rc).get(0);

        // Assert and verify
        assertEquals(expected, actual);
    }

    @Test
    public void getApplicationsForPosting() {
        byte[] emptyArray = new byte[0];

        // Setup variables
        Application expected = new ApplicationBuilder()
                .applicationId(1)
                .postingId(1)
                .userId(1)
                .resume(emptyArray)
                .coverLetter(emptyArray)
                .dateCreated("2018-07-11 05:30:00")
                .build();

        // Mock dependencies and inputs
        when(config.getString("mysql.jdbc")).thenReturn("jdbc:mysql://localhost:3306/euphoria");
        when(config.getString("mysql.user")).thenReturn("euphoria");
        when(config.getString("mysql.password")).thenReturn("euphoria");
        when(rc.pathArgs()).thenReturn(Collections.singletonMap("postingId", "1"));

        // Call test class
        Application actual = testClass.getApplicationsForPosting(rc).get(0);

        // Assert and verify
        assertEquals(expected, actual);
    }

    @Test
    public void createApplication() throws IOException {
        // Setup variables
        List<Application> expected = Collections.emptyList();
        byte[] byteArray = new byte[0];

        // Mock dependencies and inputs
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("applicationId", 1);
        map.put("postingId", 1);
        map.put("userId", 1);
        map.put("resume", byteArray);
        map.put("coverLetter", byteArray);
        when(rc.request()).thenReturn(request);
        when(request.payload()).thenReturn(Optional.of(requestPayloadByteString));
        when(requestPayloadByteString.toByteArray()).thenReturn(byteArray);
        when(objectMapper.readValue(byteArray, Map.class)).thenReturn(map);

        // Call test class
        List<Application> actual = testClass.createApplication(rc);

        // Assert and verify
        assertEquals(expected, actual);
    }
}
