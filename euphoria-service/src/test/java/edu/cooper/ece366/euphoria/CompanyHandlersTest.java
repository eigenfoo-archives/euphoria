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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CompanyHandlersTest {

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
    Request request;
    @Mock
    ByteString requestPayloadByteString;

    private CompanyHandlers testClass;

    @Before
    public void setup() {
        testClass = new CompanyHandlers(objectMapper, config);
    }

    @Test
    public void getCompany() throws SQLException {
        // Setup variables
        Company expected = new CompanyBuilder()
                .companyId(1)
                .name("Apple")
                .website("apple.com")
                .description("Macintoshes.")
                .build();

        // Mock dependencies and inputs
        when(config.getString("mysql.jdbc")).thenReturn("jdbc:mysql://localhost:3306/euphoria");
        when(config.getString("mysql.user")).thenReturn("euphoria");
        when(config.getString("mysql.password")).thenReturn("euphoria");
        when(rc.pathArgs()).thenReturn(Collections.singletonMap("companyId", "1"));

        // Call test class
        Company actual = testClass.getCompany(rc).get(0);

        // Assert and verify
        assertEquals(expected, actual);
    }

    @Test
    public void createCompany() throws IOException {
        // Setup variables
        Company company = new CompanyBuilder()
                .companyId(3)
                .name("namefield")
                .website("websitefield")
                .description("descriptonfield")
                .build();
        List<Company> expected = Collections.singletonList(company);
        byte[] byteArray = new byte[0];

        Map<String, Object> map = new HashMap<>();
        map.put("companyId", 1);
        map.put("name", "Apple");
        map.put("website", "apple.com");
        map.put("description", "Macintoshes.");

        when(config.getString("mysql.jdbc")).thenReturn("jdbc:mysql://localhost:3306/euphoria");
        when(config.getString("mysql.user")).thenReturn("euphoria");
        when(config.getString("mysql.password")).thenReturn("euphoria");
        when(rc.request()).thenReturn(request);
        when(request.payload()).thenReturn(Optional.of(requestPayloadByteString));
        when(requestPayloadByteString.toByteArray()).thenReturn(byteArray);
        when(objectMapper.readValue(byteArray, Map.class)).thenReturn(map);

        // Call test class
        List<Company> actual = testClass.createCompany(rc);

        // Assert and verify
        assertEquals(expected, actual);
    }
}
