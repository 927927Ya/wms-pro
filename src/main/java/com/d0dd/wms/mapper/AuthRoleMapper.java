package com.d0dd.wms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuthRoleMapper {
    int deleteByRoleId(Long roleId);
    int batchInsert(@Param("roleId") Long roleId, @Param("authorityIds") List<Long> authorityIds);
    List<Long> selectAuthorityListByRoleId(Long roleId);
}

