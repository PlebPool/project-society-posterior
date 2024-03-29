package project.society.security.session.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository;
import org.springframework.session.ReactiveSessionRepository;
import org.springframework.session.config.annotation.web.server.EnableSpringWebSession;
import org.springframework.util.Assert;
import org.springframework.web.server.session.CookieWebSessionIdResolver;
import org.springframework.web.server.session.WebSessionIdResolver;
import project.society.data.dao.GenericReactiveDAO;
import project.society.security.session.model.CustomizedMapSession;
import project.society.security.session.model.MySqlReactiveSessionRepository;
import project.society.security.session.model.SessionDAOService;

@Configuration
@EnableSpringWebSession
public class SessionAutoConfig {
    /**
     * @param dao {@link GenericReactiveDAO}. Generic data access object.
     * @return {@link SessionDAOService}. Session focused abstraction for {@link GenericReactiveDAO}.
     */
    @Bean
    public SessionDAOService sessionDAOService(GenericReactiveDAO dao) {
        Assert.notNull(dao, dao.getClass().getName() + " cannot be null.");
        return new SessionDAOService(dao);
    }

    /**
     * @param sessionDAOService {@link SessionDAOService}. Session focused abstraction for {@link GenericReactiveDAO}.
     * @return {@link MySqlReactiveSessionRepository} {@link org.springframework.session.ReactiveSessionRepository} backed by MySql.
     */
    @Bean
    public ReactiveSessionRepository<CustomizedMapSession> mySqlReactiveSessionRepository(
            SessionDAOService sessionDAOService,
            Environment env
    ) {
        return new MySqlReactiveSessionRepository(sessionDAOService, env);
    }

    /**
     * Persists {@link org.springframework.security.oauth2.client.OAuth2AuthorizedClient} in {@link org.springframework.web.server.WebSession}.
     * @return {@link ServerOAuth2AuthorizedClientRepository}.
     */
    @Bean
    public ServerOAuth2AuthorizedClientRepository webSessionServerOAuth2AuthorizedClientRepository() {
        return new WebSessionServerOAuth2AuthorizedClientRepository();
    }

    @Bean
    public WebSessionIdResolver webSessionIdResolver() {
        CookieWebSessionIdResolver resolver = new CookieWebSessionIdResolver();
        resolver.addCookieInitializer(responseCookieBuilder -> responseCookieBuilder.sameSite("Lax"));
        return resolver;
    }
}
