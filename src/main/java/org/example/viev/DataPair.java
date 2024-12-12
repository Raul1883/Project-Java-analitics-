package org.example.viev;

import java.util.List;
import java.util.Map;

public record DataPair(String x, Number y) {

    public static List<DataPair> fromMap(Map<String, Number> map) {
        return map.entrySet().stream()
                .map(x -> new DataPair(x.getKey(), x.getValue()))
                .toList();
    }
}