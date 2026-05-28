package com.d0dd.wms.mapper;

import com.d0dd.wms.dto.CustomerQueryDto;
import com.d0dd.wms.entity.Customer;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CustomerMapper {
    Customer selectById(Long id);
    List<Customer> selectList(Customer customer);
    List<Customer> selectListByDto(CustomerQueryDto queryDto);
    int insert(Customer customer);
    int update(Customer customer);
    int deleteById(Long id);
}
