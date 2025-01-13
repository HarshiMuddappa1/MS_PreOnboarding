package com.learning.AsynchronousProgram.rest;

import com.learning.AsynchronousProgram.service.ExchangeRatesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.Double;

@RestController
@RequestMapping("/exchangeRates")
public class ExchangeRateCalculator {

    ExchangeRatesService exchangeRatesService;

    public ExchangeRateCalculator(ExchangeRatesService exchangeRatesService){
        this.exchangeRatesService = exchangeRatesService;
    }
    // http://localhost:8080/exchangeRates/source2/rate?from=US,to=INR
    @GetMapping("/source1/rate/from={from}/to={to}")
    public ResponseEntity<Double> getExchangeRateFromSource1
            (@PathVariable String from, @PathVariable String to){
         Double exchangeVal =
                 exchangeRatesService.getExchangeRates("Source1", from, to);
        return new ResponseEntity<>(exchangeVal, HttpStatus.OK);
    }

    @GetMapping("/source2/rate/from={from}/to={to}")
    public ResponseEntity<Double> getExchangeRateFromSource2
            (@PathVariable String from, @PathVariable String to){
        Double exchangeVal =
                exchangeRatesService.getExchangeRates("Source2", from, to);
        return new ResponseEntity<>(exchangeVal, HttpStatus.OK);
    }

    @GetMapping("/source3/rate/from={from}/to={to}")
    public ResponseEntity<Double> getExchangeRateFromSource3
            (@PathVariable String from, @PathVariable String to){
        Double exchangeVal =
                exchangeRatesService.getExchangeRates("Source3", from, to);
        return new ResponseEntity<>(exchangeVal, HttpStatus.OK);
    }

    @GetMapping("/source4/rate/from={from}/to={to}")
    public ResponseEntity<Double> getExchangeRateFromSource4
            (@PathVariable String from, @PathVariable String to){
        Double exchangeVal =
                exchangeRatesService.getExchangeRates("Source4", from, to);
        return new ResponseEntity<>(exchangeVal, HttpStatus.OK);
    }

    @GetMapping("/source5/rate/from={from}/to={to}")
    public ResponseEntity<Double> getExchangeRateFromSource5
            (@PathVariable String from, @PathVariable String to){
        Double exchangeVal =
                exchangeRatesService.getExchangeRates("Source5", from, to);
        return new ResponseEntity<>(exchangeVal, HttpStatus.OK);
    }

}
