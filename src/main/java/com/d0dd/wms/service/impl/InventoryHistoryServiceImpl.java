package com.d0dd.wms.service.impl;

import com.d0dd.wms.entity.InventoryHistory;
import com.d0dd.wms.mapper.InventoryHistoryMapper;
import com.d0dd.wms.service.InventoryHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryHistoryServiceImpl implements InventoryHistoryService {

    @Autowired
    private InventoryHistoryMapper inventoryHistoryMapper;

    @Override
    public InventoryHistory getById(Long id) {
        return inventoryHistoryMapper.selectById(id);
    }

    @Override
    public List<InventoryHistory> list(InventoryHistory inventoryHistory) {
        return inventoryHistoryMapper.selectList(inventoryHistory);
    }

    @Override
    public int save(InventoryHistory inventoryHistory) {
        return inventoryHistoryMapper.insert(inventoryHistory);
    }

    @Override
    public int update(InventoryHistory inventoryHistory) {
        return inventoryHistoryMapper.update(inventoryHistory);
    }

    @Override
    public int removeById(Long id) {
        return inventoryHistoryMapper.deleteById(id);
    }
}
