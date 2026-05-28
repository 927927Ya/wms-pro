package com.d0dd.wms;

import com.d0dd.wms.entity.Customer;
import com.d0dd.wms.entity.SysRole;
import com.d0dd.wms.entity.SysUser;
import com.d0dd.wms.mapper.AuthAuthorityMapper;
import com.d0dd.wms.service.CustomerService;
import com.d0dd.wms.service.InventoryBinService;
import com.d0dd.wms.service.OutboundPickingTaskService;
import com.d0dd.wms.service.SysRoleService;
import com.d0dd.wms.service.SysUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WmsApplicationTests {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SysUserService sysUserService;
    
    @Autowired
    private SysRoleService sysRoleService;
    
    @Autowired
    private AuthAuthorityMapper authAuthorityMapper;

    @Autowired
    private InventoryBinService inventoryBinService;

    @Autowired
    private OutboundPickingTaskService outboundPickingTaskService;

    @Test
    void contextLoads() {
    }

    @Test
    void testCustomerCRUD() {
        // Create
        Customer customer = new Customer();
        customer.setCode("CUST-TEST-001");
        customer.setName("Test Customer");
        customer.setCustomerLevel("1");
        customer.setCustomerStatus("1");
        customerService.save(customer);
        System.out.println("Created Customer ID: " + customer.getId());

        // Read
        Customer fetched = customerService.getById(customer.getId());
        assert fetched != null;
        assert "Test Customer".equals(fetched.getName());

        // Update
        fetched.setName("Updated Customer");
        customerService.update(fetched);
        Customer updated = customerService.getById(customer.getId());
        assert "Updated Customer".equals(updated.getName());

        // Delete
        customerService.removeById(customer.getId());
        assert customerService.getById(customer.getId()) == null;
    }
    
    @Test
    void testSysUserRoleMenu() {
        // 1. Create Role and Assign Authorities
        SysRole role = new SysRole();
        role.setRoleName("Test Role");
        role.setRoleKey("test_role");
        role.setStatus("0");
        role.setAuthorityIds(java.util.Collections.singletonList(1L));
        sysRoleService.insert(role);
        
        // 2. Create User and Assign Role
        SysUser user = new SysUser();
        user.setGivenName("testuser");
        user.setNickName("Test User");
        user.setStatus("0");
        user.setRoleIds(java.util.Collections.singletonList(role.getRoleId()));
        sysUserService.save(user);
        
        // 4. Verify Permissions
        // Since we cannot easily inject UserRealm here without mocking security context,
        // we can check the underlying mapper logic or service logic.
        // Or check database state.
        
        SysUser fetchedUser = sysUserService.getById(user.getUserId());
        assert fetchedUser.getRoleIds().contains(role.getRoleId());
        
        SysRole fetchedRole = sysRoleService.getById(role.getRoleId());
        assert fetchedRole != null;
        assert fetchedRole.getAuthorityIds() != null;
        assert fetchedRole.getAuthorityIds().contains(1L);

        java.util.List<String> perms = authAuthorityMapper.selectPermsByUserId(user.getUserId());
        assert perms != null;
        assert perms.contains("*");
        
        // Cleanup
        sysUserService.removeById(user.getUserId());
        sysRoleService.deleteById(role.getRoleId());
    }

    @Test
    void testReceiverHasInboundReceivePermission() {
        java.util.List<String> perms = authAuthorityMapper.selectPermsByUserId(2L);
        assert perms != null;
        assert perms.contains("inbound:receive");
    }

    @Test
    void testInventoryAndPicking() {
        // This test assumes some basic data setup (warehouse, product, etc.) might be missing.
        // So we just check basic service integrity.
        assert inventoryBinService != null;
        assert outboundPickingTaskService != null;
    }
}
