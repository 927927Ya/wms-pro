package com.d0dd.wms.service;

import com.d0dd.wms.dto.StorageQueryDto;
import com.d0dd.wms.entity.InventoryBin;
import java.util.List;

public interface InventoryBinService {
    InventoryBin getById(Long id);
    List<InventoryBin> list(InventoryBin inventoryBin);
    List<InventoryBin> list(StorageQueryDto queryDto);
    List<InventoryBin> getByBatchId(Long batchId);
    int save(InventoryBin inventoryBin);
    int update(InventoryBin inventoryBin);
    int removeById(Long id);
}
