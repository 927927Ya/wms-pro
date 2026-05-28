package com.d0dd.wms.service;

import com.d0dd.wms.dto.OutboundDTO;
import com.d0dd.wms.dto.OutboundOrderQueryDto;
import com.d0dd.wms.entity.Outbound;
import java.util.List;

public interface OutboundService {
    Outbound getById(Long id);
    List<Outbound> list(OutboundOrderQueryDto queryDto);
    int save(Outbound outbound);
    int createOutbound(OutboundDTO outboundDTO);
    int updateOutbound(OutboundDTO outboundDTO);
    int update(Outbound outbound);
    int removeById(Long id);
}
