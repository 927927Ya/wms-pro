/*
 Navicat MySQL Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : localhost:3306
 Source Schema         : wms-test

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 28/12/2025 20:08:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
SET REFERENTIAL_INTEGRITY FALSE;

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '客户表',
  `code` varchar(20) DEFAULT NULL COMMENT '客户编号',
  `name` varchar(255) DEFAULT NULL COMMENT '客户名',
  `customer_level` varchar(10) DEFAULT NULL COMMENT '客户级别,参考sys_dict中的customer_level属性值',
  `customer_status` varchar(10) DEFAULT NULL COMMENT '客户状态，参考sys_dict中的customer_status属性值',
  `phone` varchar(20) DEFAULT NULL COMMENT '电话号码',
  `email` varchar(255) DEFAULT NULL COMMENT 'E-Mail',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `city` varchar(255) DEFAULT NULL COMMENT '城市',
  `province` varchar(255) DEFAULT NULL COMMENT '省份',
  `zipcode` varchar(255) DEFAULT NULL COMMENT '邮编',
  `business_intro` varchar(255) DEFAULT NULL COMMENT '主营业务',
  `is_enabled` tinyint NULL DEFAULT 1 COMMENT '激活状态：激活（1）,否则（0）;缺省为1',
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Records of customer
-- ----------------------------
INSERT INTO `customer` VALUES (1, 'CUS001', 'Ocean Kingdom Supplier', '1', '1', '13800138001', 'supplier@ocean.com', 'Philly', 'Philly', 'Pennsylvania', '19100', 'Seafood Supply', 1);
INSERT INTO `customer` VALUES (2, 'CUS002', 'Seafood Retailer', '2', '1', '13900139002', 'retailer@seafood.com', 'New York', 'New York', 'New York', '10001', 'Seafood Retail', 1);

-- ----------------------------
-- Table structure for inbound
-- ----------------------------
DROP TABLE IF EXISTS `inbound`;
CREATE TABLE `inbound`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `supplier_id` bigint NOT NULL COMMENT '供应商id，为了简单起见，关联customer表',
  `warehouse_id` bigint NULL DEFAULT NULL COMMENT '仓库id',
  `estimated_arrival_time` datetime NULL DEFAULT NULL COMMENT '预计到达时间',
  `inbound_status` varchar(10) DEFAULT NULL COMMENT '入库状态：待创建，进行中，已完成，已作废，参考字典表',
  `inbound_type` varchar(10) DEFAULT NULL COMMENT '入库类型：采购入库、退货入库等（注意：期末项目不用考虑）。参考字典表',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建者id',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改者id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改 时间',
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Records of inbound
-- ----------------------------
INSERT INTO `inbound` VALUES (1001, 1, 1828364740028174337, '2025-12-12 10:00:00.000', '2', '0', '2025-12-10 09:00:00.000', 1, 1, '2025-12-13 15:00:00.000');
INSERT INTO `inbound` VALUES (1002, 1, 1828364740028174337, '2024-12-25 14:00:00.000', '2', '0', '2024-12-23 10:00:00.000', 1, 1, '2024-12-26 16:00:00.000');
INSERT INTO `inbound` VALUES (1003, 1, 1828364740028174337, '2025-01-20 09:00:00.000', '2', '0', '2025-01-18 08:00:00.000', 1, 1, '2025-01-21 14:00:00.000');
INSERT INTO `inbound` VALUES (1004, 1, 1828364740028174337, '2025-01-07 11:00:00.000', '2', '0', '2025-01-05 09:00:00.000', 1, 1, '2025-01-08 15:00:00.000');

-- ----------------------------
-- Table structure for inbound_details
-- ----------------------------
DROP TABLE IF EXISTS `inbound_details`;
CREATE TABLE `inbound_details`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '注意，该表存储入库时sku对应的入库信息，包括批号，数量，生产日期等',
  `inbound_id` bigint NOT NULL COMMENT '入库单id',
  `sku_id` bigint NULL DEFAULT NULL COMMENT '产品sku id',
  `batch_no` varchar(64) DEFAULT NULL COMMENT '产品批号no',
  `production_date` date NULL DEFAULT NULL COMMENT '生产日期(年月日)',
  `product_status` varchar(10) DEFAULT NULL COMMENT '产品状态，参考sys_dict',
  `storage_unit` varchar(10) DEFAULT NULL COMMENT '产品单位，参考sys_dict',
  `to_received_qty` decimal(20, 2) NULL DEFAULT NULL COMMENT '应收数量',
  `received_qty` decimal(20, 2) NULL DEFAULT NULL COMMENT '实收数量',
  `weight_unit` varchar(10) DEFAULT NULL COMMENT '重量单位',
  `total_weight` decimal(20, 2) NULL DEFAULT NULL COMMENT '总重量',
  `pallet_count` int NULL DEFAULT NULL COMMENT '所需托盘数量',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  INDEX `idx_inbound_id`(`inbound_id`)
);

-- ----------------------------
-- Records of inbound_details
-- ----------------------------
INSERT INTO `inbound_details` VALUES (2001, 1001, 1, '20251204212807-1', '2025-08-29', '0', '1', 152.00, 152.00, '1', 304.00, 4, NULL);
INSERT INTO `inbound_details` VALUES (2002, 1002, 2, '20241226144529', '2024-09-11', '0', '1', 477.00, 477.00, '1', 954.00, 12, NULL);
INSERT INTO `inbound_details` VALUES (2003, 1003, 3, '20250121175344', '2024-11-29', '0', '1', 63.00, 63.00, '1', 126.00, 2, NULL);
INSERT INTO `inbound_details` VALUES (2004, 1004, 4, '20250108112608', '2024-11-13', '0', '1', 3346.00, 3346.00, '1', 6692.00, 84, NULL);
INSERT INTO `inbound_details` VALUES (2005, 1003, 5, '20251113231442-1', '2025-09-22', '0', '1', 2423.00, 2423.00, '1', 4846.00, 61, NULL);

-- ----------------------------
-- Table structure for inbound_receiving_task
-- ----------------------------
DROP TABLE IF EXISTS `inbound_receiving_task`;
CREATE TABLE `inbound_receiving_task`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '注意：一条入库详情对应一个sku商品，一个SKU商品可以对应多个收货任务，即多个托盘来收货。',
  `inbound_details_id` bigint NOT NULL COMMENT '入库单详情id',
  `pallet_name` varchar(64) DEFAULT NULL COMMENT '托盘名,例如 “PLT#1\"',
  `to_received_qty` decimal(10, 2) NULL DEFAULT NULL COMMENT '应收数量',
  `received_qty` decimal(10, 2) NULL DEFAULT NULL COMMENT '实收数量',
  `task_status` tinyint NULL DEFAULT NULL COMMENT '任务完成状态',
  `bin_id` bigint NULL DEFAULT NULL COMMENT '入库库位Id，外键关联warehouse_bin表',
  `operator_id` bigint NULL DEFAULT NULL COMMENT '操作人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `operation_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  INDEX `idx_inbound_details_id`(`inbound_details_id`)
);

-- ----------------------------
-- Records of inbound_receiving_task
-- ----------------------------
INSERT INTO `inbound_receiving_task` VALUES (3001, 2001, 'PLT#1', 56.00, 56.00, 1, 1, 1, '2025-12-13 10:30:00.000', '2025-12-13 10:30:00.000', 'C2-7-4库位收货');
INSERT INTO `inbound_receiving_task` VALUES (3002, 2001, 'PLT#2', 56.00, 56.00, 1, 2, 1, '2025-12-13 10:40:00.000', '2025-12-13 10:40:00.000', 'C2-7-5库位收货');
INSERT INTO `inbound_receiving_task` VALUES (3003, 2001, 'PLT#3', 40.00, 40.00, 1, 3, 1, '2025-12-13 10:50:00.000', '2025-12-13 10:50:00.000', 'C15-14-1库位收货');
INSERT INTO `inbound_receiving_task` VALUES (3004, 2002, 'PLT#4', 37.00, 37.00, 1, 4, 1, '2024-12-26 15:30:00.000', '2024-12-26 15:30:00.000', 'C15-26-1库位收货');
INSERT INTO `inbound_receiving_task` VALUES (3005, 2002, 'PLT#5', 110.00, 110.00, 1, 5, 1, '2024-12-26 15:40:00.000', '2024-12-26 15:40:00.000', 'C19-6-6库位收货');

-- ----------------------------
-- Table structure for inventory_batch
-- ----------------------------
DROP TABLE IF EXISTS `inventory_batch`;
CREATE TABLE `inventory_batch`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'sku, batch_no, in warehouse: 描述在各仓库中，sku在各batch_no的存储情况',
  `warehouse_id` bigint NOT NULL COMMENT '仓库id',
  `sku_id` bigint NOT NULL COMMENT 'SKU id',
  `batch_no` varchar(64) NOT NULL COMMENT '入库的批号',
  `total_qty` decimal(20, 2) NULL DEFAULT NULL COMMENT '本批次的总数量',
  `available_qty` decimal(20, 2) NULL DEFAULT NULL COMMENT '本批次的可用数量',
  `frozen_qty` decimal(20, 2) NULL DEFAULT NULL COMMENT '本批次的冻结数量',
  `fda_hold` tinyint NULL DEFAULT 0 COMMENT 'fda hold 是否被海关扣压: 默认0；否则1',
  PRIMARY KEY (`id`),
  INDEX `idx_sku_batch`(`sku_id`, `batch_no`)
);

-- ----------------------------
-- Records of inventory_batch
-- ----------------------------
INSERT INTO `inventory_batch` VALUES (1, 1828364740028174337, 1, '20251204212807-1', 152.00, 116.00, 36.00, 0);
INSERT INTO `inventory_batch` VALUES (2, 1828364740028174337, 2, '20241226144529', 477.00, 472.00, 5.00, 0);
INSERT INTO `inventory_batch` VALUES (3, 1828364740028174337, 3, '20250121175344', 63.00, 59.00, 4.00, 0);
INSERT INTO `inventory_batch` VALUES (4, 1828364740028174337, 4, '20250108112608', 3346.00, 3343.00, 3.00, 0);
INSERT INTO `inventory_batch` VALUES (5, 1828364740028174337, 5, '20251113231442-1', 2423.00, 2407.00, 16.00, 0);

-- ----------------------------
-- Table structure for inventory_bin
-- ----------------------------
DROP TABLE IF EXISTS `inventory_bin`;
CREATE TABLE `inventory_bin`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '此表为 库位与sku，batch_no之间的关联表: 描述该库位上，该批次的sku对应的库存情况',
  `bin_id` varchar(64) NOT NULL COMMENT '库位id, 库位表外键，关联warehouse_bin表',
  `batch_id` bigint NOT NULL COMMENT '批次id, 关联inventory_batch表',
  `product_status` varchar(10) DEFAULT NULL COMMENT '产品状态：合格或不合格，具体值参考sys_dict',
  `total_qty` decimal(20, 2) NULL DEFAULT NULL COMMENT '库位中该批次产品存储的总数',
  PRIMARY KEY (`id`),
  INDEX `idx_batch_id`(`batch_id`)
);

-- ----------------------------
-- Records of inventory_bin
-- ----------------------------
INSERT INTO `inventory_bin` VALUES (1, 'C2-7-4', 1, '0', 56.00);
INSERT INTO `inventory_bin` VALUES (2, 'C2-7-5', 1, '0', 56.00);
INSERT INTO `inventory_bin` VALUES (3, 'C15-14-1', 1, '0', 40.00);
INSERT INTO `inventory_bin` VALUES (4, 'C15-26-1', 2, '0', 37.00);
INSERT INTO `inventory_bin` VALUES (5, 'C19-6-6', 2, '0', 110.00);
INSERT INTO `inventory_bin` VALUES (6, 'C19-7-5', 2, '0', 110.00);
INSERT INTO `inventory_bin` VALUES (7, 'C20-6-6', 2, '0', 110.00);
INSERT INTO `inventory_bin` VALUES (8, 'C20-7-5', 2, '0', 110.00);
INSERT INTO `inventory_bin` VALUES (9, 'C18-39-1', 3, '0', 13.00);
INSERT INTO `inventory_bin` VALUES (10, 'D-21B-1', 3, '0', 50.00);

-- ----------------------------
-- Table structure for inventory_history
-- ----------------------------
DROP TABLE IF EXISTS `inventory_history`;
CREATE TABLE `inventory_history`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录库存库位的变化流水',
  `bin_id` bigint NULL DEFAULT NULL COMMENT '库存库位id, 外键关联inventory_bin表',
  `before_qty` decimal(10, 2) NULL DEFAULT NULL COMMENT '变更前数量',
  `after_qty` decimal(10, 2) NULL DEFAULT NULL COMMENT '变更后数量',
  `diff_qty` decimal(10, 2) NULL DEFAULT NULL COMMENT '变更数量',
  `inventory_op_type` varchar(10) DEFAULT NULL COMMENT '库存操作类型, 参考sys_dict表',
  `related_order_id` bigint NULL DEFAULT NULL COMMENT '关联单据id，例如出库单或入库单',
  `operation_date` datetime NULL DEFAULT NULL COMMENT '操作时间',
  `operator_id` bigint NULL DEFAULT NULL COMMENT '操作人员id',
  PRIMARY KEY (`id`),
  INDEX `idx_bin_id`(`bin_id`)
);

-- ----------------------------
-- Records of inventory_history
-- ----------------------------
INSERT INTO `inventory_history` VALUES (1843920324157591555, 1, 0.00, 56.00, 56.00, 'IN', 1001, '2025-12-13 10:30:00.000', 1);
INSERT INTO `inventory_history` VALUES (1843920324157591556, 2, 0.00, 56.00, 56.00, 'IN', 1001, '2025-12-13 10:40:00.000', 1);
INSERT INTO `inventory_history` VALUES (1843920324157591557, 3, 0.00, 40.00, 40.00, 'IN', 1001, '2025-12-13 10:50:00.000', 1);
INSERT INTO `inventory_history` VALUES (1843920324157591558, 4, 0.00, 37.00, 37.00, 'IN', 1002, '2024-12-26 15:30:00.000', 1);
INSERT INTO `inventory_history` VALUES (1843920324157591559, 5, 0.00, 110.00, 110.00, 'IN', 1002, '2024-12-26 15:40:00.000', 1);
INSERT INTO `inventory_history` VALUES (1843920324157591560, 3, 40.00, 4.00, -36.00, 'OUT', 4001, '2025-12-16 10:00:00.000', 1);
INSERT INTO `inventory_history` VALUES (1843920324157591561, 1, 56.00, 40.00, -16.00, 'OUT', 4001, '2025-12-16 10:10:00.000', 1);
INSERT INTO `inventory_history` VALUES (1843920324157591562, 9, 13.00, 9.00, -4.00, 'OUT', 4002, '2025-01-23 11:00:00.000', 1);

-- ----------------------------
-- Table structure for inventory_summary
-- ----------------------------
DROP TABLE IF EXISTS `inventory_summary`;
CREATE TABLE `inventory_summary`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `sku_id` bigint NULL DEFAULT NULL COMMENT '规格ID',
  `total_qty` decimal(20, 2) NULL DEFAULT NULL COMMENT '库存总数量',
  `available_qty` decimal(20, 2) NULL DEFAULT NULL COMMENT '库存可用总数',
  `frozen_qty` decimal(20, 2) NULL DEFAULT NULL COMMENT '库存冻结总数',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_sku_id`(`sku_id`)
);

-- ----------------------------
-- Records of inventory_summary
-- ----------------------------
INSERT INTO `inventory_summary` VALUES (1843920324082094083, 1, 152.00, 116.00, 36.00, '2025-12-16 10:10:00.000');
INSERT INTO `inventory_summary` VALUES (1843920324082094084, 2, 477.00, 472.00, 5.00, '2024-12-26 16:00:00.000');
INSERT INTO `inventory_summary` VALUES (1843920324082094085, 3, 63.00, 59.00, 4.00, '2025-01-23 11:00:00.000');
INSERT INTO `inventory_summary` VALUES (1843920324082094086, 4, 3346.00, 3343.00, 3.00, '2025-01-08 15:00:00.000');
INSERT INTO `inventory_summary` VALUES (1843920324082094087, 5, 2423.00, 2407.00, 16.00, '2025-12-16 10:10:00.000');

-- ----------------------------
-- Table structure for outbound
-- ----------------------------
DROP TABLE IF EXISTS `outbound`;
CREATE TABLE `outbound`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '出库单表',
  `warehouse_id` bigint NOT NULL COMMENT '出库仓库',
  `outbound_type` varchar(10) DEFAULT NULL COMMENT '出库类型，参考sys_dict表',
  `outbound_status` varchar(10) DEFAULT NULL COMMENT '出库状态，参考sys_dict表',
  `customer_id` bigint NULL DEFAULT NULL COMMENT '客户，关联customer表',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建者id,外键关联sys_user',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `outbound_date` date NULL DEFAULT NULL COMMENT '出货日期',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  INDEX `idx_customer_id`(`customer_id`)
);

-- ----------------------------
-- Records of outbound
-- ----------------------------
INSERT INTO `outbound` VALUES (4001, 1828364740028174337, '2', '2', 2, 1, '2025-12-15 09:00:00.000', '2025-12-16', '海鲜零售订单出库');
INSERT INTO `outbound` VALUES (4002, 1828364740028174337, '2', '2', 2, 1, '2025-01-22 10:00:00.000', '2025-01-23', '罗非鱼订单出库');

-- ----------------------------
-- Table structure for outbound_details
-- ----------------------------
DROP TABLE IF EXISTS `outbound_details`;
CREATE TABLE `outbound_details`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '出库单详情',
  `outbound_id` bigint NOT NULL COMMENT '出库单id, 关联outbound表',
  `sku_id` bigint NOT NULL COMMENT '出库产品sku id,关联prod_sku表',
  `out_qty` decimal(10, 2) NULL DEFAULT NULL COMMENT '出库数量',
  `qty_unit` varchar(10) DEFAULT NULL COMMENT '出库数量单位，参考sys_dict中的storage_unit属性值',
  `out_weight` decimal(10, 2) NULL DEFAULT NULL COMMENT '出库重量',
  `non_standard_weight` decimal(10, 2) NULL DEFAULT NULL COMMENT '非标品重量，针对非整箱出库',
  `weight_unit` varchar(10) DEFAULT NULL COMMENT '出库重量单位，参考sys_dict中的weight_unit属性值',
  PRIMARY KEY (`id`),
  INDEX `idx_outbound_id`(`outbound_id`)
);

-- ----------------------------
-- Records of outbound_details
-- ----------------------------
INSERT INTO `outbound_details` VALUES (5001, 4001, 1, 36.00, '1', 72.00, 0.00, '1');
INSERT INTO `outbound_details` VALUES (5002, 4001, 5, 16.00, '1', 32.00, 0.00, '1');
INSERT INTO `outbound_details` VALUES (5003, 4002, 3, 4.00, '1', 8.00, 0.00, '1');

-- ----------------------------
-- Table structure for outbound_picking_task
-- ----------------------------
DROP TABLE IF EXISTS `outbound_picking_task`;
CREATE TABLE `outbound_picking_task`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '拣货任务表，实现出库，并详细记录从哪个库位出了哪个sku的哪个批次的货。',
  `outbound_details_id` bigint NULL DEFAULT NULL COMMENT '出库详情id，对应一个sku',
  `inventory_bin_id` bigint NULL DEFAULT NULL COMMENT '出库库存库位id, 对应着可以出库的sku',
  `pallet_name` varchar(64) DEFAULT NULL COMMENT '托盘名字，如果需要托盘，则需要设置托盘名字',
  `task_status` tinyint NULL DEFAULT NULL COMMENT '任务状态',
  `to_picked_qty` decimal(10, 2) NULL DEFAULT NULL COMMENT '计划出库数量',
  `picked_qty` decimal(10, 2) NULL DEFAULT NULL COMMENT '实际出库数量',
  `qty_unit` varchar(10) DEFAULT NULL COMMENT '出库单位，参考sys_dict中的storage_type',
  `operation_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  `operator_id` bigint NULL DEFAULT NULL COMMENT '操作员id',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注，如果作废，可以备注失败原因。',
  PRIMARY KEY (`id`),
  INDEX `idx_outbound_details_id`(`outbound_details_id`)
);

-- ----------------------------
-- Records of outbound_picking_task
-- ----------------------------
INSERT INTO `outbound_picking_task` VALUES (6001, 5001, 3, 'PLT#10', 1, 36.00, 36.00, '1', '2025-12-16 10:00:00.000', 1, 'C15-14-1库位拣货');
INSERT INTO `outbound_picking_task` VALUES (6002, 5002, 1, 'PLT#11', 1, 16.00, 16.00, '1', '2025-12-16 10:10:00.000', 1, 'C2-7-4库位拣货');
INSERT INTO `outbound_picking_task` VALUES (6003, 5003, 9, 'PLT#12', 1, 4.00, 4.00, '1', '2025-01-23 11:00:00.000', 1, 'C18-39-1库位拣货');

-- ----------------------------
-- Table structure for prod_category
-- ----------------------------
DROP TABLE IF EXISTS `prod_category`;
CREATE TABLE `prod_category`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '类别id',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父类型id',
  `category_name` varchar(30) DEFAULT '' COMMENT '类型名称',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `status` char(1) DEFAULT '1' COMMENT '类型状态（0停用 1正常）',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_parent_id`(`parent_id`)
);

-- ----------------------------
-- Records of prod_category
-- ----------------------------
INSERT INTO `prod_category` VALUES (1, 0, '急冻海鲜/急冻寿司', 1, '1', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00');
INSERT INTO `prod_category` VALUES (2, 1, '贝类和甲壳类', 2, '1', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00');
INSERT INTO `prod_category` VALUES (3, 2, '青口', 3, '1', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00');
INSERT INTO `prod_category` VALUES (4, 1, '养殖白虾', 4, '1', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00');
INSERT INTO `prod_category` VALUES (5, 4, '虾仁去尾', 5, '1', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00');
INSERT INTO `prod_category` VALUES (6, 1, '整只鱼', 6, '1', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00');
INSERT INTO `prod_category` VALUES (7, 6, '罗非鱼', 7, '1', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00');
INSERT INTO `prod_category` VALUES (8, 1, '鱼片', 8, '1', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00');
INSERT INTO `prod_category` VALUES (9, 8, '龙利鱼', 9, '1', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00');

-- ----------------------------
-- Table structure for prod_sku
-- ----------------------------
DROP TABLE IF EXISTS `prod_sku`;
CREATE TABLE `prod_sku`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增长',
  `sku_code` varchar(64) NOT NULL,
  `prod_name_eng` varchar(64) NOT NULL COMMENT '产品名（英文)',
  `prod_name_chn` varchar(64) NOT NULL COMMENT '产品名（中文)',
  `category_id` bigint NULL DEFAULT NULL COMMENT '三级分类id',
  `second_category_id` bigint NULL DEFAULT NULL COMMENT '二级分类id',
  `first_category_id` bigint NULL DEFAULT NULL COMMENT '一级分类id',
  `image_url` varchar(255) DEFAULT NULL COMMENT '产品图片地址',
  `outer_image_url` varchar(255) DEFAULT NULL COMMENT '产品外包装图片地址',
  `inner_image_url` varchar(255) DEFAULT NULL COMMENT '产品内包装图片地址',
  `is_enabled` tinyint NULL DEFAULT NULL COMMENT '是否可用',
  `sale_storage_unit` varchar(10) DEFAULT NULL COMMENT '价格单位，关联sys_dict字典中的storage_unit属性，例如磅，箱',
  `storage_unit` varchar(10) DEFAULT NULL COMMENT '库存单位，关联sys_dict字典中的存储单位',
  `conversion_storage_to_sale_unit` decimal(10, 2) NULL DEFAULT NULL COMMENT '库存单位到价格单位的转换',
  `pallet_storage_capacity` decimal(10, 2) NULL DEFAULT NULL COMMENT '托盘容量，即一个托盘能存放多少库存单位的产品',
  `cost` decimal(10, 2) NULL DEFAULT NULL COMMENT '成本价',
  `weight` decimal(10, 3) NULL DEFAULT NULL COMMENT '重量',
  `volume` decimal(10, 3) NULL DEFAULT NULL COMMENT '体积',
  `prod_type` varchar(10) DEFAULT NULL COMMENT '产品类型，关联sys_dict字典中的product_type',
  `storage_type_id` bigint NULL DEFAULT NULL COMMENT '存放类型id',
  `origin` varchar(255) DEFAULT NULL COMMENT '产地',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  INDEX `idx_category_id`(`category_id`)
);

-- ----------------------------
-- Records of prod_sku
-- ----------------------------
INSERT INTO `prod_sku` VALUES (1, '046201004', 'LHalf Shell Green Mussel NZ', '(大)(Sea Products)青口,12x2lb', 3, 2, 1, 'url1', 'url1-outer', 'url1-inner', 1, '1', '1', 1.00, 40.00, 150.00, 2.000, 0.020, '0', 1, '新西兰', '急冻青口', '2024-01-01 00:00:00', 'admin', 'admin', '2024-01-01 00:00:00');
INSERT INTO `prod_sku` VALUES (2, '010361625', 'India P&D Tail off Shrimp', '61/70(MALUMI)虾仁,5X2LB', 5, 4, 1, 'url2', 'url2-outer', 'url2-inner', 1, '1', '1', 1.00, 40.00, 200.00, 2.000, 0.015, '0', 1, '印度', '急冻虾仁', '2024-01-01 00:00:00', 'admin', 'admin', '2024-01-01 00:00:00');
INSERT INTO `prod_sku` VALUES (3, '028413501', 'Tilapia Gutted and Scaled, NW34lb', '350/550<OK>二去罗非鱼,1X40lb', 7, 6, 1, 'url3', 'url3-outer', 'url3-inner', 1, '1', '1', 1.00, 32.00, 120.00, 2.000, 0.030, '0', 1, '越南', '急冻罗非鱼', '2024-01-01 00:00:00', 'admin', 'admin', '2024-01-01 00:00:00');
INSERT INTO `prod_sku` VALUES (4, '028417501', 'Tilapia Gutted and Scaled, NW 34LB', '750/950<OK>二去罗非鱼,1X40lb', 7, 6, 1, 'url4', 'url4-outer', 'url4-inner', 1, '1', '1', 1.00, 40.00, 130.00, 2.000, 0.035, '0', 1, '越南', '急冻罗非鱼', '2024-01-01 00:00:00', 'admin', 'admin', '2024-01-01 00:00:00');
INSERT INTO `prod_sku` VALUES (5, '038250702', 'IQF Swai Fillets 100%NW', '7/9<100%>(Original Mekong)单冻龙利鱼片,15lb', 9, 8, 1, 'url5', 'url5-outer', 'url5-inner', 1, '1', '1', 1.00, 40.00, 180.00, 2.000, 0.025, '0', 1, '湄公河', '急冻龙利鱼片', '2024-01-01 00:00:00', 'admin', 'admin', '2024-01-01 00:00:00');

-- ----------------------------
-- Table structure for prod_storage_type
-- ----------------------------
DROP TABLE IF EXISTS `prod_storage_type`;
CREATE TABLE `prod_storage_type`  (
  `id` bigint NOT NULL,
  `storage_type` varchar(255) NOT NULL COMMENT '存放类型',
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Records of prod_storage_type
-- ----------------------------
INSERT INTO `prod_storage_type` VALUES (1, '冷链存储');
INSERT INTO `prod_storage_type` VALUES (2, '常温存储');

-- ----------------------------
-- Table structure for prod_tag
-- ----------------------------
DROP TABLE IF EXISTS `prod_tag`;
CREATE TABLE `prod_tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(30) NOT NULL COMMENT '标签名称',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Records of prod_tag
-- ----------------------------
INSERT INTO `prod_tag` VALUES (1828407291103842307, 'Ocean Kingdom', 'admin', '2024-08-27 20:21:00.000', 'admin', '2024-08-27 20:21:00.000');
INSERT INTO `prod_tag` VALUES (1828407291103842308, 'Seafood Fresh', 'admin', '2024-08-27 20:22:00.000', 'admin', '2024-08-27 20:22:00.000');

-- ----------------------------
-- Table structure for prod_tag_sku
-- ----------------------------
DROP TABLE IF EXISTS `prod_tag_sku`;
CREATE TABLE `prod_tag_sku`  (
  `id` bigint NOT NULL,
  `tag_id` bigint NOT NULL COMMENT 'tag_id',
  `sku_id` bigint NULL DEFAULT NULL COMMENT 'sku_id',
  PRIMARY KEY (`id`),
  INDEX `idx_tag_sku`(`tag_id`, `sku_id`)
);

-- ----------------------------
-- Records of prod_tag_sku
-- ----------------------------
INSERT INTO `prod_tag_sku` VALUES (1, 1828407291103842307, 1);
INSERT INTO `prod_tag_sku` VALUES (2, 1828407291103842307, 2);
INSERT INTO `prod_tag_sku` VALUES (3, 1828407291103842307, 3);
INSERT INTO `prod_tag_sku` VALUES (4, 1828407291103842307, 4);
INSERT INTO `prod_tag_sku` VALUES (5, 1828407291103842307, 5);
INSERT INTO `prod_tag_sku` VALUES (6, 1828407291103842308, 1);
INSERT INTO `prod_tag_sku` VALUES (7, 1828407291103842308, 2);
INSERT INTO `prod_tag_sku` VALUES (8, 1828407291103842308, 5);

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` bigint NOT NULL,
  `dict_type` varchar(50) NOT NULL COMMENT '字典类型',
  `dict_code` varchar(10) NOT NULL COMMENT '字典选项编码',
  `dict_name` varchar(255) DEFAULT NULL COMMENT '字典选项名',
  `sort` int NULL DEFAULT NULL COMMENT '显示时的顺序',
  `is_enable` tinyint NOT NULL COMMENT '是否启用',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  INDEX `idx_dict_type`(`dict_type`)
);

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (1, 'bin_type', '0', '存货库位', 0, 1, '库位类型');
INSERT INTO `sys_dict` VALUES (2, 'bin_type', '1', '拣货库位', 1, 1, '库位类型');
INSERT INTO `sys_dict` VALUES (3, 'bin_type', '2', '退货库位', 2, 1, NULL);
INSERT INTO `sys_dict` VALUES (4, 'storage_rule', '0', '单SKU存放', 0, 1, NULL);
INSERT INTO `sys_dict` VALUES (5, 'storage_rule', '1', 'SKU混放', 1, 1, NULL);
INSERT INTO `sys_dict` VALUES (6, 'rack_type', '0', '通用货架', 0, 1, NULL);
INSERT INTO `sys_dict` VALUES (7, 'rack_type', '1', '穿梭货架', 1, 1, NULL);
INSERT INTO `sys_dict` VALUES (8, 'weight_unit', '0', 'lb', 0, 1, NULL);
INSERT INTO `sys_dict` VALUES (9, 'weight_unit', '1', 'kg', 1, 1, NULL);
INSERT INTO `sys_dict` VALUES (10, 'weight_unit', '2', 't', 2, 1, NULL);
INSERT INTO `sys_dict` VALUES (11, 'volume_unit', '0', 'ft³', 0, 1, NULL);
INSERT INTO `sys_dict` VALUES (12, 'volume_unit', '1', 'cm³', 1, 1, NULL);
INSERT INTO `sys_dict` VALUES (13, 'position_in_double', '0', '内', 0, 1, NULL);
INSERT INTO `sys_dict` VALUES (14, 'position_in_double', '1', '外', 1, 1, NULL);
INSERT INTO `sys_dict` VALUES (15, 'storage_unit', '0', 'LB', 0, 1, '库存单位：磅');
INSERT INTO `sys_dict` VALUES (16, 'storage_unit', '1', 'CS', 1, 1, '库存单位：箱');
INSERT INTO `sys_dict` VALUES (17, 'product_type', '0', '普通商品', 0, 1, '商品类型');
INSERT INTO `sys_dict` VALUES (18, 'product_type', '1', '贵重商品', 1, 1, NULL);
INSERT INTO `sys_dict` VALUES (19, 'product_status', '0', '合格品', 0, 1, '产品质量状态');
INSERT INTO `sys_dict` VALUES (20, 'product_status', '1', '不合格', 1, 1, NULL);
INSERT INTO `sys_dict` VALUES (21, 'customer_level', '1', 'Lv1', 0, 1, '客户级别');
INSERT INTO `sys_dict` VALUES (22, 'customer_level', '2', 'Lv2', 1, 1, '客户级别');
INSERT INTO `sys_dict` VALUES (23, 'customer_level', '3', 'Lv3', 2, 1, '客户级别');
INSERT INTO `sys_dict` VALUES (24, 'inbound_status', '0', '待入库', 0, 1, '入库状态');
INSERT INTO `sys_dict` VALUES (25, 'inbound_status', '1', '进行中', 1, 1, NULL);
INSERT INTO `sys_dict` VALUES (26, 'inbound_status', '2', '已完成', 2, 1, NULL);
INSERT INTO `sys_dict` VALUES (27, 'inbound_status', '3', '已作废', 3, 1, NULL);
INSERT INTO `sys_dict` VALUES (28, 'outbound_status', '0', '待出库', 0, 1, '出库状态');
INSERT INTO `sys_dict` VALUES (29, 'outbound_status', '1', '进行中', 1, 1, NULL);
INSERT INTO `sys_dict` VALUES (30, 'outbound_status', '2', '已完成', 2, 1, NULL);
INSERT INTO `sys_dict` VALUES (31, 'outbound_status', '3', '已作废', 3, 1, NULL);
INSERT INTO `sys_dict` VALUES (32, 'inventory_op_type', 'IN', '入库', 0, 1, '库存操作类型');
INSERT INTO `sys_dict` VALUES (33, 'inventory_op_type', 'OUT', '出库', 1, 1, '库存操作类型');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) DEFAULT '' COMMENT '路由地址',
  `menu_type` char(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) DEFAULT '0' COMMENT '显示状态（0显示 1隐藏）',
  `status` char(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`)
);

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 110, 'system', 'M', '1', '1', '', 'system', 'admin', '2024-06-13 16:06:26', 'admin', '2024-08-20 13:45:48', '系统管理目录');
INSERT INTO `sys_menu` VALUES (2, '系统监控', 0, 120, 'monitor', 'M', '1', '1', '', 'monitor', 'admin', '2024-06-13 16:06:26', 'admin', '2024-08-20 13:45:57', '系统监控目录');
INSERT INTO `sys_menu` VALUES (101, '用户管理', 1, 1, '/sys/user', 'C', '0', '0', '', '#', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', '用户管理菜单');
INSERT INTO `sys_menu` VALUES (102, '用户管理-查询', 101, 1, '', 'F', '0', '0', 'sys:user:list', '#', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', '用户管理查询权限');
INSERT INTO `sys_menu` VALUES (103, '用户管理-新增', 101, 2, '', 'F', '0', '0', 'sys:user:add', '#', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', '用户管理新增权限');
INSERT INTO `sys_menu` VALUES (104, '用户管理-编辑', 101, 3, '', 'F', '0', '0', 'sys:user:edit', '#', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', '用户管理编辑权限');
INSERT INTO `sys_menu` VALUES (105, '用户管理-删除', 101, 4, '', 'F', '0', '0', 'sys:user:remove', '#', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', '用户管理删除权限');
INSERT INTO `sys_menu` VALUES (111, '角色管理', 1, 2, '/sys/role', 'C', '0', '0', '', '#', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', '角色管理菜单');
INSERT INTO `sys_menu` VALUES (112, '角色管理-查询', 111, 1, '', 'F', '0', '0', 'sys:role:list', '#', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', '角色管理查询权限');
INSERT INTO `sys_menu` VALUES (113, '角色管理-新增', 111, 2, '', 'F', '0', '0', 'sys:role:add', '#', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', '角色管理新增权限');
INSERT INTO `sys_menu` VALUES (114, '角色管理-编辑', 111, 3, '', 'F', '0', '0', 'sys:role:edit', '#', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', '角色管理编辑权限');
INSERT INTO `sys_menu` VALUES (115, '角色管理-删除', 111, 4, '', 'F', '0', '0', 'sys:role:remove', '#', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', '角色管理删除权限');
INSERT INTO `sys_menu` VALUES (121, '权限管理', 1, 3, '/sys/menu', 'C', '0', '0', '', '#', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', '权限管理菜单');
INSERT INTO `sys_menu` VALUES (122, '权限管理-查询', 121, 1, '', 'F', '0', '0', 'sys:menu:list', '#', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', '权限管理查询权限');
INSERT INTO `sys_menu` VALUES (123, '权限管理-新增', 121, 2, '', 'F', '0', '0', 'sys:menu:add', '#', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', '权限管理新增权限');
INSERT INTO `sys_menu` VALUES (124, '权限管理-编辑', 121, 3, '', 'F', '0', '0', 'sys:menu:edit', '#', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', '权限管理编辑权限');
INSERT INTO `sys_menu` VALUES (125, '权限管理-删除', 121, 4, '', 'F', '0', '0', 'sys:menu:remove', '#', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', '权限管理删除权限');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) NOT NULL COMMENT '角色权限字符串',
  `role_sort` int DEFAULT 0 COMMENT '显示顺序',
  `status` char(1) NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`)
);

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '管理员', 'admin', 1, '0', '0', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', '系统管理员');
INSERT INTO `sys_role` VALUES (2, '收货员', 'receiving_clerk', 2, '0', '0', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', '负责收货任务');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`)
);

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1);
INSERT INTO `sys_role_menu` VALUES (1, 2);
INSERT INTO `sys_role_menu` VALUES (1, 101);
INSERT INTO `sys_role_menu` VALUES (1, 102);
INSERT INTO `sys_role_menu` VALUES (1, 103);
INSERT INTO `sys_role_menu` VALUES (1, 104);
INSERT INTO `sys_role_menu` VALUES (1, 105);
INSERT INTO `sys_role_menu` VALUES (1, 111);
INSERT INTO `sys_role_menu` VALUES (1, 112);
INSERT INTO `sys_role_menu` VALUES (1, 113);
INSERT INTO `sys_role_menu` VALUES (1, 114);
INSERT INTO `sys_role_menu` VALUES (1, 115);
INSERT INTO `sys_role_menu` VALUES (1, 121);
INSERT INTO `sys_role_menu` VALUES (1, 122);
INSERT INTO `sys_role_menu` VALUES (1, 123);
INSERT INTO `sys_role_menu` VALUES (1, 124);
INSERT INTO `sys_role_menu` VALUES (1, 125);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `family_name` varchar(30) DEFAULT '' COMMENT '用户姓',
  `given_name` varchar(30) NOT NULL COMMENT '用户名',
  `nick_name` varchar(30) NOT NULL COMMENT '用户昵称',
  `user_type` varchar(30) DEFAULT 'sys_user' COMMENT '用户类型（类似部门）',
  `email` varchar(50) NOT NULL DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) DEFAULT '' COMMENT '手机号码',
  `sex` char(1) DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) DEFAULT '' COMMENT '密码',
  `status` char(1) DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`)
);

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, '张', '三', 'admin', 'admin', 'admin@wms.com', '13800138000', '0', 'avatar_url', '123456', '0', '0', '127.0.0.1', '2025-12-01 00:00:00', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', '系统管理员');
INSERT INTO `sys_user` VALUES (2, '李', 'receiver', '收货员', 'receiving_clerk', 'receiver@wms.com', '13800138001', '0', 'avatar_url', '123456', '0', '0', '127.0.0.1', '2025-12-01 00:00:00', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', '收货员账号');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`)
);

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2);

-- ----------------------------
-- Table structure for authorities
-- ----------------------------
DROP TABLE IF EXISTS `authorities`;
CREATE TABLE `authorities`  (
  `authority_id` bigint NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `authority_name` varchar(100) NOT NULL COMMENT '权限名称',
  `authority_key` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `parent_id` bigint DEFAULT 0 COMMENT '父级ID',
  `order_num` int DEFAULT 0 COMMENT '排序',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`authority_id`)
);

-- ----------------------------
-- Records of authorities
-- ----------------------------
INSERT INTO `authorities` VALUES (1, '超级权限', '*', 0, 1, '0', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', '管理员全权限');
INSERT INTO `authorities` VALUES (2, '仓库模块', NULL, 0, 10, '0', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', NULL);
INSERT INTO `authorities` VALUES (3, '仓库管理', 'warehouse:manage', 2, 1, '0', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', NULL);
INSERT INTO `authorities` VALUES (4, '产品模块', NULL, 0, 20, '0', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', NULL);
INSERT INTO `authorities` VALUES (5, '产品管理', 'prod:manage', 4, 1, '0', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', NULL);
INSERT INTO `authorities` VALUES (6, '库存模块', NULL, 0, 30, '0', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', NULL);
INSERT INTO `authorities` VALUES (7, '库存查看', 'inventory:view', 6, 1, '0', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', NULL);
INSERT INTO `authorities` VALUES (8, '入库模块', NULL, 0, 40, '0', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', NULL);
INSERT INTO `authorities` VALUES (9, '入库单管理', 'inbound:manage', 8, 1, '0', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', NULL);
INSERT INTO `authorities` VALUES (10, '收货任务', 'inbound:receive', 8, 2, '0', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', NULL);
INSERT INTO `authorities` VALUES (11, '出库模块', NULL, 0, 50, '0', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', NULL);
INSERT INTO `authorities` VALUES (12, '出库单管理', 'outbound:manage', 11, 1, '0', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', NULL);
INSERT INTO `authorities` VALUES (13, '拣货任务', 'outbound:pick', 11, 2, '0', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', NULL);
INSERT INTO `authorities` VALUES (14, '客户模块', NULL, 0, 60, '0', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', NULL);
INSERT INTO `authorities` VALUES (15, '客户管理', 'customer:manage', 14, 1, '0', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', NULL);
INSERT INTO `authorities` VALUES (16, '系统模块', NULL, 0, 70, '0', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', NULL);
INSERT INTO `authorities` VALUES (17, '用户管理', 'sys:user:manage', 16, 1, '0', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', NULL);
INSERT INTO `authorities` VALUES (18, '用户新增', 'sys:user:add', 17, 1, '0', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', NULL);
INSERT INTO `authorities` VALUES (19, '用户编辑', 'sys:user:edit', 17, 2, '0', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', NULL);
INSERT INTO `authorities` VALUES (20, '用户删除', 'sys:user:remove', 17, 3, '0', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', NULL);
INSERT INTO `authorities` VALUES (21, '角色管理', 'sys:role:manage', 16, 2, '0', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', NULL);
INSERT INTO `authorities` VALUES (22, '角色新增', 'sys:role:add', 21, 1, '0', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', NULL);
INSERT INTO `authorities` VALUES (23, '角色编辑', 'sys:role:edit', 21, 2, '0', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', NULL);
INSERT INTO `authorities` VALUES (24, '角色删除', 'sys:role:remove', 21, 3, '0', 'admin', '2024-01-01 00:00:00', 'admin', '2024-01-01 00:00:00', NULL);

-- ----------------------------
-- Table structure for auth_role
-- ----------------------------
DROP TABLE IF EXISTS `auth_role`;
CREATE TABLE `auth_role`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `authority_id` bigint NOT NULL COMMENT '权限ID',
  PRIMARY KEY (`role_id`, `authority_id`)
);

-- ----------------------------
-- Records of auth_role
-- ----------------------------
INSERT INTO `auth_role` VALUES (1, 1);
INSERT INTO `auth_role` VALUES (2, 10);

-- ----------------------------
-- Table structure for warehouse
-- ----------------------------
DROP TABLE IF EXISTS `warehouse`;
CREATE TABLE `warehouse`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `warehouse_code` varchar(20) DEFAULT NULL COMMENT '编号',
  `warehouse_name` varchar(50) NOT NULL COMMENT '名称',
  `length` decimal(10, 3) NULL DEFAULT NULL COMMENT '长度（M）',
  `width` decimal(10, 3) NULL DEFAULT NULL COMMENT '宽（M)',
  `is_active` char(1) DEFAULT NULL COMMENT '是否可用：可用（1）；不可用（0）',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `city` varchar(255) DEFAULT NULL COMMENT '城市',
  `state` varchar(255) DEFAULT NULL COMMENT '州或省',
  `postal_code` varchar(255) DEFAULT NULL COMMENT '邮编',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `order_num` bigint NULL DEFAULT 0 COMMENT '排序',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Records of warehouse
-- ----------------------------
INSERT INTO `warehouse` VALUES (1828364609002311682, 'WH001', '苏州园区', 100.000, 50.000, '1', '苏州市工业园区', '苏州', '江苏', '215000', '普通仓库', 1, 'admin', '2024-08-27 17:31:06.821', 'admin', '2024-08-27 17:31:06.821');
INSERT INTO `warehouse` VALUES (1828364740028174337, 'WH002', '常熟冷链仓', 80.000, 40.000, '1', '常熟市冷链园区', '苏州', '江苏', '215500', '冷链专用仓库', 2, 'admin', '2024-08-27 17:31:38.066', 'admin', '2024-08-30 13:55:34.766');
INSERT INTO `warehouse` VALUES (1840317750635581441, 'WH003', '吴江仓', 90.000, 45.000, '1', '吴江区仓储大道', '苏州', '江苏', '215200', '综合仓库', 3, 'wms2_admin', '2024-09-29 17:08:37.859', 'wms2_admin', '2024-09-29 17:08:37.859');

-- ----------------------------
-- Table structure for warehouse_bin
-- ----------------------------
DROP TABLE IF EXISTS `warehouse_bin`;
CREATE TABLE `warehouse_bin`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '库位id',
  `bin_name` varchar(255) NOT NULL COMMENT '库位名',
  `warehouse_id` bigint NULL DEFAULT NULL COMMENT '仓库id',
  `zone_id` bigint NULL DEFAULT NULL COMMENT '库区id',
  `rack_id` bigint NULL DEFAULT NULL COMMENT '货架ID',
  `column_num` int NULL DEFAULT NULL COMMENT '列号',
  `on_zoom_level` int NULL DEFAULT NULL COMMENT '所在库区的层数',
  `rack_type` varchar(10) NOT NULL COMMENT '货架类型',
  `bin_type` varchar(10) NOT NULL COMMENT '库位类型',
  `maximum_capcity` decimal(10, 3) NULL DEFAULT NULL COMMENT '最大单库位容量',
  `capcity_unit` varchar(10) DEFAULT NULL COMMENT '库位容量单位',
  `maximum_volume` decimal(10, 3) NULL DEFAULT NULL COMMENT '最大单库位体积',
  `volume_unit` varchar(10) DEFAULT NULL COMMENT '库位体积单位',
  `storage_rule` varchar(10) DEFAULT NULL COMMENT '存放规则',
  `is_enabled` tinyint NULL DEFAULT NULL COMMENT '是否可用',
  `low_storage_alert_rato` decimal(10, 2) NULL DEFAULT NULL COMMENT '低存储率提醒值（%）',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_bin_name`(`bin_name`),
  INDEX `idx_zone_bin`(`zone_id`)
);

-- ----------------------------
-- Records of warehouse_bin
-- ----------------------------
INSERT INTO `warehouse_bin` VALUES (1, 'C2-7-4', 1828364740028174337, 1, NULL, 7, 4, '0', '0', 100.000, '1', 2.000, '11', '0', 1, 30.00);
INSERT INTO `warehouse_bin` VALUES (2, 'C2-7-5', 1828364740028174337, 1, NULL, 7, 5, '0', '0', 100.000, '1', 2.000, '11', '0', 1, 30.00);
INSERT INTO `warehouse_bin` VALUES (3, 'C15-14-1', 1828364740028174337, 1, NULL, 14, 1, '0', '1', 80.000, '1', 1.600, '11', '0', 1, 25.00);
INSERT INTO `warehouse_bin` VALUES (4, 'C15-26-1', 1828364740028174337, 1, NULL, 26, 1, '0', '1', 80.000, '1', 1.600, '11', '0', 1, 25.00);
INSERT INTO `warehouse_bin` VALUES (5, 'C19-6-6', 1828364740028174337, 1, NULL, 6, 6, '0', '0', 120.000, '1', 2.400, '11', '0', 1, 35.00);
INSERT INTO `warehouse_bin` VALUES (6, 'C19-7-5', 1828364740028174337, 1, NULL, 7, 5, '0', '0', 120.000, '1', 2.400, '11', '0', 1, 35.00);
INSERT INTO `warehouse_bin` VALUES (7, 'C20-6-6', 1828364740028174337, 1, NULL, 6, 6, '0', '0', 120.000, '1', 2.400, '11', '0', 1, 35.00);
INSERT INTO `warehouse_bin` VALUES (8, 'C20-7-5', 1828364740028174337, 1, NULL, 7, 5, '0', '0', 120.000, '1', 2.400, '11', '0', 1, 35.00);
INSERT INTO `warehouse_bin` VALUES (9, 'C18-39-1', 1828364740028174337, 1, NULL, 39, 1, '0', '1', 60.000, '1', 1.200, '11', '0', 1, 20.00);
INSERT INTO `warehouse_bin` VALUES (10, 'D-21B-1', 1828364740028174337, 1, NULL, 21, 1, '1', '0', 150.000, '1', 3.000, '11', '0', 1, 40.00);

-- ----------------------------
-- Table structure for warehouse_rack
-- ----------------------------
DROP TABLE IF EXISTS `warehouse_rack`;
CREATE TABLE `warehouse_rack`  (
  `id` bigint NOT NULL COMMENT '货架id',
  `rack_name` varchar(255) DEFAULT NULL COMMENT '货架名',
  `warehouse_id` bigint NULL DEFAULT NULL COMMENT '仓库id',
  `zone_id` bigint NULL DEFAULT NULL COMMENT '所属货区id',
  `length` decimal(10, 3) NULL DEFAULT NULL COMMENT '货架长度',
  `width` decimal(10, 3) NULL DEFAULT NULL COMMENT '货架宽度',
  `rack_type` varchar(10) DEFAULT NULL COMMENT '货架类型(参考sys_dict的rack_type)',
  `is_double` tinyint NULL DEFAULT NULL COMMENT '双排货架',
  `position_in_double` varchar(10) DEFAULT NULL COMMENT '双排位置（0内/1外，参考sys_dict）',
  `related_rack` bigint NULL DEFAULT NULL COMMENT '关联货架（双排时）',
  PRIMARY KEY (`id`),
  INDEX `idx_zone_rack`(`zone_id`)
);

-- ----------------------------
-- Records of warehouse_rack
-- ----------------------------
INSERT INTO `warehouse_rack` VALUES (1, 'Rack-C2', 1828364740028174337, 1, 8.000, 1.200, '0', 0, '0', NULL);
INSERT INTO `warehouse_rack` VALUES (2, 'Rack-C15', 1828364740028174337, 1, 8.000, 1.200, '0', 0, NULL, NULL);
INSERT INTO `warehouse_rack` VALUES (3, 'Rack-C18', 1828364740028174337, 1, 8.000, 1.200, '0', 0, NULL, NULL);
INSERT INTO `warehouse_rack` VALUES (4, 'Rack-C19', 1828364740028174337, 1, 8.000, 1.200, '0', 1, '0', 5);
INSERT INTO `warehouse_rack` VALUES (5, 'Rack-C19-Outer', 1828364740028174337, 1, 8.000, 1.200, '0', 1, '1', 4);
INSERT INTO `warehouse_rack` VALUES (6, 'Rack-D', 1828364740028174337, 1, 8.000, 1.200, '1', 0, NULL, NULL);

-- ----------------------------
-- Table structure for warehouse_zone
-- ----------------------------
DROP TABLE IF EXISTS `warehouse_zone`;
CREATE TABLE `warehouse_zone`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '库区id',
  `zone_name` varchar(255) DEFAULT NULL COMMENT '库区名',
  `warehouse_id` bigint NULL DEFAULT NULL COMMENT '仓库id',
  `layers` int NULL DEFAULT NULL COMMENT '拥有的层数',
  `length` decimal(10, 3) NULL DEFAULT NULL COMMENT '长度',
  `width` decimal(10, 3) NULL DEFAULT NULL COMMENT '宽度',
  `is_enabled` tinyint NULL DEFAULT NULL COMMENT '是否可用：1可用；0不可用',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  INDEX `idx_warehouse_zone`(`warehouse_id`)
);

-- ----------------------------
-- Records of warehouse_zone
-- ----------------------------
INSERT INTO `warehouse_zone` VALUES (1, 'Freezer(C,D)', 1828364740028174337, 5, 50.000, 30.000, 1, '冷链库区C、D区域');
INSERT INTO `warehouse_zone` VALUES (2, 'Normal Zone A', 1828364609002311682, 3, 40.000, 25.000, 1, '苏州园区常温库区A');
INSERT INTO `warehouse_zone` VALUES (3, 'Cold Zone B', 1840317750635581441, 4, 35.000, 20.000, 1, '吴江仓冷藏库区B');

SET FOREIGN_KEY_CHECKS = 1;
