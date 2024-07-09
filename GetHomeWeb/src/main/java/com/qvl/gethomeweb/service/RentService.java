package com.qvl.gethomeweb.service;

import com.qvl.gethomeweb.dto.RentItem;
import com.qvl.gethomeweb.dto.CreateRentRequest;
import com.qvl.gethomeweb.dto.RentQueryParams;
import com.qvl.gethomeweb.model.Rent;
import com.qvl.gethomeweb.model.RentInfo;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RentService {
    Integer createRent(Integer userId, CreateRentRequest createRentRequest);

    Integer payRent(Integer rentId, RentItem rentItem);

    Rent getRentById(Integer rentId);


    RentInfo getRentInfoById(Integer rentInfoId);

    List<Rent> getRentOrders(RentQueryParams rentQueryParams);
    List<RentInfo> getRentInfo(RentQueryParams rentQueryParams);

    Integer countRentOrder(RentQueryParams rentQueryParams);

    void cancelRentById(Integer rentId);

    void cancelCheckById(Integer rentId,Integer houseId);

    Integer countRentInfo(RentQueryParams rentQueryParams);
}
