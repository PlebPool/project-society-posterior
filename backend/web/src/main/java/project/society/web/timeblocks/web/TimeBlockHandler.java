package project.society.web.timeblocks.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import project.society.security.util.CustomOAuth2Util;
import project.society.web.timeblocks.exceptions.DayIndexOutOfBoundsException;
import project.society.web.timeblocks.model.TimeBlockDayDAOService;
import project.society.web.timeblocks.model.dto.TimeBlock;
import project.society.web.timeblocks.model.dto.TimeBlockDay;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class TimeBlockHandler {
    private final TimeBlockDayDAOService dayDAOService;
    private final CustomOAuth2Util oAuth2Util;

    public TimeBlockHandler(TimeBlockDayDAOService dayDAOService, CustomOAuth2Util oAuth2Util) {
        this.dayDAOService = dayDAOService;
        this.oAuth2Util = oAuth2Util;
    }

    public Mono<ServerResponse> initDb(ServerRequest request) {
        ArrayList<TimeBlock> list = new ArrayList<>();
        for(int i = 0; i < 6; i++) {
            list.add(new TimeBlock("hey"+i, Short.parseShort("60"), Short.parseShort("30"), "#ff5733"));
        }
        return this.oAuth2Util.extractPrincipalName(request)
                .flatMap(userId -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(this.dayDAOService
                                .save(new TimeBlockDay(userId, DayOfWeek.FRIDAY, list)), TimeBlockDay.class));
    }

    public Mono<ServerResponse> postTimeBlockDay(ServerRequest request) {
        return request.bodyToMono(TimeBlockDay.class)
                .map(timeBlockDay -> {
                    if(timeBlockDay.getDayIndex() > 7 || timeBlockDay.getDayIndex() < 0)
                        throw new DayIndexOutOfBoundsException(); // Returns http 400 with reason message.
                    return timeBlockDay;
                })
                .zipWith(this.oAuth2Util.extractPrincipalName(request), ((timeBlockDay, principalName) -> {
                    timeBlockDay.setUserId(principalName); // Adds principal name to timeBlock.
                    return timeBlockDay;
                }))
                .flatMap(this.dayDAOService::save) // Saves timeBlock to db.
                .flatMap(tbd -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(tbd));
    }

    public Mono<ServerResponse> getTimeBlockById(ServerRequest request) {
        return this.dayDAOService.findOneById(request.pathVariable("uuid"))
                .zipWith(this.oAuth2Util.extractPrincipalName(request))
                .filter(tuple -> tuple.getT1().getUserId().equals(tuple.getT2()))
                .map(Tuple2::getT1)
                .flatMap(tbd -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(tbd))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getTimeBlocksForUser(ServerRequest request) {
        return this.oAuth2Util.extractPrincipalName(request)
                .flatMapMany(this.dayDAOService::getByUserId)
                .collectList()
                .flatMap(list -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(list))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
