package project.society.web.google.classroom.studentSubmissions.domain;

import project.society.web.google.classroom.ClassroomResponseType;

public class ClassroomStudentSubmissionResponseSingular implements ClassroomResponseType {
    private String state;

    public ClassroomStudentSubmissionResponseSingular(String state) {
        this.state = state;
    }

    public ClassroomStudentSubmissionResponseSingular() {
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
