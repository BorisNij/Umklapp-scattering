package net.bnijik.schooldbcli.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public record Course(Long courseId, String courseName, String courseDescription) {

    public Map<String, Object> toMap() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("courseId", this.courseId);
        parameters.put("courseName", this.courseName);
        parameters.put("courseDescription", this.courseDescription);
        return parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course course)) return false;
        return Objects.equals(courseId, course.courseId) &&
                Objects.equals(courseName, course.courseName) &&
                Objects.equals(courseDescription, course.courseDescription);
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", courseDescription='" + courseDescription + '\'' +
                '}';
    }
}
