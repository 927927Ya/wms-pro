import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Table, Button, Space, Input, Form, Popconfirm, message, Card } from 'antd';
import { PlusOutlined, SearchOutlined, ReloadOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { getWarehouseList, deleteWarehouse } from '../../api/warehouse';
import usePageTable from '../../hooks/usePageTable';
import useAuth from '../../stores/useAuth';

const WarehouseListPage = () => {
  const { hasPermission } = useAuth();
  const navigate = useNavigate();
  const [searchForm] = Form.useForm();
  const { data, loading, pagination, handleSearch, handlePageChange, refresh, initLoad } =
    usePageTable(getWarehouseList);

  useEffect(() => { initLoad(); }, []);

  const columns = [
    { title: '仓库编码', dataIndex: 'warehouseCode', key: 'warehouseCode' },
    { title: '仓库名称', dataIndex: 'warehouseName', key: 'warehouseName' },
    { title: '地址', dataIndex: 'address', key: 'address', ellipsis: true },
    { title: '城市', dataIndex: 'city', key: 'city' },
    { title: '状态', dataIndex: 'isActive', key: 'isActive', render: (v) => (v === '1' ? '启用' : '停用') },
    {
      title: '操作', key: 'action',
      render: (_, r) => (
        <Space>
          {hasPermission('warehouse:manage') && <Button type="link" icon={<EditOutlined />} onClick={() => navigate(`/warehouse/edit/${r.id}`)}>编辑</Button>}
          {hasPermission('warehouse:manage') && <Popconfirm title="确定删除？" onConfirm={async () => { await deleteWarehouse(r.id); message.success('删除成功'); refresh(); }}>
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
          <Form.Item name="warehouseName"><Input placeholder="仓库名称" /></Form.Item>
          <Form.Item name="warehouseCode"><Input placeholder="仓库编码" /></Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" icon={<SearchOutlined />}>搜索</Button>
          </Form.Item>
          <Form.Item>
            <Button icon={<ReloadOutlined />} onClick={() => { searchForm.resetFields(); handleSearch({}); }}>重置</Button>
          </Form.Item>
        </Form>
      </Card>
      <Card title="仓库列表" extra={hasPermission('warehouse:manage') && <Button type="primary" icon={<PlusOutlined />} onClick={() => navigate('/warehouse/create')}>新增仓库</Button>}>
        <Table rowKey="id" columns={columns} dataSource={data} loading={loading}
          pagination={{ ...pagination, showSizeChanger: true, showTotal: (t) => `共 ${t} 条`, onChange: handlePageChange }} />
      </Card>
    </div>
  );
};

export default WarehouseListPage;
