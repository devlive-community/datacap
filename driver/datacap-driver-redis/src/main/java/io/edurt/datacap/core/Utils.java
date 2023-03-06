package io.edurt.datacap.core;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

public class Utils
{
    public static boolean isNumber(String str)
    {
        if (str == null || str.length() == 0) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            boolean digit = Character.isDigit(str.charAt(i));
            if (!digit) {
                return false;
            }
        }
        return true;
    }

    public static <T> List<T> toList(T[] arr)
    {
        if (arr == null) {
            return null;
        }
        return Arrays.stream(arr)
                .collect(Collectors.toList());
    }

    public static Op parseSql(String rawSql)
    {
        // for IDEA database tool only
        if (rawSql.contains("SELECT 'keep alive'")) {
            return new Op(rawSql, null, "PING", new String[0]);
        }

        // hints
        List<String> lines = new BufferedReader(new StringReader(rawSql))
                .lines()
                .collect(Collectors.toList());

        List<String> hintLines = new ArrayList<>();
        List<String> sqlLines = new ArrayList<>();
        lines.forEach(line -> {
            if (line.startsWith("--")) {
                hintLines.add(line);
            }
            else {
                sqlLines.add(line);
            }
        });

        List<Hint> hints = hintLines
                .stream()
                .map(line -> {
                    String hintStr = line.replace("--", "")
                            .replaceAll(" ", "");
                    String[] arr = hintStr.split(":");
                    return new Hint(HintKey.fromString(arr[0]), arr[1]);
                }).collect(Collectors.toList());

        // sql to execute
        StringBuilder sb = new StringBuilder();
        sqlLines.forEach(sb::append);

        String sql = sb.toString();

        String[] arr = sql.split(" ");

        String commandString = arr[0];

        if (arr.length == 1) {
            return new Op(rawSql, hints, commandString, new String[0]);
        }
        else {
            String[] commandParams = Arrays.copyOfRange(arr, 1, arr.length);
            return new Op(rawSql, hints, commandString, commandParams);
        }
    }

    public static Map<String, String> parseQueryStringToMap(String queryString)
    {
        String[] params = queryString.split("&");
        Map<String, String> map = new HashMap<>();
        for (String param : params) {
            String[] p = param.split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        return map;
    }
}
