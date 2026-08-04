package com.agony.salesAgent.test.stream;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: Agony
 * @create: 2026/8/3 16:03
 * @describe:
 */
public class StreamTest3 {

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

        // 1. 查询所有已支付订单，按金额降序排序，取前 5 条
        // 2. 按城市分组，统计每个城市已支付订单数量
        // 3. 按用户分组，收集每个用户所有已支付订单 ID
        // 4. 按城市分组，找出每个城市金额最高的订单
        System.out.println(t4(orders));
        // 5. 按城市分组，找出每个城市金额最高的订单 ID
        System.out.println(t5(orders));
        // 6. 统计每个商品分类的已支付总金额
        System.out.println(t6(orders));
        // 7. 将订单按状态分组，并把每组订单 ID 拼接成字符串
        System.out.println(t7(orders));
        // 8. 找出每个用户最近的一笔订单
        // 9. 按城市分组，收集每个城市所有订单标签，去重并排序
        // 10. 找出已支付订单总金额最高的前 3 个用户
        System.out.println(t10(orders));
    }

    // 1. 查询所有已支付订单，按金额降序排序，取前 5 条
    public static List<Order> t1(List<Order> orders) {
        return orders.stream()
                .filter(o -> "PAID".equals(o.status()))
                .sorted(Comparator.comparing(Order::amount).reversed())
                .limit(5)
                .toList();
    }

    // 2. 按城市分组，统计每个城市已支付订单数量
    public static Map<String, Long> t2(List<Order> orders) {

        return orders.stream()
                .filter(o -> "PAID".equals(o.status()))
                .collect(Collectors.groupingBy(
                        Order::city,
                        Collectors.counting()
                ));
    }

    // 3. 按用户分组，收集每个用户所有已支付订单 ID
    public static Map<String, List<Integer>> t3(List<Order> orders) {

        return orders.stream()
                .filter(o -> "PAID".equals(o.status()))
                .collect(Collectors.groupingBy(
                        Order::userId,
                        Collectors.mapping(
                                Order::id,
                                Collectors.toList()
                        )
                ));
    }

    // 4. 按城市分组，找出每个城市金额最高的订单
    public static Map<String, Order> t4(List<Order> orders) {

        // return orders.stream()
        //         .collect(Collectors.groupingBy(
        //                 Order::city,
        //                 Collectors.collectingAndThen(
        //                         Collectors.maxBy(Comparator.comparing(Order::amount)),
        //                         Optional::get
        //                 )
        //         ));

        return orders.stream()
                .collect(Collectors.toMap(
                        Order::city,
                        Function.identity(),
                        // (oldValue, newValue) -> oldValue.amount().compareTo(newValue.amount()) >= 0 ? oldValue : newValue
                        BinaryOperator.maxBy(Comparator.comparing(Order::amount))

                ));
    }

    // 5. 按城市分组，找出每个城市金额最高的订单 ID
    public static Map<String, Integer> t5(List<Order> orders) {

        return orders.stream()
                .collect(Collectors.toMap(
                        Order::city,
                        Function.identity(),
                        BinaryOperator.maxBy(Comparator.comparing(Order::amount))
                ))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().id()
                ));

        // return orders.stream()
        //         .collect(Collectors.groupingBy(
        //                 Order::city,
        //                 Collectors.collectingAndThen(
        //                         Collectors.maxBy(Comparator.comparing(Order::amount)),
        //                         optional -> optional.get().id()
        //                 )
        //         ));
    }

    // 6. 统计每个商品分类的已支付总金额
    public static Map<String, BigDecimal> t6(List<Order> orders) {

        // return orders.stream()
        //         .filter(o -> "PAID".equals(o.status))
        //         .collect(Collectors.groupingBy(
        //                 Order::category,
        //                 Collectors.collectingAndThen(
        //                         Collectors.toList(),
        //                         list -> list.stream()
        //                                 .map(Order::amount)
        //                                 .reduce(BigDecimal.ZERO,
        //                                         BigDecimal::add)
        //                 )
        //         ));

        // return orders.stream()
        //         .filter(o -> "PAID".equals(o.status))
        //         .collect(Collectors.groupingBy(
        //                 Order::category,
        //                 Collectors.reducing(
        //                         BigDecimal.ZERO,
        //                         Order::amount,
        //                         BigDecimal::add
        //                 )
        //         ));

        return orders.stream()
                .filter(o -> "PAID".equals(o.status))
                .collect(Collectors.toMap(
                        Order::category,
                        Order::amount,
                        BigDecimal::add
                ));
    }

    // 7. 将订单按状态分组，并把每组订单 ID 拼接成字符串
    public static Map<String, String> t7(List<Order> orders) {

        // return orders.stream()
        //         .collect(Collectors.groupingBy(
        //                 Order::status,
        //                 Collectors.mapping(
        //                         order -> String.valueOf(order.id()),
        //                         Collectors.joining("-")
        //                 )
        //         ));

        return orders.stream()
                .collect(Collectors.toMap(
                        Order::status,
                        o -> String.valueOf(o.id()),
                        (s1, s2) -> s1 + "-" + s2
                ));
    }

    // 8. 找出每个用户最近的一笔订单
    public static Map<String, Order> t8(List<Order> orders) {

        // return orders.stream()
        //         .collect(Collectors.groupingBy(
        //                 Order::userId,
        //                 Collectors.collectingAndThen(
        //                         Collectors.maxBy(Comparator.comparing(Order::orderDate)),
        //                         Optional::get
        //                 )
        //         ));

        return orders.stream()
                .collect(Collectors.toMap(
                        Order::userId,
                        Function.identity(),
                        (o1, o2) -> o1.orderDate().isAfter(o2.orderDate()) ? o1 : o2
                ));
    }

    // 9. 按城市分组，收集每个城市所有订单标签，去重并排序
    public static Map<String, Set<String>> t9(List<Order> orders) {

        return orders.stream()
                .collect(Collectors.groupingBy(
                        Order::city,
                        Collectors.flatMapping(
                                order -> order.tags().stream(),
                                Collectors.toCollection(TreeSet::new)
                        )
                ));
    }

    // 10. 找出已支付订单总金额最高的前 3 个用户
    public static Map<String, BigDecimal> t10(List<Order> orders) {

        return orders.stream()
                .filter(o -> "PAID".equals(o.status()))
                .collect(Collectors.groupingBy(
                        Order::userId,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Order::amount,
                                BigDecimal::add
                        )
                ))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldVal, newVal) -> oldVal,
                        LinkedHashMap::new
                ));
    }

}