package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.dto.InboundDTO;
import com.d0dd.wms.dto.InboundQueryDto;
import com.d0dd.wms.entity.Inbound;
import com.d0dd.wms.service.InboundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inbound")
public class InboundController {

    @Autowired
    private InboundService inboundService;

    @GetMapping("/list")
    public Result<List<Inbound>> list(Inbound inbound) {
        return Result.success(inboundService.list(inbound));
    }
    
    @GetMapping("/search")
    public Result<List<Inbound>> search(InboundQueryDto queryDto) {
        return Result.success(inboundService.list(queryDto));
    }

    @GetMapping("/{id}")
    public Result<Inbound> getById(@PathVariable Long id) {
        return Result.success(inboundService.getById(id));
    }

    @PostMapping
    public Result<String> save(@RequestBody InboundDTO inboundDTO) {
        inboundService.createInbound(inboundDTO);
        return Result.success("Created successfully");
    }

    @PutMapping
    public Result<String> update(@RequestBody Inbound inbound) {
        inboundService.update(inbound);
        return Result.success("Updated successfully");
    }

    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        inboundService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
