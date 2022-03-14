package project.society.web.mock;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class MockHandler {
    /**
     * It'll mock the end user.
     * @param request {@link ServerRequest}.
     * @return {@link Mono} of {@link ServerResponse}.
     */
    public Mono<ServerResponse> mock(ServerRequest request) {
        return ServerResponse.ok().bodyValue("I am mocking you.");
    }
}
