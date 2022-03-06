package project.society.security.cors;

import io.netty.handler.codec.http.HttpHeaderNames;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import project.society.utility.property_names.PropertyNameHolder;

import java.util.List;

@Configuration
public class CorsConfig {
    /**
     * @param environment Interface allowing us to read environment variables.
     * @return {@link CorsConfigurationSource}. Source of cors configuration.
     */
    @Bean("myCorsConfig")
    public CorsConfigurationSource corsConfigurationSource(Environment environment) {
        String frontendOrigin = environment.getProperty(PropertyNameHolder.PROJECT_DEV_FRONTEND_URL);
        Assert.hasLength(frontendOrigin, "Frontend origin cannot be empty.");
        CorsConfiguration cors = new CorsConfiguration();
        cors.addAllowedOrigin(frontendOrigin);
        cors.addAllowedHeader(String.valueOf(HttpHeaderNames.CONTENT_TYPE)); // TODO: MAKE REQUIRED FOR MUTATIVE HTTP METHODS.
        cors.setAllowedMethods(List.of("POST", "GET", "DELETE", "OPTIONS", "PUT"));
        cors.setAllowCredentials(true);
        cors.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }
}
