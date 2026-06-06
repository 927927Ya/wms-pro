import { useEffect } from 'react';
import { Table, Button, Card, message, Tag } from 'antd';
import { getReceivingTaskList, completeReceivingTask } from '../../api/inbound';
import usePageTable from '../../hooks/usePageTable';
import useAuth from '../../stores/useAuth';

const PalletReceivingPage = () => {
  const { hasAnyPermission } = useAuth();
  const { data, loading, pagination, handlePageChange, refresh, initLoad } = usePageTable(getReceivingTaskList);
  useEffect(() => { initLoad(); }, []);

  const taskStatusMap = { 0: '待处理', 1: '进行中', 2: '已完成' };
  const columns = [
    { title: '任务ID', dataIndex: 'id', key: 'id' },
    { title: '入库明细ID', dataIndex: 'inboundDetailsId', key: 'inboundDetailsId' },
    { title: '托盘号', dataIndex: 'palletName', key: 'palletName' },
    { title: '待收数量', dataIndex: 'toReceivedQty', key: 'toReceivedQty' },
    { title: '已收数量', dataIndex: 'receivedQty', key: 'receivedQty' },
    { title: '状态', dataIndex: 'taskStatus', key: 'taskStatus', render: (v) => <Tag>{taskStatusMap[v] || v}</Tag> },
    {
      title: '操作', key: 'action',
      render: (_, r) => (
        r.taskStatus !== 2 && hasAnyPermission(['inbound:manage', 'inbound:receive']) && (
          <Button type="primary" size="small" onClick={async () => { await completeReceivingTask(r.id, 0); message.success('收货完成'); refresh(); }}>完成收货</Button>
        )
      ),
    },
  ];

  return (
    <Card title="托盘收货">
      <Table rowKey="id" columns={columns} dataSource={data} loading={loading}
        pagination={{ ...pagination, showSizeChanger: true, showTotal: (t) => `共 ${t} 条`, onChange: handlePageChange }} />
    </Card>
  );
};

export default PalletReceivingPage;
