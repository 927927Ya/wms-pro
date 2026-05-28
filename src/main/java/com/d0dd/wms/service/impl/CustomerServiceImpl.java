package com.d0dd.wms.service.impl;

import com.d0dd.wms.dto.CustomerQueryDto;
import com.d0dd.wms.entity.Customer;
import com.d0dd.wms.mapper.CustomerMapper;
import com.d0dd.wms.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public Customer getById(Long id) {
        return customerMapper.selectById(id);
    }

    @Override
    public List<Customer> list(CustomerQueryDto queryDto) {
        return customerMapper.selectListByDto(queryDto);
    }

    @Override
    public int save(Customer customer) {
        return customerMapper.insert(customer);
    }

    @Override
    public int update(Customer customer) {
        return customerMapper.update(customer);
    }

    @Override
    public int removeById(Long id) {
        return customerMapper.deleteById(id);
    }
}
