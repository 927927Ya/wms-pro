package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.entity.SysDict;
import com.d0dd.wms.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys/dict")
public class SysDictController {

    @Autowired
    private SysDictService sysDictService;

    @GetMapping("/list")
    public Result<List<SysDict>> list(SysDict sysDict) {
        return Result.success(sysDictService.list(sysDict));
    }

    @GetMapping("/{id}")
    public Result<SysDict> getById(@PathVariable Long id) {
        return Result.success(sysDictService.getById(id));
    }

    @PostMapping
    public Result<String> save(@RequestBody SysDict sysDict) {
        sysDictService.save(sysDict);
        return Result.success("Created successfully");
    }

    @PutMapping
    public Result<String> update(@RequestBody SysDict sysDict) {
        sysDictService.update(sysDict);
        return Result.success("Updated successfully");
    }

    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        sysDictService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
