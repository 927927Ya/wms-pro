package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.entity.InventoryHistory;
import com.d0dd.wms.service.InventoryHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory/history")
public class InventoryHistoryController {

    @Autowired
    private InventoryHistoryService inventoryHistoryService;

    @GetMapping("/list")
    public Result<List<InventoryHistory>> list(InventoryHistory inventoryHistory) {
        return Result.success(inventoryHistoryService.list(inventoryHistory));
    }

    @GetMapping("/{id}")
    public Result<InventoryHistory> getById(@PathVariable Long id) {
        return Result.success(inventoryHistoryService.getById(id));
    }

    @PostMapping
    public Result<String> save(@RequestBody InventoryHistory inventoryHistory) {
        inventoryHistoryService.save(inventoryHistory);
        return Result.success("Created successfully");
    }

    @PutMapping
    public Result<String> update(@RequestBody InventoryHistory inventoryHistory) {
        inventoryHistoryService.update(inventoryHistory);
        return Result.success("Updated successfully");
    }

    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        inventoryHistoryService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
