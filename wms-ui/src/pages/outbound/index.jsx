import { useEffect, useState } from 'react';
import { Table, Button, Space, Input, InputNumber, Form, Popconfirm, message, Card, Modal, Select, Tag } from 'antd';
import { PlusOutlined, SearchOutlined, ReloadOutlined, EyeOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { getOutboundList, createOutbound, updateOutbound, deleteOutbound } from '../../api/outbound';
import { getCustomerList } from '../../api/customer';
import { getWarehouseList } from '../../api/warehouse';
import { getSkuList } from '../../api/product';
import usePageTable from '../../hooks/usePageTable';
import useAuth from '../../stores/useAuth';

const OutboundListPage = () => {
  const { hasPermission } = useAuth();
  const [searchForm] = Form.useForm();
  const [modalOpen, setModalOpen] = useState(false);
  const [editingRecord, setEditingRecord] = useState(null);
  const [detailOpen, setDetailOpen] = useState(false);
  const [detailRecord, setDetailRecord] = useState(null);
  const [form] = Form.useForm();
  const [submitting, setSubmitting] = useState(false);
  const [customers, setCustomers] = useState([]);
  const [warehouses, setWarehouses] = useState([]);
  const [skus, setSkus] = useState([]);
  const { data, loading, pagination, handleSearch, handlePageChange, refresh, initLoad } = usePageTable(getOutboundList);
  useEffect(() => { initLoad(); loadOptions(); }, []);

  const loadOptions = async () => {
    try {
      const [cRes, wRes, sRes] = await Promise.all([
        getCustomerList({}),
        getWarehouseList({}),
        getSkuList({}),
      ]);
      setCustomers(cRes?.records || []);
      setWarehouses(wRes?.records || []);
      setSkus(sRes?.records || []);
    } catch { /* ignore */ }
  };

  const handleCreate = () => { setEditingRecord(null); form.resetFields(); setModalOpen(true); };
  const handleEdit = (r) => { setEditingRecord(r); form.setFieldsValue(r); setModalOpen(true); };

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      setSubmitting(true);
      if (editingRecord) {
        await updateOutbound({ ...values, id: editingRecord.id });
      } else {
        const payload = {
          outbound: {
            warehouseId: Number(values.warehouseId),
            customerId: values.customerId ? Number(values.customerId) : undefined,
            remark: values.remark,
          },
          outboundDetails: [{
            skuId: Number(values.skuId),
            toPickedQty: Number(values.quantity),
          }],
        };
        await createOutbound(payload);
      }
      message.success(editingRecord ? '更新成功' : '创建成功');
      setModalOpen(false); refresh();
    } catch {} finally { setSubmitting(false); }
  };

  const columns = [
    { title: 'ID', dataIndex: 'id', key: 'id' },
    { title: '仓库ID', dataIndex: 'warehouseId', key: 'warehouseId' },
    { title: '客户ID', dataIndex: 'customerId', key: 'customerId' },
    { title: '出库日期', dataIndex: 'outboundDate', key: 'outboundDate' },
    { title: '类型', dataIndex: 'outboundType', key: 'outboundType' },
    { title: '状态', dataIndex: 'outboundStatus', key: 'outboundStatus',
      render: (v) => {
        const colors = { PENDING: 'blue', PICKING: 'orange', COMPLETED: 'green' };
        return <Tag color={colors[v]}>{v}</Tag>;
      },
    },
    {
      title: '操作', key: 'action',
      render: (_, r) => (
        <Space>
          <Button type="link" icon={<EyeOutlined />} onClick={() => { setDetailRecord(r); setDetailOpen(true); }}>详情</Button>
          {hasPermission('outbound:manage') && <Button type="link" icon={<EditOutlined />} onClick={() => handleEdit(r)}>编辑</Button>}
          {hasPermission('outbound:manage') && <Popconfirm title="确定删除？" onConfirm={async () => { await deleteOutbound(r.id); message.success('删除成功'); refresh(); }}>
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
          <Form.Item name="outboundCode"><Input placeholder="出库单号" /></Form.Item>
          <Form.Item><Button type="primary" htmlType="submit" icon={<SearchOutlined />}>搜索</Button></Form.Item>
          <Form.Item><Button icon={<ReloadOutlined />} onClick={() => { searchForm.resetFields(); handleSearch({}); }}>重置</Button></Form.Item>
        </Form>
      </Card>
      <Card title="出库单列表" extra={hasPermission('outbound:manage') && <Button type="primary" icon={<PlusOutlined />} onClick={handleCreate}>新增出库单</Button>}>
        <Table rowKey="id" columns={columns} dataSource={data} loading={loading}
          pagination={{ ...pagination, showSizeChanger: true, showTotal: (t) => `共 ${t} 条`, onChange: handlePageChange }} />
      </Card>

      <Modal title={editingRecord ? '编辑出库单' : '新增出库单'} open={modalOpen} onOk={handleSubmit} onCancel={() => setModalOpen(false)} confirmLoading={submitting} destroyOnClose>
        <Form form={form} layout="vertical">
          <Form.Item name="warehouseId" label="出库仓库" rules={[{ required: true, message: '请选择仓库' }]}>
            <Select placeholder="选择仓库" showSearch optionFilterProp="label"
              options={warehouses.map(w => ({ value: w.id, label: `${w.warehouseCode} - ${w.warehouseName}` }))} />
          </Form.Item>
          <Form.Item name="customerId" label="客户">
            <Select placeholder="选择客户" allowClear showSearch optionFilterProp="label"
              options={customers.map(c => ({ value: c.id, label: `${c.code} - ${c.name}` }))} />
          </Form.Item>
          <Form.Item name="skuId" label="产品SKU" rules={[{ required: true, message: '请选择产品SKU' }]}>
            <Select placeholder="选择产品" showSearch optionFilterProp="label"
              options={skus.map(s => ({ value: s.id, label: `${s.skuCode} - ${s.skuName}` }))} />
          </Form.Item>
          <Form.Item name="quantity" label="数量" rules={[{ required: true, message: '请输入数量' }]}>
            <InputNumber min={0.01} step={1} style={{ width: '100%' }} placeholder="出库数量" />
          </Form.Item>
          <Form.Item name="remark" label="备注"><Input.TextArea rows={2} /></Form.Item>
        </Form>
      </Modal>

      <Modal title="出库单详情" open={detailOpen} onCancel={() => setDetailOpen(false)} footer={null}>
        {detailRecord && Object.entries(detailRecord).map(([k, v]) => (
          <p key={k}><strong>{k}：</strong>{String(v)}</p>
        ))}
      </Modal>
    </div>
  );
};

export default OutboundListPage;
