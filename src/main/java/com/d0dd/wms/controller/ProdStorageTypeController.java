package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.entity.ProdStorageType;
import com.d0dd.wms.service.ProdStorageTypeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/prod/storageType")
public class ProdStorageTypeController {

    @Autowired
    private ProdStorageTypeService prodStorageTypeService;

    @GetMapping("/list")
    public Result<Map<String, Object>> list(ProdStorageType prodStorageType) {

        List<ProdStorageType> list = prodStorageTypeService.list(prodStorageType);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }

    @GetMapping("/{id}")
    public Result<ProdStorageType> getById(@PathVariable Long id) {
        return Result.success(prodStorageTypeService.getById(id));
    }

    @RequiresPermissions("prod:manage")


    @PostMapping
    public Result<String> save(@RequestBody ProdStorageType prodStorageType) {
        prodStorageTypeService.save(prodStorageType);
        return Result.success("Created successfully");
    }

    @RequiresPermissions("prod:manage")


    @PutMapping
    public Result<String> update(@RequestBody ProdStorageType prodStorageType) {
        prodStorageTypeService.update(prodStorageType);
        return Result.success("Updated successfully");
    }

    @RequiresPermissions("prod:manage")


    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        prodStorageTypeService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
