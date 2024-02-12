package com.bookmysport.userslotbooking.Models;

import java.sql.Date;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "book_slot_details")
public class BookSlotSPModel {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID slotId;

    private UUID spId;

    private UUID userId;

    private UUID sportId;

    private Date dateOfBooking;

    private int startTime;

    private int stopTime;

    private String courtNumber;

    private int priceToBePaid;
}
