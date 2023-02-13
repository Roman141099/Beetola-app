package org.beetola.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.beetola.app.model.domain.CurrencyRate;
import org.beetola.app.repository.CurrencyRateRepository;
import org.beetola.app.service.CurrencyRateService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CurrencyRateServiceImpl implements CurrencyRateService {

    private final CurrencyRateRepository currencyRateRepository;

    @Override
    public void saveOrUpdateCurrencyRate(String code, BigDecimal coefficient) {
        CurrencyRate toSaveOrUpdate = currencyRateRepository.findById(code)
                .orElse(new CurrencyRate());
        toSaveOrUpdate.setId(code);
        toSaveOrUpdate.setCoefficient(coefficient);
        currencyRateRepository.save(toSaveOrUpdate);
    }
}
