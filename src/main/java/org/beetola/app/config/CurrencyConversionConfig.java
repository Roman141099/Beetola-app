package org.beetola.app.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;

@Getter
@Validated
@Configuration
public class CurrencyConversionConfig {

    @Value("#{${app.margin-percent:2} / 100.0}")
    private Double marginPercent;

    @PostConstruct
    public void init() {
        System.out.println();
    }

}
