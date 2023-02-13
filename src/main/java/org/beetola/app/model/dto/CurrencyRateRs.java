package org.beetola.app.model.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class CurrencyRateRs {

    private Boolean success;
    private String source;
    private Map<String, String> quotes = new HashMap<>();

}
