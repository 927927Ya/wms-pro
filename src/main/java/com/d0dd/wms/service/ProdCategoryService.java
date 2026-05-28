package com.d0dd.wms.service;

import com.d0dd.wms.entity.ProdCategory;
import java.util.List;

public interface ProdCategoryService {
    ProdCategory getById(Long id);
    List<ProdCategory> list(ProdCategory prodCategory);
    int save(ProdCategory prodCategory);
    int update(ProdCategory prodCategory);
    int removeById(Long id);
}
