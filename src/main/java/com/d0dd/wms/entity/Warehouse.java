package com.d0dd.wms.entity;

import com.d0dd.wms.common.MultiDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Warehouse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String warehouseCode;
    private String warehouseName;
    private BigDecimal length;
    private BigDecimal width;
    private String isActive;
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String remark;
    private Long orderNum;
    private String createBy;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    private Date createTime;
    private String updateBy;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    private Date updateTime;
}
