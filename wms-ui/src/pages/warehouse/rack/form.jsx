import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Form, Input, Button, Card, InputNumber, message, Spin } from 'antd';
import { getRackById, createRack, updateRack } from '../../../api/warehouse-rack';

const RackFormPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const isEdit = !!id;

  useEffect(() => {
    if (isEdit) {
      setLoading(true);
      getRackById(id).then((d) => form.setFieldsValue(d)).catch(() => message.error('加载失败')).finally(() => setLoading(false));
    }
  }, [id, isEdit, form]);

  const onFinish = async (values) => {
    setSubmitting(true);
    try {
      if (isEdit) await updateRack({ ...values, id });
      else await createRack(values);
      message.success(isEdit ? '更新成功' : '创建成功');
      navigate('/warehouse/rack');
    } catch {} finally { setSubmitting(false); }
  };

  return (
    <Spin spinning={loading}>
      <Card title={isEdit ? '编辑货架' : '新增货架'}>
        <Form form={form} layout="vertical" onFinish={onFinish} style={{ maxWidth: 600 }}>
          <Form.Item name="rackName" label="货架名称" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="warehouseId" label="仓库ID" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="zoneId" label="库区ID" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="rackType" label="货架类型"><Input /></Form.Item>
          <Form.Item name="length" label="长度"><InputNumber min={0} style={{ width: '100%' }} /></Form.Item>
          <Form.Item name="width" label="宽度"><InputNumber min={0} style={{ width: '100%' }} /></Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" loading={submitting} style={{ marginRight: 12 }}>{isEdit ? '保存' : '创建'}</Button>
            <Button onClick={() => navigate('/warehouse/rack')}>取消</Button>
          </Form.Item>
        </Form>
      </Card>
    </Spin>
  );
};

export default RackFormPage;
