package com.qvl.gethomeweb.controller;

import com.qvl.gethomeweb.constant.Gender;
import com.qvl.gethomeweb.constant.HouseStatus;
import com.qvl.gethomeweb.constant.HouseType;
import com.qvl.gethomeweb.dto.HouseQueryParams;
import com.qvl.gethomeweb.dto.HouseRequest;
import com.qvl.gethomeweb.model.House;
import com.qvl.gethomeweb.service.HouseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
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

    //    查詢全部房子資訊．並加上選填的查詢條件
    @GetMapping("/houses")
    public ResponseEntity<List<House>> getAllHouses(
//            查詢條件
            @RequestParam(required = false) HouseType houseType,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) HouseStatus status,
//            排序功能，預設根據創建時間降冪排序（新->舊）
            @RequestParam(defaultValue = "created_date") String orderBy,
            @RequestParam(defaultValue = "desc") String orderType,
//           分頁功能，預設每頁5筆
//           每頁顯示筆數設定在0~100之間，避免前端回傳負數
            @RequestParam(defaultValue = "5") @Max(100) @Min(0) Integer limit,
//           跳過筆數最小值預設為0，避免前端回傳負數
            @RequestParam(defaultValue = "0") @Min(0) Integer offset
    ) {


        HouseQueryParams houseQueryParams = new HouseQueryParams();
        houseQueryParams.setHouseType(houseType);
        houseQueryParams.setSearch(search);
        houseQueryParams.setGender(gender);
        houseQueryParams.setStatus(status);
        houseQueryParams.setOrderBy(orderBy);
        houseQueryParams.setOrderType(orderType);
        houseQueryParams.setLimit(limit);
        houseQueryParams.setOffset(offset);

        List<House> houseList = houseService.getAllHouses(houseQueryParams);
        //根據RESTful設計回傳houseList
        return ResponseEntity.status(HttpStatus.OK).body(houseList);
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