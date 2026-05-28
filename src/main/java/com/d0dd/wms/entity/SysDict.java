package com.d0dd.wms.entity;

import lombok.Data;
import java.io.Serializable;

@Data
public class SysDict implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String dictType;
    private String dictCode;
    private String dictName;
    private Integer sort;
    private Integer isEnable;
    private String remark;
}
