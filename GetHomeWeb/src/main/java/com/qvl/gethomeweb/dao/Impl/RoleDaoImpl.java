package com.qvl.gethomeweb.dao.Impl;

import com.qvl.gethomeweb.dao.RoleDao;
import com.qvl.gethomeweb.model.Role;
import com.qvl.gethomeweb.rowmapper.UserRowMapper;
import com.qvl.gethomeweb.rowmapper.RoleRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RoleDaoImpl implements RoleDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private RoleRowMapper roleRowMapper;

    //    取得role table的角色名稱
    @Override
    public Role getRoleByName(String roleName) {
        String sql = "SELECT role_id , role_name FROM role WHERE role_name = :roleName";
        Map<String, Object> map = new HashMap<>();
        map.put("roleName", roleName);
        List<Role> roleList = namedParameterJdbcTemplate.query(sql, map, roleRowMapper);
        if (roleList.size() > 0) {
            return roleList.get(0);
        } else {
            return null;
        }
    }
}
