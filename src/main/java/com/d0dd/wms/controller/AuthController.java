package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.entity.AuthAuthority;
import com.d0dd.wms.entity.SysUser;
import com.d0dd.wms.mapper.AuthAuthorityMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthAuthorityMapper authAuthorityMapper;

    @GetMapping("/me")
    public Result<Map<String, Object>> me() {
        Subject subject = SecurityUtils.getSubject();
        SysUser user = (SysUser) subject.getPrincipal();

        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("perms", authAuthorityMapper.selectPermsByUserId(user.getUserId()));
        return Result.success(data);
    }

    @GetMapping("/authority/list")
    public Result<Map<String, Object>> listAuthorities() {

        List<AuthAuthority> list = authAuthorityMapper.selectAll();

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }
}

