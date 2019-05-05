package com.tpark.back.dao;

import com.tpark.back.model.domain.UnitDomain;

import java.util.List;

public interface UnitDAO {
    List<UnitDomain> getUnitsByCourse(Integer courseId, String email);

    UnitDomain getUnit(Integer groupId, String email);

    void createUnit(UnitDomain unitDomain, String email) ;

    void changeUnit(UnitDomain unitDomain, String email) ;

    void deleteUnit(int id, String email) ;

    UnitDomain getUnitForStudent(Integer unitId, String student);

    List<UnitDomain> getUnitByCourseForStudent(Integer courseId, String student);

    List<UnitDomain> getUnits(String user);

    List<UnitDomain> getUnitsForStudent(String student);
}
