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
public class PostingHandlersTest {

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

    private PostingHandlers testClass;

    @Before
    public void setup() {
        testClass = new PostingHandlers(objectMapper, config);
    }

    @Test
    public void getPostingAndAllPostings() {
        // Setup variables
        Posting expected = new PostingBuilder()
                .postingId(1)
                .companyId(123)
                .jobTitle("Underwater Basket Weaver")
                .description("What it sounds like.")
                .location(Location.valueOf("NEWYORK"))
                .industry(Industry.valueOf("FINANCE"))
                .skillLevel(SkillLevel.valueOf("INTERNSHIP"))
                .dateCreated("2019-04-16 02:12:22")
                .build();

        // Mock dependencies and inputs
        when(config.getString("mysql.jdbc")).thenReturn("jdbc:mysql://localhost:3306/euphoria");
        when(config.getString("mysql.user")).thenReturn("euphoria");
        when(config.getString("mysql.password")).thenReturn("euphoria");
        when(rc.pathArgs()).thenReturn(Collections.singletonMap("postingId", "1"));

        // Call test class
        Posting actualSingle = testClass.getPosting(rc).get(0);
        Posting actualAll = testClass.getAllPostings(rc).get(0);

        // Assert and verify
        assertEquals(expected, actualSingle);
        assertEquals(expected, actualAll);
    }

    @Test
    public void searchPostings() {
        // Setup variables
        Posting expected = new PostingBuilder()
                .postingId(1)
                .companyId(123)
                .jobTitle("Underwater Basket Weaver")
                .description("What it sounds like.")
                .location(Location.valueOf("NEWYORK"))
                .industry(Industry.valueOf("FINANCE"))
                .skillLevel(SkillLevel.valueOf("INTERNSHIP"))
                .dateCreated("2019-04-16 02:12:22")
                .build();

        // Mock dependencies and inputs
        Map<String, String> rcMap = new HashMap<>();
        rcMap.put("location", "NEWYORK");
        rcMap.put("industry", "FINANCE");
        rcMap.put("skillLevel", "INTERNSHIP");
        when(rc.pathArgs()).thenReturn(rcMap);
        when(config.getString("mysql.jdbc")).thenReturn("jdbc:mysql://localhost:3306/euphoria");
        when(config.getString("mysql.user")).thenReturn("euphoria");
        when(config.getString("mysql.password")).thenReturn("euphoria");

        // Call test class
        Posting actualSingle = testClass.searchPostings(rc).get(0);

        // Assert and verify
        assertEquals(expected, actualSingle);
    }

    @Test
    public void createAndEditPosting() throws IOException {
        // Setup variables
        List<Posting> expected = Collections.emptyList();
        byte[] byteArray = new byte[0];

        // Mock dependencies and inputs
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("companyId", 1);
        map.put("postingId", 1);
        map.put("jobTitle", "Underwater Basket Weaver");
        map.put("description", "What it sounds like.");
        map.put("location", "NEWYORK");
        map.put("industry", "FINANCE");
        map.put("skillLevel", "INTERNSHIP");

        when(config.getString("mysql.jdbc")).thenReturn("jdbc:mysql://localhost:3306/euphoria");
        when(config.getString("mysql.user")).thenReturn("euphoria");
        when(config.getString("mysql.password")).thenReturn("euphoria");
        when(rc.request()).thenReturn(request);
        when(request.payload()).thenReturn(Optional.of(requestPayloadByteString));
        when(requestPayloadByteString.toByteArray()).thenReturn(byteArray);
        when(objectMapper.readValue(byteArray, Map.class)).thenReturn(map);

        // Call test class
        List<Posting> actualCreated = testClass.createPosting(rc);
        List<Posting> actualEdited = testClass.editPosting(rc);

        // Assert and verify
        assertEquals(expected, actualCreated);
        assertEquals(expected, actualEdited);
    }

    @Test
    public void deletePosting() {
        // Setup variables
        List<Posting> expected = Collections.emptyList();

        // Mock dependencies and inputs
        when(rc.pathArgs()).thenReturn(Collections.singletonMap("postingId", "1"));

        // Call test class
        List<Posting> actual = testClass.deletePosting(rc);

        // Assert and verify
        assertEquals(expected, actual);
    }
}
