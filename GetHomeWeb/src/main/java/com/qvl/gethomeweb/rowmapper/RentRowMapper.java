package com.qvl.gethomeweb.rowmapper;

import com.qvl.gethomeweb.model.Rent;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RentRowMapper implements RowMapper<Rent> {
    @Override
    public Rent mapRow(ResultSet rs, int rowNum) throws SQLException {
        Rent rent = new Rent();
        rent.setRentId(rs.getInt("rent_id"));
        rent.setUserId(rs.getInt("user_id"));
        rent.setTotalAmount(rs.getInt("total_amount"));
        rent.setStatus(rs.getString("status"));
        rent.setCreateDate(rs.getDate("create_date"));
        rent.setUpdateDate(rs.getDate("update_date"));
        return rent;

    }
}