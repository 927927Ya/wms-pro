import { lazy } from 'react';
import MainLayout from './layouts/MainLayout';
import AuthGuard from './components/AuthGuard';
import PermGuard from './components/PermGuard';

const Login = lazy(() => import('./pages/login'));
const Dashboard = lazy(() => import('./pages/dashboard'));
const Profile = lazy(() => import('./pages/profile'));

// 仓库管理
const WarehouseList = lazy(() => import('./pages/warehouse'));
const WarehouseForm = lazy(() => import('./pages/warehouse/form'));
const ZoneList = lazy(() => import('./pages/warehouse/zone'));
const ZoneForm = lazy(() => import('./pages/warehouse/zone/form'));
const RackList = lazy(() => import('./pages/warehouse/rack'));
const RackForm = lazy(() => import('./pages/warehouse/rack/form'));
const BinList = lazy(() => import('./pages/warehouse/bin'));
const BinCreate = lazy(() => import('./pages/warehouse/bin/create'));
const BinEdit = lazy(() => import('./pages/warehouse/bin/edit'));

// 入库管理
const InboundList = lazy(() => import('./pages/inbound'));
const ReceivingTask = lazy(() => import('./pages/inbound/receiving-task'));
const PalletReceiving = lazy(() => import('./pages/inbound/pallet-receiving'));

// 出库管理
const OutboundList = lazy(() => import('./pages/outbound'));
const PickingTask = lazy(() => import('./pages/outbound/picking-task'));
const PalletPicking = lazy(() => import('./pages/outbound/pallet-picking'));

// 库存管理
const InventoryList = lazy(() => import('./pages/inventory'));

// 产品管理
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

// 客户管理
const CustomerList = lazy(() => import('./pages/customer'));

// 系统管理
const UserList = lazy(() => import('./pages/system/user'));
const RoleList = lazy(() => import('./pages/system/role'));

const routes = [
  { path: '/login', element: <Login /> },
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

      // 仓库管理 → warehouse:manage
      { path: 'warehouse', element: <PermGuard perm="warehouse:manage"><WarehouseList /></PermGuard> },
      { path: 'warehouse/create', element: <PermGuard perm="warehouse:manage"><WarehouseForm /></PermGuard> },
      { path: 'warehouse/edit/:id', element: <PermGuard perm="warehouse:manage"><WarehouseForm /></PermGuard> },
      { path: 'warehouse/zone', element: <PermGuard perm="warehouse:manage"><ZoneList /></PermGuard> },
      { path: 'warehouse/zone/create', element: <PermGuard perm="warehouse:manage"><ZoneForm /></PermGuard> },
      { path: 'warehouse/zone/edit/:id', element: <PermGuard perm="warehouse:manage"><ZoneForm /></PermGuard> },
      { path: 'warehouse/rack', element: <PermGuard perm="warehouse:manage"><RackList /></PermGuard> },
      { path: 'warehouse/rack/create', element: <PermGuard perm="warehouse:manage"><RackForm /></PermGuard> },
      { path: 'warehouse/rack/edit/:id', element: <PermGuard perm="warehouse:manage"><RackForm /></PermGuard> },
      { path: 'warehouse/bin', element: <PermGuard perm="warehouse:manage"><BinList /></PermGuard> },
      { path: 'warehouse/bin/create', element: <PermGuard perm="warehouse:manage"><BinCreate /></PermGuard> },
      { path: 'warehouse/bin/edit/:id', element: <PermGuard perm="warehouse:manage"><BinEdit /></PermGuard> },

      // 入库管理
      { path: 'inbound', element: <PermGuard perm="inbound:manage"><InboundList /></PermGuard> },
      { path: 'inbound/receiving-task', element: <PermGuard perms={['inbound:manage','inbound:receive']}><ReceivingTask /></PermGuard> },
      { path: 'inbound/pallet-receiving', element: <PermGuard perms={['inbound:manage','inbound:receive']}><PalletReceiving /></PermGuard> },

      // 出库管理
      { path: 'outbound', element: <PermGuard perm="outbound:manage"><OutboundList /></PermGuard> },
      { path: 'outbound/picking-task', element: <PermGuard perms={['outbound:manage','outbound:pick']}><PickingTask /></PermGuard> },
      { path: 'outbound/pallet-picking', element: <PermGuard perms={['outbound:manage','outbound:pick']}><PalletPicking /></PermGuard> },

      // 库存管理 → inventory:view
      { path: 'inventory', element: <PermGuard perm="inventory:view"><InventoryList /></PermGuard> },

      // 产品管理 → prod:manage
      { path: 'product/sku', element: <PermGuard perm="prod:manage"><SkuList /></PermGuard> },
      { path: 'product/sku/create', element: <PermGuard perm="prod:manage"><SkuForm /></PermGuard> },
      { path: 'product/sku/edit/:id', element: <PermGuard perm="prod:manage"><SkuForm /></PermGuard> },
      { path: 'product/category', element: <PermGuard perm="prod:manage"><CategoryList /></PermGuard> },
      { path: 'product/category/create', element: <PermGuard perm="prod:manage"><CategoryForm /></PermGuard> },
      { path: 'product/category/edit/:id', element: <PermGuard perm="prod:manage"><CategoryForm /></PermGuard> },
      { path: 'product/tag', element: <PermGuard perm="prod:manage"><TagList /></PermGuard> },
      { path: 'product/tag/create', element: <PermGuard perm="prod:manage"><TagForm /></PermGuard> },
      { path: 'product/tag/edit/:id', element: <PermGuard perm="prod:manage"><TagForm /></PermGuard> },
      { path: 'product/unit', element: <PermGuard perm="prod:manage"><UnitList /></PermGuard> },
      { path: 'product/unit/create', element: <PermGuard perm="prod:manage"><UnitForm /></PermGuard> },
      { path: 'product/unit/edit/:id', element: <PermGuard perm="prod:manage"><UnitForm /></PermGuard> },
      { path: 'product/storage-type', element: <PermGuard perm="prod:manage"><StorageTypeList /></PermGuard> },
      { path: 'product/storage-type/create', element: <PermGuard perm="prod:manage"><StorageTypeForm /></PermGuard> },
      { path: 'product/storage-type/edit/:id', element: <PermGuard perm="prod:manage"><StorageTypeForm /></PermGuard> },

      // 客户管理 → customer:manage
      { path: 'customer', element: <PermGuard perm="customer:manage"><CustomerList /></PermGuard> },

      // 系统管理
      { path: 'system/user', element: <PermGuard perm="sys:user:manage"><UserList /></PermGuard> },
      { path: 'system/role', element: <PermGuard perm="sys:role:manage"><RoleList /></PermGuard> },
    ],
  },
];

export default routes;
