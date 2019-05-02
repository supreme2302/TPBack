package com.tpark.back.service.Impl;

import com.tpark.back.dao.CourseDAO;
import com.tpark.back.model.dto.CourseDTO;
import com.tpark.back.service.CourseService;
import com.tpark.back.util.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static com.tpark.back.model.Resourses.PATH_COURSE_AVATARS_FOLDER;

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

    @Override
    public String getImageLink(int courseId) {
        return courseDAO.getImageLink(courseId);
    }

    @Override
    public String store(MultipartFile file, int id) throws IOException {
        String link = String.valueOf(id) + "." + RandomString.getRandomString() + ".jpg";
        File tosave = new File(PATH_COURSE_AVATARS_FOLDER + link);
        file.transferTo(tosave);
        courseDAO.savePicture(link, id);
        return link;
    }
}
