package com.tpark.back.dao;

import com.tpark.back.model.Group;

import java.util.List;

public interface GroupDAO {
    void deleteGroup(int id, String email);

    void changeGroup(Group group, String email);

    void createGroup(Group group, String email);

    Group getGroup(int groupID, String email);

    List<Group> getGroupsByCourse(int courseID , String email);

    List<Group> getGroupsForAdmin(String user);

    List<Group> getGroupsForStudent(String student);
}
