package com.d0dd.wms.service;

import com.d0dd.wms.dto.SysMenuQueryDto;
import com.d0dd.wms.entity.SysMenu;

import java.util.List;

public interface SysMenuService {
    List<SysMenu> list(SysMenuQueryDto queryDto);
    SysMenu getById(Long menuId);
    int insert(SysMenu sysMenu);
    int update(SysMenu sysMenu);
    int deleteById(Long menuId);
}
