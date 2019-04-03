package com.tpark.back.service.Impl;

import com.tpark.back.dao.GroupDAO;
import com.tpark.back.dao.Impl.GroupDAOImpl;
import com.tpark.back.model.dto.GroupDTO;
import com.tpark.back.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GroupServiceImpl implements GroupService {
    private final GroupDAO groupDAO;

    @Autowired
    GroupServiceImpl(GroupDAO groupDAO){
        this.groupDAO = groupDAO;
    }

    @Override
    public List<GroupDTO> getGroupsByCourse(int courseID, String email){
        return groupDAO.getGroupsByCourse(courseID, email);
    };

    @Override
    public GroupDTO getGroup(int groupID, String email){
        return groupDAO.getGroup(groupID, email);
    }

    @Override
    public void createGroup(GroupDTO groupDTO, String email){
        groupDAO.createGroup(groupDTO, email);
    }

    @Override
    public void changeGroup(GroupDTO groupDTO, String email){
        groupDAO.changeGroup(groupDTO, email);
    }

    @Override
    public void deleteGroup(int id, String email){
        groupDAO.deleteGroup(id, email);
    }

    @Override
    public GroupDTO getGroupByCourseForStudent(Integer courseId, String student) {
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
