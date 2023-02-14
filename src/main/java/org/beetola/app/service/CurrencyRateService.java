package org.beetola.app.service;

import org.beetola.app.validation.annotation.DecimalSize;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Validated
public interface CurrencyRateService {

    void saveOrUpdateCurrencyRate(@NotNull @Pattern(regexp = "[A-Z]{3}") String code,
                                  @NotNull
                                  @DecimalMin("0")
                                  @DecimalSize(maxPrecision = 20, maxScale = 4) BigDecimal coefficient);

    void updateCurrencyRates();

}
