package com.tpark.back.dao;

import com.tpark.back.model.Admin;

public interface AdminDAO {
    Admin getAdminByEmail(String email);

    void addAdmin(Admin user);
}
