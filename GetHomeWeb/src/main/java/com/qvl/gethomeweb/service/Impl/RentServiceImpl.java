package com.qvl.gethomeweb.service.Impl;

import com.qvl.gethomeweb.dao.RentDao;
import com.qvl.gethomeweb.dto.RentRequest;
import com.qvl.gethomeweb.model.Rent;
import com.qvl.gethomeweb.service.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RentServiceImpl implements RentService {
    @Autowired
    RentDao rentDao;

    @Override
    public Integer createRent(Integer userId,Integer houseId, RentRequest rentRequest) {

        return rentDao.createRent(userId,houseId,rentRequest);
    }

    @Override
    public Rent getRentById(Integer rentId) {
        return rentDao.getRentById(rentId);
    }

    @Override
    public void updateRent(Integer rentId,Integer userId, RentRequest rentRequest) {
        rentDao.updateRent(rentId,userId, rentRequest);
    }

    @Override
    public void deleteRentById(Integer rentId) {
        rentDao.deleteRentById(rentId);
    }
}
