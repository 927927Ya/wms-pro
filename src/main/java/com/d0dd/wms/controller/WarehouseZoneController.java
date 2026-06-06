package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.entity.WarehouseZone;
import com.d0dd.wms.service.WarehouseZoneService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/warehouse/zone")
public class WarehouseZoneController {

    @Autowired
    private WarehouseZoneService warehouseZoneService;

    @GetMapping("/list")
    public Result<Map<String, Object>> list(WarehouseZone warehouseZone) {

        List<WarehouseZone> list = warehouseZoneService.list(warehouseZone);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }

    @GetMapping("/{id}")
    public Result<WarehouseZone> getById(@PathVariable Long id) {
        return Result.success(warehouseZoneService.getById(id));
    }

    @RequiresPermissions("warehouse:manage")


    @PostMapping
    public Result<String> save(@RequestBody WarehouseZone warehouseZone) {
        warehouseZoneService.save(warehouseZone);
        return Result.success("Created successfully");
    }

    @RequiresPermissions("warehouse:manage")


    @PutMapping
    public Result<String> update(@RequestBody WarehouseZone warehouseZone) {
        warehouseZoneService.update(warehouseZone);
        return Result.success("Updated successfully");
    }

    @RequiresPermissions("warehouse:manage")


    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        warehouseZoneService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
