package com.qvl.gethomeweb.dao;

import com.qvl.gethomeweb.dto.RentRequest;
import com.qvl.gethomeweb.model.Rent;
import org.springframework.stereotype.Repository;

@Repository
public interface RentDao {

    Integer createRent(Integer userId,Integer houseId, RentRequest rentRequest);

    Rent getRentById(Integer rentId);

    void updateRent(Integer rentId,Integer userId, RentRequest rentRequest);

    void deleteRentById(Integer rentId);

}
