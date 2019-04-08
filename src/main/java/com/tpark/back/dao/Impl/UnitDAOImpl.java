package com.tpark.back.dao.Impl;

import com.tpark.back.dao.UnitDAO;
import com.tpark.back.mapper.UnitMapper;
import com.tpark.back.model.dto.UnitDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    private List<UnitDTO> sort(List<UnitDTO> data) {
        List<UnitDTO> lst = new ArrayList<>();
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
    public List<UnitDTO> getUnitsByCourse(Integer courseId, String email) {
        Integer schoolId = schoolIDDAO.getSchoolId(email);
        final String sql = "SELECT * FROM unit WHERE course_id = ? AND school_id = ?;";
        return sort(jdbc.query(sql, unitMapper, courseId, schoolId));
    }

    @Override
    public UnitDTO getUnit(Integer unitId, String email) {
        Integer schoolId = schoolIDDAO.getSchoolId(email);
        final String sql = "SELECT * FROM unit WHERE id = ? AND school_id=?;";
        return jdbc.queryForObject(sql, unitMapper, unitId, schoolId);

    }

    @Override
    @Transactional
    public void createUnit(UnitDTO unitDTO, String email) {
        Integer schoolId = schoolIDDAO.getSchoolId(email);
        String sql = "SELECT * FROM unit WHERE course_id = ? AND  school_id =? AND next_unit IS null;";
        try {
            UnitDTO obj = jdbc.queryForObject(sql,unitMapper,unitDTO.getCourse_id(),schoolId);
            sql = "INSERT INTO unit(unit_name, course_id, description,school_id,prev_unit) VALUES (?, ?, ?,?,?);";
            jdbc.update(sql, unitDTO.getUnit_name(), unitDTO.getCourse_id(),unitDTO.getDescription(), schoolId, obj.getId());
            sql = "SELECT * FROM unit WHERE unit_name = ? AND course_id=?";
            UnitDTO res = jdbc.queryForObject(sql, unitMapper, unitDTO.getUnit_name(), unitDTO.getCourse_id());
            //TODO unique constraint на имя и курс
            sql = "UPDATE unit SET next_unit=? WHERE id = ?";
            jdbc.update(sql,res.getId(),obj.getId());
            unitDTO.setId(res.getId());
            unitDTO.setCourse_id(res.getCourse_id());
            unitDTO.setNext_pos(res.getNext_pos());
            unitDTO.setPrev_pos(res.getPrev_pos());
        } catch (EmptyResultDataAccessException exept) {
            sql = "INSERT INTO unit(unit_name, course_id, description,school_id) VALUES (?, ?, ?,?) RETURNING id";
            Integer id = jdbc.queryForObject(sql, Integer.class,
                    unitDTO.getUnit_name(), unitDTO.getCourse_id(),unitDTO.getDescription(), schoolId);
            unitDTO.setId(id);
        }
    }

    @Override
    @Transactional
    public void changeUnit(UnitDTO unitDTO, String email) {
        Integer schoolId = schoolIDDAO.getSchoolId(email);
        String sql ="SELECT * FROM unit WHERE id = ? AND school_id=?;";
        UnitDTO old = jdbc.queryForObject(sql,unitMapper,unitDTO.getId(),schoolId);
        if ((old.getNext_pos() == unitDTO.getNext_pos() && old.getPrev_pos() == unitDTO.getPrev_pos()) ||(unitDTO.getNext_pos() == null && unitDTO.getPrev_pos() == null)) {
            sql = "UPDATE unit SET unit_name = ?, course_id = ?, description=? WHERE id = ? AND school_id=?;";
            jdbc.update(sql, unitDTO.getUnit_name(), unitDTO.getCourse_id(), unitDTO.getDescription(), unitDTO.getId(), schoolId);
        } else {
            // TODO возможность бага, когда будет два prev
            sql = "UPDATE unit SET next_unit = ? WHERE id = ?";
            jdbc.update(sql,old.getNext_pos(),old.getPrev_pos());
            sql = "UPDATE unit SET prev_unit = ? WHERE id = ?";
            jdbc.update(sql,old.getPrev_pos(),old.getNext_pos());

            sql = "UPDATE unit SET next_unit = ? WHERE id = ?";
            jdbc.update(sql,unitDTO.getId(),unitDTO.getPrev_pos());
            sql = "UPDATE unit SET prev_unit = ? WHERE id = ?";
            jdbc.update(sql,unitDTO.getId(),unitDTO.getNext_pos());
            sql = "UPDATE unit SET unit_name = ?, course_id = ?, prev_unit=?, next_unit = ?, description=? WHERE id = ? AND school_id=?;";
            jdbc.update(sql, unitDTO.getUnit_name(), unitDTO.getCourse_id(), unitDTO.getPrev_pos(), unitDTO.getNext_pos(), unitDTO.getDescription(), unitDTO.getId(), schoolId);
        }
    }

    @Transactional
    @Override
    public void deleteUnit(int id, String email) {
        Integer schoolId = schoolIDDAO.getSchoolId(email);
        Integer temp = 0;


        String sql ="SELECT * FROM unit WHERE id=?;";
        UnitDTO old = jdbc.queryForObject(sql,unitMapper,id);
        if(old.getNext_pos()==0) {
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
    public void deleteUnitsByCourse(int id, String email) {
        Integer schoolId = schoolIDDAO.getSchoolId(email);
        String sql = "SELECT * FROM unit WHERE course_id = ? AND school_id = ?;";
        List<UnitDTO> lst = jdbc.query(sql, unitMapper, id, schoolId);
        for (UnitDTO aLst : lst) {
            sql = "DELETE FROM task_unit WHERE unit_id = ? ;";
            jdbc.update(sql, aLst.getId());
        }
        sql ="DELETE FROM unit WHERE course_id = ? AND school_id = ?;";
        jdbc.update(sql,id, schoolId);
    }

    @Override
    public UnitDTO getUnitForStudent(Integer unitId, String student) {
        final String sql = "SELECT * FROM unit JOIN" +
                "((SELECT email, group_id FROM student JOIN student_group sg " +
                "on student.id = sg.student_id AND student.email = ?)" +
                " AS rg JOIN group_course ON group_course.id = rg.group_id) " +
                "AS g ON g.course_id = unit.course_id AND unit.id = ?;";
        return jdbc.queryForObject(sql, unitMapper, student, unitId);
    }

    @Override
    public List<UnitDTO> getUnitByCourseForStudent(Integer courseId, String student) {
        final String sql = "SELECT * FROM unit JOIN" +
                "((SELECT email, group_id FROM student JOIN student_group sg " +
                "on student.id = sg.student_id AND student.email = ?)" +
                " AS rg JOIN group_course ON group_course.id = rg.group_id) " +
                "AS g ON g.course_id = unit.course_id AND unit.course_id = ?;";
        return sort(jdbc.query(sql, unitMapper, student, courseId));
    }

//    private static final class IntMapper implements RowMapper<Integer> {
//        @Override
//        public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
//            int temp = 0;
//            temp = resultSet.getInt("course_id");
//            return temp;
//        }
//    }
}
