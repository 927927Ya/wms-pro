package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.entity.OutboundPickingTask;
import com.d0dd.wms.service.OutboundPickingTaskService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/outbound/picking/task")
public class OutboundPickingTaskController {

    @Autowired
    private OutboundPickingTaskService outboundPickingTaskService;

    @GetMapping("/list")
    public Result<Map<String, Object>> list(OutboundPickingTask outboundPickingTask) {
        List<OutboundPickingTask> list = outboundPickingTaskService.list(outboundPickingTask);
        Map<String, Object> result = new HashMap<>();
        result.put("records", list);
        result.put("total", list.size());
        return Result.success(result);
    }
    
    @GetMapping("/details/{outboundDetailsId}")
    public Result<Map<String, Object>> listByOutboundDetailsId(@PathVariable Long outboundDetailsId) {

        List<OutboundPickingTask> list = outboundPickingTaskService.getByOutboundDetailsId(outboundDetailsId);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }

    @GetMapping("/{id}")
    public Result<OutboundPickingTask> getById(@PathVariable Long id) {
        return Result.success(outboundPickingTaskService.getById(id));
    }

    @RequiresPermissions("outbound:manage")
    @PostMapping
    public Result<String> save(@RequestBody OutboundPickingTask outboundPickingTask) {
        outboundPickingTaskService.save(outboundPickingTask);
        return Result.success("Created successfully");
    }

    @RequiresPermissions("outbound:manage")
    @PutMapping
    public Result<String> update(@RequestBody OutboundPickingTask outboundPickingTask) {
        outboundPickingTaskService.update(outboundPickingTask);
        return Result.success("Updated successfully");
    }

    @RequiresPermissions("outbound:manage")
    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        outboundPickingTaskService.removeById(id);
        return Result.success("Deleted successfully");
    }
    
    @RequiresPermissions("outbound:manage")
    @PostMapping("/generate/{outboundId}")
    public Result<String> generateTasks(@PathVariable Long outboundId) {
        outboundPickingTaskService.generateTasks(outboundId);
        return Result.success("Tasks generated successfully");
    }

    @RequiresPermissions("outbound:manage")
    @PostMapping("/save/{outboundId}")
    public Result<String> saveTasks(@PathVariable Long outboundId, @RequestBody List<OutboundPickingTask> tasks) {
        outboundPickingTaskService.saveTasks(outboundId, tasks);
        return Result.success("Tasks saved successfully");
    }

    @RequiresPermissions("outbound:pick")
    @PostMapping("/complete/{id}")
    public Result<String> completeTask(@PathVariable Long id, @RequestParam Long binId) {
        outboundPickingTaskService.completeTask(id, binId);
        return Result.success("Task completed successfully");
    }
    
    @RequiresPermissions("outbound:pick")
    @PostMapping("/cancel/{id}")
    public Result<String> cancelTask(@PathVariable Long id) {
        outboundPickingTaskService.cancelTask(id);
        return Result.success("Task cancelled successfully");
    }
}
