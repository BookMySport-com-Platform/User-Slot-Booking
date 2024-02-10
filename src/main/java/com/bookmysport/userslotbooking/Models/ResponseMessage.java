package com.bookmysport.userslotbooking.Models;

import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class ResponseMessage {
    private Boolean success;
    private String message;
    private Map<String, Object> userDetails;
}
