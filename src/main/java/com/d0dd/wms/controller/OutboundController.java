package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.dto.OutboundDTO;
import com.d0dd.wms.dto.OutboundOrderQueryDto;
import com.d0dd.wms.dto.ProductQueryDto;
import com.d0dd.wms.entity.InboundDetails;
import com.d0dd.wms.entity.Outbound;
import com.d0dd.wms.mapper.InboundDetailsMapper;
import com.d0dd.wms.service.OutboundService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/outbound")
public class OutboundController {

    @Autowired
    private OutboundService outboundService;

    @Autowired
    private InboundDetailsMapper inboundDetailsMapper;

    @GetMapping("/list")
    public Result<Map<String, Object>> list(OutboundOrderQueryDto queryDto) {
        List<Outbound> outbounds = outboundService.list(queryDto);
        Map<String, Object> result = new HashMap<>();
        result.put("records", outbounds);
        result.put("total", outbounds.size());
        return Result.success(result);
    }
    
    @GetMapping("/search")
    public Result<Map<String, Object>> search(OutboundOrderQueryDto queryDto) {

        List<Outbound> list = outboundService.list(queryDto);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }

    @GetMapping("/{id}")
    public Result<Outbound> getById(@PathVariable Long id) {
        return Result.success(outboundService.getById(id));
    }

    @RequiresPermissions("outbound:manage")


    @PostMapping
    public Result<String> save(@RequestBody OutboundDTO outboundDTO) {
        outboundService.createOutbound(outboundDTO);
        return Result.success("Created successfully");
    }

    @GetMapping("/available-sku/list")
    public Result<Map<String, Object>> listAvailableSku(ProductQueryDto queryDto) {

        List<InboundDetails> list = inboundDetailsMapper.selectSkuSummaryForOutbound(queryDto);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }

    @RequiresPermissions("outbound:manage")


    @PutMapping
    public Result<String> update(@RequestBody Outbound outbound) {
        outboundService.update(outbound);
        return Result.success("Updated successfully");
    }

    @RequiresPermissions("outbound:manage")


    @PutMapping("/dto")
    public Result<String> updateDto(@RequestBody OutboundDTO outboundDTO) {
        outboundService.updateOutbound(outboundDTO);
        return Result.success("Updated successfully");
    }

    @RequiresPermissions("outbound:manage")


    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        outboundService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
