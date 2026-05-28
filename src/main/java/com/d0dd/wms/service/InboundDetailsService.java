package com.d0dd.wms.service;

import com.d0dd.wms.entity.InboundDetails;
import java.util.List;

public interface InboundDetailsService {
    InboundDetails getById(Long id);
    List<InboundDetails> list(InboundDetails inboundDetails);
    List<InboundDetails> getByInboundId(Long inboundId);
    int save(InboundDetails inboundDetails);
    int update(InboundDetails inboundDetails);
    int removeById(Long id);
    int removeByInboundId(Long inboundId);
}
