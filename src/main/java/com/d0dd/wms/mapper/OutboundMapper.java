package com.d0dd.wms.mapper;

import com.d0dd.wms.dto.OutboundOrderQueryDto;
import com.d0dd.wms.entity.Outbound;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface OutboundMapper {
    Outbound selectById(Long id);
    List<Outbound> selectList(OutboundOrderQueryDto queryDto);
    int countByStatus(List<String> statuses);
    int insert(Outbound outbound);
    int update(Outbound outbound);
    int deleteById(Long id);
}
