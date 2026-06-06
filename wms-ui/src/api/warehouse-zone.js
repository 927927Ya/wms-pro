import request from './request';
export const getZoneList = (params) => request({ url: '/warehouse/zone/list', method: 'get', params });
export const getZoneById = (id) => request({ url: `/warehouse/zone/${id}`, method: 'get' });
export const createZone = (data) => request({ url: '/warehouse/zone', method: 'post', data });
export const updateZone = (data) => request({ url: '/warehouse/zone', method: 'put', data });
export const deleteZone = (id) => request({ url: `/warehouse/zone/${id}`, method: 'delete' });
