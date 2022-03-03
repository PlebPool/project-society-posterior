package project.society.data.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import project.society.data.dao.GenericReactiveDAO;

@Configuration
public class DataAutoConfig {
    @Bean("dao")
    @ConditionalOnMissingBean(GenericReactiveDAO.class)
    public GenericReactiveDAO genericReactiveDAO(R2dbcEntityTemplate r2dbcEntityTemplate) {
        return new GenericReactiveDAO(r2dbcEntityTemplate);
    }
}
