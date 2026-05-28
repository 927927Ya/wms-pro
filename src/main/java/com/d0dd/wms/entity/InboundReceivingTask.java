package com.d0dd.wms.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class InboundReceivingTask implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long inboundDetailsId;
    private Long inboundId;
    private Long warehouseId;
    private String warehouseName;
    private String inboundNo;
    private Long skuId;
    private String skuCode;
    private String prodNameEng;
    private String prodNameChn;
    private String palletName;
    private BigDecimal toReceivedQty;
    private BigDecimal receivedQty;
    private Integer taskStatus;
    private Long binId;
    private Long operatorId;
    private Date createTime;
    private Date operationTime;
    private String remark;
}
