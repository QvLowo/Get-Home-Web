package com.qvl.gethomeweb.dao.Impl;

import com.qvl.gethomeweb.dao.UserDao;
import com.qvl.gethomeweb.dto.UserRegisterRequest;
import com.qvl.gethomeweb.model.User;
import com.qvl.gethomeweb.rowmapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer createUser(UserRegisterRequest userRegisterRequest) {
        String sql = "INSERT INTO user (phone,username, password,email) VALUES (:phone,:username, :password,:email )";
        Map<String, Object> map = new HashMap<>();
        map.put("username", userRegisterRequest.getUsername());
        map.put("phone", userRegisterRequest.getPhone());
        map.put("password", userRegisterRequest.getPassword());
        map.put("email", userRegisterRequest.getEmail());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
        int userId = keyHolder.getKey().intValue();
        return userId;
    }

    @Override
    public User getUserById(Integer userId) {
        String sql = "SELECT user_id,phone,username,password,email,created_date,last_update_date FROM user WHERE user_id = :user_id";
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", userId);

        List<User> userList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());

        if (userList.size() > 0) {
            return userList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public User getUserByPhone(String phone) {
        String sql = "SELECT user_id,phone,username,password,email,created_date,last_update_date FROM user WHERE phone = :phone";
        Map<String, Object> map = new HashMap<>();
        map.put("phone", phone);

        List<User> userList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());

        if (userList.size() > 0) {
            return userList.get(0);
        } else {
            return null;
        }
    }
}
