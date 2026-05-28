package com.d0dd.wms.service;

import com.d0dd.wms.entity.InboundReceivingTask;
import java.util.List;

public interface InboundReceivingTaskService {
    InboundReceivingTask getById(Long id);
    List<InboundReceivingTask> list(InboundReceivingTask inboundReceivingTask);
    List<InboundReceivingTask> getByInboundDetailsId(Long inboundDetailsId);
    int save(InboundReceivingTask inboundReceivingTask);
    int update(InboundReceivingTask inboundReceivingTask);
    int removeById(Long id);
    int removeByInboundDetailsId(Long inboundDetailsId);
    void generateTasks(Long inboundId);
    void saveTasks(Long inboundId, List<InboundReceivingTask> tasks);
    void completeTask(Long taskId, Long binId);
    void cancelTask(Long taskId);
}
