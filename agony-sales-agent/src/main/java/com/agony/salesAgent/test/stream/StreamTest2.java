package com.agony.salesAgent.test.stream;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: Agony
 * @create: 2026/8/3 13:43
 * @describe:
 */
public class StreamTest2 {

    public static void main(String[] args) {

        List<Order> orders = Arrays.asList(
                new Order(1, "U001", "张三", "上海", "数码", new BigDecimal("3999"), "PAID",
                        LocalDate.of(2026, 7, 1), List.of("手机", "新品")),
                new Order(2, "U002", "李四", "北京", "家电", new BigDecimal("2999"), "PAID",
                        LocalDate.of(2026, 7, 2), List.of("空调", "促销")),
                new Order(3, "U001", "张三", "上海", "图书", new BigDecimal("99"), "CANCELED",
                        LocalDate.of(2026, 7, 3), List.of("Java", "技术")),
                new Order(4, "U003", "王五", "广州", "数码", new BigDecimal("5999"), "PAID",
                        LocalDate.of(2026, 7, 4), List.of("电脑", "高端")),
                new Order(5, "U004", "赵六", "北京", "食品", new BigDecimal("199"), "PAID",
                        LocalDate.of(2026, 7, 5), List.of("零食", "促销")),
                new Order(6, "U002", "李四", "北京", "数码", new BigDecimal("1999"), "REFUND",
                        LocalDate.of(2026, 7, 6), List.of("耳机", "数码")),
                new Order(7, "U005", "钱七", "上海", "家电", new BigDecimal("4599"), "PAID",
                        LocalDate.of(2026, 7, 7), List.of("冰箱", "家电")),
                new Order(8, "U006", "孙八", "广州", "图书", new BigDecimal("129"), "PAID",
                        LocalDate.of(2026, 7, 8), List.of("Java", "编程")),
                new Order(9, "U003", "王五", "广州", "食品", new BigDecimal("399"), "PAID",
                        LocalDate.of(2026, 7, 9), List.of("进口", "食品")),
                new Order(10, "U001", "张三", "上海", "数码", new BigDecimal("899"), "PAID",
                        LocalDate.of(2026, 7, 10), List.of("耳机", "促销")),
                new Order(11, "U004", "赵六", "北京", "图书", new BigDecimal("89"), "PAID",
                        LocalDate.of(2026, 7, 11), List.of("小说", "图书")),
                new Order(12, "U007", "周九", "深圳", "数码", new BigDecimal("6999"), "PAID",
                        LocalDate.of(2026, 7, 12), List.of("相机", "高端"))
        );

        // 1. 按城市统计已支付订单总金额，并按金额降序排序
        System.out.println(t1(orders));
        // 2. 找出每个用户金额最高的一笔已支付订单
        System.out.println(t2(orders));

        // 3. 按商品分类统计订单数量、总金额、平均金额
        // 4. 把订单列表转成 orderId -> Order 的 Map，并保持原顺序
        // 5. 把订单列表转成 userId -> 用户累计消费金额
        // 6. 按城市分组，收集每个城市出现过的所有标签，要求去重
        // 7. 按订单状态分组，每组只保留订单编号列表
        // 8. 找出每个城市消费金额最高的前 2 笔已支付订单
    }

    public record Order(
            Integer id,
            String userId,
            String userName,
            String city,
            String category,
            BigDecimal amount,
            String status,
            LocalDate orderDate,
            List<String> tags
    ) {
    }

    // 1. 按城市统计已支付订单总金额，并按金额降序排序
    public static Map<String, BigDecimal> t1(List<Order> orders) {

        return orders.stream()
                .filter(order -> "PAID".equals(order.status))
                .collect(Collectors.groupingBy(
                        Order::city,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Order::amount,
                                BigDecimal::add
                        )
                ))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }

    // 2. 找出每个用户金额最高的一笔已支付订单
    public static Map<String, Order> t2(List<Order> orders) {

        return orders.stream()
                .filter(order -> "PAID".equals(order.status))
                .collect(Collectors.toMap(
                                Order::userId,
                                Function.identity(),
                                BinaryOperator.maxBy(Comparator.comparing(Order::amount))
                        )
                );

        // return orders.stream()
        //         .filter(order -> "PAID".equals(order.status))
        //         .collect(Collectors.groupingBy(
        //                 Order::userId,
        //                 Collectors.collectingAndThen(
        //                         Collectors.maxBy(Comparator.comparing(Order::amount)),
        //                         Optional::get
        //                 )
        //         ));
    }

    // 3. 按商品分类统计订单数量、总金额、平均金额
    record CategoryStat(
            long count,
            BigDecimal totalAmount,
            BigDecimal avgAmount) {
    }

    public static Map<String, CategoryStat> t3(List<Order> orders) {

        return orders.stream()
                .collect(Collectors.groupingBy(
                        Order::category,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    int count = list.size();

                                    BigDecimal totalAmount = list.stream()
                                            .map(Order::amount)
                                            .reduce(
                                                    BigDecimal.ZERO,
                                                    BigDecimal::add
                                            );

                                    BigDecimal avgAmount = totalAmount.divide(BigDecimal.valueOf(count), RoundingMode.HALF_UP);
                                    return new CategoryStat(count, totalAmount, avgAmount);
                                }
                        )

                ));
    }

    // 4. 把订单列表转成 orderId -> Order 的 Map，并保持原顺序
    public static Map<Integer, Order> t4(List<Order> orders) {

        return orders.stream()
                .collect(Collectors.toMap(
                        Order::id,
                        Function.identity(),
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }

    // 5. 把订单列表转成 userId -> 用户累计消费金额
    public static Map<String, BigDecimal> t5(List<Order> orders) {

        // return orders.stream()
        //         .filter(order -> "PAID".equals(order.status()))
        //         .collect(Collectors.groupingBy(
        //                 Order::userId,
        //                 Collectors.collectingAndThen(
        //                         Collectors.toList(),
        //                         list -> list.stream()
        //                                 .map(Order::amount)
        //                                 .reduce(BigDecimal.ZERO,
        //                                         BigDecimal::add)
        //                 )
        //         ));

        return orders.stream()
                .filter(o -> "PAID".equals(o.status()))
                .collect(Collectors.toMap(
                        Order::userId,
                        Order::amount,
                        BigDecimal::add
                ));
    }

    // 6. 按城市分组，收集每个城市出现过的所有标签，要求去重
    public static Map<String, Set<String>> t6(List<Order> orders) {

        return orders.stream()
                .collect(Collectors.groupingBy(
                        Order::city,
                        Collectors.flatMapping(
                                order -> order.tags().stream(),
                                Collectors.toSet()
                        )
                ));
    }

    // 7. 按订单状态分组，每组只保留订单编号列表
    public static Map<String, List<Integer>> t7(List<Order> orders) {

        return orders.stream()
                .collect(Collectors.groupingBy(
                        Order::status,
                        Collectors.mapping(
                                Order::id,
                                Collectors.toList()
                        )
                ));
    }

    // 8. 找出每个城市消费金额最高的前 2 笔已支付订单
    public static Map<String, List<Order>> t8(List<Order> orders) {

        return orders.stream()
                .filter(o -> "PAID".equals(o.status))
                .collect(Collectors.groupingBy(
                        Order::city,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparing(Order::amount).reversed())
                                        .limit(2)
                                        .toList()
                        )
                ));
    }

}