import { Result, Button } from 'antd';
import { useNavigate } from 'react-router-dom';
import useAuth from '../stores/useAuth';

const PermGuard = ({ children, perm, perms }) => {
  const { permissions } = useAuth();
  const navigate = useNavigate();

  // 管理员（*权限）放行
  if (permissions.includes('*')) return children;

  // 检查权限
  const allowed = perms
    ? perms.some(p => permissions.includes(p))
    : permissions.includes(perm);

  if (!allowed) {
    return (
      <Result
        status="403"
        title="403"
        subTitle="抱歉，你没有权限访问此页面"
        extra={<Button type="primary" onClick={() => navigate('/dashboard')}>返回首页</Button>}
      />
    );
  }

  return children;
};

export default PermGuard;
