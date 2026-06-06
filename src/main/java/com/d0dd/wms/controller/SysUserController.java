package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.entity.SysUser;
import com.d0dd.wms.service.SysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/list")
    public Result<Map<String, Object>> list(@RequestBody com.d0dd.wms.dto.SysUserQueryDto queryDto) {

        List<SysUser> list = sysUserService.list(queryDto);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }

    @GetMapping("/{id}")
    public Result<SysUser> getById(@PathVariable Long id) {
        return Result.success(sysUserService.getById(id));
    }

    @RequiresPermissions("sys:user:manage")


    @PostMapping
    public Result<String> save(@RequestBody SysUser sysUser) {
        sysUserService.save(sysUser);
        return Result.success("Created successfully");
    }

    @RequiresPermissions("sys:user:manage")


    @PutMapping
    public Result<String> update(@RequestBody SysUser sysUser) {
        sysUserService.update(sysUser);
        return Result.success("Updated successfully");
    }

    @GetMapping("/profile")
    public Result<SysUser> profile() {
        Long userId = currentUserId();
        return Result.success(sysUserService.getById(userId));
    }

    @RequiresPermissions("sys:user:manage")


    @PutMapping("/profile")
    public Result<String> updateProfile(@RequestBody SysUser sysUser) {
        Long userId = currentUserId();
        sysUser.setUserId(userId);
        sysUser.setPassword(null);
        sysUser.setRoleIds(null);
        sysUserService.updateProfile(sysUser);
        return Result.success("Updated successfully");
    }

    public static class PasswordBody {
        private String oldPassword;
        private String newPassword;

        public String getOldPassword() {
            return oldPassword;
        }

        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }

    @RequiresPermissions("sys:user:manage")


    @PutMapping("/password")
    public Result<String> updatePassword(@RequestBody PasswordBody body) {
        if (body == null || body.getOldPassword() == null || body.getOldPassword().trim().isEmpty()) {
            return Result.error("请输入旧密码");
        }
        if (body.getNewPassword() == null || body.getNewPassword().trim().isEmpty()) {
            return Result.error("请输入新密码");
        }

        Long userId = currentUserId();
        SysUser current = sysUserService.getById(userId);
        if (current == null) return Result.error("用户不存在");
        String currentPwd = current.getPassword() == null ? "" : current.getPassword();
        if (!currentPwd.equals(body.getOldPassword())) return Result.error("旧密码不正确");

        sysUserService.updatePassword(userId, body.getNewPassword());
        SecurityUtils.getSubject().logout();
        return Result.success("密码修改成功");
    }

    @RequiresPermissions("sys:user:manage")


    @DeleteMapping("/{id}")
    public Result<String> remove(@PathVariable Long id) {
        sysUserService.removeById(id);
        return Result.success("Deleted successfully");
    }

    private Long currentUserId() {
        Subject subject = SecurityUtils.getSubject();
        SysUser user = (SysUser) subject.getPrincipal();
        if (user == null || user.getUserId() == null) {
            throw new RuntimeException("未登录");
        }
        return user.getUserId();
    }
}
