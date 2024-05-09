package com.qvl.gethomeweb.service;

import com.qvl.gethomeweb.dto.UserLoginRequest;
import com.qvl.gethomeweb.dto.UserRegisterRequest;
import com.qvl.gethomeweb.model.User;

public interface UserService {
    Integer landLordRegister(UserRegisterRequest userRegisterRequest);

    Integer tenantRegister(UserRegisterRequest userRegisterRequest);


    User getUserById(Integer userId);

    User login(UserLoginRequest userLoginRequest);
}
