package com.qvl.gethomeweb.service;

import com.qvl.gethomeweb.dto.HouseRequest;
import com.qvl.gethomeweb.model.House;

import java.util.List;

public interface HouseService {
    //透過ID查詢一筆房屋資訊
    House getHouseById(Integer houseId);

    //查詢所有房屋資訊
    List<House> getAllHouses();

    //新增房屋資訊
    Integer createHouse(HouseRequest houseRequest);

    //更新房屋資訊
    void updateHouse(Integer houseId, HouseRequest houseRequest);

    //刪除房屋資訊
    void deleteHouseById(Integer houseId);
}
