package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.entity.InventoryHistory;
import com.d0dd.wms.service.InventoryHistoryService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventory/history")
public class InventoryHistoryController {

    @Autowired
    private InventoryHistoryService inventoryHistoryService;

    @GetMapping("/list")
    public Result<Map<String, Object>> list(InventoryHistory inventoryHistory) {

        List<InventoryHistory> list = inventoryHistoryService.list(inventoryHistory);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }

    @GetMapping("/{id}")
    public Result<InventoryHistory> getById(@PathVariable Long id) {
        return Result.success(inventoryHistoryService.getById(id));
    }

    @RequiresPermissions("inventory:manage")


    @PostMapping
    public Result<String> save(@RequestBody InventoryHistory inventoryHistory) {
        inventoryHistoryService.save(inventoryHistory);
        return Result.success("Created successfully");
    }

    @RequiresPermissions("inventory:manage")


    @PutMapping
    public Result<String> update(@RequestBody InventoryHistory inventoryHistory) {
        inventoryHistoryService.update(inventoryHistory);
        return Result.success("Updated successfully");
    }

    @RequiresPermissions("inventory:manage")


    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        inventoryHistoryService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
