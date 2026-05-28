package com.d0dd.wms.service.impl;

import com.d0dd.wms.entity.*;
import com.d0dd.wms.mapper.*;
import com.d0dd.wms.service.InboundReceivingTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class InboundReceivingTaskServiceImpl implements InboundReceivingTaskService {

    private static final int PALLET_NAME_MAX_LEN = 64;

    @Autowired
    private InboundReceivingTaskMapper inboundReceivingTaskMapper;
    
    @Autowired
    private InboundDetailsMapper inboundDetailsMapper;
    
    @Autowired
    private InboundMapper inboundMapper;
    
    @Autowired
    private InventoryBatchMapper inventoryBatchMapper;
    
    @Autowired
    private InventoryBinMapper inventoryBinMapper;
    
    @Autowired
    private InventorySummaryMapper inventorySummaryMapper;
    
    @Autowired
    private InventoryHistoryMapper inventoryHistoryMapper;
    
    @Autowired
    private WarehouseBinMapper warehouseBinMapper;

    @Override
    public InboundReceivingTask getById(Long id) {
        return inboundReceivingTaskMapper.selectById(id);
    }

    @Override
    public List<InboundReceivingTask> list(InboundReceivingTask inboundReceivingTask) {
        return inboundReceivingTaskMapper.selectList(inboundReceivingTask);
    }
    
    @Override
    public List<InboundReceivingTask> getByInboundDetailsId(Long inboundDetailsId) {
        return inboundReceivingTaskMapper.selectByInboundDetailsId(inboundDetailsId);
    }

    @Override
    public int save(InboundReceivingTask inboundReceivingTask) {
        if (inboundReceivingTask.getId() == null) {
            inboundReceivingTask.setId(generateId());
        }
        return inboundReceivingTaskMapper.insert(inboundReceivingTask);
    }

    @Override
    public int update(InboundReceivingTask inboundReceivingTask) {
        return inboundReceivingTaskMapper.update(inboundReceivingTask);
    }

    @Override
    public int removeById(Long id) {
        return inboundReceivingTaskMapper.deleteById(id);
    }
    
    @Override
    public int removeByInboundDetailsId(Long inboundDetailsId) {
        return inboundReceivingTaskMapper.deleteByInboundDetailsId(inboundDetailsId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateTasks(Long inboundId) {
        Inbound inbound = inboundMapper.selectById(inboundId);
        if (inbound == null) {
            throw new RuntimeException("Inbound order not found");
        }
        if (!("0".equals(inbound.getInboundStatus()) || "4".equals(inbound.getInboundStatus()))) {
            throw new RuntimeException("Inbound order status is not allowed, cannot generate tasks");
        }

        List<InboundDetails> detailsList = inboundDetailsMapper.selectByInboundId(inboundId);
        if (detailsList == null || detailsList.isEmpty()) {
            throw new RuntimeException("Inbound order has no details");
        }

        for (InboundDetails details : detailsList) {
            int palletCount = details.getPalletCount() != null ? details.getPalletCount() : 1;
            BigDecimal qtyPerPallet = details.getToReceivedQty().divide(new BigDecimal(palletCount), 2, java.math.RoundingMode.HALF_UP);
            
            for (int i = 0; i < palletCount; i++) {
                InboundReceivingTask task = new InboundReceivingTask();
                task.setId(generateId());
                task.setInboundDetailsId(details.getId());
                String palletName = ("PLT-" + System.nanoTime());
                if (palletName.length() > PALLET_NAME_MAX_LEN) {
                    palletName = palletName.substring(0, PALLET_NAME_MAX_LEN);
                }
                task.setPalletName(palletName);
                task.setToReceivedQty(qtyPerPallet);
                task.setReceivedQty(BigDecimal.ZERO);
                task.setTaskStatus(0); // 0: Uncompleted
                task.setCreateTime(new Date());
                
                // Adjust last pallet to handle rounding errors
                if (i == palletCount - 1) {
                    BigDecimal sum = qtyPerPallet.multiply(new BigDecimal(palletCount - 1));
                    task.setToReceivedQty(details.getToReceivedQty().subtract(sum));
                }
                
                inboundReceivingTaskMapper.insert(task);
            }
        }

        // Update Inbound Status to 'Pending Receiving' (4)
        inbound.setInboundStatus("4");
        inboundMapper.update(inbound);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTasks(Long inboundId, List<InboundReceivingTask> tasks) {
        Inbound inbound = inboundMapper.selectById(inboundId);
        if (inbound == null) {
            throw new RuntimeException("Inbound order not found");
        }
        if (!("0".equals(inbound.getInboundStatus()) || "4".equals(inbound.getInboundStatus()))) {
            throw new RuntimeException("Inbound order status is not allowed, cannot save tasks");
        }
        if (tasks == null || tasks.isEmpty()) {
            throw new RuntimeException("Receiving tasks cannot be empty");
        }
        List<InboundDetails> detailsList = inboundDetailsMapper.selectByInboundId(inboundId);
        if (detailsList == null || detailsList.isEmpty()) {
            throw new RuntimeException("Inbound order has no details");
        }

        for (InboundDetails details : detailsList) {
            List<InboundReceivingTask> detailTasks = tasks.stream()
                    .filter(t -> t.getInboundDetailsId() != null && t.getInboundDetailsId().equals(details.getId()))
                    .collect(java.util.stream.Collectors.toList());
            if (detailTasks.isEmpty()) {
                throw new RuntimeException("Inbound detail has no receiving tasks: " + details.getId());
            }
            for (InboundReceivingTask t : detailTasks) {
                String palletName = t.getPalletName() == null ? null : t.getPalletName().trim();
                if (palletName == null || palletName.isEmpty()) {
                    throw new RuntimeException("Pallet name cannot be empty for detail " + details.getId());
                }
                if (palletName.length() > PALLET_NAME_MAX_LEN) {
                    throw new RuntimeException("Pallet name is too long for detail " + details.getId());
                }
                if (t.getToReceivedQty() == null || t.getToReceivedQty().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new RuntimeException("Task quantity must be greater than 0 for detail " + details.getId());
                }
            }
            BigDecimal sumQty = detailTasks.stream()
                    .map(InboundReceivingTask::getToReceivedQty)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (sumQty.compareTo(details.getToReceivedQty()) != 0) {
                throw new RuntimeException("Receiving task quantities do not match inbound detail quantity for detail " + details.getId());
            }
        }

        Date now = new Date();
        for (InboundDetails details : detailsList) {
            inboundReceivingTaskMapper.deleteByInboundDetailsId(details.getId());
            List<InboundReceivingTask> detailTasks = tasks.stream()
                    .filter(t -> t.getInboundDetailsId() != null && t.getInboundDetailsId().equals(details.getId()))
                    .collect(java.util.stream.Collectors.toList());
            for (InboundReceivingTask task : detailTasks) {
                InboundReceivingTask toSave = new InboundReceivingTask();
                toSave.setId(generateId());
                toSave.setInboundDetailsId(details.getId());
                toSave.setPalletName(task.getPalletName().trim());
                toSave.setToReceivedQty(task.getToReceivedQty());
                toSave.setReceivedQty(BigDecimal.ZERO);
                toSave.setTaskStatus(0);
                toSave.setRemark(task.getRemark());
                toSave.setCreateTime(now);
                inboundReceivingTaskMapper.insert(toSave);
            }
            details.setPalletCount(detailTasks.size());
            inboundDetailsMapper.update(details);
        }

        inbound.setInboundStatus("4");
        inboundMapper.update(inbound);
    }

    private long generateId() {
        long now = System.currentTimeMillis();
        int r = ThreadLocalRandom.current().nextInt(1000);
        return now * 1000 + r;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeTask(Long taskId, Long binId) {
        InboundReceivingTask task = inboundReceivingTaskMapper.selectById(taskId);
        if (task == null) {
            throw new RuntimeException("Task not found");
        }
        // Assuming 1 is completed. 
        if (task.getTaskStatus() != null && task.getTaskStatus() == 1) {
            throw new RuntimeException("Task already completed");
        }

        WarehouseBin bin = warehouseBinMapper.selectById(binId);
        if (bin == null) {
            throw new RuntimeException("Bin not found");
        }
        
        // Update task
        task.setTaskStatus(1);
        task.setBinId(binId);
        task.setReceivedQty(task.getToReceivedQty()); // Assume full receipt
        task.setOperationTime(new Date());
        task.setRemark(bin.getBinName() + "库位收货");
        inboundReceivingTaskMapper.update(task);

        // Update Inventory
        updateInventory(task);
        
        // Update Inbound Order Status logic
        updateInboundStatus(task.getInboundDetailsId());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelTask(Long taskId) {
        InboundReceivingTask task = inboundReceivingTaskMapper.selectById(taskId);
        if (task == null) {
            throw new RuntimeException("Task not found");
        }
        if (task.getTaskStatus() != null && task.getTaskStatus() == 1) {
            throw new RuntimeException("Cannot cancel completed task");
        }
        
        task.setTaskStatus(3); // 3: Void (已作废)
        inboundReceivingTaskMapper.update(task);
        
        // Revert Inbound Status if needed?
        // Requirement: "If cancelled, Inbound Status needs to be modified to 'Pending Receiving'".
        // This implies if we were in "In Progress" (1), we might go back to "Pending Receiving" (4)
        // if no other tasks are in progress?
        // Let's implement simple logic: Force status to 4.
        
        InboundDetails details = inboundDetailsMapper.selectById(task.getInboundDetailsId());
        Inbound inbound = inboundMapper.selectById(details.getInboundId());
        inbound.setInboundStatus("4");
        inboundMapper.update(inbound);
    }
    
    private void updateInboundStatus(Long inboundDetailsId) {
        InboundDetails details = inboundDetailsMapper.selectById(inboundDetailsId);
        Long inboundId = details.getInboundId();
        Inbound inbound = inboundMapper.selectById(inboundId);
        
        // Logic:
        // If at least one task is completed -> In Progress (1)
        // If ALL tasks for ALL details are completed -> Completed (2)
        
        List<InboundDetails> allDetails = inboundDetailsMapper.selectByInboundId(inboundId);
        boolean allCompleted = true;
        boolean anyCompleted = false;
        
        for (InboundDetails d : allDetails) {
            List<InboundReceivingTask> tasks = inboundReceivingTaskMapper.selectByInboundDetailsId(d.getId());
            if (tasks.isEmpty()) {
                // If no tasks, technically not completed? Or ignored?
                // Assuming tasks generated.
                allCompleted = false; 
                continue;
            }
            for (InboundReceivingTask t : tasks) {
                if (t.getTaskStatus() == 1) {
                    anyCompleted = true;
                } else if (t.getTaskStatus() != 3) { // Ignore Void tasks? 
                    // If any task is 0 (Uncompleted), then not all completed.
                    allCompleted = false;
                }
            }
        }
        
        if (allCompleted) {
            inbound.setInboundStatus("2");
        } else if (anyCompleted) {
            inbound.setInboundStatus("1");
        } else {
            // Maybe stays at 4
        }
        
        inboundMapper.update(inbound);
    }

    private void updateInventory(InboundReceivingTask task) {
        InboundDetails details = inboundDetailsMapper.selectById(task.getInboundDetailsId());
        Inbound inbound = inboundMapper.selectById(details.getInboundId());
        WarehouseBin bin = warehouseBinMapper.selectById(task.getBinId());
        
        if (bin == null) {
            throw new RuntimeException("Bin not found");
        }

        // 1. Inventory Batch
        InventoryBatch batchQuery = new InventoryBatch();
        batchQuery.setWarehouseId(inbound.getWarehouseId());
        batchQuery.setSkuId(details.getSkuId());
        batchQuery.setBatchNo(details.getBatchNo());
        
        List<InventoryBatch> batches = inventoryBatchMapper.selectList(batchQuery);
        InventoryBatch batch;
        if (batches.isEmpty()) {
            batch = new InventoryBatch();
            batch.setWarehouseId(inbound.getWarehouseId());
            batch.setSkuId(details.getSkuId());
            batch.setBatchNo(details.getBatchNo());
            batch.setTotalQty(BigDecimal.ZERO);
            batch.setAvailableQty(BigDecimal.ZERO);
            batch.setFrozenQty(BigDecimal.ZERO);
            batch.setFdaHold(0);
            inventoryBatchMapper.insert(batch);
        } else {
            batch = batches.get(0);
        }
        
        // Update batch quantities
        BigDecimal qty = task.getReceivedQty();
        batch.setTotalQty(batch.getTotalQty().add(qty));
        batch.setAvailableQty(batch.getAvailableQty().add(qty));
        inventoryBatchMapper.update(batch);

        // 2. Inventory Bin
        InventoryBin binQuery = new InventoryBin();
        binQuery.setBinId(bin.getBinName()); // Using binName as binId per schema observation
        binQuery.setBatchId(batch.getId());
        
        List<InventoryBin> inventoryBins = inventoryBinMapper.selectList(binQuery);
        InventoryBin invBin;
        if (inventoryBins.isEmpty()) {
            invBin = new InventoryBin();
            invBin.setId(generateId());
            invBin.setBinId(bin.getBinName());
            invBin.setBatchId(batch.getId());
            invBin.setProductStatus(details.getProductStatus());
            invBin.setTotalQty(BigDecimal.ZERO);
            inventoryBinMapper.insert(invBin);
        } else {
            invBin = inventoryBins.get(0);
        }
        
        invBin.setTotalQty(invBin.getTotalQty().add(qty));
        inventoryBinMapper.update(invBin);

        // 3. Inventory Summary
        InventorySummary summary = inventorySummaryMapper.selectBySkuId(details.getSkuId());
        if (summary == null) {
            summary = new InventorySummary();
            summary.setSkuId(details.getSkuId());
            summary.setTotalQty(BigDecimal.ZERO);
            summary.setAvailableQty(BigDecimal.ZERO);
            summary.setFrozenQty(BigDecimal.ZERO);
            summary.setUpdateTime(new Date());
            inventorySummaryMapper.insert(summary);
        } else {
            summary.setTotalQty(summary.getTotalQty().add(qty));
            summary.setAvailableQty(summary.getAvailableQty().add(qty));
            summary.setUpdateTime(new Date());
            inventorySummaryMapper.update(summary);
        }

        // 4. History
        InventoryHistory history = new InventoryHistory();
        history.setBinId(invBin.getId()); // FK to inventory_bin.id
        history.setBeforeQty(invBin.getTotalQty().subtract(qty));
        history.setAfterQty(invBin.getTotalQty());
        history.setDiffQty(qty);
        history.setInventoryOpType("IN"); // From SysDict
        history.setRelatedOrderId(inbound.getId());
        history.setOperationDate(new Date());
        // history.setOperatorId(...);
        inventoryHistoryMapper.insert(history);
    }
}
