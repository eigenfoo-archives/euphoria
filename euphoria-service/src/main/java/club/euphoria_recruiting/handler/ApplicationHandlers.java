package club.euphoria_recruiting.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.route.*;
import edu.cooper.ece366.euphoria.model.Application;
import edu.cooper.ece366.euphoria.store.model.ApplicationStore;
import okio.ByteString;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ApplicationHandlers implements RouteProvider {
    private final ObjectMapper objectMapper;
    private final ApplicationStore applicationStore;

    public ApplicationHandlers(final ObjectMapper objectMapper, ApplicationStore applicationStore) {
        this.objectMapper = objectMapper;
        this.applicationStore = applicationStore;
    }

    @Override
    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
        return Stream.of(
                Route.sync("GET", "/api/application/<applicationId>", this::getApplication)
                        .withMiddleware(jsonMiddleware()),
                Route.sync("GET", "/api/application/posting/<postingId>", this::getApplicationsForPosting)
                        .withMiddleware(jsonMiddleware()),
                Route.sync("POST", "/api/application", this::createApplication)
                        .withMiddleware(jsonMiddleware())
        );
    }

    @VisibleForTesting
    public Application getApplication(final RequestContext rc) {
        return applicationStore.getApplication(rc.pathArgs().get("applicationId"));
    }

    @VisibleForTesting
    public List<Application> getApplicationsForPosting(final RequestContext rc) {
        return applicationStore.getApplicationsForPosting(rc.pathArgs().get("postingId"));
    }

    @VisibleForTesting
    public List<Application> createApplication(final RequestContext rc) {
        String postingId = null;
        String userId = null;
        String resume = null;
        String coverLetter = null;
        boolean success = false;
        try {
            byte[] requestBytes = rc.request().payload().get().toByteArray();
            Map jsonMap = objectMapper.readValue(requestBytes, Map.class);
            postingId = jsonMap.get("postingId").toString();
            userId = jsonMap.get("userId").toString();
            resume = jsonMap.get("resume").toString();
            coverLetter = jsonMap.get("coverLetter").toString();
            success = true;
        } catch (IOException ex) {
            System.out.println(ex);
        }

        if (success)
            return applicationStore.createApplication(postingId, userId, resume, coverLetter);
        else
            return null;
    }

    private <T> Middleware<AsyncHandler<T>, AsyncHandler<Response<ByteString>>> jsonMiddleware() {
        return JsonSerializerMiddlewares.<T>jsonSerialize(objectMapper.writer())
                .and(Middlewares::httpPayloadSemantics)
                .and(responseAsyncHandler -> requestContext ->
                        responseAsyncHandler.invoke(requestContext)
                                .thenApply(response -> response.withHeader("Access-Control-Allow-Origin", "*")));
    }
}