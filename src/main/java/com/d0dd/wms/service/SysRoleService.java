package com.d0dd.wms.service;

import com.d0dd.wms.dto.SysRoleQueryDto;
import com.d0dd.wms.entity.SysRole;

import java.util.List;

public interface SysRoleService {
    List<SysRole> list(SysRoleQueryDto queryDto);
    SysRole getById(Long roleId);
    int insert(SysRole sysRole);
    int update(SysRole sysRole);
    int deleteById(Long roleId);
}
