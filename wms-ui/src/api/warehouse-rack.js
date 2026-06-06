import request from './request';
export const getRackList = (params) => request({ url: '/warehouse/rack/list', method: 'get', params });
export const getRackById = (id) => request({ url: `/warehouse/rack/${id}`, method: 'get' });
export const createRack = (data) => request({ url: '/warehouse/rack', method: 'post', data });
export const updateRack = (data) => request({ url: '/warehouse/rack', method: 'put', data });
export const deleteRack = (id) => request({ url: `/warehouse/rack/${id}`, method: 'delete' });
