package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.dto.OutboundDTO;
import com.d0dd.wms.dto.OutboundOrderQueryDto;
import com.d0dd.wms.dto.ProductQueryDto;
import com.d0dd.wms.entity.InboundDetails;
import com.d0dd.wms.entity.Outbound;
import com.d0dd.wms.mapper.InboundDetailsMapper;
import com.d0dd.wms.service.OutboundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/outbound")
public class OutboundController {

    @Autowired
    private OutboundService outboundService;

    @Autowired
    private InboundDetailsMapper inboundDetailsMapper;

    @GetMapping("/list")
    public Result<List<Outbound>> list(OutboundOrderQueryDto queryDto) {
        return Result.success(outboundService.list(queryDto));
    }
    
    @GetMapping("/search")
    public Result<List<Outbound>> search(OutboundOrderQueryDto queryDto) {
        return Result.success(outboundService.list(queryDto));
    }

    @GetMapping("/{id}")
    public Result<Outbound> getById(@PathVariable Long id) {
        return Result.success(outboundService.getById(id));
    }

    @PostMapping
    public Result<String> save(@RequestBody OutboundDTO outboundDTO) {
        outboundService.createOutbound(outboundDTO);
        return Result.success("Created successfully");
    }

    @GetMapping("/available-sku/list")
    public Result<List<InboundDetails>> listAvailableSku(ProductQueryDto queryDto) {
        return Result.success(inboundDetailsMapper.selectSkuSummaryForOutbound(queryDto));
    }

    @PutMapping
    public Result<String> update(@RequestBody Outbound outbound) {
        outboundService.update(outbound);
        return Result.success("Updated successfully");
    }

    @PutMapping("/dto")
    public Result<String> updateDto(@RequestBody OutboundDTO outboundDTO) {
        outboundService.updateOutbound(outboundDTO);
        return Result.success("Updated successfully");
    }

    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        outboundService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
