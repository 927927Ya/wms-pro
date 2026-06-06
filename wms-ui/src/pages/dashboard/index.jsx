import { useState, useEffect } from 'react';
import { Row, Col, Card, Statistic, Spin } from 'antd';
import {
  ShopOutlined,
  InboxOutlined,
  ExportOutlined,
  DatabaseOutlined,
  AppstoreOutlined,
  ToolOutlined,
  CarryOutOutlined,
} from '@ant-design/icons';
import { getDashboardData } from '../../api/dashboard';

const DashboardPage = () => {
  const [data, setData] = useState({});
  const [loading, setLoading] = useState(true);

  useEffect(() => { loadData(); }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const res = await getDashboardData();
      setData(res || {});
    } catch { /* ignore */ }
    finally { setLoading(false); }
  };

  const cards = [
    { title: '库存总量', value: data.totalInventoryQty, icon: <DatabaseOutlined />, color: '#1677ff', suffix: '' },
    { title: '待收货任务', value: data.pendingReceivingTaskCount, icon: <InboxOutlined />, color: '#52c41a', suffix: '个' },
    { title: '待拣货任务', value: data.pendingPickingTaskCount, icon: <ExportOutlined />, color: '#faad14', suffix: '个' },
    { title: '入库单(处理中)', value: data.pendingInboundCount, icon: <CarryOutOutlined />, color: '#722ed1', suffix: '单' },
    { title: '出库单(处理中)', value: data.pendingOutboundCount, icon: <ToolOutlined />, color: '#eb2f96', suffix: '单' },
    { title: '产品SKU数', value: data.productCount, icon: <AppstoreOutlined />, color: '#13c2c2', suffix: '个' },
    { title: '库位占用', value: `${data.occupiedBins || 0}/${data.totalBins || 0}`, icon: <ShopOutlined />, color: '#ff7a45', suffix: '' },
  ];

  return (
    <Spin spinning={loading}>
      <h2 style={{ marginBottom: 24 }}>仪表盘</h2>
      <Row gutter={[16, 16]}>
        {cards.map((card, index) => (
          <Col xs={24} sm={12} lg={8} xl={6} key={index}>
            <Card hoverable>
              <Statistic
                title={card.title}
                value={card.value}
                suffix={card.suffix}
                prefix={
                  <span style={{ color: card.color, fontSize: 24, marginRight: 8 }}>
                    {card.icon}
                  </span>
                }
              />
            </Card>
          </Col>
        ))}
      </Row>
    </Spin>
  );
};

export default DashboardPage;
