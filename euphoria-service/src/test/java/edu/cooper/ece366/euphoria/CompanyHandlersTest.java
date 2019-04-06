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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CompanyHandlersTest {

    @Mock
    ObjectMapper objectMapper;
    @Mock
    RequestContext rc;
    @Mock
    PreparedStatement ps;
    @Mock
    ResultSet rs;

    private CompanyHandlers testClass;

    @Before
    public void setup() {
        testClass = new CompanyHandlers(objectMapper);
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
    public void createCompany() throws SQLException {
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
}
