package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.dto.WarehouseBinBatchDTO;
import com.d0dd.wms.entity.WarehouseBin;
import com.d0dd.wms.service.WarehouseBinService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/warehouse/bin")
public class WarehouseBinController {

    @Autowired
    private WarehouseBinService warehouseBinService;

    @GetMapping("/list")
    public Result<Map<String, Object>> list(WarehouseBin warehouseBin) {

        List<WarehouseBin> list = warehouseBinService.list(warehouseBin);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }

    @GetMapping("/{id}")
    public Result<WarehouseBin> getById(@PathVariable Long id) {
        return Result.success(warehouseBinService.getById(id));
    }

    @RequiresPermissions("warehouse:manage")


    @PostMapping
    public Result<String> save(@RequestBody WarehouseBin warehouseBin) {
        warehouseBinService.save(warehouseBin);
        return Result.success("Created successfully");
    }

    @RequiresPermissions("warehouse:manage")


    @PostMapping("/batch")
    public Result<String> batchSave(@RequestBody WarehouseBinBatchDTO batchDTO) {
        int count = warehouseBinService.batchSave(batchDTO);
        return Result.success("Batch created successfully: " + count + " bins");
    }

    @RequiresPermissions("warehouse:manage")


    @PutMapping
    public Result<String> update(@RequestBody WarehouseBin warehouseBin) {
        warehouseBinService.update(warehouseBin);
        return Result.success("Updated successfully");
    }

    @RequiresPermissions("warehouse:manage")


    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        warehouseBinService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
