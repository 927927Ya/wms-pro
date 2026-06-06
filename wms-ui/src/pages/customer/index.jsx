import { useEffect, useState } from 'react';
import { Table, Button, Space, Input, Form, Popconfirm, Modal, message, Card } from 'antd';
import { PlusOutlined, SearchOutlined, ReloadOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { getCustomerList, createCustomer, updateCustomer, deleteCustomer } from '../../api/customer';
import usePageTable from '../../hooks/usePageTable';
import useAuth from '../../stores/useAuth';

const CustomerPage = () => {
  const { hasPermission } = useAuth();
  const [searchForm] = Form.useForm();
  const [modalOpen, setModalOpen] = useState(false);
  const [editingRecord, setEditingRecord] = useState(null);
  const [form] = Form.useForm();
  const [submitting, setSubmitting] = useState(false);
  const { data, loading, pagination, handleSearch, handlePageChange, refresh, initLoad } = usePageTable(getCustomerList);
  useEffect(() => { initLoad(); }, []);

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      setSubmitting(true);
      if (editingRecord) await updateCustomer({ ...values, id: editingRecord.id });
      else await createCustomer(values);
      message.success(editingRecord ? '更新成功' : '创建成功');
      setModalOpen(false); refresh();
    } catch {} finally { setSubmitting(false); }
  };

  const columns = [
    { title: '客户编码', dataIndex: 'code', key: 'code' },
    { title: '客户名称', dataIndex: 'name', key: 'name' },
    { title: '电话', dataIndex: 'phone', key: 'phone' },
    { title: '邮箱', dataIndex: 'email', key: 'email' },
    { title: '省份', dataIndex: 'province', key: 'province' },
    { title: '城市', dataIndex: 'city', key: 'city' },
    { title: '地址', dataIndex: 'address', key: 'address', ellipsis: true },
    { title: '状态', dataIndex: 'isEnabled', key: 'isEnabled', render: (v) => v === 1 ? '启用' : '停用' },
    { title: '操作', key: 'action', render: (_, r) => (
      <Space>
        {hasPermission('customer:manage') && <Button type="link" icon={<EditOutlined />} onClick={() => { setEditingRecord(r); form.setFieldsValue(r); setModalOpen(true); }}>编辑</Button>}
        {hasPermission('customer:manage') && <Popconfirm title="确定删除？" onConfirm={async () => { await deleteCustomer(r.id); message.success('删除成功'); refresh(); }}>
          <Button type="link" danger icon={<DeleteOutlined />}>删除</Button>
        </Popconfirm>}
      </Space>
    )},
  ];

  return (
    <div>
      <Card style={{ marginBottom: 16 }}>
        <Form form={searchForm} layout="inline" onFinish={handleSearch}>
          <Form.Item name="name"><Input placeholder="客户名称" /></Form.Item>
          <Form.Item name="code"><Input placeholder="客户编码" /></Form.Item>
          <Form.Item><Button type="primary" htmlType="submit" icon={<SearchOutlined />}>搜索</Button></Form.Item>
          <Form.Item><Button icon={<ReloadOutlined />} onClick={() => { searchForm.resetFields(); handleSearch({}); }}>重置</Button></Form.Item>
        </Form>
      </Card>
      <Card title="客户列表" extra={hasPermission('customer:manage') && <Button type="primary" icon={<PlusOutlined />} onClick={() => { setEditingRecord(null); form.resetFields(); setModalOpen(true); }}>新增客户</Button>}>
        <Table rowKey="id" columns={columns} dataSource={data} loading={loading}
          pagination={{ ...pagination, showSizeChanger: true, showTotal: (t) => `共 ${t} 条`, onChange: handlePageChange }} />
      </Card>
      <Modal title={editingRecord ? '编辑客户' : '新增客户'} open={modalOpen} onOk={handleSubmit} onCancel={() => setModalOpen(false)} confirmLoading={submitting} destroyOnClose>
        <Form form={form} layout="vertical">
          <Form.Item name="code" label="客户编码" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="name" label="客户名称" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="phone" label="电话"><Input /></Form.Item>
          <Form.Item name="email" label="邮箱"><Input /></Form.Item>
          <Form.Item name="province" label="省份"><Input /></Form.Item>
          <Form.Item name="city" label="城市"><Input /></Form.Item>
          <Form.Item name="address" label="地址"><Input.TextArea rows={2} /></Form.Item>
          <Form.Item name="zipcode" label="邮编"><Input /></Form.Item>
          <Form.Item name="businessIntro" label="主营业务"><Input /></Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default CustomerPage;
