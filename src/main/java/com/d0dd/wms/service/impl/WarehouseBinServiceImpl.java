package com.d0dd.wms.service.impl;

import com.d0dd.wms.dto.WarehouseBinBatchDTO;
import com.d0dd.wms.entity.WarehouseBin;
import com.d0dd.wms.entity.WarehouseRack;
import com.d0dd.wms.mapper.WarehouseBinMapper;
import com.d0dd.wms.mapper.WarehouseRackMapper;
import com.d0dd.wms.service.WarehouseBinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class WarehouseBinServiceImpl implements WarehouseBinService {

    @Autowired
    private WarehouseBinMapper warehouseBinMapper;

    @Autowired
    private WarehouseRackMapper warehouseRackMapper;

    @Override
    public WarehouseBin getById(Long id) {
        return warehouseBinMapper.selectById(id);
    }

    @Override
    public List<WarehouseBin> list(WarehouseBin warehouseBin) {
        return warehouseBinMapper.selectList(warehouseBin);
    }

    @Override
    public int save(WarehouseBin warehouseBin) {
        WarehouseBin existing = warehouseBinMapper.selectByName(warehouseBin.getBinName());
        if (existing != null) {
            throw new RuntimeException("库位名称已存在");
        }
        return warehouseBinMapper.insert(warehouseBin);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchSave(WarehouseBinBatchDTO batchDTO) {
        WarehouseRack rack = warehouseRackMapper.selectById(batchDTO.getRackId());
        if (rack == null) {
            throw new RuntimeException("货架不存在");
        }

        List<WarehouseBin> binsToInsert = new ArrayList<>();
        // Generate bins: RackName-Column-Layer (e.g., A1-3-1)
        for (int col = 1; col <= batchDTO.getColumnCount(); col++) {
            for (int layer = 1; layer <= batchDTO.getLayerCount(); layer++) {
                String binName = String.format("%s-%d-%d", rack.getRackName(), col, layer);
                
                // Skip if exists
                if (warehouseBinMapper.selectByName(binName) != null) {
                    continue; 
                }

                WarehouseBin bin = new WarehouseBin();
                bin.setBinName(binName);
                bin.setWarehouseId(rack.getWarehouseId());
                bin.setZoneId(rack.getZoneId());
                bin.setRackId(rack.getId());
                bin.setColumnNum(col);
                bin.setOnZoomLevel(layer);
                
                // Set default properties from DTO
                bin.setRackType(batchDTO.getRackType());
                bin.setBinType(batchDTO.getBinType());
                bin.setMaximumCapcity(batchDTO.getMaximumCapcity());
                bin.setCapcityUnit(batchDTO.getCapcityUnit());
                bin.setMaximumVolume(batchDTO.getMaximumVolume());
                bin.setVolumeUnit(batchDTO.getVolumeUnit());
                bin.setStorageRule(batchDTO.getStorageRule());
                bin.setIsEnabled(batchDTO.getIsEnabled());
                bin.setLowStorageAlertRato(batchDTO.getLowStorageAlertRato());

                binsToInsert.add(bin);
            }
        }

        if (!binsToInsert.isEmpty()) {
            return warehouseBinMapper.batchInsert(binsToInsert);
        }
        return 0;
    }

    @Override
    public int update(WarehouseBin warehouseBin) {
        WarehouseBin existing = warehouseBinMapper.selectByName(warehouseBin.getBinName());
        if (existing != null && !existing.getId().equals(warehouseBin.getId())) {
            throw new RuntimeException("库位名称已存在");
        }
        int rows = warehouseBinMapper.update(warehouseBin);
        if (rows == 0) {
            throw new RuntimeException("更新失败：ID不存在");
        }
        return rows;
    }

    @Override
    public int removeById(Long id) {
        return warehouseBinMapper.deleteById(id);
    }
}
