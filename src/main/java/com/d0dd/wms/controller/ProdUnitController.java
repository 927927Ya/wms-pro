package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.entity.SysDict;
import com.d0dd.wms.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/prod/unit")
public class ProdUnitController {

    @Autowired
    private SysDictService sysDictService;

    // Supported unit types
    private static final List<String> UNIT_TYPES = Arrays.asList("storage_unit", "weight_unit", "volume_unit");

    @GetMapping("/list")
    public Result<List<SysDict>> list(SysDict sysDict) {
        // If no type specified, maybe return all units? 
        // Or if frontend passes type, we just pass it through.
        // Let's ensure we only return unit-related dicts if no type specified.
        List<SysDict> list = sysDictService.list(sysDict);
        if (sysDict.getDictType() == null || sysDict.getDictType().isEmpty()) {
             list = list.stream()
                     .filter(d -> UNIT_TYPES.contains(d.getDictType()))
                     .collect(Collectors.toList());
        }
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<SysDict> getById(@PathVariable Long id) {
        return Result.success(sysDictService.getById(id));
    }

    @PostMapping
    public Result<String> save(@RequestBody SysDict sysDict) {
        if (!UNIT_TYPES.contains(sysDict.getDictType())) {
            return Result.error("Invalid unit type. Must be one of: " + UNIT_TYPES);
        }
        sysDictService.save(sysDict);
        return Result.success("Created successfully");
    }

    @PutMapping
    public Result<String> update(@RequestBody SysDict sysDict) {
        // Validation could be added here to ensure we don't change type to non-unit type
        sysDictService.update(sysDict);
        return Result.success("Updated successfully");
    }

    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        sysDictService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
