package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.entity.ProdCategory;
import com.d0dd.wms.service.ProdCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prod/category")
public class ProdCategoryController {

    @Autowired
    private ProdCategoryService prodCategoryService;

    @GetMapping("/list")
    public Result<List<ProdCategory>> list(ProdCategory prodCategory) {
        return Result.success(prodCategoryService.list(prodCategory));
    }

    @GetMapping("/{id}")
    public Result<ProdCategory> getById(@PathVariable Long id) {
        return Result.success(prodCategoryService.getById(id));
    }

    @PostMapping
    public Result<String> save(@RequestBody ProdCategory prodCategory) {
        prodCategoryService.save(prodCategory);
        return Result.success("Created successfully");
    }

    @PutMapping
    public Result<String> update(@RequestBody ProdCategory prodCategory) {
        prodCategoryService.update(prodCategory);
        return Result.success("Updated successfully");
    }

    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        prodCategoryService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
