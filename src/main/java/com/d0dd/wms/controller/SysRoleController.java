package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.dto.SysRoleQueryDto;
import com.d0dd.wms.entity.SysRole;
import com.d0dd.wms.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @PostMapping("/list")
    public Result<List<SysRole>> list(@RequestBody SysRoleQueryDto queryDto) {
        return Result.success(sysRoleService.list(queryDto));
    }

    @GetMapping("/{roleId}")
    public Result<SysRole> getInfo(@PathVariable Long roleId) {
        return Result.success(sysRoleService.getById(roleId));
    }

    @PostMapping
    public Result<Integer> add(@RequestBody SysRole sysRole) {
        return Result.success(sysRoleService.insert(sysRole));
    }

    @PutMapping
    public Result<Integer> edit(@RequestBody SysRole sysRole) {
        return Result.success(sysRoleService.update(sysRole));
    }

    @DeleteMapping("/{roleId}")
    public Result<Integer> remove(@PathVariable Long roleId) {
        return Result.success(sysRoleService.deleteById(roleId));
    }
}
