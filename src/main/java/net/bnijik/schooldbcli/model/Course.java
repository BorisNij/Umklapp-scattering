package net.bnijik.schooldbcli.model;

public record Course(long courseId, String courseName, String courseDescription) {

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" +
                courseId +
                ", courseName='" +
                courseName +
                '\'' +
                ", courseDescription='" +
                courseDescription +
                '\'' +
                '}';
    }
}
