package com.tpark.back.model;

import java.nio.file.Paths;

public class Resourses {
    public static final String PATH_COURSE_AVATARS_FOLDER = Paths.get("uploads/course")
            .toAbsolutePath().toString() + '/';

    public static final String PATH_SCHOOL_AVATARS_FOLDER = Paths.get("uploads/school")
            .toAbsolutePath().toString() + '/';
}
