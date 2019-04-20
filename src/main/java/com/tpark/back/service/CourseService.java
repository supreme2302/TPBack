package com.tpark.back.service;

import com.tpark.back.model.dto.CourseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CourseService {
    List<CourseDTO> getCoursesByAdmin(String email);
    void createCourse(CourseDTO courseDTO, String email);
    void deleteCourse(int id , String email);

    CourseDTO getCourse(int courseID, String email);

    void changeCourse(CourseDTO courseDTO, String admin);

    CourseDTO getStudentCourse(Integer courseID, String student);

    List<CourseDTO> getCoursesByStudent(String student);

    String store(MultipartFile file, int id) throws IOException;

    String getImageLink(int id);
}
