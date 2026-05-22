package com.agony.prompt.test.practice;

import java.util.List;

/**
 * @author: Agony
 * @create: 2026/5/22 13:08
 * @describe:
 */
public class StreamPractice2 {
    public static void main(String[] args) {

        List<Integer> nums = List.of(3, 6, 1, 8, 2, 9, 6);

        System.out.println("筛选出所有偶数");
        findAllEvenNums(nums).forEach(System.out::println);

        System.out.println("\n每个数字乘以 2");
        multiply2(nums).forEach(System.out::println);

        System.out.println("\n求所有数字之和");
        System.out.println(sum(nums));

        System.out.println("\n求最大值");
        System.out.println(max(nums));

        System.out.println("\n去重后排序");
        sort(nums).forEach(System.out::println);
    }

    /**
     * 筛选出所有偶数
     *
     * @param nums
     * @return
     */
    public static List<Integer> findAllEvenNums(List<Integer> nums) {
        return nums.stream()
                .filter(num -> num % 2 == 0)
                .toList();
    }

    /**
     * 每个数字乘以 2
     *
     * @param nums
     * @return
     */
    public static List<Integer> multiply2(List<Integer> nums) {

        return nums.stream()
                .map(num -> num * 2)
                .toList();
    }

    /**
     * 求所有数字之和
     *
     * @param nums
     * @return
     */
    public static int sum(List<Integer> nums) {

        return nums.stream()
                .mapToInt(num -> num)
                .sum();
    }

    /**
     * 求最大值
     *
     * @param nums
     * @return
     */
    public static int max(List<Integer> nums) {

        return nums.stream()
                .mapToInt(num -> num)
                .max()
                .orElse(Integer.MIN_VALUE);
    }

    /**
     * 去重后排序
     *
     * @param nums
     * @return
     */
    public static List<Integer> sort(List<Integer> nums) {
        // return nums.stream()
        //         .distinct()
        //         .sorted()
        //         .toList();

        return nums.stream()
                .distinct()
                .sorted((a, b) -> b - a)
                .toList();
    }
}