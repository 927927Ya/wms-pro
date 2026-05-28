package com.d0dd.wms.service;

import com.d0dd.wms.entity.ProdSku;
import com.d0dd.wms.entity.ProdTag;
import java.util.List;

public interface ProdTagService {
    ProdTag getById(Long id);
    List<ProdTag> list(ProdTag prodTag);
    List<ProdSku> getSkusByTagId(Long tagId); // New method
    int save(ProdTag prodTag);
    int update(ProdTag prodTag);
    int removeById(Long id);
}
