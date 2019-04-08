package com.tpark.back.service;

import com.tpark.back.model.dto.GroupDTO;

import java.util.List;

public interface GroupService {

    List<GroupDTO> getGroupsByCourse(int courseID, String email);
    GroupDTO getGroup(int groupID, String email);
    void createGroup(GroupDTO groupDTO, String email);
    void changeGroup(GroupDTO groupDTO, String email);
    void deleteGroup( int id, String email);


    GroupDTO getGroupByCourseForStudent(Integer courseId, String student);

    Object getGroupForStudent(String student, Integer id);

    Object getGroupsForAdmin(String user);

    Object getGroupsForStudent(String student);
}
