package com.agony.salesAgent.controller;

import com.agony.salesAgent.tools.SalesQueryTool;
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
public class ToolTestController {

    private final SalesQueryTool salesQueryTool;

    public ToolTestController(SalesQueryTool salesQueryTool) {
        this.salesQueryTool = salesQueryTool;
    }

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
}