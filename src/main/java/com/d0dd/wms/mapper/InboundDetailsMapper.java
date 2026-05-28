package com.d0dd.wms.mapper;

import com.d0dd.wms.dto.ProductQueryDto;
import com.d0dd.wms.entity.InboundDetails;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface InboundDetailsMapper {
    InboundDetails selectById(Long id);
    List<InboundDetails> selectList(InboundDetails inboundDetails);
    List<InboundDetails> selectByInboundId(Long inboundId);
    List<InboundDetails> selectSkuSummaryForOutbound(ProductQueryDto queryDto);
    int insert(InboundDetails inboundDetails);
    int update(InboundDetails inboundDetails);
    int deleteById(Long id);
    int deleteByInboundId(Long inboundId);
}
