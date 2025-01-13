package com.learning.AsynchronousProgram.rest;

import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/exchangeRates")
public class ExchangeRateFetcher {


    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String url = "http://localhost:8080/exchangeRates/source%s/rate/from=%s/to=%s";

    @GetMapping("/{from}/to/{to}")
    public Map<String, Double> getExchangeRate(@PathVariable("from") String fromCurrency,
                                               @PathVariable("to") String toCurrency) throws ExecutionException, InterruptedException {


        Map<String, Double> exchangeRates = new HashMap<>();
        Map<String, Long> responseTime = new HashMap<>();

        List<CompletableFuture<Double>> futures = new ArrayList<>();

        for (int i = 1; i <= 5; i++ ) {
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create(
                            String.format(url, i, fromCurrency, toCurrency)))
                    .build();

            int finalI = i;
            long startTime = System.currentTimeMillis();
            CompletableFuture<Double> future =
                    client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                            .thenApply(response -> {
                                if (response.statusCode() == 200) {
                                    Double rate = Double.parseDouble(response.body());
                                    exchangeRates.put(("Source" + finalI), rate);
                                    return rate;
                                }
                                return 0.0;
                            });
            long endTime = System.currentTimeMillis();
            long totalTimeTaken = (endTime - startTime) ;
            responseTime.put(("Source" + finalI), totalTimeTaken);
            futures.add(future);
        }

        responseTime = responseTime.entrySet().stream().sorted(
                (e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        responseTime.forEach((key, value) -> System.out.println(key + " " + value));

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> exchangeRates).get();
    }

}
