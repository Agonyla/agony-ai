package com.agony.langchain4jai.controller.structed;

import com.agony.langchain4jai.service.StreamingAssistant;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author: Agony
 * @create: 2026/5/28 10:45
 * @describe:
 */
@RestController
@RequestMapping("/stream")
public class StreamingWriteController {

    private final StreamingAssistant streamingAssistant;

    public StreamingWriteController(StreamingAssistant streamingAssistant) {
        this.streamingAssistant = streamingAssistant;
    }

    @GetMapping(value = "/write", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter write(@RequestParam String topic) {
        SseEmitter emitter = new SseEmitter(60_000L);

        streamingAssistant.write(topic)
                .onPartialResponse(token -> {
                    try {
                        emitter.send(token);
                    } catch (Exception e) {
                        emitter.completeWithError(e);
                    }
                })
                .onCompleteResponse(response -> emitter.complete())
                .onError(emitter::completeWithError)
                .start();

        return emitter;
    }
}