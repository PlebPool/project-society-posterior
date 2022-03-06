package project.society.security.session.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.ReactiveSessionRepository;
import org.springframework.session.config.annotation.web.server.EnableSpringWebSession;
import org.springframework.util.Assert;
import org.springframework.web.server.session.CookieWebSessionIdResolver;
import org.springframework.web.server.session.WebSessionIdResolver;
import project.society.data.dao.GenericReactiveDAO;
import project.society.security.session.CustomizedMapSession;
import project.society.security.session.MySqlReactiveSessionRepository;
import project.society.security.session.model.SessionDAOService;

@Configuration
@EnableSpringWebSession
public class SessionAutoConfig {
    /**
     * @param dao {@link GenericReactiveDAO}. Generic data access object.
     * @return {@link SessionDAOService}. Session focused abstraction for {@link GenericReactiveDAO}.
     */
    @Bean
    @ConditionalOnMissingBean(SessionDAOService.class)
    public SessionDAOService sessionDAOService(GenericReactiveDAO dao) {
        Assert.notNull(dao, dao.getClass().getName() + " cannot be null.");
        return new SessionDAOService(dao);
    }

    /**
     * @param sessionDAOService {@link SessionDAOService}. Session focused abstraction for {@link GenericReactiveDAO}.
     * @return {@link MySqlReactiveSessionRepository} {@link org.springframework.session.ReactiveSessionRepository} backed by MySql.
     */
    @Bean
    public ReactiveSessionRepository<CustomizedMapSession> mySqlReactiveSessionRepository(SessionDAOService sessionDAOService) {
        return new MySqlReactiveSessionRepository(sessionDAOService);
    }

    @Bean
    public WebSessionIdResolver webSessionIdResolver() {
        CookieWebSessionIdResolver resolver = new CookieWebSessionIdResolver();
        resolver.addCookieInitializer(responseCookieBuilder -> responseCookieBuilder.sameSite("Lax"));
        return resolver;
    }
}

