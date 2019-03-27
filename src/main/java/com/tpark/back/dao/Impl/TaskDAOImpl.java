package com.tpark.back.dao.Impl;

import com.tpark.back.dao.TaskDAO;
import com.tpark.back.model.Task;
import com.tpark.back.model.TaskUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TaskDAOImpl implements TaskDAO {

    private final JdbcTemplate jdbc;

    private final static TaskMapper taskMapper = new TaskMapper();
    private final SchoolIDDAO schoolIDDAO;


    @Autowired
    public TaskDAOImpl(JdbcTemplate jdbc, SchoolIDDAO schoolIDDAO) {
        this.jdbc = jdbc;
        this.schoolIDDAO = schoolIDDAO;
    }



    @Override
    public void deleteTask(String admin ,int id) {
        Integer school_id = schoolIDDAO.GetSchoolId(admin);
        final String sql ="DELETE FROM task WHERE id = ? AND school_id=?;";
        jdbc.update(sql,id,school_id);
    }

    @Override
    public void changeTask(String admin ,Task task) {

        Integer school_id = schoolIDDAO.GetSchoolId(admin);
        final String sql = "UPDATE task SET description = ?, task_ref = ?, task_type=? WHERE id = ? AND school_id=?;";
        jdbc.update(sql,task.getDescription(), task.getTask_ref(), task.getTask_type(), task.getId(),school_id);

    }

    @Override
    public void createTask(String admin ,Task task) {
        Integer school_id = schoolIDDAO.GetSchoolId(admin);
        final String sql = "INSERT INTO task (description, task_ref, task_type,school_id) VALUES (?, ?, ?,?);";
        jdbc.update(sql, task.getDescription(), task.getTask_ref(), task.getTask_type(),school_id);
    }

    @Override
    public Task getTask(String admin ,Integer taskId) {
        Integer school_id = schoolIDDAO.GetSchoolId(admin);
        final String sql = "SELECT * FROM task WHERE id = ?  and school_id = ? LIMIT 1;";
        return jdbc.queryForObject(sql, taskMapper, taskId, school_id);
    }

    @Override
    public List<Task> getTasksByUnit(String admin ,Integer unitId) {
        Integer school_id = schoolIDDAO.GetSchoolId(admin);
        final String sql = "SELECT * FROM task JOIN task_unit ON task_unit.unit_id = ? AND task.id = task_unit.task_id AND school_id=?;";
        return jdbc.query(sql, taskMapper, unitId, school_id);
    }

    @Override
    public List<Task> getAllTasks(String admin) {
        Integer school_id = schoolIDDAO.GetSchoolId(admin);
        final String sql = "SELECT * FROM task WHERE school_id=?;";
        return jdbc.query(sql, taskMapper, school_id);
    }

    public Object getTaskStudent(Integer taskId, String student) {
        final String sql = "SELECT * FROM task JOIN ( task_unit JOIN" +
                "(SELECT unit.id FROM unit JOIN" +
                "((SELECT email, group_id FROM student JOIN student_group sg " +
                "on student.id = sg.student_id AND student.email = ?)" +
                " AS rg JOIN (SELECT id, course_id FROM group_course) AS gc ON gc.id = rg.group_id) " +
                "AS g ON g.course_id = unit.course_id) AS units" +
                " ON units.id = task_unit.unit_id)" +
                " AS task_t ON task_t.task_id = task.id;";
        return jdbc.queryForObject(sql, taskMapper, student, taskId);
    }

    public Object getTasksByUnitStudent(Integer unitId, String student) {
        final String sql = "SELECT * FROM task JOIN ( task_unit JOIN " +
                "(SELECT unit.id FROM unit JOIN" +
                "((SELECT email, group_id FROM student JOIN student_group sg " +
                "on student.id = sg.student_id AND student.email = ?)" +
                " AS rg JOIN (SELECT id, course_id FROM group_course) AS gc ON gc.id = rg.group_id) " +
                "AS g ON g.course_id = unit.course_id AND unit.id = ?) AS units" +
                " ON units.id = task_unit.unit_id) AS task_t ON task_t.task_id = task.id;";
        return jdbc.query(sql, taskMapper, student, unitId);
    }

    @Override
    public void addTaskToUnit(String user, TaskUnit task) {
        Integer school_id = schoolIDDAO.GetSchoolId(user);
        String sql  = "SELECT * FROM task WHERE task.id = ? AND task.school_id = ?;";
        if(jdbc.query(sql, taskMapper, task.getTaskID(), school_id)!= null) {
            sql = "INSERT INTO task_unit (unit_id, task_id) VALUES (?, ?);";
            jdbc.update(sql, task.getUnitID(), task.getTaskID());
        }

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
