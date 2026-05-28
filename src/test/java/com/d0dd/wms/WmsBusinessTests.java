package com.d0dd.wms;

import com.d0dd.wms.entity.*;
import com.d0dd.wms.service.*;
import com.d0dd.wms.mapper.InboundReceivingTaskMapper;
import com.d0dd.wms.mapper.OutboundPickingTaskMapper;
import com.d0dd.wms.mapper.InventoryBinMapper;
import com.d0dd.wms.mapper.InventorySummaryMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WmsBusinessTests {

    @Autowired
    private ProdSkuService prodSkuService;
    
    @Autowired
    private WarehouseBinService warehouseBinService;
    
    @Autowired
    private InboundService inboundService;
    
    @Autowired
    private InboundDetailsService inboundDetailsService;
    
    @Autowired
    private InboundReceivingTaskService inboundReceivingTaskService;
    
    @Autowired
    private OutboundService outboundService;
    
    @Autowired
    private OutboundDetailsService outboundDetailsService;
    
    @Autowired
    private OutboundPickingTaskService outboundPickingTaskService;
    
    @Autowired
    private InventoryBinService inventoryBinService;

    // Static variables to share IDs between tests in this class
    private static Long skuId;
    private static Long binId;
    private static Long inboundId;
    private static Long inboundDetailId;
    private static Long taskId;
    private static Long inventoryBinId;
    private static Long outboundId;
    private static Long outboundDetailId;
    private static Long pickingTaskId;

    @Test
    @Order(1)
    void testPrepareData() {
        // 1. Create Product
        ProdSku sku = new ProdSku();
        sku.setSkuCode("TEST-SKU-" + System.currentTimeMillis());
        sku.setProdNameEng("Test Product");
        sku.setProdNameChn("测试产品");
        sku.setIsEnabled(1);
        prodSkuService.save(sku);
        skuId = sku.getId();
        System.out.println("Created SKU ID: " + skuId);

        // 2. Create Bin
        WarehouseBin bin = new WarehouseBin();
        bin.setBinName("TEST-BIN-" + System.currentTimeMillis());
        bin.setWarehouseId(1L); // Assume warehouse 1 exists
        bin.setRackType("0");
        bin.setBinType("0");
        bin.setIsEnabled(1);
        warehouseBinService.save(bin);
        binId = bin.getId();
        System.out.println("Created Bin ID: " + binId);
    }

    @Test
    @Order(2)
    void testInboundProcess() {
        // 1. Create Inbound Order
        Inbound inbound = new Inbound();
        inbound.setWarehouseId(1L);
        inbound.setSupplierId(1L);
        inbound.setInboundStatus("0"); // Created
        inbound.setInboundType("1"); // Purchase
        inboundService.save(inbound);
        inboundId = inbound.getId();
        
        // 2. Add Detail
        InboundDetails detail = new InboundDetails();
        detail.setInboundId(inboundId);
        detail.setSkuId(skuId);
        detail.setBatchNo("BATCH-001");
        detail.setToReceivedQty(new BigDecimal("100"));
        detail.setProductStatus("Good");
        inboundDetailsService.save(detail);
        inboundDetailId = detail.getId();
        
        // 3. Generate Tasks
        inboundReceivingTaskService.generateTasks(inboundId);
        
        // 4. Verify Tasks
        List<InboundReceivingTask> tasks = inboundReceivingTaskService.getByInboundDetailsId(inboundDetailId);
        assert tasks != null && !tasks.isEmpty();
        taskId = tasks.get(0).getId();
        
        // 5. Complete Task
        inboundReceivingTaskService.completeTask(taskId, binId);
        
        // 6. Verify Task Status
        InboundReceivingTask updatedTask = inboundReceivingTaskService.getById(taskId);
        assert updatedTask.getTaskStatus() == 1;
        
        WarehouseBin wb = warehouseBinService.getById(binId);
        assert wb != null;
        InventoryBin invQuery = new InventoryBin();
        invQuery.setBinId(wb.getBinName());
        List<InventoryBin> invBins = inventoryBinService.list(invQuery);
        assert invBins != null && !invBins.isEmpty();
        inventoryBinId = invBins.get(0).getId();
    }
    
    @Test
    @Order(3)
    void testOutboundProcess() {
        // 1. Create Outbound Order
        Outbound outbound = new Outbound();
        outbound.setWarehouseId(1L);
        outbound.setOutboundStatus("0"); // Created
        outbound.setOutboundType("1"); // Sales
        outboundService.save(outbound);
        outboundId = outbound.getId();
        
        // 2. Add Detail
        OutboundDetails detail = new OutboundDetails();
        detail.setOutboundId(outboundId);
        detail.setSkuId(skuId);
        detail.setOutQty(new BigDecimal("10"));
        outboundDetailsService.save(detail);
        outboundDetailId = detail.getId();
        
        // 3. Generate Tasks
        outboundPickingTaskService.generateTasks(outboundId);
        
        // 4. Verify Tasks
        List<OutboundPickingTask> tasks = outboundPickingTaskService.getByOutboundDetailsId(outboundDetailId);
        assert tasks != null && !tasks.isEmpty();
        pickingTaskId = tasks.get(0).getId();
        
        // 5. Complete Task
        // Note: We need to pick from the bin where we put items in Inbound Process
        outboundPickingTaskService.completeTask(pickingTaskId, inventoryBinId);
        
        // 6. Verify Task Status
        OutboundPickingTask updatedTask = outboundPickingTaskService.getById(pickingTaskId);
        assert updatedTask.getTaskStatus() == 1;
    }
}
