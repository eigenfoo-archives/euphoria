package edu.cooper.ece366.euphoria;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.Environment;
import com.spotify.apollo.httpservice.HttpService;
import com.spotify.apollo.httpservice.LoadingException;
import com.spotify.apollo.route.Route;
import io.norberg.automatter.jackson.AutoMatterModule;

public class Main {
    public static void main(String[] args) throws LoadingException {
        HttpService.boot(Main::init, "euphoria-service", args);
    }

    private static void init(final Environment environment) {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new AutoMatterModule());
        PostingHandles postingHandlers = new PostingHandles(objectMapper);

        environment
                .routingEngine()
                .registerAutoRoute(Route.sync("GET", "/ping", rc -> "pong"))
                .registerRoutes(postingHandlers.routes());
    }
}
