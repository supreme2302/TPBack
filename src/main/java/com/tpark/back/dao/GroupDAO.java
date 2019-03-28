package com.tpark.back.dao;

import com.tpark.back.model.dto.GroupDTO;

import java.util.List;

public interface GroupDAO {
    void deleteGroup(int id, String email);

    void changeGroup(GroupDTO groupDTO, String email);

    void createGroup(GroupDTO groupDTO, String email);

    GroupDTO getGroup(int groupID, String email);

    List<GroupDTO> getGroupsByCourse(int courseID , String email);

    List<GroupDTO> getGroupsForAdmin(String user);

    List<GroupDTO> getGroupsForStudent(String student);
}
