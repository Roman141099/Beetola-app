package org.beetola.app.service;

import org.beetola.app.model.dto.CurrencyConversionRs;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Validated
public interface CurrencyConversionService {

    CurrencyConversionRs exchangeToSellSome(@NotNull @Pattern(regexp = "[A-Z]{3}") String from,
                                            @NotNull @Pattern(regexp = "[A-Z]{3}") String to,
                                            @NotNull @DecimalMin("1") BigDecimal sellAmount);

    CurrencyConversionRs exchangeToBySome(@NotNull @Pattern(regexp = "[A-Z]{3}") String from,
                                          @NotNull @Pattern(regexp = "[A-Z]{3}") String to,
                                          @NotNull @DecimalMin("1") BigDecimal buyAmount);
}
