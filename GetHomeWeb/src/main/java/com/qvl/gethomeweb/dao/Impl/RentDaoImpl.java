package com.qvl.gethomeweb.dao.Impl;

import com.qvl.gethomeweb.dao.RentDao;
import com.qvl.gethomeweb.dto.CreateRentRequest;
import com.qvl.gethomeweb.dto.RentItem;
import com.qvl.gethomeweb.model.Rent;
import com.qvl.gethomeweb.model.RentInfo;
import com.qvl.gethomeweb.rowmapper.RentRowMapper;
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
public class RentDaoImpl implements RentDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    //    創建租屋訂單
    @Override
    public Integer createRent(Integer userId, Integer totalAmount, CreateRentRequest createRentRequest) {
        String sql = "INSERT INTO rent(user_id,total_amount,start_date,end_date) VALUES(:userId,:totalAmount,:startDate,:endDate)";
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("totalAmount", totalAmount);
        map.put("startDate", createRentRequest.getStartDate());
        map.put("endDate", createRentRequest.getEndDate());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
        int rentId = keyHolder.getKey().intValue();
        return rentId;
    }

    @Override
    public void createRentInfo(Integer rentId, List<RentInfo> rentInfoList) {
        String sql = "INSERT INTO rent_info(rent_id,house_id,amount,month)VALUES(:rentId,:houseId,:amount,:month)";
        MapSqlParameterSource[] parameterSources = new MapSqlParameterSource[rentInfoList.size()];
        for(int i = 0;i<rentInfoList.size();i++){
            RentInfo rentInfo = rentInfoList.get(i);
            parameterSources[i]=new MapSqlParameterSource();
            parameterSources[i].addValue("rentId",rentId);
            parameterSources[i].addValue("houseId",rentInfo.getHouseId());
            parameterSources[i].addValue("amount",rentInfo.getAmount());
            parameterSources[i].addValue("month",rentInfo.getMonth());
        }
        namedParameterJdbcTemplate.batchUpdate(sql, parameterSources);

    }

    @Override
    public Rent getRentById(Integer rentId) {
        String sql = "SELECT rent_id,user_id,status,total_amount,start_date,end_date,created_date,last_update_date FROM rent WHERE rent_id = :rentId";
        Map<String, Object> map = new HashMap<>();
        map.put("rentId", rentId);

        List<Rent> rentList = namedParameterJdbcTemplate.query(sql, map, new RentRowMapper());
        if (rentList.size() > 0) {
            return rentList.get(0);
        } else {
            return null;
        }
    }

}
