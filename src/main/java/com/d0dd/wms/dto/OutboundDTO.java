package com.d0dd.wms.dto;

import com.d0dd.wms.entity.Outbound;
import com.d0dd.wms.entity.OutboundDetails;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class OutboundDTO implements Serializable {
    private Outbound outbound;
    private List<OutboundDetails> outboundDetails;
}
