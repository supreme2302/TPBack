package com.tpark.back.dao.Impl;

import com.tpark.back.dao.UnitDAO;
import com.tpark.back.mapper.UnitMapper;
import com.tpark.back.model.domain.UnitDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class UnitDAOImpl implements UnitDAO {

    private final JdbcTemplate jdbc;

    private final UnitMapper unitMapper;
    private final SchoolIDDAO schoolIDDAO;

    @Autowired
    public UnitDAOImpl(JdbcTemplate jdbc,
                       SchoolIDDAO schoolIDDAO,
                       UnitMapper unitMapper) {
        this.jdbc = jdbc;
        this.schoolIDDAO = schoolIDDAO;
        this.unitMapper = unitMapper;
    }

    private List<UnitDomain> sort(List<UnitDomain> data) {
        List<UnitDomain> lst = new ArrayList<>();
        for (int i = 0; i < data.size(); ++i){
            if (data.get(i).getPrev_pos() == 0){
                lst.add(data.get(i));
                data.remove(i);
                break;
            }
        }
        while (data.size() > 0 && lst.get(lst.size() - 1).getNext_pos() != 0) {
            for (int i = 0; i < data.size(); ++i) {
                if (data.get(i).getId().equals(lst.get(lst.size() - 1).getNext_pos())) {
                    lst.add(data.get(i));
                    data.remove(i);
                    break;
                }
            }
        }
        return lst;
    }

    @Override
    public List<UnitDomain> getUnitsByCourse(Integer courseId, String email) {
        Integer schoolId = schoolIDDAO.getSchoolId(email);
        final String sql = "SELECT * FROM unit WHERE course_id = ? AND school_id = ?;";
        return sort(jdbc.query(sql, unitMapper, courseId, schoolId));
    }

    @Override
    public UnitDomain getUnit(Integer unitId, String email) {
        Integer schoolId = schoolIDDAO.getSchoolId(email);
        final String sql = "SELECT * FROM unit WHERE id = ? AND school_id=?;";
        return jdbc.queryForObject(sql, unitMapper, unitId, schoolId);

    }

    @Override
    @Transactional
    public void createUnit(UnitDomain unitDomain, String email) {
        Integer schoolId = schoolIDDAO.getSchoolId(email);
        String sql = "SELECT * FROM unit WHERE course_id = ? AND  school_id = ? AND next_unit IS null;";
        try {
            UnitDomain obj = jdbc.queryForObject(sql, unitMapper, unitDomain.getCourse_id(), schoolId);
            sql = "INSERT INTO unit(unit_name, course_id, description, school_id, prev_unit, tags) VALUES (?, ?, ?, ?, ?, ?);";
            jdbc.update(sql, unitDomain.getUnit_name(), unitDomain.getCourse_id(),unitDomain.getDescription(), schoolId, obj.getId(), unitDomain.getTags());
            sql = "SELECT * FROM unit WHERE unit_name = ? AND course_id=?";
            UnitDomain res = jdbc.queryForObject(sql, unitMapper, unitDomain.getUnit_name(), unitDomain.getCourse_id());
            //TODO unique constraint на имя и курс
            sql = "UPDATE unit SET next_unit=? WHERE id = ?";
            jdbc.update(sql,res.getId(),obj.getId());
            unitDomain.setId(res.getId());
            unitDomain.setCourse_id(res.getCourse_id());
            unitDomain.setNext_pos(res.getNext_pos());
            unitDomain.setPrev_pos(res.getPrev_pos());
        } catch (EmptyResultDataAccessException exept) {
            sql = "INSERT INTO unit(unit_name, course_id, description,school_id, tags) VALUES (?, ?, ?, ?, ?) RETURNING id";
            Integer id = jdbc.queryForObject(sql, Integer.class,
                    unitDomain.getUnit_name(), unitDomain.getCourse_id(),unitDomain.getDescription(), schoolId, unitDomain.getTags());
            unitDomain.setId(id);
        }
    }

    @Override
    @Transactional
    public void changeUnit(UnitDomain unitDomain, String email) {
        Integer schoolId = schoolIDDAO.getSchoolId(email);
        String sql ="SELECT * FROM unit WHERE id = ? AND school_id=?;";
        UnitDomain old = jdbc.queryForObject(sql,unitMapper,unitDomain.getId(),schoolId);
        if ((old.getNext_pos().equals(unitDomain.getNext_pos()) && old.getPrev_pos().equals(unitDomain.getPrev_pos())) ||(unitDomain.getNext_pos() == null && unitDomain.getPrev_pos() == null)) {
            sql = "UPDATE unit SET unit_name = ?, course_id = ?, description = ?, tags = ? WHERE id = ? AND school_id = ?;";
            jdbc.update(sql, unitDomain.getUnit_name(), unitDomain.getCourse_id(), unitDomain.getDescription(),
                    unitDomain.getTags(), unitDomain.getId(), schoolId);
        } else {
            // TODO возможность бага, когда будет два prev
            sql = "UPDATE unit SET next_unit = ? WHERE id = ?";
            jdbc.update(sql,old.getNext_pos(),old.getPrev_pos());
            sql = "UPDATE unit SET prev_unit = ? WHERE id = ?";
            jdbc.update(sql,old.getPrev_pos(),old.getNext_pos());

            sql = "UPDATE unit SET next_unit = ? WHERE id = ?";
            jdbc.update(sql,unitDomain.getId(),unitDomain.getPrev_pos());
            sql = "UPDATE unit SET prev_unit = ? WHERE id = ?";
            jdbc.update(sql,unitDomain.getId(),unitDomain.getNext_pos());
            sql = "UPDATE unit SET unit_name = ?, course_id = ?, prev_unit=?, next_unit = ?, description=? WHERE id = ? AND school_id=?;";
            jdbc.update(sql, unitDomain.getUnit_name(), unitDomain.getCourse_id(), unitDomain.getPrev_pos(), unitDomain.getNext_pos(), unitDomain.getDescription(), unitDomain.getId(), schoolId);
        }
    }

    @Transactional
    @Override
    public void deleteUnit(int id, String email) {
        Integer schoolId = schoolIDDAO.getSchoolId(email);
        Integer temp = 0;

        String sql ="SELECT * FROM unit WHERE id=?;";
        UnitDomain old = jdbc.queryForObject(sql,unitMapper,id);
        if( old.getNext_pos() == 0 ) {
            sql = "UPDATE unit SET next_unit = ? WHERE id = ?";
            jdbc.update(sql,null,old.getPrev_pos());
        }
        else {
            sql = "UPDATE unit SET next_unit = ? WHERE id = ?";
            jdbc.update(sql, old.getNext_pos(), old.getPrev_pos());
        }
        if(old.getPrev_pos()==0){

            sql = "UPDATE unit SET prev_unit = ? WHERE id = ?";
            jdbc.update(sql, null, old.getNext_pos());
        }
        else {
            sql = "UPDATE unit SET prev_unit = ? WHERE id = ?";
            jdbc.update(sql, old.getPrev_pos(), old.getNext_pos());
        }

        sql ="SELECT course_id FROM unit WHERE id = ? AND school_id = ?;";
        temp = jdbc.queryForObject(sql, ((resultSet, i) -> resultSet.getInt("course_id")), id, schoolId);
        sql = "DELETE FROM task_unit WHERE unit_id = ? ;";
        jdbc.update(sql, id);
        sql = "UPDATE group_course SET current_unit = NULL WHERE course_id=? AND school_id = ?";
        jdbc.update(sql,temp, schoolId);
        sql ="DELETE FROM unit WHERE id = ? AND school_id = ?;";
        jdbc.update(sql,id, schoolId);
    }

    @Transactional
    public void deleteUnitsByCourse(int id, String email, int schoolId) {
        String sql = "SELECT * FROM unit WHERE course_id = ? AND school_id = ?;";
        List<UnitDomain> lst = jdbc.query(sql, unitMapper, id, schoolId);
        for (UnitDomain aLst : lst) {
            sql = "DELETE FROM task_unit WHERE unit_id = ? ;";
            jdbc.update(sql, aLst.getId());
        }
        sql ="DELETE FROM unit WHERE course_id = ? AND school_id = ?;";
        jdbc.update(sql,id, schoolId);
    }

    @Override
    public UnitDomain getUnitForStudent(Integer unitId, String student) {
        final String sql = "SELECT * FROM unit JOIN" +
                "((SELECT email, group_id FROM student JOIN student_group sg " +
                "on student.id = sg.student_id AND lower(student.email) = lower(?))" +
                " AS rg JOIN group_course ON group_course.id = rg.group_id) " +
                "AS g ON g.course_id = unit.course_id AND unit.id = ?;";
        return jdbc.queryForObject(sql, unitMapper, student, unitId);
    }

    @Override
    public List<UnitDomain> getUnitByCourseForStudent(Integer courseId, String student) {
        final String sql = "SELECT * FROM unit JOIN" +
                "((SELECT email, group_id FROM student JOIN student_group sg " +
                "on student.id = sg.student_id AND lower(student.email) = lower(?))" +
                " AS rg JOIN group_course ON group_course.id = rg.group_id) " +
                "AS g ON g.course_id = unit.course_id AND unit.course_id = ?;";
        return sort(jdbc.query(sql, unitMapper, student, courseId));
    }

    @Override
    public List<UnitDomain> getUnits(String user) {
        Integer schoolId = schoolIDDAO.getSchoolId(user);
        final String sql = "SELECT * FROM unit WHERE school_id = ?;";
        return jdbc.query(sql, unitMapper, schoolId);
    }

    @Override
    public List<UnitDomain> getUnitsForStudent(String student) {
        final String sql = "SELECT * FROM unit JOIN" +
                "((SELECT email, group_id FROM student JOIN student_group sg " +
                "on student.id = sg.student_id AND lower(student.email) = lower(?))" +
                " AS rg JOIN group_course ON group_course.id = rg.group_id) " +
                "AS g ON g.course_id = unit.course_id ;";
        return sort(jdbc.query(sql, unitMapper, student ));
    }
}
