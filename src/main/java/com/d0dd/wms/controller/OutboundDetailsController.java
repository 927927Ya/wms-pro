package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.entity.OutboundDetails;
import com.d0dd.wms.service.OutboundDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/outbound/details")
public class OutboundDetailsController {

    @Autowired
    private OutboundDetailsService outboundDetailsService;

    @GetMapping("/list")
    public Result<List<OutboundDetails>> list(OutboundDetails outboundDetails) {
        return Result.success(outboundDetailsService.list(outboundDetails));
    }
    
    @GetMapping("/outbound/{outboundId}")
    public Result<List<OutboundDetails>> listByOutboundId(@PathVariable Long outboundId) {
        return Result.success(outboundDetailsService.getByOutboundId(outboundId));
    }

    @GetMapping("/{id}")
    public Result<OutboundDetails> getById(@PathVariable Long id) {
        return Result.success(outboundDetailsService.getById(id));
    }

    @PostMapping
    public Result<String> save(@RequestBody OutboundDetails outboundDetails) {
        outboundDetailsService.save(outboundDetails);
        return Result.success("Created successfully");
    }

    @PutMapping
    public Result<String> update(@RequestBody OutboundDetails outboundDetails) {
        outboundDetailsService.update(outboundDetails);
        return Result.success("Updated successfully");
    }

    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        outboundDetailsService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
