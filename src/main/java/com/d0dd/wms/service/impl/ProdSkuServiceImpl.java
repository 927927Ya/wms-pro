package com.d0dd.wms.service.impl;

import com.d0dd.wms.entity.ProdSku;
import com.d0dd.wms.entity.ProdTagSku;
import com.d0dd.wms.mapper.ProdSkuMapper;
import com.d0dd.wms.mapper.ProdTagSkuMapper;
import com.d0dd.wms.service.ProdSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import com.d0dd.wms.dto.ProductQueryDto;

@Service
public class ProdSkuServiceImpl implements ProdSkuService {

    @Autowired
    private ProdSkuMapper prodSkuMapper;

    @Autowired
    private ProdTagSkuMapper prodTagSkuMapper;

    @Override
    public ProdSku getById(Long id) {
        ProdSku sku = prodSkuMapper.selectById(id);
        if (sku != null) {
            List<Long> tagIds = prodTagSkuMapper.selectTagIdsBySkuId(id);
            sku.setTagIds(tagIds);
        }
        return sku;
    }

    @Override
    public List<ProdSku> list(ProdSku prodSku) {
        return prodSkuMapper.selectList(prodSku);
    }
    
    @Override
    public List<ProdSku> list(ProductQueryDto queryDto) {
        return prodSkuMapper.selectListByDto(queryDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(ProdSku prodSku) {
        // Auto-generate SKU Code if empty
        if (prodSku.getSkuCode() == null || prodSku.getSkuCode().trim().isEmpty()) {
            prodSku.setSkuCode(generateSkuCode());
        }

        // Uniqueness check
        if (prodSkuMapper.selectBySkuCode(prodSku.getSkuCode()) != null) {
            throw new RuntimeException("SKU Code already exists: " + prodSku.getSkuCode());
        }

        int rows = prodSkuMapper.insert(prodSku);
        
        // Save tags
        if (prodSku.getTagIds() != null && !prodSku.getTagIds().isEmpty()) {
            saveTags(prodSku.getId(), prodSku.getTagIds());
        }
        
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(ProdSku prodSku) {
        ProdSku existing = prodSkuMapper.selectBySkuCode(prodSku.getSkuCode());
        if (existing != null && !existing.getId().equals(prodSku.getId())) {
            throw new RuntimeException("SKU Code already exists: " + prodSku.getSkuCode());
        }

        int rows = prodSkuMapper.update(prodSku);

        // Update tags: delete all and re-insert
        if (prodSku.getTagIds() != null) {
            prodTagSkuMapper.deleteBySkuId(prodSku.getId());
            if (!prodSku.getTagIds().isEmpty()) {
                saveTags(prodSku.getId(), prodSku.getTagIds());
            }
        }

        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int removeById(Long id) {
        prodTagSkuMapper.deleteBySkuId(id);
        return prodSkuMapper.deleteById(id);
    }
    
    @Override
    public int count() {
        return prodSkuMapper.count();
    }

    private void saveTags(Long skuId, List<Long> tagIds) {
        List<ProdTagSku> list = new ArrayList<>();
        long baseId = System.nanoTime(); // Simple ID generation
        for (int i = 0; i < tagIds.size(); i++) {
            ProdTagSku item = new ProdTagSku();
            item.setId(baseId + i);
            item.setSkuId(skuId);
            item.setTagId(tagIds.get(i));
            list.add(item);
        }
        prodTagSkuMapper.batchInsert(list);
    }

    @Override
    public String generateSkuCode() {
        // Strategy: SKU + yyyyMMddHHmmss + 3 random digits
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(new java.util.Date());
        int random = new java.util.Random().nextInt(900) + 100; // 100-999
        return "SKU" + timestamp + random;
    }
}
