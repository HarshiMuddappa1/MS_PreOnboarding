package com.learning.AsynchronousProgram.service;

import com.learning.AsynchronousProgram.dao.ExchangeRatesDAO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ExchangeRatesServiceImpl implements ExchangeRatesService{

    ExchangeRatesDAO exchangeRatesDAO;

    public ExchangeRatesServiceImpl(ExchangeRatesDAO exchangeRatesDAO){
        this.exchangeRatesDAO = exchangeRatesDAO;
    }
    public double getExchangeRates(String source, String fromCurrency,
                                       String toCurrency){

        Map<String, Integer> currencyRates = switch (source) {
            case "Source1" -> exchangeRatesDAO.getSource1CurrencyRates();
            case "Source2" -> exchangeRatesDAO.getSource2CurrencyRates();
            case "Source3" -> exchangeRatesDAO.getSource3CurrencyRates();
            case "Source4" -> exchangeRatesDAO.getSource4CurrencyRates();
            case "Source5" -> exchangeRatesDAO.getSource5CurrencyRates();
            default -> new HashMap<>();
        };

        int fromCurrencyRank = currencyRates.get(fromCurrency);
        int toCurrencyRank = currencyRates.get(toCurrency);

        return ((double) fromCurrencyRank / toCurrencyRank) * 10;

    }
}
