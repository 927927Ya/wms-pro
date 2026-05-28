package com.d0dd.wms.service;

import com.d0dd.wms.entity.SysDict;
import java.util.List;

public interface SysDictService {
    SysDict getById(Long id);
    List<SysDict> list(SysDict sysDict);
    int save(SysDict sysDict);
    int update(SysDict sysDict);
    int removeById(Long id);
}
