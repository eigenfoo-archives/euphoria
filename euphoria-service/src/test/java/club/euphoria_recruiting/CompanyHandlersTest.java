package edu.cooper.ece366.euphoria;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.Request;
import com.spotify.apollo.RequestContext;
import edu.cooper.ece366.euphoria.handler.CompanyHandlers;
import edu.cooper.ece366.euphoria.model.Company;
import edu.cooper.ece366.euphoria.model.CompanyBuilder;
import edu.cooper.ece366.euphoria.store.model.CompanyStore;
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
public class CompanyHandlersTest {
    @Mock
    ObjectMapper objectMapper;
    @Mock
    CompanyStore companyStore;
    @Mock
    RequestContext requestContext;
    @Mock
    Request request;
    @Mock
    ByteString requestPayloadByteString;

    private CompanyHandlers testClass;

    @Before
    public void setup() {
        testClass = new CompanyHandlers(objectMapper, companyStore);
    }

    @Test
    public void getCompany() {
        // Setup variables
        Company expected = new CompanyBuilder()
                .companyId(1)
                .name("Apple")
                .website("apple.com")
                .description("Macintoshes.")
                .build();

        // Mock dependencies and inputs
        when(requestContext.pathArgs()).thenReturn(Collections.singletonMap("companyId", "1"));
        when(companyStore.getCompany("1")).thenReturn(expected);

        // Call test class
        Company actual = testClass.getCompany(requestContext);

        // Assert and verify
        assertEquals(expected, actual);
        verifyZeroInteractions(objectMapper);
    }

    @Test
    public void createCompany() throws IOException {
        // Setup variables
        Company expected = new CompanyBuilder()
                .companyId(3)
                .name("NA")
                .website("NA")
                .description("NA")
                .build();
        byte[] byteArray = new byte[0];

        Map<String, Object> map = new HashMap<>();
        map.put("companyId", 1);
        map.put("name", "Apple");
        map.put("website", "apple.com");
        map.put("description", "Macintoshes.");

        when(requestContext.request()).thenReturn(request);
        when(request.payload()).thenReturn(Optional.of(requestPayloadByteString));
        when(requestPayloadByteString.toByteArray()).thenReturn(byteArray);
        when(objectMapper.readValue(byteArray, Map.class)).thenReturn(map);
        when(companyStore.createCompany("Apple", "apple.com", "Macintoshes."))
                .thenReturn(expected);

        // Call test class
        Company actual = testClass.createCompany(requestContext);

        // Assert
        assertEquals(expected, actual);
    }
}
