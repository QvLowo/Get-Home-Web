package com.qvl.gethomeweb.dao;

import com.qvl.gethomeweb.dto.HouseQueryParams;
import com.qvl.gethomeweb.dto.HouseRequest;
import com.qvl.gethomeweb.model.House;

import java.util.List;

public interface HouseDao {

    House getHouseById(Integer houseId);

    List<House> getAllHouses(HouseQueryParams houseQueryParams);

    Integer createHouse(HouseRequest houseRequest);

    void updateHouse(Integer houseId, HouseRequest houseRequest);

    void deleteHouseById(Integer houseId);

    Integer countAllHouses(HouseQueryParams houseQueryParams);

    Integer getPrice(Integer houseId);
}
