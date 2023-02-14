package org.beetola.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.beetola.app.exception.WebException;
import org.beetola.app.model.dto.CurrencyRateRs;
import org.beetola.app.service.CurrencyIntegrationService;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;

@Slf4j
@RequiredArgsConstructor
public class ApiLayerCurrencyIntegrationService implements CurrencyIntegrationService {

    private final WebClient webClient;
    private final String liveCurrencyPath;
    private final Map<String, List<String>> queryParams;

    @Override
    public Map<String, String> sendToGetCurrencyRates() {
        CurrencyRateRs rs = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(liveCurrencyPath)
                        .queryParams(new LinkedMultiValueMap<>(queryParams))
                        .build())
                .retrieve()
                .onStatus(httpStatus ->
                        httpStatus.value() > 200, clientResponse ->
                        Mono.error(new WebException("ApiLayer returned bad response",
                                clientResponse.statusCode(), clientResponse.statusCode().getReasonPhrase())))
                .bodyToMono(CurrencyRateRs.class)
                .block();
        return validatedResponse(rs);
    }

    private static Map<String, String> validatedResponse(CurrencyRateRs rs) {
        return ofNullable(rs)
                .map(CurrencyRateRs::getQuotes)
                .orElseGet(() -> {
                    log.warn("Response from currency rs is null");
                    return new HashMap<>();
                });
    }
}
