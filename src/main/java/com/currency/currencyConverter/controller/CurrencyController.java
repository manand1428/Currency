package com.currency.currencyConverter.controller;

import com.currency.currencyConverter.domain.*;
import com.currency.currencyConverter.domain.Currency;
import com.currency.currencyConverter.service.CurrencyConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/currency")
@CrossOrigin(origins = "*")
public class CurrencyController {

    @Autowired
    private CurrencyConversionService currencyService;

    // Endpoint to get list of available currencies
    @GetMapping("/currencies")
    public List<String> getCurrencies() {System.out.println("rrrrrrrrrr");
        return currencyService.getCurrencyCodes();
    }

    // Endpoint to convert the currency
    @GetMapping("/convert")
public Map<String, Object> convertCurrency(
        @RequestParam String sourceCurrency,
        @RequestParam String targetCurrency,
        @RequestParam double amount) {

    Currency currency = currencyService.convertCurrency(sourceCurrency, targetCurrency, amount);

    Map<String, Object> response = new HashMap<>();
    response.put("convertedAmount", currency.getConvertedAmount()); 
    response.put("targetCurrency", currency.getTargetCurrency());

    return response;
}


}
