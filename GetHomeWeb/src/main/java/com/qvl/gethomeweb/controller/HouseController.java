package com.qvl.gethomeweb.controller;

import com.qvl.gethomeweb.dto.HouseRequest;
import com.qvl.gethomeweb.model.House;
import com.qvl.gethomeweb.service.HouseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class HouseController {
    //注入HouseService
    @Autowired
    private HouseService houseService;

    //取得一筆房屋資訊
    @GetMapping("/houses/{houseId}")
    public ResponseEntity<House> getHouse(@PathVariable Integer houseId) {
        //call service透過houseId取得房屋資訊
        House house = houseService.getHouseById(houseId);
        //如果房屋存在，回傳200，不存在回傳404
        if (house != null) {
            return ResponseEntity.status(HttpStatus.OK).body(house);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //取得所有房屋資訊
    @GetMapping("/houses")
    public ResponseEntity<List<House>> getAllHouses() {
        List<House> houses = houseService.getAllHouses();
        if (houses != null) {
            return ResponseEntity.status(HttpStatus.OK).body(houses);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //新增房屋
    @PostMapping("/houses")
    public ResponseEntity<House> createHouse(@RequestBody @Valid HouseRequest houseRequest) {
        Integer houseId = houseService.createHouse(houseRequest);
        House house = houseService.getHouseById(houseId);
        return ResponseEntity.status(HttpStatus.CREATED).body(house);

    }

    //透過houseId更新房屋資訊
    @PutMapping("/houses/{houseId}")
    public ResponseEntity<House> updateHouse(@PathVariable Integer houseId, @RequestBody @Valid HouseRequest houseRequest) {

        House house = houseService.getHouseById(houseId);
        //如果房屋不存在，回傳404
        if (house == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        //更新房屋資訊，更新成功回傳200
        houseService.updateHouse(houseId, houseRequest);
        House updatedHouse = houseService.getHouseById(houseId);
        return ResponseEntity.status(HttpStatus.OK).body(updatedHouse);
    }

    //透過houseId刪除房屋，刪除成功或房屋不存在都回傳204
    @DeleteMapping("/houses/{houseId}")
    public ResponseEntity<House> deleteHouse(@PathVariable Integer houseId) {
        houseService.deleteHouseById(houseId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}//class end