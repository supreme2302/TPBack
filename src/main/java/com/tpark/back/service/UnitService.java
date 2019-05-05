package com.tpark.back.service;

import com.tpark.back.model.dto.UnitDTO;

public interface UnitService {
    Object getUnitsByCourse(Integer courseId, String email) ;

    Object getUnit(Integer groupId, String email) ;

    void createUnit(UnitDTO unitDTO, String email) ;

    void changeUnit(UnitDTO unitDTO, String email) ;

    void deleteUnit(int id, String email) ;

    Object getUnitForStudent(Integer unitId, String student);

    Object getUnitsByCourseForStudent(Integer courseId, String student);

    Object getAllUnits(String user);

    Object getAllUnitsForStudent(String student);
}
