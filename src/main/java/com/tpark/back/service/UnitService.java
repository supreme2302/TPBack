package com.tpark.back.service;

import com.tpark.back.model.dto.UnitDTO;

import java.util.List;

public interface UnitService {
    List<UnitDTO> getUnitsByCourse(Integer courseId, String email) ;

    UnitDTO getUnit(Integer groupId, String email) ;

    void createUnit(UnitDTO unitDTO, String email) ;

    void changeUnit(UnitDTO unitDTO, String email) ;

    void deleteUnit(int id, String email) ;

    UnitDTO getUnitForStudent(Integer unitId, String student);

    List<UnitDTO> getUnitsByCourseForStudent(Integer courseId, String student);

    Object getAllUnits(String user);

    Object getAllUnitsForStudent(String student);
}
