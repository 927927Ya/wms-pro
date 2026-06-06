package com.d0dd.wms.controller;

import com.alibaba.excel.EasyExcel;
import com.d0dd.wms.common.Result;
import com.d0dd.wms.dto.StorageQueryDto;
import com.d0dd.wms.entity.InventoryBin;
import com.d0dd.wms.service.InventoryBinService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventory/bin")
public class InventoryBinController {

    @Autowired
    private InventoryBinService inventoryBinService;

    @GetMapping("/list")
    public Result<Map<String, Object>> list(InventoryBin inventoryBin) {

        List<InventoryBin> list = inventoryBinService.list(inventoryBin);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }
    
    @GetMapping("/search")
    public Result<Map<String, Object>> search(StorageQueryDto queryDto) {

        List<InventoryBin> list = inventoryBinService.list(queryDto);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }
    
    @GetMapping("/export")
    public void export(HttpServletResponse response, StorageQueryDto queryDto) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("Inventory_Details", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        
        List<InventoryBin> list = inventoryBinService.list(queryDto);
        EasyExcel.write(response.getOutputStream(), InventoryBin.class).sheet("Inventory").doWrite(list);
    }
    
    @GetMapping("/batch/{batchId}")
    public Result<Map<String, Object>> listByBatchId(@PathVariable Long batchId) {

        List<InventoryBin> list = inventoryBinService.getByBatchId(batchId);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }

    @GetMapping("/{id}")
    public Result<InventoryBin> getById(@PathVariable Long id) {
        return Result.success(inventoryBinService.getById(id));
    }

    @RequiresPermissions("inventory:manage")


    @PostMapping
    public Result<String> save(@RequestBody InventoryBin inventoryBin) {
        inventoryBinService.save(inventoryBin);
        return Result.success("Created successfully");
    }

    @RequiresPermissions("inventory:manage")


    @PutMapping
    public Result<String> update(@RequestBody InventoryBin inventoryBin) {
        inventoryBinService.update(inventoryBin);
        return Result.success("Updated successfully");
    }

    @RequiresPermissions("inventory:manage")


    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        inventoryBinService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
