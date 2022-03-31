package project.society.security.session.config;

import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import project.society.data.dao.GenericReactiveDAO;
import project.society.security.session.model.MySqlReactiveSessionRepository;
import project.society.security.session.model.SessionDAOService;

@TestConfiguration
@DataR2dbcTest
public class TestConfig {
    @Bean
    public MySqlReactiveSessionRepository mySqlReactiveSessionRepository(R2dbcEntityTemplate r2dbcEntityTemplate, Environment e) {
        return new MySqlReactiveSessionRepository(new SessionDAOService(new GenericReactiveDAO(r2dbcEntityTemplate)), e);
    }
}
