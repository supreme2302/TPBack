package com.tpark.back.dao;

import com.tpark.back.model.Course;

import java.util.List;

public interface CourseDAO {
    List<Course> getCoursesByAdmin(String email);
    void createCourse(Course course, String email);
    void deleteCourse(int id, String email);
    Course getCourse(int id, String email);

    void changeCourse(Course course, String admin);

    Course getStudentCourse(Integer student, String s);

    List<Course> getCoursesByStudent(String student);
}
