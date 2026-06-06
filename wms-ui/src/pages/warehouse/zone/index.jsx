import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Table, Button, Space, Input, Form, Popconfirm, message, Card } from 'antd';
import { PlusOutlined, SearchOutlined, ReloadOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { getZoneList, deleteZone } from '../../../api/warehouse-zone';
import usePageTable from '../../../hooks/usePageTable';
import useAuth from '../../../stores/useAuth';

const ZoneListPage = () => {
  const { hasPermission } = useAuth();
  const navigate = useNavigate();
  const [searchForm] = Form.useForm();
  const { data, loading, pagination, handleSearch, handlePageChange, refresh, initLoad } = usePageTable(getZoneList);
  useEffect(() => { initLoad(); }, []);

  const columns = [
    { title: '库区名称', dataIndex: 'zoneName', key: 'zoneName' },
    { title: '仓库ID', dataIndex: 'warehouseId', key: 'warehouseId' },
    { title: '层数', dataIndex: 'layers', key: 'layers' },
    { title: '长度', dataIndex: 'length', key: 'length' },
    { title: '宽度', dataIndex: 'width', key: 'width' },
    {
      title: '操作', key: 'action',
      render: (_, r) => (
        <Space>
          {hasPermission('warehouse:manage') && <Button type="link" icon={<EditOutlined />} onClick={() => navigate(`/warehouse/zone/edit/${r.id}`)}>编辑</Button>}
          {hasPermission('warehouse:manage') && <Popconfirm title="确定删除？" onConfirm={async () => { await deleteZone(r.id); message.success('删除成功'); refresh(); }}>
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
          <Form.Item name="zoneName"><Input placeholder="库区名称" /></Form.Item>
          <Form.Item><Button type="primary" htmlType="submit" icon={<SearchOutlined />}>搜索</Button></Form.Item>
          <Form.Item><Button icon={<ReloadOutlined />} onClick={() => { searchForm.resetFields(); handleSearch({}); }}>重置</Button></Form.Item>
        </Form>
      </Card>
      <Card title="库区列表" extra={hasPermission('warehouse:manage') && <Button type="primary" icon={<PlusOutlined />} onClick={() => navigate('/warehouse/zone/create')}>新增库区</Button>}>
        <Table rowKey="id" columns={columns} dataSource={data} loading={loading}
          pagination={{ ...pagination, showSizeChanger: true, showTotal: (t) => `共 ${t} 条`, onChange: handlePageChange }} />
      </Card>
    </div>
  );
};

export default ZoneListPage;
