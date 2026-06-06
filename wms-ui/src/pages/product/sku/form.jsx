import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Form, Input, Button, Card, message, Spin } from 'antd';
import { getSkuById, createSku, updateSku } from '../../../api/product';

const SkuFormPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const isEdit = !!id;

  useEffect(() => {
    if (isEdit) {
      setLoading(true);
      getSkuById(id).then((d) => form.setFieldsValue(d)).catch(() => message.error('加载失败')).finally(() => setLoading(false));
    }
  }, [id, isEdit, form]);

  const onFinish = async (values) => {
    setSubmitting(true);
    try {
      if (isEdit) await updateSku({ ...values, id });
      else await createSku(values);
      message.success(isEdit ? '更新成功' : '创建成功');
      navigate('/product/sku');
    } catch {} finally { setSubmitting(false); }
  };

  return (
    <Spin spinning={loading}>
      <Card title={isEdit ? '编辑产品' : '新增产品'}>
        <Form form={form} layout="vertical" onFinish={onFinish} style={{ maxWidth: 600 }}>
          <Form.Item name="skuCode" label="SKU编码" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="skuName" label="产品名称" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="spec" label="规格"><Input /></Form.Item>
          <Form.Item name="unit" label="单位"><Input /></Form.Item>
          <Form.Item name="categoryId" label="分类ID"><Input /></Form.Item>
          <Form.Item name="remark" label="备注"><Input.TextArea rows={2} /></Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" loading={submitting} style={{ marginRight: 12 }}>{isEdit ? '保存' : '创建'}</Button>
            <Button onClick={() => navigate('/product/sku')}>取消</Button>
          </Form.Item>
        </Form>
      </Card>
    </Spin>
  );
};

export default SkuFormPage;
