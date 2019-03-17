package com.tpark.back.dao;

import com.tpark.back.model.Course;

import java.util.List;

public interface CourseDAO {
    List<Course> getCoursesByAdmin(String email);
    void createCourse(Course course);
    void deleteCourse(int id);
    Course getCourse(int id, String email);

    void changeCourse(Course course);

    Course getStudentCourse(Integer student, String s);

    List<Course> getCoursesByStudent(String student);
}
