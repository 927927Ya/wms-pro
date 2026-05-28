package com.d0dd.wms.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class InventoryBin implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelIgnore
    private Long id;
    
    @ExcelIgnore
    private String binId;
    
    @ExcelIgnore
    private Long batchId;
    
    @ExcelProperty("Product Status")
    private String productStatus;
    
    @ExcelProperty("Total Qty")
    private BigDecimal totalQty;

    // Transient fields for display
    @ExcelProperty("Bin Name")
    private String binName;

    @ExcelProperty("Zone Name")
    private String zoneName;
    
    @ExcelProperty("Batch No")
    private String batchNo;
    
    @ExcelProperty("SKU Code")
    private String skuCode;
    
    @ExcelProperty("Product Name")
    private String prodName;
    
    @ExcelProperty("Warehouse")
    private String warehouseName;
}
