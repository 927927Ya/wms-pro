package com.d0dd.wms.entity;

import lombok.Data;
import java.io.Serializable;

@Data
public class SysRoleMenu implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long roleId;
    private Long menuId;
}
