# WMS 仓库管理系统 API 文档

**Base URL**: `http://localhost:8080/api`

**认证**: 登录后 Shiro 维护 Session（Cookie），后续请求自动携带。

---

## 通用说明

### 响应格式

```json
{ "code": 200, "msg": "success", "data": {} }
// code: 200=成功, 401=未登录, 500=错误
```

### 前端调用

```typescript
// Vue3 + axios
const http = axios.create({ baseURL: '/api', withCredentials: true })
http.interceptors.response.use(res => {
  if (res.data.code === 401) router.push('/login')
  return res.data
})

// React + fetch
const res = await fetch('/api/warehouse/list', { credentials: 'include' })
const { code, data } = await res.json()
```

---

## 一、认证

### POST `/login`

```json
// Body: { "username": "admin", "password": "123456" }
// Response data = SysUser 对象
```

**SysUser**: userId, nickName, email, phonenumber, userType, status, roleIds(Long[])

### GET `/logout`

### GET `/auth/me`

返回当前登录用户。首次进入页面时调用，验证登录状态。

### GET `/auth/authority/list`

返回当前用户的权限/菜单列表，用于前端动态路由。

---

## 二、仓库管理

### 仓库 `/warehouse`

| 方法 | URL | 说明 |
|------|-----|------|
| GET | `/list` | 全部 |
| GET | `/{id}` | 详情 |
| POST | — | 新增 |
| PUT | — | 更新 |
| DELETE | `/{id}` | 软删除(isActive=0) |

**字段**: warehouseCode(编码), warehouseName(名称), length, width, address, city, state, postalCode, remark, orderNum

### 库区 `/warehouse/zone`

同上 CRUD。字段: zoneName(必填), warehouseId(必填), layers(层数), length, width, isEnabled(0/1)

### 货架 `/warehouse/rack`

同上 CRUD。字段: rackName(必填), warehouseId, zoneId, length, width, rackType, isDouble(双面0/1), positionInDouble, relatedRack

### 库位 `/warehouse/bin`

| 方法 | URL | 说明 |
|------|-----|------|
| GET | `/bin/list` | 全部 |
| GET | `/bin/{id}` | 详情 |
| POST | `/bin` | 新增 |
| PUT | `/bin` | 更新 |
| DELETE | `/bin/{id}` | 软删除 |
| POST | `/bin/batch` | 批量创建 |

**批量创建** Body:

```json
{ "prefix": "A-01", "startColumn": 1, "endColumn": 10,
  "rackType": "Standard", "binType": "Storage", "zoneId": 1, "warehouseId": 1 }
```

**字段**: binName(必填), warehouseId, zoneId, rackId, columnNum(列号), onZoomLevel(层数), rackType, binType(Storage/Picking/Staging), maximumCapcity, capcityUnit, maximumVolume, volumeUnit, storageRule(SingleSKU/MultiSKU), isEnabled, lowStorageAlertRato

---

## 三、产品管理

### 产品分类 `/prod/category`

标准 CRUD（GET list/GET {id}/POST/PUT/DELETE {id}），下同。
字段: parentId(父级,顶级=0), categoryName(必填), orderNum, status

### 产品SKU `/prod/sku`

| 额外接口 | 说明 |
|----------|------|
| GET `/sku/generate-code` | 自动生成SKU编码 |
| POST `/sku/upload` | SKU图片上传 |

**字段**: skuCode(必填), prodNameChn, prodNameEng, categoryId, imageUrl, isEnabled, saleStorageUnit(销售单位), storageUnit(存储单位), conversionStorageToSaleUnit(换算系数), palletStorageCapacity(每托存量), cost, weight, volume, prodType, storageTypeId, origin, isAllowSell, limitSellQty, isPalletPicking

### 存储类型 `/prod/storageType`

字段: storageType(如"常温""冷藏")

### 标签 `/prod/tag`

字段: tagName。GET `/{id}/products` 查标签下产品。

### 单位 `/prod/unit`

简单 CRUD。

---

## 四、客户管理 `/customer`

标准 CRUD。POST `/customer/list` 支持条件查询。

**字段**: code(编号), name(必填), customerLevel, customerStatus, phone, email, address, city, province, zipcode, businessIntro, isEnabled

---

## 五、入库管理

### 入库单 `/inbound`

| 方法 | URL | 说明 |
|------|-----|------|
| GET | `/list` | 全部 |
| GET | `/search` | 条件查询(warehouseId/inboundStatus/时间范围) |
| GET | `/{id}` | 详情 |
| POST | — | **创建入库单(含明细)** |
| PUT | — | 更新基本信息 |

**创建** Body:

```json
{
  "inbound": { "warehouseId": 1, "supplierId": 1, "estimatedArrivalTime": "2025-06-01 10:00:00", "inboundType": "Purchase" },
  "inboundDetails": [
    { "skuId": 1, "batchNo": "B20250601", "toReceivedQty": 100, "storageUnit": "箱" }
  ]
}
```

**状态值**: 0=已创建, 4=待收货, 1=收货中, 2=已完成

### 入库明细 `/inbound/details`

GET `/inbound/{inboundId}` 查明细（含SKU信息），其余 CRUD 同上。

**字段**: inboundId, skuId, batchNo, productionDate, productStatus, storageUnit, toReceivedQty(应收数), receivedQty(实收数), weightUnit, totalWeight, palletCount

### 收货任务 `/inbound/receiving/task`

| 方法 | URL | 说明 |
|------|-----|------|
| POST | `/generate/{inboundId}` | **自动生成任务(按托盘拆分)** |
| POST | `/save/{inboundId}` | 手动调整任务(替换旧任务) |
| POST | `/complete/{id}` | **执行收货，扣库存** `{ "binId": 1 }` |
| POST | `/cancel/{id}` | **作废任务，回退状态** |
| GET | `/list` | 全部（含关联信息） |
| GET | `/details/{inboundDetailsId}` | 按明细ID查 |

**完整入库流程**:

```typescript
// 1. 创建入库单
await http.post('/inbound', inboundDTO)
// 2. 生成任务
await http.post(`/inbound/receiving/task/generate/${inboundId}`)
// 3. 执行收货(选库位)
await http.post(`/inbound/receiving/task/complete/${taskId}`, { binId: 1 })
// 库存自动更新（批次→库位→汇总→流水）
```

---

## 六、出库管理

### 出库单 `/outbound`

| 方法 | URL | 说明 |
|------|-----|------|
| GET | `/list` | 条件查询 |
| GET | `/search` | 条件查询 |
| GET | `/{id}` | 详情 |
| POST | — | **创建(含明细)** |
| PUT | `/dto` | 更新含明细(删旧插新) |
| GET | `/available-sku/list` | 有库存的SKU |

**创建** Body:

```json
{
  "outbound": { "warehouseId": 1, "customerId": 1, "outboundType": "Sale", "outboundDate": "2025-06-01" },
  "outboundDetails": [
    { "skuId": 1, "outQty": 50, "qtyUnit": "箱" }
  ]
}
```

**状态值**: 0=已创建, 1=拣货中, 2=已完成

### 出库明细 `/outbound/details`

GET `/outbound/{outboundId}` 查明细。

**字段**: skuId, outQty(出库数), qtyUnit, outWeight, nonStandardWeight(非标重), weightUnit

### 拣货任务 `/outbound/picking/task`

| 方法 | URL | 说明 |
|------|-----|------|
| POST | `/generate/{outboundId}` | **生成任务** |
| POST | `/save/{outboundId}` | 调整任务 |
| POST | `/complete/{id}` | **执行拣货，扣库存** `{ "binId": 1 }` |
| POST | `/cancel/{id}` | **作废** |
| GET | `/list` | 全部 |
| GET | `/details/{outboundDetailsId}` | 按明细ID查 |

**完整出库流程**:

```typescript
// 1. 创建出库单
await http.post('/outbound', outboundDTO)
// 2. 生成拣货任务
await http.post(`/outbound/picking/task/generate/${outboundId}`)
// 3. 执行拣货(选有库存的库位)
await http.post(`/outbound/picking/task/complete/${taskId}`, { binId: 1 })
// 库存自动扣减
```

---

## 七、库存管理

### 库存汇总 `/inventory/summary`

SKU 维度全局库存。GET `/list` & `/search`(支持 skuCode/prodName/categoryId/minQty)。

**字段**: skuId, skuCode, prodName, totalQty, availableQty, frozenQty, updateTime

### 批次库存 `/inventory/batch`

仓库+SKU+批次号维度。GET `/list` & `/search` & `/sku/{skuId}`。

**字段**: warehouseId, warehouseName, skuCode, batchNo, totalQty, availableQty, frozenQty, fdaHold

### 库位库存 `/inventory/bin`

库位+批次维度，查看每个库位具体存了什么。GET `/list` & `/search` & `/batch/{batchId}` & `/export`。

**字段**: binId, binName, zoneName, batchNo, skuCode, prodName, totalQty, productStatus

### 库存流水 `/inventory/history`

出入库操作记录。GET `/list`(binId/inventoryOpType/relatedOrderId)。

**字段**: binId, beforeQty, afterQty, diffQty(正=入库 负=出库), inventoryOpType(IN/OUT), relatedOrderId, operationDate

---

## 八、系统管理

### 用户 `/sys/user`

| 方法 | URL | 说明 |
|------|-----|------|
| POST | `/list` | 条件查询 |
| POST | — | 新增(含roleIds) |
| PUT | — | 更新 |
| DELETE | `/{id}` | 删除 |
| PUT | `/password` | 改密 `{"userId":1,"password":"xxx"}` |
| GET | `/profile` | 个人资料 |
| PUT | `/profile` | 更新个人资料 |

### 角色 `/sys/role`

标准 CRUD + authorityIds(菜单ID数组)。

### 菜单 `/sys/menu`

POST `/list`(菜单树), GET `/{menuId}`(详情), POST/PUT/DELETE。

**字段**: menuName, parentId, orderNum, path, menuType(M=目录/C=菜单), visible, status, perms(权限标识), icon

### 字典 `/sys/dict`

标准 CRUD。字段: dictLabel, dictValue, dictType

---

## 九、仪表盘 `/dashboard/data`

GET 返回:

```json
{ "warehouseCount": 2, "enabledBinCount": 50, "occupiedBinCount": 12,
  "pendingReceivingTasks": 5, "pendingPickingTasks": 3, "totalInventoryQty": 12000 }
```
