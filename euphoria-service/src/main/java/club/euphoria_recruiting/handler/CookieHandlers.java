package club.euphoria_recruiting.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.route.*;
import edu.cooper.ece366.euphoria.model.Cookie;
import edu.cooper.ece366.euphoria.store.model.CookieStore;
import okio.ByteString;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

public class CookieHandlers implements RouteProvider {
    private final ObjectMapper objectMapper;
    private final CookieStore cookieStore;

    public CookieHandlers(final ObjectMapper objectMapper, CookieStore cookieStore) {
        this.objectMapper = objectMapper;
        this.cookieStore = cookieStore;
    }

    @Override
    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
        return Stream.of(
                Route.sync("GET", "/api/cookie/<cookieCheck>", this::getCookie),
                Route.sync("POST", "/api/cookie", this::createCookie)
        ).map(r -> r.withMiddleware(jsonMiddleware()));
    }

    @VisibleForTesting
    public Cookie getCookie(final RequestContext rc) {
        return cookieStore.getCookie(rc.pathArgs().get("cookieCheck"));
    }

    @VisibleForTesting
    public Cookie createCookie(final RequestContext rc) {
        String username = null;
        String passwordHash = null;
        boolean success = false;
        try {
            Map jsonMap = objectMapper.readValue(rc.request().payload().get().toByteArray(), Map.class);
            username = jsonMap.get("username").toString();
            passwordHash = jsonMap.get("passwordHash").toString();
            success = true;
        } catch (IOException ex) {
            System.out.println(ex);
        }

        if (success)
            return cookieStore.createCookie(username, passwordHash);
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
