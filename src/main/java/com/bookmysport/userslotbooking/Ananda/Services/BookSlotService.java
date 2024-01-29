package com.bookmysport.userslotbooking.Ananda.Services;

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
public class BookSlotService {

    @Autowired
    private BookSlotRepo bookSlotRepo;

    @Autowired
    private GetSPDetailsMW getSPDetailsMW;

    @Autowired
    private ResponseMessage responseMessage;

    public ResponseEntity<ResponseMessage> userBookSLotService(BookSlotSPModel bookSlotSPModelReq, String token,
            String role) {
        try {
            BookSlotSPModel bookSlotSPModel = new BookSlotSPModel();
            bookSlotSPModel.setSpId(bookSlotSPModelReq.getSpId());
            bookSlotSPModel
                    .setUserId(UUID.fromString(getSPDetailsMW.getSPDetailsByToken(token, role).getBody().getMessage()));

            bookSlotSPModel.setSportId(bookSlotSPModelReq.getSportId());

            bookSlotSPModel.setDateOfBooking(bookSlotSPModelReq.getDateOfBooking());

            bookSlotSPModel.setStartTime(bookSlotSPModelReq.getStartTime());

            bookSlotSPModel.setStopTime(bookSlotSPModelReq.getStopTime());

            bookSlotRepo.save(bookSlotSPModel);

            responseMessage.setSuccess(true);
            responseMessage.setMessage("Slot booked.");

            return ResponseEntity.ok().body(responseMessage);
        } catch (Exception e) {
            responseMessage.setSuccess(false);
            responseMessage.setMessage("Internal Server Error inside ImageUploadService.java " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }

    }
}
