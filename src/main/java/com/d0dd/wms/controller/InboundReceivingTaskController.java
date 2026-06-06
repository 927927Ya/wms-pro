package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.entity.InboundReceivingTask;
import com.d0dd.wms.service.InboundReceivingTaskService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inbound/receiving/task")
public class InboundReceivingTaskController {

    @Autowired
    private InboundReceivingTaskService inboundReceivingTaskService;

    @GetMapping("/list")
    public Result<Map<String, Object>> list(InboundReceivingTask inboundReceivingTask) {
        List<InboundReceivingTask> list = inboundReceivingTaskService.list(inboundReceivingTask);
        Map<String, Object> result = new HashMap<>();
        result.put("records", list);
        result.put("total", list.size());
        return Result.success(result);
    }
    
    @GetMapping("/details/{inboundDetailsId}")
    public Result<Map<String, Object>> listByInboundDetailsId(@PathVariable Long inboundDetailsId) {

        List<InboundReceivingTask> list = inboundReceivingTaskService.getByInboundDetailsId(inboundDetailsId);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }

    @GetMapping("/{id}")
    public Result<InboundReceivingTask> getById(@PathVariable Long id) {
        return Result.success(inboundReceivingTaskService.getById(id));
    }

    @RequiresPermissions("inbound:manage")
    @PostMapping
    public Result<String> save(@RequestBody InboundReceivingTask inboundReceivingTask) {
        inboundReceivingTaskService.save(inboundReceivingTask);
        return Result.success("Created successfully");
    }

    @RequiresPermissions("inbound:manage")
    @PutMapping
    public Result<String> update(@RequestBody InboundReceivingTask inboundReceivingTask) {
        inboundReceivingTaskService.update(inboundReceivingTask);
        return Result.success("Updated successfully");
    }

    @RequiresPermissions("inbound:manage")
    @PostMapping("/generate/{inboundId}")
    public Result<String> generateTasks(@PathVariable Long inboundId) {
        inboundReceivingTaskService.generateTasks(inboundId);
        return Result.success("Tasks generated successfully");
    }

    @RequiresPermissions("inbound:manage")
    @PostMapping("/save/{inboundId}")
    public Result<String> saveTasks(@PathVariable Long inboundId, @RequestBody List<InboundReceivingTask> tasks) {
        inboundReceivingTaskService.saveTasks(inboundId, tasks);
        return Result.success("Tasks saved successfully");
    }

    @RequiresPermissions("inbound:receive")
    @PostMapping("/complete/{id}")
    public Result<String> completeTask(@PathVariable Long id, @RequestParam Long binId) {
        inboundReceivingTaskService.completeTask(id, binId);
        return Result.success("Task completed successfully");
    }
    
    @RequiresPermissions("inbound:receive")
    @PostMapping("/cancel/{id}")
    public Result<String> cancelTask(@PathVariable Long id) {
        inboundReceivingTaskService.cancelTask(id);
        return Result.success("Task cancelled successfully");
    }

    @RequiresPermissions("inbound:manage")
    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        inboundReceivingTaskService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
