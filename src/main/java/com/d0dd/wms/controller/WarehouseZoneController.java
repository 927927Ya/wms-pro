package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.entity.WarehouseZone;
import com.d0dd.wms.service.WarehouseZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warehouse/zone")
public class WarehouseZoneController {

    @Autowired
    private WarehouseZoneService warehouseZoneService;

    @GetMapping("/list")
    public Result<List<WarehouseZone>> list(WarehouseZone warehouseZone) {
        return Result.success(warehouseZoneService.list(warehouseZone));
    }

    @GetMapping("/{id}")
    public Result<WarehouseZone> getById(@PathVariable Long id) {
        return Result.success(warehouseZoneService.getById(id));
    }

    @PostMapping
    public Result<String> save(@RequestBody WarehouseZone warehouseZone) {
        warehouseZoneService.save(warehouseZone);
        return Result.success("Created successfully");
    }

    @PutMapping
    public Result<String> update(@RequestBody WarehouseZone warehouseZone) {
        warehouseZoneService.update(warehouseZone);
        return Result.success("Updated successfully");
    }

    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        warehouseZoneService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
