package com.d0dd.wms.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class InventoryHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long binId;
    private BigDecimal beforeQty;
    private BigDecimal afterQty;
    private BigDecimal diffQty;
    private String inventoryOpType;
    private Long relatedOrderId;
    private Date operationDate;
    private Long operatorId;
}
