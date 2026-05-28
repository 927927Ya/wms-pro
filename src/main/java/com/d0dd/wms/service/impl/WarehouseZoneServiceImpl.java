package com.d0dd.wms.service.impl;

import com.d0dd.wms.entity.WarehouseZone;
import com.d0dd.wms.mapper.WarehouseZoneMapper;
import com.d0dd.wms.service.WarehouseZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseZoneServiceImpl implements WarehouseZoneService {

    @Autowired
    private WarehouseZoneMapper warehouseZoneMapper;

    @Override
    public WarehouseZone getById(Long id) {
        return warehouseZoneMapper.selectById(id);
    }

    @Override
    public List<WarehouseZone> list(WarehouseZone warehouseZone) {
        return warehouseZoneMapper.selectList(warehouseZone);
    }

    @Override
    public int save(WarehouseZone warehouseZone) {
        WarehouseZone existing = warehouseZoneMapper.selectByName(warehouseZone.getZoneName());
        if (existing != null) {
            throw new RuntimeException("库区名称已存在");
        }
        return warehouseZoneMapper.insert(warehouseZone);
    }

    @Override
    public int update(WarehouseZone warehouseZone) {
        WarehouseZone existing = warehouseZoneMapper.selectByName(warehouseZone.getZoneName());
        if (existing != null && !existing.getId().equals(warehouseZone.getId())) {
            throw new RuntimeException("库区名称已存在");
        }
        return warehouseZoneMapper.update(warehouseZone);
    }

    @Override
    public int removeById(Long id) {
        return warehouseZoneMapper.deleteById(id);
    }
}
