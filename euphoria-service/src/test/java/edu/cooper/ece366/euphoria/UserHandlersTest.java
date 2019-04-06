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
public class UserHandlersTest {

    @Mock
    ObjectMapper objectMapper;
    @Mock
    RequestContext rc;
    @Mock
    PreparedStatement ps;
    @Mock
    ResultSet rs;

    private UserHandlers testClass;

    @Before
    public void setup() {
        testClass = new UserHandlers(objectMapper);
    }

    @Test
    public void getUser() throws SQLException {
        // Setup variables
        User expected = new UserBuilder()
                .userId(1)
                .name("John Smith")
                .email("john@smith.com")
                .phoneNumber("1234567890")
                .educationLevel(EducationLevel.valueOf("BACHELORS"))
                .description("Am engineer pls hire.")
                .build();

        // Mock dependencies and inputs
        when(rc.pathArgs()).thenReturn(Collections.singletonMap("userId", "1"));
        when(rs.getInt("userId")).thenReturn(expected.userId());
        when(rs.getString("name")).thenReturn(expected.name());
        when(rs.getString("email")).thenReturn(expected.email());
        when(rs.getString("phoneNumber")).thenReturn(expected.phoneNumber());
        when(rs.getString("description")).thenReturn(expected.description());
        when(ps.executeQuery()).thenReturn(rs);

        // Call test class
        User actual = testClass.getUser(rc).get(0);

        // Assert and verify
        assertEquals(expected, actual);

        verifyZeroInteractions(objectMapper);
    }
}
