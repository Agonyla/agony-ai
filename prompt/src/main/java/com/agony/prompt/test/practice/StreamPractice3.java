package com.agony.prompt.test.practice;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: Agony
 * @create: 2026/5/22 13:23
 * @describe:
 */
public class StreamPractice3 {

    record Student(String name, int age, int score) {
    }

    public static void main(String[] args) {

        List<Student> students = List.of(
                new Student("张三", 18, 90),
                new Student("李四", 19, 75),
                new Student("王五", 18, 88),
                new Student("赵六", 20, 60)
        );

        System.out.println("找出成绩大于 80 的学生");
        findScoreBiggerThen80(students).forEach(System.out::println);

        System.out.println("\n只取所有学生姓名");
        getName(students).forEach(System.out::println);

        System.out.println("\n按年龄分组");
        groupByAge(students).entrySet().forEach(System.out::println);

        System.out.println("\n计算平均成绩");
        System.out.println(average(students));

        System.out.println("\n找出成绩最高的学生");
        System.out.println(findHighestScore(students));

    }

    /**
     * 找出成绩大于 80 的学生
     *
     * @param students
     * @return
     */
    public static List<Student> findScoreBiggerThen80(List<Student> students) {

        return students.stream()
                .filter(student -> student.score > 80)
                .toList();
    }

    /**
     * 只取所有学生姓名
     *
     * @param students
     * @return
     */
    public static List<String> getName(List<Student> students) {

        return students.stream()
                .map(student -> student.name)
                .toList();
    }

    /**
     * 按年龄分组
     *
     * @param students
     * @return
     */
    public static Map<Integer, List<String>> groupByAge(List<Student> students) {

        // return students.stream()
        //         .collect(Collectors.groupingBy(s -> s.age))
        //         .entrySet().stream()
        //         .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue()
        //                 .stream()
        //                 .map(Student::name)
        //                 .toList()));

        return students.stream()
                .collect(Collectors.groupingBy(Student::age, Collectors.mapping(s -> s.name, Collectors.toList())));
    }

    /**
     * 计算平均成绩
     *
     * @param students
     * @return
     */
    public static double average(List<Student> students) {

        return students.stream()
                .mapToDouble(Student::score)
                .average()
                .orElse(0.0);
    }

    /**
     * 找出成绩最高的学生
     *
     * @param students
     * @return
     */
    public static Student findHighestScore(List<Student> students) {

        return students.stream()
                .max(Comparator.comparingInt(s -> s.score))
                .orElse(null);
    }

}