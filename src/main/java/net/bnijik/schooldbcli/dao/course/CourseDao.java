package net.bnijik.schooldbcli.dao.course;

import net.bnijik.schooldbcli.dao.Dao;
import net.bnijik.schooldbcli.dao.Page;
import net.bnijik.schooldbcli.model.Course;

import java.util.stream.Stream;

public interface CourseDao extends Dao<Course> {
    Stream<Course> findAllForStudent(int studentId, Page page);
}
