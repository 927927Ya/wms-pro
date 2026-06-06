package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.entity.Warehouse;
import com.d0dd.wms.service.WarehouseService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.WebDataBinder;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/warehouse")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @GetMapping("/list")
    public Result<Map<String, Object>> list(Warehouse warehouse) {

        List<Warehouse> list = warehouseService.list(warehouse);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }

    @GetMapping("/{id}")
    public Result<Warehouse> getById(@PathVariable Long id) {
        return Result.success(warehouseService.getById(id));
    }

    @RequiresPermissions("warehouse:manage")


    @PostMapping
    public Result<String> save(@RequestBody Warehouse warehouse) {
        warehouseService.save(warehouse);
        return Result.success("Created successfully");
    }

    @RequiresPermissions("warehouse:manage")


    @PutMapping
    public Result<String> update(@RequestBody Warehouse warehouse) {
        warehouseService.update(warehouse);
        return Result.success("Updated successfully");
    }

    @RequiresPermissions("warehouse:manage")


    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        warehouseService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
