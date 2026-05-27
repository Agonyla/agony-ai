package com.agony.langchain4jai.controller.chat;

import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

/**
 * @author: Agony
 * @create: 2026/5/27 13:09
 * @describe:
 */
@RestController
@RequestMapping("/chat")
public class StreamingChatController {

    private final StreamingChatModel streamingChatModel;

    public StreamingChatController(StreamingChatModel streamingChatModel) {
        this.streamingChatModel = streamingChatModel;
    }

    @GetMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamChat(@RequestParam String message) {

        SseEmitter sseEmitter = new SseEmitter(60_000L);
        streamingChatModel.chat(message, new StreamingChatResponseHandler() {

            @Override
            public void onPartialResponse(String partialResponse) {
                try {
                    sseEmitter.send(partialResponse);
                } catch (IOException e) {
                    sseEmitter.completeWithError(e);
                }
            }

            @Override
            public void onCompleteResponse(ChatResponse completeResponse) {
                sseEmitter.complete();
            }

            @Override
            public void onError(Throwable error) {
                sseEmitter.completeWithError(error);
            }
        });

        return sseEmitter;
    }
}