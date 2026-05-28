package com.d0dd.wms.service;

import com.d0dd.wms.dto.CustomerQueryDto;
import com.d0dd.wms.entity.Customer;
import java.util.List;

public interface CustomerService {
    Customer getById(Long id);
    List<Customer> list(CustomerQueryDto queryDto);
    int save(Customer customer);
    int update(Customer customer);
    int removeById(Long id);
}
