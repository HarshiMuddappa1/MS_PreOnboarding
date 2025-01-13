package com.learning.AsynchronousProgram.service;

import java.math.BigDecimal;

public interface ExchangeRatesService {

    public double getExchangeRates(String source, String fromCurrency,
                                       String toCurrency);
}
