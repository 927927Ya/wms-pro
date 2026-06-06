import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Table, Button, Space, Input, Form, Popconfirm, message, Card } from 'antd';
import { PlusOutlined, SearchOutlined, ReloadOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { getRackList, deleteRack } from '../../../api/warehouse-rack';
import usePageTable from '../../../hooks/usePageTable';
import useAuth from '../../../stores/useAuth';

const RackListPage = () => {
  const { hasPermission } = useAuth();
  const navigate = useNavigate();
  const [searchForm] = Form.useForm();
  const { data, loading, pagination, handleSearch, handlePageChange, refresh, initLoad } = usePageTable(getRackList);
  useEffect(() => { initLoad(); }, []);

  const columns = [
    { title: '货架名称', dataIndex: 'rackName', key: 'rackName' },
    { title: '仓库ID', dataIndex: 'warehouseId', key: 'warehouseId' },
    { title: '库区ID', dataIndex: 'zoneId', key: 'zoneId' },
    { title: '类型', dataIndex: 'rackType', key: 'rackType' },
    {
      title: '操作', key: 'action',
      render: (_, r) => (
        <Space>
          {hasPermission('warehouse:manage') && <Button type="link" icon={<EditOutlined />} onClick={() => navigate(`/warehouse/rack/edit/${r.id}`)}>编辑</Button>}
          {hasPermission('warehouse:manage') && <Popconfirm title="确定删除？" onConfirm={async () => { await deleteRack(r.id); message.success('删除成功'); refresh(); }}>
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
          <Form.Item name="rackName"><Input placeholder="货架名称" /></Form.Item>
          <Form.Item><Button type="primary" htmlType="submit" icon={<SearchOutlined />}>搜索</Button></Form.Item>
          <Form.Item><Button icon={<ReloadOutlined />} onClick={() => { searchForm.resetFields(); handleSearch({}); }}>重置</Button></Form.Item>
        </Form>
      </Card>
      <Card title="货架列表" extra={hasPermission('warehouse:manage') && <Button type="primary" icon={<PlusOutlined />} onClick={() => navigate('/warehouse/rack/create')}>新增货架</Button>}>
        <Table rowKey="id" columns={columns} dataSource={data} loading={loading}
          pagination={{ ...pagination, showSizeChanger: true, showTotal: (t) => `共 ${t} 条`, onChange: handlePageChange }} />
      </Card>
    </div>
  );
};

export default RackListPage;
