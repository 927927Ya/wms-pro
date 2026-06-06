package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.entity.WarehouseRack;
import com.d0dd.wms.service.WarehouseRackService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/warehouse/rack")
public class WarehouseRackController {

    @Autowired
    private WarehouseRackService warehouseRackService;

    @GetMapping("/list")
    public Result<Map<String, Object>> list(WarehouseRack warehouseRack) {

        List<WarehouseRack> list = warehouseRackService.list(warehouseRack);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }

    @GetMapping("/{id}")
    public Result<WarehouseRack> getById(@PathVariable Long id) {
        return Result.success(warehouseRackService.getById(id));
    }

    @RequiresPermissions("warehouse:manage")


    @PostMapping
    public Result<String> save(@RequestBody WarehouseRack warehouseRack) {
        warehouseRackService.save(warehouseRack);
        return Result.success("Created successfully");
    }

    @RequiresPermissions("warehouse:manage")


    @PutMapping
    public Result<String> update(@RequestBody WarehouseRack warehouseRack) {
        warehouseRackService.update(warehouseRack);
        return Result.success("Updated successfully");
    }

    @RequiresPermissions("warehouse:manage")


    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        warehouseRackService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
