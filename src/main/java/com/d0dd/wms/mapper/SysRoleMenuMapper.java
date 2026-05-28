package com.d0dd.wms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysRoleMenuMapper {
    int deleteRoleMenuByRoleId(Long roleId);
    int batchRoleMenu(@Param("roleId") Long roleId, @Param("menuIds") List<Long> menuIds);
    List<Long> selectMenuListByRoleId(Long roleId);
}
