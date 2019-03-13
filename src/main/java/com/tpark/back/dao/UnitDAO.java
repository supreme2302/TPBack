package com.tpark.back.dao;

import com.tpark.back.model.Unit;

public interface UnitDAO {
    Object getUnitsByCourse(Integer courseId);

    Object getUnit(Integer groupId);

    void createUnit(Unit unit) ;

    void changeUnit(Unit unit) ;

    void deleteUnit(int id) ;
}
