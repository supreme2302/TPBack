package com.tpark.back.service;

import com.tpark.back.model.Course;

import java.util.List;

public interface CourseService {
    List<Course> getCoursesByAdmin(String email);
    void createCourse(Course course);
    void deleteCourse(int id);

    Course getCourse(int courseID, String email);

    void changeCourse(Course course);

    Course getStudentCourse(Integer courseID, String student);

    List<Course> getCoursesByStudent(String student);
}
