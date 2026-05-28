package com.d0dd.wms.mapper;

import com.d0dd.wms.entity.ProdStorageType;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ProdStorageTypeMapper {
    ProdStorageType selectById(Long id);
    List<ProdStorageType> selectList(ProdStorageType prodStorageType);
    int insert(ProdStorageType prodStorageType);
    int update(ProdStorageType prodStorageType);
    int deleteById(Long id);
}
