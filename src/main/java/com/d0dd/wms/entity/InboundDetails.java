package com.d0dd.wms.entity;

import com.d0dd.wms.common.MultiDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class InboundDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long inboundId;
    private Long skuId;
    private String batchNo;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    private Date productionDate;
    private String productStatus;
    private String storageUnit;
    private BigDecimal toReceivedQty;
    private BigDecimal receivedQty;
    private String weightUnit;
    private BigDecimal totalWeight;
    private Integer palletCount;
    private String remark;

    private String skuCode;
    private String prodNameEng;
    private String prodNameChn;
    private String imageUrl;
}
