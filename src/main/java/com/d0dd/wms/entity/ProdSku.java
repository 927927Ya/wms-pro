package com.d0dd.wms.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class ProdSku implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String skuCode;
    private String prodNameEng;
    private String prodNameChn;
    private Long categoryId;
    private Long secondCategoryId;
    private Long firstCategoryId;
    private String imageUrl;
    private String outerImageUrl;
    private String innerImageUrl;
    private Integer isEnabled;
    private String saleStorageUnit;
    private String storageUnit;
    private java.math.BigDecimal conversionStorageToSaleUnit;
    private java.math.BigDecimal palletStorageCapacity;
    private java.math.BigDecimal cost;
    private java.math.BigDecimal weight;
    private java.math.BigDecimal volume;
    private String prodType;
    private Long storageTypeId;
    private String origin;
    private String remark;
    
    // New fields
    private Integer isAllowSell;
    private Integer limitSellQty;
    private Integer isPalletPicking;
    
    private java.util.Date createTime;
    private String createBy;
    private String updateBy;
    private java.util.Date updateTime;

    // Extra fields for frontend
    private List<Long> tagIds;
    
    // Inventory Aggregation
    private java.math.BigDecimal totalQty;
    private java.math.BigDecimal availableQty;
    private java.math.BigDecimal frozenQty;

    // Display fields
    private String categoryName;
    private String tagNames;
}
