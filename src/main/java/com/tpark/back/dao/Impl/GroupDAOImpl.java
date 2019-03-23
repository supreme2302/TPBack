package com.tpark.back.dao.Impl;

import com.tpark.back.dao.GroupDAO;
import com.tpark.back.dao.SchoolIDDAO;
import com.tpark.back.model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Repository
public class GroupDAOImpl implements GroupDAO {

    private final DataSource dataSource;
    private JdbcTemplate jdbc;
    private final static GroupMapper groupMapper = new GroupMapper();
    private final SchoolIDDAO schoolIDDAO;



    @Autowired
    public GroupDAOImpl(DataSource dataSource, SchoolIDDAO schoolIDDAO) {
        this.dataSource = dataSource;
        this.schoolIDDAO = schoolIDDAO;
    }

    @PostConstruct
    public void init() {
        this.jdbc = new JdbcTemplate(this.dataSource);
    }


    @Override
    public void deleteGroup(int id, String email) {
        Integer school_id = schoolIDDAO.getSchoolId(email);
        final String sql ="DELETE FROM group_course WHERE id = ? and school_id=?;";
        jdbc.update(sql,id, school_id);
    }

    @Override
    public void changeGroup(Group group, String email) {
        Integer school_id = schoolIDDAO.getSchoolId(email);
        final String sql = "UPDATE group_course SET group_name = ?, course_id = ?, current_unit=? WHERE id = ? AND  school_id =?;";
        jdbc.update(sql,group.getName(), group.getCourse_id(), group.getCurr_unit(), group.getId(), school_id);
    }

    @Override
    public void createGroup(Group group, String email) {
        Integer school_id = schoolIDDAO.getSchoolId(email);
        final String sql = "INSERT INTO group_course(group_name, course_id, current_unit,school_id) VALUES (?, ?, ?,?);";
        jdbc.update(sql, group.getName(), group.getCourse_id(), group.getCurr_unit(), school_id);
    }

    @Override
    public Group getGroup(int groupID, String email) {
        Integer school_id = schoolIDDAO.getSchoolId(email);
        final String sql = "SELECT * FROM group_course WHERE id = ? AND  school_id = ? LIMIT 1;";
        return jdbc.queryForObject(sql, groupMapper, groupID, school_id);

    }

    @Override
    public List<Group> getGroupsByCourse(int courseID, String email) {
        Integer school_id = schoolIDDAO.getSchoolId(email);
        final String sql = "SELECT * FROM group_course WHERE course_id = ? AND school_id = ?;";
        return jdbc.query(sql, groupMapper, courseID, school_id);

    }

    @Override
    public List<Group> getGroupsForAdmin(String user) {
        final String sql = "SELECT * FROM group_course JOIN " +
                "(course JOIN (SELECT school_id, email FROM admin) AS t ON t.school_id = course.school_id AND lower(t.email) = lower(?)) AS corse" +
                " ON corse.id = group_course.course_id;";
        return jdbc.query(sql, groupMapper,user);
    }

    @Override
    public List<Group> getGroupsForStudent(String student) {
        final String sql = "SELECT * FROM group_course JOIN " +
                "(student JOIN student_group ON student.id = student_group.student_id " +
                "AND  student.email = ?) AS gr_r " +
                "ON group_course.id = gr_r.group_id;";
        return jdbc.query(sql, groupMapper, student);
    }

    public Group getGroupByStudent(Integer courseId, String student) {
        final String sql = "SELECT * FROM group_course  JOIN " +
                "(student JOIN student_group " +
                "ON student.id = student_group.student_id AND  lower(student.email) = lower(?)) AS gr_id " +
                "ON gr_id.group_id = group_course.id AND  group_course = (?) LIMIT 1;";
        return jdbc.queryForObject(sql, groupMapper,student, courseId);
    }

    public Group getGroupForStudent(String student, Integer id) {
        final String sql = "SELECT * FROM group_course JOIN " +
                "(student JOIN student_group ON student.id = student_group.student_id " +
                "AND  student.email = ?) AS gr_r " +
                "ON group_course.id = ? AND group_course.id = gr_r.group_id LIMIT 1;";
        return jdbc.queryForObject(sql, groupMapper, student, id);
    }

    public static class GroupMapper implements RowMapper<Group> {
        @Override
        public Group mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Group group = new Group();
            group.setId(resultSet.getInt("id"));
            group.setName(resultSet.getString("group_name"));
            group.setCourse_id(resultSet.getInt("course_id"));
            group.setCurr_unit(resultSet.getInt("current_unit"));
            //TODO: Нужно добавить Timestamp


            return group;
        }
    }
}
