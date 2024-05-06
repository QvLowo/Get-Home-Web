package com.qvl.gethomeweb.rowmapper;

import com.qvl.gethomeweb.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUserName(rs.getString("username"));
        user.setPhone(rs.getString("phone"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setCreatedDate(rs.getTimestamp("created_Date"));
        user.setLastUpdateDate(rs.getTimestamp("last_update_date"));
        return user;
    }
}
