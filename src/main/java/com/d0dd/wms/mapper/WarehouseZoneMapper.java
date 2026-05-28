package com.d0dd.wms.mapper;

import com.d0dd.wms.entity.WarehouseZone;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface WarehouseZoneMapper {
    WarehouseZone selectById(Long id);
    List<WarehouseZone> selectList(WarehouseZone warehouseZone);
    WarehouseZone selectByName(String zoneName);
    int insert(WarehouseZone warehouseZone);
    int update(WarehouseZone warehouseZone);
    int deleteById(Long id);
}
