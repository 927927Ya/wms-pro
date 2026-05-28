package com.d0dd.wms.service.impl;

import com.d0dd.wms.entity.ProdSku;
import com.d0dd.wms.entity.ProdTag;
import com.d0dd.wms.entity.ProdTagSku;
import com.d0dd.wms.mapper.ProdSkuMapper;
import com.d0dd.wms.mapper.ProdTagMapper;
import com.d0dd.wms.mapper.ProdTagSkuMapper;
import com.d0dd.wms.service.ProdTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProdTagServiceImpl implements ProdTagService {

    @Autowired
    private ProdTagMapper prodTagMapper;
    
    @Autowired
    private ProdTagSkuMapper prodTagSkuMapper;
    
    @Autowired
    private ProdSkuMapper prodSkuMapper;

    @Override
    public ProdTag getById(Long id) {
        ProdTag tag = prodTagMapper.selectById(id);
        if (tag != null) {
            List<Long> skuIds = prodTagSkuMapper.selectSkuIdsByTagId(id);
            tag.setSkuIds(skuIds);
        }
        return tag;
    }

    @Override
    public List<ProdTag> list(ProdTag prodTag) {
        return prodTagMapper.selectList(prodTag);
    }
    
    @Override
    public List<ProdSku> getSkusByTagId(Long tagId) {
        List<Long> skuIds = prodTagSkuMapper.selectSkuIdsByTagId(tagId);
        if (skuIds == null || skuIds.isEmpty()) {
            return new java.util.ArrayList<>();
        }
        List<ProdSku> skus = new java.util.ArrayList<>();
        for (Long skuId : skuIds) {
            ProdSku sku = prodSkuMapper.selectById(skuId);
            if (sku != null) {
                skus.add(sku);
            }
        }
        return skus;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(ProdTag prodTag) {
        int rows = prodTagMapper.insert(prodTag);
        if (prodTag.getSkuIds() != null && !prodTag.getSkuIds().isEmpty()) {
            saveSkuLinks(prodTag.getId(), prodTag.getSkuIds());
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(ProdTag prodTag) {
        int rows = prodTagMapper.update(prodTag);
        if (prodTag.getSkuIds() != null) {
            prodTagSkuMapper.deleteByTagId(prodTag.getId());
            if (!prodTag.getSkuIds().isEmpty()) {
                saveSkuLinks(prodTag.getId(), prodTag.getSkuIds());
            }
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int removeById(Long id) {
        // Also remove associations
        prodTagSkuMapper.deleteByTagId(id);
        return prodTagMapper.deleteById(id);
    }

    private void saveSkuLinks(Long tagId, List<Long> skuIds) {
        List<ProdTagSku> list = new ArrayList<>();
        long baseId = System.nanoTime();
        for (int i = 0; i < skuIds.size(); i++) {
            ProdTagSku item = new ProdTagSku();
            item.setId(baseId + i);
            item.setTagId(tagId);
            item.setSkuId(skuIds.get(i));
            list.add(item);
        }
        prodTagSkuMapper.batchInsert(list);
    }
}
