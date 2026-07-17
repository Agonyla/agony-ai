package com.agony.salesAgent.controller;

import com.agony.salesAgent.tools.SalesQueryTool;
import com.agony.salesAgent.tools.SalesSummaryTool;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/7/17 11:26
 * @describe:
 */
@RestController
@RequestMapping("/test/tool")
@RequiredArgsConstructor
public class ToolTestController {

    private final SalesQueryTool salesQueryTool;
    private final SalesSummaryTool salesSummaryTool;

    // -------- 工具一 --------
    record QueryRequest(
            String startDate, String endDate,
            String regionName, String repName, int limit
    ) {
    }

    @PostMapping("/query-orders")
    public String queryOrders(@RequestBody QueryRequest queryRequest) {
        return salesQueryTool.queryOrders(
                queryRequest.startDate(),
                queryRequest.endDate(),
                queryRequest.regionName(),
                queryRequest.repName(),
                queryRequest.limit()
        );
    }

    // -------- 工具二 --------
    record RepRankRequest(String startDate, String endDate, String regionName, int topN) {
    }

    record RegionRankRequest(String startDate, String endDate) {
    }

    record ProductRankRequest(String startDate, String endDate, int topN) {
    }

    @PostMapping("/top-reps")
    public String topReps(@RequestBody RepRankRequest req) {
        return salesSummaryTool.getTopReps(
                req.startDate(), req.endDate(), req.regionName(), req.topN());
    }

    @PostMapping("/region-ranking")
    public String regionRanking(@RequestBody RegionRankRequest req) {
        return salesSummaryTool.getRegionRanking(req.startDate(), req.endDate());
    }

    @PostMapping("/top-products")
    public String topProducts(@RequestBody ProductRankRequest req) {
        return salesSummaryTool.getTopProducts(req.startDate(), req.endDate(), req.topN());
    }
}