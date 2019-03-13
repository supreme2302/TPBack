package com.tpark.back.service;

import com.tpark.back.model.Unit;

public interface UnitService {
    Object getUnitsByCourse(Integer courseId) ;

    Object getUnit(Integer groupId) ;

    void createUnit(Unit unit) ;

    void changeUnit(Unit unit) ;

    void deleteUnit(int id) ;
}
