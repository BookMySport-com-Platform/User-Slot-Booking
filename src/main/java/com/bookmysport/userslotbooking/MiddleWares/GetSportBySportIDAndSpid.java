package com.bookmysport.userslotbooking.MiddleWares;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.bookmysport.userslotbooking.Models.ResponseMessage;

import reactor.core.publisher.Mono;

@Service
public class GetSportBySportIDAndSpid {
    @Autowired
    private WebClient webClient;

    @Value("${GETSPORTBYSPORTIDANDSPID_URL}")
    String getsportbyspidandsportidURL;

    @Autowired
    private ResponseMessage responseMessage;

    public int getSportAndSpDetailsService(String token, String role, String sportId) {
        try {
            Mono<Map<String, Object>> sportAndSpDetailsMono = webClient.get()
                    .uri(getsportbyspidandsportidURL)
                    .headers(headers -> {
                        headers.set("Content-Type", "application/json");
                        headers.set("token", token);
                        headers.set("role", role);
                        headers.set("sportId", sportId);
                    })
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                    });
            Map<String, Object> sportAndSpDetails = sportAndSpDetailsMono.block();
            System.out.println("This");
            System.out.println(sportAndSpDetails);
            if (sportAndSpDetails != null) {
                    System.out.println("Inside middleware");
                    System.out.println(sportAndSpDetails.get("pricePerHour"));
                    return (Integer)sportAndSpDetails.get("pricePerHour");
            } else {
                responseMessage.setSuccess(false);
                responseMessage.setMessage("No Sport exists with this spId or SportId");
                return 0;
            }
        } catch (Exception e) {
            responseMessage.setSuccess(false);
            responseMessage.setMessage("Internal Server Error in GetSportBySportIDAndSpid. Method: getSportAndSpDetailsService. Error message: " + e.getMessage());
            return 0;
        }
    }
}
