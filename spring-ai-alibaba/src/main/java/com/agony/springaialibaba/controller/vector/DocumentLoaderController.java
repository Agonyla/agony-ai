package com.agony.springaialibaba.controller.vector;

import com.agony.springaialibaba.service.DocumentLoaderService;
import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author: Agony
 * @create: 2026/5/20 16:14
 * @describe:
 */
@RestController
@RequestMapping("/api/loader")
public class DocumentLoaderController {

    private final DocumentLoaderService loaderService;

    public DocumentLoaderController(DocumentLoaderService loaderService) {
        this.loaderService = loaderService;
    }

    @GetMapping("/pdf")
    public Map<String, Object> loadPdf(@RequestParam String filename) {

        List<Document> documents = loaderService.loadPdfByPage(filename);
        return Map.of(
                "count", documents.size(),
                "first", documents.isEmpty() ? "" : documents.getFirst().getText().substring(0, Math.min(200, documents.getFirst().getText().length()))
        );
    }

    @GetMapping("/text")
    public Map<String, Object> loadText(@RequestParam String textName) {

        List<Document> texts = loaderService.loadText(textName);
        return Map.of(
                "count", texts.size(),
                "first", texts.isEmpty() ? "" : texts.getFirst().getText()
        );
    }

}