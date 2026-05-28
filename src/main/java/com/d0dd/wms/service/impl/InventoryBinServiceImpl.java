package com.d0dd.wms.service.impl;

import com.d0dd.wms.dto.StorageQueryDto;
import com.d0dd.wms.entity.InventoryBin;
import com.d0dd.wms.mapper.InventoryBinMapper;
import com.d0dd.wms.service.InventoryBinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class InventoryBinServiceImpl implements InventoryBinService {

    @Autowired
    private InventoryBinMapper inventoryBinMapper;

    @Override
    public InventoryBin getById(Long id) {
        return inventoryBinMapper.selectById(id);
    }

    @Override
    public List<InventoryBin> list(InventoryBin inventoryBin) {
        return inventoryBinMapper.selectList(inventoryBin);
    }
    
    @Override
    public List<InventoryBin> list(StorageQueryDto queryDto) {
        return inventoryBinMapper.selectListByDto(queryDto);
    }

    @Override
    public List<InventoryBin> getByBatchId(Long batchId) {
        return inventoryBinMapper.selectByBatchId(batchId);
    }

    @Override
    public int save(InventoryBin inventoryBin) {
        if (inventoryBin.getId() == null) {
            inventoryBin.setId(generateId());
        }
        return inventoryBinMapper.insert(inventoryBin);
    }

    @Override
    public int update(InventoryBin inventoryBin) {
        return inventoryBinMapper.update(inventoryBin);
    }

    @Override
    public int removeById(Long id) {
        return inventoryBinMapper.deleteById(id);
    }

    private long generateId() {
        long now = System.currentTimeMillis();
        int r = ThreadLocalRandom.current().nextInt(1000);
        return now * 1000 + r;
    }
}
