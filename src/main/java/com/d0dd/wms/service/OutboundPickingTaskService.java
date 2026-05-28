package com.d0dd.wms.service;

import com.d0dd.wms.entity.OutboundPickingTask;
import java.util.List;

public interface OutboundPickingTaskService {
    OutboundPickingTask getById(Long id);
    List<OutboundPickingTask> list(OutboundPickingTask outboundPickingTask);
    List<OutboundPickingTask> getByOutboundDetailsId(Long outboundDetailsId);
    int save(OutboundPickingTask outboundPickingTask);
    int update(OutboundPickingTask outboundPickingTask);
    int removeById(Long id);
    int removeByOutboundDetailsId(Long outboundDetailsId);
    void generateTasks(Long outboundId);
    void saveTasks(Long outboundId, List<OutboundPickingTask> tasks);
    void completeTask(Long taskId, Long binId);
    void cancelTask(Long taskId);
}
