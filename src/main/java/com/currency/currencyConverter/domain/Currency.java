package com.currency.currencyConverter.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "currency")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private String id;  // Changed from String to Long for auto-generation

    @Column(name = "source_currency", nullable = false, length = 10)
    private String sourceCurrency;

    @Column(name = "target_currency", nullable = false, length = 10)
    private String targetCurrency;

    @Column(name = "rate", nullable = false)
    private Double rate;

    @Column(name = "converted_amount")
    private Double convertedAmount;

    @Column(name = "timestamp", nullable = false)
    private long timestamp;


    // Constructor that matches the fields used in the service
    public Currency(String sourceCurrency, String targetCurrency, Double rate, Double convertedAmount, long timestamp) {
        this.id = sourceCurrency + "_" + targetCurrency;  // ID is a combination of source and target currency
        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.convertedAmount = convertedAmount;
        this.timestamp = timestamp;
    }
    public Currency(String id, String sourceCurrency, String targetCurrency, Double rate, long timestamp) {
        this.id = id;  // ID is a combination of source and target currency (like USD_EUR)
        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.timestamp = timestamp;
    }
}
