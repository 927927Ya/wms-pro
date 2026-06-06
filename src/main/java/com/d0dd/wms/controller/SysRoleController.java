package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.dto.SysRoleQueryDto;
import com.d0dd.wms.entity.SysRole;
import com.d0dd.wms.service.SysRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @PostMapping("/list")
    public Result<Map<String, Object>> list(@RequestBody SysRoleQueryDto queryDto) {

        List<SysRole> list = sysRoleService.list(queryDto);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }

    @GetMapping("/{roleId}")
    public Result<SysRole> getInfo(@PathVariable Long roleId) {
        return Result.success(sysRoleService.getById(roleId));
    }

    @RequiresPermissions("sys:role:manage")


    @PostMapping
    public Result<Integer> add(@RequestBody SysRole sysRole) {
        return Result.success(sysRoleService.insert(sysRole));
    }

    @RequiresPermissions("sys:role:manage")


    @PutMapping
    public Result<Integer> edit(@RequestBody SysRole sysRole) {
        return Result.success(sysRoleService.update(sysRole));
    }

    @RequiresPermissions("sys:role:manage")


    @DeleteMapping("/{roleId}")
    public Result<Integer> remove(@PathVariable Long roleId) {
        return Result.success(sysRoleService.deleteById(roleId));
    }
}
