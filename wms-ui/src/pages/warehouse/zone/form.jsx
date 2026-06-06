import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Form, Input, Button, Card, InputNumber, message, Spin } from 'antd';
import { getZoneById, createZone, updateZone } from '../../../api/warehouse-zone';

const ZoneFormPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const isEdit = !!id;

  useEffect(() => {
    if (isEdit) {
      setLoading(true);
      getZoneById(id).then((d) => form.setFieldsValue(d)).catch(() => message.error('加载失败')).finally(() => setLoading(false));
    }
  }, [id, isEdit, form]);

  const onFinish = async (values) => {
    setSubmitting(true);
    try {
      if (isEdit) await updateZone({ ...values, id });
      else await createZone(values);
      message.success(isEdit ? '更新成功' : '创建成功');
      navigate('/warehouse/zone');
    } catch {} finally { setSubmitting(false); }
  };

  return (
    <Spin spinning={loading}>
      <Card title={isEdit ? '编辑库区' : '新增库区'}>
        <Form form={form} layout="vertical" onFinish={onFinish} style={{ maxWidth: 600 }}>
          <Form.Item name="zoneName" label="库区名称" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="warehouseId" label="仓库ID" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="layers" label="层数"><InputNumber min={1} style={{ width: '100%' }} /></Form.Item>
          <Form.Item name="length" label="长度"><InputNumber min={0} style={{ width: '100%' }} /></Form.Item>
          <Form.Item name="width" label="宽度"><InputNumber min={0} style={{ width: '100%' }} /></Form.Item>
          <Form.Item name="remark" label="备注"><Input.TextArea rows={2} /></Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" loading={submitting} style={{ marginRight: 12 }}>{isEdit ? '保存' : '创建'}</Button>
            <Button onClick={() => navigate('/warehouse/zone')}>取消</Button>
          </Form.Item>
        </Form>
      </Card>
    </Spin>
  );
};

export default ZoneFormPage;
