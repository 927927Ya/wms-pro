package com.d0dd.wms.mapper;

import com.d0dd.wms.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface SysUserMapper {
    SysUser selectById(Long userId);
    SysUser selectByUserName(String userName);
    List<SysUser> selectList(SysUser sysUser);
    List<SysUser> selectListByDto(com.d0dd.wms.dto.SysUserQueryDto queryDto);
    int insert(SysUser sysUser);
    int update(SysUser sysUser);
    int deleteById(Long userId);
}
