package com.qvl.gethomeweb.service.Impl;

import com.qvl.gethomeweb.dao.HouseDao;
import com.qvl.gethomeweb.dao.RentDao;
import com.qvl.gethomeweb.dto.RentItem;
import com.qvl.gethomeweb.dto.CreateRentRequest;
import com.qvl.gethomeweb.model.House;
import com.qvl.gethomeweb.model.Rent;
import com.qvl.gethomeweb.model.RentInfo;
import com.qvl.gethomeweb.service.RentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RentServiceImpl implements RentService {
    private final static Logger log = LoggerFactory.getLogger(RentServiceImpl.class);
    @Autowired
    RentDao rentDao;

    @Autowired
    HouseDao houseDao;

    //    @Override
//    public Integer createRent(Integer userId, CreateRentRequest createRentRequest) {
////        java8後出的功能，計算租期總月數
//        Period period = Period.between(createRentRequest.getStartDate(), createRentRequest.getEndDate());
//        int years = period.getYears();
////      不足月的部分算一個月
//        int months = period.getMonths()+1;
////        計算租期總月數
//        int totalMonths = years * 12 + months;
////        用houseID取得房屋資料
//        House house = houseDao.getHouseById(createRentRequest.getHouseId());
////        計算租期總金額
//        int totalAmount = house.getPricePerMonth() * totalMonths;
//
//        Integer rentId = rentDao.createRent(userId,totalAmount, createRentRequest);
//
//        return rentId;
//    }
    @Transactional
    @Override
//    建立租屋訂單
    public Integer createRent(Integer userId, CreateRentRequest createRentRequest) {
//        java8後出的功能，計算租期總月數
        Period period = Period.between(createRentRequest.getStartDate(), createRentRequest.getEndDate());
//        取得年數
        int years = period.getYears();
//      取得月數,不足月的部分算一個月
        int months = period.getMonths() + 1;
//        租期總月數
        int totalMonths = years * 12 + months;

        int totalAmount = 0;

        List<RentInfo> rentInfoList = new ArrayList<>();
        for (RentItem rentItem : createRentRequest.getRentItemList()) {
//        用houseID取得房屋資料
            House house = houseDao.getHouseById(rentItem.getHouseId());
//        計算租金
            int amount = rentItem.getMonth() * house.getPricePerMonth();

//        計算租期總金額
            totalAmount = house.getPricePerMonth() * totalMonths;
            if (amount > totalAmount) {
                log.warn("該金額 {} 超過總租金", amount);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "輸入金額超過總租金");
            }
            RentInfo rentInfo = new RentInfo();
            rentInfo.setPaymentId(UUID.randomUUID().toString());
            rentInfo.setHouseId(rentItem.getHouseId());
            rentInfo.setMonth(rentItem.getMonth());
            rentInfo.setAmount(amount);
            rentInfoList.add(rentInfo);
        }

        Integer rentId = rentDao.createRent(userId, totalAmount, createRentRequest);
        rentDao.createRentInfo(rentId, rentInfoList);

        return rentId;
    }

    @Override
    public Rent getRentById(Integer rentId) {
        return rentDao.getRentById(rentId);
    }

    @Override
    public String generateRentUUID(String paymentId) {
    String randomUUID = UUID.randomUUID().toString();
        return randomUUID;
    }
}
