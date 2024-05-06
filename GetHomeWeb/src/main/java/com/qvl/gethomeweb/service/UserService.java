package com.qvl.gethomeweb.service;

import com.qvl.gethomeweb.dto.UserRegisterRequest;
import com.qvl.gethomeweb.model.User;

public interface UserService {
    Integer register(UserRegisterRequest userRegisterRequest);

    User getUserById(Integer userId);
}
