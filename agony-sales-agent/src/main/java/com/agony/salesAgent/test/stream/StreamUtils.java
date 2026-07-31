package com.agony.salesAgent.test.stream;

import java.util.List;

/**
 * @author: Agony
 * @create: 2026/7/29 13:48
 * @describe:
 */
public class StreamUtils {

    public static void printList(List<?> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("list is empty");
        }
        for (Object o : list) {
            System.out.print(o + " ");
        }
        System.out.println();
    }
}