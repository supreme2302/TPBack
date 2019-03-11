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
    public List<Group> getGroupsByCourse(int courseID){
        return groupDAO.getGroupsByCourse(courseID);
    };

    @Override
    public Group getGroup(int groupID){
        return groupDAO.getGroup(groupID);
    }

    @Override
    public void createGroup(Group group){
        groupDAO.createGroup(group);
    }

    @Override
    public void changeGroup(Group group){
        groupDAO.changeGroup(group);
    }

    @Override
    public void deleteGroup(int id){
        groupDAO.deleteGroup(id);
    }
}
