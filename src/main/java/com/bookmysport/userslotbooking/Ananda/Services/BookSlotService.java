package com.bookmysport.userslotbooking.Ananda.Services;

import java.sql.Date;
import java.util.UUID;
import java.util.List;

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

    public ResponseEntity<ResponseMessage> checkSlot(UUID spId, Date dateOfBooking, int startTime, int stopTime,
            UUID sportId) {
        try {
            BookSlotSPModel userBooking = bookSlotRepo.findSlotExists(spId, sportId, dateOfBooking, startTime,
                    stopTime);
            if (userBooking == null) {
                responseMessage.setSuccess(true);
                responseMessage.setMessage("Slot Empty");
                return ResponseEntity.ok().body(responseMessage);
            } else {
                responseMessage.setSuccess(false);
                responseMessage.setMessage("Slot full.");
                return ResponseEntity.badRequest().body(responseMessage);
            }
        } catch (Exception e) {
            responseMessage.setSuccess(false);
            responseMessage
                    .setMessage("Internal Server Error inside BookSlotServce.java Method:checkSlot " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }
    }

    public ResponseEntity<ResponseMessage> userBookSLotService(BookSlotSPModel bookSlotSPModelReq, String token,
            String role) {
        try {
            ResponseEntity<ResponseMessage> messageFromCheckSlot = checkSlot(bookSlotSPModelReq.getSpId(),
                    bookSlotSPModelReq.getDateOfBooking(), bookSlotSPModelReq.getStartTime(),
                    bookSlotSPModelReq.getStopTime(), bookSlotSPModelReq.getSportId());
            if (messageFromCheckSlot.getBody().getSuccess()) {
                BookSlotSPModel bookSlotSPModel = new BookSlotSPModel();
                bookSlotSPModel.setSpId(bookSlotSPModelReq.getSpId());
                bookSlotSPModel
                        .setUserId(UUID
                                .fromString(getSPDetailsMW.getSPDetailsByToken(token, role).getBody().getMessage()));

                bookSlotSPModel.setSportId(bookSlotSPModelReq.getSportId());

                bookSlotSPModel.setDateOfBooking(bookSlotSPModelReq.getDateOfBooking());

                bookSlotSPModel.setStartTime(bookSlotSPModelReq.getStartTime());

                bookSlotSPModel.setStopTime(bookSlotSPModelReq.getStopTime());

                bookSlotRepo.save(bookSlotSPModel);

                responseMessage.setSuccess(true);
                responseMessage.setMessage("Slot booked.");

                return ResponseEntity.ok().body(responseMessage);
            } else {
                return messageFromCheckSlot;
            }

        } catch (Exception e) {
            responseMessage.setSuccess(false);
            responseMessage.setMessage(
                    "Internal Server Error inside BookSlotServce.java Method: userBookSLotService) " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }
    }

    public ResponseEntity<Object> getSlotForAnUserService(String token,String role) {
        try {
            List<BookSlotSPModel> slots = bookSlotRepo.findByUserId(UUID.fromString(getSPDetailsMW.getSPDetailsByToken(token, role).getBody().getMessage()));
            if (slots.isEmpty()) {
                responseMessage.setSuccess(false);
                responseMessage.setMessage("No Slots exits with this userId");
                return ResponseEntity.badRequest().body(responseMessage);
            } else {
                return ResponseEntity.ok().body(slots);
            }
        } catch (Exception e) {
            responseMessage.setSuccess(false);
            responseMessage.setMessage(
                    "Internal Server Error inside BookSlotServce.java Method: getSlotForAnUserService) "
                            + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }
    }
}
