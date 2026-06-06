import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Form, Input, Button, Card, Select, message, Spin, InputNumber } from 'antd';
import { getSkuById, createSku, updateSku } from '../../../api/product';
import { getCategoryList } from '../../../api/product';

const SkuFormPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [categories, setCategories] = useState([]);
  const isEdit = !!id;

  useEffect(() => {
    getCategoryList({}).then(r => setCategories(r?.records || []));
    if (isEdit) {
      setLoading(true);
      getSkuById(id).then(d => form.setFieldsValue(d)).catch(() => message.error('加载失败')).finally(() => setLoading(false));
    }
  }, [id, isEdit, form]);

  // 递归扁平化分类
  const flattenCategories = (list, prefix = '') => {
    let result = [];
    (list || []).forEach(c => {
      result.push({ value: c.id, label: prefix + c.categoryName });
      if (c.children) result = result.concat(flattenCategories(c.children, prefix + '  '));
    });
    return result;
  };

  const onFinish = async (values) => {
    setSubmitting(true);
    try {
      const payload = {
        ...values,
        categoryId: values.categoryId ? Number(values.categoryId) : 0,
        cost: values.cost || 0,
        weight: values.weight || 0,
      };
      if (isEdit) await updateSku({ ...payload, id });
      else await createSku(payload);
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
          <Form.Item name="prodNameEng" label="英文名称"><Input /></Form.Item>
          <Form.Item name="prodNameChn" label="中文名称"><Input /></Form.Item>
          <Form.Item name="categoryId" label="产品分类">
            <Select placeholder="选择分类" allowClear showSearch optionFilterProp="label"
              options={flattenCategories(categories)} />
          </Form.Item>
          <Form.Item name="cost" label="成本"><InputNumber min={0} step={0.01} style={{ width: '100%' }} /></Form.Item>
          <Form.Item name="weight" label="重量(kg)"><InputNumber min={0} step={0.01} style={{ width: '100%' }} /></Form.Item>
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
