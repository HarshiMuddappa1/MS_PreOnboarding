package com.learning.AsynchronousProgram.dao;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ExchangeRatesDAOImpl implements ExchangeRatesDAO{

    public Map<String, Integer> getSource1CurrencyRates(){

        Map<String, Integer> source1CurrencyRates = new HashMap<>();
        source1CurrencyRates.put("INR", 80);
        source1CurrencyRates.put("Euro", 2);
        source1CurrencyRates.put("US", 5);
        source1CurrencyRates.put("CAD", 10);

        return source1CurrencyRates;
    }

    public Map<String, Integer> getSource2CurrencyRates(){

        Map<String, Integer> source2CurrencyRates = new HashMap<>();
        source2CurrencyRates.put("INR", 81);
        source2CurrencyRates.put("Euro", 1);
        source2CurrencyRates.put("US", 7);
        source2CurrencyRates.put("CAD", 12);

        return source2CurrencyRates;
    }

    public Map<String, Integer> getSource3CurrencyRates(){

        Map<String, Integer> source3CurrencyRates = new HashMap<>();
        source3CurrencyRates.put("INR", 70);
        source3CurrencyRates.put("Euro", 3);
        source3CurrencyRates.put("US", 4);
        source3CurrencyRates.put("CAD", 9);

        return source3CurrencyRates;
    }

    public Map<String, Integer> getSource4CurrencyRates(){

        Map<String, Integer> source4CurrencyRates = new HashMap<>();
        source4CurrencyRates.put("INR", 75);
        source4CurrencyRates.put("Euro", 3);
        source4CurrencyRates.put("US", 5);
        source4CurrencyRates.put("CAD", 9);

        return source4CurrencyRates;
    }

    public Map<String, Integer> getSource5CurrencyRates(){

        Map<String, Integer> source5CurrencyRates = new HashMap<>();
        source5CurrencyRates.put("INR", 78);
        source5CurrencyRates.put("Euro", 2);
        source5CurrencyRates.put("US", 6);
        source5CurrencyRates.put("CAD", 11);

        return source5CurrencyRates;
    }
}
