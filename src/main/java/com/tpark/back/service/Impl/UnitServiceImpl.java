package com.tpark.back.service.Impl;

import com.tpark.back.dao.Impl.UnitDAOImpl;
import com.tpark.back.model.Unit;
import com.tpark.back.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UnitServiceImpl implements UnitService {

    private final UnitDAOImpl unitDAO;

    @Autowired
    UnitServiceImpl(UnitDAOImpl unitDAO){
        this.unitDAO = unitDAO;
    }

    @Override
    public Object getUnitsByCourse(Integer courseId) {
        return unitDAO.getUnitsByCourse(courseId);
    }

    @Override
    public Object getUnit(Integer groupId) {
        return unitDAO.getUnit(groupId);
    }

    @Override
    public void createUnit(Unit unit) {
        unitDAO.createUnit(unit);
    }

    @Override
    public void changeUnit(Unit unit) {
        unitDAO.changeUnit(unit);
    }

    @Override
    public void deleteUnit(int id) {
        unitDAO.deleteUnit(id);
    }
}
