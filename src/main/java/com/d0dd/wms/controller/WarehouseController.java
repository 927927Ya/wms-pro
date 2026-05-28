package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.entity.Warehouse;
import com.d0dd.wms.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    public Result<List<Warehouse>> list(Warehouse warehouse) {
        return Result.success(warehouseService.list(warehouse));
    }

    @GetMapping("/{id}")
    public Result<Warehouse> getById(@PathVariable Long id) {
        return Result.success(warehouseService.getById(id));
    }

    @PostMapping
    public Result<String> save(@RequestBody Warehouse warehouse) {
        warehouseService.save(warehouse);
        return Result.success("Created successfully");
    }

    @PutMapping
    public Result<String> update(@RequestBody Warehouse warehouse) {
        warehouseService.update(warehouse);
        return Result.success("Updated successfully");
    }

    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        warehouseService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
