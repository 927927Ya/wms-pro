package com.d0dd.wms.mapper;

import com.d0dd.wms.entity.ProdTag;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ProdTagMapper {
    ProdTag selectById(Long id);
    List<ProdTag> selectList(ProdTag prodTag);
    int insert(ProdTag prodTag);
    int update(ProdTag prodTag);
    int deleteById(Long id);
}
