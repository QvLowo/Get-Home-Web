package com.qvl.gethomeweb.service;

import com.qvl.gethomeweb.dao.HouseDao;
import com.qvl.gethomeweb.dto.HouseQueryParams;
import com.qvl.gethomeweb.dto.HouseRequest;
import com.qvl.gethomeweb.model.House;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseServiceImpl implements HouseService {
    //注入HouseDao
    @Autowired
    private HouseDao houseDao;

    //    查詢全部房子資訊．並加上選填的查詢條件
    @Override
    public House getHouseById(Integer houseId) {
        return houseDao.getHouseById(houseId);
    }

    //查詢所有房屋資訊
    @Override
    public List<House> getAllHouses(HouseQueryParams houseQueryParams) {
        return houseDao.getAllHouses(houseQueryParams);
    }

    //新增房屋資訊
    @Override
    public Integer createHouse(HouseRequest houseRequest) {
        return houseDao.createHouse(houseRequest);
    }

    //更新房屋資訊
    @Override
    public void updateHouse(Integer houseId, HouseRequest houseRequest) {
        houseDao.updateHouse(houseId, houseRequest);
    }

    //刪除房屋資訊
    @Override
    public void deleteHouseById(Integer houseId) {
        houseDao.deleteHouseById(houseId);
    }
}//class end
