package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.entity.SysDict;
import com.d0dd.wms.service.SysDictService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/dict")
public class SysDictController {

    @Autowired
    private SysDictService sysDictService;

    @GetMapping("/list")
    public Result<Map<String, Object>> list(SysDict sysDict) {

        List<SysDict> list = sysDictService.list(sysDict);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }

    @GetMapping("/{id}")
    public Result<SysDict> getById(@PathVariable Long id) {
        return Result.success(sysDictService.getById(id));
    }

    @RequiresPermissions("sys:dict:manage")


    @PostMapping
    public Result<String> save(@RequestBody SysDict sysDict) {
        sysDictService.save(sysDict);
        return Result.success("Created successfully");
    }

    @RequiresPermissions("sys:dict:manage")


    @PutMapping
    public Result<String> update(@RequestBody SysDict sysDict) {
        sysDictService.update(sysDict);
        return Result.success("Updated successfully");
    }

    @RequiresPermissions("sys:dict:manage")


    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        sysDictService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
