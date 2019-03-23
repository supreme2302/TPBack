package com.tpark.back.service;

import com.tpark.back.model.Course;

import java.util.List;

public interface CourseService {
    List<Course> getCoursesByAdmin(String email);
    void createCourse(Course course, String email);
    void deleteCourse(int id , String email);

    Course getCourse(int courseID, String email);

    void changeCourse(Course course, String admin);

    Course getStudentCourse(Integer courseID, String student);

    List<Course> getCoursesByStudent(String student);
}
