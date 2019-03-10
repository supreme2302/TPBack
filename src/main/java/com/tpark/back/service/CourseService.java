package com.tpark.back.service;

import com.tpark.back.model.Course;

import java.util.List;

public interface CourseService {
    List<Course> getCoursesByAdmin(String email);
    void createCourse(Course course);
    void deleteCourse(int id);
}
