package com.agony.agent.test;

import java.util.List;

/**
 * @author: Agony
 * @create: 2026/6/5 13:37
 * @describe:
 */
public class Test {

    public static void main(String[] args) {

        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);

        List<Integer> subList = list.subList(0, 6);

        subList.forEach(System.out::println);

    }
}