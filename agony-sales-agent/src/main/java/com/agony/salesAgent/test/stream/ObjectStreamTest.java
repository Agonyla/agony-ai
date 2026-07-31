package com.agony.salesAgent.test.stream;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author: Agony
 * @create: 2026/7/29 14:37
 * @describe:
 */
public class ObjectStreamTest {

    public static void main(String[] args) {
        List<Student> students = Arrays.asList(
                new Student("张三", 18, 90),
                new Student("李四", 19, 75),
                new Student("王五", 20, 88),
                new Student("赵六", 18, 60),
                new Student("钱七", 21, 95)
        );

        // 1. 找出成绩大于 80 的学生
        // 2. 获取所有学生姓名
        // 3. 按成绩从高到低排序
        // 4. 求学生平均成绩
        // 5. 按年龄分组
        // 6. 找出成绩最高的学生
        // 7. 判断是否所有学生都及格
        // 8. 判断是否存在年龄大于 20 的学生
        // 9. 统计每个年龄段的人数
        // 10. 按年龄分组，并只保留学生姓名
        // 11. 将学生姓名用逗号拼接成字符串
        // 12. 找出成绩前 3 名的学生姓名
        // 13. 找出成绩大于平均分的学生
        // 14. 按成绩是否及格分组
        // 15. 获取每个年龄中成绩最高的学生
        Map<Integer, Student> theHighestScoreInEachAge = findTheHighestScoreInEachAge(students);
        System.out.println(theHighestScoreInEachAge);
        // 16. 将学生列表转成 Map，key 是姓名，value 是成绩
    }

    record Student(
            String name,
            int age,
            int score
    ) {
        @Override
        public String toString() {
            return name + "-" + age + "-" + score;
        }
    }

    // 1. 找出成绩大于 80 的学生
    public static List<Student> findScoreBiggerThan80(List<Student> students) {

        return students.stream()
                .filter(s -> s.score() > 80)
                .toList();
    }

    // 2. 获取所有学生姓名
    public static List<String> findAllNames(List<Student> students) {

        return students.stream()
                .map(Student::name)
                .toList();
    }

    // 3. 按成绩从高到低排序
    public static List<Student> orderByScoreDesc(List<Student> students) {

        return students.stream()
                .sorted((s1, s2) -> s2.score() - s1.score())
                .toList();
    }

    // 4. 求学生平均成绩
    public static double averageScore(List<Student> students) {

        return students.stream()
                .mapToDouble(Student::score)
                .average().orElse(0.0);
    }

    // 5. 按年龄分组
    public static Map<Integer, List<Student>> groupByAge(List<Student> students) {
        return students.stream()
                .collect(Collectors.groupingBy(s -> s.age));

    }

    // 6. 找出成绩最高的学生
    public static Student findHighestScore(List<Student> students) {

        return students.stream()
                .sorted((s1, s2) -> s2.score() - s1.score())
                .toList().getFirst();
    }

    // 7.判断是否所有学生都及格
    public static boolean ifAllStudentsPass(List<Student> students) {
        return students.stream()
                .allMatch(student -> student.score() >= 60);
    }

    // 8. 判断是否存在年龄大于 20 的学生
    public static boolean existAgeOlderThan20(List<Student> students) {

        return students.stream()
                .anyMatch(student -> student.age() > 20);
    }

    // 9. 统计每个年龄段的人数
    public static Map<Integer, Long> countStudentsGroupByAge(List<Student> students) {

        return students.stream()
                .collect(Collectors.groupingBy(Student::age,
                        Collectors.counting()));
    }

    // 10. 按年龄分组，并只保留学生姓名
    public static Map<Integer, List<String>> groupByAgeAndKeepName(List<Student> students) {

        return students.stream()
                .collect(Collectors.groupingBy(Student::age,
                        Collectors.mapping(Student::name, Collectors.toList())));
    }

    // 11. 将学生姓名用逗号拼接成字符串
    public static String getAllStudentsName(List<Student> students) {
        return students.stream()
                .map(Student::name)
                .collect(Collectors.joining(","));
    }

    // 12. 找出成绩前 3 名的学生姓名
    public static List<String> findTop3Students(List<Student> students) {

        return students.stream()
                .sorted((s1, s2) -> s2.score() - s1.score())
                .limit(3)
                .map(Student::name)
                .toList();

    }

    // 13. 找出成绩大于平均分的学生
    public static List<Student> findScoreHigherThanAverage(List<Student> students) {

        double average = students.stream()
                .mapToDouble(Student::score)
                .average().orElse(0.0);

        return students.stream()
                .filter(student -> student.score() > average)
                .toList();
    }

    // 14. 按成绩是否及格分组
    public static Map<Boolean, List<Student>> groupByPassScore(List<Student> students) {

        return students.stream()
                .collect(Collectors.partitioningBy(student -> student.score() >= 60));
    }

    // 15. 获取每个年龄中成绩最高的学生
    public static Map<Integer, Student> findTheHighestScoreInEachAge(List<Student> students) {

        return students.stream()
                .collect(Collectors.groupingBy(
                        Student::age,
                        Collectors.collectingAndThen(
                                Collectors.maxBy((s1, s2) -> s2.score() - s1.score()),
                                Optional::get
                        )
                ));

        //  Function.identity():  s -> s

        // return students.stream()
        //         .collect(Collectors.toMap(Student::age,
        //                         Function.identity(),
        //                         BinaryOperator.maxBy((s1, s2) -> s2.score() - s1.score())
        //                 )
        //         );

    }

    // 16. 将学生列表转成 Map，key 是姓名，value 是成绩
    public static Map<String, Integer> toMap(List<Student> students) {

        return students.stream()
                .collect(Collectors.toMap(Student::name, Student::score));
    }
}