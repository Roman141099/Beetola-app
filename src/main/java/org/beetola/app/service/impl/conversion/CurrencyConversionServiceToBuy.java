package org.beetola.app.service.impl.conversion;

import org.beetola.app.config.CurrencyConversionConfig;
import org.beetola.app.model.dto.CurrencyConversionRs;
import org.beetola.app.repository.CurrencyRateRepository;
import org.beetola.app.service.CurrencyConversionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service("serviceToBuy")
public class CurrencyConversionServiceToBuy extends CurrencyConversionService {

    public CurrencyConversionServiceToBuy(CurrencyRateRepository baselineRepository,
                                          CurrencyConversionConfig currencyConfig) {
        super(baselineRepository, currencyConfig);
    }

    @Override
    public CurrencyConversionRs buildResult(BigDecimal result, BigDecimal commission) {
        BigDecimal resultWithCommission = result.add(commission);
        return new CurrencyConversionRs()
                .amount(resultWithCommission)
                .commission(commission);
    }
}
