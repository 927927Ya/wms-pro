package com.d0dd.wms.service;

import com.d0dd.wms.dto.StorageQueryDto;
import com.d0dd.wms.entity.InventorySummary;
import java.util.List;

public interface InventorySummaryService {
    InventorySummary getById(Long id);
    List<InventorySummary> list(InventorySummary inventorySummary);
    List<InventorySummary> list(StorageQueryDto queryDto);
    InventorySummary getBySkuId(Long skuId);
    int save(InventorySummary inventorySummary);
    int update(InventorySummary inventorySummary);
    int removeById(Long id);
}
