package com.example.demo.stripe;

import lombok.Data;

@Data
public class pago {
    public enum Currency {
        USD, EUR;
    }

    private String description;
    private int amount;
    private Currency currency;

}