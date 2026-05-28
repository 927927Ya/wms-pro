package com.d0dd.wms.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class Inbound implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long supplierId;
    private Long warehouseId;
    private Long customerId;
    private String inboundNo;
    private Date estimatedArrivalTime;
    private String inboundStatus;
    private String inboundType;
    private String remark;
    private Date createTime;
    private Long createBy;
    private Long updateBy;
    private Date updateTime;

    private String supplierName;
    private String warehouseName;
    private String customerName;
    private String createByName;
}
