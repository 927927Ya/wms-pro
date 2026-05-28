package com.d0dd.wms.config;

import com.d0dd.wms.shiro.JsonAuthcFilter;
import com.d0dd.wms.shiro.UserRealm;
import jakarta.servlet.Filter;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    public UserRealm userRealm() {
        return new UserRealm();
    }

    @Bean("customSecurityManager")
    @org.springframework.context.annotation.Primary
    public SecurityManager customSecurityManager(UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@org.springframework.beans.factory.annotation.Qualifier("customSecurityManager") SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        
        Map<String, Filter> filters = new HashMap<>();
        filters.put("authc", new JsonAuthcFilter());
        shiroFilterFactoryBean.setFilters(filters);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        
        filterChainDefinitionMap.put("/api/login", "anon");
        filterChainDefinitionMap.put("/api/v3/api-docs/**", "anon");
        filterChainDefinitionMap.put("/swagger-ui/**", "anon");
        filterChainDefinitionMap.put("/api/**", "authc");
        filterChainDefinitionMap.put("/uploads/**", "anon");
        filterChainDefinitionMap.put("/**", "anon");
        
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator c = new DefaultAdvisorAutoProxyCreator();
        c.setProxyTargetClass(true);
        return c;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@org.springframework.beans.factory.annotation.Qualifier("customSecurityManager") SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
