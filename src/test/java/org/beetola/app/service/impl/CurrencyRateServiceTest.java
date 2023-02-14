package org.beetola.app.service.impl;

import org.assertj.core.groups.Tuple;
import org.beetola.app.model.domain.CurrencyRate;
import org.beetola.app.service.CurrencyIntegrationService;
import org.beetola.app.service.CurrencyRateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class CurrencyRateServiceTest {

    @Autowired
    CurrencyRateService service;
    @PersistenceContext
    EntityManager em;
    @MockBean
    CurrencyIntegrationService integrationServiceBEAN;

    @ParameterizedTest
    @MethodSource
    public void testInvalidAmount(BigDecimal amount) {
        assertThatThrownBy(() -> service.saveOrUpdateCurrencyRate("ABC", amount))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Number's precision or scale incorrect");
    }

    @Test
    @Transactional//For rollback after saving
    public void testInsertNewRate() {
        String id = "ABC";
        BigDecimal coef = BigDecimal.valueOf(1.0123);
        service.saveOrUpdateCurrencyRate(id, coef);
        CurrencyRate entity = em.find(CurrencyRate.class, id);
        assertThat(entity)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("coefficient", coef);
    }

    @Test
    @Transactional
    public void testUpdateRates() {
        Map<String, String> mockedCurrencyRs = Map.of(
                "AAABBB", "1.012",
                "CCCDDD", "1234.123456",
                "GBPUSD", "100.1"
        );
        Mockito.when(integrationServiceBEAN.sendToGetCurrencyRates())
                .thenReturn(mockedCurrencyRs);
        service.updateCurrencyRates();
        List<CurrencyRate> rates = em.createQuery("select c from CurrencyRate c", CurrencyRate.class)
                .getResultList();
        assertThat(rates)
                .hasSize(3 + mockedCurrencyRs.size())
                .extracting("id", "coefficient")
                .contains(
                        mockedCurrencyRs.entrySet()
                                .stream().map(e -> Tuple.tuple(
                                        e.getKey().substring(3),
                                        new BigDecimal(e.getValue()).setScale(4, RoundingMode.HALF_UP)))
                                .toArray(Tuple[]::new)
                );
    }

    public static List<BigDecimal> testInvalidAmount() {
        return List.of(BigDecimal.valueOf(1.11111), new BigDecimal("11111111111111111.1111"));
    }

}
