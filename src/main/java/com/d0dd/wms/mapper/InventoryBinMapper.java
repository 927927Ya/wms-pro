package com.d0dd.wms.mapper;

import com.d0dd.wms.dto.StorageQueryDto;
import com.d0dd.wms.entity.InventoryBin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface InventoryBinMapper {
    InventoryBin selectById(Long id);
    List<InventoryBin> selectList(InventoryBin inventoryBin);
    List<InventoryBin> selectListByDto(StorageQueryDto queryDto);
    List<InventoryBin> selectByBatchId(Long batchId);
    List<InventoryBin> selectAvailableForSkuInWarehouse(@Param("skuId") Long skuId, @Param("warehouseId") Long warehouseId, @Param("minQty") BigDecimal minQty);
    int countOccupiedBins();
    int insert(InventoryBin inventoryBin);
    int update(InventoryBin inventoryBin);
    int deleteById(Long id);
}
