package com.d0dd.wms.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ProdTag implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String tagName;
    private String productNames;
    private List<Long> skuIds;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
}
