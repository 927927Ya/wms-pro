package com.d0dd.wms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserRoleMapper {
    int deleteUserRoleByUserId(Long userId);
    int batchUserRole(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
    List<Long> selectRoleListByUserId(Long userId);
}
