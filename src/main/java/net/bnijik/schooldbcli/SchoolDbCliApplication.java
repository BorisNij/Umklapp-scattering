package net.bnijik.schooldbcli;

import net.bnijik.schooldbcli.dto.CourseDto;
import net.bnijik.schooldbcli.service.course.CourseServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.Optional;

@SpringBootApplication
@EnableAspectJAutoProxy
public class SchoolDbCliApplication {

//    CourseServiceImpl courseService;
//
//    public SchoolDbCliApplication(CourseServiceImpl courseService) {
//        this.courseService = courseService;
//        final List<CourseDto> courseDtos = courseService.findAll(Page.of(1, 22));
//    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SchoolDbCliApplication.class, args);

        // Fetching the employee object from the application context.
        CourseServiceImpl courseService = context.getBean(CourseServiceImpl.class);

        final Optional<CourseDto> optionalCourseDto = courseService.findById(1);

        System.out.println(optionalCourseDto.get().courseName());

        // Closing the context object.
        context.close();


    }

}
