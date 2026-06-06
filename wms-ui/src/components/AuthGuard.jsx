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
    } else {
      setLoading(false);
    }
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  if (loading) {
    return (
      <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
        <Spin size="large" />
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
