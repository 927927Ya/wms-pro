package com.d0dd.wms.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class Outbound implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long warehouseId;
    private String outboundType;
    private String outboundStatus;
    private Long customerId;
    private Long createBy;
    private Date createTime;
    private Date outboundDate;
    private String remark;
}
