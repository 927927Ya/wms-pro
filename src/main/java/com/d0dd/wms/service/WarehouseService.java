package com.d0dd.wms.service;

import com.d0dd.wms.entity.Warehouse;
import java.util.List;

public interface WarehouseService {
    Warehouse getById(Long id);
    List<Warehouse> list(Warehouse warehouse);
    int save(Warehouse warehouse);
    int update(Warehouse warehouse);
    int removeById(Long id);
}
