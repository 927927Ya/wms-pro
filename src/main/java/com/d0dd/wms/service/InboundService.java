package com.d0dd.wms.service;

import com.d0dd.wms.dto.InboundDTO;
import com.d0dd.wms.dto.InboundQueryDto;
import com.d0dd.wms.entity.Inbound;
import java.util.List;

public interface InboundService {
    Inbound getById(Long id);
    List<Inbound> list(Inbound inbound);
    List<Inbound> list(InboundQueryDto queryDto);
    int save(Inbound inbound);
    int createInbound(InboundDTO inboundDTO);
    int update(Inbound inbound);
    int removeById(Long id);
}
