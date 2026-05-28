package com.d0dd.wms.service.impl;

import com.d0dd.wms.entity.WarehouseRack;
import com.d0dd.wms.mapper.WarehouseRackMapper;
import com.d0dd.wms.service.WarehouseRackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class WarehouseRackServiceImpl implements WarehouseRackService {

    @Autowired
    private WarehouseRackMapper warehouseRackMapper;

    @Override
    public WarehouseRack getById(Long id) {
        return warehouseRackMapper.selectById(id);
    }

    @Override
    public List<WarehouseRack> list(WarehouseRack warehouseRack) {
        return warehouseRackMapper.selectList(warehouseRack);
    }

    @Override
    public int save(WarehouseRack warehouseRack) {
        WarehouseRack existing = warehouseRackMapper.selectByName(warehouseRack.getRackName());
        if (existing != null) {
            throw new RuntimeException("货架名称已存在");
        }
        if (warehouseRack.getId() == null) {
            long base = System.currentTimeMillis() * 1000;
            warehouseRack.setId(base + ThreadLocalRandom.current().nextInt(1000));
        }
        return warehouseRackMapper.insert(warehouseRack);
    }

    @Override
    public int update(WarehouseRack warehouseRack) {
        WarehouseRack existing = warehouseRackMapper.selectByName(warehouseRack.getRackName());
        if (existing != null && !existing.getId().equals(warehouseRack.getId())) {
            throw new RuntimeException("货架名称已存在");
        }
        return warehouseRackMapper.update(warehouseRack);
    }

    @Override
    public int removeById(Long id) {
        return warehouseRackMapper.deleteById(id);
    }
}
