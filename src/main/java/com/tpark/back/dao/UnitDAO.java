package com.tpark.back.dao;

import com.tpark.back.model.Unit;

import java.util.List;

public interface UnitDAO {
    Object getUnitsByCourse(Integer courseId, String email);

    Object getUnit(Integer groupId, String email);

    void createUnit(Unit unit, String email) ;

    void changeUnit(Unit unit, String email) ;

    void deleteUnit(int id, String email) ;

    Unit getUnitForStudent(Integer unitId, String student);

    List<Unit> getUnitByCourseForStudent(Integer courseId, String student);
}
