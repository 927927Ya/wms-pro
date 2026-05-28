package com.d0dd.wms.entity;

import lombok.Data;
import java.io.Serializable;

@Data
public class ProdStorageType implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String storageType;
}
