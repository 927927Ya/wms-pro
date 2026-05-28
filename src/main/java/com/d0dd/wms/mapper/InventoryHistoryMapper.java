package com.d0dd.wms.mapper;

import com.d0dd.wms.entity.InventoryHistory;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface InventoryHistoryMapper {
    InventoryHistory selectById(Long id);
    List<InventoryHistory> selectList(InventoryHistory inventoryHistory);
    int insert(InventoryHistory inventoryHistory);
    int update(InventoryHistory inventoryHistory);
    int deleteById(Long id);
}
