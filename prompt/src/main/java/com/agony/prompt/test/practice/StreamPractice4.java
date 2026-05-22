package com.agony.prompt.test.practice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: Agony
 * @create: 2026/5/22 14:03
 * @describe:
 */
public class StreamPractice4 {

    public static void main(String[] args) {
        List<String> risks = List.of("高风险", "低风险", "高风险", "中风险", "高风险", "中风险");

        System.out.println("统计每种风险出现次数");
        findEachRickCount(risks).entrySet().forEach(System.out::println);

        System.out.println("\n找出出现次数最多的风险");
        System.out.println(findRisk(risks));

        System.out.println("\n找出所有非低风险结果");
        findRisks(risks).forEach(System.out::println);

        System.out.println("\n判断是否存在高风险");
        System.out.println(existHighRisk(risks));

        System.out.println("\n判断是否全部都是低风险");
        System.out.println(allLowRisks(risks));
    }

    /**
     * 统计每种风险出现次数
     *
     * @param risks
     * @return
     */
    public static Map<String, Long> findEachRickCount(List<String> risks) {

        return risks.stream()
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
    }

    /**
     * 找出出现次数最多的风险
     *
     * @param risks
     * @return
     */
    public static String findRisk(List<String> risks) {
        return risks.stream()
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * 找出所有非低风险结果
     *
     * @param risks
     * @return
     */
    public static List<String> findRisks(List<String> risks) {
        return risks.stream()
                .filter(r -> !r.equals("低风险"))
                .toList();
    }

    /**
     * 判断是否存在高风险
     *
     * @param risks
     * @return
     */
    public static boolean existHighRisk(List<String> risks) {

        return risks.stream()
                .anyMatch(r -> r.equals("高风险"));
    }

    /**
     * 判断是否全部都是低风险
     *
     * @param risks
     * @return
     */
    public static boolean allLowRisks(List<String> risks) {

        return risks.stream()
                .allMatch(r -> r.equals("低风险"));
    }

}