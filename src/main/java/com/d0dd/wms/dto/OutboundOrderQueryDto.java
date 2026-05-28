package com.d0dd.wms.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class OutboundOrderQueryDto implements Serializable {
    private Long warehouseId;
    private Long id; // Outbound Order No
    private String outboundStatus;
    private String outboundType;
    private Long customerId;
    private String outboundDate;
    private String startTime;
    private String endTime;
}
