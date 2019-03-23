package com.tpark.back.service.Impl;

import com.tpark.back.dao.Impl.CourseDAOImpl;
import com.tpark.back.dao.Impl.GroupDAOImpl;
import com.tpark.back.model.Group;
import com.tpark.back.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GroupServiceImpl implements GroupService {
    private final GroupDAOImpl groupDAO;

    @Autowired
    GroupServiceImpl(GroupDAOImpl groupDAO){
        this.groupDAO = groupDAO;
    }

    @Override
    public List<Group> getGroupsByCourse(int courseID, String email){
        return groupDAO.getGroupsByCourse(courseID, email);
    };

    @Override
    public Group getGroup(int groupID, String email){
        return groupDAO.getGroup(groupID, email);
    }

    @Override
    public void createGroup(Group group, String email){
        groupDAO.createGroup(group, email);
    }

    @Override
    public void changeGroup(Group group, String email){
        groupDAO.changeGroup(group, email);
    }

    @Override
    public void deleteGroup(int id, String email){
        groupDAO.deleteGroup(id, email);
    }

    @Override
    public Group getGroupByCourseForStudent(Integer courseId, String student) {
        return groupDAO.getGroupByStudent(courseId, student);
    }

    @Override
    public Object getGroupForStudent(String student, Integer id) {
        return groupDAO.getGroupForStudent(student,id );
    }

    @Override
    public Object getGroupsForAdmin(String user) {
        return groupDAO.getGroupsForAdmin(user);
    }

    @Override
    public Object getGroupsForStudent(String student) {
        return groupDAO.getGroupsForStudent(student);
    }
}
