package com.bookmysport.userslotbooking.MiddleWares;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.bookmysport.userslotbooking.Models.IntResponseModel;

import reactor.core.publisher.Mono;

@Service
public class GetSportBySportIDAndSpid {
    @Autowired
    private WebClient webClient;

    @Value("${GETSPORTBYSPORTIDANDSPID_URL}")
    String getsportbyspidandsportidURL;

    @Autowired
    private IntResponseModel intResponseMessage;

    public ResponseEntity<IntResponseModel> getSportAndSpDetailsService(String spId, String sportId) {
        try {
            Mono<Map<String, Object>> sportAndSpDetailsMono = webClient.get()
                    .uri(getsportbyspidandsportidURL)
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
                intResponseMessage.setSuccess(true);
                intResponseMessage.setNumber((Integer) sportAndSpDetails.get("pricePerHour"));
                return ResponseEntity.ok().body(intResponseMessage);
            } else {
                intResponseMessage.setSuccess(false);
                intResponseMessage.setMessage("No Sport exists with this spId or SportId");
                return ResponseEntity.badRequest().body(intResponseMessage);
            }
        } catch (Exception e) {
            intResponseMessage.setSuccess(false);
            intResponseMessage.setMessage("Internal Server Error " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(intResponseMessage);
        }
    }
}
