import { useEffect, useState } from 'react';
import { Table, Button, Card, Form, Input, Modal, Tag } from 'antd';
import { SearchOutlined, ReloadOutlined } from '@ant-design/icons';
import { getInventorySummaryList, getInventoryBatchList } from '../../api/inventory';
import usePageTable from '../../hooks/usePageTable';

const InventoryPage = () => {
  const [searchForm] = Form.useForm();
  const [batchModalOpen, setBatchModalOpen] = useState(false);
  const [batchData, setBatchData] = useState([]);
  const [currentRecord, setCurrentRecord] = useState(null);
  const { data, loading, pagination, handleSearch, handlePageChange, initLoad } = usePageTable(getInventorySummaryList);
  useEffect(() => { initLoad(); }, []);

  const showBatchDetail = async (record) => {
    setCurrentRecord(record);
    try {
      const result = await getInventoryBatchList({ skuId: record.skuId });
      setBatchData(result?.records || []);
    } catch { setBatchData([]); }
    setBatchModalOpen(true);
  };

  const columns = [
    { title: '产品SKU', dataIndex: 'skuId', key: 'skuId' },
    { title: '产品名称', dataIndex: 'prodName', key: 'prodName' },
    { title: '总库存', dataIndex: 'totalQty', key: 'totalQty' },
    { title: '可用', dataIndex: 'availableQty', key: 'availableQty' },
    { title: '冻结', dataIndex: 'frozenQty', key: 'frozenQty' },
    { title: '操作', key: 'action', render: (_, r) => <Button type="link" onClick={() => showBatchDetail(r)}>批次详情</Button> },
  ];

  const batchColumns = [
    { title: '批次号', dataIndex: 'batchCode', key: 'batchCode' },
    { title: '库位', dataIndex: 'binName', key: 'binName' },
    { title: '数量', dataIndex: 'quantity', key: 'quantity' },
    { title: '生产日期', dataIndex: 'productionDate', key: 'productionDate' },
    { title: '过期日期', dataIndex: 'expiryDate', key: 'expiryDate' },
    { title: '状态', dataIndex: 'status', key: 'status', render: (v) => <Tag>{v}</Tag> },
  ];

  return (
    <div>
      <Card style={{ marginBottom: 16 }}>
        <Form form={searchForm} layout="inline" onFinish={handleSearch}>
          <Form.Item name="skuId"><Input placeholder="产品SKU" /></Form.Item>
          <Form.Item name="skuName"><Input placeholder="产品名称" /></Form.Item>
          <Form.Item><Button type="primary" htmlType="submit" icon={<SearchOutlined />}>搜索</Button></Form.Item>
          <Form.Item><Button icon={<ReloadOutlined />} onClick={() => { searchForm.resetFields(); handleSearch({}); }}>重置</Button></Form.Item>
        </Form>
      </Card>
      <Card title="库存汇总">
        <Table rowKey="skuId" columns={columns} dataSource={data} loading={loading}
          pagination={{ ...pagination, showSizeChanger: true, showTotal: (t) => `共 ${t} 条`, onChange: handlePageChange }} />
      </Card>
      <Modal title={`批次详情 - ${currentRecord?.skuId || ''}`} open={batchModalOpen} onCancel={() => setBatchModalOpen(false)} footer={null} width={800}>
        <Table rowKey="batchCode" columns={batchColumns} dataSource={batchData} pagination={false} size="small" />
      </Modal>
    </div>
  );
};

export default InventoryPage;
