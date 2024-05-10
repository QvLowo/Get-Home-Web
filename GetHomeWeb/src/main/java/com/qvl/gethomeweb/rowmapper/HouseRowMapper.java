package com.qvl.gethomeweb.rowmapper;


import com.qvl.gethomeweb.constant.Gender;
import com.qvl.gethomeweb.constant.HouseStatus;
import com.qvl.gethomeweb.constant.HouseType;
import com.qvl.gethomeweb.model.House;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HouseRowMapper implements RowMapper<House> {
    //取得資料庫的數據轉換成java object的RowMapper
    @Override
    public House mapRow(ResultSet rs, int rowNum) throws SQLException {
        House house = new House();
        house.setHouseId(rs.getInt("house_id"));
//        string轉enum
        house.setHouseType(HouseType.valueOf(rs.getString("house_type")));

        house.setAddress(rs.getString("address"));
        house.setImageUrl(rs.getString("image_url"));
        house.setPricePerMonth(rs.getInt("price_per_month"));
        house.setGender(Gender.valueOf(rs.getString("gender")));
        house.setStatus(HouseStatus.valueOf(rs.getString("status")));

        house.setDescription(rs.getString("description"));
        house.setCreatedDate(rs.getDate("created_date"));
        house.setLastUpdateDate(rs.getDate("last_update_date"));

        return house;
    }
}
