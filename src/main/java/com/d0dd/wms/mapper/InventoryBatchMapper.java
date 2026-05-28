package com.d0dd.wms.mapper;

import com.d0dd.wms.dto.StorageQueryDto;
import com.d0dd.wms.entity.InventoryBatch;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface InventoryBatchMapper {
    InventoryBatch selectById(Long id);
    List<InventoryBatch> selectList(InventoryBatch inventoryBatch);
    List<InventoryBatch> selectListByDto(StorageQueryDto queryDto);
    List<InventoryBatch> selectBySkuId(Long skuId);
    int insert(InventoryBatch inventoryBatch);
    int update(InventoryBatch inventoryBatch);
    int deleteById(Long id);
}
