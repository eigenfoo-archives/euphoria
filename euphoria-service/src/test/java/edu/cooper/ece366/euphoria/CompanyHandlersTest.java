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
    byte[] requestBytes;

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
                .name("BigCorp")
                .website("bigcorp.com")
                .description("The biggest of corps.")
                .build();

        // Mock dependencies and inputs
        when(rc.pathArgs()).thenReturn(Collections.singletonMap("companyId", "1"));
        when(rs.getInt("companyId")).thenReturn(expected.companyId());
        when(rs.getString("name")).thenReturn(expected.name());
        when(rs.getString("description")).thenReturn(expected.description());
        when(ps.executeQuery()).thenReturn(rs);

        // Call test class
        Company actual = testClass.getCompany(rc).get(0);

        // Assert and verify
        assertEquals(expected, actual);

        verifyZeroInteractions(objectMapper);
    }

    @Test
    public void createCompany() throws IOException {
        // Setup variables
        List<Company> expected = Collections.emptyList();
        byte[] foo = new byte[0];

        // Mock dependencies and inputs
        Map<String, Object> request = new HashMap<String, Object>();
        request.put("name", "BigCorp");
        request.put("website", "bigcorp.com");
        request.put("description", "The biggest of corps.");
        when(rc.request().payload().get().toByteArray()).thenReturn(foo);
        when(objectMapper.readValue(requestBytes, Map.class)).thenReturn(request);

        // Call test class
        List<Company> actual = testClass.createCompany(rc);

        // Assert and verify
        assertEquals(expected, actual);

        verifyZeroInteractions(objectMapper);
    }
}
