package com.bookmysport.userslotbooking.Ananda.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookmysport.userslotbooking.Ananda.Services.BookSlotService;
import com.bookmysport.userslotbooking.Ananda.Services.SlotReschedule;
import com.bookmysport.userslotbooking.Models.AmountMessage;
import com.bookmysport.userslotbooking.Models.BookSlotSPModel;
import com.bookmysport.userslotbooking.Models.ResponseMessage;
import com.bookmysport.userslotbooking.Repository.BookSlotRepo;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = "http://localhost:5173")
public class SlotBookController {

    @Autowired
    private BookSlotService bookSlotService;

    @Autowired
    private SlotReschedule slotReschedule;

    @Autowired
    private BookSlotRepo bookSlotRepo;

    @PostMapping("bookslot")
    public ResponseEntity<ResponseMessage> bookSlotByUser(@RequestBody BookSlotSPModel bookSlotSPModel,
            @RequestHeader String token, @RequestHeader String role) {
        return bookSlotService.userBookSLotService(bookSlotSPModel, token, role);
    }

    @GetMapping("getslotsofuser")
    public ResponseEntity<Object> getUserSlots(@RequestHeader String token, @RequestHeader String role) {
        return bookSlotService.getSlotForAnUserService(token, role);
    }

    @PutMapping("rescheduleslot")
    public ResponseEntity<AmountMessage> reScheduleSlot(@RequestBody BookSlotSPModel slotDetails) {
        return slotReschedule.reScheduleSlotService(slotDetails);
    }

    @PostMapping("checkslot")
    public ResponseEntity<ResponseMessage> checkSlot(@RequestBody BookSlotSPModel slotToBeChecked) {
        return bookSlotService.checkSlot(slotToBeChecked.getSpId(), slotToBeChecked.getDateOfBooking(),
                slotToBeChecked.getStartTime(), slotToBeChecked.getStopTime(), slotToBeChecked.getSportId(),
                slotToBeChecked.getCourtNumber());
    }

    @PostMapping("getbookedslots")
    public BookSlotSPModel getBookedSlots(@RequestBody BookSlotSPModel slotInfo) {
        return bookSlotRepo.findBookedSlots(slotInfo.getSpId(), slotInfo.getSportId(), slotInfo.getDateOfBooking(),
                slotInfo.getStartTime(), slotInfo.getStopTime());
    }

}
