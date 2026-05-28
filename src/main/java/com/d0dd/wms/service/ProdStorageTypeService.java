package com.d0dd.wms.service;

import com.d0dd.wms.entity.ProdStorageType;
import java.util.List;

public interface ProdStorageTypeService {
    ProdStorageType getById(Long id);
    List<ProdStorageType> list(ProdStorageType prodStorageType);
    int save(ProdStorageType prodStorageType);
    int update(ProdStorageType prodStorageType);
    int removeById(Long id);
}
