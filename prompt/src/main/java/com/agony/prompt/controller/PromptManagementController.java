package com.agony.prompt.controller;

import com.agony.prompt.entity.PromptVersionInfo;
import com.agony.prompt.service.PromptVersionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Agony
 * @create: 2026/5/26 18:21
 * @describe:
 */
@RestController
@RequestMapping("/admin/prompts")
public class PromptManagementController {

    private final PromptVersionService versionService;

    public PromptManagementController(PromptVersionService versionService) {
        this.versionService = versionService;
    }

    @GetMapping("/{key}/history")
    public List<PromptVersionInfo> getHistory(
            @PathVariable String key,
            @RequestParam(defaultValue = "production") String env) {
        return versionService.getVersionHistory(key, env);
    }

    @PostMapping("/{key}/publish")
    public void publish(@PathVariable String key, @RequestBody PublishRequest req) {
        versionService.publishVersion(
                key, req.version(), req.content(), req.description(), req.environment());
    }

    @PostMapping("/{key}/rollback")
    public void rollback(@PathVariable String key, @RequestBody RollbackRequest req) {
        versionService.rollbackTo(key, req.targetVersion(), req.environment());
    }

    record PublishRequest(String version, String content, String description, String environment) {
    }

    record RollbackRequest(String targetVersion, String environment) {
    }
}