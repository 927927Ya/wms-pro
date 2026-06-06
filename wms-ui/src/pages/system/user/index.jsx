import { useEffect, useState } from 'react';
import { Table, Button, Space, Input, Form, Popconfirm, Modal, message, Card, Select } from 'antd';
import { PlusOutlined, SearchOutlined, ReloadOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { getUserList, createUser, updateUser, deleteUser, getRoleList } from '../../../api/system';
import usePageTable from '../../../hooks/usePageTable';
import useAuth from '../../../stores/useAuth';

const UserPage = () => {
  const { hasPermission } = useAuth();
  const [searchForm] = Form.useForm();
  const [modalOpen, setModalOpen] = useState(false);
  const [editingRecord, setEditingRecord] = useState(null);
  const [form] = Form.useForm();
  const [submitting, setSubmitting] = useState(false);
  const [roles, setRoles] = useState([]);
  const { data, loading, pagination, handleSearch, handlePageChange, refresh, initLoad } = usePageTable(getUserList);
  useEffect(() => { initLoad(); getRoleList({}).then(r => setRoles(r?.records || [])); }, []);

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      setSubmitting(true);
      const payload = { ...values };
      if (payload.roleIds?.length > 0) {
        payload.roleIds = payload.roleIds.map(Number);
      }
      if (editingRecord) await updateUser({ ...payload, userId: editingRecord.userId });
      else await createUser(payload);
      message.success(editingRecord ? '更新成功' : '创建成功');
      setModalOpen(false); refresh();
    } catch {} finally { setSubmitting(false); }
  };

  const columns = [
    { title: '昵称', dataIndex: 'nickName', key: 'nickName' },
    { title: '邮箱', dataIndex: 'email', key: 'email' },
    { title: '手机号', dataIndex: 'phonenumber', key: 'phonenumber' },
    { title: '状态', dataIndex: 'status', key: 'status', render: (v) => (v === '0' ? '正常' : '停用') },
    {
      title: '操作', key: 'action',
      render: (_, r) => (
        <Space>
          {hasPermission('sys:user:manage') && <Button type="link" icon={<EditOutlined />} onClick={() => { setEditingRecord(r); form.setFieldsValue({ ...r, roleIds: r.roleIds || [] }); setModalOpen(true); }}>编辑</Button>}
          {hasPermission('sys:user:manage') && <Popconfirm title="确定删除？" onConfirm={async () => { await deleteUser(r.userId); message.success('删除成功'); refresh(); }}>
            <Button type="link" danger icon={<DeleteOutlined />}>删除</Button>
          </Popconfirm>}
        </Space>
      ),
    },
  ];

  return (
    <div>
      <Card style={{ marginBottom: 16 }}>
        <Form form={searchForm} layout="inline" onFinish={handleSearch}>
          <Form.Item name="nickName"><Input placeholder="昵称" /></Form.Item>
          <Form.Item><Button type="primary" htmlType="submit" icon={<SearchOutlined />}>搜索</Button></Form.Item>
          <Form.Item><Button icon={<ReloadOutlined />} onClick={() => { searchForm.resetFields(); handleSearch({}); }}>重置</Button></Form.Item>
        </Form>
      </Card>
      <Card title="用户列表" extra={hasPermission('sys:user:manage') && <Button type="primary" icon={<PlusOutlined />} onClick={() => { setEditingRecord(null); form.resetFields(); setModalOpen(true); }}>新增用户</Button>}>
        <Table rowKey="userId" columns={columns} dataSource={data} loading={loading}
          pagination={{ ...pagination, showSizeChanger: true, showTotal: (t) => `共 ${t} 条`, onChange: handlePageChange }} />
      </Card>

      <Modal title={editingRecord ? '编辑用户' : '新增用户'} open={modalOpen} onOk={handleSubmit} onCancel={() => setModalOpen(false)} confirmLoading={submitting} destroyOnClose>
        <Form form={form} layout="vertical">
          <Form.Item name="nickName" label="昵称(登录名)" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="familyName" label="姓"><Input /></Form.Item>
          <Form.Item name="givenName" label="名"><Input /></Form.Item>
          <Form.Item name="email" label="邮箱"><Input /></Form.Item>
          <Form.Item name="phonenumber" label="手机号"><Input /></Form.Item>
          <Form.Item name="sex" label="性别"><Select options={[{ value: '0', label: '男' }, { value: '1', label: '女' }]} /></Form.Item>
          <Form.Item name="status" label="状态"><Select options={[{ value: '0', label: '正常' }, { value: '1', label: '停用' }]} /></Form.Item>
          <Form.Item name="roleIds" label="角色"><Select mode="multiple" placeholder="选择角色" options={roles.map(r => ({ value: r.roleId, label: r.roleName }))} /></Form.Item>
          {!editingRecord && <Form.Item name="password" label="密码" rules={[{ required: true, min: 6 }]}><Input.Password /></Form.Item>}
        </Form>
      </Modal>

    </div>
  );
};

export default UserPage;
