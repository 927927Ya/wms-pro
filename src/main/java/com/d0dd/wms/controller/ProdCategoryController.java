package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.entity.ProdCategory;
import com.d0dd.wms.service.ProdCategoryService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/prod/category")
public class ProdCategoryController {

    @Autowired
    private ProdCategoryService prodCategoryService;

    @GetMapping("/list")
    public Result<Map<String, Object>> list(ProdCategory prodCategory) {

        List<ProdCategory> list = prodCategoryService.list(prodCategory);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }

    @GetMapping("/{id}")
    public Result<ProdCategory> getById(@PathVariable Long id) {
        return Result.success(prodCategoryService.getById(id));
    }

    @RequiresPermissions("prod:manage")


    @PostMapping
    public Result<String> save(@RequestBody ProdCategory prodCategory) {
        prodCategoryService.save(prodCategory);
        return Result.success("Created successfully");
    }

    @RequiresPermissions("prod:manage")


    @PutMapping
    public Result<String> update(@RequestBody ProdCategory prodCategory) {
        prodCategoryService.update(prodCategory);
        return Result.success("Updated successfully");
    }

    @RequiresPermissions("prod:manage")


    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        prodCategoryService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
