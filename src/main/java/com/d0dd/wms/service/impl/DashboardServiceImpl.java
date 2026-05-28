package com.d0dd.wms.service.impl;

import com.d0dd.wms.dto.DashboardDataDTO;
import com.d0dd.wms.mapper.InboundMapper;
import com.d0dd.wms.mapper.InventoryBinMapper;
import com.d0dd.wms.mapper.InventorySummaryMapper;
import com.d0dd.wms.mapper.OutboundMapper;
import com.d0dd.wms.mapper.WarehouseBinMapper;
import com.d0dd.wms.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private InventorySummaryMapper inventorySummaryMapper;

    @Autowired
    private InboundMapper inboundMapper;

    @Autowired
    private OutboundMapper outboundMapper;

    @Autowired
    private WarehouseBinMapper warehouseBinMapper;

    @Autowired
    private InventoryBinMapper inventoryBinMapper;
    
    @Autowired
    private com.d0dd.wms.mapper.ProdSkuMapper prodSkuMapper;
    
    @Autowired
    private com.d0dd.wms.mapper.InboundReceivingTaskMapper inboundReceivingTaskMapper;
    
    @Autowired
    private com.d0dd.wms.mapper.OutboundPickingTaskMapper outboundPickingTaskMapper;

    @Override
    public DashboardDataDTO getDashboardData() {
        DashboardDataDTO data = new DashboardDataDTO();

        // Total Inventory Quantity
        BigDecimal totalQty = inventorySummaryMapper.sumTotalQty();
        data.setTotalInventoryQty(totalQty != null ? totalQty : BigDecimal.ZERO);
        
        // Product Quantity (SKU Count)
        int productCount = prodSkuMapper.count();
        data.setProductCount(productCount);

        // Pending Inbound/Outbound Counts (Orders)
        // Assuming '1' is pending/created status, adjust based on SysDict if needed
        List<String> pendingStatuses = Arrays.asList("1", "2"); 
        
        int pendingInbound = inboundMapper.countByStatus(pendingStatuses);
        data.setPendingInboundCount(pendingInbound);

        int pendingOutbound = outboundMapper.countByStatus(pendingStatuses);
        data.setPendingOutboundCount(pendingOutbound);
        
        // Pending Receiving Tasks (Status 0)
        int pendingReceivingTasks = inboundReceivingTaskMapper.countByStatus(0);
        data.setPendingReceivingTaskCount(pendingReceivingTasks);
        
        // Pending Picking Tasks (Status 0)
        int pendingPickingTasks = outboundPickingTaskMapper.countByStatus(0);
        data.setPendingPickingTaskCount(pendingPickingTasks);

        // Bin Statistics
        int totalBins = warehouseBinMapper.countAll();
        data.setTotalBins(totalBins);

        int occupiedBins = inventoryBinMapper.countOccupiedBins();
        data.setOccupiedBins(occupiedBins);

        return data;
    }
}
