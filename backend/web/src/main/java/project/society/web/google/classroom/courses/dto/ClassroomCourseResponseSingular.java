package project.society.web.google.classroom.courses.dto;

import project.society.web.google.classroom.ClassroomResponseType;

import java.util.Objects;

public class ClassroomCourseResponseSingular implements ClassroomResponseType {
    private String id;
    private String name;
    private String descriptionHeading;
    private String courseState;

    public ClassroomCourseResponseSingular(String id, String name, String descriptionHeading, String courseState) {
        this.id = id;
        this.name = name;
        this.descriptionHeading = descriptionHeading;
        this.courseState = courseState;
    }

    public ClassroomCourseResponseSingular() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassroomCourseResponseSingular that = (ClassroomCourseResponseSingular) o;
        return id.equals(that.id) && Objects.equals(name, that.name) && Objects.equals(descriptionHeading, that.descriptionHeading) && Objects.equals(courseState, that.courseState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, descriptionHeading, courseState);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptionHeading() {
        return descriptionHeading;
    }

    public void setDescriptionHeading(String descriptionHeading) {
        this.descriptionHeading = descriptionHeading;
    }

    public String getCourseState() {
        return courseState;
    }

    public void setCourseState(String courseState) {
        this.courseState = courseState;
    }

    @Override
    public String toString() {
        return "ClassroomCourseResponseSingular{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", descriptionHeading='" + descriptionHeading + '\'' +
                ", courseState='" + courseState + '\'' +
                '}';
    }
}
