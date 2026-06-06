import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Table, Button, Space, Input, Form, Popconfirm, message, Card } from 'antd';
import { PlusOutlined, SearchOutlined, ReloadOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { getSkuList, deleteSku } from '../../../api/product';
import usePageTable from '../../../hooks/usePageTable';
import useAuth from '../../../stores/useAuth';

const SkuListPage = () => {
  const { hasPermission } = useAuth();
  const navigate = useNavigate();
  const [searchForm] = Form.useForm();
  const { data, loading, pagination, handleSearch, handlePageChange, refresh, initLoad } = usePageTable(getSkuList);
  useEffect(() => { initLoad(); }, []);

  const columns = [
    { title: 'SKU编码', dataIndex: 'skuCode', key: 'skuCode' },
    { title: '产品名称', dataIndex: 'skuName', key: 'skuName' },
    { title: '英文名称', dataIndex: 'prodNameEng', key: 'prodNameEng' },
    { title: '中文名称', dataIndex: 'prodNameChn', key: 'prodNameChn' },
    { title: '分类', dataIndex: 'categoryName', key: 'categoryName' },
    {
      title: '操作', key: 'action',
      render: (_, r) => (
        <Space>
          {hasPermission('prod:manage') && <Button type="link" icon={<EditOutlined />} onClick={() => navigate(`/product/sku/edit/${r.id}`)}>编辑</Button>}
          {hasPermission('prod:manage') && <Popconfirm title="确定删除？" onConfirm={async () => { await deleteSku(r.id); message.success('删除成功'); refresh(); }}>
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
          <Form.Item name="skuName"><Input placeholder="产品名称" /></Form.Item>
          <Form.Item name="skuCode"><Input placeholder="SKU编码" /></Form.Item>
          <Form.Item><Button type="primary" htmlType="submit" icon={<SearchOutlined />}>搜索</Button></Form.Item>
          <Form.Item><Button icon={<ReloadOutlined />} onClick={() => { searchForm.resetFields(); handleSearch({}); }}>重置</Button></Form.Item>
        </Form>
      </Card>
      <Card title="产品列表" extra={hasPermission('prod:manage') && <Button type="primary" icon={<PlusOutlined />} onClick={() => navigate('/product/sku/create')}>新增产品</Button>}>
        <Table rowKey="id" columns={columns} dataSource={data} loading={loading}
          pagination={{ ...pagination, showSizeChanger: true, showTotal: (t) => `共 ${t} 条`, onChange: handlePageChange }} />
      </Card>
    </div>
  );
};

export default SkuListPage;
