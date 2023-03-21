DROP DATABASE IF EXISTS `pangu-cloud`;

CREATE DATABASE `pangu-cloud` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

USE `pangu-cloud`;

SET NAMES utf8mb4;
-- ----------------------------
-- Table structure for iot_device
-- ----------------------------
DROP TABLE IF EXISTS `iot_device`;
CREATE TABLE `iot_device`
(
    `id`            bigint                                                 NOT NULL COMMENT '设备主键',
    `code`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '设备编号',
    `group_id`      bigint                                                 NOT NULL DEFAULT 0 COMMENT '设备分组ID',
    `product_id`    bigint                                                 NOT NULL DEFAULT 0 COMMENT '产品ID',
    `name`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '设备名称',
    `type`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL COMMENT '设备类型',
    `address`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '设备地址',
    `position`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '地址坐标',
    `latest_online` datetime                                               NULL     DEFAULT NULL COMMENT '最近在线时间',
    `status`        tinyint(1)                                             NOT NULL DEFAULT 1 COMMENT '启用状态',
    `create_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT '' COMMENT '创建者',
    `create_time`   datetime                                               NULL     DEFAULT NULL COMMENT '创建时间',
    `update_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT '' COMMENT '更新者',
    `update_time`   datetime                                               NULL     DEFAULT NULL COMMENT '更新时间',
    `remark`        varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT '' COMMENT '备注',
    `zbx_id`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT 'Zabbix对应模板ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '设备'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_device
-- ----------------------------

-- ----------------------------
-- Table structure for iot_device_attribute
-- ----------------------------
DROP TABLE IF EXISTS `iot_device_attribute`;
CREATE TABLE `iot_device_attribute`
(
    `id`                 bigint                                                 NOT NULL COMMENT '主键',
    `product_id`         bigint                                                 NOT NULL COMMENT '产品ID',
    `device_id`          bigint                                                 NULL     DEFAULT 0 COMMENT '设备编号',
    `name`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '属性名称',
    `key`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '属性唯一Key\n',
    `value_type`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT '' COMMENT '值类型',
    `source`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT '' COMMENT '来源',
    `unit`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '单位描述',
    `master_item_id`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '主条目id\n',
    `dependency_attr_id` bigint                                                 NULL     DEFAULT NULL COMMENT ' 依赖属性 id， 当 source为18时不为空\n',
    `zbx_id`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'zabbix ItemId',
    `template_id`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '继承的ID',
    `value_map_id`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT '' COMMENT 'zabbix 值映射ID',
    `create_by`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT '' COMMENT '创建者',
    `create_time`        datetime                                               NULL     DEFAULT NULL COMMENT '创建时间',
    `update_by`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT '' COMMENT '更新者',
    `update_time`        datetime                                               NULL     DEFAULT NULL COMMENT '更新时间',
    `remark`             varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT '' COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '产品属性'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_device_attribute
-- ----------------------------

-- ----------------------------
-- Table structure for iot_device_group
-- ----------------------------
DROP TABLE IF EXISTS `iot_device_group`;
CREATE TABLE `iot_device_group`
(
    `id`          bigint                                                 NOT NULL COMMENT '主键',
    `name`        varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL COMMENT '分组名称',
    `zbx_id`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'zabbix ItemId',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT '' COMMENT '创建者',
    `create_time` datetime                                               NULL     DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT '' COMMENT '更新者',
    `update_time` datetime                                               NULL     DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT '' COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '设备分组'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_device_group
-- ----------------------------
INSERT INTO `iot_device_group`
VALUES (1611599188680720384, '默认设备组', '26', 'admin', '2023-01-07 13:42:43', 'admin', '2023-01-07 13:42:43', '');

-- ----------------------------
-- Table structure for iot_device_group_relation
-- ----------------------------
DROP TABLE IF EXISTS `iot_device_group_relation`;
CREATE TABLE `iot_device_group_relation`
(
    `id`              bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `device_id`       bigint NOT NULL COMMENT '设备ID',
    `device_group_id` bigint NOT NULL COMMENT '设备组ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1633348004011876354
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '设备与分组关系'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_device_group_relation
-- ----------------------------

-- ----------------------------
-- Table structure for iot_device_status_function
-- ----------------------------
DROP TABLE IF EXISTS `iot_device_status_function`;
CREATE TABLE `iot_device_status_function`
(
    `id`                      bigint                                                 NOT NULL COMMENT '主键',
    `attribute_id`            bigint                                                 NULL DEFAULT NULL COMMENT '下线属性ID',
    `rule_function`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '下线规则函数',
    `rule_condition`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '下线规则条件',
    `unit`                    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '下线单位描述',
    `attribute_id_recovery`   bigint                                                 NULL DEFAULT NULL COMMENT '上线属性ID',
    `rule_function_recovery`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '上线规则函数',
    `rule_condition_recovery` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '上线规则条件',
    `unit_recovery`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '上线单位描述',
    `status`                  int                                                    NULL DEFAULT NULL COMMENT '状态',
    `create_by`               varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '创建者',
    `create_time`             datetime                                               NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`               varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '更新者',
    `update_time`             datetime                                               NULL DEFAULT NULL COMMENT '更新时间',
    `remark`                  varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '设备上下线规则'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_device_status_function
-- ----------------------------

-- ----------------------------
-- Table structure for iot_device_status_function_relation
-- ----------------------------
DROP TABLE IF EXISTS `iot_device_status_function_relation`;
CREATE TABLE `iot_device_status_function_relation`
(
    `id`              int                                                   NOT NULL AUTO_INCREMENT COMMENT '主键',
    `rule_id`         bigint                                                NULL DEFAULT NULL COMMENT '规则ID',
    `relation_id`     bigint                                                NULL DEFAULT NULL COMMENT '产品/设备ID',
    `zbx_id`          varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'zbx下线触发器主键',
    `zbx_id_recovery` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'zbx上线触发器主键',
    `create_by`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
    `create_time`     datetime                                              NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
    `update_time`     datetime                                              NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 14
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '设备上下线规则与设备关系'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_device_status_function_relation
-- ----------------------------

-- ----------------------------
-- Table structure for iot_driver
-- ----------------------------
DROP TABLE IF EXISTS `iot_driver`;
CREATE TABLE `iot_driver`
(
    `id`           bigint                                                  NOT NULL COMMENT '主键ID',
    `name`         varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '协议名称',
    `display_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '显示名称',
    `enable`       tinyint                                                 NULL DEFAULT 1 COMMENT '启用|禁用',
    `description`  varchar(380) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '描述',
    `create_time`  datetime                                                NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime                                                NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `name` (`name` ASC) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '协议驱动表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of iot_driver
-- ----------------------------

-- ----------------------------
-- Table structure for iot_driver_attribute
-- ----------------------------
DROP TABLE IF EXISTS `iot_driver_attribute`;
CREATE TABLE `iot_driver_attribute`
(
    `id`           bigint                                                  NOT NULL COMMENT '主键ID',
    `driver_id`    bigint                                                  NOT NULL COMMENT '驱动ID',
    `display_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '显示名称',
    `name`         varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '名称',
    `type`         varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '类型',
    `value`        varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '默认值',
    `description`  varchar(380) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '描述',
    `create_time`  datetime                                                NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime                                                NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `driver_id` (`driver_id` ASC) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '连接配置信息表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of iot_driver_attribute
-- ----------------------------

-- ----------------------------
-- Table structure for iot_driver_info
-- ----------------------------
DROP TABLE IF EXISTS `iot_driver_info`;
CREATE TABLE `iot_driver_info`
(
    `id`                  bigint                                                  NOT NULL COMMENT '主键ID',
    `driver_attribute_id` bigint                                                  NOT NULL COMMENT '驱动属性ID',
    `device_id`           bigint                                                  NOT NULL COMMENT '设备ID',
    `value`               varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '值',
    `description`         varchar(380) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '描述',
    `create_time`         datetime                                                NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`         datetime                                                NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `driver_attribute_id` (`driver_attribute_id` ASC) USING BTREE,
    INDEX `device_id` (`device_id` ASC) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '模板连接配置信息表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of iot_driver_info
-- ----------------------------

-- ----------------------------
-- Table structure for iot_driver_service
-- ----------------------------
DROP TABLE IF EXISTS `iot_driver_service`;
CREATE TABLE `iot_driver_service`
(
    `id`           varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键ID',
    `driver_id`    bigint                                                 NOT NULL COMMENT '驱动ID',
    `service_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '协议服务名称',
    `host`         varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主机IP',
    `port`         int                                                    NOT NULL COMMENT '端口',
    `create_time`  datetime                                               NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime                                               NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '驱动服务表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of iot_driver_service
-- ----------------------------

-- ----------------------------
-- Table structure for iot_gateway_device_bind
-- ----------------------------
DROP TABLE IF EXISTS `iot_gateway_device_bind`;
CREATE TABLE `iot_gateway_device_bind`
(
    `id`                int    NOT NULL AUTO_INCREMENT,
    `gateway_device_id` bigint NULL DEFAULT NULL COMMENT '网关设备ID',
    `device_id`         bigint NULL DEFAULT NULL COMMENT '设备ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '网关设备绑定子设备'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_gateway_device_bind
-- ----------------------------

-- ----------------------------
-- Table structure for iot_point_attribute
-- ----------------------------
DROP TABLE IF EXISTS `iot_point_attribute`;
CREATE TABLE `iot_point_attribute`
(
    `id`           bigint                                                  NOT NULL COMMENT '主键ID',
    `driver_id`    bigint                                                  NOT NULL COMMENT '驱动ID',
    `display_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '显示名称',
    `name`         varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '名称',
    `type`         varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '类型',
    `value`        varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '默认值',
    `description`  varchar(380) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '描述',
    `create_time`  datetime                                                NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime                                                NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `driver_id` (`driver_id` ASC) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '点位属性表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of iot_point_attribute
-- ----------------------------

-- ----------------------------
-- Table structure for iot_point_info
-- ----------------------------
DROP TABLE IF EXISTS `iot_point_info`;
CREATE TABLE `iot_point_info`
(
    `id`                  bigint                                                  NOT NULL COMMENT '主键ID',
    `device_attribute_id` bigint                                                  NOT NULL COMMENT '设备属性ID',
    `point_attribute_id`  bigint                                                  NOT NULL COMMENT '点位属性ID',
    `device_id`           bigint                                                  NOT NULL COMMENT '设备ID',
    `value`               varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '值',
    `description`         varchar(380) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '描述',
    `create_time`         datetime                                                NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`         datetime                                                NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `point_attribute_id` (`point_attribute_id` ASC) USING BTREE,
    INDEX `device_id` (`device_id` ASC) USING BTREE,
    INDEX `point_id` (`device_attribute_id` ASC) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '点位属性配置信息表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of iot_point_info
-- ----------------------------

-- ----------------------------
-- Table structure for iot_problem
-- ----------------------------
DROP TABLE IF EXISTS `iot_problem`;
CREATE TABLE `iot_problem`
(
    `event_id`     bigint                                                  NOT NULL COMMENT 'event_id',
    `object_id`    bigint                                                  NULL DEFAULT NULL COMMENT '对象ID',
    `severity`     int                                                     NULL DEFAULT 0 COMMENT '告警级别',
    `name`         varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '名称',
    `acknowledged` int                                                     NULL DEFAULT 0 COMMENT '待确认状态',
    `clock`        datetime                                                NULL DEFAULT NULL COMMENT '时间',
    `r_clock`      datetime                                                NULL DEFAULT NULL COMMENT '解决时间',
    `device_id`    bigint                                                  NULL DEFAULT NULL COMMENT '设备ID',
    `create_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NULL DEFAULT '' COMMENT '创建者',
    `create_time`  datetime                                                NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NULL DEFAULT '' COMMENT '更新者',
    `update_time`  datetime                                                NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`event_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '告警记录'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_problem
-- ----------------------------

-- ----------------------------
-- Table structure for iot_product
-- ----------------------------
DROP TABLE IF EXISTS `iot_product`;
CREATE TABLE `iot_product`
(
    `id`           bigint                                                 NOT NULL COMMENT '产品主键',
    `group_id`     bigint                                                 NOT NULL DEFAULT 0 COMMENT '产品分组ID',
    `code`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '产品编号',
    `name`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '产品名称',
    `type`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT NULL COMMENT '产品类型',
    `driver`       varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '驱动IDs',
    `icon`         varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '图标',
    `manufacturer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '厂家',
    `model`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '型号',
    `device_count` int                                                    NULL     DEFAULT 0 COMMENT '设备总数',
    `create_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT '' COMMENT '创建者',
    `create_time`  datetime                                               NULL     DEFAULT NULL COMMENT '创建时间',
    `update_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT '' COMMENT '更新者',
    `update_time`  datetime                                               NULL     DEFAULT NULL COMMENT '更新时间',
    `remark`       varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT '' COMMENT '备注',
    `zbx_id`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT 'Zabbix对应模板ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '产品'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_product
-- ----------------------------

-- ----------------------------
-- Table structure for iot_product_event
-- ----------------------------
DROP TABLE IF EXISTS `iot_product_event`;
CREATE TABLE `iot_product_event`
(
    `id`           bigint                                                 NOT NULL COMMENT '主键',
    `name`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '告警规则名称',
    `level`        int                                                    NULL DEFAULT NULL COMMENT '告警等级',
    `exp_logic`    varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT 'and 或者 or',
    `notify`       int                                                    NULL DEFAULT NULL COMMENT '0 否 1 是',
    `classify`     int                                                    NULL DEFAULT NULL COMMENT '0 告警 1场景联动',
    `task_id`      bigint                                                 NULL DEFAULT NULL COMMENT '任务ID',
    `trigger_type` int                                                    NULL DEFAULT 0 COMMENT '触发类型 0-条件触发 1-定时触发',
    `status`       int                                                    NULL DEFAULT 1 COMMENT '状态',
    `create_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '创建者',
    `create_time`  datetime                                               NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '更新者',
    `update_time`  datetime                                               NULL DEFAULT NULL COMMENT '更新时间',
    `remark`       varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '告警规则'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_product_event
-- ----------------------------

-- ----------------------------
-- Table structure for iot_product_event_expression
-- ----------------------------
DROP TABLE IF EXISTS `iot_product_event_expression`;
CREATE TABLE `iot_product_event_expression`
(
    `id`                     bigint                                                 NOT NULL,
    `rule_id`                bigint                                                 NULL DEFAULT NULL COMMENT '告警规则主键',
    `function`               varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '函数',
    `scope`                  varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '作用域',
    `condition`              varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '条件',
    `value`                  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '值',
    `unit`                   varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '单位',
    `relation_id`            bigint                                                 NULL DEFAULT NULL COMMENT '产品、设备ID',
    `product_attribute_id`   bigint                                                 NULL DEFAULT NULL COMMENT '属性ID',
    `product_attribute_key`  varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '属性KEY',
    `product_attribute_type` int                                                    NULL DEFAULT NULL COMMENT '属性类型 属性 事件',
    `period`                 int                                                    NULL DEFAULT NULL COMMENT '取值： 时间 周期',
    `attribute_value_type`   varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '属性值类型',
    `remark`                 varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
    `create_by`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '创建者',
    `create_time`            datetime                                               NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '更新者',
    `update_time`            datetime                                               NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '告警规则达式'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_product_event_expression
-- ----------------------------

-- ----------------------------
-- Table structure for iot_product_event_relation
-- ----------------------------
DROP TABLE IF EXISTS `iot_product_event_relation`;
CREATE TABLE `iot_product_event_relation`
(
    `id`            bigint                                                 NOT NULL AUTO_INCREMENT COMMENT '主键',
    `event_rule_id` bigint                                                 NULL DEFAULT NULL COMMENT '规则ID',
    `relation_id`   bigint                                                 NULL DEFAULT NULL COMMENT '关联产品或设备ID',
    `zbx_id`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT 'trigger id',
    `inherit`       int                                                    NULL DEFAULT 1 COMMENT '是否来自产品',
    `status`        int                                                    NULL DEFAULT 1 COMMENT '状态',
    `create_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '创建者',
    `create_time`   datetime                                               NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '更新者',
    `update_time`   datetime                                               NULL DEFAULT NULL COMMENT '更新时间',
    `remark`        varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 46
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '告警规则关系'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_product_event_relation
-- ----------------------------

-- ----------------------------
-- Table structure for iot_product_event_service
-- ----------------------------
DROP TABLE IF EXISTS `iot_product_event_service`;
CREATE TABLE `iot_product_event_service`
(
    `id`                bigint                                                NOT NULL COMMENT '主键',
    `event_rule_id`     bigint                                                NULL DEFAULT NULL COMMENT '告警规则ID',
    `service_id`        bigint                                                NULL DEFAULT NULL COMMENT '功能ID',
    `relation_id`       bigint                                                NULL DEFAULT NULL COMMENT '产品、设备ID',
    `execute_device_id` bigint                                                NULL DEFAULT NULL COMMENT '执行目标设备ID',
    `create_by`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
    `create_time`       datetime                                              NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
    `update_time`       datetime                                              NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '告警规则与功能关系表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_product_event_service
-- ----------------------------

-- ----------------------------
-- Table structure for iot_product_group
-- ----------------------------
DROP TABLE IF EXISTS `iot_product_group`;
CREATE TABLE `iot_product_group`
(
    `id`          bigint                                                 NOT NULL COMMENT '主键',
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
    `parent_id`   bigint                                                 NULL DEFAULT 0,
    `pids`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime                                               NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime                                               NULL DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '产品分组'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_product_group
-- ----------------------------
INSERT INTO `iot_product_group`
VALUES (1, '默认分组', 0, '[0],', 'admin', '2022-11-01 00:31:23', '', NULL, '');

-- ----------------------------
-- Table structure for iot_product_service
-- ----------------------------
DROP TABLE IF EXISTS `iot_product_service`;
CREATE TABLE `iot_product_service`
(
    `id`          bigint                                                 NOT NULL COMMENT '主键',
    `name`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '功能名称',
    `mark`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '功能标识',
    `remark`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
    `async`       int                                                    NULL DEFAULT 0 COMMENT '执行方式 0-同步 1-异步',
    `data_type`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '数据类型',
    `specs`       json                                                   NULL COMMENT '数据类型对象参数',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime                                               NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime                                               NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '产品功能'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_product_service
-- ----------------------------

-- ----------------------------
-- Table structure for iot_product_service_param
-- ----------------------------
DROP TABLE IF EXISTS `iot_product_service_param`;
CREATE TABLE `iot_product_service_param`
(
    `id`          bigint                                                 NOT NULL,
    `service_id`  bigint                                                 NULL DEFAULT NULL COMMENT '服务ID',
    `key`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '参数标识',
    `name`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '参数名',
    `value`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '参数值',
    `remark`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
    `device_id`   bigint                                                 NULL DEFAULT NULL COMMENT '设备IDremark',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime                                               NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime                                               NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '产品功能参数'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_product_service_param
-- ----------------------------

-- ----------------------------
-- Table structure for iot_product_service_relation
-- ----------------------------
DROP TABLE IF EXISTS `iot_product_service_relation`;
CREATE TABLE `iot_product_service_relation`
(
    `id`          int                                                   NOT NULL AUTO_INCREMENT,
    `service_id`  bigint                                                NULL DEFAULT NULL COMMENT '服务ID',
    `relation_id` bigint                                                NULL DEFAULT NULL COMMENT '关联ID',
    `inherit`     int                                                   NULL DEFAULT 0 COMMENT '是否继承',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime                                              NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime                                              NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 83
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '产品功能关联关系'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_product_service_relation
-- ----------------------------

-- ----------------------------
-- Table structure for iot_service_execute_record
-- ----------------------------
DROP TABLE IF EXISTS `iot_service_execute_record`;
CREATE TABLE `iot_service_execute_record`
(
    `id`              bigint                                                 NOT NULL,
    `service_name`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '功能名称',
    `param`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '参数',
    `device_id`       bigint                                                 NULL DEFAULT NULL COMMENT '设备ID',
    `execute_type`    int                                                    NULL DEFAULT 0 COMMENT '执行方式   0手动触发 1告警触发 2场景触发',
    `execute_user`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '执行人执行方式未手动触发时有值',
    `execute_rule_id` bigint                                                 NULL DEFAULT NULL COMMENT '执行场景ID',
    `execute_status`  int                                                    NULL DEFAULT 0 COMMENT '执行状态 0 失败 1成功',
    `create_by`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '创建者',
    `create_time`     datetime                                               NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '更新者',
    `update_time`     datetime                                               NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '功能执行记录'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_service_execute_record
-- ----------------------------

-- ----------------------------
-- Table structure for sys_api_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_api_token`;
CREATE TABLE `sys_api_token`
(
    `id`              bigint                                                 NOT NULL COMMENT '主键id',
    `name`            varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '名称',
    `token`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT 'token',
    `expiration_time` datetime                                               NULL DEFAULT NULL COMMENT '过期时间',
    `status`          int                                                    NULL DEFAULT 0 COMMENT '状态（0正常 1停用）',
    `create_by`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '创建者',
    `create_time`     datetime                                               NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '更新者',
    `update_time`     datetime                                               NULL DEFAULT NULL COMMENT '更新时间',
    `remark`          varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '三方授权'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_api_token
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`
(
    `config_id`    bigint                                                 NOT NULL COMMENT '参数主键',
    `config_name`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '参数名称',
    `config_key`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '参数键名',
    `config_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '参数键值',
    `config_type`  char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin      NULL DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
    `create_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '创建者',
    `create_time`  datetime                                               NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '更新者',
    `update_time`  datetime                                               NULL DEFAULT NULL COMMENT '更新时间',
    `remark`       varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '参数配置表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config`
VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '2022-12-12 14:38:42', '',
        NULL, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO `sys_config`
VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2022-12-12 14:38:42', '', NULL,
        '初始化密码 123456');
INSERT INTO `sys_config`
VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2022-12-12 14:38:42', '', NULL,
        '深色主题theme-dark，浅色主题theme-light');
INSERT INTO `sys_config`
VALUES (4, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 'admin', '2022-12-12 14:38:42',
        '', NULL, '是否开启注册用户功能（true开启，false关闭）');
INSERT INTO `sys_config`
VALUES (11, 'OSS预览列表资源开关', 'sys.oss.previewListResource', 'true', 'Y', 'admin', '2022-12-12 14:38:42', '', NULL,
        'true:开启, false:关闭');
INSERT INTO `sys_config`
VALUES (1611188425570959362, 'Zabbix默认分组ID', 'iot.global.host.group.id', '19', 'Y', 'admin', '2023-01-06 10:30:30',
        NULL, '2023-03-20 10:59:43', NULL);

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`
(
    `dept_id`     bigint                                                 NOT NULL COMMENT '部门id',
    `parent_id`   bigint                                                 NULL DEFAULT 0 COMMENT '父部门id',
    `ancestors`   varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '祖级列表',
    `dept_name`   varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '部门名称',
    `order_num`   int                                                    NULL DEFAULT 0 COMMENT '显示顺序',
    `leader`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '负责人',
    `phone`       varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '联系电话',
    `email`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '邮箱',
    `status`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin      NULL DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
    `del_flag`    char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin      NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime                                               NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime                                               NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '部门表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept`
VALUES (100, 0, '0', '若依科技', 0, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-12-12 14:38:39', '',
        NULL);
INSERT INTO `sys_dept`
VALUES (101, 100, '0,100', '深圳总公司', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin',
        '2022-12-12 14:38:39', '', NULL);
INSERT INTO `sys_dept`
VALUES (102, 100, '0,100', '长沙分公司', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin',
        '2022-12-12 14:38:39', '', NULL);
INSERT INTO `sys_dept`
VALUES (103, 101, '0,100,101', '研发部门', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin',
        '2022-12-12 14:38:39', '', NULL);
INSERT INTO `sys_dept`
VALUES (104, 101, '0,100,101', '市场部门', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin',
        '2022-12-12 14:38:39', '', NULL);
INSERT INTO `sys_dept`
VALUES (105, 101, '0,100,101', '测试部门', 3, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin',
        '2022-12-12 14:38:39', '', NULL);
INSERT INTO `sys_dept`
VALUES (106, 101, '0,100,101', '财务部门', 4, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin',
        '2022-12-12 14:38:39', '', NULL);
INSERT INTO `sys_dept`
VALUES (107, 101, '0,100,101', '运维部门', 5, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin',
        '2022-12-12 14:38:39', '', NULL);
INSERT INTO `sys_dept`
VALUES (108, 102, '0,100,102', '市场部门', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin',
        '2022-12-12 14:38:39', '', NULL);
INSERT INTO `sys_dept`
VALUES (109, 102, '0,100,102', '财务部门', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin',
        '2022-12-12 14:38:39', '', NULL);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`
(
    `dict_code`   bigint                                                 NOT NULL COMMENT '字典编码',
    `dict_sort`   int                                                    NULL DEFAULT 0 COMMENT '字典排序',
    `dict_label`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '字典标签',
    `dict_value`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '字典键值',
    `dict_type`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '字典类型',
    `css_class`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
    `list_class`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '表格回显样式',
    `is_default`  char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin      NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
    `groups`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '数据分组字段',
    `status`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin      NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime                                               NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime                                               NULL DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '字典数据表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data`
VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', NULL, '0', 'admin', '2022-12-12 14:38:42', '', NULL, '性别男');
INSERT INTO `sys_dict_data`
VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', NULL, '0', 'admin', '2022-12-12 14:38:42', '', NULL, '性别女');
INSERT INTO `sys_dict_data`
VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', NULL, '0', 'admin', '2022-12-12 14:38:42', '', NULL,
        '性别未知');
INSERT INTO `sys_dict_data`
VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', NULL, '0', 'admin', '2022-12-12 14:38:42', '', NULL,
        '显示菜单');
INSERT INTO `sys_dict_data`
VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', NULL, '0', 'admin', '2022-12-12 14:38:42', '', NULL,
        '隐藏菜单');
INSERT INTO `sys_dict_data`
VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', NULL, '0', 'admin', '2022-12-12 14:38:42', '',
        NULL, '正常状态');
INSERT INTO `sys_dict_data`
VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', NULL, '0', 'admin', '2022-12-12 14:38:42', '', NULL,
        '停用状态');
INSERT INTO `sys_dict_data`
VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', NULL, '0', 'admin', '2022-12-12 14:38:42', '', NULL,
        '正常状态');
INSERT INTO `sys_dict_data`
VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', NULL, '0', 'admin', '2022-12-12 14:38:42', '', NULL,
        '停用状态');
INSERT INTO `sys_dict_data`
VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', NULL, '0', 'admin', '2022-12-12 14:38:42', '', NULL,
        '默认分组');
INSERT INTO `sys_dict_data`
VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', NULL, '0', 'admin', '2022-12-12 14:38:42', '', NULL,
        '系统分组');
INSERT INTO `sys_dict_data`
VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', NULL, '0', 'admin', '2022-12-12 14:38:42', '', NULL,
        '系统默认是');
INSERT INTO `sys_dict_data`
VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', NULL, '0', 'admin', '2022-12-12 14:38:42', '', NULL,
        '系统默认否');
INSERT INTO `sys_dict_data`
VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', NULL, '0', 'admin', '2022-12-12 14:38:42', '', NULL,
        '通知');
INSERT INTO `sys_dict_data`
VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', NULL, '0', 'admin', '2022-12-12 14:38:42', '', NULL,
        '公告');
INSERT INTO `sys_dict_data`
VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', NULL, '0', 'admin', '2022-12-12 14:38:42', '',
        NULL, '正常状态');
INSERT INTO `sys_dict_data`
VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', NULL, '0', 'admin', '2022-12-12 14:38:42', '', NULL,
        '关闭状态');
INSERT INTO `sys_dict_data`
VALUES (18, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', NULL, '0', 'admin', '2022-12-12 14:38:42', '', NULL,
        '新增操作');
INSERT INTO `sys_dict_data`
VALUES (19, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', NULL, '0', 'admin', '2022-12-12 14:38:42', '', NULL,
        '修改操作');
INSERT INTO `sys_dict_data`
VALUES (20, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', NULL, '0', 'admin', '2022-12-12 14:38:42', '', NULL,
        '删除操作');
INSERT INTO `sys_dict_data`
VALUES (21, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', NULL, '0', 'admin', '2022-12-12 14:38:42', '', NULL,
        '授权操作');
INSERT INTO `sys_dict_data`
VALUES (22, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', NULL, '0', 'admin', '2022-12-12 14:38:42', '', NULL,
        '导出操作');
INSERT INTO `sys_dict_data`
VALUES (23, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', NULL, '0', 'admin', '2022-12-12 14:38:42', '', NULL,
        '导入操作');
INSERT INTO `sys_dict_data`
VALUES (24, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', NULL, '0', 'admin', '2022-12-12 14:38:42', '', NULL,
        '强退操作');
INSERT INTO `sys_dict_data`
VALUES (25, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', NULL, '0', 'admin', '2022-12-12 14:38:42', '',
        NULL, '生成操作');
INSERT INTO `sys_dict_data`
VALUES (26, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', NULL, '0', 'admin', '2022-12-12 14:38:42', '', NULL,
        '清空操作');
INSERT INTO `sys_dict_data`
VALUES (27, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', NULL, '0', 'admin', '2022-12-12 14:38:42', '',
        NULL, '正常状态');
INSERT INTO `sys_dict_data`
VALUES (28, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', NULL, '0', 'admin', '2022-12-12 14:38:42', '', NULL,
        '停用状态');
INSERT INTO `sys_dict_data`
VALUES (29, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', NULL, '0', 'admin', '2022-12-12 14:38:42', '', NULL,
        '其他操作');
INSERT INTO `sys_dict_data`
VALUES (1423897784372199435, 0, '百分比', '%', 'units', NULL, NULL, 'N', '常用单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199436, 0, '次', 'count', 'units', NULL, NULL, 'N', '常用单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199437, 0, '转每分钟', 'r/min', 'units', NULL, NULL, 'N', '常用单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199438, 0, '纳米', 'nm', 'units', NULL, NULL, 'N', '长度单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199439, 0, '微米', 'μm', 'units', NULL, NULL, 'N', '长度单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199440, 0, '毫米', 'mm', 'units', NULL, NULL, 'N', '长度单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199441, 0, '厘米', 'cm', 'units', NULL, NULL, 'N', '长度单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199442, 0, '米', 'm', 'units', NULL, NULL, 'N', '长度单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199443, 0, '千米', 'km', 'units', NULL, NULL, 'N', '长度单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199444, 0, '平方毫米', 'mm²', 'units', NULL, NULL, 'N', '面积单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199445, 0, '平方厘米', 'cm²', 'units', NULL, NULL, 'N', '面积单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199446, 0, '平方米', 'm²', 'units', NULL, NULL, 'N', '面积单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199447, 0, '平方千米', 'km²', 'units', NULL, NULL, 'N', '面积单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199448, 0, '公顷', 'hm²', 'units', NULL, NULL, 'N', '面积单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199449, 0, '天', 'd', 'units', NULL, NULL, 'N', '时间单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199450, 0, '小时', 'h', 'units', NULL, NULL, 'N', '时间单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199451, 0, '分钟', 'min', 'units', NULL, NULL, 'N', '时间单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199452, 0, '秒', 's', 'units', NULL, NULL, 'N', '时间单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199453, 0, '毫秒', 'ms', 'units', NULL, NULL, 'N', '时间单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199454, 0, '微秒', 'μs', 'units', NULL, NULL, 'N', '时间单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199455, 0, '纳秒', 'ns', 'units', NULL, NULL, 'N', '时间单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199456, 0, '立方毫米', 'mm³', 'units', NULL, NULL, 'N', '体积单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199457, 0, '立方厘米', 'cm³', 'units', NULL, NULL, 'N', '体积单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199458, 0, '立方米', 'm³', 'units', NULL, NULL, 'N', '体积单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199459, 0, '立方千米', 'km³', 'units', NULL, NULL, 'N', '体积单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199460, 0, '立方米每秒', 'm³/s', 'units', NULL, NULL, 'N', '流量单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199461, 0, '立方千米每秒', 'km³/s', 'units', NULL, NULL, 'N', '流量单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199462, 0, '立方厘米每秒', 'cm³/s', 'units', NULL, NULL, 'N', '流量单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199463, 0, '升每秒', 'l/s', 'units', NULL, NULL, 'N', '流量单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199464, 0, '立方米每小时', 'm³/h', 'units', NULL, NULL, 'N', '流量单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199465, 0, '立方千米每小时', 'km³/h', 'units', NULL, NULL, 'N', '流量单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199466, 0, '立方厘米每小时', 'cm³/h', 'units', NULL, NULL, 'N', '流量单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199467, 0, '升每小时', 'l/h', 'units', NULL, NULL, 'N', '流量单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199468, 0, '毫升', 'mL', 'units', NULL, NULL, 'N', '容积单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199469, 0, '升', 'L', 'units', NULL, NULL, 'N', '容积单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199470, 0, '毫克', 'mg', 'units', NULL, NULL, 'N', '质量单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199471, 0, '克', 'g', 'units', NULL, NULL, 'N', '质量单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199472, 0, '千克', 'kg', 'units', NULL, NULL, 'N', '质量单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199473, 0, '吨', 't', 'units', NULL, NULL, 'N', '质量单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199474, 0, '帕斯卡', 'Pa', 'units', NULL, NULL, 'N', '压力单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199475, 0, '千帕斯卡', 'kPa', 'units', NULL, NULL, 'N', '压力单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199476, 0, '牛顿', 'N', 'units', NULL, NULL, 'N', '力单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199477, 0, '牛·米', 'N.m', 'units', NULL, NULL, 'N', '力单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199478, 0, '开尔文', 'K', 'units', NULL, NULL, 'N', '温度单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199479, 0, '摄氏度', '℃', 'units', NULL, NULL, 'N', '温度单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199480, 0, '华氏度', '℉', 'units', NULL, NULL, 'N', '温度单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199481, 0, '焦耳', 'J', 'units', NULL, NULL, 'N', '能量单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199482, 0, '卡', 'cal', 'units', NULL, NULL, 'N', '能量单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199483, 0, '瓦特', 'W', 'units', NULL, NULL, 'N', '功率单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199484, 0, '千瓦特', 'kW', 'units', NULL, NULL, 'N', '功率单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199485, 0, '弧度', 'rad', 'units', NULL, NULL, 'N', '角度单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199486, 0, '度', '°', 'units', NULL, NULL, 'N', '角度单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199487, 0, '[角]分', '′', 'units', NULL, NULL, 'N', '角度单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199488, 0, '[角]秒', '″', 'units', NULL, NULL, 'N', '角度单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199489, 0, '赫兹', 'Hz', 'units', NULL, NULL, 'N', '频率单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199490, 0, '兆赫兹', 'MHz', 'units', NULL, NULL, 'N', '频率单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199491, 0, 'G赫兹', 'GHz', 'units', NULL, NULL, 'N', '频率单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199492, 0, '米每秒', 'm/s', 'units', NULL, NULL, 'N', '速度单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199493, 0, '千米每小时', 'km/h', 'units', NULL, NULL, 'N', '速度单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199494, 0, '节', 'kn', 'units', NULL, NULL, 'N', '速度单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199495, 0, '伏特', 'V', 'units', NULL, NULL, 'N', '电力单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199496, 0, '千伏', 'kV', 'units', NULL, NULL, 'N', '电力单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199497, 0, '毫伏', 'mV', 'units', NULL, NULL, 'N', '电力单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199498, 0, '微伏', 'μV', 'units', NULL, NULL, 'N', '电力单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199499, 0, '安培', 'A', 'units', NULL, NULL, 'N', '电力单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199500, 0, '欧姆', 'Ω', 'units', NULL, NULL, 'N', '电力单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199501, 0, '千欧', 'KΩ', 'units', NULL, NULL, 'N', '电力单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199502, 0, '兆欧', 'MΩ', 'units', NULL, NULL, 'N', '电力单位', '0', 'admin', '2022-12-12 14:38:42',
        '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199503, 0, '电子伏', 'eV', 'units', NULL, NULL, 'N', '电力单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199504, 0, '千瓦·时', 'kW·h', 'units', NULL, NULL, 'N', '电力单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1423897784372199505, 0, 'Kg标准煤', 'kgce', 'units', NULL, NULL, 'N', '能源单位', '0', 'admin',
        '2022-12-12 14:38:42', '', NULL, NULL);
INSERT INTO `sys_dict_data`
VALUES (1610907445811527682, 0, '直连设备', '1', 'device_type', NULL, 'default', 'N', '', '0', 'admin',
        '2022-12-12 14:38:42', 'admin', '2023-01-05 15:53:59', NULL);
INSERT INTO `sys_dict_data`
VALUES (1610907520189120514, 0, '网关设备', '2', 'device_type', NULL, 'default', 'N', NULL, '0', 'admin',
        '2022-12-12 14:38:42', 'admin', '2023-01-05 15:54:17', NULL);
INSERT INTO `sys_dict_data`
VALUES (1610907557791055874, 0, '网关子设备', '3', 'device_type', NULL, 'default', 'N', NULL, '0', 'admin',
        '2022-12-12 14:38:42', 'admin', '2023-01-05 15:54:26', NULL);
INSERT INTO `sys_dict_data`
VALUES (1610911522922479617, 0, '主动上报', '2', 'attr_type', NULL, 'default', 'N', NULL, '0', 'admin',
        '2022-12-12 14:38:42', 'admin', '2023-01-05 16:10:11', NULL);
INSERT INTO `sys_dict_data`
VALUES (1610911571203112961, 0, '根据单个属性预处理', '18', 'attr_type', NULL, 'default', 'N', NULL, '0', 'admin',
        '2022-12-12 14:38:42', 'admin', '2023-01-05 16:10:23', NULL);
INSERT INTO `sys_dict_data`
VALUES (1610911628254035969, 0, 'Agent 采集', '0', 'attr_type', NULL, 'default', 'N', NULL, '0', 'admin',
        '2022-12-12 14:38:42', 'admin', '2023-01-05 16:10:36', NULL);
INSERT INTO `sys_dict_data`
VALUES (1611000298143268866, 0, '浮点数', '0', 'data_type', NULL, 'default', 'N', '', '0', 'admin',
        '2023-01-05 22:02:57', 'admin', '2023-01-05 22:02:57', NULL);
INSERT INTO `sys_dict_data`
VALUES (1611000437339635714, 0, '整数', '3', 'data_type', NULL, 'default', 'N', '', '0', 'admin', '2023-01-05 22:03:30',
        'admin', '2023-01-05 22:03:30', NULL);
INSERT INTO `sys_dict_data`
VALUES (1611000515915726849, 0, '字符', '1', 'data_type', NULL, 'default', 'N', '', '0', 'admin', '2023-01-05 22:03:49',
        'admin', '2023-01-05 22:03:49', NULL);
INSERT INTO `sys_dict_data`
VALUES (1611000543556190209, 0, '文本', '4', 'data_type', NULL, 'default', 'N', '', '0', 'admin', '2023-01-05 22:03:55',
        'admin', '2023-01-05 22:03:55', NULL);
INSERT INTO `sys_dict_data`
VALUES (1622609929748291586, 0, '异步', '1', 'execute_type', NULL, 'default', 'N', '', '0', 'admin',
        '2023-02-06 22:55:29', 'admin', '2023-02-06 22:55:29', NULL);
INSERT INTO `sys_dict_data`
VALUES (1622609958416359425, 0, '同步', '0', 'execute_type', NULL, 'default', 'N', '', '0', 'admin',
        '2023-02-06 22:55:35', 'admin', '2023-02-06 22:55:35', NULL);
INSERT INTO `sys_dict_data`
VALUES (1625040016351371265, 0, '信息', '1', 'event_level', NULL, 'default', 'N', '', '0', 'admin',
        '2023-02-13 15:51:46', 'admin', '2023-02-13 15:51:46', NULL);
INSERT INTO `sys_dict_data`
VALUES (1625040071724572673, 0, '低级', '2', 'event_level', NULL, 'default', 'N', '', '0', 'admin',
        '2023-02-13 15:52:00', 'admin', '2023-02-13 15:52:00', NULL);
INSERT INTO `sys_dict_data`
VALUES (1625040104410783746, 0, '中级', '3', 'event_level', NULL, 'default', 'N', '', '0', 'admin',
        '2023-02-13 15:52:07', 'admin', '2023-02-13 15:52:07', NULL);
INSERT INTO `sys_dict_data`
VALUES (1625040144135036930, 0, '高级', '4', 'event_level', NULL, 'default', 'N', '', '0', 'admin',
        '2023-02-13 15:52:17', 'admin', '2023-02-13 15:52:17', NULL);
INSERT INTO `sys_dict_data`
VALUES (1625040176724779009, 0, '紧急', '5', 'event_level', NULL, 'default', 'N', '', '0', 'admin',
        '2023-02-13 15:52:25', 'admin', '2023-02-13 15:52:25', NULL);
INSERT INTO `sys_dict_data`
VALUES (1625410335968796674, 0, '手动触发', '0', 'service_trigger_type', NULL, 'default', 'N', '', '0', 'admin',
        '2023-02-14 16:23:17', 'admin', '2023-02-14 16:23:17', NULL);
INSERT INTO `sys_dict_data`
VALUES (1625410378977189889, 0, '告警触发', '1', 'service_trigger_type', NULL, 'default', 'N', '', '0', 'admin',
        '2023-02-14 16:23:28', 'admin', '2023-02-14 16:23:28', NULL);
INSERT INTO `sys_dict_data`
VALUES (1625410426960027650, 0, '场景触发', '2', 'service_trigger_type', NULL, 'default', 'N', '', '0', 'admin',
        '2023-02-14 16:23:39', 'admin', '2023-02-14 16:23:44', NULL);

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`
(
    `dict_id`     bigint                                                 NOT NULL COMMENT '字典主键',
    `dict_name`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '字典名称',
    `dict_type`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '字典类型',
    `status`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin      NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime                                               NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime                                               NULL DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`dict_id`) USING BTREE,
    UNIQUE INDEX `dict_type` (`dict_type` ASC) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '字典类型表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type`
VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2022-12-12 14:38:42', '', NULL, '用户性别列表');
INSERT INTO `sys_dict_type`
VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2022-12-12 14:38:42', '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type`
VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2022-12-12 14:38:42', '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type`
VALUES (4, '任务状态', 'sys_job_status', '0', 'admin', '2022-12-12 14:38:42', '', NULL, '任务状态列表');
INSERT INTO `sys_dict_type`
VALUES (5, '任务分组', 'sys_job_group', '0', 'admin', '2022-12-12 14:38:42', '', NULL, '任务分组列表');
INSERT INTO `sys_dict_type`
VALUES (6, '系统是否', 'sys_yes_no', '0', 'admin', '2022-12-12 14:38:42', '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type`
VALUES (7, '通知类型', 'sys_notice_type', '0', 'admin', '2022-12-12 14:38:42', '', NULL, '通知类型列表');
INSERT INTO `sys_dict_type`
VALUES (8, '通知状态', 'sys_notice_status', '0', 'admin', '2022-12-12 14:38:42', '', NULL, '通知状态列表');
INSERT INTO `sys_dict_type`
VALUES (9, '操作类型', 'sys_oper_type', '0', 'admin', '2022-12-12 14:38:42', '', NULL, '操作类型列表');
INSERT INTO `sys_dict_type`
VALUES (10, '系统状态', 'sys_common_status', '0', 'admin', '2022-12-12 14:38:42', '', NULL, '登录状态列表');
INSERT INTO `sys_dict_type`
VALUES (1610907287417831426, '设备类型', 'device_type', '0', 'admin', '2023-01-05 15:53:21', 'admin',
        '2023-01-05 15:53:21', NULL);
INSERT INTO `sys_dict_type`
VALUES (1610911439493578753, '产品属性类型', 'attr_type', '0', 'admin', '2023-01-05 16:09:51', 'admin',
        '2023-01-05 16:09:51', NULL);
INSERT INTO `sys_dict_type`
VALUES (1610994274699100162, '产品属性单位', 'units', '0', 'admin', '2023-01-05 21:39:01', 'admin',
        '2023-01-05 21:39:01', NULL);
INSERT INTO `sys_dict_type`
VALUES (1611000121072336897, '产品属性值类型', 'data_type', '0', 'admin', '2023-01-05 22:02:14', 'admin',
        '2023-01-05 22:02:14', NULL);
INSERT INTO `sys_dict_type`
VALUES (1622609789079724034, '服务执行方式', 'execute_type', '0', 'admin', '2023-02-06 22:54:55', 'admin',
        '2023-02-06 22:57:47', NULL);
INSERT INTO `sys_dict_type`
VALUES (1625039943030743042, '告警级别', 'event_level', '0', 'admin', '2023-02-13 15:51:29', 'admin',
        '2023-02-13 15:51:29', NULL);
INSERT INTO `sys_dict_type`
VALUES (1625410270457962498, '功能触发类型', 'service_trigger_type', '0', 'admin', '2023-02-14 16:23:02', 'admin',
        '2023-02-14 16:23:02', NULL);

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor`
(
    `info_id`     bigint                                                 NOT NULL COMMENT '访问ID',
    `user_name`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '用户账号',
    `ipaddr`      varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '登录IP地址',
    `status`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin      NULL DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
    `msg`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '提示信息',
    `access_time` datetime                                               NULL DEFAULT NULL COMMENT '访问时间',
    PRIMARY KEY (`info_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '系统访问记录'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_logininfor
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`
(
    `menu_id`     bigint                                                 NOT NULL COMMENT '菜单ID',
    `menu_name`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '菜单名称',
    `parent_id`   bigint                                                 NULL DEFAULT 0 COMMENT '父菜单ID',
    `order_num`   int                                                    NULL DEFAULT 0 COMMENT '显示顺序',
    `path`        varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '路由地址',
    `component`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '组件路径',
    `query_param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '路由参数',
    `is_frame`    int                                                    NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
    `is_cache`    int                                                    NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
    `menu_type`   char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin      NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
    `visible`     char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin      NULL DEFAULT '0' COMMENT '显示状态（0显示 1隐藏）',
    `status`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin      NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
    `perms`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '权限标识',
    `icon`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '#' COMMENT '菜单图标',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime                                               NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime                                               NULL DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '备注',
    PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '菜单权限表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu`
VALUES (1, '系统管理', 0, 1, 'system', NULL, '', 1, 0, 'M', '0', '0', '', 'system', 'admin', '2022-12-12 14:38:39', '',
        NULL, '系统管理目录');
INSERT INTO `sys_menu`
VALUES (2, '系统监控', 0, 2, 'monitor', NULL, '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', '2022-12-12 14:38:39',
        '', NULL, '系统监控目录');
INSERT INTO `sys_menu`
VALUES (3, '系统工具', 0, 3, 'tool', NULL, '', 1, 0, 'M', '0', '0', '', 'tool', 'admin', '2022-12-12 14:38:39', '',
        NULL, '系统工具目录');
INSERT INTO `sys_menu`
VALUES (4, 'PLUS官网', 0, 4, 'https://gitee.com/JavaLionLi/PanGu-Cloud-Plus', NULL, '', 0, 0, 'M', '0', '0', '',
        'guide', 'admin', '2022-12-12 14:38:39', '', NULL, '若依官网地址');
INSERT INTO `sys_menu`
VALUES (5, '测试菜单', 0, 5, 'demo', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'star', 'admin', '2022-12-16 09:51:41',
        NULL, NULL, '');
INSERT INTO `sys_menu`
VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user',
        'admin', '2022-12-12 14:38:39', '', NULL, '用户管理菜单');
INSERT INTO `sys_menu`
VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples',
        'admin', '2022-12-12 14:38:39', '', NULL, '角色管理菜单');
INSERT INTO `sys_menu`
VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table',
        'admin', '2022-12-12 14:38:39', '', NULL, '菜单管理菜单');
INSERT INTO `sys_menu`
VALUES (103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', 1, 0, 'C', '0', '0', 'system:dept:list', 'tree',
        'admin', '2022-12-12 14:38:40', '', NULL, '部门管理菜单');
INSERT INTO `sys_menu`
VALUES (104, '岗位管理', 1, 5, 'post', 'system/post/index', '', 1, 0, 'C', '0', '0', 'system:post:list', 'post',
        'admin', '2022-12-12 14:38:40', '', NULL, '岗位管理菜单');
INSERT INTO `sys_menu`
VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'dict',
        'admin', '2022-12-12 14:38:40', '', NULL, '字典管理菜单');
INSERT INTO `sys_menu`
VALUES (106, '参数设置', 1, 7, 'config', 'system/config/index', '', 1, 0, 'C', '0', '0', 'system:config:list', 'edit',
        'admin', '2022-12-12 14:38:40', '', NULL, '参数设置菜单');
INSERT INTO `sys_menu`
VALUES (107, '通知公告', 1, 8, 'notice', 'system/notice/index', '', 1, 0, 'C', '0', '0', 'system:notice:list',
        'message', 'admin', '2022-12-12 14:38:40', '', NULL, '通知公告菜单');
INSERT INTO `sys_menu`
VALUES (108, '日志管理', 1, 9, 'log', '', '', 1, 0, 'M', '0', '0', '', 'log', 'admin', '2022-12-12 14:38:40', '', NULL,
        '日志管理菜单');
INSERT INTO `sys_menu`
VALUES (109, '在线用户', 2, 1, 'online', 'monitor/online/index', '', 1, 0, 'C', '0', '0', 'monitor:online:list',
        'online', 'admin', '2022-12-12 14:38:40', '', NULL, '在线用户菜单');
INSERT INTO `sys_menu`
VALUES (110, 'XxlJob控制台', 2, 2, 'http://localhost:9900', '', '', 0, 0, 'C', '0', '0', 'monitor:job:list', 'job',
        'admin', '2022-12-12 14:38:40', '', NULL, '定时任务菜单');
INSERT INTO `sys_menu`
VALUES (111, 'Sentinel控制台', 2, 3, 'http://localhost:8718', '', '', 0, 0, 'C', '0', '0', 'monitor:sentinel:list',
        'sentinel', 'admin', '2022-12-12 14:38:40', '', NULL, '流量控制菜单');
INSERT INTO `sys_menu`
VALUES (112, 'Nacos控制台', 2, 4, 'http://localhost:8848/nacos', '', '', 0, 0, 'C', '0', '0', 'monitor:nacos:list',
        'nacos', 'admin', '2022-12-12 14:38:40', '', NULL, '服务治理菜单');
INSERT INTO `sys_menu`
VALUES (113, 'Admin控制台', 2, 5, 'http://localhost:9100/login', '', '', 0, 0, 'C', '0', '0', 'monitor:server:list',
        'server', 'admin', '2022-12-12 14:38:40', '', NULL, '服务监控菜单');
INSERT INTO `sys_menu`
VALUES (114, '表单构建', 3, 1, 'build', 'tool/build/index', '', 1, 0, 'C', '0', '0', 'tool:build:list', 'build',
        'admin', '2022-12-12 14:38:40', '', NULL, '表单构建菜单');
INSERT INTO `sys_menu`
VALUES (115, '代码生成', 3, 2, 'gen', 'tool/gen/index', '', 1, 0, 'C', '0', '0', 'tool:gen:list', 'code', 'admin',
        '2022-12-12 14:38:40', '', NULL, '代码生成菜单');
INSERT INTO `sys_menu`
VALUES (118, '文件管理', 1, 10, 'oss', 'system/oss/index', '', 1, 0, 'C', '0', '0', 'system:oss:list', 'upload',
        'admin', '2022-12-12 14:38:40', '', NULL, '文件管理菜单');
INSERT INTO `sys_menu`
VALUES (500, '操作日志', 108, 1, 'operlog', 'system/operlog/index', '', 1, 0, 'C', '0', '0', 'system:operlog:list',
        'form', 'admin', '2022-12-12 14:38:40', '', NULL, '操作日志菜单');
INSERT INTO `sys_menu`
VALUES (501, '登录日志', 108, 2, 'logininfor', 'system/logininfor/index', '', 1, 0, 'C', '0', '0',
        'system:logininfor:list', 'logininfor', 'admin', '2022-12-12 14:38:40', '', NULL, '登录日志菜单');
INSERT INTO `sys_menu`
VALUES (1001, '用户查询', 100, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1002, '用户新增', 100, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1003, '用户修改', 100, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1004, '用户删除', 100, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1005, '用户导出', 100, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1006, '用户导入', 100, 6, '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1007, '重置密码', 100, 7, '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1008, '角色查询', 101, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1009, '角色新增', 101, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1010, '角色修改', 101, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1011, '角色删除', 101, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1012, '角色导出', 101, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1013, '菜单查询', 102, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1014, '菜单新增', 102, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1015, '菜单修改', 102, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1016, '菜单删除', 102, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1017, '部门查询', 103, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1018, '部门新增', 103, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1019, '部门修改', 103, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1020, '部门删除', 103, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1021, '岗位查询', 104, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1022, '岗位新增', 104, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1023, '岗位修改', 104, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1024, '岗位删除', 104, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1025, '岗位导出', 104, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1026, '字典查询', 105, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1027, '字典新增', 105, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1028, '字典修改', 105, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1029, '字典删除', 105, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1030, '字典导出', 105, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1031, '参数查询', 106, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1032, '参数新增', 106, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1033, '参数修改', 106, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1034, '参数删除', 106, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1035, '参数导出', 106, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1036, '公告查询', 107, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1037, '公告新增', 107, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1038, '公告修改', 107, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1039, '公告删除', 107, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1040, '操作查询', 500, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:operlog:query', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1041, '操作删除', 500, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:operlog:remove', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1042, '日志导出', 500, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:operlog:export', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1043, '登录查询', 501, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:logininfor:query', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1044, '登录删除', 501, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:logininfor:remove', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1045, '日志导出', 501, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:logininfor:export', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1046, '在线查询', 109, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1047, '批量强退', 109, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1048, '单条强退', 109, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1050, '账户解锁', 501, 4, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:unlock', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1055, '生成查询', 115, 1, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1056, '生成修改', 115, 2, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1057, '生成删除', 115, 3, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1058, '导入代码', 115, 2, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1059, '预览代码', 115, 4, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1060, '生成代码', 115, 5, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1500, '测试单表', 5, 1, 'demo', 'demo/demo/index', NULL, 1, 0, 'C', '0', '0', 'demo:demo:list', '#', 'admin',
        '2022-12-16 09:51:41', '', NULL, '测试单表菜单');
INSERT INTO `sys_menu`
VALUES (1501, '测试单表查询', 1500, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'demo:demo:query', '#', 'admin',
        '2022-12-16 09:51:41', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1502, '测试单表新增', 1500, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'demo:demo:add', '#', 'admin',
        '2022-12-16 09:51:42', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1503, '测试单表修改', 1500, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'demo:demo:edit', '#', 'admin',
        '2022-12-16 09:51:42', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1504, '测试单表删除', 1500, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'demo:demo:remove', '#', 'admin',
        '2022-12-16 09:51:42', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1505, '测试单表导出', 1500, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'demo:demo:export', '#', 'admin',
        '2022-12-16 09:51:42', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1506, '测试树表', 5, 1, 'tree', 'demo/tree/index', NULL, 1, 0, 'C', '0', '0', 'demo:tree:list', '#', 'admin',
        '2022-12-16 09:51:42', '', NULL, '测试树表菜单');
INSERT INTO `sys_menu`
VALUES (1507, '测试树表查询', 1506, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'demo:tree:query', '#', 'admin',
        '2022-12-16 09:51:42', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1508, '测试树表新增', 1506, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'demo:tree:add', '#', 'admin',
        '2022-12-16 09:51:42', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1509, '测试树表修改', 1506, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'demo:tree:edit', '#', 'admin',
        '2022-12-16 09:51:43', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1510, '测试树表删除', 1506, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'demo:tree:remove', '#', 'admin',
        '2022-12-16 09:51:43', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1511, '测试树表导出', 1506, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'demo:tree:export', '#', 'admin',
        '2022-12-16 09:51:43', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1600, '文件查询', 118, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:oss:query', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1601, '文件上传', 118, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:oss:upload', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1602, '文件下载', 118, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:oss:download', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1603, '文件删除', 118, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:oss:remove', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1604, '配置添加', 118, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:oss:add', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1605, '配置编辑', 118, 6, '#', '', '', 1, 0, 'F', '0', '0', 'system:oss:edit', '#', 'admin',
        '2022-12-12 14:38:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1608751046587367425, '物联网', 0, 0, 'iot', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'client', 'admin',
        '2022-12-30 17:05:13', 'admin', '2023-01-05 14:04:06', '');
INSERT INTO `sys_menu`
VALUES (1610875007865503744, '产品', 1608751046587367425, 1, 'product', 'manager/product/index', NULL, 1, 0, 'C', '0',
        '0', 'manager:product:list', '#', 'admin', '2023-01-05 05:46:09', 'admin', '2023-01-05 14:04:32', '产品菜单');
INSERT INTO `sys_menu`
VALUES (1610875007865503745, '产品查询', 1610875007865503744, 1, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:product:query', '#', 'admin', '2023-01-05 05:46:09', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1610875007865503746, '产品新增', 1610875007865503744, 2, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:product:add', '#', 'admin', '2023-01-05 05:46:09', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1610875007865503747, '产品修改', 1610875007865503744, 3, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:product:edit', '#', 'admin', '2023-01-05 05:46:09', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1610875007865503748, '产品删除', 1610875007865503744, 4, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:product:remove', '#', 'admin', '2023-01-05 05:46:09', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1610875007865503749, '产品导出', 1610875007865503744, 5, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:product:export', '#', 'admin', '2023-01-05 05:46:09', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1610893079963811840, '产品分组', 1608751046587367425, 1, 'product_group', 'manager/product_group/index', NULL,
        1, 0, 'C', '0', '0', 'manager:product_group:list', '#', 'admin', '2023-01-05 06:57:12', '', NULL,
        '产品分组菜单');
INSERT INTO `sys_menu`
VALUES (1610893079963811841, '产品分组查询', 1610893079963811840, 1, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:product_group:query', '#', 'admin', '2023-01-05 06:57:12', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1610893079963811842, '产品分组新增', 1610893079963811840, 2, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:product_group:add', '#', 'admin', '2023-01-05 06:57:12', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1610893079963811843, '产品分组修改', 1610893079963811840, 3, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:product_group:edit', '#', 'admin', '2023-01-05 06:57:12', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1610893079963811844, '产品分组删除', 1610893079963811840, 4, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:product_group:remove', '#', 'admin', '2023-01-05 06:57:12', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1610893079963811845, '产品分组导出', 1610893079963811840, 5, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:product_group:export', '#', 'admin', '2023-01-05 06:57:12', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1610919188784791552, '设备属性', 1608751046587367425, 1, 'attribute', 'manager/attribute/index', NULL, 1, 0,
        'C', '0', '0', 'manager:attribute:list', '#', 'admin', '2023-01-05 08:42:04', '', NULL, '设备属性菜单');
INSERT INTO `sys_menu`
VALUES (1610919188784791553, '设备属性查询', 1610919188784791552, 1, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:attribute:query', '#', 'admin', '2023-01-05 08:42:04', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1610919188784791554, '设备属性新增', 1610919188784791552, 2, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:attribute:add', '#', 'admin', '2023-01-05 08:42:04', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1610919188784791555, '设备属性修改', 1610919188784791552, 3, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:attribute:edit', '#', 'admin', '2023-01-05 08:42:04', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1610919188784791556, '设备属性删除', 1610919188784791552, 4, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:attribute:remove', '#', 'admin', '2023-01-05 08:42:04', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1610919188784791557, '设备属性导出', 1610919188784791552, 5, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:attribute:export', '#', 'admin', '2023-01-05 08:42:04', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1611390509855588352, '设备', 1608751046587367425, 1, 'device', 'manager/device/index', NULL, 1, 0, 'C', '0',
        '0', 'manager:device:list', '#', 'admin', '2023-01-06 15:53:49', '', NULL, '设备菜单');
INSERT INTO `sys_menu`
VALUES (1611390509855588353, '设备查询', 1611390509855588352, 1, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:device:query', '#', 'admin', '2023-01-06 15:53:49', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1611390509855588354, '设备新增', 1611390509855588352, 2, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:device:add', '#', 'admin', '2023-01-06 15:53:49', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1611390509855588355, '设备修改', 1611390509855588352, 3, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:device:edit', '#', 'admin', '2023-01-06 15:53:49', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1611390509855588356, '设备删除', 1611390509855588352, 4, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:device:remove', '#', 'admin', '2023-01-06 15:53:49', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1611390509855588357, '设备导出', 1611390509855588352, 5, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:device:export', '#', 'admin', '2023-01-06 15:53:49', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1611394291175833600, '设备分组', 1608751046587367425, 1, 'device_group', 'manager/device_group/index', NULL, 1,
        0, 'C', '0', '0', 'manager:device_group:list', '#', 'admin', '2023-01-06 16:08:52', '', NULL, '设备分组菜单');
INSERT INTO `sys_menu`
VALUES (1611394291175833601, '设备分组查询', 1611394291175833600, 1, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:device_group:query', '#', 'admin', '2023-01-06 16:08:52', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1611394291175833602, '设备分组新增', 1611394291175833600, 2, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:device_group:add', '#', 'admin', '2023-01-06 16:08:52', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1611394291175833603, '设备分组修改', 1611394291175833600, 3, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:device_group:edit', '#', 'admin', '2023-01-06 16:08:52', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1611394291175833604, '设备分组删除', 1611394291175833600, 4, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:device_group:remove', '#', 'admin', '2023-01-06 16:08:52', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1611394291175833605, '设备分组导出', 1611394291175833600, 5, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:device_group:export', '#', 'admin', '2023-01-06 16:08:52', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1620978104546304000, '设备上下线规则', 1608751046587367425, 1, 'device_status_function',
        'manager/device_status_function/index', NULL, 1, 0, 'C', '0', '0', 'manager:device_status_function:list', '#',
        'admin', '2023-02-02 02:53:04', '', NULL, '设备上下线规则菜单');
INSERT INTO `sys_menu`
VALUES (1620978104546304001, '设备上下线规则查询', 1620978104546304000, 1, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:device_status_function:query', '#', 'admin', '2023-02-02 02:53:04', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1620978104546304002, '设备上下线规则新增', 1620978104546304000, 2, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:device_status_function:add', '#', 'admin', '2023-02-02 02:53:04', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1620978104546304003, '设备上下线规则修改', 1620978104546304000, 3, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:device_status_function:edit', '#', 'admin', '2023-02-02 02:53:04', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1620978104546304004, '设备上下线规则删除', 1620978104546304000, 4, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:device_status_function:remove', '#', 'admin', '2023-02-02 02:53:04', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1620978104546304005, '设备上下线规则导出', 1620978104546304000, 5, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:device_status_function:export', '#', 'admin', '2023-02-02 02:53:04', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1621328607855714304, '告警规则', 1608751046587367425, 1, 'product_event', 'manager/product_event/index', NULL,
        1, 0, 'C', '0', '0', 'manager:product_event:list', '#', 'admin', '2023-02-03 02:04:44', '', NULL,
        '告警规则菜单');
INSERT INTO `sys_menu`
VALUES (1621328607855714305, '告警规则查询', 1621328607855714304, 1, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:product_event:query', '#', 'admin', '2023-02-03 02:04:44', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1621328607855714306, '告警规则新增', 1621328607855714304, 2, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:product_event:add', '#', 'admin', '2023-02-03 02:04:44', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1621328607855714307, '告警规则修改', 1621328607855714304, 3, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:product_event:edit', '#', 'admin', '2023-02-03 02:04:44', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1621328607855714308, '告警规则删除', 1621328607855714304, 4, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:product_event:remove', '#', 'admin', '2023-02-03 02:04:44', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1621328607855714309, '告警规则导出', 1621328607855714304, 5, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:product_event:export', '#', 'admin', '2023-02-03 02:04:44', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1622524875093254144, '产品功能', 1608751046587367425, 1, 'product_service', 'manager/product_service/index',
        NULL, 1, 0, 'C', '0', '0', 'manager:product_service:list', '#', 'admin', '2023-02-06 09:18:33', '', NULL,
        '产品功能菜单');
INSERT INTO `sys_menu`
VALUES (1622524875093254145, '产品功能查询', 1622524875093254144, 1, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:product_service:query', '#', 'admin', '2023-02-06 09:18:33', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1622524875093254146, '产品功能新增', 1622524875093254144, 2, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:product_service:add', '#', 'admin', '2023-02-06 09:18:33', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1622524875093254147, '产品功能修改', 1622524875093254144, 3, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:product_service:edit', '#', 'admin', '2023-02-06 09:18:33', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1622524875093254148, '产品功能删除', 1622524875093254144, 4, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:product_service:remove', '#', 'admin', '2023-02-06 09:18:33', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1622524875093254149, '产品功能导出', 1622524875093254144, 5, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:product_service:export', '#', 'admin', '2023-02-06 09:18:33', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1625012324310654976, '告警规则达式', 1608751046587367425, 1, 'product_event_expression',
        'manager/product_event_expression/index', NULL, 1, 0, 'C', '0', '0', 'manager:product_event_expression:list',
        '#', 'admin', '2023-02-13 06:02:14', '', NULL, '告警规则达式菜单');
INSERT INTO `sys_menu`
VALUES (1625012324310654977, '告警规则达式查询', 1625012324310654976, 1, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:product_event_expression:query', '#', 'admin', '2023-02-13 06:02:14', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1625012324310654978, '告警规则达式新增', 1625012324310654976, 2, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:product_event_expression:add', '#', 'admin', '2023-02-13 06:02:14', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1625012324310654979, '告警规则达式修改', 1625012324310654976, 3, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:product_event_expression:edit', '#', 'admin', '2023-02-13 06:02:14', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1625012324310654980, '告警规则达式删除', 1625012324310654976, 4, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:product_event_expression:remove', '#', 'admin', '2023-02-13 06:02:14', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1625012324310654981, '告警规则达式导出', 1625012324310654976, 5, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:product_event_expression:export', '#', 'admin', '2023-02-13 06:02:14', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1625012799600795648, '告警记录', 1608751046587367425, 1, 'problem', 'manager/problem/index', NULL, 1, 0, 'C',
        '0', '0', 'manager:problem:list', '#', 'admin', '2023-02-13 06:03:53', '', NULL, '告警记录菜单');
INSERT INTO `sys_menu`
VALUES (1625012799600795649, '告警记录查询', 1625012799600795648, 1, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:problem:query', '#', 'admin', '2023-02-13 06:03:53', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1625012799600795650, '告警记录新增', 1625012799600795648, 2, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:problem:add', '#', 'admin', '2023-02-13 06:03:53', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1625012799600795651, '告警记录修改', 1625012799600795648, 3, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:problem:edit', '#', 'admin', '2023-02-13 06:03:53', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1625012799600795652, '告警记录删除', 1625012799600795648, 4, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:problem:remove', '#', 'admin', '2023-02-13 06:03:53', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1625012799600795653, '告警记录导出', 1625012799600795648, 5, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:problem:export', '#', 'admin', '2023-02-13 06:03:53', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1625313411102089216, '功能执行记录', 1608751046587367425, 1, 'service_execute_record',
        'manager/service_execute_record/index', NULL, 1, 0, 'C', '0', '0', 'manager:service_execute_record:list', '#',
        'admin', '2023-02-14 01:59:38', '', NULL, '功能执行记录菜单');
INSERT INTO `sys_menu`
VALUES (1625313411102089217, '功能执行记录查询', 1625313411102089216, 1, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:service_execute_record:query', '#', 'admin', '2023-02-14 01:59:38', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1625313411102089218, '功能执行记录新增', 1625313411102089216, 2, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:service_execute_record:add', '#', 'admin', '2023-02-14 01:59:38', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1625313411102089219, '功能执行记录修改', 1625313411102089216, 3, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:service_execute_record:edit', '#', 'admin', '2023-02-14 01:59:38', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1625313411102089220, '功能执行记录删除', 1625313411102089216, 4, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:service_execute_record:remove', '#', 'admin', '2023-02-14 01:59:38', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1625313411102089221, '功能执行记录导出', 1625313411102089216, 5, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:service_execute_record:export', '#', 'admin', '2023-02-14 01:59:38', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1630406325855526912, '协议驱动', 1608751046587367425, 1, 'driver', 'manager/driver/index', NULL, 1, 0, 'C', '0',
        '0', 'manager:driver:list', '#', 'admin', '2023-02-28 03:16:49', '', NULL, '协议驱动菜单');
INSERT INTO `sys_menu`
VALUES (1630406325855526913, '协议驱动查询', 1630406325855526912, 1, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:driver:query', '#', 'admin', '2023-02-28 03:16:49', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1630406325855526914, '协议驱动新增', 1630406325855526912, 2, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:driver:add', '#', 'admin', '2023-02-28 03:16:49', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1630406325855526915, '协议驱动修改', 1630406325855526912, 3, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:driver:edit', '#', 'admin', '2023-02-28 03:16:49', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1630406325855526916, '协议驱动删除', 1630406325855526912, 4, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:driver:remove', '#', 'admin', '2023-02-28 03:16:49', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1630406325855526917, '协议驱动导出', 1630406325855526912, 5, '#', '', NULL, 1, 0, 'F', '0', '0',
        'manager:driver:export', '#', 'admin', '2023-02-28 03:16:49', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1635467869735821312, '三方授权', 1, 1, 'token', 'system/token/index', NULL, 1, 0, 'C', '0', '0',
        'system:token:list', '#', 'admin', '2023-03-14 02:30:01', '', NULL, '三方授权菜单');
INSERT INTO `sys_menu`
VALUES (1635467869735821313, '三方授权查询', 1635467869735821312, 1, '#', '', NULL, 1, 0, 'F', '0', '0',
        'system:token:query', '#', 'admin', '2023-03-14 02:30:01', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1635467869735821314, '三方授权新增', 1635467869735821312, 2, '#', '', NULL, 1, 0, 'F', '0', '0',
        'system:token:add', '#', 'admin', '2023-03-14 02:30:01', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1635467869735821315, '三方授权修改', 1635467869735821312, 3, '#', '', NULL, 1, 0, 'F', '0', '0',
        'system:token:edit', '#', 'admin', '2023-03-14 02:30:01', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1635467869735821316, '三方授权删除', 1635467869735821312, 4, '#', '', NULL, 1, 0, 'F', '0', '0',
        'system:token:remove', '#', 'admin', '2023-03-14 02:30:01', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1635467869735821317, '三方授权导出', 1635467869735821312, 5, '#', '', NULL, 1, 0, 'F', '0', '0',
        'system:token:export', '#', 'admin', '2023-03-14 02:30:01', '', NULL, '');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`
(
    `notice_id`      bigint                                                 NOT NULL COMMENT '公告ID',
    `notice_title`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '公告标题',
    `notice_type`    char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin      NOT NULL COMMENT '公告类型（1通知 2公告）',
    `notice_content` longblob                                               NULL COMMENT '公告内容',
    `status`         char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin      NULL DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
    `create_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '创建者',
    `create_time`    datetime                                               NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '更新者',
    `update_time`    datetime                                               NULL DEFAULT NULL COMMENT '更新时间',
    `remark`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '通知公告表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice`
VALUES (1, '温馨提醒：2018-07-01 若依新版本发布啦', '2', 0xE696B0E78988E69CACE58685E5AEB9, '0', 'admin',
        '2022-12-12 14:38:43', '', NULL, '管理员');
INSERT INTO `sys_notice`
VALUES (2, '维护通知：2018-07-01 若依系统凌晨维护', '1', 0xE7BBB4E68AA4E58685E5AEB9, '0', 'admin', '2022-12-12 14:38:43',
        '', NULL, '管理员');

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`
(
    `oper_id`        bigint                                                  NOT NULL COMMENT '日志主键',
    `title`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NULL DEFAULT '' COMMENT '模块标题',
    `business_type`  int                                                     NULL DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除）',
    `method`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '方法名称',
    `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NULL DEFAULT '' COMMENT '请求方式',
    `operator_type`  int                                                     NULL DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
    `oper_name`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NULL DEFAULT '' COMMENT '操作人员',
    `dept_name`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NULL DEFAULT '' COMMENT '部门名称',
    `oper_url`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '请求URL',
    `oper_ip`        varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '主机地址',
    `oper_location`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '操作地点',
    `oper_param`     varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '请求参数',
    `json_result`    varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '返回参数',
    `status`         int                                                     NULL DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
    `error_msg`      varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '错误消息',
    `oper_time`      datetime                                                NULL DEFAULT NULL COMMENT '操作时间',
    PRIMARY KEY (`oper_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '操作日志记录'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_oss
-- ----------------------------
DROP TABLE IF EXISTS `sys_oss`;
CREATE TABLE `sys_oss`
(
    `oss_id`        bigint                                                 NOT NULL COMMENT '对象存储主键',
    `file_name`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件名',
    `original_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '原名',
    `file_suffix`   varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL DEFAULT '' COMMENT '文件后缀名',
    `url`           varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'URL地址',
    `create_time`   datetime                                               NULL     DEFAULT NULL COMMENT '创建时间',
    `create_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT '' COMMENT '上传人',
    `update_time`   datetime                                               NULL     DEFAULT NULL COMMENT '更新时间',
    `update_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT '' COMMENT '更新人',
    `service`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL DEFAULT 'minio' COMMENT '服务商',
    PRIMARY KEY (`oss_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = 'OSS对象存储表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oss
-- ----------------------------

-- ----------------------------
-- Table structure for sys_oss_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_oss_config`;
CREATE TABLE `sys_oss_config`
(
    `oss_config_id` bigint                                                 NOT NULL COMMENT '主建',
    `config_key`    varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL DEFAULT '' COMMENT '配置key',
    `access_key`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT '' COMMENT 'accessKey',
    `secret_key`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT '' COMMENT '秘钥',
    `bucket_name`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT '' COMMENT '桶名称',
    `prefix`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT '' COMMENT '前缀',
    `endpoint`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT '' COMMENT '访问站点',
    `domain`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT '' COMMENT '自定义域名',
    `is_https`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin      NULL     DEFAULT 'N' COMMENT '是否https（Y=是,N=否）',
    `region`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT '' COMMENT '域',
    `access_policy` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin      NOT NULL DEFAULT '1' COMMENT '桶权限类型(0=private 1=public 2=custom)',
    `status`        char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin      NULL     DEFAULT '1' COMMENT '状态（0=正常,1=停用）',
    `ext1`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT '' COMMENT '扩展字段',
    `create_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT '' COMMENT '创建者',
    `create_time`   datetime                                               NULL     DEFAULT NULL COMMENT '创建时间',
    `update_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL     DEFAULT '' COMMENT '更新者',
    `update_time`   datetime                                               NULL     DEFAULT NULL COMMENT '更新时间',
    `remark`        varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`oss_config_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '对象存储配置表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oss_config
-- ----------------------------
INSERT INTO `sys_oss_config`
VALUES (1, 'minio', 'pangu', 'pangu123', 'pangu', '', '127.0.0.1:9000', '', 'N', '', '1', '0', '', 'admin',
        '2022-12-12 14:38:43', 'admin', '2022-12-12 14:38:43', NULL);
INSERT INTO `sys_oss_config`
VALUES (2, 'qiniu', 'XXXXXXXXXXXXXXX', 'XXXXXXXXXXXXXXX', 'pangu', '', 's3-cn-north-1.qiniucs.com', '', 'N', '', '1',
        '1', '', 'admin', '2022-12-12 14:38:43', 'admin', '2022-12-12 14:38:43', NULL);
INSERT INTO `sys_oss_config`
VALUES (3, 'aliyun', 'XXXXXXXXXXXXXXX', 'XXXXXXXXXXXXXXX', 'pangu', '', 'oss-cn-beijing.aliyuncs.com', '', 'N', '', '1',
        '1', '', 'admin', '2022-12-12 14:38:43', 'admin', '2022-12-12 14:38:43', NULL);
INSERT INTO `sys_oss_config`
VALUES (4, 'qcloud', 'XXXXXXXXXXXXXXX', 'XXXXXXXXXXXXXXX', 'pangu-1250000000', '', 'cos.ap-beijing.myqcloud.com', '',
        'N', 'ap-beijing', '1', '1', '', 'admin', '2022-12-12 14:38:43', 'admin', '2022-12-12 14:38:43', NULL);
INSERT INTO `sys_oss_config`
VALUES (5, 'image', 'pangu', 'pangu123', 'pangu', 'image', '127.0.0.1:9000', '', 'N', '', '1', '1', '', 'admin',
        '2022-12-12 14:38:43', 'admin', '2022-12-12 14:38:43', NULL);

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`
(
    `post_id`     bigint                                                 NOT NULL COMMENT '岗位ID',
    `post_code`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '岗位编码',
    `post_name`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '岗位名称',
    `post_sort`   int                                                    NOT NULL COMMENT '显示顺序',
    `status`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin      NOT NULL COMMENT '状态（0正常 1停用）',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime                                               NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime                                               NULL DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '岗位信息表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post`
VALUES (1, 'ceo', '董事长', 1, '0', 'admin', '2022-12-12 14:38:39', '', NULL, '');
INSERT INTO `sys_post`
VALUES (2, 'se', '项目经理', 2, '0', 'admin', '2022-12-12 14:38:39', '', NULL, '');
INSERT INTO `sys_post`
VALUES (3, 'hr', '人力资源', 3, '0', 'admin', '2022-12-12 14:38:39', '', NULL, '');
INSERT INTO `sys_post`
VALUES (4, 'user', '普通员工', 4, '0', 'admin', '2022-12-12 14:38:39', '', NULL, '');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `role_id`             bigint                                                 NOT NULL COMMENT '角色ID',
    `role_name`           varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '角色名称',
    `role_key`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色权限字符串',
    `role_sort`           int                                                    NOT NULL COMMENT '显示顺序',
    `data_scope`          char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin      NULL DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
    `menu_check_strictly` tinyint(1)                                             NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
    `dept_check_strictly` tinyint(1)                                             NULL DEFAULT 1 COMMENT '部门树选择项是否关联显示',
    `status`              char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin      NOT NULL COMMENT '角色状态（0正常 1停用）',
    `del_flag`            char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin      NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '创建者',
    `create_time`         datetime                                               NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '更新者',
    `update_time`         datetime                                               NULL DEFAULT NULL COMMENT '更新时间',
    `remark`              varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '角色信息表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role`
VALUES (1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', 'admin', '2022-12-12 14:38:39', '', NULL, '超级管理员');
INSERT INTO `sys_role`
VALUES (2, '普通角色', 'common', 2, '2', 1, 1, '0', '0', 'admin', '2022-12-12 14:38:39', '', NULL, '普通角色');
INSERT INTO `sys_role`
VALUES (3, '本部门及以下', 'test1', 3, '4', 1, 1, '0', '0', 'admin', '2022-12-16 09:51:43', 'admin', NULL, NULL);
INSERT INTO `sys_role`
VALUES (4, '仅本人', 'test2', 4, '5', 1, 1, '0', '0', 'admin', '2022-12-16 09:51:43', 'admin', NULL, NULL);

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`
(
    `role_id` bigint NOT NULL COMMENT '角色ID',
    `dept_id` bigint NOT NULL COMMENT '部门ID',
    PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '角色和部门关联表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO `sys_role_dept`
VALUES (2, 100);
INSERT INTO `sys_role_dept`
VALUES (2, 101);
INSERT INTO `sys_role_dept`
VALUES (2, 105);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`
(
    `role_id` bigint NOT NULL COMMENT '角色ID',
    `menu_id` bigint NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '角色和菜单关联表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu`
VALUES (2, 1);
INSERT INTO `sys_role_menu`
VALUES (2, 2);
INSERT INTO `sys_role_menu`
VALUES (2, 3);
INSERT INTO `sys_role_menu`
VALUES (2, 4);
INSERT INTO `sys_role_menu`
VALUES (2, 100);
INSERT INTO `sys_role_menu`
VALUES (2, 101);
INSERT INTO `sys_role_menu`
VALUES (2, 102);
INSERT INTO `sys_role_menu`
VALUES (2, 103);
INSERT INTO `sys_role_menu`
VALUES (2, 104);
INSERT INTO `sys_role_menu`
VALUES (2, 105);
INSERT INTO `sys_role_menu`
VALUES (2, 106);
INSERT INTO `sys_role_menu`
VALUES (2, 107);
INSERT INTO `sys_role_menu`
VALUES (2, 108);
INSERT INTO `sys_role_menu`
VALUES (2, 109);
INSERT INTO `sys_role_menu`
VALUES (2, 110);
INSERT INTO `sys_role_menu`
VALUES (2, 111);
INSERT INTO `sys_role_menu`
VALUES (2, 112);
INSERT INTO `sys_role_menu`
VALUES (2, 113);
INSERT INTO `sys_role_menu`
VALUES (2, 114);
INSERT INTO `sys_role_menu`
VALUES (2, 115);
INSERT INTO `sys_role_menu`
VALUES (2, 116);
INSERT INTO `sys_role_menu`
VALUES (2, 500);
INSERT INTO `sys_role_menu`
VALUES (2, 501);
INSERT INTO `sys_role_menu`
VALUES (2, 1000);
INSERT INTO `sys_role_menu`
VALUES (2, 1001);
INSERT INTO `sys_role_menu`
VALUES (2, 1002);
INSERT INTO `sys_role_menu`
VALUES (2, 1003);
INSERT INTO `sys_role_menu`
VALUES (2, 1004);
INSERT INTO `sys_role_menu`
VALUES (2, 1005);
INSERT INTO `sys_role_menu`
VALUES (2, 1006);
INSERT INTO `sys_role_menu`
VALUES (2, 1007);
INSERT INTO `sys_role_menu`
VALUES (2, 1008);
INSERT INTO `sys_role_menu`
VALUES (2, 1009);
INSERT INTO `sys_role_menu`
VALUES (2, 1010);
INSERT INTO `sys_role_menu`
VALUES (2, 1011);
INSERT INTO `sys_role_menu`
VALUES (2, 1012);
INSERT INTO `sys_role_menu`
VALUES (2, 1013);
INSERT INTO `sys_role_menu`
VALUES (2, 1014);
INSERT INTO `sys_role_menu`
VALUES (2, 1015);
INSERT INTO `sys_role_menu`
VALUES (2, 1016);
INSERT INTO `sys_role_menu`
VALUES (2, 1017);
INSERT INTO `sys_role_menu`
VALUES (2, 1018);
INSERT INTO `sys_role_menu`
VALUES (2, 1019);
INSERT INTO `sys_role_menu`
VALUES (2, 1020);
INSERT INTO `sys_role_menu`
VALUES (2, 1021);
INSERT INTO `sys_role_menu`
VALUES (2, 1022);
INSERT INTO `sys_role_menu`
VALUES (2, 1023);
INSERT INTO `sys_role_menu`
VALUES (2, 1024);
INSERT INTO `sys_role_menu`
VALUES (2, 1025);
INSERT INTO `sys_role_menu`
VALUES (2, 1026);
INSERT INTO `sys_role_menu`
VALUES (2, 1027);
INSERT INTO `sys_role_menu`
VALUES (2, 1028);
INSERT INTO `sys_role_menu`
VALUES (2, 1029);
INSERT INTO `sys_role_menu`
VALUES (2, 1030);
INSERT INTO `sys_role_menu`
VALUES (2, 1031);
INSERT INTO `sys_role_menu`
VALUES (2, 1032);
INSERT INTO `sys_role_menu`
VALUES (2, 1033);
INSERT INTO `sys_role_menu`
VALUES (2, 1034);
INSERT INTO `sys_role_menu`
VALUES (2, 1035);
INSERT INTO `sys_role_menu`
VALUES (2, 1036);
INSERT INTO `sys_role_menu`
VALUES (2, 1037);
INSERT INTO `sys_role_menu`
VALUES (2, 1038);
INSERT INTO `sys_role_menu`
VALUES (2, 1039);
INSERT INTO `sys_role_menu`
VALUES (2, 1040);
INSERT INTO `sys_role_menu`
VALUES (2, 1041);
INSERT INTO `sys_role_menu`
VALUES (2, 1042);
INSERT INTO `sys_role_menu`
VALUES (2, 1043);
INSERT INTO `sys_role_menu`
VALUES (2, 1044);
INSERT INTO `sys_role_menu`
VALUES (2, 1045);
INSERT INTO `sys_role_menu`
VALUES (2, 1046);
INSERT INTO `sys_role_menu`
VALUES (2, 1047);
INSERT INTO `sys_role_menu`
VALUES (2, 1048);
INSERT INTO `sys_role_menu`
VALUES (2, 1049);
INSERT INTO `sys_role_menu`
VALUES (2, 1050);
INSERT INTO `sys_role_menu`
VALUES (2, 1051);
INSERT INTO `sys_role_menu`
VALUES (2, 1052);
INSERT INTO `sys_role_menu`
VALUES (2, 1053);
INSERT INTO `sys_role_menu`
VALUES (2, 1054);
INSERT INTO `sys_role_menu`
VALUES (2, 1055);
INSERT INTO `sys_role_menu`
VALUES (2, 1056);
INSERT INTO `sys_role_menu`
VALUES (2, 1057);
INSERT INTO `sys_role_menu`
VALUES (2, 1058);
INSERT INTO `sys_role_menu`
VALUES (2, 1059);
INSERT INTO `sys_role_menu`
VALUES (2, 1060);
INSERT INTO `sys_role_menu`
VALUES (3, 1);
INSERT INTO `sys_role_menu`
VALUES (3, 5);
INSERT INTO `sys_role_menu`
VALUES (3, 100);
INSERT INTO `sys_role_menu`
VALUES (3, 101);
INSERT INTO `sys_role_menu`
VALUES (3, 102);
INSERT INTO `sys_role_menu`
VALUES (3, 103);
INSERT INTO `sys_role_menu`
VALUES (3, 104);
INSERT INTO `sys_role_menu`
VALUES (3, 105);
INSERT INTO `sys_role_menu`
VALUES (3, 106);
INSERT INTO `sys_role_menu`
VALUES (3, 107);
INSERT INTO `sys_role_menu`
VALUES (3, 108);
INSERT INTO `sys_role_menu`
VALUES (3, 500);
INSERT INTO `sys_role_menu`
VALUES (3, 501);
INSERT INTO `sys_role_menu`
VALUES (3, 1001);
INSERT INTO `sys_role_menu`
VALUES (3, 1002);
INSERT INTO `sys_role_menu`
VALUES (3, 1003);
INSERT INTO `sys_role_menu`
VALUES (3, 1004);
INSERT INTO `sys_role_menu`
VALUES (3, 1005);
INSERT INTO `sys_role_menu`
VALUES (3, 1006);
INSERT INTO `sys_role_menu`
VALUES (3, 1007);
INSERT INTO `sys_role_menu`
VALUES (3, 1008);
INSERT INTO `sys_role_menu`
VALUES (3, 1009);
INSERT INTO `sys_role_menu`
VALUES (3, 1010);
INSERT INTO `sys_role_menu`
VALUES (3, 1011);
INSERT INTO `sys_role_menu`
VALUES (3, 1012);
INSERT INTO `sys_role_menu`
VALUES (3, 1013);
INSERT INTO `sys_role_menu`
VALUES (3, 1014);
INSERT INTO `sys_role_menu`
VALUES (3, 1015);
INSERT INTO `sys_role_menu`
VALUES (3, 1016);
INSERT INTO `sys_role_menu`
VALUES (3, 1017);
INSERT INTO `sys_role_menu`
VALUES (3, 1018);
INSERT INTO `sys_role_menu`
VALUES (3, 1019);
INSERT INTO `sys_role_menu`
VALUES (3, 1020);
INSERT INTO `sys_role_menu`
VALUES (3, 1021);
INSERT INTO `sys_role_menu`
VALUES (3, 1022);
INSERT INTO `sys_role_menu`
VALUES (3, 1023);
INSERT INTO `sys_role_menu`
VALUES (3, 1024);
INSERT INTO `sys_role_menu`
VALUES (3, 1025);
INSERT INTO `sys_role_menu`
VALUES (3, 1026);
INSERT INTO `sys_role_menu`
VALUES (3, 1027);
INSERT INTO `sys_role_menu`
VALUES (3, 1028);
INSERT INTO `sys_role_menu`
VALUES (3, 1029);
INSERT INTO `sys_role_menu`
VALUES (3, 1030);
INSERT INTO `sys_role_menu`
VALUES (3, 1031);
INSERT INTO `sys_role_menu`
VALUES (3, 1032);
INSERT INTO `sys_role_menu`
VALUES (3, 1033);
INSERT INTO `sys_role_menu`
VALUES (3, 1034);
INSERT INTO `sys_role_menu`
VALUES (3, 1035);
INSERT INTO `sys_role_menu`
VALUES (3, 1036);
INSERT INTO `sys_role_menu`
VALUES (3, 1037);
INSERT INTO `sys_role_menu`
VALUES (3, 1038);
INSERT INTO `sys_role_menu`
VALUES (3, 1039);
INSERT INTO `sys_role_menu`
VALUES (3, 1040);
INSERT INTO `sys_role_menu`
VALUES (3, 1041);
INSERT INTO `sys_role_menu`
VALUES (3, 1042);
INSERT INTO `sys_role_menu`
VALUES (3, 1043);
INSERT INTO `sys_role_menu`
VALUES (3, 1044);
INSERT INTO `sys_role_menu`
VALUES (3, 1045);
INSERT INTO `sys_role_menu`
VALUES (3, 1500);
INSERT INTO `sys_role_menu`
VALUES (3, 1501);
INSERT INTO `sys_role_menu`
VALUES (3, 1502);
INSERT INTO `sys_role_menu`
VALUES (3, 1503);
INSERT INTO `sys_role_menu`
VALUES (3, 1504);
INSERT INTO `sys_role_menu`
VALUES (3, 1505);
INSERT INTO `sys_role_menu`
VALUES (3, 1506);
INSERT INTO `sys_role_menu`
VALUES (3, 1507);
INSERT INTO `sys_role_menu`
VALUES (3, 1508);
INSERT INTO `sys_role_menu`
VALUES (3, 1509);
INSERT INTO `sys_role_menu`
VALUES (3, 1510);
INSERT INTO `sys_role_menu`
VALUES (3, 1511);
INSERT INTO `sys_role_menu`
VALUES (4, 5);
INSERT INTO `sys_role_menu`
VALUES (4, 1500);
INSERT INTO `sys_role_menu`
VALUES (4, 1501);
INSERT INTO `sys_role_menu`
VALUES (4, 1502);
INSERT INTO `sys_role_menu`
VALUES (4, 1503);
INSERT INTO `sys_role_menu`
VALUES (4, 1504);
INSERT INTO `sys_role_menu`
VALUES (4, 1505);
INSERT INTO `sys_role_menu`
VALUES (4, 1506);
INSERT INTO `sys_role_menu`
VALUES (4, 1507);
INSERT INTO `sys_role_menu`
VALUES (4, 1508);
INSERT INTO `sys_role_menu`
VALUES (4, 1509);
INSERT INTO `sys_role_menu`
VALUES (4, 1510);
INSERT INTO `sys_role_menu`
VALUES (4, 1511);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `user_id`     bigint                                                 NOT NULL COMMENT '用户ID',
    `dept_id`     bigint                                                 NULL DEFAULT NULL COMMENT '部门ID',
    `user_name`   varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '用户账号',
    `nick_name`   varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '用户昵称',
    `user_type`   varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT 'sys_user' COMMENT '用户类型（sys_user系统用户）',
    `email`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '用户邮箱',
    `phonenumber` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '手机号码',
    `sex`         char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin      NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
    `avatar`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '头像地址',
    `password`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '密码',
    `status`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin      NULL DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
    `del_flag`    char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin      NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `login_ip`    varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '最后登录IP',
    `login_date`  datetime                                               NULL DEFAULT NULL COMMENT '最后登录时间',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime                                               NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime                                               NULL DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '用户信息表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user`
VALUES (1, 103, 'admin', '超级管理', 'sys_user', 'crazyLionLi@163.com', '15888888888', '1', '',
        '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', '2022-12-12 14:38:39',
        'admin', '2022-12-12 14:38:39', '', NULL, '管理员');

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`
(
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `post_id` bigint NOT NULL COMMENT '岗位ID',
    PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '用户与岗位关联表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post`
VALUES (1, 1);
INSERT INTO `sys_user_post`
VALUES (2, 2);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`
(
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `role_id` bigint NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '用户和角色关联表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role`
VALUES (1, 1);
INSERT INTO `sys_user_role`
VALUES (2, 2);
INSERT INTO `sys_user_role`
VALUES (3, 3);
INSERT INTO `sys_user_role`
VALUES (4, 4);

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`
(
    `branch_id`     bigint                                                 NOT NULL COMMENT 'branch transaction id',
    `xid`           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'global transaction id',
    `context`       varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` longblob                                               NOT NULL COMMENT 'rollback info',
    `log_status`    int                                                    NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   datetime(6)                                            NOT NULL COMMENT 'create datetime',
    `log_modified`  datetime(6)                                            NOT NULL COMMENT 'modify datetime',
    UNIQUE INDEX `ux_undo_log` (`xid` ASC, `branch_id` ASC) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = 'AT transaction mode undo table'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of undo_log
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;

