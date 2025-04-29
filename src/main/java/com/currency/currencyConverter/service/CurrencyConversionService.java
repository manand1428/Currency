package com.currency.currencyConverter.service;

import com.currency.currencyConverter.domain.Currency;
import com.currency.currencyConverter.repo.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CurrencyConversionService {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String API_KEY = "1SMxQumaAcC996SUXGsnlBPW44t2RR82";
    private static final String API_URL = "https://api.currencybeacon.com/v1/currencies?api_key=" + API_KEY;
    private static final String CURRENCY_BEACON_API_URL = "https://api.currencybeacon.com/v1/convert";


    public List<String> getCurrencyCodes() {
        Map<String, Object> response = restTemplate.getForObject(API_URL, Map.class);
         // Safely cast the 'response' to List
         List<Map<String, Object>> currencies = (List<Map<String, Object>>) response.get("response");
      
         return currencies.stream()
             .map(currency -> (String) currency.get("short_code"))
             .collect(Collectors.toList());
       
    }
    public Currency convertCurrency(String sourceCurrency, String targetCurrency, double amount) {
            Currency rate = fetchConversionRateFromAPI(sourceCurrency, targetCurrency, amount);
            System.out.println("rate " + rate);
            currencyRepository.save(rate); 
        double convertedAmount = rate.getRate();
        System.out.println("convertedAmount " + convertedAmount);

        Currency currency = new Currency(sourceCurrency + "_" + targetCurrency, sourceCurrency, targetCurrency, convertedAmount, System.currentTimeMillis());
        currency.setConvertedAmount(convertedAmount);  // Set the converted amount
        return currency;
    }
    

    private boolean isExpired(Currency rate) {
        long currentTime = System.currentTimeMillis();
        return currentTime - rate.getTimestamp() > 3600000;  // 1 hour in milliseconds
    }

    private Currency fetchConversionRateFromAPI(String sourceCurrency, String targetCurrency, double amount) {
        String url = String.format("%s?api_key=%s&from=%s&to=%s&amount=%.2f", 
            CURRENCY_BEACON_API_URL, API_KEY, sourceCurrency, targetCurrency, amount);

        Map<String, Object> apiResponse = restTemplate.getForObject(url, Map.class);
        
        if (apiResponse == null || apiResponse.get("response") == null) {
            throw new RuntimeException("Invalid response from Currency API");
        }

        Map<String, Object> responseMap = (Map<String, Object>) apiResponse.get("response");
        Double value = Double.valueOf(responseMap.get("value").toString());

        Currency currency = new Currency();
        currency.setSourceCurrency(sourceCurrency);
        currency.setTargetCurrency(targetCurrency);
        currency.setRate(value);
        currency.setConvertedAmount(amount);
        currency.setTimestamp(System.currentTimeMillis());
    
        return currency;
    }
    
    
}
