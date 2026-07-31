package com.agony.salesAgent.test.stream;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.agony.salesAgent.test.stream.StreamUtils.printList;

/**
 * @author: Agony
 * @create: 2026/7/29 10:37
 * @describe:
 */
public class BaseStreamTest {

    public static void main(String[] args) {
        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // 1. 筛选出所有偶数
        System.out.println("筛选出所有偶数");
        printList(evenNums(nums));

        // 2. 将每个数字平方
        System.out.println("将每个数字平方");
        printList(powNums(nums));

        // 3. 求所有数字的和
        System.out.println("求所有数字的和");
        System.out.println(sumNums(nums));

        // 4. 找出大于 5 的数字数量
        System.out.println("找出大于 5 的数字数量");
        printList(biggerThan5(nums));

        // 5. 找出最大值和最小值
        System.out.println("找出最大值和最小值");
        System.out.println(Arrays.toString(smallestAndBiggest(nums)));

    }

    /**
     * 筛选出所有偶数
     *
     * @param nums
     * @return
     */
    public static List<Integer> evenNums(List<Integer> nums) {

        return nums.stream()
                .filter(n -> n % 2 == 0)
                .toList();
    }

    /**
     * 将每个数字平方
     *
     * @param nums
     * @return
     */
    public static List<Integer> powNums(List<Integer> nums) {

        return nums.stream()
                .map(n -> n * n)
                .toList();
    }

    /**
     * 求所有数字的和
     *
     * @param nums
     * @return
     */
    public static int sumNums(List<Integer> nums) {

        // return nums.stream()
        //         .reduce(0, Integer::sum);

        return nums.stream()
                .mapToInt(n -> n)
                .sum();
    }

    /**
     * 找出大于 5 的数字数量
     *
     * @param nums
     * @return
     */
    public static List<Integer> biggerThan5(List<Integer> nums) {

        return nums.stream()
                .filter(n -> n > 5)
                .toList();
    }

    public static int[] smallestAndBiggest(List<Integer> nums) {

        int smallest = nums.stream()
                .min(Comparator.comparingInt(a -> a))
                .orElse(Integer.MIN_VALUE);

        int biggest = nums.stream()
                .max(Comparator.comparingInt(a -> a))
                .orElse(Integer.MAX_VALUE);

        return new int[]{smallest, biggest};
    }
}