package com.d0dd.wms.mapper;

import com.d0dd.wms.dto.StorageQueryDto;
import com.d0dd.wms.entity.InventorySummary;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface InventorySummaryMapper {
    InventorySummary selectById(Long id);
    List<InventorySummary> selectList(InventorySummary inventorySummary);
    List<InventorySummary> selectListByDto(StorageQueryDto queryDto);
    InventorySummary selectBySkuId(Long skuId);
    java.math.BigDecimal sumTotalQty();
    int insert(InventorySummary inventorySummary);
    int update(InventorySummary inventorySummary);
    int deleteById(Long id);
}
