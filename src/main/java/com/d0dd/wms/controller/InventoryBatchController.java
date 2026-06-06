package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.dto.StorageQueryDto;
import com.d0dd.wms.entity.InventoryBatch;
import com.d0dd.wms.service.InventoryBatchService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventory/batch")
public class InventoryBatchController {

    @Autowired
    private InventoryBatchService inventoryBatchService;

    @GetMapping("/list")
    public Result<Map<String, Object>> list(InventoryBatch inventoryBatch) {

        List<InventoryBatch> list = inventoryBatchService.list(inventoryBatch);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }
    
    @GetMapping("/search")
    public Result<Map<String, Object>> search(StorageQueryDto queryDto) {

        List<InventoryBatch> list = inventoryBatchService.list(queryDto);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }
    
    @GetMapping("/sku/{skuId}")
    public Result<Map<String, Object>> listBySkuId(@PathVariable Long skuId) {

        List<InventoryBatch> list = inventoryBatchService.getBySkuId(skuId);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }

    @GetMapping("/{id}")
    public Result<InventoryBatch> getById(@PathVariable Long id) {
        return Result.success(inventoryBatchService.getById(id));
    }

    @RequiresPermissions("inventory:manage")


    @PostMapping
    public Result<String> save(@RequestBody InventoryBatch inventoryBatch) {
        inventoryBatchService.save(inventoryBatch);
        return Result.success("Created successfully");
    }

    @RequiresPermissions("inventory:manage")


    @PutMapping
    public Result<String> update(@RequestBody InventoryBatch inventoryBatch) {
        inventoryBatchService.update(inventoryBatch);
        return Result.success("Updated successfully");
    }

    @RequiresPermissions("inventory:manage")


    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        inventoryBatchService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
