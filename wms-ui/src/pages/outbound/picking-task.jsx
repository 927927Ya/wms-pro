import { useEffect } from 'react';
import { Table, Button, Card, message, Tag } from 'antd';
import { getPickingTaskList, completePickingTask } from '../../api/outbound';
import usePageTable from '../../hooks/usePageTable';
import useAuth from '../../stores/useAuth';

const PickingTaskPage = () => {
  const { hasAnyPermission } = useAuth();
  const { data, loading, pagination, handlePageChange, refresh, initLoad } = usePageTable(getPickingTaskList);
  useEffect(() => { initLoad(); }, []);

  const taskStatusMap = { 0: '待处理', 1: '进行中', 2: '已完成' };
  const columns = [
    { title: '出库单ID', dataIndex: 'outboundId', key: 'outboundId' },
    { title: '托盘号', dataIndex: 'palletName', key: 'palletName' },
    { title: '源库位ID', dataIndex: 'inventoryBinId', key: 'inventoryBinId' },
    { title: '待拣数量', dataIndex: 'toPickedQty', key: 'toPickedQty' },
    { title: '已拣数量', dataIndex: 'pickedQty', key: 'pickedQty' },
    { title: '状态', dataIndex: 'taskStatus', key: 'taskStatus', render: (v) => <Tag>{taskStatusMap[v] || v}</Tag> },
    {
      title: '操作', key: 'action',
      render: (_, r) => (
        r.taskStatus !== 2 && hasAnyPermission(['outbound:manage', 'outbound:pick']) && (
          <Button type="link" onClick={async () => { await completePickingTask(r.id, 0); message.success('拣货完成'); refresh(); }}>完成拣货</Button>
        )
      ),
    },
  ];

  return (
    <Card title="拣货任务">
      <Table rowKey="id" columns={columns} dataSource={data} loading={loading}
        pagination={{ ...pagination, showSizeChanger: true, showTotal: (t) => `共 ${t} 条`, onChange: handlePageChange }} />
    </Card>
  );
};

export default PickingTaskPage;
