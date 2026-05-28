package com.d0dd.wms.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class InventorySummary implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long skuId;
    private BigDecimal totalQty;
    private BigDecimal availableQty;
    private BigDecimal frozenQty;
    private Date updateTime;

    // Transient
    private String skuCode;
    private String prodName;
    private String prodNameEng;
    private String prodNameChn;
}
