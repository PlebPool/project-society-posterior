package project.society.web.google.classroom.progress.web.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CourseProgress {
    private String courseId;
    private String courseName;
    private BigDecimal progressPercent;

    public CourseProgress(String courseId, String courseName, TotalCompletedPair totalCompletedPair) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.progressPercent = totalCompletedPair.calculateProgressPercent();
    }

    public CourseProgress() {
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public BigDecimal getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(BigDecimal progressPercent) {
        this.progressPercent = progressPercent;
    }

    public static class TotalCompletedPair {
        private double total = 0.00;
        private double completed = 0.00;

        public void incrementTotal() {
            total++;
        }

        public void incrementCompleted() {
            completed++;
        }

        public BigDecimal calculateProgressPercent() {
            try {
                return BigDecimal.valueOf(completed/total)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_EVEN);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
