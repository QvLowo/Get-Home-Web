package com.qvl.gethomeweb.dao;

import com.qvl.gethomeweb.dto.UserRegisterRequest;
import com.qvl.gethomeweb.model.Role;
import com.qvl.gethomeweb.model.User;

import java.util.List;

public interface UserDao {
    Integer createUser(UserRegisterRequest userRegisterRequest);

    User getUserById(Integer userId);

    User getUserByPhone(String phone);

    //    權限相關設定
    List<Role> getRolesByUserId(Integer userId);

    void addRoleForUserId(Integer userId, Role role);

    void deleteRoleFormUserId(Integer userId, Role role);
}


