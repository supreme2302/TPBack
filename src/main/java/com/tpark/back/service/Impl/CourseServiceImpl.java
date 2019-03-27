package com.tpark.back.service.Impl;

import com.tpark.back.dao.CourseDAO;
import com.tpark.back.model.Course;
import com.tpark.back.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseDAO courseDAO;

    @Autowired
    CourseServiceImpl(CourseDAO courseDAO){
        this.courseDAO = courseDAO;
    }


    @Override
    public List<Course> getCoursesByAdmin(String email) {
        return courseDAO.getCoursesByAdmin(email);
    }

    @Override
    public void createCourse(Course course, String email) {
        courseDAO.createCourse(course, email);
    }

    @Override
    public void deleteCourse(int id , String email) {
        courseDAO.deleteCourse(id, email);
    }

    @Override
    public Course getCourse(int courseID, String email){ return courseDAO.getCourse(courseID, email); };

    @Override
    public void changeCourse(Course course, String admin){ courseDAO.changeCourse(course, admin); }

    @Override
    public Course getStudentCourse(Integer id, String student) {
        return courseDAO.getStudentCourse(id, student);
    }

    @Override
    public List<Course> getCoursesByStudent(String student) {
        return courseDAO.getCoursesByStudent(student);
    }

    ;
}


//TODO: Написать рабочее создание school
//TODO: В школе должен быть ID владельца