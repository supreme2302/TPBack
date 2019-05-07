package com.tpark.back.dao.Impl;

import com.tpark.back.dao.TaskDAO;
import com.tpark.back.mapper.TaskMapper;
import com.tpark.back.model.dto.TaskDTO;
import com.tpark.back.model.dto.TaskUnitDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class TaskDAOImpl implements TaskDAO {

    private final JdbcTemplate jdbc;

    private final TaskMapper taskMapper;
    private final SchoolIDDAO schoolIDDAO;


    @Autowired
    public TaskDAOImpl(JdbcTemplate jdbc,
                       SchoolIDDAO schoolIDDAO,
                       TaskMapper taskMapper) {
        this.jdbc = jdbc;
        this.schoolIDDAO = schoolIDDAO;
        this.taskMapper = taskMapper;
    }

    @Override
    @Transactional
    public void deleteTask(String admin ,int id) {
        Integer school_id = schoolIDDAO.getSchoolId(admin);
        String sql = "DELETE FROM task_unit WHERE task_id=?;";
        jdbc.update(sql,id);
        sql ="DELETE FROM task WHERE id = ? AND school_id=?;";
        jdbc.update(sql,id,school_id);
    }
    //TODO: дописать таску(работа с unit_id и Jsonищем)

    @Override
    @Transactional
    public void changeTask(String admin , TaskDTO taskDTO) {

        Integer school_id = schoolIDDAO.getSchoolId(admin);
        String sql = "UPDATE task SET task_val = ?::jsonb, task_val2 = ?::jsonb, task_val3 = ?::jsonb WHERE id = ? AND school_id = ?;";
        jdbc.update(sql, taskDTO.getDataT1(), taskDTO.getDataT2(), taskDTO.getDataT3(), taskDTO.getId(),school_id);
        sql = "DELETE FROM task_unit WHERE task_id = ?";
        jdbc.update(sql,taskDTO.getId());
        int i = 0;
        while (i < taskDTO.getUnit_id().size()) {
            sql = "INSERT INTO task_unit (task_id, unit_id) VALUES (?,?);";
            jdbc.update(sql,taskDTO.getId(),taskDTO.getUnit_id().get(i));
            i++;
        }
    }

    @Transactional
    @Override
    public void createTask(String admin , TaskDTO taskDTO) {
        Integer school_id = schoolIDDAO.getSchoolId(admin);
        Integer id = 0;
        String sql = "";
        if(taskDTO.getDataT1()!=null) {
            sql = "INSERT INTO task (name, task_val, task_type, school_id) VALUES (?, ?::jsonb, ?, ?) RETURNING id";
            id = jdbc.queryForObject(sql, Integer.class, taskDTO.getName(), taskDTO.getDataT1(), taskDTO.getTask_type(), school_id);
        }else {
            if(taskDTO.getDataT2()!=null) {
                sql = "INSERT INTO task (name, task_val2, task_type,school_id) VALUES (?, ?::jsonb, ?, ?) RETURNING id";
                id = jdbc.queryForObject(sql, Integer.class, taskDTO.getName(), taskDTO.getDataT2(), taskDTO.getTask_type(), school_id);
            }
            else {
                sql = "INSERT INTO task (name, task_val3, task_type,school_id) VALUES (?, ?::jsonb, ?, ?) RETURNING id";
                id = jdbc.queryForObject(sql, Integer.class, taskDTO.getName(), taskDTO.getDataT3(), taskDTO.getTask_type(), school_id);

            }
        }
        int i = 0;
        while (i < taskDTO.getUnit_id().size()) {
            sql = "INSERT INTO task_unit (task_id, unit_id) VALUES (?, ?);";
            jdbc.update(sql, id, taskDTO.getUnit_id().get(i));
            i++;
        }
        taskDTO.setId(id);
    }

    @Override
    public TaskDTO getTask(String admin , Integer taskId) {
        TaskDTO taskDTO = jdbc.queryForObject("SELECT * FROM task WHERE id = ?",
                taskMapper, taskId);
        List<Integer> unitIds = jdbc.queryForList("SELECT unit_id FROM task_unit WHERE task_id = ?",
                Integer.class, taskId);
        if (taskDTO != null) {
            taskDTO.setUnit_id(unitIds);
        }
        return taskDTO;
    }

    @Override
    public List<TaskDTO> getTasksByUnit(String admin , Integer unitId) {
        Integer school_id = schoolIDDAO.getSchoolId(admin);
        final String sql = "SELECT * FROM task JOIN task_unit ON task_unit.unit_id = ? AND task.id = task_unit.task_id AND school_id=?;";
        return jdbc.query(sql, taskMapper, unitId, school_id);
    }

    @Override
    public List<TaskDTO> getAllTasksBySchoolId(int schoolId) {
        final String sql = "SELECT * FROM task WHERE school_id= ? ;";
        return jdbc.query(sql, taskMapper, schoolId);
    }


//    TODO: Надо поправить гет таск(для
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
        Integer school_id = schoolIDDAO.getSchoolId(user);
        String sql  = "SELECT * FROM task WHERE task.id = ? AND task.school_id = ?";
        if (jdbc.query(sql, taskMapper, task.getTaskID(), school_id)!= null) {
            sql = "INSERT INTO task_unit (unit_id, task_id) VALUES (?, ?);";
            jdbc.update(sql, task.getUnitID(), task.getTaskID());
        }
    }
}
