package com.bookmysport.userslotbooking.Nikunj.Services;

import java.sql.Date;
import java.util.Optional;
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
public class DeleteSlotService {

    @Autowired
    private BookSlotRepo bookSlotRepo;

    @Autowired
    private GetSPDetailsMW getSPDetailsMW;

    @Autowired
    private ResponseMessage responseMessage;

    // This method is used to check the slot if it is present or not

    public ResponseEntity<ResponseMessage> checkSlot(UUID spId, Date dateOfBooking, int startTime, int stopTime,UUID sportId) {
        try {
            BookSlotSPModel userBooking = bookSlotRepo.findSlotExists(spId, sportId,dateOfBooking, startTime, stopTime);
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
            responseMessage.setMessage("Internal Server Error inside BookSlotServce.java Method:checkSlot " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }
    }

  // @Author - Nikunj Khandelwal
  // Method to delete the Slot by authorized user who has created the slot
  // Optional is used here to get null or not null values 
    public ResponseEntity<ResponseMessage> deleteSlotService(UUID slotId, String token, String role) {
        try {
            Optional<BookSlotSPModel> optionalSlotToDelete = bookSlotRepo.findById(slotId);

            if (optionalSlotToDelete.isPresent()) {
                // this line is getting the slot if present from the database 
                BookSlotSPModel slotToDelete = optionalSlotToDelete.get();

                // Ensure that the user making the request has the necessary permissions
                UUID userIdFromToken = UUID.fromString(getSPDetailsMW.getSPDetailsByToken(token, role).getBody().getMessage());
                if (userIdFromToken.equals(slotToDelete.getUserId())) {
                    // Delete the slot
                    bookSlotRepo.delete(slotToDelete);
                    responseMessage.setSuccess(true);
                    responseMessage.setMessage("Slot deleted.");
                    return ResponseEntity.ok().body(responseMessage);
                } else {
                    responseMessage.setSuccess(false);
                    responseMessage.setMessage("Unauthorized to delete this slot.");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMessage);
                }
            } else {
                responseMessage.setSuccess(false);
                responseMessage.setMessage("Slot not found.");
                return ResponseEntity.badRequest().body(responseMessage);
            }
        } catch (Exception e) {
            responseMessage.setSuccess(false);
            responseMessage.setMessage("Internal Server Error inside BookSlotService.java Method: deleteSlotService " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }
    }

}
