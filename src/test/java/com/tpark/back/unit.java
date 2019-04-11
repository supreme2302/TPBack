package com.tpark.back;

import com.tpark.back.model.dto.GroupDTO;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class unit {

    @Test
    public void test() {
        String message = String.format(
                "Welcome to lingvomake! Link to download the application" +
                        "\nhttp://lingvomake.ml/%s.apk",
                5

        );
        System.out.println(message);
    }
}
