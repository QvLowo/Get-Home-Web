package com.qvl.gethomeweb.service.Impl;

import com.qvl.gethomeweb.dao.RoleDao;
import com.qvl.gethomeweb.dao.UserDao;
import com.qvl.gethomeweb.dto.UserRegisterRequest;
import com.qvl.gethomeweb.model.Member;
import com.qvl.gethomeweb.model.MyUserDetails;
import com.qvl.gethomeweb.model.Role;
import com.qvl.gethomeweb.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Override
    public Integer register(Integer roleId, UserRegisterRequest userRegisterRequest) {
//        檢查手機號碼是否已被註冊
        Member member = userDao.getUserByPhone(userRegisterRequest.getPhone());

        if (member != null) {
//            提示log warn資訊顯示手機號碼被誰重複註冊
            log.warn("該手機號碼 {} 已被{}註冊", userRegisterRequest.getPhone(), userRegisterRequest.getUsername());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "手機號碼已被註冊");
        }
//        設定預設角色1為房東，2為租客
        Role role = roleDao.getRoleById(roleId);
        switch (roleId) {
            case 1:
                roleDao.getRoleByName("ROLE_LANDLORD");
                break;
            case 2:
                roleDao.getRoleByName("ROLE_ADMIN");
                break;
            default:
                log.warn("角色不存在");
        }
        Integer userId = userDao.createUser(userRegisterRequest);
//        加入userId與角色關聯
        userDao.addRoleForUserId(userId, role);
//        註冊帳號
        return userId;
    }

    @Override
    public Member getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }
}
