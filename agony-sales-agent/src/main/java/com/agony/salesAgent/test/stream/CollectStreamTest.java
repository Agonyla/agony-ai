package com.agony.salesAgent.test.stream;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: Agony
 * @create: 2026/7/30 15:47
 * @describe:
 */
public class CollectStreamTest {

    record Student(String name, int age, int score, String city, String gender) {
    }

    public static void main(String[] args) {

        List<Student> students = Arrays.asList(
                new Student("张三", 18, 90, "上海", "男"),
                new Student("李四", 19, 75, "北京", "男"),
                new Student("王五", 20, 88, "上海", "男"),
                new Student("赵六", 18, 60, "北京", "女"),
                new Student("钱七", 21, 95, "上海", "女"),
                new Student("孙八", 20, 70, "广州", "男"),
                new Student("周九", 19, 82, "广州", "女"),
                new Student("吴十", 21, 58, "北京", "男")
        );

        // 1. 收集所有学生姓名为 List
        // 2. 收集所有城市为 Set
        // 3. 统计学生总人数
        // 4. 按城市分组
        // 5. 按年龄分组
        // 6. 统计每个城市的人数
        System.out.println(t6(students));

        // 7. 统计每个年龄的人数
        System.out.println(t7(students));

        // 8. 按城市分组，只保留学生姓名
        System.out.println(t8(students));
        // 9. 按性别分组，只保留学生姓名
        // 10. 把所有学生姓名用逗号拼接
        // 11. 把所有学生姓名拼接，并加上中括号
        // 12. 按城市分组，并把每个城市的学生姓名拼接成字符串
        System.out.println(t12(students));
        // 13. 找出成绩最高的学生
        System.out.println(t13(students));
        // 14. 找出成绩最低的学生
        System.out.println(t14(students));
        // 15. 按城市分组，找每个城市成绩最高的学生
        System.out.println(t15(students));
        // 16. 按城市分组，找最高分学生，并去掉 Optional
        System.out.println(t16(students));
        // 17. 按城市分组，计算每个城市学生平均分
        System.out.println(t17(students));
        // 18. 按城市分组，计算每个城市总分
        System.out.println(t18(students));
        // 19. 按城市分组，统计每个城市成绩信息
        // 20. 把学生列表转成 Map，姓名作为 key，成绩作为 value
        System.out.println(t20(students));
        System.out.println(t21(students));
    }

    // 1. 收集所有学生姓名为 List
    public static List<String> t1(List<Student> students) {

        return students.stream()
                .map(Student::name)
                .collect(Collectors.toList());
    }
    // 2. 收集所有城市为 Set

    public static Set<String> t2(List<Student> students) {
        return students.stream()
                .map(Student::city)
                .collect(Collectors.toSet());
    }

    // 3. 统计学生总人数
    public static Long t3(List<Student> students) {
        return students.stream()
                .count();
    }

    // 4. 按城市分组
    public static Map<String, List<Student>> t4(List<Student> students) {

        return students.stream()
                .collect(Collectors.groupingBy(Student::city, Collectors.toList()));
    }

    // 5. 按年龄分组
    public static Map<Integer, List<Student>> t5(List<Student> students) {

        return students.stream()
                .collect(Collectors.groupingBy(Student::age, Collectors.toList()));
    }

    // 6. 统计每个城市的人数
    public static Map<String, Long> t6(List<Student> students) {

        return students.stream()
                .collect(Collectors.groupingBy(Student::city, Collectors.counting()));
    }

    // 7. 统计每个年龄的人数
    public static Map<Integer, Long> t7(List<Student> students) {

        return students.stream()
                .collect(Collectors.groupingBy(Student::age, Collectors.counting()));
    }

    // 8. 按城市分组，只保留学生姓名
    public static Map<String, List<String>> t8(List<Student> students) {

        return students.stream()
                .collect(Collectors.groupingBy(Student::city, Collectors.mapping(Student::name, Collectors.toList())
                ));

    }
    // 9. 按性别分组，只保留学生姓名

    public static Map<String, List<String>> t9(List<Student> students) {

        return students.stream()
                .collect(Collectors.groupingBy(Student::gender,
                        Collectors.mapping(Student::name, Collectors.toList())));
    }

    // 10. 把所有学生姓名用逗号拼接
    public static String t10(List<Student> students) {

        return students.stream()
                .map(Student::name)
                .collect(Collectors.joining(","));
    }

    // 11. 把所有学生姓名拼接，并加上中括号
    public static String t11(List<Student> students) {
        return students.stream()
                .map(Student::name)
                .collect(Collectors.joining(",", "[", "]"));
    }

    // 12. 按城市分组，并把每个城市的学生姓名拼接成字符串
    public static Map<String, String> t12(List<Student> students) {

        return students.stream()
                .collect(Collectors.groupingBy(Student::city,
                        Collectors.mapping(
                                Student::name,
                                Collectors.joining("")
                        )));
    }

    // 13. 找出成绩最高的学生
    public static Student t13(List<Student> students) {

        return students.stream()
                .max(Comparator.comparingInt(Student::score))
                .orElse(null);
    }

    // 14. 找出成绩最低的学生
    public static Student t14(List<Student> students) {
        return students.stream()
                .min(Comparator.comparingInt(Student::score))
                .orElse(null);
    }

    // 15. 按城市分组，找每个城市成绩最高的学生
    public static Map<String, Optional<Student>> t15(List<Student> students) {

        return students.stream()
                .collect(Collectors.groupingBy(Student::city,
                        Collectors.maxBy(Comparator.comparingInt(Student::score))));
    }

    // 16. 按城市分组，找最高分学生，并去掉 Optional
    public static Map<String, Student> t16(List<Student> students) {

        return students.stream()
                .collect(Collectors.groupingBy(Student::city,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparingInt(Student::score)),
                                Optional::get
                        )));
    }

    // 17. 按城市分组，计算每个城市学生平均分
    public static Map<String, Double> t17(List<Student> students) {

        return students.stream()
                .collect(Collectors.groupingBy(Student::city, Collectors.averagingDouble(Student::score)));
    }

    // 18. 按城市分组，计算每个城市总分
    public static Map<String, Integer> t18(List<Student> students) {

        return students.stream()
                .collect(Collectors.groupingBy(Student::city, Collectors.summingInt(Student::score)));
    }

    // 19. 按城市分组，统计每个城市成绩信息

    // 20. 把学生列表转成 Map，姓名作为 key，成绩作为 value
    public static Map<String, Integer> t20(List<Student> students) {
        return students.stream()
                .collect(Collectors.toMap(
                        Student::name,
                        Student::score
                ));
    }

    public static Map<Boolean, Long> t21(List<Student> students) {

        return students.stream()
                .collect(Collectors.groupingBy(
                        s -> s.score() >= 60,
                        Collectors.counting()
                ));
    }
}