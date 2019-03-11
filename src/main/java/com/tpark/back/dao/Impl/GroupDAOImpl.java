package com.tpark.back.dao.Impl;

import com.tpark.back.dao.GroupDAO;
import com.tpark.back.model.Group;
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

    @Autowired
    public GroupDAOImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }


    @Override
    public void deleteGroup(int id) {
        final String sql ="DELETE FROM group_course WHERE id = ?;";
        jdbc.update(sql,id);
    }

    @Override
    public void changeGroup(Group group) {
        final String sql = "UPDATE group_course SET group_name = ?, course_id = ?, current_unit=? WHERE id = ?;";
        jdbc.update(sql,group.getName(), group.getCourse_id(), group.getCurr_unit(), group.getId());
    }

    @Override
    public void createGroup(Group group) {
        final String sql = "INSERT INTO group_course(group_name, course_id, current_unit) VALUES (?, ?, ?);";
        jdbc.update(sql, group.getName(), group.getCourse_id(), group.getCurr_unit());
    }

    @Override
    public Group getGroup(int groupID) {
        final String sql = "SELECT * FROM group_course WHERE id = ? LIMIT 1;";
        return jdbc.queryForObject(sql, groupMapper, groupID);

    }

    @Override
    public List<Group> getGroupsByCourse(int courseID) {
        final String sql = "SELECT * FROM group_course WHERE course_id = ?;";
        return jdbc.query(sql, groupMapper, courseID);

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
