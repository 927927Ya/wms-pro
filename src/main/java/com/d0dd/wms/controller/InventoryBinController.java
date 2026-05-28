package com.d0dd.wms.controller;

import com.alibaba.excel.EasyExcel;
import com.d0dd.wms.common.Result;
import com.d0dd.wms.dto.StorageQueryDto;
import com.d0dd.wms.entity.InventoryBin;
import com.d0dd.wms.service.InventoryBinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/inventory/bin")
public class InventoryBinController {

    @Autowired
    private InventoryBinService inventoryBinService;

    @GetMapping("/list")
    public Result<List<InventoryBin>> list(InventoryBin inventoryBin) {
        return Result.success(inventoryBinService.list(inventoryBin));
    }
    
    @GetMapping("/search")
    public Result<List<InventoryBin>> search(StorageQueryDto queryDto) {
        return Result.success(inventoryBinService.list(queryDto));
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
    public Result<List<InventoryBin>> listByBatchId(@PathVariable Long batchId) {
        return Result.success(inventoryBinService.getByBatchId(batchId));
    }

    @GetMapping("/{id}")
    public Result<InventoryBin> getById(@PathVariable Long id) {
        return Result.success(inventoryBinService.getById(id));
    }

    @PostMapping
    public Result<String> save(@RequestBody InventoryBin inventoryBin) {
        inventoryBinService.save(inventoryBin);
        return Result.success("Created successfully");
    }

    @PutMapping
    public Result<String> update(@RequestBody InventoryBin inventoryBin) {
        inventoryBinService.update(inventoryBin);
        return Result.success("Updated successfully");
    }

    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        inventoryBinService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
