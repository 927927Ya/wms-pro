package com.d0dd.wms.mapper;

import com.d0dd.wms.entity.WarehouseRack;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface WarehouseRackMapper {
    WarehouseRack selectById(Long id);
    List<WarehouseRack> selectList(WarehouseRack warehouseRack);
    WarehouseRack selectByName(String rackName);
    int insert(WarehouseRack warehouseRack);
    int update(WarehouseRack warehouseRack);
    int deleteById(Long id);
}
