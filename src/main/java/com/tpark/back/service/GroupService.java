package com.tpark.back.service;

import com.tpark.back.model.Group;

import java.util.List;

public interface GroupService {

    List<Group> getGroupsByCourse(int courseID, String email);
    Group getGroup(int groupID, String email);
    void createGroup(Group group, String email);
    void changeGroup(Group group, String email);
    void deleteGroup( int id, String email);


    Group getGroupByCourseForStudent(Integer courseId, String student);

    Object getGroupForStudent(String student, Integer id);

    Object getGroupsForAdmin(String user);

    Object getGroupsForStudent(String student);
}
