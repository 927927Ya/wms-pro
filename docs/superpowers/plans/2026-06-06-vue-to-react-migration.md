# Vue → React 迁移实施计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 wms-ui 前端从 Vue 3 + Element Plus 完整迁移到 React 18 + Ant Design 5 + Zustand。

**Architecture:** Vite + React SPA，Ant Design Layout 布局，Zustand 管理认证状态，Axios 实例通过 /api 代理到 Spring Boot 后端（8080）。先用 H2 内存数据库跑通开发，后续切 MySQL。

**Tech Stack:** React 18, Vite 7, Ant Design 5, Zustand, React Router 6, Axios, JavaScript

---

## 后端 API 关键约定

- 所有响应：`{ code: 200, msg: "success", data: ... }`，code 非 200 为错误，code=401 未登录
- 认证：Shiro session (cookie)，登录 POST `/api/login` `{username, password}`
- 用户信息：GET `/api/auth/me` → `{ user: {...}, perms: [...] }`
- localStorage key: `wms_me`（用户对象）, `wms_perms`（权限数组）
- axios baseURL: `/api`，response 拦截器：code===200 返回 data，否则 reject

---

## Phase 1: 基础设施

### Task 1: 初始化 React + Vite 项目

**Files:**
- Replace: `wms-ui/package.json`
- Create: `wms-ui/vite.config.js`
- Create: `wms-ui/index.html`
- Create: `wms-ui/.gitignore`

- [ ] **Step 1: 备份旧的 dist 和 Docker 相关文件**

```bash
mkdir -p wms-ui-backup
cp -r wms-ui/dist wms-ui-backup/
cp wms-ui/Dockerfile wms-ui-backup/
cp wms-ui/nginx.conf wms-ui-backup/
cp wms-ui/.dockerignore wms-ui-backup/
cp wms-ui/.npmrc wms-ui-backup/
```

- [ ] **Step 2: 写入新 package.json**

```json
{
  "name": "wms-ui-react",
  "private": true,
  "version": "1.0.0",
  "type": "module",
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview --port 4173"
  },
  "dependencies": {
    "react": "^18.3.1",
    "react-dom": "^18.3.1",
    "react-router-dom": "^6.26.0",
    "antd": "^5.21.0",
    "@ant-design/icons": "^5.4.0",
    "axios": "^1.7.0",
    "zustand": "^4.5.0",
    "dayjs": "^1.11.0"
  },
  "devDependencies": {
    "@vitejs/plugin-react": "^4.3.0",
    "vite": "^5.4.0"
  }
}
```

- [ ] **Step 3: 写入 vite.config.js**

```js
import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: process.env.VITE_BACKEND_URL || 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
  preview: {
    port: 4173,
    proxy: {
      '/api': {
        target: process.env.VITE_BACKEND_URL || 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
});
```

- [ ] **Step 4: 写入 index.html**

```html
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8" />
    <link rel="icon" type="image/svg+xml" href="/vite.svg" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>WMS 仓库管理系统</title>
  </head>
  <body>
    <div id="root"></div>
    <script type="module" src="/src/main.jsx"></script>
  </body>
</html>
```

- [ ] **Step 5: 写入 .gitignore**

```
node_modules
dist
*.local
```

- [ ] **Step 6: 安装依赖**

```bash
cd wms-ui && npm install
```

---

### Task 2: API 层 - axios 实例 + 拦截器

**Files:**
- Create: `wms-ui/src/api/request.js`

- [ ] **Step 1: 创建 request.js**

```js
import axios from 'axios';
import { message } from 'antd';

const request = axios.create({
  baseURL: '/api',
  timeout: 5000,
});

// 请求拦截器（暂不处理，Shiro cookie 自动携带）
request.interceptors.request.use(
  (config) => config,
  (error) => Promise.reject(error)
);

// 响应拦截器
request.interceptors.response.use(
  (response) => {
    const { config } = response;
    // Blob/ArrayBuffer 直接返回
    if (config.responseType === 'blob' || config.responseType === 'arraybuffer') {
      return response.data;
    }

    const { code, msg, data } = response.data;

    if (code === 200) {
      return data;
    }

    // 401 未登录
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

    // 其他业务错误
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
```

---

### Task 3: API 模块

**Files:**
- Create: `wms-ui/src/api/auth.js`
- Create: `wms-ui/src/api/warehouse.js`
- Create: `wms-ui/src/api/warehouse-zone.js`
- Create: `wms-ui/src/api/warehouse-rack.js`
- Create: `wms-ui/src/api/warehouse-bin.js`
- Create: `wms-ui/src/api/inbound.js`
- Create: `wms-ui/src/api/outbound.js`
- Create: `wms-ui/src/api/inventory.js`
- Create: `wms-ui/src/api/product.js`
- Create: `wms-ui/src/api/customer.js`
- Create: `wms-ui/src/api/system.js`

- [ ] **Step 1: 创建 auth.js**

```js
import request from './request';

// 登录
export const login = (data) => request({ url: '/login', method: 'post', data });

// 登出
export const logout = () => request({ url: '/logout', method: 'get' });

// 获取当前用户信息和权限
export const getCurrentUser = () => request({ url: '/auth/me', method: 'get' });

// 获取权限列表
export const getAuthorities = () => request({ url: '/auth/authority/list', method: 'get' });

// 修改密码
export const updatePassword = (data) => request({ url: '/auth/password', method: 'put', data });
```

- [ ] **Step 2: 创建 warehouse.js**

```js
import request from './request';

// 仓库
export const getWarehouseList = (params) => request({ url: '/warehouse/list', method: 'get', params });
export const getWarehouseById = (id) => request({ url: `/warehouse/${id}`, method: 'get' });
export const createWarehouse = (data) => request({ url: '/warehouse', method: 'post', data });
export const updateWarehouse = (data) => request({ url: '/warehouse', method: 'put', data });
export const deleteWarehouse = (id) => request({ url: `/warehouse/${id}`, method: 'delete' });

// 库区
export const getZoneList = (params) => request({ url: '/warehouse/zone/list', method: 'get', params });
export const getZoneById = (id) => request({ url: `/warehouse/zone/${id}`, method: 'get' });
export const createZone = (data) => request({ url: '/warehouse/zone', method: 'post', data });
export const updateZone = (data) => request({ url: '/warehouse/zone', method: 'put', data });
export const deleteZone = (id) => request({ url: `/warehouse/zone/${id}`, method: 'delete' });

// 货架
export const getRackList = (params) => request({ url: '/warehouse/rack/list', method: 'get', params });
export const getRackById = (id) => request({ url: `/warehouse/rack/${id}`, method: 'get' });
export const createRack = (data) => request({ url: '/warehouse/rack', method: 'post', data });
export const updateRack = (data) => request({ url: '/warehouse/rack', method: 'put', data });
export const deleteRack = (id) => request({ url: `/warehouse/rack/${id}`, method: 'delete' });

// 库位
export const getBinList = (params) => request({ url: '/warehouse/bin/list', method: 'get', params });
export const getBinById = (id) => request({ url: `/warehouse/bin/${id}`, method: 'get' });
export const createBin = (data) => request({ url: '/warehouse/bin', method: 'post', data });
export const updateBin = (data) => request({ url: '/warehouse/bin', method: 'put', data });
export const deleteBin = (id) => request({ url: `/warehouse/bin/${id}`, method: 'delete' });
```

- [ ] **Step 3: 创建 warehouse-zone.js、warehouse-rack.js、warehouse-bin.js**

这三个文件内容与 warehouse.js 中对应模块相同，但独立导出。

`warehouse-zone.js`:
```js
import request from './request';
export const getZoneList = (params) => request({ url: '/warehouse/zone/list', method: 'get', params });
export const getZoneById = (id) => request({ url: `/warehouse/zone/${id}`, method: 'get' });
export const createZone = (data) => request({ url: '/warehouse/zone', method: 'post', data });
export const updateZone = (data) => request({ url: '/warehouse/zone', method: 'put', data });
export const deleteZone = (id) => request({ url: `/warehouse/zone/${id}`, method: 'delete' });
```

`warehouse-rack.js`:
```js
import request from './request';
export const getRackList = (params) => request({ url: '/warehouse/rack/list', method: 'get', params });
export const getRackById = (id) => request({ url: `/warehouse/rack/${id}`, method: 'get' });
export const createRack = (data) => request({ url: '/warehouse/rack', method: 'post', data });
export const updateRack = (data) => request({ url: '/warehouse/rack', method: 'put', data });
export const deleteRack = (id) => request({ url: `/warehouse/rack/${id}`, method: 'delete' });
```

`warehouse-bin.js`:
```js
import request from './request';
export const getBinList = (params) => request({ url: '/warehouse/bin/list', method: 'get', params });
export const getBinById = (id) => request({ url: `/warehouse/bin/${id}`, method: 'get' });
export const createBin = (data) => request({ url: '/warehouse/bin', method: 'post', data });
export const updateBin = (data) => request({ url: '/warehouse/bin', method: 'put', data });
export const deleteBin = (id) => request({ url: `/warehouse/bin/${id}`, method: 'delete' });
```

- [ ] **Step 4: 创建 inbound.js**

```js
import request from './request';

// 入库单
export const getInboundList = (params) => request({ url: '/inbound/list', method: 'get', params });
export const getInboundById = (id) => request({ url: `/inbound/${id}`, method: 'get' });
export const createInbound = (data) => request({ url: '/inbound', method: 'post', data });
export const updateInbound = (data) => request({ url: '/inbound', method: 'put', data });
export const deleteInbound = (id) => request({ url: `/inbound/${id}`, method: 'delete' });
export const confirmInbound = (id) => request({ url: `/inbound/${id}/confirm`, method: 'put' });
export const importInbound = (data) => request({ url: '/inbound/import', method: 'post', data });

// 收货任务
export const getReceivingTaskList = (params) => request({ url: '/inbound/receiving/list', method: 'get', params });
export const createReceivingTask = (data) => request({ url: '/inbound/receiving', method: 'post', data });
export const completeReceiving = (id, data) => request({ url: `/inbound/receiving/${id}/complete`, method: 'put', data });

// 托盘收货
export const getPalletReceivingList = (params) => request({ url: '/inbound/pallet/list', method: 'get', params });
export const startPalletReceiving = (data) => request({ url: '/inbound/pallet/start', method: 'post', data });
export const closePallet = (id) => request({ url: `/inbound/pallet/${id}/close`, method: 'put' });
```

- [ ] **Step 5: 创建 outbound.js**

```js
import request from './request';

// 出库单
export const getOutboundList = (params) => request({ url: '/outbound/list', method: 'get', params });
export const getOutboundById = (id) => request({ url: `/outbound/${id}`, method: 'get' });
export const createOutbound = (data) => request({ url: '/outbound', method: 'post', data });
export const updateOutbound = (data) => request({ url: '/outbound', method: 'put', data });
export const deleteOutbound = (id) => request({ url: `/outbound/${id}`, method: 'delete' });
export const confirmOutbound = (id) => request({ url: `/outbound/${id}/confirm`, method: 'put' });
export const importOutbound = (data) => request({ url: '/outbound/import', method: 'post', data });

// 拣货任务
export const getPickingTaskList = (params) => request({ url: '/outbound/picking/list', method: 'get', params });
export const createPickingTask = (data) => request({ url: '/outbound/picking', method: 'post', data });
export const completePicking = (id, data) => request({ url: `/outbound/picking/${id}/complete`, method: 'put', data });

// 托盘拣货
export const getPalletPickingList = (params) => request({ url: '/outbound/pallet/list', method: 'get', params });
export const startPalletPicking = (data) => request({ url: '/outbound/pallet/start', method: 'post', data });
export const closePickingPallet = (id) => request({ url: `/outbound/pallet/${id}/close`, method: 'put' });
```

- [ ] **Step 6: 创建 inventory.js**

```js
import request from './request';

export const getInventoryList = (params) => request({ url: '/inventory/list', method: 'get', params });
export const getInventoryDetail = (id) => request({ url: `/inventory/${id}`, method: 'get' });
export const getInventoryBatchList = (params) => request({ url: '/inventory/batch/list', method: 'get', params });
export const getInventoryByBin = (params) => request({ url: '/inventory/bin/list', method: 'get', params });
```

- [ ] **Step 7: 创建 product.js**

```js
import request from './request';

// SKU
export const getSkuList = (params) => request({ url: '/product/sku/list', method: 'get', params });
export const getSkuById = (id) => request({ url: `/product/sku/${id}`, method: 'get' });
export const createSku = (data) => request({ url: '/product/sku', method: 'post', data });
export const updateSku = (data) => request({ url: '/product/sku', method: 'put', data });
export const deleteSku = (id) => request({ url: `/product/sku/${id}`, method: 'delete' });
export const importSku = (data) => request({ url: '/product/sku/import', method: 'post', data });

// 分类
export const getCategoryList = (params) => request({ url: '/product/category/list', method: 'get', params });
export const getCategoryTree = () => request({ url: '/product/category/tree', method: 'get' });
export const getCategoryById = (id) => request({ url: `/product/category/${id}`, method: 'get' });
export const createCategory = (data) => request({ url: '/product/category', method: 'post', data });
export const updateCategory = (data) => request({ url: '/product/category', method: 'put', data });
export const deleteCategory = (id) => request({ url: `/product/category/${id}`, method: 'delete' });

// 标签
export const getTagList = (params) => request({ url: '/product/tag/list', method: 'get', params });
export const getTagById = (id) => request({ url: `/product/tag/${id}`, method: 'get' });
export const createTag = (data) => request({ url: '/product/tag', method: 'post', data });
export const updateTag = (data) => request({ url: '/product/tag', method: 'put', data });
export const deleteTag = (id) => request({ url: `/product/tag/${id}`, method: 'delete' });

// 单位
export const getUnitList = (params) => request({ url: '/product/unit/list', method: 'get', params });
export const getUnitById = (id) => request({ url: `/product/unit/${id}`, method: 'get' });
export const createUnit = (data) => request({ url: '/product/unit', method: 'post', data });
export const updateUnit = (data) => request({ url: '/product/unit', method: 'put', data });
export const deleteUnit = (id) => request({ url: `/product/unit/${id}`, method: 'delete' });

// 存放类型
export const getStorageTypeList = (params) => request({ url: '/product/storage-type/list', method: 'get', params });
export const getStorageTypeById = (id) => request({ url: `/product/storage-type/${id}`, method: 'get' });
export const createStorageType = (data) => request({ url: '/product/storage-type', method: 'post', data });
export const updateStorageType = (data) => request({ url: '/product/storage-type', method: 'put', data });
export const deleteStorageType = (id) => request({ url: `/product/storage-type/${id}`, method: 'delete' });
```

- [ ] **Step 8: 创建 customer.js**

```js
import request from './request';

export const getCustomerList = (params) => request({ url: '/customer/list', method: 'get', params });
export const getCustomerById = (id) => request({ url: `/customer/${id}`, method: 'get' });
export const createCustomer = (data) => request({ url: '/customer', method: 'post', data });
export const updateCustomer = (data) => request({ url: '/customer', method: 'put', data });
export const deleteCustomer = (id) => request({ url: `/customer/${id}`, method: 'delete' });
```

- [ ] **Step 9: 创建 system.js**

```js
import request from './request';

// 用户管理
export const getUserList = (params) => request({ url: '/system/user/list', method: 'get', params });
export const getUserById = (id) => request({ url: `/system/user/${id}`, method: 'get' });
export const createUser = (data) => request({ url: '/system/user', method: 'post', data });
export const updateUser = (data) => request({ url: '/system/user', method: 'put', data });
export const deleteUser = (id) => request({ url: `/system/user/${id}`, method: 'delete' });
export const resetPassword = (id, data) => request({ url: `/system/user/${id}/reset-password`, method: 'put', data });

// 角色管理
export const getRoleList = (params) => request({ url: '/system/role/list', method: 'get', params });
export const getRoleById = (id) => request({ url: `/system/role/${id}`, method: 'get' });
export const createRole = (data) => request({ url: '/system/role', method: 'post', data });
export const updateRole = (data) => request({ url: '/system/role', method: 'put', data });
export const deleteRole = (id) => request({ url: `/system/role/${id}`, method: 'delete' });
```

---

### Task 4: Zustand 状态管理

**Files:**
- Create: `wms-ui/src/stores/useAuth.js`
- Create: `wms-ui/src/stores/useApp.js`

- [ ] **Step 1: 创建 useAuth.js**

```js
import { create } from 'zustand';
import { getCurrentUser } from '../api/auth';

const useAuth = create((set, get) => ({
  // 状态
  user: JSON.parse(localStorage.getItem('wms_me') || 'null'),
  permissions: JSON.parse(localStorage.getItem('wms_perms') || '[]'),
  initialized: false,

  // 设置用户信息
  setUser: (user) => {
    localStorage.setItem('wms_me', JSON.stringify(user));
    set({ user });
  },

  // 设置权限
  setPermissions: (permissions) => {
    localStorage.setItem('wms_perms', JSON.stringify(permissions));
    set({ permissions });
  },

  // 初始化认证状态（从后端获取）
  initAuth: async () => {
    const { user } = get();
    // 如果内存中已有用户且 localStorage 有数据，直接使用
    if (user) {
      set({ initialized: true });
      return true;
    }

    // 尝试从后端获取
    try {
      const data = await getCurrentUser();
      if (data && data.user) {
        get().setUser(data.user);
        get().setPermissions(data.perms || []);
        set({ initialized: true });
        return true;
      }
    } catch {
      // 获取失败，清除
      get().clearAuth();
    }

    set({ initialized: true });
    return false;
  },

  // 清除认证
  clearAuth: () => {
    localStorage.removeItem('wms_me');
    localStorage.removeItem('wms_perms');
    set({ user: null, permissions: [], initialized: true });
  },

  // 退出登录
  logout: async () => {
    try {
      const { logout } = await import('../api/auth');
      await logout();
    } catch {
      // 即使后端登出失败也清除前端状态
    }
    get().clearAuth();
  },

  // 检查权限
  hasPermission: (perm) => {
    const { permissions } = get();
    return permissions.includes('*') || permissions.includes(perm);
  },
}));

export default useAuth;
```

- [ ] **Step 2: 创建 useApp.js**

```js
import { create } from 'zustand';

const useApp = create((set) => ({
  sidebarCollapsed: false,
  toggleSidebar: () => set((state) => ({ sidebarCollapsed: !state.sidebarCollapsed })),
}));

export default useApp;
```

---

### Task 5: 布局组件

**Files:**
- Create: `wms-ui/src/layouts/MainLayout.jsx`

- [ ] **Step 1: 创建 MainLayout.jsx**

```jsx
import { useState } from 'react';
import { Outlet, useNavigate, useLocation } from 'react-router-dom';
import { Layout, Menu, Button, Dropdown, Avatar, theme } from 'antd';
import {
  DashboardOutlined,
  ShopOutlined,
  InboxOutlined,
  ExportOutlined,
  DatabaseOutlined,
  AppstoreOutlined,
  TeamOutlined,
  SettingOutlined,
  UserOutlined,
  LogoutOutlined,
  MenuFoldOutlined,
  MenuUnfoldOutlined,
} from '@ant-design/icons';
import useAuth from '../stores/useAuth';
import useApp from '../stores/useApp';

const { Header, Sider, Content } = Layout;

const menuItems = [
  {
    key: '/dashboard',
    icon: <DashboardOutlined />,
    label: '仪表盘',
  },
  {
    key: '/warehouse',
    icon: <ShopOutlined />,
    label: '仓库管理',
    children: [
      { key: '/warehouse', label: '仓库列表' },
      { key: '/warehouse/zone', label: '库区管理' },
      { key: '/warehouse/rack', label: '货架管理' },
      { key: '/warehouse/bin', label: '库位管理' },
    ],
  },
  {
    key: '/inbound',
    icon: <InboxOutlined />,
    label: '入库管理',
    children: [
      { key: '/inbound', label: '入库单' },
      { key: '/inbound/receiving-task', label: '收货任务' },
      { key: '/inbound/pallet-receiving', label: '托盘收货' },
    ],
  },
  {
    key: '/outbound',
    icon: <ExportOutlined />,
    label: '出库管理',
    children: [
      { key: '/outbound', label: '出库单' },
      { key: '/outbound/picking-task', label: '拣货任务' },
      { key: '/outbound/pallet-picking', label: '托盘拣货' },
    ],
  },
  {
    key: '/inventory',
    icon: <DatabaseOutlined />,
    label: '库存管理',
  },
  {
    key: '/product',
    icon: <AppstoreOutlined />,
    label: '产品管理',
    children: [
      { key: '/product/sku', label: '产品列表' },
      { key: '/product/category', label: '产品分类' },
      { key: '/product/tag', label: '产品标签' },
      { key: '/product/unit', label: '单位管理' },
      { key: '/product/storage-type', label: '存放类型' },
    ],
  },
  {
    key: '/customer',
    icon: <TeamOutlined />,
    label: '客户管理',
  },
  {
    key: '/system',
    icon: <SettingOutlined />,
    label: '系统管理',
    children: [
      { key: '/system/user', label: '用户管理' },
      { key: '/system/role', label: '角色管理' },
    ],
  },
];

const MainLayout = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { user, logout } = useAuth();
  const { sidebarCollapsed, toggleSidebar } = useApp();
  const {
    token: { colorBgContainer },
  } = theme.useToken();

  // 根据当前路径确定选中的菜单项和展开的菜单
  const getSelectedKeys = () => {
    const path = location.pathname;
    return [path];
  };

  const getOpenKeys = () => {
    const path = location.pathname;
    const parts = path.split('/').filter(Boolean);
    if (parts.length > 1) {
      return ['/' + parts.slice(0, parts.length - 1).join('/')];
    }
    return [];
  };

  const handleMenuClick = ({ key }) => {
    navigate(key);
  };

  const handleLogout = async () => {
    await logout();
    navigate('/login');
  };

  const userMenuItems = [
    {
      key: 'profile',
      icon: <UserOutlined />,
      label: '个人中心',
      onClick: () => navigate('/profile'),
    },
    { type: 'divider' },
    {
      key: 'logout',
      icon: <LogoutOutlined />,
      label: '退出登录',
      onClick: handleLogout,
    },
  ];

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider
        trigger={null}
        collapsible
        collapsed={sidebarCollapsed}
        theme="dark"
        width={220}
      >
        <div
          style={{
            height: 64,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: '#fff',
            fontSize: sidebarCollapsed ? 16 : 20,
            fontWeight: 'bold',
            whiteSpace: 'nowrap',
            overflow: 'hidden',
          }}
        >
          {sidebarCollapsed ? 'WMS' : 'WMS 仓库管理系统'}
        </div>
        <Menu
          theme="dark"
          mode="inline"
          selectedKeys={getSelectedKeys()}
          defaultOpenKeys={getOpenKeys()}
          items={menuItems}
          onClick={handleMenuClick}
        />
      </Sider>
      <Layout>
        <Header
          style={{
            padding: '0 24px',
            background: colorBgContainer,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'space-between',
            boxShadow: '0 1px 4px rgba(0,0,0,0.08)',
          }}
        >
          <Button
            type="text"
            icon={sidebarCollapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
            onClick={toggleSidebar}
          />
          <Dropdown menu={{ items: userMenuItems }} placement="bottomRight">
            <div style={{ cursor: 'pointer', display: 'flex', alignItems: 'center', gap: 8 }}>
              <Avatar icon={<UserOutlined />} />
              <span>{user?.nickName || user?.userName || '用户'}</span>
            </div>
          </Dropdown>
        </Header>
        <Content
          style={{
            margin: 24,
            padding: 24,
            background: colorBgContainer,
            borderRadius: 8,
            minHeight: 280,
            overflow: 'auto',
          }}
        >
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  );
};

export default MainLayout;
```

---

### Task 6: 路由配置 + app 入口

**Files:**
- Create: `wms-ui/src/routes.jsx`
- Create: `wms-ui/src/App.jsx`
- Create: `wms-ui/src/main.jsx`

- [ ] **Step 1: 创建 routes.jsx**

```jsx
import { lazy } from 'react';
import MainLayout from './layouts/MainLayout';
import AuthGuard from './components/AuthGuard';

// 懒加载页面
const Login = lazy(() => import('./pages/login'));
const Dashboard = lazy(() => import('./pages/dashboard'));
const Profile = lazy(() => import('./pages/profile'));

const WarehouseList = lazy(() => import('./pages/warehouse'));
const WarehouseForm = lazy(() => import('./pages/warehouse/form'));
const ZoneList = lazy(() => import('./pages/warehouse/zone'));
const ZoneForm = lazy(() => import('./pages/warehouse/zone/form'));
const RackList = lazy(() => import('./pages/warehouse/rack'));
const RackForm = lazy(() => import('./pages/warehouse/rack/form'));
const BinList = lazy(() => import('./pages/warehouse/bin'));
const BinCreate = lazy(() => import('./pages/warehouse/bin/create'));
const BinEdit = lazy(() => import('./pages/warehouse/bin/edit'));

const InboundList = lazy(() => import('./pages/inbound'));
const ReceivingTask = lazy(() => import('./pages/inbound/receiving-task'));
const PalletReceiving = lazy(() => import('./pages/inbound/pallet-receiving'));

const OutboundList = lazy(() => import('./pages/outbound'));
const PickingTask = lazy(() => import('./pages/outbound/picking-task'));
const PalletPicking = lazy(() => import('./pages/outbound/pallet-picking'));

const InventoryList = lazy(() => import('./pages/inventory'));

const SkuList = lazy(() => import('./pages/product/sku'));
const SkuForm = lazy(() => import('./pages/product/sku/form'));
const CategoryList = lazy(() => import('./pages/product/category'));
const CategoryForm = lazy(() => import('./pages/product/category/form'));
const TagList = lazy(() => import('./pages/product/tag'));
const TagForm = lazy(() => import('./pages/product/tag/form'));
const UnitList = lazy(() => import('./pages/product/unit'));
const UnitForm = lazy(() => import('./pages/product/unit/form'));
const StorageTypeList = lazy(() => import('./pages/product/storage-type'));
const StorageTypeForm = lazy(() => import('./pages/product/storage-type/form'));

const CustomerList = lazy(() => import('./pages/customer'));

const UserList = lazy(() => import('./pages/system/user'));
const RoleList = lazy(() => import('./pages/system/role'));

const routes = [
  {
    path: '/login',
    element: <Login />,
  },
  {
    path: '/',
    element: (
      <AuthGuard>
        <MainLayout />
      </AuthGuard>
    ),
    children: [
      { index: true, element: <Dashboard /> },
      { path: 'dashboard', element: <Dashboard /> },
      { path: 'profile', element: <Profile /> },
      // 仓库管理
      { path: 'warehouse', element: <WarehouseList /> },
      { path: 'warehouse/create', element: <WarehouseForm /> },
      { path: 'warehouse/edit/:id', element: <WarehouseForm /> },
      { path: 'warehouse/zone', element: <ZoneList /> },
      { path: 'warehouse/zone/create', element: <ZoneForm /> },
      { path: 'warehouse/zone/edit/:id', element: <ZoneForm /> },
      { path: 'warehouse/rack', element: <RackList /> },
      { path: 'warehouse/rack/create', element: <RackForm /> },
      { path: 'warehouse/rack/edit/:id', element: <RackForm /> },
      { path: 'warehouse/bin', element: <BinList /> },
      { path: 'warehouse/bin/create', element: <BinCreate /> },
      { path: 'warehouse/bin/edit/:id', element: <BinEdit /> },
      // 入库管理
      { path: 'inbound', element: <InboundList /> },
      { path: 'inbound/receiving-task', element: <ReceivingTask /> },
      { path: 'inbound/pallet-receiving', element: <PalletReceiving /> },
      // 出库管理
      { path: 'outbound', element: <OutboundList /> },
      { path: 'outbound/picking-task', element: <PickingTask /> },
      { path: 'outbound/pallet-picking', element: <PalletPicking /> },
      // 库存管理
      { path: 'inventory', element: <InventoryList /> },
      // 产品管理
      { path: 'product/sku', element: <SkuList /> },
      { path: 'product/sku/create', element: <SkuForm /> },
      { path: 'product/sku/edit/:id', element: <SkuForm /> },
      { path: 'product/category', element: <CategoryList /> },
      { path: 'product/category/create', element: <CategoryForm /> },
      { path: 'product/category/edit/:id', element: <CategoryForm /> },
      { path: 'product/tag', element: <TagList /> },
      { path: 'product/tag/create', element: <TagForm /> },
      { path: 'product/tag/edit/:id', element: <TagForm /> },
      { path: 'product/unit', element: <UnitList /> },
      { path: 'product/unit/create', element: <UnitForm /> },
      { path: 'product/unit/edit/:id', element: <UnitForm /> },
      { path: 'product/storage-type', element: <StorageTypeList /> },
      { path: 'product/storage-type/create', element: <StorageTypeForm /> },
      { path: 'product/storage-type/edit/:id', element: <StorageTypeForm /> },
      // 客户
      { path: 'customer', element: <CustomerList /> },
      // 系统
      { path: 'system/user', element: <UserList /> },
      { path: 'system/role', element: <RoleList /> },
    ],
  },
];

export default routes;
```

- [ ] **Step 2: 创建 AuthGuard 组件**

```jsx
// wms-ui/src/components/AuthGuard.jsx
import { useEffect, useState } from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { Spin } from 'antd';
import useAuth from '../stores/useAuth';

const AuthGuard = ({ children }) => {
  const location = useLocation();
  const { initialized, initAuth, user } = useAuth();
  const [loading, setLoading] = useState(!initialized);

  useEffect(() => {
    if (!initialized) {
      initAuth().finally(() => setLoading(false));
    }
  }, []);

  if (loading) {
    return (
      <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
        <Spin size="large" tip="加载中..." />
      </div>
    );
  }

  if (!user) {
    const redirect = encodeURIComponent(location.pathname + location.search);
    return <Navigate to={`/login?redirect=${redirect}`} replace />;
  }

  return children;
};

export default AuthGuard;
```

- [ ] **Step 3: 创建 App.jsx**

```jsx
import { Suspense } from 'react';
import { BrowserRouter, useRoutes } from 'react-router-dom';
import { ConfigProvider, App as AntApp, Spin } from 'antd';
import zhCN from 'antd/locale/zh_CN';
import routes from './routes';

const RouteFallback = () => (
  <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
    <Spin size="large" />
  </div>
);

const AppRoutes = () => useRoutes(routes);

const App = () => {
  return (
    <ConfigProvider locale={zhCN}>
      <AntApp>
        <BrowserRouter>
          <Suspense fallback={<RouteFallback />}>
            <AppRoutes />
          </Suspense>
        </BrowserRouter>
      </AntApp>
    </ConfigProvider>
  );
};

export default App;
```

- [ ] **Step 4: 创建 main.jsx**

```jsx
import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
```

- [ ] **Step 5: 验证基础设施能启动**

```bash
cd wms-ui && npm run dev
```

应看到空白页面，访问 `http://localhost:5173/login` 无报错即可。

---

## Phase 2: 核心页面

### Task 7: 登录页

**Files:**
- Create: `wms-ui/src/pages/login/index.jsx`

- [ ] **Step 1: 创建登录页**

```jsx
import { useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { Form, Input, Button, Card, message } from 'antd';
import { UserOutlined, LockOutlined } from '@ant-design/icons';
import { login as loginApi } from '../../api/auth';
import useAuth from '../../stores/useAuth';

const LoginPage = () => {
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const { setUser, setPermissions } = useAuth();

  const onFinish = async (values) => {
    setLoading(true);
    try {
      // 登录
      const loginRes = await loginApi(values);
      // 获取用户信息
      const { getCurrentUser } = await import('../../api/auth');
      const userData = await getCurrentUser();

      if (userData && userData.user) {
        setUser(userData.user);
        setPermissions(userData.perms || []);
        message.success('登录成功');

        const redirect = searchParams.get('redirect') || '/dashboard';
        navigate(decodeURIComponent(redirect), { replace: true });
      } else {
        message.error('登录失败，请检查账号密码');
      }
    } catch (err) {
      message.error(err?.message || '登录失败');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div
      style={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        minHeight: '100vh',
        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
      }}
    >
      <Card
        title="WMS 仓库管理系统"
        style={{ width: 400, boxShadow: '0 8px 24px rgba(0,0,0,0.15)' }}
        headStyle={{ textAlign: 'center', fontSize: 20 }}
      >
        <Form name="login" onFinish={onFinish} size="large">
          <Form.Item name="username" rules={[{ required: true, message: '请输入账号' }]}>
            <Input prefix={<UserOutlined />} placeholder="账号" />
          </Form.Item>
          <Form.Item name="password" rules={[{ required: true, message: '请输入密码' }]}>
            <Input.Password prefix={<LockOutlined />} placeholder="密码" />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" loading={loading} block>
              登录
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </div>
  );
};

export default LoginPage;
```

---

### Task 8: 仪表盘

**Files:**
- Create: `wms-ui/src/pages/dashboard/index.jsx`

- [ ] **Step 1: 创建仪表盘**

```jsx
import { useState, useEffect } from 'react';
import { Row, Col, Card, Statistic, Spin } from 'antd';
import {
  ShopOutlined,
  InboxOutlined,
  ExportOutlined,
  DatabaseOutlined,
  AppstoreOutlined,
  TeamOutlined,
} from '@ant-design/icons';
import { getWarehouseList } from '../../api/warehouse';
import { getInboundList } from '../../api/inbound';
import { getOutboundList } from '../../api/outbound';
import { getCustomerList } from '../../api/customer';
import { getSkuList } from '../../api/product';
import { getInventoryList } from '../../api/inventory';

const DashboardPage = () => {
  const [stats, setStats] = useState({});
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadStats();
  }, []);

  const loadStats = async () => {
    setLoading(true);
    try {
      const [warehouses, inbounds, outbounds, customers, products, inventory] =
        await Promise.allSettled([
          getWarehouseList({ pageNum: 1, pageSize: 1 }),
          getInboundList({ pageNum: 1, pageSize: 1 }),
          getOutboundList({ pageNum: 1, pageSize: 1 }),
          getCustomerList({ pageNum: 1, pageSize: 1 }),
          getSkuList({ pageNum: 1, pageSize: 1 }),
          getInventoryList({ pageNum: 1, pageSize: 1 }),
        ]);

      setStats({
        warehouseCount: warehouses.value?.total || 0,
        inboundCount: inbounds.value?.total || 0,
        outboundCount: outbounds.value?.total || 0,
        customerCount: customers.value?.total || 0,
        productCount: products.value?.total || 0,
        inventoryCount: inventory.value?.total || 0,
      });
    } catch {
      // ignore
    } finally {
      setLoading(false);
    }
  };

  const cards = [
    { title: '仓库总数', value: stats.warehouseCount, icon: <ShopOutlined />, color: '#1677ff' },
    { title: '入库单', value: stats.inboundCount, icon: <InboxOutlined />, color: '#52c41a' },
    { title: '出库单', value: stats.outboundCount, icon: <ExportOutlined />, color: '#faad14' },
    { title: '库存记录', value: stats.inventoryCount, icon: <DatabaseOutlined />, color: '#722ed1' },
    { title: '产品数量', value: stats.productCount, icon: <AppstoreOutlined />, color: '#eb2f96' },
    { title: '客户数量', value: stats.customerCount, icon: <TeamOutlined />, color: '#13c2c2' },
  ];

  return (
    <Spin spinning={loading}>
      <h2 style={{ marginBottom: 24 }}>仪表盘</h2>
      <Row gutter={[16, 16]}>
        {cards.map((card, index) => (
          <Col xs={24} sm={12} lg={8} key={index}>
            <Card hoverable>
              <Statistic
                title={card.title}
                value={card.value}
                prefix={
                  <span style={{ color: card.color, fontSize: 24, marginRight: 8 }}>
                    {card.icon}
                  </span>
                }
              />
            </Card>
          </Col>
        ))}
      </Row>
    </Spin>
  );
};

export default DashboardPage;
```

---

### Task 9: 通用表格 Hook

**Files:**
- Create: `wms-ui/src/hooks/usePageTable.js`

- [ ] **Step 1: 创建 usePageTable hook**

```js
import { useState, useCallback, useRef } from 'react';

const usePageTable = (fetchFn, options = {}) => {
  const { defaultPageSize = 10, defaultParams = {} } = options;

  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [pagination, setPagination] = useState({
    current: 1,
    pageSize: defaultPageSize,
    total: 0,
  });
  const [searchParams, setSearchParams] = useState({});
  const fetchLock = useRef(false);

  const fetchData = useCallback(
    async (params = {}, page = 1, size = pagination.pageSize) => {
      if (fetchLock.current) return;
      fetchLock.current = true;
      setLoading(true);
      try {
        const mergedParams = { ...defaultParams, ...searchParams, ...params, pageNum: page, pageSize: size };
        const result = await fetchFn(mergedParams);
        const list = result?.records || result?.list || [];
        const total = result?.total || 0;
        setData(list);
        setPagination({ current: page, pageSize: size, total });
      } catch {
        setData([]);
      } finally {
        setLoading(false);
        fetchLock.current = false;
      }
    },
    [fetchFn, searchParams, pagination.pageSize]
  );

  const handleSearch = useCallback(
    (values) => {
      const newParams = { ...values };
      Object.keys(newParams).forEach((key) => {
        if (newParams[key] === undefined || newParams[key] === null || newParams[key] === '') {
          delete newParams[key];
        }
      });
      setSearchParams(newParams);
      // 需要用更新的参数重新获取
      setTimeout(() => {
        fetchData(newParams, 1);
      }, 0);
    },
    [fetchData]
  );

  const handlePageChange = useCallback(
    (page, size) => {
      fetchData(searchParams, page, size);
    },
    [fetchData, searchParams]
  );

  const refresh = useCallback(() => {
    fetchData(searchParams, pagination.current, pagination.pageSize);
  }, [fetchData, searchParams, pagination]);

  // 初始加载
  const initLoad = useCallback(() => {
    fetchData(searchParams, 1);
  }, [fetchData, searchParams]);

  return {
    data,
    loading,
    pagination,
    setData,
    searchParams,
    fetchData,
    handleSearch,
    handlePageChange,
    refresh,
    initLoad,
  };
};

export default usePageTable;
```

---

### Task 10: 仓库管理 - 仓库列表

**Files:**
- Create: `wms-ui/src/pages/warehouse/index.jsx`
- Create: `wms-ui/src/pages/warehouse/form.jsx`

- [ ] **Step 1: 创建仓库列表页**

```jsx
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Table, Button, Space, Input, Form, Popconfirm, message, Card, Row, Col } from 'antd';
import { PlusOutlined, SearchOutlined, ReloadOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { getWarehouseList, deleteWarehouse } from '../../api/warehouse';
import usePageTable from '../../hooks/usePageTable';

const WarehouseListPage = () => {
  const navigate = useNavigate();
  const [searchForm] = Form.useForm();
  const { data, loading, pagination, handleSearch, handlePageChange, refresh, initLoad } =
    usePageTable(getWarehouseList);

  useEffect(() => {
    initLoad();
  }, []);

  const columns = [
    { title: '仓库编码', dataIndex: 'warehouseCode', key: 'warehouseCode' },
    { title: '仓库名称', dataIndex: 'warehouseName', key: 'warehouseName' },
    { title: '地址', dataIndex: 'address', key: 'address' },
    { title: '城市', dataIndex: 'city', key: 'city' },
    { title: '是否启用', dataIndex: 'isActive', key: 'isActive', render: (val) => (val === '1' ? '是' : '否') },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Space>
          <Button
            type="link"
            icon={<EditOutlined />}
            onClick={() => navigate(`/warehouse/edit/${record.id}`)}
          >
            编辑
          </Button>
          <Popconfirm
            title="确定删除该仓库？"
            onConfirm={async () => {
              await deleteWarehouse(record.id);
              message.success('删除成功');
              refresh();
            }}
          >
            <Button type="link" danger icon={<DeleteOutlined />}>
              删除
            </Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <div>
      <Card style={{ marginBottom: 16 }}>
        <Form form={searchForm} layout="inline" onFinish={handleSearch}>
          <Form.Item name="warehouseName">
            <Input placeholder="仓库名称" />
          </Form.Item>
          <Form.Item name="warehouseCode">
            <Input placeholder="仓库编码" />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" icon={<SearchOutlined />}>
              搜索
            </Button>
          </Form.Item>
          <Form.Item>
            <Button
              icon={<ReloadOutlined />}
              onClick={() => {
                searchForm.resetFields();
                handleSearch({});
              }}
            >
              重置
            </Button>
          </Form.Item>
        </Form>
      </Card>

      <Card
        title="仓库列表"
        extra={
          <Button type="primary" icon={<PlusOutlined />} onClick={() => navigate('/warehouse/create')}>
            新增仓库
          </Button>
        }
      >
        <Table
          rowKey="id"
          columns={columns}
          dataSource={data}
          loading={loading}
          pagination={{
            ...pagination,
            showSizeChanger: true,
            showTotal: (total) => `共 ${total} 条`,
            onChange: handlePageChange,
          }}
        />
      </Card>
    </div>
  );
};

export default WarehouseListPage;
```

- [ ] **Task 10 complete.**

### Task 11: 仓库管理 - 仓库表单页

**Files:** Create `wms-ui/src/pages/warehouse/form.jsx`

```jsx
import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Form, Input, Button, Card, Select, InputNumber, Switch, message, Spin } from 'antd';
import { getWarehouseById, createWarehouse, updateWarehouse } from '../../api/warehouse';

const WarehouseFormPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const isEdit = !!id;

  useEffect(() => {
    if (isEdit) {
      loadDetail();
    }
  }, [id]);

  const loadDetail = async () => {
    setLoading(true);
    try {
      const data = await getWarehouseById(id);
      form.setFieldsValue(data);
    } catch {
      message.error('加载数据失败');
    } finally {
      setLoading(false);
    }
  };

  const onFinish = async (values) => {
    setSubmitting(true);
    try {
      if (isEdit) {
        await updateWarehouse({ ...values, id });
      } else {
        await createWarehouse(values);
      }
      message.success(isEdit ? '更新成功' : '创建成功');
      navigate('/warehouse');
    } catch {
      // 错误已在拦截器中处理
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <Spin spinning={loading}>
      <Card title={isEdit ? '编辑仓库' : '新增仓库'}>
        <Form form={form} layout="vertical" onFinish={onFinish} style={{ maxWidth: 600 }}>
          <Form.Item name="warehouseCode" label="仓库编码" rules={[{ required: true, message: '请输入仓库编码' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="warehouseName" label="仓库名称" rules={[{ required: true, message: '请输入仓库名称' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="address" label="地址">
            <Input.TextArea rows={2} />
          </Form.Item>
          <Form.Item name="city" label="城市">
            <Input />
          </Form.Item>
          <Form.Item name="state" label="省/州">
            <Input />
          </Form.Item>
          <Form.Item name="postalCode" label="邮编">
            <Input />
          </Form.Item>
          <Form.Item name="length" label="长度">
            <InputNumber min={0} style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="width" label="宽度">
            <InputNumber min={0} style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="isActive" label="是否启用" valuePropName="checked">
            <Switch checkedChildren="启用" unCheckedChildren="停用" />
          </Form.Item>
          <Form.Item name="remark" label="备注">
            <Input.TextArea rows={3} />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" loading={submitting} style={{ marginRight: 12 }}>
              {isEdit ? '保存' : '创建'}
            </Button>
            <Button onClick={() => navigate('/warehouse')}>取消</Button>
          </Form.Item>
        </Form>
      </Card>
    </Spin>
  );
};

export default WarehouseFormPage;
```

---

### Task 12: 库区管理（列表 + 表单）

**Files:**
- Create: `wms-ui/src/pages/warehouse/zone/index.jsx`
- Create: `wms-ui/src/pages/warehouse/zone/form.jsx`

- [ ] **Step 1: 创建库区列表页**

```jsx
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Table, Button, Space, Input, Form, Popconfirm, message, Card } from 'antd';
import { PlusOutlined, SearchOutlined, ReloadOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { getZoneList, deleteZone } from '../../../api/warehouse-zone';
import usePageTable from '../../../hooks/usePageTable';

const ZoneListPage = () => {
  const navigate = useNavigate();
  const [searchForm] = Form.useForm();
  const { data, loading, pagination, handleSearch, handlePageChange, refresh, initLoad } =
    usePageTable(getZoneList);

  useEffect(() => { initLoad(); }, []);

  const columns = [
    { title: '库区名称', dataIndex: 'zoneName', key: 'zoneName' },
    { title: '所属仓库', dataIndex: 'warehouseId', key: 'warehouseId' },
    { title: '层数', dataIndex: 'layers', key: 'layers' },
    { title: '长度', dataIndex: 'length', key: 'length' },
    { title: '宽度', dataIndex: 'width', key: 'width' },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Space>
          <Button type="link" icon={<EditOutlined />} onClick={() => navigate(`/warehouse/zone/edit/${record.id}`)}>编辑</Button>
          <Popconfirm title="确定删除？" onConfirm={async () => { await deleteZone(record.id); message.success('删除成功'); refresh(); }}>
            <Button type="link" danger icon={<DeleteOutlined />}>删除</Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <div>
      <Card style={{ marginBottom: 16 }}>
        <Form form={searchForm} layout="inline" onFinish={handleSearch}>
          <Form.Item name="zoneName"><Input placeholder="库区名称" /></Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" icon={<SearchOutlined />}>搜索</Button>
          </Form.Item>
          <Form.Item>
            <Button icon={<ReloadOutlined />} onClick={() => { searchForm.resetFields(); handleSearch({}); }}>重置</Button>
          </Form.Item>
        </Form>
      </Card>
      <Card
        title="库区列表"
        extra={<Button type="primary" icon={<PlusOutlined />} onClick={() => navigate('/warehouse/zone/create')}>新增库区</Button>}
      >
        <Table rowKey="id" columns={columns} dataSource={data} loading={loading}
          pagination={{ ...pagination, showSizeChanger: true, showTotal: (t) => `共 ${t} 条`, onChange: handlePageChange }}
        />
      </Card>
    </div>
  );
};

export default ZoneListPage;
```

- [ ] **Step 2: 创建库区表单页**

```jsx
import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Form, Input, Button, Card, InputNumber, message, Spin } from 'antd';
import { getZoneById, createZone, updateZone } from '../../../api/warehouse-zone';

const ZoneFormPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const isEdit = !!id;

  useEffect(() => {
    if (isEdit) {
      (async () => {
        setLoading(true);
        try { const data = await getZoneById(id); form.setFieldsValue(data); } catch { message.error('加载失败'); }
        setLoading(false);
      })();
    }
  }, [id]);

  const onFinish = async (values) => {
    setSubmitting(true);
    try {
      if (isEdit) { await updateZone({ ...values, id }); } else { await createZone(values); }
      message.success(isEdit ? '更新成功' : '创建成功');
      navigate('/warehouse/zone');
    } catch {} finally { setSubmitting(false); }
  };

  return (
    <Spin spinning={loading}>
      <Card title={isEdit ? '编辑库区' : '新增库区'}>
        <Form form={form} layout="vertical" onFinish={onFinish} style={{ maxWidth: 600 }}>
          <Form.Item name="zoneName" label="库区名称" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="warehouseId" label="仓库ID" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="layers" label="层数"><InputNumber min={1} style={{ width: '100%' }} /></Form.Item>
          <Form.Item name="length" label="长度"><InputNumber min={0} style={{ width: '100%' }} /></Form.Item>
          <Form.Item name="width" label="宽度"><InputNumber min={0} style={{ width: '100%' }} /></Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" loading={submitting} style={{ marginRight: 12 }}>
              {isEdit ? '保存' : '创建'}
            </Button>
            <Button onClick={() => navigate('/warehouse/zone')}>取消</Button>
          </Form.Item>
        </Form>
      </Card>
    </Spin>
  );
};

export default ZoneFormPage;
```

---

### Task 13: 货架管理、库位管理

遵循与仓库/库区相同的模式创建：

**Files:**
- Create: `wms-ui/src/pages/warehouse/rack/index.jsx`
- Create: `wms-ui/src/pages/warehouse/rack/form.jsx`
- Create: `wms-ui/src/pages/warehouse/bin/index.jsx`
- Create: `wms-ui/src/pages/warehouse/bin/create.jsx`
- Create: `wms-ui/src/pages/warehouse/bin/edit.jsx`

> 货架和库位的列表页、表单页与仓库/库区模式完全一致，只需替换 API 导入路径和字段名。此处省略重复代码，实施时直接套用仓库模板。

---

## Phase 3: 业务模块

### Task 14: 入库管理

**Files:**
- Create: `wms-ui/src/pages/inbound/index.jsx`
- Create: `wms-ui/src/pages/inbound/receiving-task.jsx`
- Create: `wms-ui/src/pages/inbound/pallet-receiving.jsx`

- [ ] **Step 1: 创建入库单列表页**

```jsx
import { useEffect, useState } from 'react';
import { Table, Button, Space, Input, Form, Popconfirm, message, Card, Modal, Select, Tag } from 'antd';
import { PlusOutlined, SearchOutlined, ReloadOutlined, EyeOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { getInboundList, createInbound, updateInbound, deleteInbound, confirmInbound } from '../../api/inbound';
import usePageTable from '../../hooks/usePageTable';

const InboundListPage = () => {
  const [searchForm] = Form.useForm();
  const [modalOpen, setModalOpen] = useState(false);
  const [editingRecord, setEditingRecord] = useState(null);
  const [detailOpen, setDetailOpen] = useState(false);
  const [detailRecord, setDetailRecord] = useState(null);
  const [form] = Form.useForm();
  const [submitting, setSubmitting] = useState(false);

  const { data, loading, pagination, handleSearch, handlePageChange, refresh, initLoad } =
    usePageTable(getInboundList);

  useEffect(() => { initLoad(); }, []);

  const handleCreate = () => {
    setEditingRecord(null);
    form.resetFields();
    setModalOpen(true);
  };

  const handleEdit = (record) => {
    setEditingRecord(record);
    form.setFieldsValue(record);
    setModalOpen(true);
  };

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      setSubmitting(true);
      if (editingRecord) {
        await updateInbound({ ...values, id: editingRecord.id });
      } else {
        await createInbound(values);
      }
      message.success(editingRecord ? '更新成功' : '创建成功');
      setModalOpen(false);
      refresh();
    } catch {} finally { setSubmitting(false); }
  };

  const columns = [
    { title: '入库单号', dataIndex: 'inboundCode', key: 'inboundCode' },
    { title: '产品SKU', dataIndex: 'skuId', key: 'skuId' },
    { title: '数量', dataIndex: 'quantity', key: 'quantity' },
    { title: '来源', dataIndex: 'source', key: 'source' },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (val) => {
        const map = { PENDING: '待入库', RECEIVING: '收货中', COMPLETED: '已完成' };
        return <Tag>{map[val] || val}</Tag>;
      },
    },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Space>
          <Button type="link" icon={<EyeOutlined />} onClick={() => { setDetailRecord(record); setDetailOpen(true); }}>详情</Button>
          <Button type="link" icon={<EditOutlined />} onClick={() => handleEdit(record)}>编辑</Button>
          {record.status === 'PENDING' && (
            <Popconfirm title="确认入库？" onConfirm={async () => { await confirmInbound(record.id); message.success('确认入库成功'); refresh(); }}>
              <Button type="link">确认入库</Button>
            </Popconfirm>
          )}
          <Popconfirm title="确定删除？" onConfirm={async () => { await deleteInbound(record.id); message.success('删除成功'); refresh(); }}>
            <Button type="link" danger icon={<DeleteOutlined />}>删除</Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <div>
      <Card style={{ marginBottom: 16 }}>
        <Form form={searchForm} layout="inline" onFinish={handleSearch}>
          <Form.Item name="inboundCode"><Input placeholder="入库单号" /></Form.Item>
          <Form.Item name="status">
            <Select placeholder="状态" allowClear style={{ width: 120 }}
              options={[
                { label: '待入库', value: 'PENDING' },
                { label: '收货中', value: 'RECEIVING' },
                { label: '已完成', value: 'COMPLETED' },
              ]}
            />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" icon={<SearchOutlined />}>搜索</Button>
          </Form.Item>
          <Form.Item>
            <Button icon={<ReloadOutlined />} onClick={() => { searchForm.resetFields(); handleSearch({}); }}>重置</Button>
          </Form.Item>
        </Form>
      </Card>

      <Card title="入库单列表" extra={<Button type="primary" icon={<PlusOutlined />} onClick={handleCreate}>新增入库单</Button>}>
        <Table rowKey="id" columns={columns} dataSource={data} loading={loading}
          pagination={{ ...pagination, showSizeChanger: true, showTotal: (t) => `共 ${t} 条`, onChange: handlePageChange }}
        />
      </Card>

      <Modal
        title={editingRecord ? '编辑入库单' : '新增入库单'}
        open={modalOpen}
        onOk={handleSubmit}
        onCancel={() => setModalOpen(false)}
        confirmLoading={submitting}
        destroyOnClose
      >
        <Form form={form} layout="vertical">
          <Form.Item name="inboundCode" label="入库单号" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="skuId" label="产品SKU" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="quantity" label="数量" rules={[{ required: true }]}><Input type="number" /></Form.Item>
          <Form.Item name="source" label="来源"><Input /></Form.Item>
          <Form.Item name="remark" label="备注"><Input.TextArea rows={2} /></Form.Item>
        </Form>
      </Modal>

      <Modal title="入库单详情" open={detailOpen} onCancel={() => setDetailOpen(false)} footer={null}>
        {detailRecord && (
          <div>
            <p><strong>入库单号：</strong>{detailRecord.inboundCode}</p>
            <p><strong>产品SKU：</strong>{detailRecord.skuId}</p>
            <p><strong>数量：</strong>{detailRecord.quantity}</p>
            <p><strong>来源：</strong>{detailRecord.source}</p>
            <p><strong>状态：</strong>{detailRecord.status}</p>
            <p><strong>备注：</strong>{detailRecord.remark}</p>
          </div>
        )}
      </Modal>
    </div>
  );
};

export default InboundListPage;
```

- [ ] **Step 2: 创建收货任务页**

```jsx
import { useEffect } from 'react';
import { Table, Button, Card, message, Tag } from 'antd';
import { getReceivingTaskList, completeReceiving } from '../../api/inbound';
import usePageTable from '../../hooks/usePageTable';

const ReceivingTaskPage = () => {
  const { data, loading, pagination, handlePageChange, refresh, initLoad } = usePageTable(getReceivingTaskList);

  useEffect(() => { initLoad(); }, []);

  const columns = [
    { title: '入库单号', dataIndex: 'inboundCode', key: 'inboundCode' },
    { title: '托盘号', dataIndex: 'palletCode', key: 'palletCode' },
    { title: '目标库位', dataIndex: 'targetBinId', key: 'targetBinId' },
    { title: '数量', dataIndex: 'quantity', key: 'quantity' },
    {
      title: '状态', dataIndex: 'status', key: 'status',
      render: (val) => <Tag>{val}</Tag>,
    },
    {
      title: '操作', key: 'action',
      render: (_, record) => (
        record.status !== 'COMPLETED' && (
          <Button type="link" onClick={async () => {
            await completeReceiving(record.id, {});
            message.success('收货完成');
            refresh();
          }}>完成收货</Button>
        )
      ),
    },
  ];

  return (
    <Card title="收货任务">
      <Table rowKey="id" columns={columns} dataSource={data} loading={loading}
        pagination={{ ...pagination, showSizeChanger: true, showTotal: (t) => `共 ${t} 条`, onChange: handlePageChange }}
      />
    </Card>
  );
};

export default ReceivingTaskPage;
```

- [ ] **Step 3: 创建托盘收货页**（结构与收货任务类似，使用 pallet API）

```jsx
import { useEffect } from 'react';
import { Table, Button, Card, message, Tag } from 'antd';
import { getPalletReceivingList, closePallet } from '../../api/inbound';
import usePageTable from '../../hooks/usePageTable';

const PalletReceivingPage = () => {
  const { data, loading, pagination, handlePageChange, refresh, initLoad } =
    usePageTable(getPalletReceivingList);

  useEffect(() => { initLoad(); }, []);

  const columns = [
    { title: '托盘号', dataIndex: 'palletCode', key: 'palletCode' },
    { title: '入库单号', dataIndex: 'inboundCode', key: 'inboundCode' },
    { title: '数量', dataIndex: 'quantity', key: 'quantity' },
    { title: '状态', dataIndex: 'status', key: 'status', render: (val) => <Tag>{val}</Tag> },
    {
      title: '操作', key: 'action',
      render: (_, record) => (
        record.status !== 'CLOSED' && (
          <Button type="link" onClick={async () => {
            await closePallet(record.id);
            message.success('托盘已关闭');
            refresh();
          }}>关闭托盘</Button>
        )
      ),
    },
  ];

  return (
    <Card title="托盘收货">
      <Table rowKey="id" columns={columns} dataSource={data} loading={loading}
        pagination={{ ...pagination, showSizeChanger: true, showTotal: (t) => `共 ${t} 条`, onChange: handlePageChange }}
      />
    </Card>
  );
};

export default PalletReceivingPage;
```

---

### Task 15: 出库管理

**Files:**
- Create: `wms-ui/src/pages/outbound/index.jsx`
- Create: `wms-ui/src/pages/outbound/picking-task.jsx`
- Create: `wms-ui/src/pages/outbound/pallet-picking.jsx`

出库管理与入库管理完全对称，API 从 `../../api/outbound` 导入，将"入库"替换为"出库"，"收货"替换为"拣货"即可。此处省略重复代码。

### Task 16: 库存管理

**Files:**
- Create: `wms-ui/src/pages/inventory/index.jsx`

```jsx
import { useEffect, useState } from 'react';
import { Table, Card, Form, Input, Button, Select, Tag, Modal } from 'antd';
import { SearchOutlined, ReloadOutlined } from '@ant-design/icons';
import { getInventoryList, getInventoryBatchList, getInventoryByBin } from '../../api/inventory';
import usePageTable from '../../hooks/usePageTable';

const InventoryPage = () => {
  const [searchForm] = Form.useForm();
  const [batchModalOpen, setBatchModalOpen] = useState(false);
  const [batchData, setBatchData] = useState([]);
  const [currentRecord, setCurrentRecord] = useState(null);

  const { data, loading, pagination, handleSearch, handlePageChange, refresh, initLoad } =
    usePageTable(getInventoryList);

  useEffect(() => { initLoad(); }, []);

  const showBatchDetail = async (record) => {
    setCurrentRecord(record);
    try {
      const result = await getInventoryBatchList({ skuId: record.skuId });
      setBatchData(result?.records || result?.list || []);
    } catch { setBatchData([]); }
    setBatchModalOpen(true);
  };

  const columns = [
    { title: '产品SKU', dataIndex: 'skuId', key: 'skuId' },
    { title: '产品名称', dataIndex: 'skuName', key: 'skuName' },
    { title: '总库存数量', dataIndex: 'totalQuantity', key: 'totalQuantity' },
    { title: '可用数量', dataIndex: 'availableQuantity', key: 'availableQuantity' },
    { title: '锁定数量', dataIndex: 'lockedQuantity', key: 'lockedQuantity' },
    { title: '库位数量', dataIndex: 'binCount', key: 'binCount' },
    {
      title: '操作', key: 'action',
      render: (_, record) => (
        <Button type="link" onClick={() => showBatchDetail(record)}>批次详情</Button>
      ),
    },
  ];

  const batchColumns = [
    { title: '批次号', dataIndex: 'batchCode', key: 'batchCode' },
    { title: '库位', dataIndex: 'binName', key: 'binName' },
    { title: '数量', dataIndex: 'quantity', key: 'quantity' },
    { title: '生产日期', dataIndex: 'productionDate', key: 'productionDate' },
    { title: '过期日期', dataIndex: 'expiryDate', key: 'expiryDate' },
    { title: '状态', dataIndex: 'status', key: 'status', render: (v) => <Tag>{v}</Tag> },
  ];

  return (
    <div>
      <Card style={{ marginBottom: 16 }}>
        <Form form={searchForm} layout="inline" onFinish={handleSearch}>
          <Form.Item name="skuId"><Input placeholder="产品SKU" /></Form.Item>
          <Form.Item name="skuName"><Input placeholder="产品名称" /></Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" icon={<SearchOutlined />}>搜索</Button>
          </Form.Item>
          <Form.Item>
            <Button icon={<ReloadOutlined />} onClick={() => { searchForm.resetFields(); handleSearch({}); }}>重置</Button>
          </Form.Item>
        </Form>
      </Card>

      <Card title="库存汇总">
        <Table rowKey="skuId" columns={columns} dataSource={data} loading={loading}
          pagination={{ ...pagination, showSizeChanger: true, showTotal: (t) => `共 ${t} 条`, onChange: handlePageChange }}
        />
      </Card>

      <Modal title={`批次详情 - ${currentRecord?.skuId || ''}`} open={batchModalOpen} onCancel={() => setBatchModalOpen(false)} footer={null} width={800}>
        <Table rowKey="batchCode" columns={batchColumns} dataSource={batchData} pagination={false} size="small" />
      </Modal>
    </div>
  );
};

export default InventoryPage;
```

---

## Phase 4: 辅助模块

### Task 17: 产品管理（SKU、分类、标签、单位、存放类型）

每个子模块的列表页 + 表单页结构与仓库管理完全一致，只需替换 API 导入路径和列/字段定义。

**Files to create:**
- `wms-ui/src/pages/product/sku/index.jsx` + `form.jsx`
- `wms-ui/src/pages/product/category/index.jsx` + `form.jsx`
- `wms-ui/src/pages/product/tag/index.jsx` + `form.jsx`
- `wms-ui/src/pages/product/unit/index.jsx` + `form.jsx`
- `wms-ui/src/pages/product/storage-type/index.jsx` + `form.jsx`

> 全部复用仓库管理的列表+表单模板，替换 API 导入即可。分类页额外使用 Ant Design `Tree` 组件展示树形结构。

### Task 18: 客户管理

**Files:** Create `wms-ui/src/pages/customer/index.jsx`

```jsx
import { useEffect, useState } from 'react';
import { Table, Button, Space, Input, Form, Popconfirm, Modal, message, Card } from 'antd';
import { PlusOutlined, SearchOutlined, ReloadOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { getCustomerList, createCustomer, updateCustomer, deleteCustomer } from '../../api/customer';
import usePageTable from '../../hooks/usePageTable';

const CustomerPage = () => {
  const [searchForm] = Form.useForm();
  const [modalOpen, setModalOpen] = useState(false);
  const [editingRecord, setEditingRecord] = useState(null);
  const [form] = Form.useForm();
  const [submitting, setSubmitting] = useState(false);

  const { data, loading, pagination, handleSearch, handlePageChange, refresh, initLoad } =
    usePageTable(getCustomerList);

  useEffect(() => { initLoad(); }, []);

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      setSubmitting(true);
      if (editingRecord) {
        await updateCustomer({ ...values, id: editingRecord.id });
      } else {
        await createCustomer(values);
      }
      message.success(editingRecord ? '更新成功' : '创建成功');
      setModalOpen(false);
      refresh();
    } catch {} finally { setSubmitting(false); }
  };

  const columns = [
    { title: '客户编码', dataIndex: 'code', key: 'code' },
    { title: '客户名称', dataIndex: 'name', key: 'name' },
    { title: '联系人', dataIndex: 'contact', key: 'contact' },
    { title: '电话', dataIndex: 'phone', key: 'phone' },
    { title: '邮箱', dataIndex: 'email', key: 'email' },
    { title: '地址', dataIndex: 'address', key: 'address' },
    {
      title: '操作', key: 'action',
      render: (_, record) => (
        <Space>
          <Button type="link" icon={<EditOutlined />} onClick={() => {
            setEditingRecord(record); form.setFieldsValue(record); setModalOpen(true);
          }}>编辑</Button>
          <Popconfirm title="确定删除？" onConfirm={async () => {
            await deleteCustomer(record.id); message.success('删除成功'); refresh();
          }}>
            <Button type="link" danger icon={<DeleteOutlined />}>删除</Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <div>
      <Card style={{ marginBottom: 16 }}>
        <Form form={searchForm} layout="inline" onFinish={handleSearch}>
          <Form.Item name="name"><Input placeholder="客户名称" /></Form.Item>
          <Form.Item name="code"><Input placeholder="客户编码" /></Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" icon={<SearchOutlined />}>搜索</Button>
          </Form.Item>
          <Form.Item>
            <Button icon={<ReloadOutlined />} onClick={() => { searchForm.resetFields(); handleSearch({}); }}>重置</Button>
          </Form.Item>
        </Form>
      </Card>

      <Card title="客户列表"
        extra={<Button type="primary" icon={<PlusOutlined />} onClick={() => {
          setEditingRecord(null); form.resetFields(); setModalOpen(true);
        }}>新增客户</Button>}
      >
        <Table rowKey="id" columns={columns} dataSource={data} loading={loading}
          pagination={{ ...pagination, showSizeChanger: true, showTotal: (t) => `共 ${t} 条`, onChange: handlePageChange }}
        />
      </Card>

      <Modal
        title={editingRecord ? '编辑客户' : '新增客户'}
        open={modalOpen}
        onOk={handleSubmit}
        onCancel={() => setModalOpen(false)}
        confirmLoading={submitting}
        destroyOnClose
      >
        <Form form={form} layout="vertical">
          <Form.Item name="code" label="客户编码" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="name" label="客户名称" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="contact" label="联系人"><Input /></Form.Item>
          <Form.Item name="phone" label="电话"><Input /></Form.Item>
          <Form.Item name="email" label="邮箱"><Input /></Form.Item>
          <Form.Item name="address" label="地址"><Input.TextArea rows={2} /></Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default CustomerPage;
```

### Task 19: 系统管理（用户管理 + 角色管理）

**Files:**
- Create: `wms-ui/src/pages/system/user/index.jsx`
- Create: `wms-ui/src/pages/system/role/index.jsx`

结构与客户管理类似，用户管理额外包含重置密码功能，角色管理额外包含权限分配（Ant Design `Tree` 组件）。

### Task 20: 个人中心

**Files:** Create `wms-ui/src/pages/profile/index.jsx`

```jsx
import { Card, Descriptions, Button, Form, Input, Modal, message } from 'antd';
import { useState } from 'react';
import useAuth from '../../stores/useAuth';
import { updateUser } from '../../api/system';

const ProfilePage = () => {
  const { user, setUser } = useAuth();
  const [modalOpen, setModalOpen] = useState(false);
  const [passwordModalOpen, setPasswordModalOpen] = useState(false);
  const [form] = Form.useForm();
  const [passwordForm] = Form.useForm();
  const [submitting, setSubmitting] = useState(false);

  const handleEdit = () => {
    form.setFieldsValue(user);
    setModalOpen(true);
  };

  const handleSave = async () => {
    try {
      const values = await form.validateFields();
      setSubmitting(true);
      await updateUser({ ...values, userId: user.userId });
      setUser({ ...user, ...values });
      message.success('保存成功');
      setModalOpen(false);
    } catch {} finally { setSubmitting(false); }
  };

  const handleChangePassword = async () => {
    try {
      const values = await passwordForm.validateFields();
      setSubmitting(true);
      const { updatePassword } = await import('../../api/auth');
      await updatePassword(values);
      message.success('密码修改成功');
      setPasswordModalOpen(false);
    } catch {} finally { setSubmitting(false); }
  };

  return (
    <Card title="个人中心">
      <Descriptions column={1} bordered>
        <Descriptions.Item label="昵称">{user?.nickName}</Descriptions.Item>
        <Descriptions.Item label="邮箱">{user?.email}</Descriptions.Item>
        <Descriptions.Item label="手机号">{user?.phonenumber}</Descriptions.Item>
        <Descriptions.Item label="性别">{user?.sex === '0' ? '男' : user?.sex === '1' ? '女' : '未知'}</Descriptions.Item>
        <Descriptions.Item label="账号">{user?.userName}</Descriptions.Item>
      </Descriptions>

      <div style={{ marginTop: 24 }}>
        <Button type="primary" onClick={handleEdit} style={{ marginRight: 12 }}>编辑资料</Button>
        <Button onClick={() => setPasswordModalOpen(true)}>修改密码</Button>
      </div>

      <Modal title="编辑资料" open={modalOpen} onOk={handleSave} onCancel={() => setModalOpen(false)} confirmLoading={submitting}>
        <Form form={form} layout="vertical">
          <Form.Item name="nickName" label="昵称" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="email" label="邮箱"><Input /></Form.Item>
          <Form.Item name="phonenumber" label="手机号"><Input /></Form.Item>
          <Form.Item name="sex" label="性别"><Input /></Form.Item>
        </Form>
      </Modal>

      <Modal title="修改密码" open={passwordModalOpen} onOk={handleChangePassword} onCancel={() => setPasswordModalOpen(false)} confirmLoading={submitting}>
        <Form form={passwordForm} layout="vertical">
          <Form.Item name="oldPassword" label="旧密码" rules={[{ required: true }]}><Input.Password /></Form.Item>
          <Form.Item name="newPassword" label="新密码" rules={[{ required: true, min: 6 }]}><Input.Password /></Form.Item>
          <Form.Item name="confirmPassword" label="确认密码"
            dependencies={['newPassword']}
            rules={[{ required: true }, ({ getFieldValue }) => ({
              validator(_, value) {
                if (!value || getFieldValue('newPassword') === value) return Promise.resolve();
                return Promise.reject(new Error('两次密码不一致'));
              },
            })]}
          >
            <Input.Password />
          </Form.Item>
        </Form>
      </Modal>
    </Card>
  );
};

export default ProfilePage;
```

---

### Task 21: Dockerfile 更新

**Files:** Modify `wms-ui/Dockerfile`

```dockerfile
# 构建阶段
FROM node:20-alpine AS build
WORKDIR /app

COPY package.json package-lock.json .npmrc ./
RUN npm ci

COPY . .
RUN npm run build

# 运行阶段 - 使用 nginx 替代 vite preview（生产环境推荐）
FROM nginx:alpine

COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
```

nginx.conf 保持不变（已有反向代理到后端）。

---

### Task 22: 最终验证

- [ ] **启动后端**：`./mvnw.cmd spring-boot:run`（H2 内存数据库）
- [ ] **启动前端**：`cd wms-ui && npm run dev`
- [ ] **验证登录流程**：访问 `http://localhost:5173/login`，用 admin/123456 登录
- [ ] **验证各页面**：逐一检查仪表盘、仓库、入库、出库、库存、产品、客户、系统、个人中心页面
- [ ] **验证 Docker**：`docker compose up -d --build` 确认三容器正常启动
- [ ] **提交代码**：`git add -A && git commit -m "feat: migrate frontend from Vue to React + Ant Design"`
