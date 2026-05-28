package com.d0dd.wms.service;

import com.d0dd.wms.dto.StorageQueryDto;
import com.d0dd.wms.entity.InventoryBatch;
import java.util.List;

public interface InventoryBatchService {
    InventoryBatch getById(Long id);
    List<InventoryBatch> list(InventoryBatch inventoryBatch);
    List<InventoryBatch> list(StorageQueryDto queryDto);
    List<InventoryBatch> getBySkuId(Long skuId);
    int save(InventoryBatch inventoryBatch);
    int update(InventoryBatch inventoryBatch);
    int removeById(Long id);
}
