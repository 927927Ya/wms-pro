package com.d0dd.wms.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class SysUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String familyName;
    private String givenName;
    private String nickName;
    private String userType;
    private String email;
    private String phonenumber;
    private String sex;
    private String avatar;
    private String password;
    private String status;
    private String delFlag;
    private String loginIp;
    private Date loginDate;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private String remark;

    // Transient field for role assignment
    private java.util.List<Long> roleIds;
}
