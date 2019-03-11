package com.tpark.back.service.Impl;

import com.tpark.back.dao.CourseDAO;
import com.tpark.back.dao.Impl.CourseDAOImpl;
import com.tpark.back.model.Course;
import com.tpark.back.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseDAOImpl courseDAO;

    @Autowired
    CourseServiceImpl(CourseDAOImpl courseDAO){
        this.courseDAO = courseDAO;
    }


    @Override
    public List<Course> getCoursesByAdmin(String email) {
        return courseDAO.getCoursesByAdmin(email);
    }

    @Override
    public void createCourse(Course course) {
        courseDAO.createCourse(course);
    }

    @Override
    public void deleteCourse(int id) {
        courseDAO.deleteCourse(id);
    }

    @Override
    public Course getCourse(int courseID){ return courseDAO.getCourse(courseID); };

    @Override
    public void changeCourse(Course course){ courseDAO.changeCourse(course); };
}
