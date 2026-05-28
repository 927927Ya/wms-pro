package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.dto.SysMenuQueryDto;
import com.d0dd.wms.entity.SysMenu;
import com.d0dd.wms.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys/menu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @PostMapping("/list")
    public Result<List<SysMenu>> list(@RequestBody SysMenuQueryDto queryDto) {
        return Result.success(sysMenuService.list(queryDto));
    }

    @GetMapping("/{menuId}")
    public Result<SysMenu> getInfo(@PathVariable Long menuId) {
        return Result.success(sysMenuService.getById(menuId));
    }

    @PostMapping
    public Result<Integer> add(@RequestBody SysMenu sysMenu) {
        return Result.success(sysMenuService.insert(sysMenu));
    }

    @PutMapping
    public Result<Integer> edit(@RequestBody SysMenu sysMenu) {
        return Result.success(sysMenuService.update(sysMenu));
    }

    @DeleteMapping("/{menuId}")
    public Result<Integer> remove(@PathVariable Long menuId) {
        return Result.success(sysMenuService.deleteById(menuId));
    }
}
