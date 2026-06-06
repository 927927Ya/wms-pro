package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.entity.InboundDetails;
import com.d0dd.wms.service.InboundDetailsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inbound/details")
public class InboundDetailsController {

    @Autowired
    private InboundDetailsService inboundDetailsService;

    @GetMapping("/list")
    public Result<Map<String, Object>> list(InboundDetails inboundDetails) {

        List<InboundDetails> list = inboundDetailsService.list(inboundDetails);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }
    
    @GetMapping("/inbound/{inboundId}")
    public Result<Map<String, Object>> listByInboundId(@PathVariable Long inboundId) {

        List<InboundDetails> list = inboundDetailsService.getByInboundId(inboundId);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }

    @GetMapping("/{id}")
    public Result<InboundDetails> getById(@PathVariable Long id) {
        return Result.success(inboundDetailsService.getById(id));
    }

    @RequiresPermissions("inbound:manage")


    @PostMapping
    public Result<String> save(@RequestBody InboundDetails inboundDetails) {
        inboundDetailsService.save(inboundDetails);
        return Result.success("Created successfully");
    }

    @RequiresPermissions("inbound:manage")


    @PutMapping
    public Result<String> update(@RequestBody InboundDetails inboundDetails) {
        inboundDetailsService.update(inboundDetails);
        return Result.success("Updated successfully");
    }

    @RequiresPermissions("inbound:manage")


    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        inboundDetailsService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
