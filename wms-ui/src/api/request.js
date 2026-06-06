import axios from 'axios';
import { message } from 'antd';

const request = axios.create({
  baseURL: '/api',
  timeout: 5000,
});

request.interceptors.request.use(
  (config) => config,
  (error) => Promise.reject(error)
);

request.interceptors.response.use(
  (response) => {
    const { config } = response;
    if (config.responseType === 'blob' || config.responseType === 'arraybuffer') {
      return response.data;
    }
    const { code, msg, data } = response.data;
    if (code === 200) {
      return data;
    }
    if (code === 401) {
      localStorage.removeItem('wms_me');
      localStorage.removeItem('wms_perms');
      window.dispatchEvent(new Event('wms-auth-updated'));
      if (!window.location.pathname.startsWith('/login')) {
        const redirect = encodeURIComponent(
          window.location.pathname + window.location.search
        );
        window.location.href = `/login?redirect=${redirect}`;
      }
      return Promise.reject(new Error(msg || '未登录'));
    }
    message.error(msg || '请求失败');
    return Promise.reject(new Error(msg || 'Error'));
  },
  (error) => {
    if (error?.response?.status === 401) {
      localStorage.removeItem('wms_me');
      localStorage.removeItem('wms_perms');
      window.dispatchEvent(new Event('wms-auth-updated'));
      if (!window.location.pathname.startsWith('/login')) {
        const redirect = encodeURIComponent(
          window.location.pathname + window.location.search
        );
        window.location.href = `/login?redirect=${redirect}`;
      }
      return Promise.reject(error);
    }
    const msg = error?.response?.data?.msg || error?.message || '网络错误';
    message.error(msg);
    return Promise.reject(error);
  }
);

export default request;
