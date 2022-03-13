package project.society.web.apis.google.classroom.classroom.courses.web;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import project.society.web.apis.google.classroom.classroom.courses.model.ClassroomCourseResponsePlural;
import project.society.web.apis.google.classroom.client.ClassroomService;
import reactor.core.publisher.Mono;

@Component
public class ClassroomCourseHandler {
    private final ClassroomService classroomService;

    public ClassroomCourseHandler(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    public Mono<ServerResponse> getAllCoursesForUser(ServerRequest request) {
        return classroomService.getResponseMono("/courses", request, ClassroomCourseResponsePlural.class)
                .flatMap(classroomCourseResponsePlural -> ServerResponse.ok().bodyValue(classroomCourseResponsePlural));
    }
}
