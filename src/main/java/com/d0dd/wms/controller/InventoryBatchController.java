package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.dto.StorageQueryDto;
import com.d0dd.wms.entity.InventoryBatch;
import com.d0dd.wms.service.InventoryBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory/batch")
public class InventoryBatchController {

    @Autowired
    private InventoryBatchService inventoryBatchService;

    @GetMapping("/list")
    public Result<List<InventoryBatch>> list(InventoryBatch inventoryBatch) {
        return Result.success(inventoryBatchService.list(inventoryBatch));
    }
    
    @GetMapping("/search")
    public Result<List<InventoryBatch>> search(StorageQueryDto queryDto) {
        return Result.success(inventoryBatchService.list(queryDto));
    }
    
    @GetMapping("/sku/{skuId}")
    public Result<List<InventoryBatch>> listBySkuId(@PathVariable Long skuId) {
        return Result.success(inventoryBatchService.getBySkuId(skuId));
    }

    @GetMapping("/{id}")
    public Result<InventoryBatch> getById(@PathVariable Long id) {
        return Result.success(inventoryBatchService.getById(id));
    }

    @PostMapping
    public Result<String> save(@RequestBody InventoryBatch inventoryBatch) {
        inventoryBatchService.save(inventoryBatch);
        return Result.success("Created successfully");
    }

    @PutMapping
    public Result<String> update(@RequestBody InventoryBatch inventoryBatch) {
        inventoryBatchService.update(inventoryBatch);
        return Result.success("Updated successfully");
    }

    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        inventoryBatchService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
