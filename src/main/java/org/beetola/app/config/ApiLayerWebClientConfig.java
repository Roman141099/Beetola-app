package org.beetola.app.config;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.beetola.app.service.CurrencyIntegrationService;
import org.beetola.app.service.impl.ApiLayerCurrencyIntegrationService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//We can switch between APIs using @Profile or @ConditionalOn...
@Setter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "integration.apilayer")
public class ApiLayerWebClientConfig {

    private String baseUri;
    private String liveCurrencyRatesPath;
    private Map<String, List<String>> queryParams = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();

    public WebClient apiLayerWebClient() {
        return WebClient.builder()
                .baseUrl(baseUri)
                .defaultHeaders(httpHeaders -> headers.forEach(httpHeaders::add))
                .build();
    }

    @Bean
    public CurrencyIntegrationService apiLayerIntegrationService() {
        return new ApiLayerCurrencyIntegrationService(apiLayerWebClient(), liveCurrencyRatesPath, queryParams);
    }

}
