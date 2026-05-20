package com.agony.springaialibaba.controller.rag;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: Agony
 * @create: 2026/5/20 14:43
 * @describe:
 */
@RestController
@RequestMapping("/api/rag-demo")
public class SimpleRagController {

    private final VectorStore vectorStore;
    private final ChatClient chatClient;

    public SimpleRagController(DashScopeEmbeddingModel embeddingModel, DashScopeChatModel chatModel) {

        this.vectorStore = SimpleVectorStore.builder(embeddingModel).build();

        this.vectorStore.add(List.of(
                new Document("公司退货政策：购买后 7 天内可无理由退货，商品需保持原包装。"),
                new Document("会员积分规则：每消费 1 元积累 1 积分，积分可用于兑换优惠券。"),
                new Document("配送说明：满 99 元免运费，普通快递 1-3 个工作日到达。")
        ));

        this.chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(QuestionAnswerAdvisor.builder(this.vectorStore).build())
                .build();
    }

    @GetMapping
    public String ask(@RequestParam String question) {

        return chatClient.prompt()
                .user(question)
                .call()
                .content();
    }

}