package com.d0dd.wms.config;

import com.d0dd.wms.entity.SysUser;
import com.d0dd.wms.service.SysUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    private SysUserService sysUserService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // In a real app, we would query roles and permissions from DB
        // info.addRole("admin");
        // info.addStringPermission("sys:user:list");
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken userToken = (UsernamePasswordToken) token;
        SysUser user = sysUserService.getByUserName(userToken.getUsername());
        
        if (user == null) {
            return null; // Unknown user
        }
        
        // Return authentication info with the password from DB
        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
    }
}
