package com.d0dd.wms.service;

import com.d0dd.wms.entity.OutboundDetails;
import java.util.List;

public interface OutboundDetailsService {
    OutboundDetails getById(Long id);
    List<OutboundDetails> list(OutboundDetails outboundDetails);
    List<OutboundDetails> getByOutboundId(Long outboundId);
    int save(OutboundDetails outboundDetails);
    int update(OutboundDetails outboundDetails);
    int removeById(Long id);
    int removeByOutboundId(Long outboundId);
}
