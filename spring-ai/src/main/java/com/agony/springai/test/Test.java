package com.agony.springai.test;

/**
 * @author: Agony
 * @create: 2026/4/22 16:10
 * @describe:
 */
public class Test {

    public static void main(String[] args) {

        String test1 = """
                hello1""";

        String test2 = "hello2";

        System.out.println(test2);
        System.out.println("-------------------");
        System.out.println(test1);
        System.out.println("-------------------");

        String name = "agony";
        String formatName = String.format("""
                hi, my name is %s""", name);

        System.out.println(formatName);
    }

}