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

// 权限→菜单映射：每个菜单项需要哪些权限才能显示
const buildMenuItems = (permissions) => {
  const has = (p) => permissions.includes('*') || permissions.includes(p);
  const any = (...ps) => ps.some(p => permissions.includes('*') || permissions.includes(p));

  const items = [];

  // 仪表盘：所有人可见（登录即可）
  items.push({ key: '/dashboard', icon: <DashboardOutlined />, label: '仪表盘' });

  // 仓库管理
  if (has('warehouse:manage')) {
    items.push({
      key: '/warehouse', icon: <ShopOutlined />, label: '仓库管理',
      children: [
        { key: '/warehouse', label: '仓库列表' },
        { key: '/warehouse/zone', label: '库区管理' },
        { key: '/warehouse/rack', label: '货架管理' },
        { key: '/warehouse/bin', label: '库位管理' },
      ],
    });
  }

  // 入库管理
  if (any('inbound:manage', 'inbound:receive')) {
    const inboundChildren = [];
    if (has('inbound:manage')) inboundChildren.push({ key: '/inbound', label: '入库单' });
    if (any('inbound:manage', 'inbound:receive')) {
      inboundChildren.push({ key: '/inbound/receiving-task', label: '收货任务' });
      inboundChildren.push({ key: '/inbound/pallet-receiving', label: '托盘收货' });
    }
    if (inboundChildren.length > 0) {
      items.push({ key: '/inbound', icon: <InboxOutlined />, label: '入库管理', children: inboundChildren });
    }
  }

  // 出库管理
  if (any('outbound:manage', 'outbound:pick')) {
    const outboundChildren = [];
    if (has('outbound:manage')) outboundChildren.push({ key: '/outbound', label: '出库单' });
    if (any('outbound:manage', 'outbound:pick')) {
      outboundChildren.push({ key: '/outbound/picking-task', label: '拣货任务' });
      outboundChildren.push({ key: '/outbound/pallet-picking', label: '托盘拣货' });
    }
    if (outboundChildren.length > 0) {
      items.push({ key: '/outbound', icon: <ExportOutlined />, label: '出库管理', children: outboundChildren });
    }
  }

  // 库存管理
  if (has('inventory:view')) {
    items.push({ key: '/inventory', icon: <DatabaseOutlined />, label: '库存管理' });
  }

  // 产品管理
  if (has('prod:manage')) {
    items.push({
      key: '/product', icon: <AppstoreOutlined />, label: '产品管理',
      children: [
        { key: '/product/sku', label: '产品列表' },
        { key: '/product/category', label: '产品分类' },
        { key: '/product/tag', label: '产品标签' },
        { key: '/product/unit', label: '单位管理' },
        { key: '/product/storage-type', label: '存放类型' },
      ],
    });
  }

  // 客户管理
  if (has('customer:manage')) {
    items.push({ key: '/customer', icon: <TeamOutlined />, label: '客户管理' });
  }

  // 系统管理
  if (any('sys:user:manage', 'sys:role:manage')) {
    const sysChildren = [];
    if (has('sys:user:manage')) sysChildren.push({ key: '/system/user', label: '用户管理' });
    if (has('sys:role:manage')) sysChildren.push({ key: '/system/role', label: '角色管理' });
    if (sysChildren.length > 0) {
      items.push({ key: '/system', icon: <SettingOutlined />, label: '系统管理', children: sysChildren });
    }
  }

  return items;
};

const MainLayout = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { user, permissions, logout } = useAuth();
  const { sidebarCollapsed, toggleSidebar } = useApp();
  const { token: { colorBgContainer } } = theme.useToken();

  const menuItems = buildMenuItems(permissions);

  const getSelectedKeys = () => [location.pathname];

  const getOpenKeys = () => {
    const parts = location.pathname.split('/').filter(Boolean);
    if (parts.length > 1) {
      return ['/' + parts.slice(0, parts.length - 1).join('/')];
    }
    return [];
  };

  const handleLogout = async () => {
    await logout();
    navigate('/login');
  };

  const userMenuItems = [
    { key: 'profile', icon: <UserOutlined />, label: '个人中心', onClick: () => navigate('/profile') },
    { type: 'divider', key: 'divider' },
    { key: 'logout', icon: <LogoutOutlined />, label: '退出登录', onClick: handleLogout },
  ];

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider trigger={null} collapsible collapsed={sidebarCollapsed} theme="dark" width={220}>
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
          onClick={({ key }) => navigate(key)}
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
