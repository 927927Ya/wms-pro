package com.d0dd.wms.mapper;

import com.d0dd.wms.entity.WarehouseBin;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface WarehouseBinMapper {
    WarehouseBin selectById(Long id);
    List<WarehouseBin> selectList(WarehouseBin warehouseBin);
    WarehouseBin selectByName(String binName);
    List<WarehouseBin> selectByWarehouseId(Long warehouseId);
    int countAll();
    int insert(WarehouseBin warehouseBin);
    int batchInsert(List<WarehouseBin> list);
    int update(WarehouseBin warehouseBin);
    int deleteById(Long id);
}
