package com.bookmysport.userslotbooking.Repository;

import java.sql.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.bookmysport.userslotbooking.Models.BookSlotSPModel;
import jakarta.transaction.Transactional;

@Repository
public interface BookSlotRepo extends JpaRepository<BookSlotSPModel, UUID> {

    @Transactional
    @Query(value = "SELECT * FROM book_slot_details WHERE sp_id = :spId AND sport_id= :sportId AND date_of_booking = :dateOfBooking AND court_number LIKE CONCAT('%', :courtNumber, '%') AND (start_time = :startTime OR stop_time = :stopTime) ", nativeQuery = true)
    BookSlotSPModel findSlotExists(@Param("spId") UUID spId, @Param("sportId") UUID sportId,
            @Param("dateOfBooking") Date dateOfBooking, @Param("startTime") int startTime,
            @Param("stopTime") int stopTime, @Param("courtNumber") String courtNumber);

    List<BookSlotSPModel> findByUserId(UUID userId);

    List<BookSlotSPModel> findByspId(UUID spId);

    BookSlotSPModel findBySlotId(UUID slotId);

}
