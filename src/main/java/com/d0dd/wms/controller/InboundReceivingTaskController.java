package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.entity.InboundReceivingTask;
import com.d0dd.wms.service.InboundReceivingTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inbound/receiving/task")
public class InboundReceivingTaskController {

    @Autowired
    private InboundReceivingTaskService inboundReceivingTaskService;

    @GetMapping("/list")
    public Result<List<InboundReceivingTask>> list(InboundReceivingTask inboundReceivingTask) {
        return Result.success(inboundReceivingTaskService.list(inboundReceivingTask));
    }
    
    @GetMapping("/details/{inboundDetailsId}")
    public Result<List<InboundReceivingTask>> listByInboundDetailsId(@PathVariable Long inboundDetailsId) {
        return Result.success(inboundReceivingTaskService.getByInboundDetailsId(inboundDetailsId));
    }

    @GetMapping("/{id}")
    public Result<InboundReceivingTask> getById(@PathVariable Long id) {
        return Result.success(inboundReceivingTaskService.getById(id));
    }

    @PostMapping
    public Result<String> save(@RequestBody InboundReceivingTask inboundReceivingTask) {
        inboundReceivingTaskService.save(inboundReceivingTask);
        return Result.success("Created successfully");
    }

    @PutMapping
    public Result<String> update(@RequestBody InboundReceivingTask inboundReceivingTask) {
        inboundReceivingTaskService.update(inboundReceivingTask);
        return Result.success("Updated successfully");
    }

    @PostMapping("/generate/{inboundId}")
    public Result<String> generateTasks(@PathVariable Long inboundId) {
        inboundReceivingTaskService.generateTasks(inboundId);
        return Result.success("Tasks generated successfully");
    }

    @PostMapping("/save/{inboundId}")
    public Result<String> saveTasks(@PathVariable Long inboundId, @RequestBody List<InboundReceivingTask> tasks) {
        inboundReceivingTaskService.saveTasks(inboundId, tasks);
        return Result.success("Tasks saved successfully");
    }

    @PostMapping("/complete/{id}")
    public Result<String> completeTask(@PathVariable Long id, @RequestParam Long binId) {
        inboundReceivingTaskService.completeTask(id, binId);
        return Result.success("Task completed successfully");
    }
    
    @PostMapping("/cancel/{id}")
    public Result<String> cancelTask(@PathVariable Long id) {
        inboundReceivingTaskService.cancelTask(id);
        return Result.success("Task cancelled successfully");
    }

    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        inboundReceivingTaskService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
