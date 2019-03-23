package com.tpark.back.dao.Impl;

import com.tpark.back.dao.SchoolIDDAO;
import com.tpark.back.dao.TaskDAO;
import com.tpark.back.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TaskDAOImpl implements TaskDAO {

    private final DataSource dataSource;
    private JdbcTemplate jdbc;

    private final static TaskMapper taskMapper = new TaskMapper();
    private final SchoolIDDAO schoolIDDAO;


    @Autowired
    public TaskDAOImpl(DataSource dataSource, SchoolIDDAO schoolIDDAO) {
        this.dataSource = dataSource;
        this.schoolIDDAO = schoolIDDAO;
    }

    @PostConstruct
    public void init() {
        this.jdbc = new JdbcTemplate(this.dataSource);
    }

    @Override
    public void deleteTask(String admin ,int id) {
        Integer school_id = schoolIDDAO.getSchoolId(admin);
        final String sql ="DELETE FROM task WHERE id = ? AND school_id=?;";
        jdbc.update(sql,id,school_id);
    }

    @Override
    public void changeTask(String admin ,Task task) {

        Integer school_id = schoolIDDAO.getSchoolId(admin);
        final String sql = "UPDATE task SET description = ?, task_ref = ?, task_type=? WHERE id = ? AND school_id=?;";
        jdbc.update(sql,task.getDescription(), task.getTask_ref(), task.getTask_type(), task.getId(),school_id);

    }

    @Override
    public void createTask(String admin ,Task task) {
        Integer school_id = schoolIDDAO.getSchoolId(admin);
        final String sql = "INSERT INTO task (description, task_ref, task_type,school_id) VALUES (?, ?, ?,?);";
        jdbc.update(sql, task.getDescription(), task.getTask_ref(), task.getTask_type(),school_id);
    }

    @Override
    public Task getTask(String admin ,Integer taskId) {
        Integer school_id = schoolIDDAO.getSchoolId(admin);
        final String sql = "SELECT * FROM task WHERE id = ?  and school_id = ? LIMIT 1;";
        return jdbc.queryForObject(sql, taskMapper, taskId, school_id);
    }

    @Override
    public List<Task> getTasksByUnit(String admin ,Integer unitId) {
        Integer school_id = schoolIDDAO.getSchoolId(admin);
        final String sql = "SELECT * FROM task WHERE unit_id = ? AND school_id=?;";
        return jdbc.query(sql, taskMapper, unitId, school_id);
    }

    @Override
    public List<Task> getAllTasks(String admin) {
        Integer school_id = schoolIDDAO.getSchoolId(admin);
        final String sql = "SELECT * FROM task WHERE school_id=?;";
        return jdbc.query(sql, taskMapper, school_id);
    }

    public Object getTaskStudent(Integer taskId, String student) {
        final String sql = "SELECT * FROM task JOIN (SELECT unit.id FROM unit JOIN" +
                "((SELECT email, group_id FROM student JOIN student_group sg " +
                "on student.id = sg.student_id AND student.email = ?)" +
                " AS rg JOIN (SELECT id, course_id FROM group_course) AS gc ON gc.id = rg.group_id) " +
                "AS g ON g.course_id = unit.course_id) AS units" +
                " ON task.id = ? AND units.id = task.unit_id;";
        return jdbc.queryForObject(sql, taskMapper, student, taskId);
    }

    public Object getTasksByUnitStudent(Integer unitId, String student) {
        final String sql = "SELECT * FROM task JOIN (SELECT unit.id FROM unit JOIN" +
                "((SELECT email, group_id FROM student JOIN student_group sg " +
                "on student.id = sg.student_id AND student.email = ?)" +
                " AS rg JOIN (SELECT id, course_id FROM group_course) AS gc ON gc.id = rg.group_id) " +
                "AS g ON g.course_id = unit.course_id AND unit.id = ?) AS units" +
                " ON units.id = task.unit_id;";
        return jdbc.query(sql, taskMapper, student, unitId);
    }

    private static final class TaskMapper implements RowMapper<Task> {
        @Override
        public Task mapRow(ResultSet resultSet, int i) throws SQLException {
            Task task = new Task();
            task.setId(resultSet.getInt("id"));
            task.setTask_type(resultSet.getInt("unit_id"));
            task.setTask_ref(resultSet.getString("task_ref"));
            task.setDescription(resultSet.getString("description"));
            return task;
        }
    }
}
