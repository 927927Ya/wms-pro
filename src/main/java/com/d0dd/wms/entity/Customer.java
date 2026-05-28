package com.d0dd.wms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("customer")
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 客户表ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 客户编号
     */
    private String code;

    /**
     * 客户名
     */
    private String name;

    /**
     * 客户级别,参考sys_dict中的customer_level属性值
     */
    private String customerLevel;

    /**
     * 客户状态，参考sys_dict中的customer_status属性值
     */
    private String customerStatus;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * E-Mail
     */
    private String email;

    /**
     * 地址
     */
    private String address;

    /**
     * 城市
     */
    private String city;

    /**
     * 省份
     */
    private String province;

    /**
     * 邮编
     */
    private String zipcode;

    /**
     * 主营业务
     */
    private String businessIntro;

    /**
     * 激活状态：激活（1）,否则（0）;缺省为1
     */
    private Integer isEnabled;
}
