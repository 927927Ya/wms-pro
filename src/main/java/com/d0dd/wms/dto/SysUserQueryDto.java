package com.d0dd.wms.dto;

import lombok.Data;

@Data
public class SysUserQueryDto {
    private String userName; // Maps to givenName or nickName usually, let's use givenName for login or nickName for display
    private String phonenumber;
    private String status;
}
