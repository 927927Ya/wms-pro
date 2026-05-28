package com.d0dd.wms.mapper;

import com.d0dd.wms.entity.OutboundDetails;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface OutboundDetailsMapper {
    OutboundDetails selectById(Long id);
    List<OutboundDetails> selectList(OutboundDetails outboundDetails);
    List<OutboundDetails> selectByOutboundId(Long outboundId);
    int insert(OutboundDetails outboundDetails);
    int update(OutboundDetails outboundDetails);
    int deleteById(Long id);
    int deleteByOutboundId(Long outboundId);
}
