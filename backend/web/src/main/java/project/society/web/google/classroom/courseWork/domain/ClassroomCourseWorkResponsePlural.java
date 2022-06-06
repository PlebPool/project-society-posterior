package project.society.web.google.classroom.courseWork.domain;

import java.util.List;
import project.society.web.google.classroom.ClassroomResponseType;

public class ClassroomCourseWorkResponsePlural implements ClassroomResponseType {
  private List<ClassroomCourseWorkResponseSingular> courseWork;

  public ClassroomCourseWorkResponsePlural(List<ClassroomCourseWorkResponseSingular> courseWork) {
    this.courseWork = courseWork;
  }

  public ClassroomCourseWorkResponsePlural() {}

  public List<ClassroomCourseWorkResponseSingular> getCourseWork() {
    return courseWork;
  }

  public void setCourseWork(List<ClassroomCourseWorkResponseSingular> courseWork) {
    this.courseWork = courseWork;
  }
}
