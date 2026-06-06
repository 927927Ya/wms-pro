import { useEffect, useState } from 'react';
import { Table, Button, Space, Input, InputNumber, Form, Popconfirm, message, Card, Modal, Select, Tag } from 'antd';
import { PlusOutlined, SearchOutlined, ReloadOutlined, EyeOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { getInboundList, createInbound, updateInbound, deleteInbound, generateReceivingTasks } from '../../api/inbound';
import { getCustomerList } from '../../api/customer';
import { getWarehouseList } from '../../api/warehouse';
import { getSkuList } from '../../api/product';
import usePageTable from '../../hooks/usePageTable';
import useAuth from '../../stores/useAuth';

const InboundListPage = () => {
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
  const { data, loading, pagination, handleSearch, handlePageChange, refresh, initLoad } = usePageTable(getInboundList);
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
        await updateInbound({ ...values, id: editingRecord.id });
      } else {
        const payload = {
          inbound: {
            supplierId: Number(values.supplierId),
            warehouseId: Number(values.warehouseId),
            inboundType: values.inboundType || 'PURCHASE',
            remark: values.remark,
          },
          inboundDetails: [{
            skuId: Number(values.skuId),
            toReceivedQty: Number(values.quantity),
          }],
        };
        await createInbound(payload);
      }
      message.success(editingRecord ? '更新成功' : '创建成功');
      setModalOpen(false); refresh();
    } catch {} finally { setSubmitting(false); }
  };

  const statusMap = { '0': '待入库', '1': '收货中', '2': '已完成', '4': '待收货' };
  const statusColor = { '0': 'blue', '1': 'orange', '2': 'green', '4': 'purple' };
  const columns = [
    { title: '入库单号', dataIndex: 'inboundNo', key: 'inboundNo' },
    { title: '供应商', dataIndex: 'supplierName', key: 'supplierName' },
    { title: '仓库', dataIndex: 'warehouseName', key: 'warehouseName' },
    { title: '类型', dataIndex: 'inboundType', key: 'inboundType' },
    { title: '状态', dataIndex: 'inboundStatus', key: 'inboundStatus',
      render: (v) => <Tag color={statusColor[v] || 'default'}>{statusMap[v] || v}</Tag>,
    },
    {
      title: '操作', key: 'action',
      render: (_, r) => (
        <Space>
          <Button type="link" icon={<EyeOutlined />} onClick={() => { setDetailRecord(r); setDetailOpen(true); }}>详情</Button>
          {hasPermission('inbound:manage') && <>
            <Button type="link" icon={<EditOutlined />} onClick={() => handleEdit(r)}>编辑</Button>
            {(r.inboundStatus === '0' || r.inboundStatus === '4') && (
              <Button type="link" onClick={async () => { await generateReceivingTasks(r.id); message.success('收货任务已生成'); refresh(); }}>生成收货任务</Button>
            )}
            <Popconfirm title="确定删除？" onConfirm={async () => { await deleteInbound(r.id); message.success('删除成功'); refresh(); }}>
              <Button type="link" danger icon={<DeleteOutlined />}>删除</Button>
            </Popconfirm>
          </>}
        </Space>
      ),
    },
  ];

  return (
    <div>
      <Card style={{ marginBottom: 16 }}>
        <Form form={searchForm} layout="inline" onFinish={handleSearch}>
          <Form.Item name="inboundNo"><Input placeholder="入库单号" /></Form.Item>
          <Form.Item name="inboundStatus"><Select placeholder="状态" allowClear style={{ width: 120 }} options={[{ label: '待入库', value: '0' }, { label: '收货中', value: '1' }, { label: '已完成', value: '2' }, { label: '待收货', value: '4' }]} /></Form.Item>
          <Form.Item><Button type="primary" htmlType="submit" icon={<SearchOutlined />}>搜索</Button></Form.Item>
          <Form.Item><Button icon={<ReloadOutlined />} onClick={() => { searchForm.resetFields(); handleSearch({}); }}>重置</Button></Form.Item>
        </Form>
      </Card>
      <Card title="入库单列表" extra={hasPermission('inbound:manage') && <Button type="primary" icon={<PlusOutlined />} onClick={handleCreate}>新增入库单</Button>}>
        <Table rowKey="id" columns={columns} dataSource={data} loading={loading}
          pagination={{ ...pagination, showSizeChanger: true, showTotal: (t) => `共 ${t} 条`, onChange: handlePageChange }} />
      </Card>

      <Modal title={editingRecord ? '编辑入库单' : '新增入库单'} open={modalOpen} onOk={handleSubmit} onCancel={() => setModalOpen(false)} confirmLoading={submitting} destroyOnClose>
        <Form form={form} layout="vertical">
          <Form.Item name="inboundType" label="入库类型" rules={[{ required: true }]}>
            <Select placeholder="选择类型" options={[{ value: 'PURCHASE', label: '采购入库' }, { value: 'RETURN', label: '退货入库' }, { value: 'TRANSFER', label: '调拨入库' }, { value: 'OTHER', label: '其它' }]} />
          </Form.Item>
          <Form.Item name="supplierId" label="供应商" rules={[{ required: true, message: '请选择供应商' }]}>
            <Select placeholder="选择供应商" showSearch optionFilterProp="label"
              options={customers.map(c => ({ value: c.id, label: `${c.code} - ${c.name}` }))} />
          </Form.Item>
          <Form.Item name="warehouseId" label="仓库" rules={[{ required: true }]}>
            <Select placeholder="选择仓库" allowClear showSearch optionFilterProp="label"
              options={warehouses.map(w => ({ value: w.id, label: `${w.warehouseCode} - ${w.warehouseName}` }))} />
          </Form.Item>
          <Form.Item name="skuId" label="产品SKU" rules={[{ required: true, message: '请选择产品SKU' }]}>
            <Select placeholder="选择产品" showSearch optionFilterProp="label"
              options={skus.map(s => ({ value: s.id, label: `${s.skuCode} - ${s.skuName}` }))} />
          </Form.Item>
          <Form.Item name="quantity" label="数量" rules={[{ required: true, message: '请输入数量' }]}>
            <InputNumber min={0.01} step={1} style={{ width: '100%' }} placeholder="入库数量" />
          </Form.Item>
          <Form.Item name="remark" label="备注"><Input.TextArea rows={2} /></Form.Item>
        </Form>
      </Modal>

      <Modal title="入库单详情" open={detailOpen} onCancel={() => setDetailOpen(false)} footer={null}>
        {detailRecord && Object.entries(detailRecord).map(([k, v]) => (
          <p key={k}><strong>{k}：</strong>{String(v)}</p>
        ))}
      </Modal>
    </div>
  );
};

export default InboundListPage;
