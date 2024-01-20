package net.bnijik.schooldbcli.service.course;

import net.bnijik.schooldbcli.mapper.CourseMapper;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = CourseMapper.class)
public class CourseServiceImplTestConfig {
}
