package org.beetola.app.util;


import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class CurrencyUtil {

    public static final int COEFFICIENT_ROUNDING_SCALE = 4;
    public static final int GLOBAL_DECIMAL_SCALE = 2;
    public static final RoundingMode GLOBAL_ROUNDING_MODE = RoundingMode.HALF_UP;

    public static BigDecimal getCoefficient(BigDecimal fromRate, BigDecimal toRate) {
        return fromRate.divide(toRate, COEFFICIENT_ROUNDING_SCALE, GLOBAL_ROUNDING_MODE);
    }

}
