package org.beetola.app.service.impl.conversion;

import org.assertj.core.util.Lists;
import org.beetola.app.exception.CurrencyException;
import org.beetola.app.model.dto.CurrencyConversionRs;
import org.beetola.app.service.CurrencyConversionService;
import org.beetola.app.util.MessageUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class CurrencyConversionServiceTest {

    @Autowired
    CurrencyConversionService serviceToSell;
    @Autowired
    CurrencyConversionService serviceToBuy;

    @ParameterizedTest
    @MethodSource
    public void testSellAmountOf(String currencyFrom,
                                 String currencyTo,
                                 BigDecimal amount,
                                 BigDecimal resultWithCommission,
                                 BigDecimal commission) {
        CurrencyConversionRs result = serviceToSell.exchange(currencyFrom, currencyTo, amount);
        assertThat(result)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .extracting("amount", "commission")
                .containsExactly(resultWithCommission, commission);
    }

    @ParameterizedTest
    @MethodSource
    public void testBuyAmountOf(String currencyFrom,
                                String currencyTo,
                                BigDecimal amount,
                                BigDecimal resultWithCommission,
                                BigDecimal commission) {
        CurrencyConversionRs result = serviceToBuy.exchange(currencyTo, currencyFrom, amount);
        assertThat(result)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .extracting("amount", "commission")
                .containsExactly(resultWithCommission, commission);
    }

    @Test
    public void testDecimalLessThan1() {
        assertThatThrownBy(() -> serviceToSell.exchange("ABC", "ABC", BigDecimal.valueOf(0.9)))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("exchange.amount")
                .hasMessageContaining("быть больше, чем или равно 1");
    }

    @Test
    public void testCurrencyInvalid() {
        assertThatThrownBy(() -> serviceToSell.exchange("TEST", "ABC", BigDecimal.ONE))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessage("exchange.from")
                .hasMessageContaining("должно соответствовать");
    }

    @Test
    public void testCurrencyNotAvailable() {
        String notAvailableCurrency = "YYY";
        assertThatThrownBy(() -> serviceToSell.exchange(notAvailableCurrency, "ABC", BigDecimal.ONE))
                .isInstanceOf(CurrencyException.class)
                .hasMessage(MessageUtil.buildCurrencyNotAvailableMsg(notAvailableCurrency));
    }

    public static List<Arguments> testSellAmountOf() {
        return Lists.newArrayList(
                Arguments.of("RUB", "USD", BigDecimal.valueOf(150), BigDecimal.valueOf(2.02), BigDecimal.valueOf(0.04)),
                Arguments.of("USD", "RUB", BigDecimal.valueOf(2500), BigDecimal.valueOf(178341.13), BigDecimal.valueOf(3639.62))
        );
    }

    public static List<Arguments> testBuyAmountOf() {
        return Lists.newArrayList(
                Arguments.of("RUB", "USD", BigDecimal.valueOf(150), BigDecimal.valueOf(11137.23), BigDecimal.valueOf(218.38)),
                Arguments.of("USD", "RUB", BigDecimal.valueOf(2500), BigDecimal.valueOf(34.94), BigDecimal.valueOf(0.69))
        );
    }
}
