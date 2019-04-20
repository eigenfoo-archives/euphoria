package edu.cooper.ece366.euphoria.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.route.*;
import edu.cooper.ece366.euphoria.model.User;
import edu.cooper.ece366.euphoria.store.model.UserStore;
import edu.cooper.ece366.euphoria.utils.EducationLevel;
import okio.ByteString;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

public class UserHandlers implements RouteProvider {
    private final ObjectMapper objectMapper;
    private final UserStore userStore;

    public UserHandlers(final ObjectMapper objectMapper, UserStore userStore) {
        this.objectMapper = objectMapper;
        this.userStore = userStore;
    }

    @Override
    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
        return Stream.of(
                Route.sync("GET", "/api/user/<userId>", this::getUser),
                Route.sync("POST", "/api/user", this::createUser)
        ).map(r -> r.withMiddleware(jsonMiddleware()));
    }

    @VisibleForTesting
    User getUser(final RequestContext rc) {
        return userStore.getUser(rc.pathArgs().get("userId"));
    }

    @VisibleForTesting
    User createUser(final RequestContext rc) {
        String name, email, phoneNumber, description;
        name = null;
        email = null;
        phoneNumber = null;
        description = null;
        EducationLevel educationLevel = null;
        boolean success = false;
        try {
            byte[] requestBytes = rc.request().payload().get().toByteArray();
            Map jsonMap = objectMapper.readValue(requestBytes, Map.class);
            name = jsonMap.get("name").toString();
            email = jsonMap.get("email").toString();
            phoneNumber = jsonMap.get("phoneNumber").toString();
            educationLevel = EducationLevel.valueOf(jsonMap.get("educationLevel").toString());
            description = jsonMap.get("description").toString();
            success = true;
        } catch (IOException ex) {
            System.out.println(ex);
        }

        if (success) {
            return userStore.createUser(name, email, phoneNumber, educationLevel, description);
        }
        else {
            return null;
        }
    }

    private <T> Middleware<AsyncHandler<T>, AsyncHandler<Response<ByteString>>> jsonMiddleware() {
        return JsonSerializerMiddlewares.<T>jsonSerialize(objectMapper.writer())
                .and(Middlewares::httpPayloadSemantics)
                .and(responseAsyncHandler -> requestContext ->
                        responseAsyncHandler.invoke(requestContext)
                                .thenApply(response -> response.withHeader("Access-Control-Allow-Origin", "*")));
    }
}
