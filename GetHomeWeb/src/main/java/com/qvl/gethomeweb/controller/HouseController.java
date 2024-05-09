package com.qvl.gethomeweb.controller;

import com.qvl.gethomeweb.constant.Gender;
import com.qvl.gethomeweb.constant.HouseStatus;
import com.qvl.gethomeweb.constant.HouseType;
import com.qvl.gethomeweb.dto.HouseQueryParams;
import com.qvl.gethomeweb.dto.HouseRequest;
import com.qvl.gethomeweb.model.House;
import com.qvl.gethomeweb.service.HouseService;
import com.qvl.gethomeweb.util.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<Page<House>> getAllHouses(
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

//        取得房屋list
        List<House> houseList = houseService.getAllHouses(houseQueryParams);

//        計算房屋總筆數（可使用查詢條件）
        Integer total = houseService.countAllHouses(houseQueryParams);

        //設定json Object，改成回傳json Object給前端
        Page<House> page = new Page<>();
//        joson Object放入limit offset total results的值
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(total);
        page.setResults(houseList);

        //回傳json Object
        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    //    限定只有管理員或房東才可以執行新增房屋的方法
    @PreAuthorize("hasRole('ADMIN' or 'LANDLORD')")
    //新增房屋
    @PostMapping("/landlord/create")
    public ResponseEntity<House> createHouse(@RequestBody @Valid HouseRequest houseRequest) {
        Integer houseId = houseService.createHouse(houseRequest);
        House house = houseService.getHouseById(houseId);
        return ResponseEntity.status(HttpStatus.CREATED).body(house);

    }

    //    限定只有管理員或房東才可以執行更新房屋資訊的方法
    @PreAuthorize("hasRole('ADMIN' or 'LANDLORD')")
    //透過houseId更新房屋資訊
    @PutMapping("/landlord/update/{houseId}")
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

    //    限定只有管理員或房東才可以執行刪除房屋的方法
    @PreAuthorize("hasRole('ADMIN' or 'LANDLORD')")
    //透過houseId刪除房屋，刪除成功或房屋不存在都回傳204
    @DeleteMapping("/landlord/delete/{houseId}")
    public ResponseEntity<House> deleteHouse(@PathVariable Integer houseId) {
        houseService.deleteHouseById(houseId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}