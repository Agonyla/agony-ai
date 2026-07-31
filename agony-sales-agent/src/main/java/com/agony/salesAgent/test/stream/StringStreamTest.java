package com.agony.salesAgent.test.stream;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author: Agony
 * @create: 2026/7/29 13:47
 * @describe:
 */
public class StringStreamTest {

    public static void main(String[] args) {
        List<String> names = Arrays.asList("Tom", "Jack", "Lucy", "Tony", "Jerry", "Bob");

        // 1. 找出长度大于 3 的名字
        System.out.println("找出长度大于 3 的名字");
        StreamUtils.printList(lengthLongerThan3(names));

        // 2. 将所有名字转成大写
        System.out.println("将所有名字转成大写");
        StreamUtils.printList(toUpperCase(names));

        // 3. 找出以 J 开头的名字
        System.out.println("找出以 J 开头的名字");
        StreamUtils.printList(findStartWithJ(names));

        // 4. 按名字长度排序
        System.out.println("按名字长度排序");
        StreamUtils.printList(orderByNameLength(names));
    }

    /**
     * 找出长度大于 3 的名字
     *
     * @param nums
     * @return
     */
    public static List<String> lengthLongerThan3(List<String> nums) {

        return nums.stream()
                .filter(s -> s.length() > 3)
                .toList();
    }

    public static List<String> toUpperCase(List<String> nums) {
        return nums.stream()
                .map(String::toUpperCase)
                .toList();
    }

    public static List<String> findStartWithJ(List<String> nums) {
        return nums.stream()
                .filter(s -> s.startsWith("J"))
                .toList();
    }

    public static List<String> orderByNameLength(List<String> nums) {
        return nums.stream()
                .sorted(Comparator.comparingInt(String::length))
                .toList();
    }

}