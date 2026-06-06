import { useEffect, useState } from 'react';
import { Table, Button, Space, Input, Form, Popconfirm, Modal, message, Card, Tree } from 'antd';
import { PlusOutlined, SearchOutlined, ReloadOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { getRoleList, createRole, updateRole, deleteRole } from '../../../api/system';
import { getAuthorities } from '../../../api/auth';
import usePageTable from '../../../hooks/usePageTable';
import useAuth from '../../../stores/useAuth';

const RolePage = () => {
  const { hasPermission } = useAuth();
  const [searchForm] = Form.useForm();
  const [modalOpen, setModalOpen] = useState(false);
  const [editingRecord, setEditingRecord] = useState(null);
  const [form] = Form.useForm();
  const [submitting, setSubmitting] = useState(false);
  const [authorityTree, setAuthorityTree] = useState([]);
  const [checkedKeys, setCheckedKeys] = useState([]);
  const { data, loading, pagination, handleSearch, handlePageChange, refresh, initLoad } = usePageTable(getRoleList);
  useEffect(() => { initLoad(); loadAuthorities(); }, []);

  const loadAuthorities = async () => {
    try {
      const list = await getAuthorities();
      const buildTree = (items, parentId = 0) =>
        items.filter((i) => i.parentId === parentId).map((i) => ({ title: i.authorityName, key: String(i.authorityId), children: buildTree(items, i.authorityId) }));
      setAuthorityTree(buildTree(list || []));
    } catch { setAuthorityTree([]); }
  };

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      setSubmitting(true);
      const payload = { ...values, authorityIds: checkedKeys.map(Number) };
      if (editingRecord) await updateRole({ ...payload, roleId: editingRecord.roleId });
      else await createRole(payload);
      message.success(editingRecord ? '更新成功' : '创建成功');
      setModalOpen(false); refresh();
    } catch {} finally { setSubmitting(false); }
  };

  const openEdit = (r) => {
    setEditingRecord(r);
    form.setFieldsValue(r);
    setCheckedKeys((r.authorityIds || []).map(String));
    setModalOpen(true);
  };

  const columns = [
    { title: '角色名称', dataIndex: 'roleName', key: 'roleName' },
    { title: '角色键', dataIndex: 'roleKey', key: 'roleKey' },
    { title: '排序', dataIndex: 'roleSort', key: 'roleSort' },
    { title: '状态', dataIndex: 'status', key: 'status' },
    {
      title: '操作', key: 'action',
      render: (_, r) => (
        <Space>
          {hasPermission('sys:role:manage') && <Button type="link" icon={<EditOutlined />} onClick={() => openEdit(r)}>编辑</Button>}
          {hasPermission('sys:role:manage') && <Popconfirm title="确定删除？" onConfirm={async () => { await deleteRole(r.roleId); message.success('删除成功'); refresh(); }}>
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
          <Form.Item name="roleName"><Input placeholder="角色名称" /></Form.Item>
          <Form.Item><Button type="primary" htmlType="submit" icon={<SearchOutlined />}>搜索</Button></Form.Item>
          <Form.Item><Button icon={<ReloadOutlined />} onClick={() => { searchForm.resetFields(); handleSearch({}); }}>重置</Button></Form.Item>
        </Form>
      </Card>
      <Card title="角色列表" extra={hasPermission('sys:role:manage') && <Button type="primary" icon={<PlusOutlined />} onClick={() => { setEditingRecord(null); form.resetFields(); setCheckedKeys([]); setModalOpen(true); }}>新增角色</Button>}>
        <Table rowKey="roleId" columns={columns} dataSource={data} loading={loading}
          pagination={{ ...pagination, showSizeChanger: true, showTotal: (t) => `共 ${t} 条`, onChange: handlePageChange }} />
      </Card>

      <Modal title={editingRecord ? '编辑角色' : '新增角色'} open={modalOpen} onOk={handleSubmit} onCancel={() => setModalOpen(false)} confirmLoading={submitting} width={600} destroyOnClose>
        <Form form={form} layout="vertical">
          <Form.Item name="roleName" label="角色名称" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="roleKey" label="角色键" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="roleSort" label="排序"><Input /></Form.Item>
          <Form.Item name="status" label="状态"><Input placeholder="0正常 1停用" /></Form.Item>
          <Form.Item label="权限分配">
            <Tree checkable defaultExpandAll checkedKeys={checkedKeys} onCheck={(keys) => setCheckedKeys(keys)} treeData={authorityTree} style={{ maxHeight: 300, overflow: 'auto' }} />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default RolePage;
