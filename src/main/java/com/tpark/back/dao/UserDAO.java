package com.tpark.back.dao;

import com.tpark.back.model.User;

public interface UserDAO {
    User getUserByEmail(String email);

    void addUser(User user);
}
