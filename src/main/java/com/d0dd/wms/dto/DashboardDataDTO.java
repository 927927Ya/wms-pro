package com.d0dd.wms.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

@Data
public class DashboardDataDTO {
    private BigDecimal totalInventoryQty;
    private Integer pendingInboundCount;
    private Integer pendingOutboundCount;
    private Integer totalBins;
    private Integer occupiedBins;
    private Integer productCount;
    private Integer pendingReceivingTaskCount;
    private Integer pendingPickingTaskCount;
    private Map<String, Integer> inboundStatusCounts;
    private Map<String, Integer> outboundStatusCounts;
}
