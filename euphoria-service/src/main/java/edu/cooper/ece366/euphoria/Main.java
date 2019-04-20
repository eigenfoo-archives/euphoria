package edu.cooper.ece366.euphoria;

import edu.cooper.ece366.euphoria.handler.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.Environment;
import com.spotify.apollo.httpservice.HttpService;
import com.spotify.apollo.httpservice.LoadingException;
import com.spotify.apollo.route.Route;
import com.typesafe.config.Config;
import io.norberg.automatter.jackson.AutoMatterModule;

public class Main {
    public static void main(String[] args) throws LoadingException {
        HttpService.boot(Main::init, "euphoria-service", args);
    }

    private static void init(final Environment environment) {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new AutoMatterModule());
        Config config = environment.config();
        AuthenticationHandlers authenticationHandlers = new AuthenticationHandlers(objectMapper, config);
        PostingHandlers postingHandlers = new PostingHandlers(objectMapper, config);
        UserHandlers userHandlers = new UserHandlers(objectMapper, config);
        CompanyHandlers companyHandlers = new CompanyHandlers(objectMapper, config);
        ApplicationHandlers applicationHandlers = new ApplicationHandlers(objectMapper, config);
        CookieHandlers cookieHandlers = new CookieHandlers(objectMapper, config);

        environment
                .routingEngine()
                .registerAutoRoute(Route.sync("GET", "/ping", rc -> "pong"))
                .registerRoutes(authenticationHandlers.routes())
                .registerRoutes(postingHandlers.routes())
                .registerRoutes(userHandlers.routes())
                .registerRoutes(companyHandlers.routes())
                .registerRoutes(applicationHandlers.routes())
                .registerRoutes(cookieHandlers.routes());
    }
}
