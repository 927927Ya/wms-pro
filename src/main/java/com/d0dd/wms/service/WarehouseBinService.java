package com.d0dd.wms.service;

import com.d0dd.wms.dto.WarehouseBinBatchDTO;
import com.d0dd.wms.entity.WarehouseBin;
import java.util.List;

public interface WarehouseBinService {
    WarehouseBin getById(Long id);
    List<WarehouseBin> list(WarehouseBin warehouseBin);
    int save(WarehouseBin warehouseBin);
    int batchSave(WarehouseBinBatchDTO batchDTO);
    int update(WarehouseBin warehouseBin);
    int removeById(Long id);
}
