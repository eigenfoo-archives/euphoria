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
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserHandlersTest {

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

    private UserHandlers testClass;

    @Before
    public void setup() {
        testClass = new UserHandlers(objectMapper, config);
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
        when(config.getString("mysql.jdbc")).thenReturn("jdbc:mysql://localhost:3306/euphoria");
        when(config.getString("mysql.user")).thenReturn("euphoria");
        when(config.getString("mysql.password")).thenReturn("euphoria");
        when(rc.pathArgs()).thenReturn(Collections.singletonMap("userId", "1"));

        // Call test class
        User actual = testClass.getUser(rc).get(0);

        // Assert and verify
        assertEquals(expected, actual);
    }

    @Test
    public void createUser() throws IOException {
        // Setup variables
        User user = new UserBuilder()
                .userId(4)
                .name("NA")
                .email("NA")
                .phoneNumber("NA")
                .educationLevel(EducationLevel.NOHIGHSCHOOL)
                .description("NA")
                .build();
        List<User> expected = Collections.singletonList(user);
        byte[] byteArray = new byte[0];

        Map<String, Object> map = new HashMap<>();
        map.put("userId", 1);
        map.put("name", "John Smith");
        map.put("email", "john@smith.com");
        map.put("phoneNumber", "123-456-7890");
        map.put("educationLevel", "BACHELORS");
        map.put("description", "Am engineer pls hire.");

        when(config.getString("mysql.jdbc")).thenReturn("jdbc:mysql://localhost:3306/euphoria");
        when(config.getString("mysql.user")).thenReturn("euphoria");
        when(config.getString("mysql.password")).thenReturn("euphoria");
        when(rc.request()).thenReturn(request);
        when(request.payload()).thenReturn(Optional.of(requestPayloadByteString));
        when(requestPayloadByteString.toByteArray()).thenReturn(byteArray);
        when(objectMapper.readValue(byteArray, Map.class)).thenReturn(map);

        // Call test class
        List<User> actual = testClass.createUser(rc);

        // Assert and verify
        assertEquals(expected, actual);
    }
}
