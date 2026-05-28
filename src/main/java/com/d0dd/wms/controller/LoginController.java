package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.dto.LoginBody;
import com.d0dd.wms.entity.SysUser;
import com.d0dd.wms.service.SysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/login")
    public Result<SysUser> login(@RequestBody LoginBody loginBody) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(loginBody.getUsername(), loginBody.getPassword());
        
        try {
            subject.login(token);
            SysUser user = (SysUser) subject.getPrincipal();
            return Result.success(user);
        } catch (UnknownAccountException e) {
            return Result.error("User not found");
        } catch (IncorrectCredentialsException e) {
            return Result.error("Invalid password");
        } catch (AuthenticationException e) {
            return Result.error("Login failed: " + e.getMessage());
        }
    }

    @GetMapping("/logout")
    public Result<String> logout() {
        SecurityUtils.getSubject().logout();
        return Result.success("Logged out successfully");
    }
}
