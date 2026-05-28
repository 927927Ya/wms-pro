package com.d0dd.wms.mapper;

import com.d0dd.wms.entity.ProdTagSku;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ProdTagSkuMapper {
    int insert(ProdTagSku prodTagSku);
    int deleteBySkuId(Long skuId);
    List<Long> selectTagIdsBySkuId(Long skuId);
    List<Long> selectSkuIdsByTagId(Long tagId);
    int batchInsert(List<ProdTagSku> list);
    int deleteByTagId(Long tagId);
}
