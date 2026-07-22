package com.agony.salesAgent.service;

import com.agony.salesAgent.dto.MonthlyTrendDTO;
import com.agony.salesAgent.dto.ProductSalesDTO;
import com.agony.salesAgent.dto.RegionSalesDTO;
import com.agony.salesAgent.dto.RepSalesDTO;
import com.agony.salesAgent.entity.Product;
import com.agony.salesAgent.entity.SalesOrder;
import com.agony.salesAgent.entity.SalesRegion;
import com.agony.salesAgent.entity.SalesRep;
import com.agony.salesAgent.repository.ProductRepository;
import com.agony.salesAgent.repository.SalesOrderRepository;
import com.agony.salesAgent.repository.SalesRegionRepository;
import com.agony.salesAgent.repository.SalesRepRepository;
import com.agony.salesAgent.security.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: Agony
 * @create: 2026/6/22 17:09
 * @describe:
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SalesQueryService {

    private final ProductRepository productRepository;
    private final SalesOrderRepository orderRepository;
    private final SalesRegionRepository regionRepository;
    private final SalesRepRepository repRepository;

    // ============================================================
    // 基础查询
    // ============================================================

    /**
     * 查询指定时段的订单
     *
     * @param repId     销售员Id
     * @param regionId  地区Id
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 订单列表
     */
    public List<SalesOrder> queryOrders(Long repId, Long regionId,
                                        LocalDate startDate, LocalDate endDate) {

        // SALES_REP      销售员：只能查自己（repId 过滤）
        // SALES_MANAGER  销售主管：只能查本大区（regionId 过滤）
        // SALES_DIRECTOR 总监：查全公司（不过滤）

        UserContext.UserInfo currentUser = UserContext.get();

        if (currentUser != null) {

            if ("SALES_REP".equals(currentUser.role())) {
                // 普通销售员只能查自己的订单
                repId = currentUser.repId();
            } else if ("SALES_MANAGER".equals(currentUser.role())) {

                // 主管只能查本大区（若传入的 regionId 不是自己管辖的大区，强制覆盖）
                if (regionId == null || !regionId.equals(currentUser.regionId())) {
                    regionId = currentUser.regionId();
                }
            }

            // SALES_DIRECTOR：不限制，查询范围由传入参数决定
        }

        return doQueryOrders(repId, regionId, startDate, endDate);
    }

    public List<SalesOrder> doQueryOrders(Long repId, Long regionId,
                                          LocalDate startDate, LocalDate endDate) {

        if (repId != null) {
            return orderRepository.findByRepIdAndOrderDateBetween(repId, startDate, endDate);
        }

        if (regionId != null) {
            return orderRepository.findByRegionIdAndOrderDateBetween(regionId, startDate, endDate);
        }

        return orderRepository.findAll().stream()
                .filter(o -> !o.getOrderDate().isBefore(startDate)
                        && !o.getOrderDate().isAfter(endDate))
                .toList();
    }

    /**
     * 查询总销售额
     *
     * @param regionId  区域Id
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 销售总额
     */
    public BigDecimal queryTotalAmount(Long regionId, LocalDate startDate, LocalDate endDate) {

        if (regionId != null) {
            return orderRepository.sumAmountByRegion(regionId, startDate, endDate);
        }

        return orderRepository.findAll().stream()
                .filter(o -> "COMPLETED".equals(o.getStatus()))
                .filter(o -> !o.getOrderDate().isBefore(startDate)
                        && !o.getOrderDate().isAfter(endDate))
                .map(SalesOrder::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ============================================================
    // 排名查询
    // ============================================================

    /**
     * 销售员业绩排名（带姓名、大区信息）
     *
     * @param start 开始时间
     * @param end   结束时间
     * @param topN  排名
     * @return 前 N 个销售员信息
     */
    public List<RepSalesDTO> queryRepRanking(LocalDate start, LocalDate end, int topN) {

        List<Object[]> raw = orderRepository.findRepRanking(start, end);

        Map<Long, SalesRep> repMap = repRepository.findAll().stream()
                .collect(Collectors.toMap(SalesRep::getId, rep -> rep));

        Map<Long, String> regionMap = regionRepository.findAll().stream()
                .collect(Collectors.toMap(SalesRegion::getId, SalesRegion::getName));

        ArrayList<RepSalesDTO> result = new ArrayList<>();

        for (Object[] obj : raw) {

            Long repId = ((Number) obj[0]).longValue();
            BigDecimal total = new BigDecimal(obj[1].toString());

            SalesRep salesRep = repMap.get(repId);
            if (salesRep == null) continue;

            Long regionId = salesRep.getRegionId();
            String regionName = regionMap.getOrDefault(regionId, "未知");

            // 这里 orderCount 需要单独查，简化处理用 0
            result.add(new RepSalesDTO(repId, salesRep.getName(), regionId, regionName, total, 0));

            if (result.size() >= topN) break;
        }

        return result;
    }

    /**
     * 大区业绩排名
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 大区业绩列表
     */
    public List<RegionSalesDTO> queryRegionRanking(LocalDate start, LocalDate end) {

        List<Object[]> raw = orderRepository.findRegionRanking(start, end);

        Map<Long, String> regionMap = regionRepository.findAll().stream()
                .collect(Collectors.toMap(SalesRegion::getId, SalesRegion::getName));

        ArrayList<RegionSalesDTO> result = new ArrayList<>();
        for (Object[] obj : raw) {
            Long regionId = ((Number) obj[0]).longValue();
            BigDecimal total = new BigDecimal(obj[1].toString());

            String regionName = regionMap.getOrDefault(regionId, "未知");

            result.add(new RegionSalesDTO(regionId, regionName, total, 0, BigDecimal.ZERO));
        }

        return result;
    }

    /**
     * 产品销售排行
     *
     * @param start 开始时间
     * @param end   结束时间
     * @param topN  排名
     * @return 排名前 N ge 产品销售
     */
    public List<ProductSalesDTO> queryProductRanking(LocalDate start, LocalDate end, int topN) {

        List<Object[]> raw = orderRepository.findProductRanking(start, end);

        Map<Long, Product> productMap = productRepository.findAll().stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        ArrayList<ProductSalesDTO> result = new ArrayList<>();

        for (Object[] obj : raw) {

            Long productId = ((Number) obj[0]).longValue();
            BigDecimal total = new BigDecimal(obj[1].toString());
            Integer qty = ((Number) obj[2]).intValue();

            Product product = productMap.get(productId);
            if (product == null) continue;
            String skuCode = product.getSkuCode();
            String productName = product.getName();
            String category = product.getCategory();

            result.add(new ProductSalesDTO(productId, skuCode, productName, category, total, qty));
            if (result.size() >= topN) break;
        }
        return result;
    }

    // ============================================================
    // 趋势分析
    // ============================================================

    /**
     * 月度趋势数据（近 N 个月）
     *
     * @param regionId 区域Id
     * @param months   近 N 个月
     * @return 近 N 个月的趋势数据
     */
    public List<MonthlyTrendDTO> queryMonthlyTrend(Long regionId, int months) {

        LocalDate now = LocalDate.now();
        LocalDate start = now.minusMonths(months).withDayOfMonth(1);

        List<Object[]> raw = orderRepository.findMonthlyTrend(regionId, start, now);

        return raw.stream().map(
                row -> new MonthlyTrendDTO(
                        row[0].toString(),
                        new BigDecimal(row[1].toString()),
                        ((Number) row[2]).intValue())
        ).toList();
    }

    /**
     * 计算环比增长率（当期 vs 上期）
     *
     * @param current  当期
     * @param previous 上期
     * @return 环比增长率
     */
    public BigDecimal calcGrowthRate(BigDecimal current, BigDecimal previous) {
        if (previous == null || previous.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }

        return current.subtract(previous)
                .divide(previous, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    // ============================================================
    // 异常检测辅助
    // ============================================================

    /**
     * 查询产品最后一次出单日期
     */
    public LocalDate queryLastOrderDate(Long productId) {
        return orderRepository.findLastOrderDateByProduct(productId);
    }

    /**
     * 查询大区在指定时段内的订单数
     */
    public Long queryOrderCount(Long regionId, LocalDate start, LocalDate end) {
        return orderRepository.countCompletedByRegion(regionId, start, end);
    }

    /**
     * 查询所有销售员退单率
     */
    public List<Object[]> queryRefundRates(LocalDate start, LocalDate end) {
        return orderRepository.findRefundRateByRep(start, end);
    }

    // ============================================================
    // 辅助查询（名称解析）
    // ============================================================

    public String getRepName(Long repId) {
        return repRepository.findById(repId)
                .map(SalesRep::getName)
                .orElse("未知销售员");
    }

    public String getRegionName(Long regionId) {
        return regionRepository.findById(regionId)
                .map(SalesRegion::getName)
                .orElse("未知大区");
    }

    public Long getRegionIdByName(String regionName) {
        return regionRepository.findByName(regionName)
                .map(SalesRegion::getId)
                .orElse(null);
    }

    public Long getRepIdByName(String repName) {
        return repRepository.findByName(repName)
                .map(SalesRep::getId)
                .orElse(null);
    }
}