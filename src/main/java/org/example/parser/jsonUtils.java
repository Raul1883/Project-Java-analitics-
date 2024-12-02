package org.example.parser;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class jsonUtils {
    public static ArrayList<String> getStringValues(String jsonString, String key) {
        ArrayList<String> matches = new ArrayList<>();
        Pattern pattern = Pattern.compile("\"%s\":\".*?\"".formatted(key));
        Matcher matcher = pattern.matcher(jsonString);

        while (matcher.find()) {
            String s = matcher.group().replace("\"apiUrl\":\"", "").replace("\"", "");
            matches.add(s);
        }

        return matches;
    }

    public static String getStringValue(String jsonString, String key) {
        Pattern pattern = Pattern.compile("\"%s\":\".*?\"".formatted(key));
        Matcher matcher = pattern.matcher(jsonString);

        return matcher.find() ? matcher.group().replace("\"" + key + "\":\"", "").replace("\"", "") : null;
    }

    public static int getIntValue(String jsonString, String key) {
        Pattern pattern = Pattern.compile("\"%s\":\\d*".formatted(key));
        Matcher matcher = pattern.matcher(jsonString);

        return matcher.find() ? Integer.parseInt(matcher.group().split(":")[1]) : -1;
    }
}
