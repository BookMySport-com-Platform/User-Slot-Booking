package com.bookmysport.userslotbooking.Ananda.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookmysport.userslotbooking.Ananda.Services.BookSlotService;
import com.bookmysport.userslotbooking.MiddleWares.GetSportBySportIDAndSpid;
import com.bookmysport.userslotbooking.Models.BookSlotSPModel;
import com.bookmysport.userslotbooking.Models.IntResponseModel;
import com.bookmysport.userslotbooking.Models.ResponseMessage;

@RestController
@RequestMapping("api")
public class SlotBookController {

    @Autowired
    private BookSlotService bookSlotService;

    @Autowired
    private GetSportBySportIDAndSpid getSportBySportIDAndSpid;

    @Autowired
    private IntResponseModel intResponseModel;

    @PostMapping("bookslot")
    public ResponseEntity<ResponseMessage> bookSlotByUser(@RequestBody BookSlotSPModel bookSlotSPModel,
            @RequestHeader String token, @RequestHeader String role) {
        return bookSlotService.userBookSLotService(bookSlotSPModel, token, role);
    }

    @GetMapping("getslots")
    public ResponseEntity<Object> getUserSlots(@RequestHeader String token, @RequestHeader String role) {
        return bookSlotService.getSlotForAnUserService(token, role);
    }

    //Run this before booking the slot to set the price of the sport in the IntResponseModel and this can be accessed by the bookslot end-point to get the price of the sport and calculate the total price
    @GetMapping("getsportprice")
    public int getSportPrice(@RequestHeader String token, @RequestHeader String role, @RequestHeader String sportId) {
        int price = getSportBySportIDAndSpid.getSportAndSpDetailsService(token, role, sportId);
        intResponseModel.setNumber(price);
        return price;
    }

}
