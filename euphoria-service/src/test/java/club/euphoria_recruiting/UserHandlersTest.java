package club.euphoria_recruiting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.Request;
import com.spotify.apollo.RequestContext;
import club.euphoria_recruiting.handler.UserHandlers;
import club.euphoria_recruiting.model.User;
import club.euphoria_recruiting.model.UserBuilder;
import club.euphoria_recruiting.store.model.UserStore;
import club.euphoria_recruiting.utils.EducationLevel;
import okio.ByteString;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserHandlersTest {
    @Mock
    ObjectMapper objectMapper;
    @Mock
    UserStore userStore;
    @Mock
    RequestContext requestContext;
    @Mock
    Request request;
    @Mock
    ByteString requestPayloadByteString;

    private UserHandlers testClass;

    @Before
    public void setup() {
        testClass = new UserHandlers(objectMapper, userStore);
    }

    @Test
    public void getUser() {
        // Setup variables
        User expected = new UserBuilder()
                .userId(1)
                .name("Johnny Appleseed")
                .email("john@appleseed.com")
                .phoneNumber("123-456-7890")
                .educationLevel(EducationLevel.JD)
                .description("I like Macintoshes.")
                .build();

        // Mock dependencies and inputs
        when(requestContext.pathArgs()).thenReturn(Collections.singletonMap("userId", "1"));
        when(userStore.getUser("1")).thenReturn(expected);

        // Call test class
        User actual = testClass.getUser(requestContext);

        // Assert and verify
        assertEquals(expected, actual);
        verifyZeroInteractions(objectMapper);
    }

    @Test
    public void createUser() throws IOException {
        // Setup variables
        User expected = new UserBuilder()
                .userId(4)
                .name("NA")
                .email("NA")
                .phoneNumber("NA")
                .educationLevel(EducationLevel.NOHIGHSCHOOL)
                .description("NA")
                .build();
        byte[] byteArray = new byte[0];

        Map<String, Object> map = new HashMap<>();
        map.put("userId", 1);
        map.put("name", "John Smith");
        map.put("email", "john@smith.com");
        map.put("phoneNumber", "123-456-7890");
        map.put("educationLevel", "BACHELORS");
        map.put("description", "Am engineer pls hire.");

        when(requestContext.request()).thenReturn(request);
        when(request.payload()).thenReturn(Optional.of(requestPayloadByteString));
        when(requestPayloadByteString.toByteArray()).thenReturn(byteArray);
        when(objectMapper.readValue(byteArray, Map.class)).thenReturn(map);
        when(userStore.createUser("John Smith", "john@smith.com", "123-456-7890", EducationLevel.BACHELORS, "Am engineer pls hire."))
                .thenReturn(expected);

        // Call test class
        User actual = testClass.createUser(requestContext);

        // Assert
        assertEquals(expected, actual);
    }
}
