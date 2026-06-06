import request from './request';

export const getCustomerList = (data) => request({ url: '/customer/list', method: 'post', data });
export const getCustomerById = (id) => request({ url: `/customer/${id}`, method: 'get' });
export const createCustomer = (data) => request({ url: '/customer', method: 'post', data });
export const updateCustomer = (data) => request({ url: '/customer', method: 'put', data });
export const deleteCustomer = (id) => request({ url: `/customer/${id}`, method: 'delete' });
