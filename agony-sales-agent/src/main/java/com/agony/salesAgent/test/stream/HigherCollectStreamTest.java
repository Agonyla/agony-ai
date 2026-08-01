package com.agony.salesAgent.test.stream;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: Agony
 * @create: 2026/7/30 16:27
 * @describe:
 */
public class HigherCollectStreamTest {

    record Student(
            String name,
            int age,
            int score,
            String city,
            String gender,
            List<String> hobbies
    ) {
    }

    public static void main(String[] args) {

        List<Student> students = Arrays.asList(
                new Student("张三", 18, 90, "上海", "男", List.of("篮球", "游戏")),
                new Student("李四", 19, 75, "北京", "男", List.of("足球", "音乐")),
                new Student("王五", 20, 88, "上海", "男", List.of("篮球", "电影")),
                new Student("赵六", 18, 60, "北京", "女", List.of("阅读", "音乐")),
                new Student("钱七", 21, 95, "上海", "女", List.of("电影", "旅行")),
                new Student("孙八", 20, 70, "广州", "男", List.of("游戏", "旅行")),
                new Student("周九", 19, 82, "广州", "女", List.of("阅读", "篮球")),
                new Student("吴十", 21, 58, "北京", "男", List.of("游戏", "足球"))
        );

        // 1. 按城市分组，统计每个城市的平均分，并按平均分从高到低排序

        // 2. 按城市分组，找出每个城市成绩最高的学生姓名
        System.out.println(t2(students));
        // 3. 按城市分组，只统计及格学生人数
        System.out.println(t3(students));
        // 4. 按城市分组，收集每个城市所有学生的兴趣爱好，去重
        // 5. 按城市分组，统计每个城市的最高分、最低分、平均分、人数

        t5(students);

        // 6. 按城市分组，找出每个城市中成绩最高的学生，如果同分，保留年龄更小的
        // 7. 把学生按照城市分组，每个城市只保留成绩前三名
        // 8. 将学生列表转成 Map，城市作为 key，该城市最高分学生作为 value
        System.out.println(t8(students));
        // 9. 统计每个兴趣爱好有多少学生喜欢
    }

    // 2. ！
    public static Map<String, String> t2(List<Student> students) {

        return students.stream()
                .collect(Collectors.groupingBy(
                        Student::city,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparingInt(Student::score)),
                                op -> op.get().name()

                        )));
    }

    // 3.
    public static Map<String, Long> t3(List<Student> students) {
        return students.stream()
                .filter(s -> s.score() >= 60)
                .collect(Collectors.groupingBy(
                        Student::city,
                        Collectors.counting()
                ));
    }

    // 4. ！
    public static Map<String, Set<String>> t4(List<Student> students) {

        return students.stream()
                .collect(Collectors.groupingBy(
                        Student::city,
                        Collectors.flatMapping(
                                student -> student.hobbies.stream(), Collectors.toSet())
                ));
    }

    // 5.
    public static void t5(List<Student> students) {

        Map<String, DoubleSummaryStatistics> doubleCollect = students.stream()
                .collect(Collectors.groupingBy(Student::city,
                        Collectors.summarizingDouble(Student::score)));

        for (Map.Entry<String, DoubleSummaryStatistics> entry : doubleCollect.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue().getMax());
        }

        Map<String, IntSummaryStatistics> intCollect = students.stream()
                .collect(Collectors.groupingBy(Student::city,
                        Collectors.summarizingInt(Student::score)));

        for (Map.Entry<String, IntSummaryStatistics> entry : intCollect.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue().getMax());
        }
    }

    // 6.
    public static Map<String, Student> t6(List<Student> students) {

        // toMap: 一个key对应一个value
        Map<String, Student> collect = students.stream()
                .collect(Collectors.toMap(
                        Student::city,
                        s -> s
                ));

        // groupingBy: 一个key对应一组value
        Map<String, List<String>> collect1 = students.stream()
                .collect(Collectors.groupingBy(
                        Student::city,
                        Collectors.mapping(
                                Student::name,
                                Collectors.toList())));

        return students.stream()
                .collect(Collectors.groupingBy(
                        Student::city,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparingInt(Student::score)
                                        .thenComparing(Comparator.comparing(Student::age).reversed())
                                ),
                                Optional::get
                        )
                ));

        // return students.stream()
        //         .collect(Collectors.toMap(
        //                 Student::city,
        //                 Function.identity(),
        //                 BinaryOperator.maxBy(Comparator.comparingInt(Student::score)
        //                         .thenComparing(Comparator.comparing(Student::age).reversed()))));
    }

    // 7. ！
    public static Map<String, List<Student>> t7(List<Student> students) {

        return students.stream()
                .collect(Collectors.groupingBy(
                        Student::city,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted((s1, s2) -> s2.score() - s1.score())
                                        .limit(3)
                                        .collect(Collectors.toList())

                        )
                ));
    }

    // 8. ！
    public static Map<String, Integer> t8(List<Student> students) {

        return students.stream()
                .collect(Collectors.groupingBy(
                        Student::city,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparingInt(Student::score)),
                                op -> op.get().score()
                        )
                ));
    }
    // 9. ！

}