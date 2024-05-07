package com.qvl.gethomeweb.service.Impl;

import com.qvl.gethomeweb.dao.UserDao;
import com.qvl.gethomeweb.dto.UserLoginRequest;
import com.qvl.gethomeweb.dto.UserRegisterRequest;
import com.qvl.gethomeweb.model.User;
import com.qvl.gethomeweb.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService {
    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserDao userDao;

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
//        檢查手機號碼是否已被註冊
        User user = userDao.getUserByPhone(userRegisterRequest.getPhone());

        if (user != null) {
//            提示log warn資訊顯示手機號碼被誰重複註冊
            log.warn("該手機號碼 {} 已被{}註冊", userRegisterRequest.getPhone(), userRegisterRequest.getUsername());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "手機號碼已被註冊");
        }
//        註冊帳號
        return userDao.createUser(userRegisterRequest);
    }

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public User login(UserLoginRequest userLoginRequest) {
        User user = userDao.getUserByPhone(userLoginRequest.getPhone());
        if (user == null) {
            log.warn("該手機號碼 {} 尚未註冊", userLoginRequest.getPhone());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "該手機號碼尚未註冊");
        }
        if (user.getPassword().equals(userLoginRequest.getPassword())) {
            return user;
        } else {
            log.warn("該手機號碼{}的密碼錯誤", userLoginRequest.getPhone());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "密碼錯誤!");
        }
    }
}
