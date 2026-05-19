package com.agony.springaialibaba.controller.parallel;

import com.agony.springaialibaba.service.MultiModelVotingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/19 10:30
 * @describe:
 */

@RestController
@RequestMapping("/api/vote")
public class MultiModelVotingController {

    private final MultiModelVotingService votingService;

    public MultiModelVotingController(MultiModelVotingService votingService) {
        this.votingService = votingService;
    }

    @GetMapping
    public MultiModelVotingService.VoteResult vote(@RequestParam String question) throws Exception {
        return votingService.vote(question);
    }
}