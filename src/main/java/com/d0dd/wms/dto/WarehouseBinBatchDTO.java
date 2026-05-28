package com.d0dd.wms.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class WarehouseBinBatchDTO implements Serializable {
    private Long rackId;
    private Integer columnCount;
    private Integer layerCount;
    
    // Default properties for all bins
    private String rackType;
    private String binType;
    private BigDecimal maximumCapcity;
    private String capcityUnit;
    private BigDecimal maximumVolume;
    private String volumeUnit;
    private String storageRule;
    private Integer isEnabled;
    private BigDecimal lowStorageAlertRato;
}
