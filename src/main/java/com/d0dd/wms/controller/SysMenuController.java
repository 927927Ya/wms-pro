package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.dto.SysMenuQueryDto;
import com.d0dd.wms.entity.SysMenu;
import com.d0dd.wms.service.SysMenuService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/menu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @PostMapping("/list")
    public Result<Map<String, Object>> list(@RequestBody SysMenuQueryDto queryDto) {

        List<SysMenu> list = sysMenuService.list(queryDto);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }

    @GetMapping("/{menuId}")
    public Result<SysMenu> getInfo(@PathVariable Long menuId) {
        return Result.success(sysMenuService.getById(menuId));
    }

    @RequiresPermissions("sys:menu:manage")


    @PostMapping
    public Result<Integer> add(@RequestBody SysMenu sysMenu) {
        return Result.success(sysMenuService.insert(sysMenu));
    }

    @RequiresPermissions("sys:menu:manage")


    @PutMapping
    public Result<Integer> edit(@RequestBody SysMenu sysMenu) {
        return Result.success(sysMenuService.update(sysMenu));
    }

    @RequiresPermissions("sys:menu:manage")


    @DeleteMapping("/{menuId}")
    public Result<Integer> remove(@PathVariable Long menuId) {
        return Result.success(sysMenuService.deleteById(menuId));
    }
}
