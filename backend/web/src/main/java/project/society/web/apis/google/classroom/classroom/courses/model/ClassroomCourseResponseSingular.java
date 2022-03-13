package project.society.web.apis.google.classroom.classroom.courses.model;

import project.society.web.apis.google.classroom.ClassroomResponseType;

public class ClassroomCourseResponseSingular implements ClassroomResponseType {
    private String id;
    private String name;
    private String descriptionHeading;
    private String description;
    private String courseState;

    public ClassroomCourseResponseSingular() {
    }

    public ClassroomCourseResponseSingular(String id, String name, String descriptionHeading, String description, String courseState) {
        this.id = id;
        this.name = name;
        this.descriptionHeading = descriptionHeading;
        this.description = description;
        this.courseState = courseState;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCourseState() {
        return courseState;
    }

    public void setCourseState(String courseState) {
        this.courseState = courseState;
    }
}
