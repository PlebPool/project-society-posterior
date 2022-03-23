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

    /**
     * Sends a request to google classroom api with access token provided in {@link ServerRequest}.
     * If a request to "/courses" returns anything other than a 200 with json.
     * It means that the user either has no courses, or that there is an issue with the Google Classroom api.
     * If we don't get a valid response from Google, we redirect the user to "/no-bitches.jpg".
     * This way, the user knows it has no bitches (courses).
     * @param request Current {@link ServerRequest}.
     * @return {@link Mono} of {@link ServerResponse}.
     */
    public Mono<ServerResponse> getAllCoursesForUser(ServerRequest request) {
        return this.classroomService.getResponseMono("/courses", request, ClassroomCourseResponsePlural.class)
                .map(ClassroomCourseResponsePlural::getCourses)
                .switchIfEmpty(Mono.just(new ArrayList<>()))
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
