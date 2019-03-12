package com.tpark.back.dao;

import com.tpark.back.model.Group;

import java.util.List;

public interface GroupDAO {
    void deleteGroup(int id);

    void changeGroup(Group group);

    void createGroup(Group group);

    Group getGroup(int groupID);

    List<Group> getGroupsByCourse(int courseID);
}
