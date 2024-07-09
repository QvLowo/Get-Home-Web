package com.qvl.gethomeweb.rowmapper;

import com.qvl.gethomeweb.constant.Gender;
import com.qvl.gethomeweb.model.Role;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RoleRowMapper implements RowMapper<Role> {
    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
        Role role = new Role();
        role.setRoleId(rs.getInt("role_id"));
        role.setRoleName(Gender.UserRole.valueOf(rs.getString("role_name")));
        return role;
    }
}
