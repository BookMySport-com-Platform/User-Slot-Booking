package com.bookmysport.userslotbooking.Ananda.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bookmysport.userslotbooking.MiddleWares.GetSportBySportIDAndSpid;
import com.bookmysport.userslotbooking.Models.AmountMessage;
import com.bookmysport.userslotbooking.Models.BookSlotSPModel;
import com.bookmysport.userslotbooking.Repository.BookSlotRepo;

@Service
public class SlotReschedule {

    @Autowired
    private BookSlotService bookSlotService;

    @Autowired
    private BookSlotRepo bookSlotRepo;

    @Autowired
    private GetSportBySportIDAndSpid getSportBySportIDAndSpid;

    @Autowired
    private AmountMessage amountMessage;

    public ResponseEntity<AmountMessage> reScheduleSlotService(BookSlotSPModel slotDetails) {
        try {
            if (bookSlotService
                    .checkSlot(slotDetails.getSpId(), slotDetails.getDateOfBooking(), slotDetails.getStartTime(),
                            slotDetails.getStopTime(), slotDetails.getSportId(), slotDetails.getCourtNumber())
                    .getBody().getSuccess()) {

                BookSlotSPModel updateSlot = bookSlotRepo.findBySlotId(slotDetails.getSlotId());
                updateSlot.setDateOfBooking(slotDetails.getDateOfBooking());
                updateSlot.setStartTime(slotDetails.getStartTime());
                updateSlot.setStopTime(slotDetails.getStopTime());
                updateSlot.setCourtNumber(slotDetails.getCourtNumber());

                int pricePerSportPerHour = getSportBySportIDAndSpid
                        .getSportAndSpDetailsService(updateSlot.getSpId().toString(),
                                updateSlot.getSportId().toString())
                        .getBody().getNumber();

                int calculatedPriceForUpdating = pricePerSportPerHour
                        * (slotDetails.getStopTime() - slotDetails.getStartTime())
                        * slotDetails.getCourtNumber().split(",").length;

                if (calculatedPriceForUpdating > slotDetails.getPriceToBePaid()) {
                    amountMessage.setMessage("Amount to be paid");
                    amountMessage.setAmount(calculatedPriceForUpdating - slotDetails.getPriceToBePaid());
                    return ResponseEntity.ok().body(amountMessage);
                } else if (calculatedPriceForUpdating < slotDetails.getPriceToBePaid()) {
                    amountMessage.setMessage("Amount to be refunded by company");
                    amountMessage.setAmount(slotDetails.getPriceToBePaid() - calculatedPriceForUpdating);
                    return ResponseEntity.ok().body(amountMessage);
                } else if (calculatedPriceForUpdating == slotDetails.getPriceToBePaid()) {
                    amountMessage.setMessage("No amount to be charged");
                    amountMessage.setAmount(0);
                    return ResponseEntity.ok().body(amountMessage);
                } else {
                    amountMessage.setMessage("Invalid amount");
                    amountMessage.setAmount(0);
                    return ResponseEntity.ok().body(amountMessage);
                }

            } else {
                amountMessage.setMessage("Slot with id: " + slotDetails.getSlotId() + "does not exists");
                amountMessage.setAmount(0);
                return ResponseEntity.badRequest().body(amountMessage);
            }
        } catch (Exception e) {
            amountMessage.setMessage("Internal Server Error in SlotReSchedule.java. Reaso: " + e.getMessage());
            amountMessage.setAmount(0);
            return ResponseEntity.ok().body(amountMessage);
        }
    }
}
