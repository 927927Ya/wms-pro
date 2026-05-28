package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.dto.WarehouseBinBatchDTO;
import com.d0dd.wms.entity.WarehouseBin;
import com.d0dd.wms.service.WarehouseBinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warehouse/bin")
public class WarehouseBinController {

    @Autowired
    private WarehouseBinService warehouseBinService;

    @GetMapping("/list")
    public Result<List<WarehouseBin>> list(WarehouseBin warehouseBin) {
        return Result.success(warehouseBinService.list(warehouseBin));
    }

    @GetMapping("/{id}")
    public Result<WarehouseBin> getById(@PathVariable Long id) {
        return Result.success(warehouseBinService.getById(id));
    }

    @PostMapping
    public Result<String> save(@RequestBody WarehouseBin warehouseBin) {
        warehouseBinService.save(warehouseBin);
        return Result.success("Created successfully");
    }

    @PostMapping("/batch")
    public Result<String> batchSave(@RequestBody WarehouseBinBatchDTO batchDTO) {
        int count = warehouseBinService.batchSave(batchDTO);
        return Result.success("Batch created successfully: " + count + " bins");
    }

    @PutMapping
    public Result<String> update(@RequestBody WarehouseBin warehouseBin) {
        warehouseBinService.update(warehouseBin);
        return Result.success("Updated successfully");
    }

    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        warehouseBinService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
