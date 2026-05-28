package com.d0dd.wms.mapper;

import com.d0dd.wms.entity.InboundReceivingTask;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

import org.apache.ibatis.annotations.Param;

@Mapper
public interface InboundReceivingTaskMapper {
    InboundReceivingTask selectById(Long id);
    List<InboundReceivingTask> selectList(InboundReceivingTask inboundReceivingTask);
    List<InboundReceivingTask> selectByInboundDetailsId(Long inboundDetailsId);
    int countByStatus(@Param("status") Integer status);
    int insert(InboundReceivingTask inboundReceivingTask);
    int update(InboundReceivingTask inboundReceivingTask);
    int deleteById(Long id);
    int deleteByInboundDetailsId(Long inboundDetailsId);
}
