//package project.society.web.apis.mock;
//
//import org.assertj.core.api.Assertions;
//import org.junit.Before;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
//import org.springframework.context.ApplicationContext;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.reactive.server.WebTestClient;
//import org.springframework.web.reactive.function.server.ServerRequest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@RunWith(SpringRunner.class)
//@ContextConfiguration(classes = {MockRouterConfig.class, MockHandler.class})
//@WebFluxTest
//class MockHandlerTest {
//
//    @Autowired
//    private ApplicationContext context;
//
//    private WebTestClient webTestClient;
//
//    @BeforeEach
//    public void setUp() {
//        webTestClient = WebTestClient.bindToApplicationContext(context).build();
//    }
//
//    @Test
//    void mock() {
//        webTestClient.get().uri("/mock")
//                        .accept(MediaType.APPLICATION_JSON)
//                                .exchange()
//                                        .expectStatus().isOk()
//                        .expectBody(String.class)
//                                .value(responseStr -> {
//                                    Assertions.assertThat(responseStr).isEqualTo("I am mocking you.");
//                                });
//    }
//}