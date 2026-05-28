package com.d0dd.wms.mapper;

import com.d0dd.wms.entity.OutboundPickingTask;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

import org.apache.ibatis.annotations.Param;

@Mapper
public interface OutboundPickingTaskMapper {
    OutboundPickingTask selectById(Long id);
    List<OutboundPickingTask> selectList(OutboundPickingTask outboundPickingTask);
    List<OutboundPickingTask> selectByOutboundDetailsId(Long outboundDetailsId);
    int countByStatus(@Param("status") Integer status);
    int insert(OutboundPickingTask outboundPickingTask);
    int update(OutboundPickingTask outboundPickingTask);
    int deleteById(Long id);
    int deleteByOutboundDetailsId(Long outboundDetailsId);
}
