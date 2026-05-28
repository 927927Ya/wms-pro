package com.d0dd.wms.service.impl;

import com.d0dd.wms.entity.ProdStorageType;
import com.d0dd.wms.mapper.ProdStorageTypeMapper;
import com.d0dd.wms.service.ProdStorageTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdStorageTypeServiceImpl implements ProdStorageTypeService {

    @Autowired
    private ProdStorageTypeMapper prodStorageTypeMapper;

    @Override
    public ProdStorageType getById(Long id) {
        return prodStorageTypeMapper.selectById(id);
    }

    @Override
    public List<ProdStorageType> list(ProdStorageType prodStorageType) {
        return prodStorageTypeMapper.selectList(prodStorageType);
    }

    @Override
    public int save(ProdStorageType prodStorageType) {
        return prodStorageTypeMapper.insert(prodStorageType);
    }

    @Override
    public int update(ProdStorageType prodStorageType) {
        return prodStorageTypeMapper.update(prodStorageType);
    }

    @Override
    public int removeById(Long id) {
        return prodStorageTypeMapper.deleteById(id);
    }
}
