package com.bookmysport.userslotbooking.Models;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class AmountMessage {
    private String message;
    private int amount;
}
