import { useState } from 'react';
import { Card, Descriptions, Button, Form, Input, Modal, message } from 'antd';
import useAuth from '../../stores/useAuth';
import { updateUser } from '../../api/system';

const ProfilePage = () => {
  const { user, setUser } = useAuth();
  const [modalOpen, setModalOpen] = useState(false);
  const [passwordModalOpen, setPasswordModalOpen] = useState(false);
  const [form] = Form.useForm();
  const [passwordForm] = Form.useForm();
  const [submitting, setSubmitting] = useState(false);

  const handleEdit = () => { form.setFieldsValue(user); setModalOpen(true); };

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
      const { updateUserPassword } = await import('../../api/system');
      await updateUserPassword(values);
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
        </Form>
      </Modal>
    </Card>
  );
};

export default ProfilePage;
