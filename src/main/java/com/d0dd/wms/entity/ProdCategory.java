package com.d0dd.wms.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProdCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long parentId;
    private String categoryName;
    private Integer orderNum;
    private String status;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    
    private List<ProdCategory> children = new ArrayList<>();
}
