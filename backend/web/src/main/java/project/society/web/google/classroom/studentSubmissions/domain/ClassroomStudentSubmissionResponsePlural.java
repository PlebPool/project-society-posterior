package project.society.web.google.classroom.studentSubmissions.domain;

import java.util.List;
import project.society.web.google.classroom.ClassroomResponseType;

public class ClassroomStudentSubmissionResponsePlural implements ClassroomResponseType {
  private List<ClassroomStudentSubmissionResponseSingular> studentSubmissions;

  public ClassroomStudentSubmissionResponsePlural(
      List<ClassroomStudentSubmissionResponseSingular> studentSubmissions) {
    this.studentSubmissions = studentSubmissions;
  }

  public ClassroomStudentSubmissionResponsePlural() {}

  public List<ClassroomStudentSubmissionResponseSingular> getStudentSubmissions() {
    return studentSubmissions;
  }

  public void setStudentSubmissions(
      List<ClassroomStudentSubmissionResponseSingular> studentSubmissions) {
    this.studentSubmissions = studentSubmissions;
  }
}
