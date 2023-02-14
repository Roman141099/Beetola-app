package org.beetola.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.beetola.app.exception.CurrencyException;
import org.beetola.app.exception.WebException;
import org.beetola.app.model.dto.CurrencyRateRs;
import org.beetola.app.service.CurrencyIntegrationService;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
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
                        httpStatus.value() > HttpStatus.OK.value(), clientResponse ->
                        Mono.error(new WebException("ApiLayer returned bad response",
                                clientResponse.statusCode(), clientResponse.statusCode().getReasonPhrase())))
                .bodyToMono(CurrencyRateRs.class)
                .block();
        return validatedResponse(rs);
    }

    private static Map<String, String> validatedResponse(CurrencyRateRs rs) {
        return ofNullable(rs)
                .map(response -> {
                    Boolean isSuccess = ofNullable(response.getSuccess())
                            .orElseThrow(() ->
                                    new CurrencyException("Status from response is null!"));
                    if (!isSuccess) {
                        throw new CurrencyException("Status from response is not success!");
                    }
                    Map<String, String> filtered = response.getQuotes()
                            .entrySet().stream()
                            .filter(e -> nonNull(e.getKey()) &&
                                    e.getKey().length() == 6 &&
                                    NumberUtils.isCreatable(e.getValue()))
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    Map.Entry::getValue
                            ));
                    if (filtered.isEmpty()) {
                        throw new CurrencyException("Values from response are invalid!");
                    }
                    return filtered;
                }).orElseThrow(() -> new CurrencyException("Response body is empty!"));
    }
}
