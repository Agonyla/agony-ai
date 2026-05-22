package com.agony.prompt.test.practice;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: Agony
 * @create: 2026/5/22 14:03
 * @describe:
 */
public class StreamPractice5 {
    record ContractRisk(String clause, String level, int score) {
    }

    public static void main(String[] args) {

        List<ContractRisk> list = List.of(
                new ContractRisk("付款条款", "高风险", 90),
                new ContractRisk("保密条款", "低风险", 30),
                new ContractRisk("违约责任", "中风险", 70),
                new ContractRisk("解除条款", "高风险", 85)
        );

        System.out.println("按 level 分组");
        groupByLevel(list).entrySet().forEach(System.out::println);

        System.out.println("\n统计每个风险等级数量");
        countEachLevelCount(list).entrySet().forEach(System.out::println);

        System.out.println("\n找出 score 最高的条款");
        System.out.println(findClause(list));

        System.out.println("\n筛选出高风险条款名称");
        findHighRiskClauses(list).entrySet().forEach(System.out::println);

        System.out.println("\n计算平均风险分数");
        System.out.println(averageRisk(list));
    }

    /**
     * 按 level 分组
     *
     * @param list
     * @return
     */
    public static Map<String, List<ContractRisk>> groupByLevel(List<ContractRisk> list) {

        return list.stream()
                .collect(Collectors.groupingBy(ContractRisk::level, Collectors.toList()));
    }

    /**
     * 统计每个风险等级数量
     *
     * @param list
     * @return
     */
    public static Map<String, Long> countEachLevelCount(List<ContractRisk> list) {
        return list.stream()
                .collect(Collectors.groupingBy(ContractRisk::level, Collectors.counting()));
    }

    /**
     * 找出 score 最高的条款
     *
     * @param list
     * @return
     */
    public static String findClause(List<ContractRisk> list) {

        return list.stream()
                .max(Comparator.comparingInt(ContractRisk::score))
                .map(ContractRisk::clause)
                .orElse(null);
    }

    /**
     * 筛选出高风险条款名称
     *
     * @param list
     * @return
     */
    public static Map<String, List<String>> findHighRiskClauses(List<ContractRisk> list) {

        return list.stream()
                .filter(c -> c.level().equals("高风险"))
                .collect(Collectors.groupingBy(ContractRisk::level, Collectors.mapping(ContractRisk::clause, Collectors.toList())));
    }

    /**
     * 计算平均风险分数
     *
     * @param list
     * @return
     */
    public static double averageRisk(List<ContractRisk> list) {

        return list.stream()
                .collect(Collectors.averagingDouble(ContractRisk::score));
    }
}