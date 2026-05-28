package com.d0dd.wms.service.impl;

import com.d0dd.wms.dto.StorageQueryDto;
import com.d0dd.wms.entity.InventorySummary;
import com.d0dd.wms.mapper.InventorySummaryMapper;
import com.d0dd.wms.service.InventorySummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventorySummaryServiceImpl implements InventorySummaryService {

    @Autowired
    private InventorySummaryMapper inventorySummaryMapper;

    @Override
    public InventorySummary getById(Long id) {
        return inventorySummaryMapper.selectById(id);
    }

    @Override
    public List<InventorySummary> list(InventorySummary inventorySummary) {
        return inventorySummaryMapper.selectList(inventorySummary);
    }
    
    @Override
    public List<InventorySummary> list(StorageQueryDto queryDto) {
        return inventorySummaryMapper.selectListByDto(queryDto);
    }

    @Override
    public InventorySummary getBySkuId(Long skuId) {
        return inventorySummaryMapper.selectBySkuId(skuId);
    }

    @Override
    public int save(InventorySummary inventorySummary) {
        return inventorySummaryMapper.insert(inventorySummary);
    }

    @Override
    public int update(InventorySummary inventorySummary) {
        return inventorySummaryMapper.update(inventorySummary);
    }

    @Override
    public int removeById(Long id) {
        return inventorySummaryMapper.deleteById(id);
    }
}
