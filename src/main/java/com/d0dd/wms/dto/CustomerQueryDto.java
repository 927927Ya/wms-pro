package com.d0dd.wms.dto;

import lombok.Data;

@Data
public class CustomerQueryDto {
    private String code;
    private String name;
    private String customerLevel;
    private String customerStatus;
}
