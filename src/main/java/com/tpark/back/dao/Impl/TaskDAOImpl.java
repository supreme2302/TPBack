package com.tpark.back.dao.Impl;

import com.tpark.back.dao.TaskDAO;
import com.tpark.back.model.dto.TaskDTO;
import com.tpark.back.model.dto.TaskUnitDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletContext;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TaskDAOImpl implements TaskDAO {

    private final JdbcTemplate jdbc;

    private final static TaskMapper taskMapper = new TaskMapper();
    private final SchoolIDDAO schoolIDDAO;
    private final String FILE_PATH = "~/FinalFight/TPBack/src/main/resources/taskVault/";

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
    //TODO: дописать таску(работа с unit_id и Jsonищем)

    @Override
    public void changeTask(String admin , TaskDTO taskDTO) {

        Integer school_id = schoolIDDAO.GetSchoolId(admin);
        String sql = "UPDATE task SET name = ?, task_val = ?, task_type=? WHERE id = ? AND school_id=?;";
        jdbc.update(sql, taskDTO.getName(), taskDTO.getTask(), taskDTO.getTask_type(), taskDTO.getId(),school_id);
        sql = "DELETE FROM task_unit WHERE task_id = ?";
        jdbc.update(sql,taskDTO.getId());
        Integer i = 0;
        while (i<taskDTO.getUnit_id().size()){
            sql = "INSERT INTO task_unit (task_id, unit_id) VALUES (?,?);";
            jdbc.update(sql,taskDTO.getId(),taskDTO.getUnit_id().get(i));
            i++;
        }
    }

    @Override
    @Transactional
    public void createTask(String admin , TaskDTO taskDTO) {
        Integer school_id = schoolIDDAO.GetSchoolId(admin);
        String sql = "INSERT INTO task (name, task_val, task_type,school_id) VALUES (?, ?, ?,?);";
        jdbc.update(sql, taskDTO.getName(), taskDTO.getTask(), taskDTO.getTask_type(),school_id);
        sql = "SELECT * FROM task WHERE name = ? AND school_id=?";
        TaskDTO created = jdbc.queryForObject(sql, taskMapper, taskDTO.getName(),school_id);
        Integer i = 0;
        while (i<taskDTO.getUnit_id().size()){
            sql = "INSERT INTO task_unit (task_id, unit_id) VALUES (?,?);";
            jdbc.update(sql,created.getId(),taskDTO.getUnit_id().get(i));
            i++;
        }
    }

    @Override
    public TaskDTO getTask(String admin , Integer taskId) {
        Integer school_id = schoolIDDAO.GetSchoolId(admin);
        final String sql = "SELECT * FROM task WHERE id = ?  and school_id = ? LIMIT 1;";
        return jdbc.queryForObject(sql, taskMapper, taskId, school_id);
    }

    @Override
    public List<TaskDTO> getTasksByUnit(String admin , Integer unitId) {
        Integer school_id = schoolIDDAO.GetSchoolId(admin);
        final String sql = "SELECT * FROM task JOIN task_unit ON task_unit.unit_id = ? AND task.id = task_unit.task_id AND school_id=?;";
        return jdbc.query(sql, taskMapper, unitId, school_id);
    }

    @Override
    public List<TaskDTO> getAllTasks(String admin) {
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
    public void addTaskToUnit(String user, TaskUnitDTO task) {
        Integer school_id = schoolIDDAO.GetSchoolId(user);
        String sql  = "SELECT * FROM task WHERE task.id = ? AND task.school_id = ?;";
        if(jdbc.query(sql, taskMapper, task.getTaskID(), school_id)!= null) {
            sql = "INSERT INTO task_unit (unit_id, task_id) VALUES (?, ?);";
            jdbc.update(sql, task.getUnitID(), task.getTaskID());
        }

    }

    private static final class TaskMapper implements RowMapper<TaskDTO> {
        @Override
        public TaskDTO mapRow(ResultSet resultSet, int i) throws SQLException {
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setId(resultSet.getInt("id"));
            taskDTO.setTask_type(resultSet.getInt("task_type"));
            taskDTO.setName(resultSet.getString("name"));
            taskDTO.setTask(resultSet.getObject("task_val"));
            return taskDTO;
        }
    }
}
