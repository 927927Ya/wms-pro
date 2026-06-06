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
      await loginApi(values);
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
