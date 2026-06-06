import request from './request';
export const getBinList = (params) => request({ url: '/warehouse/bin/list', method: 'get', params });
export const getBinById = (id) => request({ url: `/warehouse/bin/${id}`, method: 'get' });
export const createBin = (data) => request({ url: '/warehouse/bin', method: 'post', data });
export const updateBin = (data) => request({ url: '/warehouse/bin', method: 'put', data });
export const deleteBin = (id) => request({ url: `/warehouse/bin/${id}`, method: 'delete' });
