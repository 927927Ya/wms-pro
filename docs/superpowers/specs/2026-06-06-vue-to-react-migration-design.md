# WMS 前端 Vue → React 迁移设计方案

**日期**: 2026-06-06 | **状态**: 已确认

## 1. 技术选型

| 类别 | 选择 | 替代 |
|------|------|------|
| 框架 | React 18 | Vue 3 |
| 语言 | JavaScript | ← |
| 构建 | Vite | ← |
| UI 库 | Ant Design 5 | Element Plus |
| 状态管理 | Zustand | Pinia |
| 路由 | React Router 6 | Vue Router |
| HTTP | Axios | ← |

## 2. 项目结构

```
wms-ui/
├── package.json
├── vite.config.js        # @vitejs/plugin-react + proxy → backend:8080
├── index.html
├── Dockerfile             # 微调 npm run build
└── src/
    ├── main.jsx           # ReactDOM.createRoot
    ├── App.jsx            # Router + AuthProvider
    ├── routes.jsx         # 懒加载路由配置
    ├── api/
    │   ├── request.js     # axios 实例 + 拦截器
    │   └── *.js           # 各模块 API
    ├── stores/
    │   ├── useAuth.js     # token/用户/权限/登录登出
    │   └── useApp.js      # 侧边栏折叠
    ├── layouts/
    │   └── MainLayout.jsx # Sider + Header + Content + Outlet
    ├── hooks/
    │   └── usePageTable.js
    └── pages/
        ├── login/
        ├── dashboard/
        ├── warehouse/
        ├── inbound/
        ├── outbound/
        ├── inventory/
        ├── product/
        ├── customer/
        └── system/
```

## 3. 路由（25+ 条，全部懒加载）

/login, /dashboard, /profile, /warehouse, /warehouse/create, /warehouse/edit/:id, /warehouse/zone, /warehouse/zone/create, /warehouse/zone/edit/:id, /warehouse/rack, /warehouse/rack/create, /warehouse/rack/edit/:id, /warehouse/bin, /warehouse/bin/create, /warehouse/bin/edit/:id, /inbound, /inbound/receiving-task, /inbound/pallet-receiving, /outbound, /outbound/picking-task, /outbound/pallet-picking, /inventory, /product/sku, /product/sku/create, /product/sku/edit/:id, /product/category, /product/category/create, /product/category/edit/:id, /product/tag, /product/tag/create, /product/tag/edit/:id, /product/unit, /product/unit/create, /product/unit/edit/:id, /product/storage-type, /product/storage-type/create, /product/storage-type/edit/:id, /customer, /system/user, /system/role

## 4. 组件映射（Element Plus → Ant Design）

| 功能 | Element Plus | Ant Design |
|------|-------------|------------|
| 表格 | el-table | Table |
| 表单 | el-form | Form |
| 对话框 | el-dialog | Modal |
| 树 | el-tree | Tree |
| 上传 | el-upload | Upload |
| 菜单 | el-menu | Menu |
| 消息提示 | ElMessage | message |
| 级联选择 | el-cascader | Cascader |

## 5. 迁移策略

1. **基础设施** — Vite + Router + Layout + API层 + Auth
2. **核心模块** — 登录 → 仪表盘 → 仓库管理
3. **业务模块** — 入库 → 出库 → 库存
4. **辅助模块** — 产品 → 客户 → 系统管理 → 个人中心
