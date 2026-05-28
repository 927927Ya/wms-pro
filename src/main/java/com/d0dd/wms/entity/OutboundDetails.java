package com.d0dd.wms.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OutboundDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long outboundId;
    private Long skuId;
    private BigDecimal outQty;
    private String qtyUnit;
    private BigDecimal outWeight;
    private BigDecimal nonStandardWeight;
    private String weightUnit;
}
