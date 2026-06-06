package com.d0dd.wms.service.impl;

import com.d0dd.wms.dto.StorageQueryDto;
import com.d0dd.wms.entity.*;
import com.d0dd.wms.mapper.*;
import com.d0dd.wms.service.OutboundPickingTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class OutboundPickingTaskServiceImpl implements OutboundPickingTaskService {

    private static final int PALLET_NAME_MAX_LEN = 20;

    @Autowired
    private OutboundPickingTaskMapper outboundPickingTaskMapper;

    @Autowired
    private InventoryBinMapper inventoryBinMapper;

    @Autowired
    private InventoryBatchMapper inventoryBatchMapper;

    @Autowired
    private InventorySummaryMapper inventorySummaryMapper;

    @Autowired
    private InventoryHistoryMapper inventoryHistoryMapper;

    @Autowired
    private OutboundDetailsMapper outboundDetailsMapper;
    
    @Autowired
    private OutboundMapper outboundMapper;

    @Autowired
    private WarehouseBinMapper warehouseBinMapper;

    @Override
    public OutboundPickingTask getById(Long id) {
        return outboundPickingTaskMapper.selectById(id);
    }

    @Override
    public List<OutboundPickingTask> list(OutboundPickingTask outboundPickingTask) {
        return outboundPickingTaskMapper.selectList(outboundPickingTask);
    }
    
    @Override
    public List<OutboundPickingTask> getByOutboundDetailsId(Long outboundDetailsId) {
        return outboundPickingTaskMapper.selectByOutboundDetailsId(outboundDetailsId);
    }

    @Override
    public int save(OutboundPickingTask outboundPickingTask) {
        if (outboundPickingTask.getId() == null) {
            outboundPickingTask.setId(generateId());
        }
        if (outboundPickingTask.getTaskStatus() == null) {
            outboundPickingTask.setTaskStatus(0);
        }
        if (outboundPickingTask.getPickedQty() == null) {
            outboundPickingTask.setPickedQty(BigDecimal.ZERO);
        }
        return outboundPickingTaskMapper.insert(outboundPickingTask);
    }

    @Override
    public int update(OutboundPickingTask outboundPickingTask) {
        return outboundPickingTaskMapper.update(outboundPickingTask);
    }

    @Override
    public int removeById(Long id) {
        return outboundPickingTaskMapper.deleteById(id);
    }
    
    @Override
    public int removeByOutboundDetailsId(Long outboundDetailsId) {
        return outboundPickingTaskMapper.deleteByOutboundDetailsId(outboundDetailsId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateTasks(Long outboundId) {
        Outbound outbound = outboundMapper.selectById(outboundId);
        if (outbound == null) {
            throw new RuntimeException("Outbound order not found");
        }
        
        if (!"0".equals(outbound.getOutboundStatus())) {
            throw new RuntimeException("Outbound order status is not 'Created', cannot generate tasks");
        }

        List<OutboundDetails> detailsList = outboundDetailsMapper.selectByOutboundId(outboundId);
        if (detailsList == null || detailsList.isEmpty()) {
            throw new RuntimeException("Outbound order has no details");
        }

        for (OutboundDetails details : detailsList) {
            OutboundPickingTask task = new OutboundPickingTask();
            task.setOutboundDetailsId(details.getId());
            task.setTaskStatus(0); // Uncompleted
            task.setToPickedQty(details.getOutQty());
            task.setPickedQty(BigDecimal.ZERO);
            save(task);
        }

        outbound.setOutboundStatus("1");
        outboundMapper.update(outbound);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTasks(Long outboundId, List<OutboundPickingTask> tasks) {
        Outbound outbound = outboundMapper.selectById(outboundId);
        if (outbound == null) {
            throw new RuntimeException("Outbound order not found");
        }
        if (!("0".equals(outbound.getOutboundStatus()) || "1".equals(outbound.getOutboundStatus()))) {
            throw new RuntimeException("Outbound order status is not allowed, cannot save tasks");
        }
        if (tasks == null || tasks.isEmpty()) {
            throw new RuntimeException("Picking tasks cannot be empty");
        }

        List<OutboundDetails> detailsList = outboundDetailsMapper.selectByOutboundId(outboundId);
        if (detailsList == null || detailsList.isEmpty()) {
            throw new RuntimeException("Outbound order has no details");
        }

        for (OutboundDetails details : detailsList) {
            List<OutboundPickingTask> detailTasks = tasks.stream()
                    .filter(t -> t.getOutboundDetailsId() != null && t.getOutboundDetailsId().equals(details.getId()))
                    .collect(Collectors.toList());
            if (detailTasks.isEmpty()) {
                throw new RuntimeException("Outbound detail has no picking tasks: " + details.getId());
            }
            for (OutboundPickingTask t : detailTasks) {
                String palletName = t.getPalletName() == null ? null : t.getPalletName().trim();
                if (palletName == null || palletName.isEmpty()) {
                    throw new RuntimeException("Pallet name cannot be empty for detail " + details.getId());
                }
                if (palletName.length() > PALLET_NAME_MAX_LEN) {
                    throw new RuntimeException("Pallet name is too long for detail " + details.getId());
                }
                if (t.getToPickedQty() == null || t.getToPickedQty().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new RuntimeException("Task quantity must be greater than 0 for detail " + details.getId());
                }
            }
            BigDecimal sumQty = detailTasks.stream()
                    .map(OutboundPickingTask::getToPickedQty)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (sumQty.compareTo(details.getOutQty()) != 0) {
                throw new RuntimeException("Picking task quantities do not match outbound detail quantity for detail " + details.getId());
            }
        }

        Date now = new Date();
        for (OutboundDetails details : detailsList) {
            outboundPickingTaskMapper.deleteByOutboundDetailsId(details.getId());
            List<OutboundPickingTask> detailTasks = tasks.stream()
                    .filter(t -> t.getOutboundDetailsId() != null && t.getOutboundDetailsId().equals(details.getId()))
                    .collect(Collectors.toList());
            for (OutboundPickingTask task : detailTasks) {
                OutboundPickingTask toSave = new OutboundPickingTask();
                toSave.setId(generateId());
                toSave.setOutboundDetailsId(details.getId());
                toSave.setPalletName(task.getPalletName().trim());
                toSave.setToPickedQty(task.getToPickedQty());
                toSave.setPickedQty(BigDecimal.ZERO);
                toSave.setQtyUnit(details.getQtyUnit());
                toSave.setTaskStatus(0);
                toSave.setRemark(task.getRemark());
                toSave.setOperationTime(now);
                outboundPickingTaskMapper.insert(toSave);
            }
        }

        outbound.setOutboundStatus("1");
        outboundMapper.update(outbound);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeTask(Long taskId, Long binId) {
        OutboundPickingTask task = outboundPickingTaskMapper.selectById(taskId);
        if (task == null) {
            throw new RuntimeException("Task not found");
        }
        if (task.getTaskStatus() != null && task.getTaskStatus() == 1) {
            throw new RuntimeException("Task already completed");
        }

        OutboundDetails details = outboundDetailsMapper.selectById(task.getOutboundDetailsId());
        if (details == null) {
            throw new RuntimeException("Outbound details not found");
        }
        Outbound outbound = outboundMapper.selectById(details.getOutboundId());
        if (outbound == null) {
            throw new RuntimeException("Outbound order not found");
        }

        BigDecimal requiredQty = task.getPickedQty();
        if (requiredQty == null || requiredQty.compareTo(BigDecimal.ZERO) == 0) {
            requiredQty = task.getToPickedQty();
        }
        if (requiredQty == null || requiredQty.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Picked qty is invalid");
        }

        List<InventoryBin> candidates = inventoryBinMapper.selectAvailableForSkuInWarehouse(details.getSkuId(), outbound.getWarehouseId(), requiredQty);
        if (candidates == null || candidates.isEmpty()) {
            throw new RuntimeException("No available inventory for this SKU");
        }

        InventoryBin selectedInventoryBin = null;
        String selectedWarehouseBinName = null;

        if (binId != null && binId > 0) {
            selectedInventoryBin = inventoryBinMapper.selectById(binId);
            if (selectedInventoryBin == null) {
                WarehouseBin selectedBin = warehouseBinMapper.selectById(binId);
                if (selectedBin != null) {
                    selectedWarehouseBinName = selectedBin.getBinName();
                    String selectedWarehouseBinIdStr = String.valueOf(selectedBin.getId());
                    selectedInventoryBin = candidates.stream()
                            .filter(b -> Objects.equals(b.getBinId(), selectedWarehouseBinIdStr)
                                    || Objects.equals(b.getBinId(), selectedBin.getBinName()))
                            .findFirst()
                            .orElse(null);
                }
            }
        }

        // Fallback: auto-pick first available inventory bin
        if (selectedInventoryBin == null) {
            selectedInventoryBin = candidates.get(0);
        }

        InventoryBin finalSelectedInventoryBin = selectedInventoryBin;
        boolean isCandidate = candidates.stream().anyMatch(b -> Objects.equals(b.getId(), finalSelectedInventoryBin.getId()));
        if (!isCandidate) {
            throw new RuntimeException("Selected bin has no available inventory for this SKU");
        }

        task.setTaskStatus(1);
        task.setInventoryBinId(finalSelectedInventoryBin.getId());
        task.setOperationTime(new Date());
        String remarkBin = selectedWarehouseBinName != null ? selectedWarehouseBinName : finalSelectedInventoryBin.getBinId();
        task.setRemark(remarkBin + "库位拣货");
        
        if (task.getPickedQty() == null || task.getPickedQty().compareTo(BigDecimal.ZERO) == 0) {
            task.setPickedQty(task.getToPickedQty());
        }
        
        outboundPickingTaskMapper.update(task);
        
        // Deduct Inventory
        updateInventory(task);
        
        // Update Outbound Order Status
        updateOutboundStatus(task.getOutboundDetailsId());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelTask(Long taskId) {
        OutboundPickingTask task = outboundPickingTaskMapper.selectById(taskId);
        if (task == null) {
            throw new RuntimeException("Task not found");
        }
        if (task.getTaskStatus() != null && task.getTaskStatus() == 1) {
            throw new RuntimeException("Cannot cancel completed task");
        }
        
        task.setTaskStatus(3); // 3: Void
        outboundPickingTaskMapper.update(task);

        updateOutboundStatus(task.getOutboundDetailsId());
    }

    private void updateOutboundStatus(Long outboundDetailsId) {
        OutboundDetails details = outboundDetailsMapper.selectById(outboundDetailsId);
        if (details == null) {
            return;
        }

        Long outboundId = details.getOutboundId();
        Outbound outbound = outboundMapper.selectById(outboundId);
        if (outbound == null) {
            return;
        }
        
        List<OutboundDetails> allDetails = outboundDetailsMapper.selectByOutboundId(outboundId);
        boolean anyNonVoidTask = false;
        boolean allNonVoidTasksCompleted = true;
        boolean allDetailsHaveNonVoidTask = true;
        
        for (OutboundDetails d : allDetails) {
            List<OutboundPickingTask> tasks = outboundPickingTaskMapper.selectByOutboundDetailsId(d.getId());
            if (tasks == null || tasks.isEmpty()) {
                allDetailsHaveNonVoidTask = false;
                continue;
            }

            boolean hasNonVoid = false;
            for (OutboundPickingTask t : tasks) {
                Integer taskStatus = t.getTaskStatus();
                if (taskStatus == null || taskStatus != 3) {
                    hasNonVoid = true;
                    anyNonVoidTask = true;
                    if (!Integer.valueOf(1).equals(taskStatus)) {
                        allNonVoidTasksCompleted = false;
                    }
                }
            }

            if (!hasNonVoid) {
                allDetailsHaveNonVoidTask = false;
            }
        }

        if (!anyNonVoidTask) {
            outbound.setOutboundStatus("0");
        } else if (allDetailsHaveNonVoidTask && allNonVoidTasksCompleted) {
            outbound.setOutboundStatus("2");
        } else {
            outbound.setOutboundStatus("1");
        }
        
        outboundMapper.update(outbound);
    }

    private void updateInventory(OutboundPickingTask task) {
        BigDecimal qty = task.getPickedQty();
        if (qty.compareTo(BigDecimal.ZERO) <= 0) {
            return; 
        }

        // 1. Inventory Bin
        InventoryBin bin = inventoryBinMapper.selectById(task.getInventoryBinId());
        if (bin == null) {
            throw new RuntimeException("Inventory Bin not found: " + task.getInventoryBinId());
        }
        if (bin.getTotalQty().compareTo(qty) < 0) {
            throw new RuntimeException("Insufficient inventory in bin: " + bin.getId());
        }
        bin.setTotalQty(bin.getTotalQty().subtract(qty));
        inventoryBinMapper.update(bin);

        // 2. Inventory Batch
        InventoryBatch batch = inventoryBatchMapper.selectById(bin.getBatchId());
        if (batch != null) {
            batch.setTotalQty(batch.getTotalQty().subtract(qty));
            batch.setAvailableQty(batch.getAvailableQty().subtract(qty));
            inventoryBatchMapper.update(batch);
        }

        // 3. Inventory Summary
        // Need to get SKU ID from Batch or Outbound Details
        Long skuId = null;
        if (batch != null) {
            skuId = batch.getSkuId();
        } else {
            // Fallback to outbound details
            OutboundDetails details = outboundDetailsMapper.selectById(task.getOutboundDetailsId());
            if (details != null) {
                skuId = details.getSkuId();
            }
        }

        if (skuId != null) {
            InventorySummary summary = inventorySummaryMapper.selectBySkuId(skuId);
            if (summary != null) {
                summary.setTotalQty(summary.getTotalQty().subtract(qty));
                summary.setAvailableQty(summary.getAvailableQty().subtract(qty));
                summary.setUpdateTime(new Date());
                inventorySummaryMapper.update(summary);
            }
        }

        // 4. History
        OutboundDetails details = outboundDetailsMapper.selectById(task.getOutboundDetailsId());
        Long orderId = (details != null) ? details.getOutboundId() : null;

        InventoryHistory history = new InventoryHistory();
        history.setBinId(bin.getId());
        history.setBeforeQty(bin.getTotalQty().add(qty));
        history.setAfterQty(bin.getTotalQty());
        history.setDiffQty(qty.negate()); // Negative for outbound
        history.setInventoryOpType("OUT");
        history.setRelatedOrderId(orderId);
        history.setOperationDate(new Date());
        // history.setOperatorId(...);
        inventoryHistoryMapper.insert(history);
    }

    private long generateId() {
        long now = System.currentTimeMillis();
        int r = ThreadLocalRandom.current().nextInt(1000);
        return now * 1000 + r;
    }
}
