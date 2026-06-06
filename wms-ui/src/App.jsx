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

const App = () => (
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

export default App;
