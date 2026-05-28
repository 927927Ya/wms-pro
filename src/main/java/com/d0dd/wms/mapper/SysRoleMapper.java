package com.d0dd.wms.mapper;

import com.d0dd.wms.dto.SysRoleQueryDto;
import com.d0dd.wms.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysRoleMapper {
    List<SysRole> selectList(SysRole sysRole);
    List<SysRole> selectListByDto(SysRoleQueryDto queryDto);
    SysRole selectById(Long roleId);
    int insert(SysRole sysRole);
    int update(SysRole sysRole);
    int deleteById(Long roleId);
}
