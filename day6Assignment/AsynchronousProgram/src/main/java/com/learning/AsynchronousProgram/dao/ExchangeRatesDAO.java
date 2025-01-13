package com.learning.AsynchronousProgram.dao;

import java.util.Map;

public interface ExchangeRatesDAO {

    public Map<String, Integer> getSource1CurrencyRates();
    public Map<String, Integer> getSource2CurrencyRates();
    public Map<String, Integer> getSource3CurrencyRates();
    public Map<String, Integer> getSource4CurrencyRates();
    public Map<String, Integer> getSource5CurrencyRates();
}
