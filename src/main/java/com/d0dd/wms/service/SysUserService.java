package com.d0dd.wms.service;

import com.d0dd.wms.entity.SysUser;
import java.util.List;

public interface SysUserService {
    SysUser getById(Long userId);
    SysUser getByUserName(String userName);
    List<SysUser> getAll();
    List<SysUser> list(com.d0dd.wms.dto.SysUserQueryDto queryDto);
    int save(SysUser sysUser);
    int update(SysUser sysUser);
    int updateProfile(SysUser sysUser);
    int updatePassword(Long userId, String newPassword);
    int removeById(Long userId);
}
