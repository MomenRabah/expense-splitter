package com.team.splitapp.logic;

/**
 * Converts between what the user types ("12.50") and what we store (1250 halalas).
 *
 * Team rule: money is a long number of halalas everywhere in the app.
 * Never use float or double for an amount.
 */
public class Money {

    /** Returned by parse() when the text is not a valid amount. */
    public static final long INVALID = -1;

    /**
     * Converts text like "12", "12.5" or "12.50" into halalas (1200, 1250, 1250).
     * Returns INVALID if the text is empty, negative, not a number,
     * or has more than 2 digits after the dot.
     * "0" returns 0, so the caller must still check that the amount is greater than 0.
     */
    public static long parse(String text) {
        if (text == null) {
            return INVALID;
        }
        text = text.trim();
        if (text.length() == 0) {
            return INVALID;
        }

        String riyalPart;
        String halalaPart;
        int dot = text.indexOf('.');
        if (dot == -1) {
            riyalPart = text;
            halalaPart = "";
        } else {
            riyalPart = text.substring(0, dot);
            halalaPart = text.substring(dot + 1);
        }

        if (riyalPart.length() == 0 && halalaPart.length() == 0) {
            return INVALID; // just "."
        }
        if (riyalPart.length() == 0) {
            riyalPart = "0"; // ".5" means 0.5
        }
        if (!isDigits(riyalPart) || !isDigits(halalaPart)) {
            return INVALID;
        }
        if (halalaPart.length() > 2) {
            return INVALID; // 12.345
        }
        if (riyalPart.length() > 9) {
            return INVALID; // too large
        }

        // Make the halala part exactly 2 digits: "" -> "00", "5" -> "50"
        while (halalaPart.length() < 2) {
            halalaPart = halalaPart + "0";
        }

        long riyals = Long.parseLong(riyalPart);
        long halalas = Long.parseLong(halalaPart);
        return riyals * 100 + halalas;
    }

    /** Converts halalas into text: 1250 -> "12.50", -5500 -> "-55.00". */
    public static String format(long halalas) {
        String sign = "";
        if (halalas < 0) {
            sign = "-";
            halalas = -halalas;
        }
        long riyals = halalas / 100;
        long rest = halalas % 100;
        String restText;
        if (rest < 10) {
            restText = "0" + rest;
        } else {
            restText = "" + rest;
        }
        return sign + riyals + "." + restText;
    }

    /** Same as format() with the currency after it: 1250 -> "12.50 SAR". */
    public static String format(long halalas, String currency) {
        return format(halalas) + " " + currency;
    }

    /** True if every character of text is a digit 0-9 (an empty string counts as true). */
    private static boolean isDigits(String text) {
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
