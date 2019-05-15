package com.tpark.back.dao.Impl;

import com.tpark.back.dao.GroupDAO;
import com.tpark.back.mapper.GroupMapper;
import com.tpark.back.model.dto.GroupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@Transactional
public class GroupDAOImpl implements GroupDAO {

    private final JdbcTemplate jdbc;
    private final GroupMapper groupMapper;
    private final SchoolIDDAO schoolIDDAO;


    @Autowired
    public GroupDAOImpl(JdbcTemplate jdbc,
                        SchoolIDDAO schoolIDDAO,
                        GroupMapper groupMapper) {
        this.jdbc = jdbc;
        this.schoolIDDAO = schoolIDDAO;
        this.groupMapper = groupMapper;
    }


    @Override
    @Transactional
    public void deleteGroup(int id, String email) {

        Integer school_id = schoolIDDAO.getSchoolId(email);
        String sql ="SELECT school_id FROM group_course WHERE id = ?;";
        Integer Sid = jdbc.queryForObject(sql, ((resultSet, i) -> resultSet.getInt("school_id")) ,id);
        if (Sid.equals(school_id)) {
            sql = "DELETE  FROM  student_group  WHERE group_id = ?;";
            jdbc.update(sql, id);
            sql = "DELETE FROM group_course WHERE id = ?;";
            jdbc.update(sql, id);
        }
    }

    @Override
    public void deleteGroupByCourseIdAndSchoolId(int courseId, int schoolId) {
        List<Integer> ids = jdbc.queryForList("SELECT id FROM group_course " +
                "WHERE course_id = ? AND school_id = ? ", Integer.class, courseId, schoolId);
        for (Integer id: ids) {
            jdbc.update("DELETE FROM student_group WHERE group_id = ?", id);
        }
        jdbc.update("DELETE FROM group_course WHERE course_id = ? AND school_id = ?",
                courseId, schoolId);
    }

    @Override
    @Transactional
    public void changeGroup(GroupDTO groupDTO, String email) {
        Integer school_id = schoolIDDAO.getSchoolId(email);
        String sql = "UPDATE group_course SET group_name = ?, description = ?, course_id = ?  WHERE id = ? AND  school_id = ?;";
        jdbc.update(sql, groupDTO.getName(), groupDTO.getDescription(), groupDTO.getCourse_id(), groupDTO.getId(), school_id);
        if (groupDTO.getCurr_unit() != 0) {
            try {
                sql = "SELECT id FROM unit WHERE course_id=? AND id=?;";
                Integer id = jdbc.queryForObject(sql, Integer.class,
                        groupDTO.getCourse_id(), groupDTO.getCurr_unit());
                sql = "UPDATE group_course SET current_unit=? WHERE id=? AND school_id=?;";
                jdbc.update(sql, groupDTO.getCurr_unit(), groupDTO.getId(), school_id);
            } catch (EmptyResultDataAccessException e){
                sql = "UPDATE group_course SET current_unit=? WHERE id=? AND school_id=?;";
                jdbc.update(sql, null, groupDTO.getId(), school_id);
            }
        } else {
            sql = "UPDATE group_course SET current_unit=? WHERE id=? AND school_id=?;";
            jdbc.update(sql, null, groupDTO.getId(), school_id);
        }
    }

    @Override
    public void createGroup(GroupDTO groupDTO, String email) {
        Integer school_id = schoolIDDAO.getSchoolId(email);
        final String sql = "INSERT INTO group_course(group_name, course_id, current_unit,school_id, description) VALUES (?, ?, ?,?,?);";
        if (groupDTO.getCurr_unit() == 0) {
            jdbc.update(sql, groupDTO.getName(), groupDTO.getCourse_id(), null, school_id, groupDTO.getDescription());
        }
        else {
            jdbc.update(sql, groupDTO.getName(), groupDTO.getCourse_id(), groupDTO.getCurr_unit(), school_id, groupDTO.getDescription());
        }
        groupDTO.setCourse_id(school_id);
    }

    @Override
    public GroupDTO getGroup(int groupID, String email) {
        Integer school_id = schoolIDDAO.getSchoolId(email);
        final String sql = "SELECT * FROM group_course WHERE id = ? AND  school_id = ? LIMIT 1;";
        return jdbc.queryForObject(sql, groupMapper, groupID, school_id);

    }

    @Override
    public List<GroupDTO> getGroupsByCourse(int courseID, String email) {
        Integer school_id = schoolIDDAO.getSchoolId(email);
        final String sql = "SELECT * FROM group_course WHERE course_id = ? AND school_id = ?;";
        return jdbc.query(sql, groupMapper, courseID, school_id);

    }

    @Override
    public List<GroupDTO> getGroupsForAdmin(String user) {
        final String sql = "SELECT * FROM group_course JOIN " +
                "(course JOIN (SELECT school_id, email FROM admin) AS t ON t.school_id = course.school_id AND lower(t.email) = lower(?)) AS corse" +
                " ON corse.id = group_course.course_id;";
        return jdbc.query(sql, groupMapper,user);
    }

    @Override
    public List<GroupDTO> getGroupsForStudent(String student) {
        final String sql = "SELECT * FROM group_course JOIN " +
                "(student JOIN student_group ON student.id = student_group.student_id " +
                "AND  lower(student.email) = lower(?)) AS gr_r " +
                "ON group_course.id = gr_r.group_id;";
        return jdbc.query(sql, groupMapper, student);
    }

    public GroupDTO getGroupByStudent(Integer courseId, String student) {
        final String sql = "SELECT * FROM group_course  JOIN " +
                "(student JOIN student_group " +
                "ON student.id = student_group.student_id AND  lower(student.email) = lower(?)) AS gr_id " +
                "ON gr_id.group_id = group_course.id AND  group_course = (?) LIMIT 1;";
        return jdbc.queryForObject(sql, groupMapper,student, courseId);
    }

    public GroupDTO getGroupForStudent(String student, Integer id) {
        final String sql = "SELECT * FROM group_course JOIN " +
                "(student JOIN student_group ON student.id = student_group.student_id " +
                "AND  lower(student.email) = lower(?)) AS gr_r " +
                "ON group_course.id = ? AND group_course.id = gr_r.group_id LIMIT 1;";
        return jdbc.queryForObject(sql, groupMapper, student, id);
    }

}
