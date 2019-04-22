package edu.cooper.ece366.euphoria.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.Request;
import com.spotify.apollo.RequestContext;
import edu.cooper.ece366.euphoria.model.Cookie;
import edu.cooper.ece366.euphoria.model.CookieBuilder;
import edu.cooper.ece366.euphoria.store.model.CookieStore;
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
public class CookieHandlersTest {
    @Mock
    ObjectMapper objectMapper;
    @Mock
    CookieStore cookieStore;
    @Mock
    RequestContext requestContext;
    @Mock
    Request request;
    @Mock
    ByteString requestPayloadByteString;

    private CookieHandlers testClass;

    @Before
    public void setup() {
        testClass = new CookieHandlers(objectMapper, cookieStore);
    }

    @Test
    public void getCookie() {
        // Setup variables
        Cookie expected = new CookieBuilder()
                .id(1)
                .isUser(true)
                .cookie("1234567890")
                .build();

        // Mock dependencies and inputs
        when(requestContext.pathArgs()).thenReturn(Collections.singletonMap("cookieCheck", "1234567890"));
        when(cookieStore.getCookie("1234567890")).thenReturn(expected);

        // Call test class
        Cookie actual = testClass.getCookie(requestContext);

        // Assert and verify
        assertEquals(expected, actual);
        verifyZeroInteractions(objectMapper);
    }

    @Test
    public void createCookie() throws IOException {
        // Setup variables
        Cookie expected = new CookieBuilder()
                .id(1)
                .isUser(true)
                .cookie("1234567890")
                .build();
        byte[] byteArray = new byte[0];

        // Mock dependencies and inputs
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", "johnsmith");
        map.put("passwordHash", "hash");

        when(requestContext.request()).thenReturn(request);
        when(request.payload()).thenReturn(Optional.of(requestPayloadByteString));
        when(requestPayloadByteString.toByteArray()).thenReturn(byteArray);
        when(objectMapper.readValue(byteArray, Map.class)).thenReturn(map);
        when(cookieStore.createCookie("johnsmith", "hash")).thenReturn(expected);

        // Call test class
        Cookie actual = testClass.createCookie(requestContext);

        // Assert
        assertEquals(expected, actual);
    }
}
