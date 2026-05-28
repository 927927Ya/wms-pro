package com.d0dd.wms.service.impl;

import com.d0dd.wms.dto.SysRoleQueryDto;
import com.d0dd.wms.entity.SysRole;
import com.d0dd.wms.mapper.SysRoleMapper;
import com.d0dd.wms.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private com.d0dd.wms.mapper.AuthRoleMapper authRoleMapper;

    @Override
    public List<SysRole> list(SysRoleQueryDto queryDto) {
        return sysRoleMapper.selectListByDto(queryDto);
    }

    @Override
    public SysRole getById(Long roleId) {
        SysRole role = sysRoleMapper.selectById(roleId);
        if (role != null) {
            role.setAuthorityIds(authRoleMapper.selectAuthorityListByRoleId(roleId));
        }
        return role;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public int insert(SysRole sysRole) {
        int rows = sysRoleMapper.insert(sysRole);
        insertRoleAuthority(sysRole);
        return rows;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public int update(SysRole sysRole) {
        int rows = sysRoleMapper.update(sysRole);
        authRoleMapper.deleteByRoleId(sysRole.getRoleId());
        insertRoleAuthority(sysRole);
        return rows;
    }
    
    public void insertRoleAuthority(SysRole role) {
        List<Long> authorityIds = role.getAuthorityIds();
        if (authorityIds != null && !authorityIds.isEmpty()) {
            authRoleMapper.batchInsert(role.getRoleId(), authorityIds);
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public int deleteById(Long roleId) {
        authRoleMapper.deleteByRoleId(roleId);
        return sysRoleMapper.deleteById(roleId);
    }
}
