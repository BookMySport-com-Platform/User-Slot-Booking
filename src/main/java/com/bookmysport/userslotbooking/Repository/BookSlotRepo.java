package com.bookmysport.userslotbooking.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookmysport.userslotbooking.Models.BookSlotSPModel;

public interface BookSlotRepo extends JpaRepository<BookSlotSPModel, UUID> {

}
