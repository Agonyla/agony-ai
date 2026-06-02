package com.agony.langchain4jai.tools.agent;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

/**
 * @author: Agony
 * @create: 2026/6/2 10:12
 * @describe:
 */
@Component
public class ArithmeticMathTools {

    @Tool("执行简单的数学计算，仅支持加减乘除")
    public double calculate(
            @P("第一个数字") double a,
            @P("运算符：+、-、*、/") String op,
            @P("第二个数字") double b) {
        return switch (op) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> b != 0 ? a / b : Double.NaN;
            default -> throw new IllegalArgumentException("不支持的运算符：" + op);
        };
    }
}