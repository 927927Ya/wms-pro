package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.entity.WarehouseRack;
import com.d0dd.wms.service.WarehouseRackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warehouse/rack")
public class WarehouseRackController {

    @Autowired
    private WarehouseRackService warehouseRackService;

    @GetMapping("/list")
    public Result<List<WarehouseRack>> list(WarehouseRack warehouseRack) {
        return Result.success(warehouseRackService.list(warehouseRack));
    }

    @GetMapping("/{id}")
    public Result<WarehouseRack> getById(@PathVariable Long id) {
        return Result.success(warehouseRackService.getById(id));
    }

    @PostMapping
    public Result<String> save(@RequestBody WarehouseRack warehouseRack) {
        warehouseRackService.save(warehouseRack);
        return Result.success("Created successfully");
    }

    @PutMapping
    public Result<String> update(@RequestBody WarehouseRack warehouseRack) {
        warehouseRackService.update(warehouseRack);
        return Result.success("Updated successfully");
    }

    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        warehouseRackService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
