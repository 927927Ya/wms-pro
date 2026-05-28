package com.d0dd.wms.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class StorageQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    // SKU Filters
    private String skuCode;
    private String prodName; // Matches Eng or Chn
    private Long categoryId;
    private String prodType;

    // Inventory Filters
    private Long warehouseId;
    private Long zoneId;
    private Long rackId;
    private String binName;
    private String batchNo;
    
    // Quantity Filters
    private BigDecimal minQty;
    private BigDecimal maxQty;
}
