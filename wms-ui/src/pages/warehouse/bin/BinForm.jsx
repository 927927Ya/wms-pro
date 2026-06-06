import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Form, Input, Button, Card, InputNumber, Select, message, Spin } from 'antd';
import { getBinById, createBin, updateBin } from '../../../api/warehouse-bin';

const BinFormPage = ({ mode = 'edit' }) => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const isEdit = mode === 'edit' && !!id;
  const title = isEdit ? '编辑库位' : '新增库位';

  useEffect(() => {
    if (isEdit) {
      setLoading(true);
      getBinById(id).then((d) => form.setFieldsValue(d)).catch(() => message.error('加载失败')).finally(() => setLoading(false));
    }
  }, [id, isEdit, form]);

  const onFinish = async (values) => {
    setSubmitting(true);
    try {
      if (isEdit) await updateBin({ ...values, id });
      else await createBin(values);
      message.success(isEdit ? '更新成功' : '创建成功');
      navigate('/warehouse/bin');
    } catch {} finally { setSubmitting(false); }
  };

  return (
    <Spin spinning={loading}>
      <Card title={title}>
        <Form form={form} layout="vertical" onFinish={onFinish} style={{ maxWidth: 600 }}>
          <Form.Item name="binName" label="库位名称" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="warehouseId" label="仓库ID" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="zoneId" label="库区ID" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="rackId" label="货架ID"><Input /></Form.Item>
          <Form.Item name="columnNum" label="列数"><InputNumber min={1} style={{ width: '100%' }} /></Form.Item>
          <Form.Item name="onZoomLevel" label="层数"><InputNumber min={1} style={{ width: '100%' }} /></Form.Item>
          <Form.Item name="binType" label="库位类型"><Input /></Form.Item>
          <Form.Item name="maximumCapcity" label="最大容量"><InputNumber min={0} style={{ width: '100%' }} /></Form.Item>
          <Form.Item name="capcityUnit" label="容量单位"><Input /></Form.Item>
          <Form.Item name="isEnabled" label="是否启用"><Select options={[{ value: 1, label: '启用' }, { value: 0, label: '停用' }]} /></Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" loading={submitting} style={{ marginRight: 12 }}>{isEdit ? '保存' : '创建'}</Button>
            <Button onClick={() => navigate('/warehouse/bin')}>取消</Button>
          </Form.Item>
        </Form>
      </Card>
    </Spin>
  );
};

const BinCreatePage = () => <BinFormPage mode="create" />;
const BinEditPage = () => <BinFormPage mode="edit" />;

export { BinCreatePage, BinEditPage };
