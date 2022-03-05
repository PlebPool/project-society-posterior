package project.society.web.apis.mock;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class MockHandler {
    public Mono<ServerResponse> mock(ServerRequest request) {
        return ServerResponse.ok().bodyValue("I am mocking you.");
    }
}
