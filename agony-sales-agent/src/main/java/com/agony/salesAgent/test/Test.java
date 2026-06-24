package com.agony.salesAgent.test;

import java.time.LocalDate;

/**
 * @author: Agony
 * @create: 2026/6/23 15:59
 * @describe:
 */
public class Test {

    public static void main(String[] args) {

        LocalDate date = LocalDate.now();
        LocalDate date2 = date;
        System.out.println(date2.isBefore(date));

    }

}