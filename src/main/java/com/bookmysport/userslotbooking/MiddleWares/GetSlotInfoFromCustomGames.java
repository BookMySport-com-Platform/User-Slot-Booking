package com.bookmysport.userslotbooking.MiddleWares;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.bookmysport.userslotbooking.Models.BookSlotSPModel;
import com.bookmysport.userslotbooking.Models.ResponseMessage;

import reactor.core.publisher.Mono;

@Service
public class GetSlotInfoFromCustomGames {

    @Autowired
    private WebClient webClient;

    @Autowired
    private ResponseMessage responseMessage;

    public ResponseEntity<ResponseMessage> getSlotInfoFromCG(BookSlotSPModel gameDetails) {

        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("arenaId", gameDetails.getSpId());
            requestBody.put("sportId", gameDetails.getSportId());
            requestBody.put("dateOfBooking", gameDetails.getDateOfBooking());
            requestBody.put("startTime", gameDetails.getStartTime());
            requestBody.put("stopTime", gameDetails.getStopTime());
            requestBody.put("courtNumber", gameDetails.getCourtNumber());

            Mono<Map<String, Object>> slotState = webClient.post()
                    .uri(System.getenv("URL_FOR_CHECK_SLOT"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(requestBody))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            Map<String, Object> slot = slotState.block();

            if (Boolean.TRUE.equals(slot.get("success"))) {
                responseMessage.setSuccess(true);
                responseMessage.setMessage("Slot empty");
                return ResponseEntity.ok().body(responseMessage);

            } else {
                responseMessage.setSuccess(false);
                responseMessage.setMessage("Slot full");
                return ResponseEntity.badRequest().body(responseMessage);
            }

        } catch (Exception e) {
            responseMessage.setSuccess(false);
            responseMessage.setMessage("Internal Server Error " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }

    }
}
