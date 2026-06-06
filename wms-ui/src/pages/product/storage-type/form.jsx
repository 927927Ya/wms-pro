import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Form, Input, Button, Card, message, Spin } from 'antd';
import { getStorageTypeById, createStorageType, updateStorageType } from '../../../api/product';

const StorageTypeFormPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const isEdit = !!id;

  useEffect(() => {
    if (isEdit) {
      setLoading(true);
      getStorageTypeById(id).then((d) => form.setFieldsValue(d)).catch(() => message.error('加载失败')).finally(() => setLoading(false));
    }
  }, [id, isEdit, form]);

  const onFinish = async (values) => {
    setSubmitting(true);
    try {
      if (isEdit) await updateStorageType({ ...values, id });
      else await createStorageType(values);
      message.success(isEdit ? '更新成功' : '创建成功');
      navigate('/product/storage-type');
    } catch {} finally { setSubmitting(false); }
  };

  return (
    <Spin spinning={loading}>
      <Card title={isEdit ? '编辑存放类型' : '新增存放类型'}>
        <Form form={form} layout="vertical" onFinish={onFinish} style={{ maxWidth: 600 }}>
          <Form.Item name="storageTypeName" label="存放类型名称" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="remark" label="备注"><Input.TextArea rows={2} /></Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" loading={submitting} style={{ marginRight: 12 }}>{isEdit ? '保存' : '创建'}</Button>
            <Button onClick={() => navigate('/product/storage-type')}>取消</Button>
          </Form.Item>
        </Form>
      </Card>
    </Spin>
  );
};

export default StorageTypeFormPage;
