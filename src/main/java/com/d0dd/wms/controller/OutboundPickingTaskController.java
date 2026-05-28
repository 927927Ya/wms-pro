package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.entity.OutboundPickingTask;
import com.d0dd.wms.service.OutboundPickingTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/outbound/picking/task")
public class OutboundPickingTaskController {

    @Autowired
    private OutboundPickingTaskService outboundPickingTaskService;

    @GetMapping("/list")
    public Result<List<OutboundPickingTask>> list(OutboundPickingTask outboundPickingTask) {
        return Result.success(outboundPickingTaskService.list(outboundPickingTask));
    }
    
    @GetMapping("/details/{outboundDetailsId}")
    public Result<List<OutboundPickingTask>> listByOutboundDetailsId(@PathVariable Long outboundDetailsId) {
        return Result.success(outboundPickingTaskService.getByOutboundDetailsId(outboundDetailsId));
    }

    @GetMapping("/{id}")
    public Result<OutboundPickingTask> getById(@PathVariable Long id) {
        return Result.success(outboundPickingTaskService.getById(id));
    }

    @PostMapping
    public Result<String> save(@RequestBody OutboundPickingTask outboundPickingTask) {
        outboundPickingTaskService.save(outboundPickingTask);
        return Result.success("Created successfully");
    }

    @PutMapping
    public Result<String> update(@RequestBody OutboundPickingTask outboundPickingTask) {
        outboundPickingTaskService.update(outboundPickingTask);
        return Result.success("Updated successfully");
    }

    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        outboundPickingTaskService.removeById(id);
        return Result.success("Deleted successfully");
    }
    
    @PostMapping("/generate/{outboundId}")
    public Result<String> generateTasks(@PathVariable Long outboundId) {
        outboundPickingTaskService.generateTasks(outboundId);
        return Result.success("Tasks generated successfully");
    }

    @PostMapping("/save/{outboundId}")
    public Result<String> saveTasks(@PathVariable Long outboundId, @RequestBody List<OutboundPickingTask> tasks) {
        outboundPickingTaskService.saveTasks(outboundId, tasks);
        return Result.success("Tasks saved successfully");
    }

    @PostMapping("/complete/{id}")
    public Result<String> completeTask(@PathVariable Long id, @RequestParam Long binId) {
        outboundPickingTaskService.completeTask(id, binId);
        return Result.success("Task completed successfully");
    }
    
    @PostMapping("/cancel/{id}")
    public Result<String> cancelTask(@PathVariable Long id) {
        outboundPickingTaskService.cancelTask(id);
        return Result.success("Task cancelled successfully");
    }
}
