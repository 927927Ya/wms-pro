package com.d0dd.wms.dto;

import com.d0dd.wms.entity.Inbound;
import com.d0dd.wms.entity.InboundDetails;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class InboundDTO implements Serializable {
    private Inbound inbound;
    private List<InboundDetails> inboundDetails;
}
