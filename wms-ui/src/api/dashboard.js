import request from './request';
export const getDashboardData = () => request({ url: '/dashboard/data', method: 'get' });
