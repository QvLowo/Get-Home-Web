package com.qvl.gethomeweb.dao;

import com.qvl.gethomeweb.dto.CreateRentRequest;
import com.qvl.gethomeweb.dto.RentItem;
import com.qvl.gethomeweb.model.Rent;
import com.qvl.gethomeweb.model.RentInfo;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface RentDao {

    Integer createRent(Integer userId, Integer totalAmount, CreateRentRequest createRentRequest);
    void createRentInfo(Integer rentId, List<RentInfo> rentInfoList);

    Rent getRentById(Integer rentId);

}
