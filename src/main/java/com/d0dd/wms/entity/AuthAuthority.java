package com.d0dd.wms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("authorities")
public class AuthAuthority implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long authorityId;
    private String authorityName;
    private String authorityKey;
    private Long parentId;
    private Integer orderNum;
    private String status;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private String remark;
}

