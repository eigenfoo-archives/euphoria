package edu.cooper.ece366.euphoria;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.route.AsyncHandler;
import com.spotify.apollo.route.JsonSerializerMiddlewares;
import com.spotify.apollo.route.Middleware;
import com.spotify.apollo.route.Middlewares;
import com.spotify.apollo.route.Route;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import okio.ByteString;

import static edu.cooper.ece366.euphoria.Industry.HEALTHCARE;
import static edu.cooper.ece366.euphoria.Location.NEWYORK;
import static edu.cooper.ece366.euphoria.SkillLevel.INTERNSHIP;

public class PostingHandles {

    private final ObjectMapper objectMapper;

    public PostingHandles(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
        return Stream.of(
                Route.sync("GET", "/postings", this::getPostings)
        ).map(r -> r.withMiddleware(jsonMiddleware()));
    }

    private List<edu.cooper.ece366.euphoria.Posting> getPostings(final RequestContext requestContext) {
        edu.cooper.ece366.euphoria.Posting posting = new PostingBuilder()
                .postingId(31415)
                .jobTitle("Underwater Basket Weaver")
                .description("What it sounds like.")
                .location(NEWYORK)
                .skillLevel(INTERNSHIP)
                .industry(HEALTHCARE)
                .build();
        return Collections.singletonList(posting);
    }

    private <T> Middleware<AsyncHandler<T>, AsyncHandler<Response<ByteString>>> jsonMiddleware() {
        return JsonSerializerMiddlewares.<T>jsonSerialize(objectMapper.writer())
                .and(Middlewares::httpPayloadSemantics);
    }
}