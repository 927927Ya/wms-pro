package com.d0dd.wms.controller;

import com.d0dd.wms.common.Result;
import com.d0dd.wms.dto.CustomerQueryDto;
import com.d0dd.wms.entity.Customer;
import com.d0dd.wms.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Result<List<Customer>> list(@RequestBody CustomerQueryDto queryDto) {
        return Result.success(customerService.list(queryDto));
    }

    @PostMapping
    public Result<String> save(@RequestBody Customer customer) {
        customerService.save(customer);
        return Result.success("Saved successfully");
    }

    @PutMapping
    public Result<String> update(@RequestBody Customer customer) {
        customerService.update(customer);
        return Result.success("Updated successfully");
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        customerService.removeById(id);
        return Result.success("Deleted successfully");
    }
}
