package project.society.web.apis.google.classroom.classroom.courses.model;

import project.society.web.apis.google.classroom.ClassroomResponseType;

import java.util.List;

public class ClassroomCourseResponsePlural implements ClassroomResponseType {
    List<ClassroomCourseResponseSingular> courses;

    public ClassroomCourseResponsePlural() {
    }

    public ClassroomCourseResponsePlural(List<ClassroomCourseResponseSingular> courses) {
        this.courses = courses;
    }

    public List<ClassroomCourseResponseSingular> getCourses() {
        return courses;
    }

    public void setCourses(List<ClassroomCourseResponseSingular> courses) {
        this.courses = courses;
    }
}
