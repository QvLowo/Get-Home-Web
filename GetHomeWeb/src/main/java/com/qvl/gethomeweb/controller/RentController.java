package com.qvl.gethomeweb.controller;

import com.qvl.gethomeweb.dto.RentRequest;
import com.qvl.gethomeweb.model.Rent;
import com.qvl.gethomeweb.service.RentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
public class RentController {
    @Autowired
    private RentService rentService;

    @PostMapping("/users/{userId}/rent/{houseId}")
    public ResponseEntity<Rent> createRent(@PathVariable("userId")Integer userId, @RequestBody @Valid RentRequest rentRequest,@PathVariable("houseId")Integer houseId) {
        Integer rentId = rentService.createRent(userId,houseId ,rentRequest);

        Rent rent = rentService.getRentById(rentId);

        return ResponseEntity.status(HttpStatus.CREATED).body(rent);
    }



}
