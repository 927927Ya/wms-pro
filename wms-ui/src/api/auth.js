import request from './request';

export const login = (data) => request({ url: '/login', method: 'post', data });
export const logout = () => request({ url: '/logout', method: 'get' });
export const getCurrentUser = () => request({ url: '/auth/me', method: 'get' });
export const getAuthorities = () => request({ url: '/auth/authority/list', method: 'get' });
