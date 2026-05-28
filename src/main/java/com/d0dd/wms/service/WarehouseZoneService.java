package com.d0dd.wms.service;

import com.d0dd.wms.entity.WarehouseZone;
import java.util.List;

public interface WarehouseZoneService {
    WarehouseZone getById(Long id);
    List<WarehouseZone> list(WarehouseZone warehouseZone);
    int save(WarehouseZone warehouseZone);
    int update(WarehouseZone warehouseZone);
    int removeById(Long id);
}
