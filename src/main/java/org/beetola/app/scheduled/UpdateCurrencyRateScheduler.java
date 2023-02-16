package org.beetola.app.scheduled;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.beetola.app.service.CurrencyRateService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.scheduling.update-currency-rates.enable", havingValue = "true", matchIfMissing = true)
public class UpdateCurrencyRateScheduler {

    private final CurrencyRateService currencyRateService;

    @EventListener(ApplicationReadyEvent.class)
    @Scheduled(cron = "${app.scheduling.update-currency-rates.cron:0 0 */12 * * *}")
    public void updateCurrencies() {
        currencyRateService.updateCurrencyRates();
        log.info("Currencies SUCCESSFULLY UPLOADED");
    }

}
