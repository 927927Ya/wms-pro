import request from './request';

export const getOutboundList = (params) => request({ url: '/outbound/list', method: 'get', params });
export const searchOutbound = (params) => request({ url: '/outbound/search', method: 'get', params });
export const getOutboundById = (id) => request({ url: `/outbound/${id}`, method: 'get' });
export const createOutbound = (data) => request({ url: '/outbound', method: 'post', data });
export const updateOutbound = (data) => request({ url: '/outbound', method: 'put', data });
export const updateOutboundDto = (data) => request({ url: '/outbound/dto', method: 'put', data });
export const deleteOutbound = (id) => request({ url: `/outbound/${id}`, method: 'delete' });
export const getAvailableSkuList = (params) => request({ url: '/outbound/available-sku/list', method: 'get', params });

export const getOutboundDetailsList = (params) => request({ url: '/outbound/details/list', method: 'get', params });
export const getOutboundDetailsById = (id) => request({ url: `/outbound/details/${id}`, method: 'get' });
export const getOutboundDetailsByOutboundId = (outboundId) => request({ url: `/outbound/details/outbound/${outboundId}`, method: 'get' });
export const createOutboundDetails = (data) => request({ url: '/outbound/details', method: 'post', data });
export const updateOutboundDetails = (data) => request({ url: '/outbound/details', method: 'put', data });
export const deleteOutboundDetails = (id) => request({ url: `/outbound/details/${id}`, method: 'delete' });

export const getPickingTaskList = (params) => request({ url: '/outbound/picking/task/list', method: 'get', params });
export const getPickingTaskById = (id) => request({ url: `/outbound/picking/task/${id}`, method: 'get' });
export const getPickingTaskByDetailsId = (outboundDetailsId) => request({ url: `/outbound/picking/task/details/${outboundDetailsId}`, method: 'get' });
export const createPickingTask = (data) => request({ url: '/outbound/picking/task', method: 'post', data });
export const updatePickingTask = (data) => request({ url: '/outbound/picking/task', method: 'put', data });
export const deletePickingTask = (id) => request({ url: `/outbound/picking/task/${id}`, method: 'delete' });
export const generatePickingTasks = (outboundId) => request({ url: `/outbound/picking/task/generate/${outboundId}`, method: 'post' });
export const savePickingTasks = (outboundId, tasks) => request({ url: `/outbound/picking/task/save/${outboundId}`, method: 'post', data: tasks });
export const completePickingTask = (id, binId) => request({ url: `/outbound/picking/task/complete/${id}`, method: 'post', params: { binId } });
export const cancelPickingTask = (id) => request({ url: `/outbound/picking/task/cancel/${id}`, method: 'post' });
