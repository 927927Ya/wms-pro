package com.d0dd.wms.shiro;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import java.nio.charset.StandardCharsets;

public class JsonAuthcFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.setStatus(401);
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write("{\"code\":401,\"msg\":\"未登录\",\"data\":null}");
        resp.getWriter().flush();
        return false;
    }
}

