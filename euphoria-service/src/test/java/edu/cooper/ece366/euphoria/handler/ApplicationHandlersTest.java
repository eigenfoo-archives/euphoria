package edu.cooper.ece366.euphoria;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.Request;
import com.spotify.apollo.RequestContext;
import edu.cooper.ece366.euphoria.handler.ApplicationHandlers;
import edu.cooper.ece366.euphoria.model.Application;
import edu.cooper.ece366.euphoria.model.ApplicationBuilder;
import edu.cooper.ece366.euphoria.store.model.ApplicationStore;
import okio.ByteString;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationHandlersTest {
    @Mock
    ObjectMapper objectMapper;
    @Mock
    ApplicationStore applicationStore;
    @Mock
    RequestContext requestContext;
    @Mock
    Request request;
    @Mock
    ByteString requestPayloadByteString;

    private ApplicationHandlers testClass;

    @Before
    public void setup() {
        testClass = new ApplicationHandlers(objectMapper, applicationStore);
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
        when(requestContext.pathArgs()).thenReturn(Collections.singletonMap("applicationId", "1"));
        when(applicationStore.getApplication("1")).thenReturn(expected);

        // Call test class
        Application actual = testClass.getApplication(requestContext);

        // Assert and verify
        assertEquals(expected, actual);
        verifyZeroInteractions(objectMapper);
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
        when(requestContext.pathArgs()).thenReturn(Collections.singletonMap("postingId", "1"));
        when(applicationStore.getApplicationsForPosting("1")).thenReturn(Collections.singletonList(expected));

        // Call test class
        Application actual = testClass.getApplicationsForPosting(requestContext).get(0);

        // Assert and verify
        assertEquals(expected, actual);
        verifyZeroInteractions(objectMapper);
    }

    @Test
    public void createApplication() throws IOException {
        // Setup variables
        List<Application> expected = Collections.emptyList();
        byte[] byteArray = new byte[0];

        // Mock dependencies and inputs
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("applicationId", "1");
        map.put("postingId", "1");
        map.put("userId", "1");
        map.put("resume", "");
        map.put("coverLetter", "");
        when(requestContext.request()).thenReturn(request);
        when(request.payload()).thenReturn(Optional.of(requestPayloadByteString));
        when(requestPayloadByteString.toByteArray()).thenReturn(byteArray);
        when(objectMapper.readValue(byteArray, Map.class)).thenReturn(map);
        when(applicationStore.createApplication("1", "1", "", ""))
                .thenReturn(Collections.emptyList());

        // Call test class
        List<Application> actual = testClass.createApplication(requestContext);

        // Assert
        assertEquals(expected, actual);
    }
}
