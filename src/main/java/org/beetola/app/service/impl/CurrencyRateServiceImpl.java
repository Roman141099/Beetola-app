package org.beetola.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.beetola.app.model.domain.CurrencyRate;
import org.beetola.app.repository.CurrencyRateRepository;
import org.beetola.app.service.CurrencyIntegrationService;
import org.beetola.app.service.CurrencyRateService;
import org.beetola.app.util.CurrencyUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CurrencyRateServiceImpl implements CurrencyRateService {

    private final CurrencyRateRepository currencyRateRepository;
    private final CurrencyIntegrationService integrationService;

    @Override
    public void saveOrUpdateCurrencyRate(String code, BigDecimal coefficient) {
        CurrencyRate toSaveOrUpdate = new CurrencyRate(code, coefficient);
        currencyRateRepository.save(toSaveOrUpdate);
    }

    @Override
    public void updateCurrencyRates() {
        Map<String, String> rates = integrationService.sendToGetCurrencyRates();
        Set<CurrencyRate> currencyRates = rates.entrySet().stream().map(e -> new CurrencyRate(e.getKey().substring(3),
                        new BigDecimal(e.getValue()).setScale(CurrencyUtil.COEFFICIENT_ROUNDING_SCALE, RoundingMode.HALF_UP)))
                .collect(Collectors.toSet());
        currencyRateRepository.saveAll(currencyRates);
    }
}
