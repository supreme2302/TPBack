package com.tpark.back.service.Impl;

import com.tpark.back.dao.Impl.UnitDAOImpl;
import com.tpark.back.model.dto.UnitDTO;
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
    public Object getUnitsByCourse(Integer courseId, String email) {
        return unitDAO.getUnitsByCourse(courseId, email);
    }

    @Override
    public Object getUnit(Integer groupId, String email) {
        return unitDAO.getUnit(groupId, email);
    }

    @Override
    public void createUnit(UnitDTO unitDTO, String email) {
        unitDAO.createUnit(unitDTO, email);
    }

    @Override
    public void changeUnit(UnitDTO unitDTO, String email) {
        unitDAO.changeUnit(unitDTO, email);
    }

    @Override
    public void deleteUnit(int id, String email) {
        unitDAO.deleteUnit(id, email);
    }

    @Override
    public Object getUnitForStudent(Integer unitId, String student) {
        return unitDAO.getUnitForStudent(unitId, student);
    }

    @Override
    public Object getUnitsByCourseForStudent(Integer courseId, String student) {
        return unitDAO.getUnitByCourseForStudent(courseId, student);
    }


}
