package edu.cooper.ece366.euphoria;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.Environment;
import com.spotify.apollo.httpservice.HttpService;
import com.spotify.apollo.httpservice.LoadingException;
import com.spotify.apollo.route.Route;
import com.typesafe.config.Config;
import edu.cooper.ece366.euphoria.handler.*;
import edu.cooper.ece366.euphoria.store.jdbc.*;
import edu.cooper.ece366.euphoria.store.model.*;

import io.norberg.automatter.jackson.AutoMatterModule;

public class Main {
    public static void main(String[] args) throws LoadingException {
        HttpService.boot(Main::init, "euphoria-service", args);
    }

    private static void init(final Environment environment) {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new AutoMatterModule());
        Config config = environment.config();

        UserStore userStore = new UserStoreJdbc(environment.config());
        UserHandlers userHandlers = new UserHandlers(objectMapper, userStore);

        CompanyStore companyStore = new CompanyStoreJdbc(environment.config());
        CompanyHandlers companyHandlers = new CompanyHandlers(objectMapper, companyStore);

        AuthenticationStore authenticationStore = new AuthenticationStoreJdbc(environment.config());
        AuthenticationHandlers authenticationHandlers = new AuthenticationHandlers(objectMapper, authenticationStore);

        CookieStore cookieStore = new CookieStoreJdbc(environment.config());
        CookieHandlers cookieHandlers = new CookieHandlers(objectMapper, cookieStore);

        PostingHandlers postingHandlers = new PostingHandlers(objectMapper, config);
        ApplicationHandlers applicationHandlers = new ApplicationHandlers(objectMapper, config);
      

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
