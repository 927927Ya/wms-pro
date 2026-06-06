import request from './request';

export const getInboundList = (params) => request({ url: '/inbound/list', method: 'get', params });
export const searchInbound = (params) => request({ url: '/inbound/search', method: 'get', params });
export const getInboundById = (id) => request({ url: `/inbound/${id}`, method: 'get' });
export const createInbound = (data) => request({ url: '/inbound', method: 'post', data });
export const updateInbound = (data) => request({ url: '/inbound', method: 'put', data });
export const deleteInbound = (id) => request({ url: `/inbound/${id}`, method: 'delete' });

export const getInboundDetailsList = (params) => request({ url: '/inbound/details/list', method: 'get', params });
export const getInboundDetailsById = (id) => request({ url: `/inbound/details/${id}`, method: 'get' });
export const getInboundDetailsByInboundId = (inboundId) => request({ url: `/inbound/details/inbound/${inboundId}`, method: 'get' });
export const createInboundDetails = (data) => request({ url: '/inbound/details', method: 'post', data });
export const updateInboundDetails = (data) => request({ url: '/inbound/details', method: 'put', data });
export const deleteInboundDetails = (id) => request({ url: `/inbound/details/${id}`, method: 'delete' });

export const getReceivingTaskList = (params) => request({ url: '/inbound/receiving/task/list', method: 'get', params });
export const getReceivingTaskById = (id) => request({ url: `/inbound/receiving/task/${id}`, method: 'get' });
export const getReceivingTaskByDetailsId = (inboundDetailsId) => request({ url: `/inbound/receiving/task/details/${inboundDetailsId}`, method: 'get' });
export const createReceivingTask = (data) => request({ url: '/inbound/receiving/task', method: 'post', data });
export const updateReceivingTask = (data) => request({ url: '/inbound/receiving/task', method: 'put', data });
export const deleteReceivingTask = (id) => request({ url: `/inbound/receiving/task/${id}`, method: 'delete' });
export const generateReceivingTasks = (inboundId) => request({ url: `/inbound/receiving/task/generate/${inboundId}`, method: 'post' });
export const saveReceivingTasks = (inboundId, tasks) => request({ url: `/inbound/receiving/task/save/${inboundId}`, method: 'post', data: tasks });
export const completeReceivingTask = (id, binId) => request({ url: `/inbound/receiving/task/complete/${id}`, method: 'post', params: { binId } });
export const cancelReceivingTask = (id) => request({ url: `/inbound/receiving/task/cancel/${id}`, method: 'post' });
