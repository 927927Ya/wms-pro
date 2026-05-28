package com.d0dd.wms.service.impl;

import com.d0dd.wms.entity.SysDict;
import com.d0dd.wms.mapper.SysDictMapper;
import com.d0dd.wms.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysDictServiceImpl implements SysDictService {

    @Autowired
    private SysDictMapper sysDictMapper;

    @Override
    public SysDict getById(Long id) {
        return sysDictMapper.selectById(id);
    }

    @Override
    public List<SysDict> list(SysDict sysDict) {
        return sysDictMapper.selectList(sysDict);
    }

    @Override
    public int save(SysDict sysDict) {
        return sysDictMapper.insert(sysDict);
    }

    @Override
    public int update(SysDict sysDict) {
        return sysDictMapper.update(sysDict);
    }

    @Override
    public int removeById(Long id) {
        return sysDictMapper.deleteById(id);
    }
}
