package com.d0dd.wms.mapper;

import com.d0dd.wms.entity.SysDict;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface SysDictMapper {
    SysDict selectById(Long id);
    List<SysDict> selectList(SysDict sysDict);
    int insert(SysDict sysDict);
    int update(SysDict sysDict);
    int deleteById(Long id);
}
