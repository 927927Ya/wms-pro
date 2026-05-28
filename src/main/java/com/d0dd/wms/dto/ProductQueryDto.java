package com.d0dd.wms.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class ProductQueryDto implements Serializable {
    private String skuCode;
    private String prodName; // Matches either English or Chinese name
    private Long categoryId;
    private Integer isEnabled;
    private String prodType;
    private Long tagId; // Filter by tag
    private Long warehouseId; // Filter by warehouse
}
