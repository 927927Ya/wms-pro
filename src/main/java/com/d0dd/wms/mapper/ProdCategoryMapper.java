package com.d0dd.wms.mapper;

import com.d0dd.wms.entity.ProdCategory;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ProdCategoryMapper {
    ProdCategory selectById(Long id);
    List<ProdCategory> selectList(ProdCategory prodCategory);
    ProdCategory selectByName(String categoryName);
    int selectCountByParentId(Long parentId);
    int insert(ProdCategory prodCategory);
    int update(ProdCategory prodCategory);
    int deleteById(Long id);
}
