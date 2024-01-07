package net.bnijik.schooldbcli.model;

public record Student(long studentId, long groupId, String firstName, String lastName) {
    @Override
    public String toString() {
        return "Student{" +
                "studentId=" +
                studentId +
                ", groupId=" +
                groupId +
                ", firstName='" +
                firstName +
                '\'' +
                ", lastName='" +
                lastName +
                '\'' +
                '}';
    }
}
