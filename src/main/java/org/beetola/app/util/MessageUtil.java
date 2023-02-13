package org.beetola.app.util;

public abstract class MessageUtil {

    public static final String CURRENCY_UNAVAILABLE_MSG = "Sorry, '%s' currency is not available right now :(";

    public static String buildCurrencyNotAvailableMsg(String from) {
        return String.format(CURRENCY_UNAVAILABLE_MSG, from);
    }

}
