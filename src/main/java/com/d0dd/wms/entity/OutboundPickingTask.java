package com.d0dd.wms.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OutboundPickingTask implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long outboundId;
    private Long outboundDetailsId;
    private Long inventoryBinId;
    private String palletName;
    private Integer taskStatus;
    private BigDecimal toPickedQty;
    private BigDecimal pickedQty;
    private String qtyUnit;
    private Date operationTime;
    private Long operatorId;
    private String remark;
}
