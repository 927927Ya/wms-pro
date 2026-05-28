package com.d0dd.wms.service.impl;

import com.d0dd.wms.dto.StorageQueryDto;
import com.d0dd.wms.entity.InventoryBatch;
import com.d0dd.wms.mapper.InventoryBatchMapper;
import com.d0dd.wms.service.InventoryBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryBatchServiceImpl implements InventoryBatchService {

    @Autowired
    private InventoryBatchMapper inventoryBatchMapper;

    @Override
    public InventoryBatch getById(Long id) {
        return inventoryBatchMapper.selectById(id);
    }

    @Override
    public List<InventoryBatch> list(InventoryBatch inventoryBatch) {
        return inventoryBatchMapper.selectList(inventoryBatch);
    }
    
    @Override
    public List<InventoryBatch> list(StorageQueryDto queryDto) {
        return inventoryBatchMapper.selectListByDto(queryDto);
    }

    @Override
    public List<InventoryBatch> getBySkuId(Long skuId) {
        return inventoryBatchMapper.selectBySkuId(skuId);
    }

    @Override
    public int save(InventoryBatch inventoryBatch) {
        return inventoryBatchMapper.insert(inventoryBatch);
    }

    @Override
    public int update(InventoryBatch inventoryBatch) {
        return inventoryBatchMapper.update(inventoryBatch);
    }

    @Override
    public int removeById(Long id) {
        return inventoryBatchMapper.deleteById(id);
    }
}
