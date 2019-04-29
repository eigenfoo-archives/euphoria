package club.euphoria_recruiting.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.route.*;
import club.euphoria_recruiting.model.Posting;
import club.euphoria_recruiting.store.model.PostingStore;
import club.euphoria_recruiting.utils.Industry;
import club.euphoria_recruiting.utils.Location;
import club.euphoria_recruiting.utils.SkillLevel;
import okio.ByteString;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class PostingHandlers implements RouteProvider {
    private final ObjectMapper objectMapper;
    private final PostingStore postingStore;

    public PostingHandlers(final ObjectMapper objectMapper, PostingStore postingStore) {
        this.objectMapper = objectMapper;
        this.postingStore = postingStore;
    }

    @Override
    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
        return Stream.of(
                Route.sync("GET", "/api/posting/<postingId>", this::getPosting).withMiddleware(jsonMiddleware()),
                Route.sync("GET", "/api/posting/all", this::getAllPostings).withMiddleware(jsonMiddleware()),
                Route.sync("GET", "/api/posting/random", this::getRandomPostings).withMiddleware(jsonMiddleware()),
                Route.sync("GET", "/api/posting/company/<companyId>", this::getPostingsForCompany).withMiddleware(jsonMiddleware()),
                Route.sync("GET", "/api/posting/<location>/<industry>/<skillLevel>", this::searchPostings).withMiddleware(jsonMiddleware()),
                Route.sync("POST", "/api/posting/", this::createPosting).withMiddleware(jsonMiddleware()),
                Route.sync("OPTIONS", "/api/posting/", rc -> "ok").withMiddleware(jsonMiddleware()),
                Route.sync("PUT", "/api/posting/", this::editPosting).withMiddleware(jsonMiddleware()),
                Route.sync("OPTIONS", "/api/posting/", rc -> "ok").withMiddleware(jsonMiddleware()),
                Route.sync("DELETE", "/api/posting/<postingId>", this::deletePosting).withMiddleware(jsonMiddleware()),
                Route.sync("OPTIONS", "/api/posting/<postingId>", rc -> "ok").withMiddleware(jsonMiddleware())
        );
    }

    @VisibleForTesting
    public Posting getPosting(final RequestContext rc) {
        return postingStore.getPosting(rc.pathArgs().get("postingId"));
    }

    @VisibleForTesting
    public List<Posting> searchPostings(final RequestContext rc) {
        String location = "%";
        String industry = "%";
        String skillLevel = "%";
        boolean success1 = false;
        boolean success2 = false;
        boolean success3 = false;
        try {
            location = Location.valueOf(rc.pathArgs().get("location")).toString();
            success1 = true;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        try {
            industry = Industry.valueOf(rc.pathArgs().get("industry")).toString();
            success2 = true;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        try {
            skillLevel = SkillLevel.valueOf(rc.pathArgs().get("skillLevel")).toString();
            success3 = true;
        } catch (Exception ex) {
            System.out.println(ex);
        }

        //if curl has some fields blank, still ok
        if (String.valueOf(rc.pathArgs().get("location")).isEmpty())
            success1 = true;
        if (String.valueOf(rc.pathArgs().get("industry")).isEmpty())
            success2 = true;
        if (String.valueOf(rc.pathArgs().get("skillLevel")).isEmpty())
            success3 = true;

        if (success1 && success2 && success3)
            return postingStore.searchPostings(location, industry, skillLevel);
        else
            return null;  //if misspell an ENUM constant
    }

    @VisibleForTesting
    public List<Posting> getAllPostings(final RequestContext rc) {
        return postingStore.getAllPostings();
    }

    @VisibleForTesting
    public List<Posting> getRandomPostings(final RequestContext rc) {
        return postingStore.getRandomPostings();
    }

    @VisibleForTesting
    public List<Posting> getPostingsForCompany(final RequestContext rc) {
        return postingStore.getPostingsForCompany(rc.pathArgs().get("companyId"));
    }

    @VisibleForTesting
    public List<Posting> createPosting(final RequestContext rc) {
        String companyId = null;
        String jobTitle = null;
        String description = null;
        Location location = null;
        Industry industry = null;
        SkillLevel skillLevel = null;
        boolean success = false;
        try {
            byte[] requestBytes = rc.request().payload().get().toByteArray();
            Map jsonMap = objectMapper.readValue(requestBytes, Map.class);
            companyId = jsonMap.get("companyId").toString();
            jobTitle = jsonMap.get("jobTitle").toString();
            description = jsonMap.get("description").toString();
            location = Location.valueOf(jsonMap.get("location").toString());
            industry = Industry.valueOf(jsonMap.get("industry").toString());
            skillLevel = SkillLevel.valueOf(jsonMap.get("skillLevel").toString());
            success = true;
        } catch (IOException ex) {
            System.out.println(ex);
        }

        if (success)
            return postingStore.createPosting(companyId, jobTitle, description, location, industry, skillLevel);
        else
            return null;
    }

    @VisibleForTesting
    public List<Posting> editPosting(final RequestContext rc) {
        String postingId = null;
        String jobTitle = null;
        String description = null;
        Location location = null;
        Industry industry = null;
        SkillLevel skillLevel = null;
        boolean success = false;
        try {
            byte[] requestBytes = rc.request().payload().get().toByteArray();
            Map jsonMap = objectMapper.readValue(requestBytes, Map.class);
            postingId = jsonMap.get("postingId").toString();
            jobTitle = jsonMap.get("jobTitle").toString();
            description = jsonMap.get("description").toString();
            location = Location.valueOf(jsonMap.get("location").toString());
            industry = Industry.valueOf(jsonMap.get("industry").toString());
            skillLevel = SkillLevel.valueOf(jsonMap.get("skillLevel").toString());
            success = true;
        } catch (IOException ex) {
            System.out.println(ex);
        }

        if (success)
            return postingStore.editPosting(postingId, jobTitle, description, location, industry, skillLevel);
        else
            return null;
    }

    @VisibleForTesting
    public List<Posting> deletePosting(final RequestContext rc) {
        return postingStore.deletePosting(rc.pathArgs().get("postingId"));
    }

    private <T> Middleware<AsyncHandler<T>, AsyncHandler<Response<ByteString>>> jsonMiddleware() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        headers.put("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with, session-token");
        headers.put("Access-Control-Max-Age", "3600");

        return JsonSerializerMiddlewares.<T>jsonSerialize(objectMapper.writer())
                .and(Middlewares::httpPayloadSemantics)
                .and(responseAsyncHandler -> requestContext ->
                        responseAsyncHandler.invoke(requestContext)
                                .thenApply(response -> response.withHeaders(headers)));
    }
}
