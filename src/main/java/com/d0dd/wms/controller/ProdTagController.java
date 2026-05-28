package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.entity.ProdTag;
import com.d0dd.wms.service.ProdTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.d0dd.wms.entity.ProdSku;

@RestController
@RequestMapping("/prod/tag")
public class ProdTagController {

    @Autowired
    private ProdTagService prodTagService;

    @GetMapping("/list")
    public Result<List<ProdTag>> list(ProdTag prodTag) {
        return Result.success(prodTagService.list(prodTag));
    }
    
    @GetMapping("/{id}/products")
    public Result<List<ProdSku>> listProducts(@PathVariable Long id) {
        return Result.success(prodTagService.getSkusByTagId(id));
    }

    @GetMapping("/{id}")
    public Result<ProdTag> getById(@PathVariable Long id) {
        return Result.success(prodTagService.getById(id));
    }

    @PostMapping
    public Result<String> save(@RequestBody ProdTag prodTag) {
        prodTagService.save(prodTag);
        return Result.success("Created successfully");
    }

    @PutMapping
    public Result<String> update(@RequestBody ProdTag prodTag) {
        prodTagService.update(prodTag);
        return Result.success("Updated successfully");
    }

    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        prodTagService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
