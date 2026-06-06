import request from './request';

export const getInventorySummaryList = (params) => request({ url: '/inventory/summary/list', method: 'get', params });
export const getInventorySummaryById = (id) => request({ url: `/inventory/summary/${id}`, method: 'get' });
export const searchInventorySummary = (params) => request({ url: '/inventory/summary/search', method: 'get', params });

export const getInventoryBatchList = (params) => request({ url: '/inventory/batch/list', method: 'get', params });
export const getInventoryBatchById = (id) => request({ url: `/inventory/batch/${id}`, method: 'get' });
export const getInventoryBatchBySkuId = (skuId) => request({ url: `/inventory/batch/sku/${skuId}`, method: 'get' });

export const getInventoryBinList = (params) => request({ url: '/inventory/bin/list', method: 'get', params });
export const getInventoryBinById = (id) => request({ url: `/inventory/bin/${id}`, method: 'get' });
export const getInventoryBinByBatchId = (batchId) => request({ url: `/inventory/bin/batch/${batchId}`, method: 'get' });

export const getInventoryHistoryList = (params) => request({ url: '/inventory/history/list', method: 'get', params });
