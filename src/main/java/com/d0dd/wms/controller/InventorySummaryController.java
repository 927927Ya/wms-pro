package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.dto.StorageQueryDto;
import com.d0dd.wms.entity.InventorySummary;
import com.d0dd.wms.service.InventorySummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory/summary")
public class InventorySummaryController {

    @Autowired
    private InventorySummaryService inventorySummaryService;

    @GetMapping("/list")
    public Result<List<InventorySummary>> list(InventorySummary inventorySummary) {
        return Result.success(inventorySummaryService.list(inventorySummary));
    }

    @GetMapping("/search")
    public Result<List<InventorySummary>> search(StorageQueryDto queryDto) {
        return Result.success(inventorySummaryService.list(queryDto));
    }

    @GetMapping("/{id}")
    public Result<InventorySummary> getById(@PathVariable Long id) {
        return Result.success(inventorySummaryService.getById(id));
    }
}
