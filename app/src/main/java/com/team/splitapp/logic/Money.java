package com.team.splitapp.logic;

import java.math.BigDecimal;
import java.util.Locale;

/**
 * Converts between what the user types ("12.50") and what we store (1250 halalas).
 *
 * <p>Team rule: money is a {@code long} number of halalas everywhere in the app.
 * Never use float or double for an amount.
 */
public final class Money {

    public static final int HALALAS_PER_RIYAL = 100;

    /** Largest amount accepted from the user: 10,000,000.00 SAR. Stops overflow and typos. */
    public static final long MAX_HALALAS = 1_000_000_000L;

    private Money() {
    }

    /**
     * Parses user input into halalas.
     *
     * <p>Accepts "12", "12.5", "12.50", ".5", surrounding spaces, thousands commas ("1,250.00"),
     * Arabic-Indic digits and the Arabic decimal separator. Returns {@code null} if the text is
     * empty, not a number, negative, has more than 2 decimals, or is above {@link #MAX_HALALAS}.
     * Zero is returned as 0: the caller decides whether 0 is allowed (for an expense it is not).
     */
    public static Long parse(String text) {
        if (text == null) {
            return null;
        }
        BigDecimal value = toDecimal(text);
        if (value == null || value.signum() < 0) {
            return null;
        }
        if (value.stripTrailingZeros().scale() > 2) {
            return null; // 12.345
        }
        BigDecimal halalas = value.movePointRight(2);
        if (halalas.compareTo(BigDecimal.valueOf(MAX_HALALAS)) > 0) {
            return null;
        }
        return halalas.longValueExact();
    }

    /** Why {@link #parse} rejected a value, for showing the right TextInputLayout error. */
    public enum Problem { EMPTY, NOT_A_NUMBER, NOT_POSITIVE, TOO_MANY_DECIMALS, TOO_LARGE }

    /**
     * Validates an expense/payment amount. Returns {@code null} if it is valid (> 0, at most two
     * decimals, not too large), otherwise the first problem found.
     */
    public static Problem validatePositive(String text) {
        if (text == null || text.trim().isEmpty()) {
            return Problem.EMPTY;
        }
        BigDecimal value = toDecimal(text);
        if (value == null) {
            return Problem.NOT_A_NUMBER;
        }
        if (value.signum() <= 0) {
            return Problem.NOT_POSITIVE;
        }
        if (value.stripTrailingZeros().scale() > 2) {
            return Problem.TOO_MANY_DECIMALS;
        }
        if (value.movePointRight(2).compareTo(BigDecimal.valueOf(MAX_HALALAS)) > 0) {
            return Problem.TOO_LARGE;
        }
        return null;
    }

    /** 1250 → "12.50", -5500 → "-55.00". Plain digits, for edit fields and CSV. */
    public static String format(long halalas) {
        long abs = Math.abs(halalas);
        String sign = halalas < 0 ? "-" : "";
        return String.format(Locale.US, "%s%d.%02d", sign, abs / HALALAS_PER_RIYAL, abs % HALALAS_PER_RIYAL);
    }

    /** 1250 → "12.50 SAR". For labels; the currency code comes from the group. */
    public static String format(long halalas, String currency) {
        return format(halalas) + " " + currency;
    }

    /** Plain decimal only: optional minus, digits, optional dot + digits. No exponents. */
    private static final java.util.regex.Pattern DECIMAL =
            java.util.regex.Pattern.compile("-?(\\d+\\.?\\d*|\\.\\d+)");

    private static BigDecimal toDecimal(String text) {
        String s = normalizeDigits(text.trim()).replace(",", "");
        if (!DECIMAL.matcher(s).matches()) {
            return null;
        }
        return new BigDecimal(s);
    }

    /** Maps Arabic-Indic (٠-٩) and Eastern Arabic-Indic (۰-۹) digits and '٫' to ASCII. */
    static String normalizeDigits(String s) {
        StringBuilder out = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= '٠' && c <= '٩') {
                out.append((char) ('0' + (c - '٠')));
            } else if (c >= '۰' && c <= '۹') {
                out.append((char) ('0' + (c - '۰')));
            } else if (c == '٫') {
                out.append('.');
            } else if (c == '٬') {
                out.append(',');
            } else {
                out.append(c);
            }
        }
        return out.toString();
    }
}
