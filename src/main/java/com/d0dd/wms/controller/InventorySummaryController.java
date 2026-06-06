package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.dto.StorageQueryDto;
import com.d0dd.wms.entity.InventorySummary;
import com.d0dd.wms.service.InventorySummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventory/summary")
public class InventorySummaryController {

    @Autowired
    private InventorySummaryService inventorySummaryService;

    @GetMapping("/list")
    public Result<Map<String, Object>> list(InventorySummary inventorySummary) {

        List<InventorySummary> list = inventorySummaryService.list(inventorySummary);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }

    @GetMapping("/search")
    public Result<Map<String, Object>> search(StorageQueryDto queryDto) {

        List<InventorySummary> list = inventorySummaryService.list(queryDto);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }

    @GetMapping("/{id}")
    public Result<InventorySummary> getById(@PathVariable Long id) {
        return Result.success(inventorySummaryService.getById(id));
    }
}
