package com.peltashield.inferno.utils;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by javier on 12/19/14.
 */
public class StringCommons {

    public static String capitalize(String s) {
        return s.length() == 0 ? "" : s.substring(0, 1)
                .toUpperCase(Locale.ENGLISH).concat(s.substring(1));
    }

    /**
     * Example: containsInsensitive(bar, b) = true
     */
    public static boolean containsInsensitive(String base, String contains) {
        return Pattern
                .compile(contains, Pattern.CASE_INSENSITIVE | Pattern.LITERAL)
                .matcher(base).find();
    }

	//API 9 needed
	public static String removeAccents(String text) {
		return text == null ? null : Normalizer.normalize(text, Normalizer.Form.NFD)
				.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}

    /**
     * It works with Latin1 and Latin2
     */
    public static String removeDiacritics(String source) {
        String tab00c0 = "AAAAAAACEEEEIIII" +
                "DNOOOOO\u00d7\u00d8UUUUYI\u00df" +
                "aaaaaaaceeeeiiii" +
                "\u00f0nooooo\u00f7\u00f8uuuuy\u00fey" +
                "AaAaAaCcCcCcCcDd" +
                "DdEeEeEeEeEeGgGg" +
                "GgGgHhHhIiIiIiIi" +
                "IiJjJjKkkLlLlLlL" +
                "lLlNnNnNnnNnOoOo" +
                "OoOoRrRrRrSsSsSs" +
                "SsTtTtTtUuUuUuUu" +
                "UuUuWwYyYZzZzZzF";
        char[] vysl = new char[source.length()];
        char one;
        for (int i = 0; i < source.length(); i++) {
            one = source.charAt(i);
            if (one >= '\u00c0' && one <= '\u017f') {
                one = tab00c0.charAt((int) one - '\u00c0');
            }
            vysl[i] = one;
        }
        return new String(vysl);
    }

    public static boolean matches(String base, String match) {
        return StringCommons.containsInsensitive(StringCommons.removeDiacritics(base),
                StringCommons.removeDiacritics(match));
    }
}
