package com.tpark.back.dao.Impl;

import com.tpark.back.dao.GroupDAO;
import com.tpark.back.model.dto.GroupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Repository
public class GroupDAOImpl implements GroupDAO {

    private final JdbcTemplate jdbc;
    private final static GroupMapper groupMapper = new GroupMapper();
    private final SchoolIDDAO schoolIDDAO;



    @Autowired
    public GroupDAOImpl(JdbcTemplate jdbc, SchoolIDDAO schoolIDDAO) {
        this.jdbc = jdbc;
        this.schoolIDDAO = schoolIDDAO;
    }


    @Override
    public void deleteGroup(int id, String email) {
        Integer school_id = schoolIDDAO.GetSchoolId(email);
        final String sql ="DELETE FROM group_course WHERE id = ? and school_id=?;";
        jdbc.update(sql,id, school_id);
    }

    @Override
    public void changeGroup(GroupDTO groupDTO, String email) {
        Integer school_id = schoolIDDAO.GetSchoolId(email);
        final String sql = "UPDATE group_course SET group_name = ?, course_id = ?, current_unit=? WHERE id = ? AND  school_id =?;";
        jdbc.update(sql, groupDTO.getName(), groupDTO.getCourse_id(), groupDTO.getCurr_unit(), groupDTO.getId(), school_id);
    }

    @Override
    public void createGroup(GroupDTO groupDTO, String email) {
        Integer school_id = schoolIDDAO.GetSchoolId(email);
        final String sql = "INSERT INTO group_course(group_name, course_id, current_unit,school_id) VALUES (?, ?, ?,?);";
        jdbc.update(sql, groupDTO.getName(), groupDTO.getCourse_id(), groupDTO.getCurr_unit(), school_id);
    }

    @Override
    public GroupDTO getGroup(int groupID, String email) {
        Integer school_id = schoolIDDAO.GetSchoolId(email);
        final String sql = "SELECT * FROM group_course WHERE id = ? AND  school_id = ? LIMIT 1;";
        return jdbc.queryForObject(sql, groupMapper, groupID, school_id);

    }

    @Override
    public List<GroupDTO> getGroupsByCourse(int courseID, String email) {
        Integer school_id = schoolIDDAO.GetSchoolId(email);
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
                "AND  student.email = ?) AS gr_r " +
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
                "AND  student.email = ?) AS gr_r " +
                "ON group_course.id = ? AND group_course.id = gr_r.group_id LIMIT 1;";
        return jdbc.queryForObject(sql, groupMapper, student, id);
    }

    public static class GroupMapper implements RowMapper<GroupDTO> {
        @Override
        public GroupDTO mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            GroupDTO groupDTO = new GroupDTO();
            groupDTO.setId(resultSet.getInt("id"));
            groupDTO.setName(resultSet.getString("group_name"));
            groupDTO.setCourse_id(resultSet.getInt("course_id"));
            groupDTO.setCurr_unit(resultSet.getInt("current_unit"));
            groupDTO.setDescription(resultSet.getString("description"));
            //TODO: Нужно добавить Timestamp


            return groupDTO;
        }
    }
}
