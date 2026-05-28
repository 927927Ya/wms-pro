package com.d0dd.wms.shiro;

import com.d0dd.wms.entity.SysUser;
import com.d0dd.wms.mapper.AuthAuthorityMapper;
import com.d0dd.wms.service.SysUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private AuthAuthorityMapper authAuthorityMapper;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUser user = (SysUser) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        
        List<String> perms = authAuthorityMapper.selectPermsByUserId(user.getUserId());
        if (perms != null) {
            for (String perm : perms) {
                if (perm != null && !perm.isEmpty()) {
                    info.addStringPermission(perm);
                }
            }
        }
        
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();
        
        SysUser user = sysUserService.getByUserName(username);
        if (user == null) {
            return null; // Unknown account
        }
        
        // Assuming plain text password for now based on LoginController logic
        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
    }
}
