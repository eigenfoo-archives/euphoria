package edu.cooper.ece366.euphoria.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.route.*;
import edu.cooper.ece366.euphoria.model.Company;
import edu.cooper.ece366.euphoria.store.model.CompanyStore;
import okio.ByteString;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

public class CompanyHandlers implements RouteProvider {
    private final ObjectMapper objectMapper;
    private final CompanyStore companyStore;

    public CompanyHandlers(final ObjectMapper objectMapper, CompanyStore companyStore) {
        this.objectMapper = objectMapper;
        this.companyStore = companyStore;
    }

    @Override
    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
        return Stream.of(
                Route.sync("GET", "/api/company/<companyId>", this::getCompany),
                Route.sync("POST", "/api/company", this::createCompany)
        ).map(r -> r.withMiddleware(jsonMiddleware()));
    }

    @VisibleForTesting
    public Company getCompany(final RequestContext rc) {
        return companyStore.getCompany(rc.pathArgs().get("companyId"));
    }

    @VisibleForTesting
    public Company createCompany(final RequestContext rc) {
        String name, website, description;
        name = null;
        website = null;
        description = null;
        boolean success = false;
        try {
            byte[] requestBytes = rc.request().payload().get().toByteArray();
            Map jsonMap = objectMapper.readValue(requestBytes, Map.class);
            name = jsonMap.get("name").toString();
            website = jsonMap.get("website").toString();
            description = jsonMap.get("description").toString();
            success = true;
        } catch (IOException ex) {
            System.out.println(ex);
        }

        if (success)
            return companyStore.createCompany(name, website, description);
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
