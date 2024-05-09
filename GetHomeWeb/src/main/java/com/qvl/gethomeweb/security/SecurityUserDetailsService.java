package com.qvl.gethomeweb.security;

import com.qvl.gethomeweb.dao.UserDao;
import com.qvl.gethomeweb.model.Role;
import com.qvl.gethomeweb.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SecurityUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
//    根據使用者帳號查詢使用者詳細數據
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        從資料庫中根據使用者電話號碼查詢使用者數據
        User user = userDao.getUserByPhone(username);
//        檢查使用者是否存在，存在的話取得登入用的電話及密碼
        if (user == null) {
            throw new UsernameNotFoundException("使用者{}不存在" + username);
        } else {
            String userPhone = user.getPhone();
            String userPassword = user.getPassword();

//            權限相關設定
            List<Role> roleList = userDao.getRolesByUserId(user.getUserId());
            List<GrantedAuthority> authorities = convertToAuthorities(roleList);
//            轉換成 Spring Security 指定的 User 格式
            return new org.springframework.security.core.userdetails.User(userPhone, userPassword, authorities);

        }
    }

    private List<GrantedAuthority> convertToAuthorities(List<Role> roleList) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (Role role : roleList) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName().toString()));
        }

        return authorities;
    }
}

