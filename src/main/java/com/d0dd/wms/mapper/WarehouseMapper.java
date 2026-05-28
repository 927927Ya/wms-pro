package com.d0dd.wms.mapper;

import com.d0dd.wms.entity.Warehouse;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface WarehouseMapper {
    Warehouse selectById(Long id);
    List<Warehouse> selectList(Warehouse warehouse);
    Warehouse selectByName(String warehouseName);
    int insert(Warehouse warehouse);
    int update(Warehouse warehouse);
    int deleteById(Long id);
}
