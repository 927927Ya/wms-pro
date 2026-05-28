package com.d0dd.wms.entity;

import lombok.Data;
import java.io.Serializable;

@Data
public class SysUserRole implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private Long roleId;
}
