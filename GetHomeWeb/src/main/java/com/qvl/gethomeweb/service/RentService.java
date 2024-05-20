package com.qvl.gethomeweb.service;

import com.qvl.gethomeweb.dto.RentItem;
import com.qvl.gethomeweb.dto.CreateRentRequest;
import com.qvl.gethomeweb.model.Rent;
import org.springframework.stereotype.Service;

@Service
public interface RentService {
    Integer createRent(Integer userId,CreateRentRequest createRentRequest);
    Rent getRentById(Integer rentId);
}
