package com.d0dd.wms.service.impl;

import com.d0dd.wms.entity.SysUser;
import com.d0dd.wms.mapper.SysUserMapper;
import com.d0dd.wms.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private com.d0dd.wms.mapper.SysUserRoleMapper sysUserRoleMapper;

    @Override
    public SysUser getById(Long userId) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user != null) {
            user.setRoleIds(sysUserRoleMapper.selectRoleListByUserId(userId));
        }
        return user;
    }

    @Override
    public SysUser getByUserName(String userName) {
        return sysUserMapper.selectByUserName(userName);
    }

    @Override
    public List<SysUser> getAll() {
        return sysUserMapper.selectList(new SysUser());
    }

    @Override
    public List<SysUser> list(com.d0dd.wms.dto.SysUserQueryDto queryDto) {
        return sysUserMapper.selectListByDto(queryDto);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public int save(SysUser sysUser) {
        int rows = sysUserMapper.insert(sysUser);
        insertUserRole(sysUser);
        return rows;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public int update(SysUser sysUser) {
        // Only update user info if not null
        int rows = sysUserMapper.update(sysUser);
        // Delete and re-insert roles
        sysUserRoleMapper.deleteUserRoleByUserId(sysUser.getUserId());
        insertUserRole(sysUser);
        return rows;
    }

    @Override
    public int updateProfile(SysUser sysUser) {
        return sysUserMapper.update(sysUser);
    }

    @Override
    public int updatePassword(Long userId, String newPassword) {
        SysUser update = new SysUser();
        update.setUserId(userId);
        update.setPassword(newPassword);
        return sysUserMapper.update(update);
    }
    
    public void insertUserRole(SysUser user) {
        List<Long> roleIds = user.getRoleIds();
        if (roleIds != null && !roleIds.isEmpty()) {
            sysUserRoleMapper.batchUserRole(user.getUserId(), roleIds);
        }
    }

    @Override
    public int removeById(Long userId) {
        return sysUserMapper.deleteById(userId);
    }
}
