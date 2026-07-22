package com.agony.salesAgent.security;

/**
 * @author: Agony
 * @create: 2026/7/21 13:54
 * @describe: ThreadLocal 用户上下文
 */

public class UserContext {

    private static final ThreadLocal<UserInfo> HOLDER = new ThreadLocal<>();

    public record UserInfo(
            Long userId,
            String username,
            String role,
            Long regionId,
            Long repId
    ) {
    }

    public static void set(UserInfo userInfo) {
        HOLDER.set(userInfo);
    }

    public static UserInfo get() {
        return HOLDER.get();
    }

    public static void clear() {
        HOLDER.remove();
    }

    public static boolean isDirector() {
        UserInfo u = get();
        return u != null && "SALES_DIRECTOR".equals(u.role);
    }

    public static boolean isManager() {
        UserInfo u = get();
        return u != null && "SALES_MANAGER".equals(u.role);
    }
}