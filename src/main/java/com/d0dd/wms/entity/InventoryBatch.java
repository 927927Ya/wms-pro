package com.d0dd.wms.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class InventoryBatch implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long warehouseId;
    private Long skuId;
    private String batchNo;
    private BigDecimal totalQty;
    private BigDecimal availableQty;
    private BigDecimal frozenQty;
    private Integer fdaHold;

    // Transient fields
    private String skuCode;
    private String prodName;
    private String warehouseName;
}
