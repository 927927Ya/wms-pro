package com.d0dd.wms.mapper;

import com.d0dd.wms.dto.InboundQueryDto;
import com.d0dd.wms.entity.Inbound;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface InboundMapper {
    Inbound selectById(Long id);
    List<Inbound> selectList(Inbound inbound);
    List<Inbound> selectListByDto(InboundQueryDto queryDto);
    int countByStatus(List<String> statuses);
    int insert(Inbound inbound);
    int update(Inbound inbound);
    int deleteById(Long id);
}
