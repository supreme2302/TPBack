package com.tpark.back;

import com.tpark.back.model.dto.GroupDTO;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class unit {

    @Test
    public void test() {
        List<GroupDTO> list = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            GroupDTO groupDTO = new GroupDTO();
            groupDTO.setId(i);
            groupDTO.setName("a + " + String.valueOf(i));
            list.add(groupDTO);
        }
        List<Integer> newList = list.stream()
                .map(GroupDTO::getId).collect(Collectors.toList());
        for (Integer id: newList) {
            System.out.println(id);
        }
    }
}
