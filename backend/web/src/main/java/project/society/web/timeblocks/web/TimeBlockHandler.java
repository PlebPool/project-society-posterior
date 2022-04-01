package project.society.web.timeblocks.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import project.society.web.timeblocks.model.TimeBlockDayDAOService;
import project.society.web.timeblocks.model.dto.TimeBlockDay;
import reactor.core.publisher.Mono;

@Component
public class TimeBlockHandler {
    private final TimeBlockDayDAOService dayDAOService;

    public TimeBlockHandler(TimeBlockDayDAOService dayDAOService) {
        this.dayDAOService = dayDAOService;
    }

    public Mono<ServerResponse> initDb(ServerRequest request) {

    }

    public Mono<ServerResponse> getTimeBlockById(ServerRequest request) {
        // TODO: Check if user has access to target.
        String targetId = request.pathVariable("uuid");
        Mono<TimeBlockDay> timeBlockDayMono = this.dayDAOService.findOneById(targetId);
        return timeBlockDayMono
                .flatMap(timeBlockDay -> ServerResponse.ok()
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                        .bodyValue(timeBlockDay))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
