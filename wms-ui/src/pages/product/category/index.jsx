import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Table, Button, Space, Input, Form, Popconfirm, message, Card, Modal } from 'antd';
import { PlusOutlined, SearchOutlined, ReloadOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { getCategoryList, createCategory, updateCategory, deleteCategory } from '../../../api/product';
import usePageTable from '../../../hooks/usePageTable';
import useAuth from '../../../stores/useAuth';


const CategoryListPage = () => {
  const { hasPermission } = useAuth();
  const navigate = useNavigate();
  const [searchForm] = Form.useForm();
  const [modalOpen, setModalOpen] = useState(false);
  const [editingRecord, setEditingRecord] = useState(null);
  const [form] = Form.useForm();
  const [submitting, setSubmitting] = useState(false);
  const { data, loading, pagination, handleSearch, handlePageChange, refresh, initLoad } = usePageTable(getCategoryList);
  useEffect(() => { initLoad(); }, []);

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      setSubmitting(true);
      if (editingRecord) await updateCategory({ ...values, id: editingRecord.id });
      else await createCategory(values);
      message.success(editingRecord ? '更新成功' : '创建成功');
      setModalOpen(false); refresh();
    } catch {} finally { setSubmitting(false); }
  };

  const columns = [
    { title: '分类名称', dataIndex: 'categoryName', key: 'categoryName' },
    { title: '父级ID', dataIndex: 'parentId', key: 'parentId' },
    { title: '排序', dataIndex: 'orderNum', key: 'orderNum' },
    {
      title: '操作', key: 'action',
      render: (_, r) => (
        <Space>
          {hasPermission('prod:manage') && <Button type="link" icon={<EditOutlined />} onClick={() => { setEditingRecord(r); form.setFieldsValue(r); setModalOpen(true); }}>编辑</Button>}
          {hasPermission('prod:manage') && <Popconfirm title="确定删除？" onConfirm={async () => { await deleteCategory(r.id); message.success('删除成功'); refresh(); }}>
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
          <Form.Item name="categoryName"><Input placeholder="分类名称" /></Form.Item>
          <Form.Item><Button type="primary" htmlType="submit" icon={<SearchOutlined />}>搜索</Button></Form.Item>
          <Form.Item><Button icon={<ReloadOutlined />} onClick={() => { searchForm.resetFields(); handleSearch({}); }}>重置</Button></Form.Item>
        </Form>
      </Card>
      <Card title="产品分类" extra={hasPermission('prod:manage') && <Button type="primary" icon={<PlusOutlined />} onClick={() => { setEditingRecord(null); form.resetFields(); setModalOpen(true); }}>新增分类</Button>}>
        <Table rowKey="id" columns={columns} dataSource={data} loading={loading}
          pagination={{ ...pagination, showSizeChanger: true, showTotal: (t) => `共 ${t} 条`, onChange: handlePageChange }} />
      </Card>

      <Modal title={editingRecord ? '编辑分类' : '新增分类'} open={modalOpen} onOk={handleSubmit} onCancel={() => setModalOpen(false)} confirmLoading={submitting} destroyOnClose>
        <Form form={form} layout="vertical">
          <Form.Item name="categoryName" label="分类名称" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="parentId" label="父级ID"><Input placeholder="0表示顶级" /></Form.Item>
          <Form.Item name="orderNum" label="排序"><Input /></Form.Item>
          <Form.Item name="remark" label="备注"><Input.TextArea rows={2} /></Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default CategoryListPage;
