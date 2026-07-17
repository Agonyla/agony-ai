package com.agony.salesAgent.test;

import java.time.LocalDate;
import java.util.List;

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

        List<User> users = List.of(new User("jack", 29),
                new User("marry", 20),
                new User("ted", 47));

        int totalAge = users.stream()
                .mapToInt(User::age)
                .sum();

        Integer sumAge = users.stream()
                .map(User::age)
                .reduce(0, (a, b) -> a + b);

        System.out.println(totalAge);
        System.out.println(sumAge);
    }

    public record User(
            String name,
            int age) {
    }

}