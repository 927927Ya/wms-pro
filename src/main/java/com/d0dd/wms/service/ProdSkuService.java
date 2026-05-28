package com.d0dd.wms.service;

import com.d0dd.wms.dto.ProductQueryDto;
import com.d0dd.wms.entity.ProdSku;
import java.util.List;

public interface ProdSkuService {
    ProdSku getById(Long id);
    List<ProdSku> list(ProdSku prodSku);
    List<ProdSku> list(ProductQueryDto queryDto); // New method
    int save(ProdSku prodSku);
    int update(ProdSku prodSku);
    int removeById(Long id);
    int count();
    String generateSkuCode(); // Expose generation logic
}
