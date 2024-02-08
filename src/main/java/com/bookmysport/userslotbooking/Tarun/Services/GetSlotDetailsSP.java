package com.bookmysport.userslotbooking.Tarun.Services;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bookmysport.userslotbooking.MiddleWares.GetSPDetailsMW;
import com.bookmysport.userslotbooking.Models.BookSlotSPModel;
import com.bookmysport.userslotbooking.Models.ResponseMessage;
import com.bookmysport.userslotbooking.Repository.BookSlotRepo;

@Service
public class GetSlotDetailsSP {
    @Autowired
    private BookSlotRepo bookSlotRepo;

    @Autowired
    private ResponseMessage responseMessage;

    @Autowired
    private GetSPDetailsMW getSPDetailsMW;

    // @Author - Tarun
    // This is the method to fetch all the users information  present in the database

    public ResponseEntity<Object> getAllSlotIdsService(String token, String role) {
        try {
            UUID spId = UUID.fromString(getSPDetailsMW.getSPDetailsByToken(token, role).getBody().get("id").toString());
            List<BookSlotSPModel> serviceProviderSlots = bookSlotRepo.findByspId(spId);
            return ResponseEntity.ok().body(serviceProviderSlots);

        } catch (Exception e) {
            responseMessage.setSuccess(false);
            responseMessage.setMessage("Internal Server Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }
    }
}
