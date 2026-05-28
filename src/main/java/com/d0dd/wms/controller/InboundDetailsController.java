package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.entity.InboundDetails;
import com.d0dd.wms.service.InboundDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inbound/details")
public class InboundDetailsController {

    @Autowired
    private InboundDetailsService inboundDetailsService;

    @GetMapping("/list")
    public Result<List<InboundDetails>> list(InboundDetails inboundDetails) {
        return Result.success(inboundDetailsService.list(inboundDetails));
    }
    
    @GetMapping("/inbound/{inboundId}")
    public Result<List<InboundDetails>> listByInboundId(@PathVariable Long inboundId) {
        return Result.success(inboundDetailsService.getByInboundId(inboundId));
    }

    @GetMapping("/{id}")
    public Result<InboundDetails> getById(@PathVariable Long id) {
        return Result.success(inboundDetailsService.getById(id));
    }

    @PostMapping
    public Result<String> save(@RequestBody InboundDetails inboundDetails) {
        inboundDetailsService.save(inboundDetails);
        return Result.success("Created successfully");
    }

    @PutMapping
    public Result<String> update(@RequestBody InboundDetails inboundDetails) {
        inboundDetailsService.update(inboundDetails);
        return Result.success("Updated successfully");
    }

    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        inboundDetailsService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
