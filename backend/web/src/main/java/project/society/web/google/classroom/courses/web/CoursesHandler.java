package project.society.web.google.classroom.courses.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import project.society.web.google.classroom.client.ClassroomService;
import project.society.web.google.classroom.courses.web.domain.ClassroomCourseResponsePlural;
import project.society.web.google.classroom.courses.web.domain.ClassroomCourseResponseSingular;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CoursesHandler {
    private final ClassroomService classroomService;

    public CoursesHandler(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    public Mono<ServerResponse> getAllCoursesForUser(ServerRequest request) {
        return ServerResponse.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                .body(this.classroomService
                        .getResponseMono("/courses", request, ClassroomCourseResponsePlural.class)
                        .map(ClassroomCourseResponsePlural::getCourses)
                        .flatMapMany(Flux::fromIterable), ClassroomCourseResponseSingular.class);
    }
}
