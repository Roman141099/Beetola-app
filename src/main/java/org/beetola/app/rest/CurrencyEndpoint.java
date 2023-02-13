package org.beetola.app.rest;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.beetola.app.model.dto.CurrencyConversionRs;
import org.beetola.app.service.CurrencyConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CurrencyEndpoint implements CurrencyApi {

    private final CurrencyConversionService serviceToSell;
    private final CurrencyConversionService serviceToBuy;

    @Override
    public ResponseEntity<CurrencyConversionRs> convertToSellSome(String from, String to, BigDecimal amount) {
        log.debug("Got request for sell money");
        ResponseEntity<CurrencyConversionRs> rs = ResponseEntity.ok(serviceToSell.exchange(from, to, amount));
        log.debug("Request for sell money succeed");
        return rs;
    }

    @Override
    public ResponseEntity<CurrencyConversionRs> convertToBuySome(String from, String to, BigDecimal amount) {
        log.debug("Got request for buy money");
        ResponseEntity<CurrencyConversionRs> rs = ResponseEntity.ok(serviceToBuy.exchange(to, from, amount));
        log.debug("Request for buy money succeed");
        return rs;
    }
}
