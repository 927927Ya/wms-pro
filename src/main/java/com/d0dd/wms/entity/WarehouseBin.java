package com.d0dd.wms.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class WarehouseBin implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String binName;
    private Long warehouseId;
    private Long zoneId;
    private Long rackId;
    private Integer columnNum;
    private Integer onZoomLevel; // This maps to Layer Number
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
