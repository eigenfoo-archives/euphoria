package club.euphoria_recruiting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.Request;
import com.spotify.apollo.RequestContext;
import club.euphoria_recruiting.handler.PostingHandlers;
import club.euphoria_recruiting.model.Posting;
import club.euphoria_recruiting.model.PostingBuilder;
import club.euphoria_recruiting.store.model.PostingStore;
import club.euphoria_recruiting.utils.Industry;
import club.euphoria_recruiting.utils.Location;
import club.euphoria_recruiting.utils.SkillLevel;
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
public class PostingHandlersTest {
    @Mock
    ObjectMapper objectMapper;
    @Mock
    PostingStore postingStore;
    @Mock
    RequestContext requestContext;
    @Mock
    Request request;
    @Mock
    ByteString requestPayloadByteString;

    private PostingHandlers testClass;

    @Before
    public void setup() {
        testClass = new PostingHandlers(objectMapper, postingStore);
    }

    @Test
    public void getPostingAndGetAllPostings() {
        // Setup variables
        Posting expected = new PostingBuilder()
                .postingId(1)
                .companyId(123)
                .jobTitle("Underwater Basket Weaver")
                .description("What it sounds like.")
                .location(Location.NEWYORK)
                .industry(Industry.FINANCE)
                .skillLevel(SkillLevel.INTERNSHIP)
                .dateCreated("2019-04-16 02:12:22")
                .build();

        // Mock dependencies and inputs
        when(requestContext.pathArgs()).thenReturn(Collections.singletonMap("postingId", "1"));
        when(postingStore.getPosting("1")).thenReturn(expected);
        when(postingStore.getAllPostings()).thenReturn(Collections.singletonList(expected));

        // Call test class
        Posting actualSingle = testClass.getPosting(requestContext);
        List<Posting> actualAll = testClass.getAllPostings(requestContext);

        // Assert and verify
        assertEquals(expected, actualSingle);
        assertEquals(Collections.singletonList(expected), actualAll);
        verifyZeroInteractions(objectMapper);
    }

    @Test
    public void getPostingForCompany() {
        // Setup variables
        Posting expected = new PostingBuilder()
                .postingId(1)
                .companyId(123)
                .jobTitle("Underwater Basket Weaver")
                .description("What it sounds like.")
                .location(Location.NEWYORK)
                .industry(Industry.FINANCE)
                .skillLevel(SkillLevel.INTERNSHIP)
                .dateCreated("2019-04-16 02:12:22")
                .build();

        // Mock dependencies and inputs
        when(requestContext.pathArgs()).thenReturn(Collections.singletonMap("companyId", "1"));
        when(postingStore.getPostingsForCompany("1")).thenReturn(Collections.singletonList(expected));

        // Call test class
        List<Posting> actual = testClass.getPostingsForCompany(requestContext);

        // Assert and verify
        assertEquals(Collections.singletonList(expected), actual);
        verifyZeroInteractions(objectMapper);
    }

    @Test
    public void searchPostings() {
        // Setup variables
        Posting expected = new PostingBuilder()
                .postingId(1)
                .companyId(123)
                .jobTitle("Underwater Basket Weaver")
                .description("What it sounds like.")
                .location(Location.NEWYORK)
                .industry(Industry.FINANCE)
                .skillLevel(SkillLevel.INTERNSHIP)
                .dateCreated("2019-04-16 02:12:22")
                .build();

        // Mock dependencies and inputs
        Map<String, String> map = new HashMap<>();
        map.put("location", "NEWYORK");
        map.put("industry", "FINANCE");
        map.put("skillLevel", "INTERNSHIP");

        when(requestContext.pathArgs()).thenReturn(map);
        when(postingStore.searchPostings("NEWYORK", "FINANCE", "INTERNSHIP"))
                .thenReturn(Collections.singletonList(expected));

        // Call test class
        Posting actualSingle = testClass.searchPostings(requestContext).get(0);

        // Assert and verify
        assertEquals(expected, actualSingle);
    }

    @Test
    public void createPostingAndEditPosting() throws IOException {
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

        when(requestContext.request()).thenReturn(request);
        when(request.payload()).thenReturn(Optional.of(requestPayloadByteString));
        when(requestPayloadByteString.toByteArray()).thenReturn(byteArray);
        when(objectMapper.readValue(byteArray, Map.class)).thenReturn(map);
        when(postingStore.createPosting("1", "Underwater Basket Weaver", "What it sounds like.", Location.NEWYORK, Industry.FINANCE, SkillLevel.INTERNSHIP))
                .thenReturn(Collections.emptyList());
        when(postingStore.editPosting("1", "Underwater Basket Weaver", "What it sounds like.", Location.NEWYORK, Industry.FINANCE, SkillLevel.INTERNSHIP))
                .thenReturn(Collections.emptyList());

        // Call test class
        List<Posting> actualCreated = testClass.createPosting(requestContext);
        List<Posting> actualEdited = testClass.editPosting(requestContext);

        // Assert
        assertEquals(expected, actualCreated);
        assertEquals(expected, actualEdited);
    }

    @Test
    public void deletePosting() {
        // Setup variables
        List<Posting> expected = Collections.emptyList();

        // Mock dependencies and inputs
        when(requestContext.pathArgs()).thenReturn(Collections.singletonMap("postingId", "1"));
        when(postingStore.deletePosting("1")).thenReturn(Collections.emptyList());

        // Call test class
        List<Posting> actual = testClass.deletePosting(requestContext);

        // Assert
        assertEquals(expected, actual);
    }
}
