package com.agony.prompt.test.practice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: Agony
 * @create: 2026/5/22 12:52
 * @describe:
 */
public class StreamPractice1 {

    public static void main(String[] args) {
        List<String> list = List.of("Tom", "Jerry", "Jack", "Rose", "Tony");

        System.out.println("找出所有以 T 开头的名字");
        findStartWithT(list).forEach(System.out::println);

        System.out.println("\n把所有名字转成大写");
        toUpperCase(list).forEach(System.out::println);

        System.out.println("\n统计每个名字的长度");
        countEachLength(list).entrySet().forEach(System.out::println);

        System.out.println("\n找出长度大于 4 的名字");
        lengthBiggerThan4(list).forEach(System.out::println);

        System.out.println("\n找出名字长度的总和");
        System.out.println(allLength(list));

    }

    /**
     * 找出所有以 T 开头的名字
     *
     * @param list
     * @return
     */
    public static List<String> findStartWithT(List<String> list) {

        return list.stream()
                .filter(s -> s.startsWith("T")).toList();
    }

    /**
     * 把所有名字转成大写
     *
     * @param list
     * @return
     */
    public static List<String> toUpperCase(List<String> list) {
        return list.stream()
                .map(String::toUpperCase)
                .toList();
    }

    /**
     * 统计每个名字的长度
     *
     * @param list
     * @return
     */
    public static Map<String, Integer> countEachLength(List<String> list) {

        return list.stream()
                .collect(Collectors.toMap(s -> s, String::length));

    }

    /**
     * 找出长度大于 4 的名字
     *
     * @param list
     * @return
     */
    public static List<String> lengthBiggerThan4(List<String> list) {

        return list.stream()
                .filter(s -> s.length() > 4)
                .toList();
    }

    /**
     * 找出名字长度的总和
     *
     * @param list
     * @return
     */
    public static int allLength(List<String> list) {

        return list.stream()
                .mapToInt(String::length)
                .sum();
    }
}