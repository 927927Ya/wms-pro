package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.dto.CustomerQueryDto;
import com.d0dd.wms.entity.Customer;
import com.d0dd.wms.service.CustomerService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/{id}")
    public Result<Customer> getById(@PathVariable Long id) {
        return Result.success(customerService.getById(id));
    }

    @PostMapping("/list")
    public Result<Map<String, Object>> list(@RequestBody CustomerQueryDto queryDto) {

        List<Customer> list = customerService.list(queryDto);

        Map<String, Object> result = new HashMap<>();

        result.put("records", list);

        result.put("total", list.size());

        return Result.success(result);

    }

    @RequiresPermissions("customer:manage")


    @PostMapping
    public Result<String> save(@RequestBody Customer customer) {
        customerService.save(customer);
        return Result.success("Saved successfully");
    }

    @RequiresPermissions("customer:manage")


    @PutMapping
    public Result<String> update(@RequestBody Customer customer) {
        customerService.update(customer);
        return Result.success("Updated successfully");
    }

    @RequiresPermissions("customer:manage")


    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        customerService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
