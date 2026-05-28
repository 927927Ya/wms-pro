package com.d0dd.wms.service.impl;

import com.d0dd.wms.entity.ProdCategory;
import com.d0dd.wms.mapper.ProdCategoryMapper;
import com.d0dd.wms.service.ProdCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProdCategoryServiceImpl implements ProdCategoryService {

    @Autowired
    private ProdCategoryMapper prodCategoryMapper;

    @Override
    public ProdCategory getById(Long id) {
        return prodCategoryMapper.selectById(id);
    }

    @Override
    public List<ProdCategory> list(ProdCategory prodCategory) {
        List<ProdCategory> allCategories = prodCategoryMapper.selectList(prodCategory);
        // If search condition is empty, return tree structure
        if (prodCategory == null || 
            (prodCategory.getCategoryName() == null && prodCategory.getStatus() == null)) {
            return buildTree(allCategories);
        }
        return allCategories;
    }

    @Override
    public int save(ProdCategory prodCategory) {
        // Validate name uniqueness
        ProdCategory existing = prodCategoryMapper.selectByName(prodCategory.getCategoryName());
        if (existing != null) {
            throw new RuntimeException("分类名称已存在");
        }
        return prodCategoryMapper.insert(prodCategory);
    }

    @Override
    public int update(ProdCategory prodCategory) {
        ProdCategory old = prodCategoryMapper.selectById(prodCategory.getId());
        if (old == null) {
            throw new RuntimeException("分类不存在");
        }

        ProdCategory existing = prodCategoryMapper.selectByName(prodCategory.getCategoryName());
        if (existing != null && !existing.getId().equals(prodCategory.getId())) {
            throw new RuntimeException("分类名称已存在");
        }

        if (prodCategory.getParentId() != null) {
            Long oldParentId = old.getParentId() == null ? 0L : old.getParentId();
            Long newParentId = prodCategory.getParentId();
            if (newParentId == null) {
                newParentId = 0L;
            }
            if (!newParentId.equals(oldParentId)) {
                throw new RuntimeException("不允许修改上级分类");
            }
        }

        return prodCategoryMapper.update(prodCategory);
    }

    @Override
    @Transactional
    public int removeById(Long id) {
        // Cascading delete: delete this category and all its sub-categories
        // First check if it has children
        if (hasChildren(id)) {
            // Option 1: Recursive delete (simple implementation)
            // Option 2: Throw error (safe implementation) - Req says "delete this and all sub-categories"
            // Let's implement recursive delete for "delete this and all sub-categories"
            deleteChildren(id);
        }
        return prodCategoryMapper.deleteById(id);
    }

    private boolean hasChildren(Long parentId) {
        return prodCategoryMapper.selectCountByParentId(parentId) > 0;
    }

    private void deleteChildren(Long parentId) {
        ProdCategory query = new ProdCategory();
        query.setParentId(parentId);
        List<ProdCategory> children = prodCategoryMapper.selectList(query);
        for (ProdCategory child : children) {
            deleteChildren(child.getId()); // Recursively delete grandchildren
            prodCategoryMapper.deleteById(child.getId());
        }
    }

    /**
     * Build tree structure from flat list
     */
    private List<ProdCategory> buildTree(List<ProdCategory> list) {
        List<ProdCategory> tree = new ArrayList<>();
        Map<Long, List<ProdCategory>> childrenMap = list.stream()
            .filter(c -> c.getParentId() != null)
            .collect(Collectors.groupingBy(ProdCategory::getParentId));

        for (ProdCategory node : list) {
            // Top level categories (parentId is null or 0)
            if (node.getParentId() == null || node.getParentId() == 0L) {
                node.setChildren(getChildren(node.getId(), childrenMap));
                tree.add(node);
            }
        }
        return tree;
    }

    private List<ProdCategory> getChildren(Long parentId, Map<Long, List<ProdCategory>> childrenMap) {
        List<ProdCategory> children = childrenMap.get(parentId);
        if (children != null) {
            for (ProdCategory child : children) {
                child.setChildren(getChildren(child.getId(), childrenMap));
            }
        } else {
            children = new ArrayList<>();
        }
        return children;
    }
}
