package com.tpark.back.dao;

import com.tpark.back.model.dto.UnitDTO;

import java.util.List;

public interface UnitDAO {
    Object getUnitsByCourse(Integer courseId, String email);

    Object getUnit(Integer groupId, String email);

    void createUnit(UnitDTO unitDTO, String email) ;

    void changeUnit(UnitDTO unitDTO, String email) ;

    void deleteUnit(int id, String email) ;

    UnitDTO getUnitForStudent(Integer unitId, String student);

    List<UnitDTO> getUnitByCourseForStudent(Integer courseId, String student);

    Object getUnits(String user);

    Object getUnitsForStudent(String student);
}
