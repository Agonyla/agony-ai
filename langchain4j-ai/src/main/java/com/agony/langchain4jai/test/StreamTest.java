package com.agony.langchain4jai.test;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: Agony
 * @create: 2026/5/29 15:08
 * @describe:
 */
public class StreamTest {

    record Summary(
            String sessionId,
            String summary,
            LocalDateTime lastActive
    ) {
    }

    private final static List<Summary> data = List.of(
            new Summary("user1", "a", LocalDateTime.now().minusDays(1)),
            new Summary("user2", "n", LocalDateTime.now().minusDays(10)),
            new Summary("user3", "v", LocalDateTime.now())
    );

    public static void main(String[] args) {

        String summary = data.stream()
                .filter(s -> "user3".equals(s.sessionId))
                .findFirst()
                .map(Summary::summary)
                .orElse("no summary");
        System.out.println(summary);
    }
}