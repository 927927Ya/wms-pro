package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.entity.OutboundDetails;
import com.d0dd.wms.service.OutboundDetailsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/outbound/details")
public class OutboundDetailsController {

    @Autowired
    private OutboundDetailsService outboundDetailsService;

    @GetMapping("/list")
    public Result<Map<String, Object>> list(OutboundDetails outboundDetails) {

        List<OutboundDetails> list = outboundDetailsService.list(outboundDetails);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }
    
    @GetMapping("/outbound/{outboundId}")
    public Result<Map<String, Object>> listByOutboundId(@PathVariable Long outboundId) {

        List<OutboundDetails> list = outboundDetailsService.getByOutboundId(outboundId);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }

    @GetMapping("/{id}")
    public Result<OutboundDetails> getById(@PathVariable Long id) {
        return Result.success(outboundDetailsService.getById(id));
    }

    @RequiresPermissions("outbound:manage")


    @PostMapping
    public Result<String> save(@RequestBody OutboundDetails outboundDetails) {
        outboundDetailsService.save(outboundDetails);
        return Result.success("Created successfully");
    }

    @RequiresPermissions("outbound:manage")


    @PutMapping
    public Result<String> update(@RequestBody OutboundDetails outboundDetails) {
        outboundDetailsService.update(outboundDetails);
        return Result.success("Updated successfully");
    }

    @RequiresPermissions("outbound:manage")


    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        outboundDetailsService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
