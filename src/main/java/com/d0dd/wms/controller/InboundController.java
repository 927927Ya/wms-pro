package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.dto.InboundDTO;
import com.d0dd.wms.dto.InboundQueryDto;
import com.d0dd.wms.entity.Inbound;
import com.d0dd.wms.service.InboundService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inbound")
public class InboundController {

    @Autowired
    private InboundService inboundService;

    @GetMapping("/list")
    public Result<Map<String, Object>> list(Inbound inbound) {
        List<Inbound> list = inboundService.list(inbound);
        Map<String, Object> result = new HashMap<>();
        result.put("records", list);
        result.put("total", list.size());
        return Result.success(result);
    }
    
    @GetMapping("/search")
    public Result<Map<String, Object>> search(InboundQueryDto queryDto) {

        List<Inbound> list = inboundService.list(queryDto);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }

    @GetMapping("/{id}")
    public Result<Inbound> getById(@PathVariable Long id) {
        return Result.success(inboundService.getById(id));
    }

    @RequiresPermissions("inbound:manage")


    @PostMapping
    public Result<String> save(@RequestBody InboundDTO inboundDTO) {
        inboundService.createInbound(inboundDTO);
        return Result.success("Created successfully");
    }

    @RequiresPermissions("inbound:manage")


    @PutMapping
    public Result<String> update(@RequestBody Inbound inbound) {
        inboundService.update(inbound);
        return Result.success("Updated successfully");
    }

    @RequiresPermissions("inbound:manage")


    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        inboundService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
