package project.society.web.google.classroom.courseWork.domain;

import project.society.web.google.classroom.ClassroomResponseType;

public class ClassroomCourseWorkResponseSingular implements ClassroomResponseType {
  private String id;

  public ClassroomCourseWorkResponseSingular(String id) {
    this.id = id;
  }

  public ClassroomCourseWorkResponseSingular() {}

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
