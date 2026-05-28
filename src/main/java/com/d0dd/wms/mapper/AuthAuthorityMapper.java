package com.d0dd.wms.mapper;

import com.d0dd.wms.entity.AuthAuthority;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuthAuthorityMapper {
    List<AuthAuthority> selectAll();
    List<String> selectPermsByUserId(@Param("userId") Long userId);
}

