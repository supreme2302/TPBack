package com.tpark.back.dao;

import com.tpark.back.model.dto.CourseDTO;

import java.util.List;

public interface CourseDAO {
    List<CourseDTO> getCoursesByAdmin(String email);
    void createCourse(CourseDTO courseDTO, String email);
    void deleteCourse(int id, String email);
    CourseDTO getCourse(int id, String email);

    void changeCourse(CourseDTO courseDTO, String admin);

    CourseDTO getStudentCourse(Integer student, String s);

    List<CourseDTO> getCoursesByStudent(String student);

    void savePicture(String link, int id);

    String getImageLink(int courseId);
}
