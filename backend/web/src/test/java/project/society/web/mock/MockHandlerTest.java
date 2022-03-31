package project.society.web.mock;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {MockRouterConfig.class, MockHandler.class})
@WebFluxTest(excludeAutoConfiguration = {ReactiveSecurityAutoConfiguration.class})
class MockHandlerTest {
    @Autowired
    private ApplicationContext context;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToApplicationContext(context).build();
    }

    @AfterEach
    void tearDown() {
        webTestClient = null;
    }

    @Test
    void mock() {
        webTestClient.get().uri("/no-auth/mock")
                        .accept(MediaType.TEXT_PLAIN)
                                .exchange()
                                        .expectStatus().isOk()
                        .expectBody(String.class)
                                .value(responseStr -> {
                                    Assertions.assertThat(responseStr).isEqualTo("I am mocking you.");
                                });
    }
}