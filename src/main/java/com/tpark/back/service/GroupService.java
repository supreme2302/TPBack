package com.tpark.back.service;

import com.tpark.back.model.Group;

import java.util.List;

public interface GroupService {

    List<Group> getGroupsByCourse(int courseID);
    Group getGroup(int groupID);
    void createGroup(Group group);
    void changeGroup(Group group);
    void deleteGroup( int id);


}