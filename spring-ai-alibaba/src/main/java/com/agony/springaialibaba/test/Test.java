package com.agony.springaialibaba.test;

/**
 * @author: Agony
 * @create: 2026/5/20 17:12
 * @describe: Optional的一些用法
 */
public class Test {

    public static void main(String[] args) {

        User user = new User("Agony");

        // 方式1
        // String name = null;
        // if (user != null) {
        //     name = user.name();
        // } else {
        //     name = "未知用户";
        // }
        // System.out.println(name);

        // 方式2
        // String name = Optional.ofNullable(user)
        //         .map(User::name)
        //         .orElse("未知用户");
        // System.out.println(name);

    }

    record User(String name) {
    }
}