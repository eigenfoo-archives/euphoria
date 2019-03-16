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
import okio.ByteString;

public class PostingHandlers {

  private final ObjectMapper objectMapper;

  public PostingHandlers(final ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
    return Stream.of(
        Route.sync("GET", "/postings", this::getPostings)
    ).map(r -> r.withMiddleware(jsonMiddleware()));
  }

  private List<Posting> getPostings(final RequestContext requestContext) {
    Posting posting = new PostingBuilder().postingId(31415).jobTitle("Underwater Basket Weaver").description("What it sounds like.").build();
    return Collections.singletonList(posting);
  }

  private <T> Middleware<AsyncHandler<T>, AsyncHandler<Response<ByteString>>> jsonMiddleware() {
    return JsonSerializerMiddlewares.<T>jsonSerialize(objectMapper.writer())
        .and(Middlewares::httpPayloadSemantics);
  }
}