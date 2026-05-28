package com.d0dd.wms.mapper;

import com.d0dd.wms.dto.ProductQueryDto;
import com.d0dd.wms.entity.ProdSku;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ProdSkuMapper {
    ProdSku selectById(Long id);
    ProdSku selectBySkuCode(String skuCode);
    List<ProdSku> selectList(ProdSku prodSku);
    List<ProdSku> selectListByDto(ProductQueryDto queryDto); // New method
    int insert(ProdSku prodSku);
    int update(ProdSku prodSku);
    int deleteById(Long id);
    int count();
}
