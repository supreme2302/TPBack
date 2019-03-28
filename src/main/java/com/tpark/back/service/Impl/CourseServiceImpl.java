package com.tpark.back.service.Impl;

import com.tpark.back.dao.CourseDAO;
import com.tpark.back.model.dto.CourseDTO;
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
    public List<CourseDTO> getCoursesByAdmin(String email) {
        return courseDAO.getCoursesByAdmin(email);
    }

    @Override
    public void createCourse(CourseDTO courseDTO, String email) {
        courseDAO.createCourse(courseDTO, email);
    }

    @Override
    public void deleteCourse(int id , String email) {
        courseDAO.deleteCourse(id, email);
    }

    @Override
    public CourseDTO getCourse(int courseID, String email){ return courseDAO.getCourse(courseID, email); };

    @Override
    public void changeCourse(CourseDTO courseDTO, String admin){ courseDAO.changeCourse(courseDTO, admin); }

    @Override
    public CourseDTO getStudentCourse(Integer id, String student) {
        return courseDAO.getStudentCourse(id, student);
    }

    @Override
    public List<CourseDTO> getCoursesByStudent(String student) {
        return courseDAO.getCoursesByStudent(student);
    }

    ;
}


//TODO: Написать рабочее создание school
//TODO: В школе должен быть ID владельца