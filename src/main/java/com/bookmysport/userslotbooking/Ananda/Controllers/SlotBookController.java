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
// import com.bookmysport.userslotbooking.Ananda.Services.PdfEmailService;
import com.bookmysport.userslotbooking.Models.BookSlotSPModel;
import com.bookmysport.userslotbooking.Models.ResponseMessage;

@RestController
@RequestMapping("api")
public class SlotBookController {

    @Autowired
    private BookSlotService bookSlotService;

    // @Autowired
    // private PdfEmailService pdfEmailService;

    @PostMapping("bookslot")
    public ResponseEntity<ResponseMessage> bookSlotByUser(@RequestBody BookSlotSPModel bookSlotSPModel,
            @RequestHeader String token, @RequestHeader String role) {
        return bookSlotService.userBookSLotService(bookSlotSPModel, token, role);
    }

    @GetMapping("getslots")
    public ResponseEntity<Object> getUserSlots(@RequestHeader String token, @RequestHeader String role) {
        return bookSlotService.getSlotForAnUserService(token, role);
    }

    // @PostMapping("email")
    // public void sendEmail(@RequestHeader String email) throws Exception
    // {
    //     pdfEmailService.generatePdfAndSendEmail(email);
    // }

}
