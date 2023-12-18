package net.bnijik.schooldbcli.dao.course;

import net.bnijik.schooldbcli.config.YamlPropertySourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:queries.yml")
public record CourseQueries(@Value("${course.create}") String create,
                            @Value("${course.find-by-id}") String findById,
                            @Value("${course.find-by-name}") String findByName,
                            @Value("${course.find-all}") String finaAll, @Value("${course.update}") String update,
                            @Value("${course.delete-by-id}") String deleteById) {

    public static final String COURSE_ID_PARAM = "courseId";
    public static final String COURSE_NAME_PARAM = "courseName";
    public static final String COURSE_DESCRIPTION_PARAM = "courseDescription";
    public static final String COURSE_ID_COLUMN = "course_id";
    public static final String COURSE_NAME_COLUMN = "course_name";
    public static final String COURSE_DESCRIPTION_COLUMN = "course_description";
    public static final String COURSE_TABLE_NAME = "courses";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseQueries that)) return false;
        return Objects.equals(create, that.create) &&
                Objects.equals(findById, that.findById) &&
                Objects.equals(findByName, that.findByName) &&
                Objects.equals(finaAll, that.finaAll) &&
                Objects.equals(update, that.update) &&
                Objects.equals(deleteById, that.deleteById);
    }

    @Override
    public int hashCode() {
        return Objects.hash(create, findById, findByName, finaAll, update, deleteById);
    }

    @Override
    public String toString() {
        return "CourseQueries{" +
                "create='" +
                create +
                '\'' +
                ", findById='" +
                findById +
                '\'' +
                ", findByName='" +
                findByName +
                '\'' +
                ", finaAll='" +
                finaAll +
                '\'' +
                ", update='" +
                update +
                '\'' +
                ", deleteById='" +
                deleteById +
                '\'' +
                '}';
    }
}
