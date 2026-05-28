package com.d0dd.wms.service;

import com.d0dd.wms.entity.InventoryHistory;
import java.util.List;

public interface InventoryHistoryService {
    InventoryHistory getById(Long id);
    List<InventoryHistory> list(InventoryHistory inventoryHistory);
    int save(InventoryHistory inventoryHistory);
    int update(InventoryHistory inventoryHistory);
    int removeById(Long id);
}
