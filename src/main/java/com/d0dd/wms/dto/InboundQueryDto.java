package com.d0dd.wms.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class InboundQueryDto implements Serializable {
    private Long warehouseId;
    private String inboundNo;
    private String inboundStatus;
    private String inboundType;
    private Long supplierId;
    private Long customerId;
    private String startTime;
    private String endTime;
}
