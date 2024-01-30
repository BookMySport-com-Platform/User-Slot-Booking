package com.bookmysport.userslotbooking.Nikunj.Controllers;

import java.util.List;
import java.util.UUID;

import org.aspectj.weaver.patterns.ConcreteCflowPointcut.Slot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.bookmysport.userslotbooking.Models.ResponseMessage;
import com.bookmysport.userslotbooking.Nikunj.Services.DeleteSlotService;

@RestController
@RequestMapping("api")
public class NikunjController {
    
    

    // @Author - Nikunj Khandelwal
    // this is the feature to delete slot by the user who has create the slot 
    
    @Autowired
    private DeleteSlotService deleteSlotService;

    @DeleteMapping("delete")
    public ResponseEntity<ResponseMessage> deleteSlot(@RequestHeader UUID slotId, @RequestHeader String token, @RequestHeader String role) {
        return deleteSlotService.deleteSlotService(slotId, token, role);
    }
     
   
}

