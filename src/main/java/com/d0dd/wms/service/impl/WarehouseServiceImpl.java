package com.d0dd.wms.service.impl;

import com.d0dd.wms.entity.Warehouse;
import com.d0dd.wms.mapper.WarehouseMapper;
import com.d0dd.wms.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Override
    public Warehouse getById(Long id) {
        return warehouseMapper.selectById(id);
    }

    @Override
    public List<Warehouse> list(Warehouse warehouse) {
        return warehouseMapper.selectList(warehouse);
    }

    @Override
    public int save(Warehouse warehouse) {
        Warehouse existing = warehouseMapper.selectByName(warehouse.getWarehouseName());
        if (existing != null) {
            throw new RuntimeException("仓库名称已存在");
        }
        return warehouseMapper.insert(warehouse);
    }

    @Override
    public int update(Warehouse warehouse) {
        Warehouse existing = warehouseMapper.selectByName(warehouse.getWarehouseName());
        if (existing != null && !existing.getId().equals(warehouse.getId())) {
            throw new RuntimeException("仓库名称已存在");
        }
        return warehouseMapper.update(warehouse);
    }

    @Override
    public int removeById(Long id) {
        return warehouseMapper.deleteById(id);
    }
}
