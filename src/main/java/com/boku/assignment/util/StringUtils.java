package com.boku.assignment.util;

import lombok.experimental.UtilityClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class StringUtils {
    public static String findFirstWord(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        Pattern pattern = Pattern.compile("\\w+");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}
