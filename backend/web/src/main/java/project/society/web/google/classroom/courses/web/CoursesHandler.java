package project.society.web.google.classroom.courses.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import project.society.web.google.classroom.client.ClassroomService;
import project.society.web.google.classroom.courses.web.domain.ClassroomCourseResponsePlural;
import project.society.web.google.classroom.courses.web.domain.ClassroomCourseResponseSingular;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

import java.net.URI;

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
        Hooks.onOperatorDebug();
        String uri = "/courses";
        Flux<ClassroomCourseResponseSingular> responseFlux = this.classroomService.getResponseMono(uri, request, ClassroomCourseResponsePlural.class)
                .map(ClassroomCourseResponsePlural::getCourses)
                .flatMapMany(Flux::fromIterable);
        return responseFlux.hasElements().flatMap(hasElements -> {
            if(hasElements) {
                return ServerResponse.ok().body(BodyInserters.fromPublisher(responseFlux, ClassroomCourseResponseSingular.class));
            } else {
                return ServerResponse.temporaryRedirect(URI.create("/no-bitches.jpg")).build();
            }
        });
    }
}
