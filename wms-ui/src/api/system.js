import request from './request';

export const getUserList = (data) => request({ url: '/sys/user/list', method: 'post', data });
export const getUserById = (id) => request({ url: `/sys/user/${id}`, method: 'get' });
export const createUser = (data) => request({ url: '/sys/user', method: 'post', data });
export const updateUser = (data) => request({ url: '/sys/user', method: 'put', data });
export const deleteUser = (id) => request({ url: `/sys/user/${id}`, method: 'delete' });
export const getUserProfile = () => request({ url: '/sys/user/profile', method: 'get' });
export const updateUserProfile = (data) => request({ url: '/sys/user/profile', method: 'put', data });
export const updateUserPassword = (data) => request({ url: '/sys/user/password', method: 'put', data });

export const getRoleList = (data) => request({ url: '/sys/role/list', method: 'post', data });
export const getRoleById = (id) => request({ url: `/sys/role/${id}`, method: 'get' });
export const createRole = (data) => request({ url: '/sys/role', method: 'post', data });
export const updateRole = (data) => request({ url: '/sys/role', method: 'put', data });
export const deleteRole = (id) => request({ url: `/sys/role/${id}`, method: 'delete' });
