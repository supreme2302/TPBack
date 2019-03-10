package com.tpark.back.service;

import com.tpark.back.model.User;

public interface UserService {

    User getUserByEmail(String email);

    void addUser(User user);
}
