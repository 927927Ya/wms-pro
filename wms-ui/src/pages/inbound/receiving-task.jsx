import { useEffect } from 'react';
import { Table, Button, Card, message, Tag } from 'antd';
import { getReceivingTaskList, completeReceivingTask } from '../../api/inbound';
import usePageTable from '../../hooks/usePageTable';
import useAuth from '../../stores/useAuth';

const ReceivingTaskPage = () => {
  const { hasAnyPermission } = useAuth();
  const { data, loading, pagination, handlePageChange, refresh, initLoad } = usePageTable(getReceivingTaskList);
  useEffect(() => { initLoad(); }, []);

  const taskStatusMap = { 0: '待处理', 1: '进行中', 2: '已完成' };
  const columns = [
    { title: '入库单号', dataIndex: 'inboundNo', key: 'inboundNo' },
    { title: '托盘号', dataIndex: 'palletName', key: 'palletName' },
    { title: 'SKU', dataIndex: 'skuCode', key: 'skuCode' },
    { title: '待收数量', dataIndex: 'toReceivedQty', key: 'toReceivedQty' },
    { title: '已收数量', dataIndex: 'receivedQty', key: 'receivedQty' },
    { title: '目标库位ID', dataIndex: 'binId', key: 'binId' },
    { title: '状态', dataIndex: 'taskStatus', key: 'taskStatus', render: (v) => <Tag>{taskStatusMap[v] || v}</Tag> },
    {
      title: '操作', key: 'action',
      render: (_, r) => (
        r.taskStatus !== 2 && hasAnyPermission(['inbound:manage', 'inbound:receive']) && (
          <Button type="link" onClick={async () => { await completeReceivingTask(r.id, 0); message.success('收货完成'); refresh(); }}>完成收货</Button>
        )
      ),
    },
  ];

  return (
    <Card title="收货任务">
      <Table rowKey="id" columns={columns} dataSource={data} loading={loading}
        pagination={{ ...pagination, showSizeChanger: true, showTotal: (t) => `共 ${t} 条`, onChange: handlePageChange }} />
    </Card>
  );
};

export default ReceivingTaskPage;
