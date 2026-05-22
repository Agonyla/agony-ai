package com.agony.prompt.test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: Agony
 * @create: 2026/5/22 10:17
 * @describe:
 */
public class StreamTest {

    public static void main(String[] args) {

        // 找出出现频率最多的值
        List<String> list = List.of("A", "B", "A", "C", "A", "B");

        String result = list.stream()
                .collect(Collectors.groupingBy(t -> t, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("null");
        System.out.println(result);

    }
}