package com.bookmysport.userslotbooking.Tarun.Controllers;
import org.springframework.web.bind.annotation.RestController;
import com.bookmysport.userslotbooking.Tarun.Services.GetSlotDetailsSP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping("api")
public class TarunController {
    
    @Autowired
    private GetSlotDetailsSP Getslotdetails;

    // @Author - Tarun get all endpoint for fetching all the slot id present in the database 
    
    @GetMapping("getall")
    public ResponseEntity<Object> getAllSlotIds(@RequestHeader String token,@RequestHeader String role) {
        return Getslotdetails.getAllSlotIdsService(token,role);
    }
}
    



