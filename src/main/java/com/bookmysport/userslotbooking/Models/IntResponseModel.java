package com.bookmysport.userslotbooking.Models;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class IntResponseModel {
    private boolean success;
    private int number;
    private String message;
}
