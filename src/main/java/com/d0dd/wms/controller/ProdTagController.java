package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.entity.ProdTag;
import com.d0dd.wms.service.ProdTagService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.d0dd.wms.entity.ProdSku;

@RestController
@RequestMapping("/prod/tag")
public class ProdTagController {

    @Autowired
    private ProdTagService prodTagService;

    @GetMapping("/list")
    public Result<Map<String, Object>> list(ProdTag prodTag) {

        List<ProdTag> list = prodTagService.list(prodTag);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }
    
    @GetMapping("/{id}/products")
    public Result<Map<String, Object>> listProducts(@PathVariable Long id) {

        List<ProdSku> list = prodTagService.getSkusByTagId(id);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }

    @GetMapping("/{id}")
    public Result<ProdTag> getById(@PathVariable Long id) {
        return Result.success(prodTagService.getById(id));
    }

    @RequiresPermissions("prod:manage")


    @PostMapping
    public Result<String> save(@RequestBody ProdTag prodTag) {
        prodTagService.save(prodTag);
        return Result.success("Created successfully");
    }

    @RequiresPermissions("prod:manage")


    @PutMapping
    public Result<String> update(@RequestBody ProdTag prodTag) {
        prodTagService.update(prodTag);
        return Result.success("Updated successfully");
    }

    @RequiresPermissions("prod:manage")


    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        prodTagService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
