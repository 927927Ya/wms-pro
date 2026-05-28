package com.d0dd.wms.service;

import com.d0dd.wms.entity.WarehouseRack;
import java.util.List;

public interface WarehouseRackService {
    WarehouseRack getById(Long id);
    List<WarehouseRack> list(WarehouseRack warehouseRack);
    int save(WarehouseRack warehouseRack);
    int update(WarehouseRack warehouseRack);
    int removeById(Long id);
}
