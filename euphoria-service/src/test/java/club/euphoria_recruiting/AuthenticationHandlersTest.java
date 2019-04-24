package club.euphoria_recruiting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.Request;
import com.spotify.apollo.RequestContext;
import club.euphoria_recruiting.handler.AuthenticationHandlers;
import club.euphoria_recruiting.model.Application;
import club.euphoria_recruiting.model.Authentication;
import club.euphoria_recruiting.model.AuthenticationBuilder;
import club.euphoria_recruiting.store.model.AuthenticationStore;
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
public class AuthenticationHandlersTest {
    @Mock
    ObjectMapper objectMapper;
    @Mock
    AuthenticationStore authenticationStore;
    @Mock
    RequestContext requestContext;
    @Mock
    Request request;
    @Mock
    ByteString requestPayloadByteString;

    private AuthenticationHandlers testClass;

    @Before
    public void setup() {
        testClass = new AuthenticationHandlers(objectMapper, authenticationStore);
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
        when(requestContext.pathArgs()).thenReturn(map);
        when(authenticationStore.getAuthentication("johnnyappleseed", "hash"))
                .thenReturn(expected);

        // Call test class
        Authentication actual = testClass.getAuthentication(requestContext);

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

        when(requestContext.request()).thenReturn(request);
        when(request.payload()).thenReturn(Optional.of(requestPayloadByteString));
        when(requestPayloadByteString.toByteArray()).thenReturn(byteArray);
        when(objectMapper.readValue(byteArray, Authentication.class)).thenReturn(authentication);
        when(authenticationStore.createAuthentication(1, "johnnyappleseed", "hash", true))
                .thenReturn(Collections.emptyList());

        // Call test class
        List<Authentication> actual = testClass.createAuthentication(requestContext);

        // Assert
        assertEquals(expected, actual);
    }
}
