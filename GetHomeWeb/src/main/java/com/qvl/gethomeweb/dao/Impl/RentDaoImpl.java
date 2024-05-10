package com.qvl.gethomeweb.dao.Impl;

import com.qvl.gethomeweb.dao.RentDao;
import com.qvl.gethomeweb.dto.RentRequest;
import com.qvl.gethomeweb.model.Rent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class RentDaoImpl implements RentDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer createRent(Integer userId,Integer houseId,RentRequest rentRequest) {
        String sql="INSERT INTO rent(user_id,house_id,month,total_amount) SELECT :userId, :houseId,:month," +
                "price_per_month * :month FROM house WHERE house_id = :houseId";
        Map<String,Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("houseId",houseId);
        map.put("month",rentRequest.getMonth());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
        int rentId = keyHolder.getKey().intValue();
        return rentId;
    }

    @Override
    public Rent getRentById(Integer rentId) {
        return null;
    }

    @Override
    public void updateRent(Integer rentId, Integer userId, RentRequest rentRequest) {

    }

    @Override
    public void deleteRentById(Integer rentId) {

    }
}
