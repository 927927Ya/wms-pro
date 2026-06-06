import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Table, Button, Space, Input, Form, Popconfirm, message, Card } from 'antd';
import { PlusOutlined, SearchOutlined, ReloadOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { getUnitList, deleteUnit } from '../../../api/product';
import usePageTable from '../../../hooks/usePageTable';
import useAuth from '../../../stores/useAuth';

const UnitListPage = () => {
  const { hasPermission } = useAuth();
  const navigate = useNavigate();
  const [searchForm] = Form.useForm();
  const { data, loading, pagination, handleSearch, handlePageChange, refresh, initLoad } = usePageTable(getUnitList);
  useEffect(() => { initLoad(); }, []);

  const columns = [
    { title: '单位名称', dataIndex: 'dictName', key: 'dictName' },
    { title: '单位代码', dataIndex: 'dictCode', key: 'dictCode' },
    { title: '备注', dataIndex: 'remark', key: 'remark' },
    { title: '操作', key: 'action', render: (_, r) => (
      <Space>
        {hasPermission('prod:manage') && <Button type="link" icon={<EditOutlined />} onClick={() => navigate(`/product/unit/edit/${r.id}`)}>编辑</Button>}
        {hasPermission('prod:manage') && <Popconfirm title="确定删除？" onConfirm={async () => { await deleteUnit(r.id); message.success('删除成功'); refresh(); }}>
          <Button type="link" danger icon={<DeleteOutlined />}>删除</Button>
        </Popconfirm>}
      </Space>
    )},
  ];

  return (
    <div>
      <Card style={{ marginBottom: 16 }}>
        <Form form={searchForm} layout="inline" onFinish={handleSearch}>
          <Form.Item name="dictName"><Input placeholder="单位名称" /></Form.Item>
          <Form.Item><Button type="primary" htmlType="submit" icon={<SearchOutlined />}>搜索</Button></Form.Item>
          <Form.Item><Button icon={<ReloadOutlined />} onClick={() => { searchForm.resetFields(); handleSearch({}); }}>重置</Button></Form.Item>
        </Form>
      </Card>
      <Card title="单位列表" extra={hasPermission('prod:manage') && <Button type="primary" icon={<PlusOutlined />} onClick={() => navigate('/product/unit/create')}>新增单位</Button>}>
        <Table rowKey="id" columns={columns} dataSource={data} loading={loading}
          pagination={{ ...pagination, showSizeChanger: true, showTotal: (t) => `共 ${t} 条`, onChange: handlePageChange }} />
      </Card>
    </div>
  );
};

export default UnitListPage;
