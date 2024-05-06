package com.qvl.gethomeweb.dao;

import com.qvl.gethomeweb.dto.UserRegisterRequest;
import com.qvl.gethomeweb.model.User;

public interface UserDao {
    Integer createUser(UserRegisterRequest userRegisterRequest);

    User getUserById(Integer userId);

    User getUserByPhone(String phone);
}
