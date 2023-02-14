package org.beetola.app.service.impl;

import org.beetola.app.model.domain.CurrencyRate;
import org.beetola.app.service.CurrencyRateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class CurrencyRateServiceTest {

    @Autowired
    CurrencyRateService service;
    @PersistenceContext
    EntityManager em;

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

    public static List<BigDecimal> testInvalidAmount() {
        return List.of(BigDecimal.valueOf(1.11111), new BigDecimal("11111111111111111.1111"));
    }

}
