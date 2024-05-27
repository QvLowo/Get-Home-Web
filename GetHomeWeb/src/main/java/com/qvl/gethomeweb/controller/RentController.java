package com.qvl.gethomeweb.controller;

import com.qvl.gethomeweb.dto.CreateRentRequest;
import com.qvl.gethomeweb.model.Rent;
import com.qvl.gethomeweb.service.RentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@Tag(name = "租屋相關")
public class RentController {
    @Autowired
    private RentService rentService;

    @Operation(summary = "租客新增租屋訂單")
//    新增租屋訂單，新增訂單方法限制房客權限
    @PreAuthorize("hasRole('TENANT')")
    @PostMapping("/users/{userId}/rent")
    public ResponseEntity<Rent> createRent(@PathVariable Integer userId, @RequestBody @Valid CreateRentRequest createRentRequest) {
        Integer rentId =rentService.createRent(userId, createRentRequest);
        Rent rent = rentService.getRentById(rentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(rent);
    }

}
