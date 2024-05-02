package com.qvl.gethomeweb.dao;

import com.qvl.gethomeweb.dto.HouseRequest;
import com.qvl.gethomeweb.model.House;

import java.util.List;

public interface HouseDao {
    //查詢所有房屋資訊
    House getHouseById(Integer houseId);

    List<House> getAllHouses();

    Integer createHouse(HouseRequest houseRequest);

    void updateHouse(Integer houseId, HouseRequest houseRequest);

    void deleteHouseById(Integer houseId);


}
