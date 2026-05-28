package com.d0dd.wms.service.impl;

import com.d0dd.wms.dto.SysMenuQueryDto;
import com.d0dd.wms.entity.SysMenu;
import com.d0dd.wms.mapper.SysMenuMapper;
import com.d0dd.wms.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> list(SysMenuQueryDto queryDto) {
        return sysMenuMapper.selectListByDto(queryDto);
    }

    @Override
    public SysMenu getById(Long menuId) {
        return sysMenuMapper.selectById(menuId);
    }

    @Override
    public int insert(SysMenu sysMenu) {
        return sysMenuMapper.insert(sysMenu);
    }

    @Override
    public int update(SysMenu sysMenu) {
        return sysMenuMapper.update(sysMenu);
    }

    @Override
    public int deleteById(Long menuId) {
        return sysMenuMapper.deleteById(menuId);
    }
}
