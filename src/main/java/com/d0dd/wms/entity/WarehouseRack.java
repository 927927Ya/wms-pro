package com.d0dd.wms.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class WarehouseRack implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String rackName;
    private Long warehouseId;
    private Long zoneId;
    private BigDecimal length;
    private BigDecimal width;
    private String rackType;
    private Integer isDouble;
    private String positionInDouble;
    private Long relatedRack;
}
