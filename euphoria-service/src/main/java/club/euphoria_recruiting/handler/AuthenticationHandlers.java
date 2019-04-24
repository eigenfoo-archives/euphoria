package club.euphoria_recruiting.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.route.*;
import club.euphoria_recruiting.model.Authentication;
import club.euphoria_recruiting.store.model.AuthenticationStore;
import okio.ByteString;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public class AuthenticationHandlers implements RouteProvider {
    private final ObjectMapper objectMapper;
    private final AuthenticationStore authenticationStore;

    public AuthenticationHandlers(final ObjectMapper objectMapper, AuthenticationStore authenticationStore) {
        this.objectMapper = objectMapper;
        this.authenticationStore = authenticationStore;
    }

    @Override
    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
        return Stream.of(
                Route.sync("GET", "/api/authentication/<username>/<passwordHash>", this::getAuthentication)
                        .withMiddleware(jsonMiddleware()),
                Route.sync("POST", "/api/authentication", this::createAuthentication)
                        .withMiddleware(jsonMiddleware())
        );
    }

    @VisibleForTesting
    public Authentication getAuthentication(final RequestContext rc) {
        return authenticationStore.getAuthentication(rc.pathArgs().get("username"), rc.pathArgs().get("passwordHash"));
    }

    @VisibleForTesting
    public List<Authentication> createAuthentication(final RequestContext rc) {
        Integer id = null;
        String username = null;
        String passwordHash = null;
        Boolean isUser = true;
        boolean success = false;
        try {
            byte[] requestBytes = rc.request().payload().get().toByteArray();
            Authentication authentication = objectMapper.readValue(requestBytes, Authentication.class);
            id = authentication.id();
            username = authentication.username();
            passwordHash = authentication.passwordHash();
            isUser = authentication.isUser();
            success = true;
        } catch (IOException ex) {
            System.out.println(ex);
        }

        if (success)
            return authenticationStore.createAuthentication(id, username, passwordHash, isUser);
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
