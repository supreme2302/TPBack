package com.tpark.back.service;

import com.tpark.back.model.Unit;

public interface UnitService {
    Object getUnitsByCourse(Integer courseId, String email) ;

    Object getUnit(Integer groupId, String email) ;

    void createUnit(Unit unit, String email) ;

    void changeUnit(Unit unit, String email) ;

    void deleteUnit(int id, String email) ;

    Object getUnitForStudent(Integer unitId, String student);

    Object getUnitsByCourseForStudent(Integer courseId, String student);
}
