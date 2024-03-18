package com.bookmysport.userslotbooking.MiddleWares;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class GetSportBySportIDAndSpid {
    @Autowired
    private WebClient webClient;

    public ResponseEntity<Map<String, Object>> getSportAndSpDetailsService(String spId, String sportId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Mono<Map<String, Object>> sportAndSpDetailsMono = webClient.get()
                    .uri(System.getenv("GETSPORTBYSPORTIDANDSPID_URL"))
                    .headers(headers -> {
                        headers.set("Content-Type", "application/json");
                        headers.set("spId", spId);
                        headers.set("sportId", sportId);
                    })
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                    });
            Map<String, Object> sportAndSpDetails = sportAndSpDetailsMono.block();
            if (sportAndSpDetails != null) {
                response.put("success", true);
                response.put("message", sportAndSpDetails.get("pricePerHour"));
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response.put("success", false);
                response.put("message", "No Sport exists with this spId or SportId");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Internal Server Error in getSportAndSpDetailsService. Reason: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
