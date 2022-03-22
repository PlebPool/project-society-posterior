package project.society.web.google.classroom.courses.dto;

import project.society.web.google.classroom.ClassroomResponseType;

import java.util.List;
import java.util.Objects;

public class ClassroomCourseResponsePlural implements ClassroomResponseType {
    private List<ClassroomCourseResponseSingular> courses;

    public ClassroomCourseResponsePlural(List<ClassroomCourseResponseSingular> courses) {
        this.courses = courses;
    }

    public ClassroomCourseResponsePlural() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassroomCourseResponsePlural that = (ClassroomCourseResponsePlural) o;
        return Objects.equals(courses, that.courses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courses);
    }

    public List<ClassroomCourseResponseSingular> getCourses() {
        return courses;
    }

    public void setCourses(List<ClassroomCourseResponseSingular> courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "ClassroomCourseResponsePlural{" +
                "courses=" + courses +
                '}';
    }
}
