package project.society.web.google.classroom.progress.web;

import java.util.Locale;
import net.minidev.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import project.society.web.google.classroom.client.ClassroomService;
import project.society.web.google.classroom.courseWork.domain.ClassroomCourseWorkResponsePlural;
import project.society.web.google.classroom.courses.web.domain.ClassroomCourseResponsePlural;
import project.society.web.google.classroom.courses.web.domain.ClassroomCourseResponseSingular;
import project.society.web.google.classroom.progress.web.domain.CourseProgress;
import project.society.web.google.classroom.studentSubmissions.domain.ClassroomStudentSubmissionResponsePlural;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CourseProgressHandler {
  private final ClassroomService classroomService;

  public CourseProgressHandler(ClassroomService classroomService) {
    this.classroomService = classroomService;
  }

  public Mono<ServerResponse> getCourseProgressById(ServerRequest request) {
    return this.classroomService
        .getResponseMono(
            "/courses/" + request.pathVariable("courseId"),
            request,
            ClassroomCourseResponseSingular.class)
        .flatMap(course -> this.calculateCourseProgress(course, request))
        .flatMap(
            courseProgress ->
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(courseProgress))
        .switchIfEmpty(ServerResponse.notFound().build());
  }

  public Mono<ServerResponse> getCourseProgressForUser(ServerRequest request) {
    return this.classroomService
        .getResponseMono("/courses", request, ClassroomCourseResponsePlural.class)
        .mapNotNull(ClassroomCourseResponsePlural::getCourses)
        .flatMapMany(Flux::fromIterable)
        .flatMap(course -> this.calculateCourseProgress(course, request))
        .collectList()
        .map(this::buildResBody)
        .flatMap(
            courseProgresses ->
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(courseProgresses))
        .switchIfEmpty(ServerResponse.notFound().build());
  }

  private JSONObject buildResBody(Object resObj) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.appendField("res", resObj);
    return jsonObject;
  }

  private Mono<CourseProgress> calculateCourseProgress(
      ClassroomCourseResponseSingular course, ServerRequest request) {
    return this.classroomService
        .getResponseMono(
            "/courses/" + course.getId() + "/courseWork",
            request,
            ClassroomCourseWorkResponsePlural.class)
        .mapNotNull(ClassroomCourseWorkResponsePlural::getCourseWork)
        .flatMapMany(Flux::fromIterable)
        .flatMap(
            courseWork -> this.courseWorkIsHandedIn(course.getId(), courseWork.getId(), request))
        .reduce(
            new CourseProgress.TotalCompletedPair(),
            (subTotal, element) -> {
              subTotal.incrementTotal();
              if (element) subTotal.incrementCompleted();
              return subTotal;
            })
        .switchIfEmpty(Mono.empty())
        .map(
            totalCompletedPair ->
                new CourseProgress(course.getId(), course.getName(), totalCompletedPair));
  }

  private Flux<Boolean> courseWorkIsHandedIn(
      String courseId, String courseWorkId, ServerRequest request) {
    return this.classroomService
        .getResponseMono(
            "/courses/" + courseId + "/courseWork/" + courseWorkId + "/studentSubmissions",
            request,
            ClassroomStudentSubmissionResponsePlural.class)
        .map(ClassroomStudentSubmissionResponsePlural::getStudentSubmissions)
        .flatMapMany(Flux::fromIterable)
        .map(
            studentSubmission ->
                studentSubmission.getState().toUpperCase(Locale.ROOT).equals("TURNED_IN"));
  }
}
