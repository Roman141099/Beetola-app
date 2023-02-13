package org.beetola.app.service;

import lombok.RequiredArgsConstructor;
import org.beetola.app.config.CurrencyConversionConfig;
import org.beetola.app.exception.CurrencyException;
import org.beetola.app.model.domain.CurrencyRate;
import org.beetola.app.model.dto.CurrencyConversionRs;
import org.beetola.app.repository.CurrencyRateRepository;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;
import static org.beetola.app.util.CurrencyUtil.*;
import static org.beetola.app.util.MessageUtil.buildCurrencyNotAvailableMsg;

@Validated
@RequiredArgsConstructor
public abstract class CurrencyConversionService {

    private final CurrencyRateRepository baselineRepository;
    private final CurrencyConversionConfig currencyConfig;

    public CurrencyConversionRs exchange(@NotNull @Pattern(regexp = "[A-Z]{3}") String from,
                                         @NotNull @Pattern(regexp = "[A-Z]{3}") String to,
                                         @NotNull @DecimalMin("1") BigDecimal amount) {
        CurrencyRate fromRate = baselineRepository.findById(from)
                .orElseThrow(() -> new CurrencyException(buildCurrencyNotAvailableMsg(from)));
        CurrencyRate toRate = baselineRepository.findById(to)
                .orElseThrow(() -> new CurrencyException(buildCurrencyNotAvailableMsg(to)));
        BigDecimal coefficient = getCoefficient(toRate.getCoefficient(), fromRate.getCoefficient());

        BigDecimal result = coefficient.multiply(amount).setScale(GLOBAL_DECIMAL_SCALE, GLOBAL_ROUNDING_MODE);
        BigDecimal commission = result.multiply(valueOf(currencyConfig.getMarginPercent()))
                .setScale(GLOBAL_DECIMAL_SCALE, GLOBAL_ROUNDING_MODE);
        return buildResult(result, commission);
    }

    protected abstract CurrencyConversionRs buildResult(BigDecimal result, BigDecimal commission);

}
