package com.agony.langchain4jai.tools;

import com.agony.langchain4jai.model.DateRange;
import com.agony.langchain4jai.model.OrderStatus;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: Agony
 * @create: 2026/5/28 11:19
 * @describe:
 */
@Component
public class OrderQueryTools {

    // 基础类型
    @Tool("根据用户ID查询用户信息，返回用户名和注册时间")
    public String getUserById(@P("用户ID，正整数") long userId) {
        // Mock 数据
        return String.format("{\"id\":%d,\"username\":\"user%d\",\"createdAt\":\"2024-01-01\"}", userId, userId);
    }

    // String
    @Tool("根据用户名关键词模糊搜索用户，返回匹配的用户名列表")
    public List<String> searchUsers(@P("用户名关键词，至少2个字符") String keyword) {
        return List.of("user_" + keyword + "_01", "user_" + keyword + "_02");
    }

    // 枚举
    @Tool("查询指定状态的订单列表，返回订单ID和金额")
    public String getOrdersByStatus(
            @P("订单状态：PENDING待付款/PAID已付款/SHIPPED已发货/DELIVERED已完成/CANCELLED已取消")
            OrderStatus status) {
        return String.format("[{\"orderId\":\"ORD001\",\"status\":\"%s\",\"amount\":299.0}]", status);
    }

    // 复杂对象（模型会自动构造）
    @Tool("查询指定日期范围内的订单统计，返回订单数量和总金额")
    public String getOrderStats(
            @P("日期范围，包含 startDate 和 endDate，格式 YYYY-MM-DD")
            DateRange dateRange) {
        return String.format("{\"startDate\":\"%s\",\"endDate\":\"%s\",\"count\":42,\"totalAmount\":12800.0}",
                dateRange.startDate(), dateRange.endDate());
    }
}