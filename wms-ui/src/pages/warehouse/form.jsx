import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Form, Input, Button, Card, InputNumber, Switch, message, Spin } from 'antd';
import { getWarehouseById, createWarehouse, updateWarehouse } from '../../api/warehouse';

const WarehouseFormPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const isEdit = !!id;

  useEffect(() => {
    if (isEdit) {
      setLoading(true);
      getWarehouseById(id).then((d) => {
        form.setFieldsValue({ ...d, isActive: d.isActive === '1' || d.isActive === 1 || d.isActive === true });
      }).catch(() => message.error('加载失败')).finally(() => setLoading(false));
    }
  }, [id, isEdit, form]);

  const onFinish = async (values) => {
    setSubmitting(true);
    try {
      const payload = { ...values, isActive: values.isActive ? '1' : '0' };
      if (isEdit) await updateWarehouse({ ...payload, id });
      else await createWarehouse(payload);
      message.success(isEdit ? '更新成功' : '创建成功');
      navigate('/warehouse');
    } catch {} finally { setSubmitting(false); }
  };

  return (
    <Spin spinning={loading}>
      <Card title={isEdit ? '编辑仓库' : '新增仓库'}>
        <Form form={form} layout="vertical" onFinish={onFinish} style={{ maxWidth: 600 }}>
          <Form.Item name="warehouseCode" label="仓库编码" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="warehouseName" label="仓库名称" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="address" label="地址"><Input.TextArea rows={2} /></Form.Item>
          <Form.Item name="city" label="城市"><Input /></Form.Item>
          <Form.Item name="state" label="省/州"><Input /></Form.Item>
          <Form.Item name="postalCode" label="邮编"><Input /></Form.Item>
          <Form.Item name="length" label="长度"><InputNumber min={0} style={{ width: '100%' }} /></Form.Item>
          <Form.Item name="width" label="宽度"><InputNumber min={0} style={{ width: '100%' }} /></Form.Item>
          <Form.Item name="isActive" label="是否启用" valuePropName="checked"><Switch /></Form.Item>
          <Form.Item name="remark" label="备注"><Input.TextArea rows={3} /></Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" loading={submitting} style={{ marginRight: 12 }}>{isEdit ? '保存' : '创建'}</Button>
            <Button onClick={() => navigate('/warehouse')}>取消</Button>
          </Form.Item>
        </Form>
      </Card>
    </Spin>
  );
};

export default WarehouseFormPage;
