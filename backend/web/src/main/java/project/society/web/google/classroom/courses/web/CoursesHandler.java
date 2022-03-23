package project.society.web.google.classroom.courses.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import project.society.web.google.classroom.client.ClassroomService;
import project.society.web.google.classroom.courses.web.domain.ClassroomCourseResponsePlural;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;

@Component
public class CoursesHandler {
    private final ClassroomService classroomService;

    public CoursesHandler(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    public Mono<ServerResponse> getAllCoursesForUser(ServerRequest request) {
        return this.classroomService.getResponseMono("/courses", request, ClassroomCourseResponsePlural.class)
                .map(ClassroomCourseResponsePlural::getCourses)
                .switchIfEmpty(Mono.just(new ArrayList<>())) // Create an empty array if we don't get a response.
                .flatMap(singulars -> {
                    if(singulars.isEmpty()) {
                        return ServerResponse.temporaryRedirect(URI.create("/no-bitches.jpg")).build();
                    }
                    return ServerResponse.ok().header(
                            HttpHeaders.CONTENT_TYPE,
                            MediaType.APPLICATION_JSON.toString()
                    ).bodyValue(singulars);
                });
    }
}
