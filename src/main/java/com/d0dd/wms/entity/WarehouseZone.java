package com.d0dd.wms.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class WarehouseZone implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String zoneName;
    private Long warehouseId;
    private Integer layers;
    private BigDecimal length;
    private BigDecimal width;
    private Integer isEnabled;
    private String remark;
}
