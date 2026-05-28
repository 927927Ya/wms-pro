package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.entity.ProdStorageType;
import com.d0dd.wms.service.ProdStorageTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prod/storageType")
public class ProdStorageTypeController {

    @Autowired
    private ProdStorageTypeService prodStorageTypeService;

    @GetMapping("/list")
    public Result<List<ProdStorageType>> list(ProdStorageType prodStorageType) {
        return Result.success(prodStorageTypeService.list(prodStorageType));
    }

    @GetMapping("/{id}")
    public Result<ProdStorageType> getById(@PathVariable Long id) {
        return Result.success(prodStorageTypeService.getById(id));
    }

    @PostMapping
    public Result<String> save(@RequestBody ProdStorageType prodStorageType) {
        prodStorageTypeService.save(prodStorageType);
        return Result.success("Created successfully");
    }

    @PutMapping
    public Result<String> update(@RequestBody ProdStorageType prodStorageType) {
        prodStorageTypeService.update(prodStorageType);
        return Result.success("Updated successfully");
    }

    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        prodStorageTypeService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
