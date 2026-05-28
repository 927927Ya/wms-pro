package com.d0dd.wms.mapper;

import com.d0dd.wms.dto.SysMenuQueryDto;
import com.d0dd.wms.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysMenuMapper {
    List<SysMenu> selectList(SysMenu sysMenu);
    List<SysMenu> selectListByDto(SysMenuQueryDto queryDto);
    List<String> selectPermsByUserId(Long userId);
    SysMenu selectById(Long menuId);
    int insert(SysMenu sysMenu);
    int update(SysMenu sysMenu);
    int deleteById(Long menuId);
}
