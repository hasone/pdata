﻿SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id`          BIGINT(18)   NOT NULL AUTO_INCREMENT,
  `enter_id`    BIGINT(18)   NOT NULL,
  `owner_id`    BIGINT(18)   NOT NULL,
  `product_id`  BIGINT(18)   NOT NULL,
  `count`       FLOAT(12, 2) NOT NULL,
  `min_count`   FLOAT(12, 2) NOT NULL,
  `create_time` DATETIME     NOT NULL
  COMMENT '创建时间',
  `update_time` DATETIME              DEFAULT NULL
  COMMENT '更新时间',
  `delete_flag` INT(1)       NOT NULL DEFAULT '0'
  COMMENT '删除标记， 0:未删除；1：已删除',
  `version`     INT(8)       NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of account
-- ----------------------------

-- ----------------------------
-- Table structure for account_change_approval_record
-- ----------------------------
DROP TABLE IF EXISTS `account_change_approval_record`;
CREATE TABLE `account_change_approval_record` (
  `id`                        BIGINT(18)  NOT NULL AUTO_INCREMENT,
  `account_change_request_id` BIGINT(18)  NOT NULL,
  `operator_id`               BIGINT(18)  NOT NULL,
  `operator_comment`          VARCHAR(256)         DEFAULT NULL,
  `serial_num`                VARCHAR(64) NOT NULL,
  `operator_result`           INT(8)      NOT NULL
  COMMENT '0为提交申请，1为申请通过，2为驳回申请',
  `create_time`               DATETIME    NOT NULL
  COMMENT '创建时间',
  `update_time`               DATETIME             DEFAULT NULL
  COMMENT '更新时间',
  `delete_flag`               INT(1)      NOT NULL DEFAULT '0'
  COMMENT '删除标记， 0:未删除；1：已删除',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of account_change_approval_record
-- ----------------------------

-- ----------------------------
-- Table structure for account_change_request
-- ----------------------------
DROP TABLE IF EXISTS `account_change_request`;
CREATE TABLE `account_change_request` (
  `id`          BIGINT(18)   NOT NULL AUTO_INCREMENT,
  `count`       FLOAT(12, 2) NOT NULL,
  `account_id`  BIGINT(18)   NOT NULL,
  `ent_id`      BIGINT(18)   NOT NULL,
  `prd_id`      BIGINT(18)   NOT NULL,
  `creator_id`  BIGINT(18)   NOT NULL,
  `desc`        VARCHAR(256) NOT NULL,
  `serial_num`  VARCHAR(64)  NOT NULL,
  `status`      INT(8)       NOT NULL
  COMMENT '0为草稿， 1为已提交，2为审批中，3为审批通过，4为审批驳回，5为取消申请',
  `create_time` DATETIME     NOT NULL
  COMMENT '创建时间',
  `update_time` DATETIME              DEFAULT NULL
  COMMENT '更新时间',
  `delete_flag` INT(1)       NOT NULL DEFAULT '0'
  COMMENT '删除标记， 0:未删除；1：已删除',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of account_change_request
-- ----------------------------

-- ----------------------------
-- Table structure for account_record
-- ----------------------------
DROP TABLE IF EXISTS `account_record`;
CREATE TABLE `account_record` (
  `id`          BIGINT(18)   NOT NULL AUTO_INCREMENT
  COMMENT '现金帐户记录标识',
  `enter_id`    BIGINT(18)   NOT NULL,
  `owner_id`    BIGINT(18)   NOT NULL
  COMMENT '源用户ID',
  `account_id`  BIGINT(18)   NOT NULL
  COMMENT '源帐户ID',
  `type`        TINYINT(4)   NOT NULL
  COMMENT '操作类型，0代表收入，1代表支出',
  `serial_num`  VARCHAR(64)  NOT NULL
  COMMENT '操作流水号',
  `count`       FLOAT(12, 2) NOT NULL
  COMMENT '变化数量',
  `app_key`     VARCHAR(50) COMMENT '发起操作的应用标识',
  `description` VARCHAR(255)          DEFAULT NULL
  COMMENT '描述',
  `create_time` TIMESTAMP    NULL     DEFAULT NULL
  COMMENT '创建时间',
  `update_time` TIMESTAMP    NULL     DEFAULT NULL
  COMMENT '更新时间',
  `delete_flag` TINYINT(4)            DEFAULT NULL
  COMMENT '删除标记，0为未删除，1为已删除',
  PRIMARY KEY (`id`),
  KEY `guid` (`enter_id`) USING BTREE,
  KEY `user_guid` (`owner_id`) USING BTREE,
  KEY `account_guid` (`account_id`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '用户的现金帐户变化记录表';


DROP TABLE IF EXISTS `activity_prize`;
CREATE TABLE `activity_prize` (
  `id`            BIGINT(18)   NOT NULL AUTO_INCREMENT,
  `id_prefix`     VARCHAR(18)  NOT NULL
  COMMENT '前缀，从0开始计数',
  `rank_name`     VARCHAR(64)  NOT NULL
  COMMENT '几等奖',
  `enterprise_id` BIGINT(18)   NOT NULL
  COMMENT '企业ID',
  `product_id`    BIGINT(18)   NOT NULL
  COMMENT '产品ID',
  `count`         BIGINT(18)   NOT NULL
  COMMENT '奖品数量',
  `probability`   VARCHAR(18)           DEFAULT NULL
  COMMENT '中奖概率',
  `activity_id`   VARCHAR(225) NOT NULL
  COMMENT '活动id',
  `create_time`   DATETIME     NOT NULL
  COMMENT '创建时间',
  `update_time`   DATETIME     NOT NULL
  COMMENT '更新时间',
  `delete_flag`   INT(1)                DEFAULT '0'
  COMMENT '删除标记：0未删除；1已删除',
  `prize_name`    VARCHAR(255)          DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 14
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of activity_prize
-- ----------------------------

-- ----------------------------
-- Table structure for activity_record
-- ----------------------------
DROP TABLE IF EXISTS `activity_record`;
CREATE TABLE `activity_record` (
  `id`                 BIGINT(20)  NOT NULL AUTO_INCREMENT,
  `ent_id`             BIGINT(18)  NOT NULL,
  `activity_name`      VARCHAR(50)          DEFAULT NULL
  COMMENT '活动名称',
  `create_time`        DATETIME    NOT NULL,
  `update_time`        DATETIME    NOT NULL,
  `activity_type`      INT(11)     NOT NULL
  COMMENT '活动类型',
  `activity_type_name` VARCHAR(20) NOT NULL
  COMMENT '活动类型的名称',
  `rule_id`            BIGINT(18)  NOT NULL
  COMMENT '活动具体配置信息ruleId',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 16
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of activity_record
-- ----------------------------

-- ----------------------------
-- Table structure for administer
-- ----------------------------
DROP TABLE IF EXISTS `administer`;
CREATE TABLE `administer` (
  `user_name`    VARCHAR(64)          DEFAULT NULL
  COMMENT '用户名',
  `password`     VARCHAR(64)          DEFAULT NULL
  COMMENT '用户密码',
  `mobile_phone` VARCHAR(11) NOT NULL
  COMMENT '用户手机号码',
  `creator_id`   BIGINT(18)           DEFAULT NULL
  COMMENT '创建者ID， admin时为null， 其它不能为null',
  `pic_url`      VARCHAR(128)         DEFAULT ''
  COMMENT '头像url',
  `email`        VARCHAR(64)          DEFAULT NULL
  COMMENT '邮箱地址',
  `create_time`  DATETIME    NOT NULL
  COMMENT '创建时间',
  `update_time`  DATETIME             DEFAULT NULL
  COMMENT '更新时间',
  `delete_flag`  INT(1)      NOT NULL DEFAULT '0'
  COMMENT '删除标记， 0:未删除；1：已删除',
  `citys`        VARCHAR(30)          DEFAULT NULL
  COMMENT '本项目中该字段无效',
  `id`           BIGINT(18)  NOT NULL AUTO_INCREMENT
  COMMENT '用户ID',
  `code`         VARCHAR(59)          DEFAULT NULL
  COMMENT '工号（或其他编码信息）',
  PRIMARY KEY (`id`),
  KEY `name_index` (`delete_flag`, `user_name`) USING BTREE,
  KEY `mobile_phone_index` (`mobile_phone`, `delete_flag`) USING BTREE,
  KEY `all_index` (`delete_flag`) USING BTREE,
  KEY `id_phone_index` (`mobile_phone`, `delete_flag`, `id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1119
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of administer
-- ----------------------------
INSERT INTO `administer` VALUES
  ('admin', 'e10adc3949ba59abbe56e057f20f883e', '18867103685', '0', NULL, '595692190@qq.com',
            '2015-01-27 17:38:25',
            '2015-05-15 17:08:05', '0', NULL, '1', NULL);

-- ----------------------------
-- Table structure for admin_district
-- ----------------------------
DROP TABLE IF EXISTS `admin_district`;
CREATE TABLE `admin_district` (
  `id`          BIGINT(18) NOT NULL AUTO_INCREMENT,
  `admin_id`    BIGINT(18) NOT NULL,
  `district_id` BIGINT(18) NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 504
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of admin_district
-- ----------------------------

-- ----------------------------
-- Table structure for admin_enter
-- ----------------------------
DROP TABLE IF EXISTS `admin_enter`;
CREATE TABLE `admin_enter` (
  `admin_id` BIGINT(18) NOT NULL
  COMMENT '用户ID',
  `enter_id` BIGINT(18) NOT NULL
  COMMENT '企业ID',
  KEY `admin_enter_index` (`admin_id`, `enter_id`) USING BTREE,
  KEY `admin_index` (`admin_id`) USING BTREE,
  KEY `enter_index` (`enter_id`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of admin_enter
-- ----------------------------

-- ----------------------------
-- Table structure for admin_manager_enter
-- ----------------------------
DROP TABLE IF EXISTS `admin_manager_enter`;
CREATE TABLE `admin_manager_enter` (
  `admin_id` BIGINT(18) NOT NULL,
  `enter_id` BIGINT(18) NOT NULL
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of admin_manager_enter
-- ----------------------------

-- ----------------------------
-- Table structure for admin_role
-- ----------------------------
DROP TABLE IF EXISTS `admin_role`;
CREATE TABLE `admin_role` (
  `role_id`  BIGINT(18) NOT NULL
  COMMENT '角色ID',
  `admin_id` BIGINT(18) NOT NULL
  COMMENT '用户ID',
  PRIMARY KEY (`admin_id`),
  KEY `admin_index` (`admin_id`) USING BTREE,
  KEY `role_index` (`role_id`) USING BTREE,
  KEY `admin_role_index` (`role_id`, `admin_id`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of admin_role
-- ----------------------------
INSERT INTO `admin_role` VALUES ('1', '1');

-- ----------------------------
-- Table structure for app_info
-- ----------------------------
DROP TABLE IF EXISTS `app_info`;
CREATE TABLE `app_info` (
  `id`              BIGINT(18)         NOT NULL AUTO_INCREMENT,
  `name`            VARCHAR(255)
                    CHARACTER SET utf8          DEFAULT '名称',
  `enterprise_id`   BIGINT(18)         NOT NULL,
  `enterprise_code` VARCHAR(20)
                    CHARACTER SET utf8 NOT NULL
  COMMENT '企业代码',
  `app_key`         VARCHAR(64)
                    CHARACTER SET utf8          DEFAULT NULL
  COMMENT '应用key',
  `app_secret`      VARCHAR(64)
                    CHARACTER SET utf8          DEFAULT NULL
  COMMENT '应用密钥',
  `status`          INT(1)                      DEFAULT NULL
  COMMENT '应用审核状态：0审核中，1正常，2 注销',
  `delete_flag`     INT(1)                      DEFAULT NULL
  COMMENT '0:正常 1:删除',
  `version`         INT(11)                     DEFAULT '0',
  `create_time`     DATETIME                    DEFAULT NULL,
  `update_time`     DATETIME                    DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 12
  DEFAULT CHARSET = gbk
  ROW_FORMAT = FIXED;

-- ----------------------------
-- Records of app_info
-- ----------------------------

-- ----------------------------
-- Table structure for authority
-- ----------------------------
DROP TABLE IF EXISTS `authority`;
CREATE TABLE `authority` (
  `AUTHORITY_ID`   BIGINT(18)  NOT NULL AUTO_INCREMENT
  COMMENT '权限ID',
  `PARENT_ID`      BIGINT(18)           DEFAULT NULL
  COMMENT '本项目中该字段无效',
  `NAME`           VARCHAR(20) NOT NULL
  COMMENT '权限名称',
  `AUTHORITY_NAME` VARCHAR(50) NOT NULL
  COMMENT '提供给spring_security使用，使用ROLE_作为开头',
  `CODE`           VARCHAR(6)  NOT NULL
  COMMENT '权限Code，唯一',
  `AUTHORITY_URL`  VARCHAR(255)         DEFAULT NULL
  COMMENT '权限对应的URL',
  `CREATE_TIME`    DATETIME    NOT NULL
  COMMENT '创建时间',
  `CREATOR`        VARCHAR(20)          DEFAULT NULL
  COMMENT '创建者ID',
  `UPDATE_USER`    VARCHAR(20)          DEFAULT NULL
  COMMENT '更新者ID',
  `UPDATE_TIME`    DATETIME             DEFAULT NULL
  COMMENT '更新时间',
  `DELETE_FLAG`    INT(1)      NOT NULL DEFAULT '0'
  COMMENT '删除Flag，0:未删除；1：已删除',
  PRIMARY KEY (`AUTHORITY_ID`),
  KEY `name_index` (`NAME`, `DELETE_FLAG`) USING BTREE,
  KEY `code_index` (`CODE`, `DELETE_FLAG`) USING BTREE,
  KEY `id_index` (`AUTHORITY_ID`, `DELETE_FLAG`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 502
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of authority
-- ----------------------------
INSERT INTO `authority`
VALUES ('1', NULL, '用户管理', 'ROLE_USER', '101001', '', '2016-02-18 10:31:57', '', '',
             '2016-02-18 10:31:59', '0');
INSERT INTO `authority`
VALUES ('2', NULL, '角色管理', 'ROLE_ROLE', '101002', '', '2016-02-18 10:33:15', '', '',
             '2016-02-18 10:33:17', '0');
INSERT INTO `authority`
VALUES ('3', NULL, '账户管理', 'ROLE_USER_INFO', '101003', '', '2016-02-18 10:33:56', '', '',
             '2016-02-18 10:33:58', '0');
INSERT INTO `authority`
VALUES ('4', NULL, '产品管理', 'ROLE_PRODUCT', '102001', '', '2016-02-18 11:08:15', '', '',
             '2016-02-18 11:08:17', '0');
INSERT INTO `authority`
VALUES ('5', NULL, '企业列表', 'ROLE_ENTERPRISE', '103001', '', '2016-02-18 11:09:44', '', '',
             '2016-02-18 11:09:46', '0');
INSERT INTO `authority` VALUES
  ('6', NULL, '企业统计', 'ROLE_STATISTIC_ENTERPRISE', '105002', '', '2016-02-18 11:10:33', '', '',
        '2016-02-18 11:10:35',
        '0');
INSERT INTO `authority` VALUES
  ('7', NULL, '合作伙伴开户', 'ROLE_ENTERPRISE_NEW', '103003', '', '2016-02-22 10:09:42', '', '',
        '2016-02-22 10:09:46', '0');
INSERT INTO `authority`
VALUES ('8', NULL, '普通赠送', 'ROLE_GIVE', '104001', '', '2016-02-23 18:37:48', '', '',
             '2016-02-23 18:37:50', '0');
INSERT INTO `authority`
VALUES ('9', NULL, '包月赠送', 'ROLE_MONTH_GIVE', '104002', '', '2016-02-23 15:24:40', '', '',
             '2016-02-23 15:24:42', '0');
INSERT INTO `authority`
VALUES ('40', NULL, '红包管理', 'ROLE_REDPACKET', '104003', '', '2015-02-03 18:42:31', '', '',
              '2015-02-03 18:42:34', '0');
INSERT INTO `authority` VALUES
  ('300', NULL, '对账管理', 'ROLE_CHECK_ACCOUNT', '104001', '', '2015-11-21 23:51:43', '', '',
          '2015-12-11 14:47:55', '0');
INSERT INTO `authority` VALUES
  ('310', NULL, '对账记录', 'ROLE_CHECK_RECORD', '104002', '', '2015-11-29 12:02:46', '', '',
          '2015-11-29 12:03:04', '0');
INSERT INTO `authority` VALUES
  ('360', NULL, '大转盘', 'ROLE_LOTTERY_TEMPLATE', '104005', NULL, '2015-12-28 20:42:45', NULL, NULL,
          '2015-12-28 20:42:48', '0');
INSERT INTO `authority` VALUES
  ('392', NULL, '订购信息查询', 'ROLE_PRODUCT_ORDER', '102002', '', '2016-03-04 15:09:40', '', '',
          '2016-03-04 15:09:44',
          '0');
INSERT INTO `authority` VALUES
  ('393', NULL, '月报下载', 'ROLE_CHECK_MONTH', '104004', '', '2016-03-07 09:27:59', '', '',
          '2016-03-07 09:28:01', '0');
INSERT INTO `authority` VALUES
  ('394', NULL, '人员统计', 'ROLE_STATISTIC_USER', '105001', '', '2016-03-15 16:30:33', '', '',
          '2016-03-15 16:30:37', '0');
INSERT INTO `authority` VALUES
  ('395', NULL, '充值统计', 'ROLE_STATISTIC_CHARGE', '105003', '', '2016-03-17 11:27:44', '', '',
          '2016-03-17 11:27:46',
          '0');
INSERT INTO `authority` VALUES
  ('396', NULL, '活动统计', 'ROLE_STATISTIC_ACTIVITY', '105004', '', '2016-03-28 13:35:49', '', '',
          '2016-03-28 13:35:51',
          '0');
INSERT INTO `authority` VALUES
  ('397', NULL, '潜在客户', 'ROLE_POTENTIAL_CUSTOMER', '106001', '', '2016-03-28 20:16:25', '', '',
          '2016-03-28 20:16:25',
          '0');
INSERT INTO `authority` VALUES
  ('398', NULL, '充值报表', 'ROLE_CHARGE_TABLE', '105005', '', '2016-03-30 22:24:33', '', '',
          '2016-03-30 22:24:36', '0');
INSERT INTO `authority` VALUES
  ('399', NULL, '短信模板管理', 'ROLE_ENTERPRISE_SMS_TEMPLATE', '103004', NULL, '2016-04-08 14:06:11',
          NULL, NULL,
          '2016-04-08 14:06:16', '0');
INSERT INTO `authority` VALUES
  ('400', NULL, '设置短信模板', 'ROLE_ENTERPRISE_SME_TEMPLATE_SET', '103005', NULL, '2016-04-08 14:52:09',
          NULL, NULL,
          '2016-04-08 14:52:11', '0');
INSERT INTO `authority` VALUES
  ('401', NULL, '一级企业审批权', 'ROLE_ENTERPRISE_VERIFY_ONE', '103006', NULL, '2016-04-15 17:23:35',
          NULL, NULL,
          '2016-04-15 17:23:37', '0');
INSERT INTO `authority` VALUES
  ('402', NULL, '二级企业审批权', 'ROLE_ENTERPRISE_VERIFY_TWO', '103007', NULL, '2016-04-15 17:24:21',
          NULL, NULL,
          '2016-04-15 17:24:23', '0');
INSERT INTO `authority` VALUES
  ('403', NULL, '三级企业审批权', 'ROLE_ENTERPRISE_VERIFY_THREE', '103008', NULL, '2016-04-15 17:25:19',
          NULL, NULL,
          '2016-04-15 17:25:21', '0');
INSERT INTO `authority` VALUES
  ('404', NULL, '企业审批列表', 'ROLE_ENTERPRISE_VERIFY_LIST', '103009', NULL, '2016-04-15 17:27:12',
          NULL, NULL,
          '2016-04-15 17:27:15', '0');
INSERT INTO `authority` VALUES
  ('500', NULL, '企业充值申请', 'ROLE_ACCOUNT_CHANGE_REQUEST', '100600', NULL, '2016-04-20 08:52:33',
          NULL, NULL,
          '2016-04-20 08:52:30', '0');
INSERT INTO `authority` VALUES
  ('501', NULL, '企业充值审批', 'ROLE_ACCOUNT_CHANGE_APPROVAL', '100601', NULL, '2016-04-20 08:53:17',
          NULL, NULL,
          '2016-04-20 08:53:20', '0');

-- ----------------------------
-- Table structure for benefit_grade
-- ----------------------------
DROP TABLE IF EXISTS `benefit_grade`;
CREATE TABLE `benefit_grade` (
  `id`          BIGINT(18)   NOT NULL AUTO_INCREMENT,
  `down_limit`  INT(16)      NOT NULL,
  `upper_limit` INT(16)      NOT NULL,
  `grade`       VARCHAR(100) NOT NULL DEFAULT '',
  `description` VARCHAR(100)          DEFAULT NULL
  COMMENT '效益区间描述',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of benefit_grade
-- ----------------------------

-- ----------------------------
-- Table structure for black_and_white_list
-- ----------------------------
DROP TABLE IF EXISTS `black_and_white_list`;
CREATE TABLE `black_and_white_list` (
  `id`            BIGINT(16)               NOT NULL AUTO_INCREMENT,
  `activity_id`   BIGINT(18)               NOT NULL
  COMMENT '活动id',
  `activity_type` VARCHAR(2)               NOT NULL,
  `phone`         VARCHAR(11)              NOT NULL,
  `isWhite_flag`  INT(1)                   NOT NULL
  COMMENT '判断是否为白名单：1白名单；2黑名单',
  `delete_flag`   INT(1) UNSIGNED ZEROFILL NOT NULL DEFAULT '0'
  COMMENT '0逻辑未删除；1逻辑已删除',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = gbk;

-- ----------------------------
-- Records of black_and_white_list
-- ----------------------------

-- ----------------------------
-- Table structure for business_type
-- ----------------------------
DROP TABLE IF EXISTS `business_type`;
CREATE TABLE `business_type` (
  `id`   BIGINT(18) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100)        DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of business_type
-- ----------------------------

-- ----------------------------
-- Table structure for charge_record
-- ----------------------------
DROP TABLE IF EXISTS `charge_record`;
CREATE TABLE `charge_record` (
  `id`            BIGINT(18)  NOT NULL AUTO_INCREMENT,
  `prd_id`        BIGINT(18)  NOT NULL,
  `enter_id`      BIGINT(18)  NOT NULL,
  `type_code`     INT(10)     NOT NULL,
  `record_id`     BIGINT(18)  NOT NULL,
  `charge_time`   DATETIME             DEFAULT NULL,
  `status`        INT(11)     NOT NULL,
  `error_message` VARCHAR(100)         DEFAULT NULL,
  `type`          VARCHAR(50) NOT NULL,
  `phone`         VARCHAR(11) NOT NULL,
  `aName`         VARCHAR(100)         DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 328313
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of charge_record
-- ----------------------------

-- ----------------------------
-- Table structure for coupon_transfer
-- ----------------------------
DROP TABLE IF EXISTS `coupon_transfer`;
CREATE TABLE `coupon_transfer` (
  `id`          BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `record_id`   BIGINT(20)   NOT NULL,
  `mobile_from` VARCHAR(100) NOT NULL,
  `mobile_to`   VARCHAR(13)  NOT NULL,
  `create_time` DATETIME     NOT NULL,
  `type`        INT(4)       NOT NULL
  COMMENT '类型：充值0，赠送1',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of coupon_transfer
-- ----------------------------

-- ----------------------------
-- Table structure for customer_type
-- ----------------------------
DROP TABLE IF EXISTS `customer_type`;
CREATE TABLE `customer_type` (
  `id`   BIGINT(18)   NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of customer_type
-- ----------------------------
INSERT INTO `customer_type` VALUES ('1', '行业');

-- ----------------------------
-- Table structure for discount
-- ----------------------------
DROP TABLE IF EXISTS `discount`;
CREATE TABLE `discount` (
  `id`       BIGINT(18)  NOT NULL,
  `name`     VARCHAR(50) NOT NULL,
  `discount` DOUBLE(10, 2) DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of discount
-- ----------------------------
INSERT INTO `discount` VALUES ('1', '无折扣', '1.00');
INSERT INTO `discount` VALUES ('2', '9.9折', '0.99');
INSERT INTO `discount` VALUES ('3', '9.8折', '0.98');
INSERT INTO `discount` VALUES ('4', '9.7折', '0.97');
INSERT INTO `discount` VALUES ('5', '9.6折', '0.96');
INSERT INTO `discount` VALUES ('6', '9.5折', '0.95');

-- ----------------------------
-- Table structure for district
-- ----------------------------
DROP TABLE IF EXISTS `district`;
CREATE TABLE `district` (
  `id`        BIGINT(18)  NOT NULL AUTO_INCREMENT,
  `name`      VARCHAR(50) NOT NULL,
  `level`     INT(16)     NOT NULL
  COMMENT '表示该地区层级：1-省，2-地市，3-区县',
  `parent_id` INT(18)              DEFAULT NULL,
  `code`      VARCHAR(10)          DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 125
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for enterprises
-- ----------------------------
DROP TABLE IF EXISTS `enterprises`;
CREATE TABLE `enterprises` (
  `id`               BIGINT(18)   NOT NULL AUTO_INCREMENT
  COMMENT '企业ID',
  `name`             VARCHAR(255) NOT NULL
  COMMENT '企业名称',
  `code`             VARCHAR(20)  NOT NULL
  COMMENT '企业代码',
  `phone`            VARCHAR(50)  NOT NULL
  COMMENT '企业手机号码',
  `email`            VARCHAR(100)          DEFAULT NULL,
  `create_time`      DATETIME     NOT NULL
  COMMENT '创建时间',
  `update_time`      DATETIME              DEFAULT NULL
  COMMENT '更新时间',
  `delete_flag`      INT(1)       NOT NULL DEFAULT '0'
  COMMENT '删除Flag，0:未删除；1：已删除; 2:暂停;3-下线(BOSS关停)；4-等待市级管员审核；5-等待省级管理员审核',
  `creator_id`       BIGINT(18)            DEFAULT NULL,
  `app_secret`       VARCHAR(64)           DEFAULT '',
  `ent_name`         VARCHAR(255)          DEFAULT NULL
  COMMENT '企业品牌',
  `district_id`      BIGINT(18)            DEFAULT NULL
  COMMENT '企业所属地区id',
  `customer_type_id` BIGINT(18)            DEFAULT NULL
  COMMENT '客户分类id',
  `benefit_grade_id` BIGINT(18)            DEFAULT NULL
  COMMENT '效益标识id',
  `discount`         BIGINT(18)            DEFAULT '100'
  COMMENT '折扣信息关联到折扣表id',
  `business_type_id` BIGINT(18)            DEFAULT NULL
  COMMENT '业务类别',
  `pay_type_id`      BIGINT(18)            DEFAULT NULL
  COMMENT '支付方式',
  `interface`        INT(10)      NOT NULL DEFAULT '1'
  COMMENT '是否开通接口调用：1-是，0-否',
  `start_time`       DATETIME              DEFAULT NULL
  COMMENT '合作开始时间',
  `end_time`         DATETIME              DEFAULT NULL
  COMMENT '合作结束时间',
  `app_key`          VARCHAR(64)           DEFAULT NULL
  COMMENT '企业appKey',
  PRIMARY KEY (`id`),
  KEY `name_index` (`name`(191), `delete_flag`) USING BTREE,
  KEY `code_index` (`code`, `delete_flag`) USING BTREE,
  KEY `all_index` (`delete_flag`) USING BTREE,
  KEY `createor_index` (`delete_flag`, `creator_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 103
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of enterprises
-- ----------------------------

-- ----------------------------
-- Table structure for enterprise_account
-- ----------------------------
DROP TABLE IF EXISTS `enterprise_account`;
CREATE TABLE `enterprise_account` (
  `id`          BIGINT(18) NOT NULL AUTO_INCREMENT
  COMMENT '记录ID',
  `ent_id`      BIGINT(18) NOT NULL
  COMMENT '企业ID',
  `prd_id`      BIGINT(18) NOT NULL
  COMMENT '流量包ID',
  `account`     BIGINT(18) NOT NULL
  COMMENT '企业当前流量包个数',
  `create_time` DATETIME   NOT NULL
  COMMENT '创建时间',
  `update_time` DATETIME            DEFAULT NULL,
  `delete_flag` INT(1)     NOT NULL
  COMMENT '删除标记，0:未删除；1：已删除',
  `version`     BIGINT(18) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `ent_prd_index` (`ent_id`, `prd_id`) USING BTREE,
  KEY `ent_index` (`ent_id`, `delete_flag`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of enterprise_account
-- ----------------------------

-- ----------------------------
-- Table structure for enterprise_approval_opinion
-- ----------------------------
DROP TABLE IF EXISTS `enterprise_approval_opinion`;
CREATE TABLE `enterprise_approval_opinion` (
  `id`          BIGINT(18) NOT NULL AUTO_INCREMENT,
  `enter_id`    BIGINT(18) NOT NULL,
  `admin_id`    BIGINT(18) NOT NULL,
  `content`     VARCHAR(310)        DEFAULT NULL,
  `create_time` DATETIME   NOT NULL,
  `level`       INT(2)     NOT NULL,
  `isPass`      INT(2)     NOT NULL,
  `isLast`      INT(2)              DEFAULT NULL,
  `isNew`       INT(2)              DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 32
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of enterprise_approval_opinion
-- ----------------------------

-- ----------------------------
-- Table structure for enterprise_file
-- ----------------------------
DROP TABLE IF EXISTS `enterprise_file`;
CREATE TABLE `enterprise_file` (
  `ent_id`            BIGINT(18) NOT NULL,
  `customerfile_name` VARCHAR(100) DEFAULT NULL,
  `image_name`        VARCHAR(100) DEFAULT NULL,
  `contract_name`     VARCHAR(100) DEFAULT NULL,
  `create_time`       DATETIME   NOT NULL,
  `update_time`       DATETIME   NOT NULL
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of enterprise_file
-- ----------------------------

-- ----------------------------
-- Table structure for enterprise_money_account
-- ----------------------------
DROP TABLE IF EXISTS `enterprise_money_account`;
CREATE TABLE `enterprise_money_account` (
  `id`          BIGINT(18) NOT NULL,
  `enter_id`    BIGINT(18) NOT NULL,
  `money`       DECIMAL(18, 0) DEFAULT NULL,
  `boss_money`  DECIMAL(10, 0) DEFAULT NULL,
  `update_time` DATETIME   NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of enterprise_money_account
-- ----------------------------

-- ----------------------------
-- Table structure for enterprise_sms_template
-- ----------------------------
DROP TABLE IF EXISTS `enterprise_sms_template`;
CREATE TABLE `enterprise_sms_template` (
  `enter_id`        BIGINT(18) NOT NULL,
  `sms_template_id` BIGINT(18) NOT NULL,
  `status`          INT(2)     NOT NULL DEFAULT '0'
  COMMENT '标识位：启用该模板则设为1'
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of enterprise_sms_template
-- ----------------------------

-- ----------------------------
-- Table structure for enterprise_user_id
-- ----------------------------
DROP TABLE IF EXISTS `enterprise_user_id`;
CREATE TABLE `enterprise_user_id` (
  `id`          BIGINT(18)  NOT NULL AUTO_INCREMENT,
  `code`        VARCHAR(50) NOT NULL,
  `user_id`     VARCHAR(50) NOT NULL,
  `create_time` DATETIME    NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 93
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of enterprise_user_id
-- ----------------------------

-- ----------------------------
-- Table structure for ent_product
-- ----------------------------
DROP TABLE IF EXISTS `ent_product`;
CREATE TABLE `ent_product` (
  `id`            BIGINT(16)               NOT NULL AUTO_INCREMENT,
  `product_id`    BIGINT(16)               NOT NULL
  COMMENT '平台产品ID',
  `enterprize_id` BIGINT(16)               NOT NULL,
  `amount`        BIGINT(16)                        DEFAULT NULL,
  `delete_flag`   INT(1) UNSIGNED ZEROFILL NOT NULL DEFAULT '0',
  `version`       INT(1) UNSIGNED ZEROFILL NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `ent_index` (`enterprize_id`, `delete_flag`) USING BTREE,
  KEY `prd_index` (`product_id`, `enterprize_id`) USING BTREE,
  KEY `ent_prd_index` (`product_id`, `enterprize_id`, `delete_flag`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of ent_product
-- ----------------------------

-- ----------------------------
-- Table structure for ent_redpacket
-- ----------------------------
DROP TABLE IF EXISTS `ent_redpacket`;
CREATE TABLE `ent_redpacket` (
  `id`               BIGINT(18)   NOT NULL AUTO_INCREMENT
  COMMENT '标识（活动id）',
  `ent_id`           BIGINT(18)   NOT NULL
  COMMENT '企业标识',
  `start_time`       DATETIME     NOT NULL
  COMMENT '活动开始时间',
  `total`            INT(11)      NOT NULL
  COMMENT '红包总数',
  `used_amount`      INT(11)      NOT NULL DEFAULT '0'
  COMMENT '已使用红包数',
  `max_per_user`     TINYINT(4)   NOT NULL DEFAULT '1'
  COMMENT '每用户最大红包数',
  `status`           TINYINT(4)   NOT NULL DEFAULT '0'
  COMMENT '上下架状态， 1为上架， 0为下架',
  `url`              VARCHAR(255)          DEFAULT NULL
  COMMENT '红包地址',
  `title`            VARCHAR(100) NOT NULL DEFAULT ''
  COMMENT '红包标题',
  `aName`            VARCHAR(64)  NOT NULL
  COMMENT '红包描述',
  `template_id`      BIGINT(18)            DEFAULT NULL
  COMMENT '模板标识',
  `create_time`      DATETIME     NOT NULL
  COMMENT '创建时间',
  `update_time`      DATETIME              DEFAULT NULL
  COMMENT '最近一次修改时间',
  `creator_id`       BIGINT(18)            DEFAULT NULL
  COMMENT '创建者标识',
  `updater_id`       BIGINT(18)            DEFAULT NULL
  COMMENT '最近一次修改者标识',
  `delete_flag`      TINYINT(4)   NOT NULL DEFAULT '0'
  COMMENT '删除Flag，0:未删除；1：已删除',
  `version`          BIGINT(18)   NOT NULL DEFAULT '0'
  COMMENT '版本号',
  `white_or_black`   INT(1)                DEFAULT NULL
  COMMENT '红包名单对应的类型：0无黑白名单，1白名单，2黑名单',
  `list_null`        INT(1)                DEFAULT NULL
  COMMENT '设置的黑白名单是否为空：1空，2不空',
  `type`             INT(2)                DEFAULT NULL
  COMMENT '模板类型：1：默认；自定义：2',
  `use_sms`          INT(2)                DEFAULT NULL
  COMMENT '0：不使用短信功能；1使用',
  `sms_template_id`  BIGINT(18)            DEFAULT NULL
  COMMENT '短信模板id',
  `end_time`         DATETIME     NOT NULL
  COMMENT '活动结束时间',
  `activity_id`      VARCHAR(100) NOT NULL,
  `probability_type` INT(18)               DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_1` (`status`, `delete_flag`) USING BTREE,
  KEY `index_2` (`ent_id`, `creator_id`) USING BTREE,
  KEY `index_3` (`activity_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 9
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of ent_redpacket
-- ----------------------------

-- ----------------------------
-- Table structure for ent_redpacket_record
-- ----------------------------
DROP TABLE IF EXISTS `ent_redpacket_record`;
CREATE TABLE `ent_redpacket_record` (
  `id`             BIGINT(18)  NOT NULL AUTO_INCREMENT
  COMMENT '标识',
  `rule_id`        BIGINT(18)  NOT NULL
  COMMENT '规则标识',
  `prd_id`         BIGINT(18)  NOT NULL,
  `mobile`         VARCHAR(15) NOT NULL
  COMMENT '手机号码',
  `status`         TINYINT(4)  NOT NULL
  COMMENT '充值状态, 1待充值；2已发送充值请求；3充值成功；4充值失败',
  `error_message`  VARCHAR(100)         DEFAULT NULL
  COMMENT '充值失败信息',
  `create_time`    DATETIME    NOT NULL
  COMMENT '创建时间',
  `operate_time`   DATETIME             DEFAULT NULL
  COMMENT '操作时间',
  `sys_serial_num` VARCHAR(50)          DEFAULT NULL
  COMMENT '系统流水号',
  PRIMARY KEY (`id`),
  KEY `index_1` (`rule_id`) USING BTREE,
  KEY `rule_mobile_index` (`rule_id`, `mobile`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 17
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of ent_redpacket_record
-- ----------------------------

-- ----------------------------
-- Table structure for flow_account
-- ----------------------------
DROP TABLE IF EXISTS `flow_account`;
CREATE TABLE `flow_account` (
  `id`                BIGINT(18) NOT NULL AUTO_INCREMENT,
  `author_serial_num` VARCHAR(225)        DEFAULT NULL
  COMMENT '系统赠送充值流水号',
  `ent_serial_num`    VARCHAR(225)        DEFAULT NULL
  COMMENT '企业充值赠送流水号',
  `phone`             VARCHAR(20)         DEFAULT NULL,
  `enterprise_code`   VARCHAR(20)         DEFAULT NULL
  COMMENT '13位企业编码',
  `product_code`      VARCHAR(64)         DEFAULT NULL
  COMMENT '产品编码',
  `response_time`     DATETIME            DEFAULT NULL
  COMMENT '返回时间：指接收到boss返回的充值时间，对账已返回时间为准',
  `request_time`      DATETIME            DEFAULT NULL,
  `return_code`       VARCHAR(20)         DEFAULT NULL
  COMMENT '返回代码，100表示返回成功，其余失败',
  `create_time`       DATETIME            DEFAULT NULL,
  `update_time`       DATETIME            DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of flow_account
-- ----------------------------

-- ----------------------------
-- Table structure for flow_account_analysis
-- ----------------------------
DROP TABLE IF EXISTS `flow_account_analysis`;
CREATE TABLE `flow_account_analysis` (
  `id`                 BIGINT(18) NOT NULL AUTO_INCREMENT,
  `check_account_date` DATETIME            DEFAULT NULL
  COMMENT '对账日期',
  `account_start_date` DATETIME            DEFAULT NULL
  COMMENT '目账起始日期',
  `account_end_date`   DATETIME            DEFAULT NULL
  COMMENT '目账终止日期',
  `sys_account_cnt`    BIGINT(255)         DEFAULT NULL
  COMMENT '指定账目日期系统相应的记录数',
  `ent_account_cnt`    BIGINT(255)         DEFAULT NULL
  COMMENT '业企上传指定日期的成功账目记录数',
  `diff_cnt`           BIGINT(255)         DEFAULT NULL
  COMMENT '差异记录数',
  `sys_lost_cnt`       BIGINT(255)         DEFAULT NULL
  COMMENT '系统缺失记录数=企业记录数-系统记录数-无效记录数（不在指定时间范围内的记录）',
  `ent_lost_cnt`       BIGINT(255)         DEFAULT NULL
  COMMENT '企业缺失记录数',
  `healthy_rate`       VARCHAR(20)         DEFAULT NULL
  COMMENT '健康度：（差异数-无效记录数）/（企业上传成功数目-无效记录数+企业缺失记录数）',
  `create_date`        DATETIME            DEFAULT NULL,
  `update_date`        DATETIME            DEFAULT NULL,
  `enterprise_code`    VARCHAR(50)         DEFAULT NULL
  COMMENT '对账企业编号',
  `check_result`       BIGINT(2)           DEFAULT '2'
  COMMENT '对账结果。0成功，1失败',
  `check_account_type` BIGINT(2)           DEFAULT NULL
  COMMENT '对账类型:按月，按天，自定义',
  `reason`             VARCHAR(255)        DEFAULT NULL
  COMMENT '失败原因',
  `invalid_cnt`        BIGINT(225)         DEFAULT NULL
  COMMENT '账目返回代码不是100的记录数',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of flow_account_analysis
-- ----------------------------

-- ----------------------------
-- Table structure for flow_account_difference
-- ----------------------------
DROP TABLE IF EXISTS `flow_account_difference`;
CREATE TABLE `flow_account_difference` (
  `id`                     BIGINT(18) NOT NULL AUTO_INCREMENT,
  `difference_code`        BIGINT(18)          DEFAULT NULL
  COMMENT '差异编号',
  `author_serial_num`      VARCHAR(255)        DEFAULT NULL,
  `ent_serial_num`         VARCHAR(255)        DEFAULT NULL,
  `phone`                  VARCHAR(20)         DEFAULT NULL,
  `enterprise_code`        VARCHAR(20)         DEFAULT NULL,
  `product_code`           VARCHAR(64)         DEFAULT NULL,
  `request_time`           DATETIME            DEFAULT NULL,
  `response_time`          DATETIME            DEFAULT NULL,
  `return_code`            BIGINT(20)          DEFAULT NULL,
  `create_time`            DATETIME            DEFAULT NULL,
  `update_time`            DATETIME            DEFAULT NULL,
  `difference_description` VARCHAR(255)        DEFAULT NULL
  COMMENT '差异描述',
  `check_id`               BIGINT(18)          DEFAULT NULL
  COMMENT '对账记录id',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of flow_account_difference
-- ----------------------------

-- ----------------------------
-- Table structure for give_money
-- ----------------------------
DROP TABLE IF EXISTS `give_money`;
CREATE TABLE `give_money` (
  `id`          BIGINT(18)   NOT NULL AUTO_INCREMENT,
  `name`        VARCHAR(100) NOT NULL,
  `status`      INT(2)       NOT NULL DEFAULT '1'
  COMMENT '状态：1，可正常使用',
  `create_time` DATETIME     NOT NULL,
  `update_time` DATETIME     NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of give_money
-- ----------------------------
INSERT INTO `give_money` VALUES ('1', '无存送', '1', '2016-04-22 16:07:56', '2016-04-22 16:13:04');
INSERT INTO `give_money` VALUES ('2', '送10%', '1', '2016-04-22 15:51:15', '2016-04-22 16:13:06');
INSERT INTO `give_money` VALUES ('3', '送20%', '1', '2016-04-22 15:51:17', '2016-04-22 16:13:09');
INSERT INTO `give_money` VALUES ('4', '送30%', '1', '2016-04-22 15:51:19', '2016-04-22 16:13:11');

-- ----------------------------
-- Table structure for give_money_enter
-- ----------------------------
DROP TABLE IF EXISTS `give_money_enter`;
CREATE TABLE `give_money_enter` (
  `enter_id`      BIGINT(18) NOT NULL,
  `give_money_id` BIGINT(18) NOT NULL,
  `create_time`   DATETIME   NOT NULL,
  `update_time`   DATETIME   NOT NULL
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of give_money_enter
-- ----------------------------

-- ----------------------------
-- Table structure for global_config
-- ----------------------------
DROP TABLE IF EXISTS `global_config`;
CREATE TABLE `global_config` (
  `id`            BIGINT(18)   NOT NULL AUTO_INCREMENT
  COMMENT '标识',
  `name`          VARCHAR(50)  NOT NULL
  COMMENT '配置名称',
  `description`   VARCHAR(200)          DEFAULT NULL
  COMMENT '配置描述',
  `config_key`    VARCHAR(50)  NOT NULL
  COMMENT '配置KEY值',
  `config_value`  VARCHAR(300) NOT NULL
  COMMENT '配置VALUE值',
  `create_time`   DATETIME     NOT NULL,
  `update_time`   DATETIME              DEFAULT NULL,
  `creator_id`    BIGINT(18)   NOT NULL,
  `updater_id`    BIGINT(18)   NOT NULL,
  `delete_flag`   INT(1)       NOT NULL,
  `config_update` INT(1)                DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `key_index` (`config_key`) USING BTREE,
  KEY `id_index` (`id`, `delete_flag`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 46
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of global_config
-- ----------------------------
INSERT INTO `global_config` VALUES
  ('15', '充值启用标记位', 'bbbvnbnn', 'CHARGE_ENABLE_KEY', 'OK', '2015-05-08 15:10:39',
         '2015-05-13 14:36:08', '1', '1', '0',
         '0');
INSERT INTO `global_config` VALUES
  ('21', '每日游戏次数上限标记位', 'OK为启用，其它为不启用', 'DAILY_LIMIT_KEY', 'OK', '2015-05-14 14:16:16',
         '2015-05-14 14:16:16', '1', '1',
         '0', '0');
INSERT INTO `global_config` VALUES
  ('22', '每月获赠流量上限标记位', 'OK为启用，其它为不启用', 'MONTHLY_LIMIT_KEY', 'OK', '2015-05-18 14:12:12',
         '2015-05-18 14:12:12', '1',
         '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('33', '业务电话号码boss数量检测', '红包，包月赠送，普通赠送检测电话数是否超过Boss存量', 'PHONES_TEST_BOSS', 'NO',
         '2015-06-05 16:50:14',
         '2015-06-05 16:50:14', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('34', '流量充值测试标记位', 'OK为启用，其它为不启用', 'LOTTERY_CHARGE_TEST', 'NO', '2015-06-24 22:20:17',
         '2015-06-24 22:20:19', '1',
         '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('35', '真实发送短信配置项', '值为OK，会真实连接短信网关，发送短信', 'SENDMESSAGE_CHECK', 'OK', '2015-07-01 16:32:25',
         '2015-08-14 16:27:03',
         '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('36', '测试随机验证码标记位', 'OK为启用，其它为不启用', 'RANDOMPASS_CHECK', 'OK', '2015-07-01 15:40:58',
         '2015-09-24 11:00:17', '1', '1',
         '0', '0');
INSERT INTO `global_config` VALUES
  ('41', '是否启用全国流量充值接口', '是否启用全国流量充值接口', 'ENABLE_CHARGE_KEY', 'NO', '2015-07-15 09:25:04',
         '2015-09-07 16:47:08', '1',
         '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('43', 'boss检测流量包个数标记位', '是否从boss检测流量包个数', 'PHONENUMBERS_CHECK', 'NO', '2015-08-25 11:27:58',
         '2015-10-15 14:24:42',
         '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('44', '采用漫道短信接口', '是否采用漫道短信接口下发短信,OK为启用', 'MDMESSAGE_CHECK', 'NO', '2016-03-09 09:48:25',
         '2016-03-09 09:48:29', '1',
         '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('45', '检查企业BOSS余额', '是否检查企业BOSS余额（四川）', 'SC_BOSSBALANCE_CHECK', 'OK', '2016-04-25 16:26:17',
         '2016-04-25 16:26:21',
         '1', '1', '0', '0');
INSERT INTO `global_config`
VALUES
  ('46', '营销卡卡密数据序列号', '营销卡卡密数据序列号', 'serialNumKey', '26837', now(), now(), '1', '1', '0', '0');

-- ----------------------------
-- Table structure for interface_record
-- ----------------------------
DROP TABLE IF EXISTS `interface_record`;
CREATE TABLE `interface_record` (
  `id`                    BIGINT(18)  NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  `enterprise_code`       VARCHAR(13) NOT NULL
  COMMENT '企业编码',
  `product_code`          VARCHAR(20) NOT NULL
  COMMENT '产品编码',
  `phone_num`             VARCHAR(11) NOT NULL
  COMMENT '充值手机号',
  `serial_num`            VARCHAR(50) NOT NULL,
  `ip_address`            VARCHAR(15)          DEFAULT NULL
  COMMENT '创建订单的ip地址',
  `status`                INT(4)      NOT NULL
  COMMENT '状态码:1 已创建  2.已发送请求  3.充值成功  4.充值失败',
  `err_msg`               VARCHAR(200)         DEFAULT NULL,
  `create_time`           DATETIME    NOT NULL
  COMMENT '创建时间',
  `charge_time`           DATETIME             DEFAULT NULL
  COMMENT '更新时间',
  `delete_flag`           INT(4)      NOT NULL,
  `enterprise_serial_num` VARCHAR(50)          DEFAULT NULL
  COMMENT '企业流水号',
  `sys_serial_num`        VARCHAR(50)          DEFAULT NULL
  COMMENT '系统流水号',
  `boss_serial_num`       VARCHAR(50)          DEFAULT NULL
  COMMENT 'BOSS侧返回的序列号',
  PRIMARY KEY (`id`),
  KEY `ifExistOrder` (`enterprise_code`, `product_code`, `serial_num`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 324727
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of interface_record
-- ----------------------------

-- ----------------------------
-- Table structure for lottery_activity
-- ----------------------------
DROP TABLE IF EXISTS `lottery_activity`;
CREATE TABLE `lottery_activity` (
  `id`               BIGINT(18)   NOT NULL AUTO_INCREMENT,
  `activity_id`      VARCHAR(225) NOT NULL
  COMMENT '活动ID',
  `name`             VARCHAR(64)           DEFAULT NULL
  COMMENT '活动名称',
  `charge_type`      INT(11)               DEFAULT NULL
  COMMENT '充值方式,是否延后充值:0立即充值；1延后充值',
  `charge_url`       VARCHAR(255)          DEFAULT NULL
  COMMENT '充值接口地址',
  `flow_type`        INT(2)                DEFAULT NULL
  COMMENT '流量类型，0:流量包',
  `start_time`       DATETIME              DEFAULT NULL
  COMMENT '活动开始时间',
  `end_time`         DATETIME              DEFAULT NULL
  COMMENT '活动结束时间',
  `max_play_number`  INT(18)               DEFAULT NULL
  COMMENT '抽奖次数',
  `gived_number`     INT(18)               DEFAULT NULL
  COMMENT '中奖次数',
  `lottery_type`     INT(2)                DEFAULT NULL
  COMMENT '抽奖类型：0:活动期间只能抽lottery_count次；1:每天能抽取lottery_count次',
  `probability_type` INT(2)                DEFAULT NULL
  COMMENT '中奖概率:0固定概率，奖项概率需手动设置；1非固定概率，活动期间奖项概率自动调整，无需设置',
  `creator_id`       BIGINT(18)            DEFAULT NULL
  COMMENT '动活创建者',
  `create_time`      DATETIME              DEFAULT NULL,
  `update_time`      DATETIME              DEFAULT NULL,
  `status`           INT(4)                DEFAULT NULL
  COMMENT '活动状态1:未开始；2进行中；3已结束；4已删除',
  `url`              VARCHAR(255)          DEFAULT NULL
  COMMENT '大转盘地址',
  `code`             VARCHAR(10)           DEFAULT NULL
  COMMENT '返回代码；200正确；其余错误',
  `rule`             VARCHAR(255)          DEFAULT NULL,
  `delete_flag`      INT(1)                DEFAULT NULL
  COMMENT '删除标记:0未删除；1已删除',
  `ent_id`           BIGINT(18)            DEFAULT NULL
  COMMENT '活动绑定的企业ID',
  `check_type`       INT(18)               DEFAULT NULL
  COMMENT '鉴权方式',
  `check_url`        VARCHAR(255)          DEFAULT NULL
  COMMENT '鉴权url',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of lottery_activity
-- ----------------------------

-- ----------------------------
-- Table structure for lottery_record
-- ----------------------------
DROP TABLE IF EXISTS `lottery_record`;
CREATE TABLE `lottery_record` (
  `activity_id`     VARCHAR(50)  NOT NULL
  COMMENT '活动ID',
  `id`              BIGINT(18)   NOT NULL AUTO_INCREMENT
  COMMENT '记录ID',
  `wx_openid`       VARCHAR(50)  NOT NULL
  COMMENT '微信openId',
  `product_code`    VARCHAR(50)  NOT NULL
  COMMENT '品产编码',
  `enterprise_code` VARCHAR(50)  NOT NULL
  COMMENT '企业编码',
  `mobile`          VARCHAR(15)  NOT NULL
  COMMENT '奖中手机号码',
  `product_name`    VARCHAR(255) NOT NULL
  COMMENT '产品名称',
  `win_time`        DATETIME     NOT NULL
  COMMENT '中奖时间',
  `create_time`     DATETIME     NOT NULL,
  `rank_name`       VARCHAR(20)  NOT NULL
  COMMENT '几等奖',
  `status`          INT(4)       NOT NULL
  COMMENT '充值状态',
  `reason`          VARCHAR(100)          DEFAULT NULL
  COMMENT '败失原因',
  `delete_flag`     INT(1)                DEFAULT NULL
  COMMENT '除删标识：0未删除；1已删除',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 17
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of lottery_record
-- ----------------------------

-- ----------------------------
-- Table structure for mdrc_cardmaker
-- ----------------------------
DROP TABLE IF EXISTS `mdrc_cardmaker`;
CREATE TABLE `mdrc_cardmaker` (
  `id`            BIGINT(18)   NOT NULL AUTO_INCREMENT
  COMMENT '标识，主键',
  `name`          VARCHAR(100) NOT NULL
  COMMENT '制卡商名称',
  `serial_number` VARCHAR(4)            DEFAULT NULL,
  `create_time`   DATETIME     NOT NULL
  COMMENT '记录创建时间',
  `operator_id`   BIGINT(20)            DEFAULT NULL
  COMMENT '专员标识',
  `delete_flag`   TINYINT(4)   NOT NULL
  COMMENT '删除标记位',
  `public_key`    TEXT COMMENT '公钥',
  `private_key`   TEXT COMMENT '私钥',
  `creator_id`    BIGINT(18)   NOT NULL
  COMMENT '创建者标识',
  PRIMARY KEY (`id`),
  KEY `serial_num_index` (`serial_number`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of mdrc_cardmaker
-- ----------------------------

-- ----------------------------
-- Table structure for mdrc_card_info
-- ----------------------------
DROP TABLE IF EXISTS `mdrc_card_info`;
CREATE TABLE `mdrc_card_info` (
  `id`             BIGINT(18)  NOT NULL AUTO_INCREMENT
  COMMENT '标识',
  `config_id`      BIGINT(18)  NOT NULL
  COMMENT '生成规则标识',
  `card_number`    VARCHAR(27) NOT NULL
  COMMENT '序列号',
  `card_password`  VARCHAR(32)          DEFAULT NULL
  COMMENT '密码',
  `status`         TINYINT(1)  NOT NULL
  COMMENT '卡状态，1新制卡, 2已入库、3已激活、4已绑定、5已使用、6已过期、7已锁定',
  `user_mobile`    VARCHAR(11)          DEFAULT NULL
  COMMENT '使用者手机号',
  `user_ip`        VARCHAR(15)          DEFAULT NULL
  COMMENT '使用者IP地址',
  `deadline`       DATETIME             DEFAULT NULL
  COMMENT '失效日期',
  `create_time`    DATETIME    NOT NULL
  COMMENT '记录创建时间',
  `stored_time`    DATETIME             DEFAULT NULL
  COMMENT '入库时间',
  `activated_time` DATETIME             DEFAULT NULL
  COMMENT '激活时间',
  `bound_time`     DATETIME             DEFAULT NULL
  COMMENT '绑定时间',
  `used_time`      DATETIME             DEFAULT NULL
  COMMENT '使用时间',
  `locked_time`    DATETIME             DEFAULT NULL
  COMMENT '锁定时间',
  `enter_id`       BIGINT(18)           DEFAULT NULL,
  `product_id`     BIGINT(18)           DEFAULT NULL,
  `sys_serial_num` VARCHAR(50)          DEFAULT NULL
  COMMENT '系统流水号',
  PRIMARY KEY (`id`),
  KEY `configId_status` (`config_id`, `status`) USING BTREE,
  KEY `cardnumber_index` (`card_number`) USING BTREE,
  KEY `configId_index` (`config_id`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of mdrc_card_info
-- ----------------------------

-- ----------------------------
-- Table structure for mdrc_enterprises
-- ----------------------------
DROP TABLE IF EXISTS `mdrc_enterprises`;
CREATE TABLE `mdrc_enterprises` (
  `id`          BIGINT(18)   NOT NULL AUTO_INCREMENT
  COMMENT '企业ID',
  `name`        VARCHAR(255) NOT NULL
  COMMENT '企业名称',
  `code`        VARCHAR(20)  NOT NULL
  COMMENT '企业代码',
  `phone`       VARCHAR(11)  NOT NULL
  COMMENT '企业手机号码',
  `create_time` DATETIME     NOT NULL
  COMMENT '创建时间',
  `update_time` DATETIME              DEFAULT NULL
  COMMENT '更新时间',
  `delete_flag` INT(1)       NOT NULL DEFAULT '0'
  COMMENT '删除Flag，0:未删除；1：已删除',
  `creator_id`  BIGINT(18)            DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `name_index` (`name`(191), `delete_flag`) USING BTREE,
  KEY `code_index` (`code`, `delete_flag`) USING BTREE,
  KEY `all_index` (`delete_flag`) USING BTREE,
  KEY `createor_index` (`delete_flag`, `creator_id`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of mdrc_enterprises
-- ----------------------------

-- ----------------------------
-- Table structure for mdrc_entprd
-- ----------------------------
DROP TABLE IF EXISTS `mdrc_entprd`;
CREATE TABLE `mdrc_entprd` (
  `id`            BIGINT(16)               NOT NULL AUTO_INCREMENT,
  `product_id`    BIGINT(16)               NOT NULL
  COMMENT '2ú?·ID',
  `enterprize_id` BIGINT(16)               NOT NULL,
  `amount`        BIGINT(16)                        DEFAULT NULL,
  `delete_flag`   INT(1) UNSIGNED ZEROFILL NOT NULL DEFAULT '0',
  `version`       INT(1) UNSIGNED ZEROFILL NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `ent_index` (`enterprize_id`, `delete_flag`) USING BTREE,
  KEY `prd_index` (`product_id`, `enterprize_id`) USING BTREE,
  KEY `ent_prd_index` (`product_id`, `enterprize_id`, `delete_flag`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of mdrc_entprd
-- ----------------------------

-- ----------------------------
-- Table structure for mdrc_template
-- ----------------------------
DROP TABLE IF EXISTS `mdrc_template`;
CREATE TABLE `mdrc_template` (
  `id`              BIGINT(18)   NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  `name`            VARCHAR(100) NOT NULL
  COMMENT '模板名称',
  `front_image`     VARCHAR(255)          DEFAULT NULL
  COMMENT '正面预览图',
  `rear_image`      VARCHAR(255)          DEFAULT NULL
  COMMENT '背面预览图',
  `product_id`      BIGINT(18)            DEFAULT NULL
  COMMENT '产品标识',
  `create_time`     DATETIME     NOT NULL
  COMMENT '记录创建时间',
  `delete_flag`     INT(1)       NOT NULL
  COMMENT '删除标记位',
  `delete_time`     DATETIME              DEFAULT NULL
  COMMENT '删除时间',
  `creator_id`      BIGINT(18)   NOT NULL
  COMMENT '创建者ID',
  `theme`           VARCHAR(100)          DEFAULT NULL
  COMMENT '模板主题',
  `resources_count` INT(11)      NOT NULL
  COMMENT '资源文件个数',
  `product_size`    BIGINT(18)            DEFAULT '0'
  COMMENT '产品流量包大小',
  `pro_size`        VARCHAR(100) NOT NULL
  COMMENT '流量包大小（字符型）',
  PRIMARY KEY (`id`),
  KEY `theme_index` (`theme`) USING BTREE,
  KEY `name_index` (`name`, `delete_flag`) USING BTREE,
  KEY `delete_index` (`delete_flag`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of mdrc_template
-- ----------------------------

-- ----------------------------
-- Table structure for monthly_present_record
-- ----------------------------
DROP TABLE IF EXISTS `monthly_present_record`;
CREATE TABLE `monthly_present_record` (
  `id`             BIGINT(18)  NOT NULL AUTO_INCREMENT
  COMMENT '标识',
  `rule_id`        BIGINT(18)  NOT NULL
  COMMENT '规则标识',
  `mobile`         VARCHAR(15) NOT NULL
  COMMENT '手机号码',
  `serial_number`  TINYINT(4)  NOT NULL
  COMMENT '当前是第几期',
  `status`         TINYINT(4)  NOT NULL
  COMMENT '充值状态， 1待充值；2已发送充值请求；3充值成功; 4充值失败',
  `error_message`  VARCHAR(100)         DEFAULT NULL
  COMMENT '充值失败信息',
  `create_time`    DATETIME    NOT NULL
  COMMENT '创建时间',
  `operate_time`   DATETIME    NOT NULL
  COMMENT '操作时间',
  `sys_serial_num` VARCHAR(100)         DEFAULT NULL
  COMMENT '系统流水号',
  PRIMARY KEY (`id`),
  KEY `rule_serial` (`rule_id`, `serial_number`) USING BTREE,
  KEY `rule_index` (`rule_id`) USING BTREE,
  KEY `serial_index` (`serial_number`) USING BTREE,
  KEY `index4` (`rule_id`, `mobile`, `serial_number`, `status`) USING BTREE,
  KEY `index3` (`rule_id`, `serial_number`, `status`) USING BTREE,
  KEY `status_index` (`status`) USING BTREE,
  KEY `mobile_index` (`mobile`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of monthly_present_record
-- ----------------------------


-- ----------------------------
-- Table structure for monthly_present_rule
-- ----------------------------
DROP TABLE IF EXISTS `monthly_present_rule`;
CREATE TABLE `monthly_present_rule` (
  `id`              BIGINT(18) NOT NULL AUTO_INCREMENT
  COMMENT '标识',
  `ent_id`          BIGINT(18) NOT NULL
  COMMENT '企业标识',
  `prd_id`          BIGINT(18) NOT NULL
  COMMENT '产品标识',
  `total`           INT(11)    NOT NULL
  COMMENT '被赠送人总数',
  `status`          TINYINT(4) NOT NULL
  COMMENT '状态',
  `month_count`     INT(11)    NOT NULL
  COMMENT '赠送总月数',
  `start_time`      DATETIME   NOT NULL
  COMMENT '赠送开始时间',
  `end_time`        DATETIME   NOT NULL
  COMMENT '赠送结束时间',
  `create_time`     DATETIME   NOT NULL
  COMMENT '创建时间',
  `update_time`     DATETIME   NOT NULL
  COMMENT '更新时间',
  `creator_id`      BIGINT(18) NOT NULL
  COMMENT '创建者标识',
  `updater_id`      BIGINT(18) NOT NULL
  COMMENT '最近一次修改者标识',
  `delete_flag`     TINYINT(4) NOT NULL
  COMMENT '删除标记，0未删除， 1已删除',
  `version`         TINYINT(4) NOT NULL
  COMMENT '更新版本号',
  `use_sms`         INT(2)              DEFAULT NULL,
  `sms_template_id` BIGINT(18)          DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of monthly_present_rule
-- ----------------------------

-- ----------------------------
-- Table structure for pay_type
-- ----------------------------
DROP TABLE IF EXISTS `pay_type`;
CREATE TABLE `pay_type` (
  `id`   BIGINT(18) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100)        DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of pay_type
-- ----------------------------
INSERT INTO `pay_type` (`id`, `name`) VALUES ('1', '预付费');
INSERT INTO `pay_type` (`id`, `name`) VALUES ('2', '后付费');

-- ----------------------------
-- Table structure for phone_region
-- ----------------------------
DROP TABLE IF EXISTS `phone_region`;
CREATE TABLE `phone_region` (
  `id`            BIGINT(18)  NOT NULL AUTO_INCREMENT
  COMMENT '自增ID，主键',
  `number_prefix` VARCHAR(7)  NOT NULL
  COMMENT '手机号码段',
  `province`      VARCHAR(20) NOT NULL
  COMMENT '省份',
  `city`          VARCHAR(20)          DEFAULT NULL
  COMMENT '城市',
  `type`          VARCHAR(20)          DEFAULT NULL
  COMMENT '类型 1 全球通 2 神州行 3 动感地带',
  `create_time`   DATETIME    NOT NULL,
  `update_time`   DATETIME             DEFAULT NULL,
  `delete_flag`   INT(1)      NOT NULL DEFAULT '0'
  COMMENT '删除标记， 0:未删除；1：已删除',
  PRIMARY KEY (`id`),
  KEY `number_prefix` (`number_prefix`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of phone_region
-- ----------------------------

-- ----------------------------
-- Table structure for potential_customer
-- ----------------------------
DROP TABLE IF EXISTS `potential_customer`;
CREATE TABLE `potential_customer` (
  `id`          BIGINT(18)   NOT NULL AUTO_INCREMENT
  COMMENT '移动用户ID',
  `mobile`      VARCHAR(11)  NOT NULL
  COMMENT '潜在用户联系方式',
  `create_time` DATETIME     NOT NULL
  COMMENT '创建时间',
  `update_time` DATETIME              DEFAULT NULL
  COMMENT '更新时间',
  `delete_flag` INT(1)       NOT NULL DEFAULT '0'
  COMMENT '删除标记， 0:未删除；1：已删除',
  `name`        VARCHAR(128) NOT NULL
  COMMENT '企业名称',
  `priority`    INT(8)       NOT NULL
  COMMENT '意愿等级',
  `status`      INT(8)       NOT NULL
  COMMENT '潜在客户的状态,0为推进中,1为已签约',
  `creator_id`  BIGINT(18)   NOT NULL
  COMMENT '潜在客户的创建者',
  `district_id` BIGINT(18)   NOT NULL
  COMMENT '潜在客户的区域信息',
  PRIMARY KEY (`id`),
  KEY `openid_index` (`delete_flag`) USING BTREE,
  KEY `mobile_phone_index` (`mobile`, `delete_flag`) USING BTREE,
  KEY `mobile_index2` (`mobile`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8
  COMMENT = '潜在用户表';

-- ----------------------------
-- Records of potential_customer
-- ----------------------------

-- ----------------------------
-- Table structure for present_record
-- ----------------------------
DROP TABLE IF EXISTS `present_record`;
CREATE TABLE `present_record` (
  `id`              BIGINT(18)  NOT NULL AUTO_INCREMENT
  COMMENT '标识',
  `rule_id`         BIGINT(18)  NOT NULL
  COMMENT '规则标识',
  `prd_id`          BIGINT(18)  NOT NULL,
  `mobile`          VARCHAR(15) NOT NULL
  COMMENT '手机号码',
  `status`          TINYINT(4)  NOT NULL
  COMMENT '充值状态',
  `error_message`   VARCHAR(100)         DEFAULT NULL
  COMMENT '充值失败信息',
  `create_time`     DATETIME    NOT NULL
  COMMENT '创建时间',
  `operate_time`    DATETIME             DEFAULT NULL
  COMMENT '操作时间',
  `sys_serial_num`  VARCHAR(50)          DEFAULT NULL
  COMMENT '系统流水号',
  `boss_serial_num` VARCHAR(50)          DEFAULT NULL
  COMMENT 'BOSS流水号',
  PRIMARY KEY (`id`),
  KEY `rule_index` (`rule_id`) USING BTREE,
  KEY `rule_status` (`rule_id`, `status`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 3582
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of present_record
-- ----------------------------

-- ----------------------------
-- Table structure for present_rule
-- ----------------------------
DROP TABLE IF EXISTS `present_rule`;
CREATE TABLE `present_rule` (
  `id`              BIGINT(18) NOT NULL AUTO_INCREMENT
  COMMENT '标识',
  `ent_id`          BIGINT(18) NOT NULL
  COMMENT '企业标识',
  `total`           INT(11)    NOT NULL
  COMMENT '被赠送人总数',
  `status`          TINYINT(4) NOT NULL
  COMMENT '状态',
  `create_time`     DATETIME   NOT NULL
  COMMENT '创建时间',
  `update_time`     DATETIME            DEFAULT NULL
  COMMENT '最近一次修改时间',
  `creator_id`      BIGINT(18)          DEFAULT NULL
  COMMENT '创建者标识',
  `updater_id`      BIGINT(18)          DEFAULT NULL
  COMMENT '最近一次修改者标识',
  `delete_flag`     TINYINT(4) NOT NULL
  COMMENT '删除标记位',
  `version`         TINYINT(4) NOT NULL
  COMMENT '版本号',
  `use_sms`         INT(2)              DEFAULT NULL,
  `sms_template_id` BIGINT(18)          DEFAULT NULL,
  `activity_name`   VARCHAR(100)        DEFAULT NULL
  COMMENT '活动名称',
  PRIMARY KEY (`id`),
  KEY `index_1` (`status`, `delete_flag`) USING BTREE,
  KEY `mult_index` (`ent_id`, `total`, `creator_id`) USING BTREE,
  KEY `id_index` (`id`, `delete_flag`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 461
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of present_rule
-- ----------------------------

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id`           BIGINT(18)   NOT NULL AUTO_INCREMENT
  COMMENT '自增ID，主键',
  `product_code` VARCHAR(64)
                 CHARACTER SET utf8
                 COLLATE utf8_bin      DEFAULT NULL,
  `type`         INT(8)       NOT NULL DEFAULT '2'
  COMMENT '产品类型，0为现金产品，1为流量池产品，2为流量包产品',
  `name`         VARCHAR(128) NOT NULL,
  `status`       INT(1)       NOT NULL
  COMMENT '上下架状态：0 下架 1 上架',
  `create_time`  DATETIME     NOT NULL
  COMMENT '创建时间',
  `update_time`  DATETIME              DEFAULT NULL
  COMMENT '更新时间',
  `delete_flag`  INT(1)       NOT NULL DEFAULT '0'
  COMMENT '删除Flag，0:未删除；1：已删除',
  `product_size` VARCHAR(100)          DEFAULT NULL
  COMMENT '流量包大小（字符型）',
  `price`        DOUBLE(40, 2)         DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `delete_index` (`delete_flag`) USING BTREE,
  KEY `name_index` (`name`, `delete_flag`) USING BTREE,
  KEY `code_index` (`product_code`, `delete_flag`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 364
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of product
-- ----------------------------


-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars` (
  `SCHED_NAME`    VARCHAR(120) NOT NULL,
  `CALENDAR_NAME` VARCHAR(200) NOT NULL,
  `CALENDAR`      BLOB         NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details` (
  `SCHED_NAME`        VARCHAR(120) NOT NULL,
  `JOB_NAME`          VARCHAR(200) NOT NULL,
  `JOB_GROUP`         VARCHAR(200) NOT NULL,
  `DESCRIPTION`       VARCHAR(250) DEFAULT NULL,
  `JOB_CLASS_NAME`    VARCHAR(250) NOT NULL,
  `IS_DURABLE`        VARCHAR(1)   NOT NULL,
  `IS_NONCONCURRENT`  VARCHAR(1)   NOT NULL,
  `IS_UPDATE_DATA`    VARCHAR(1)   NOT NULL,
  `REQUESTS_RECOVERY` VARCHAR(1)   NOT NULL,
  `JOB_DATA`          BLOB,
  PRIMARY KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`),
  KEY `IDX_QRTZ_J_REQ_RECOVERY` (`SCHED_NAME`, `REQUESTS_RECOVERY`) USING BTREE,
  KEY `IDX_QRTZ_J_GRP` (`SCHED_NAME`, `JOB_GROUP`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers` (
  `SCHED_NAME`     VARCHAR(120) NOT NULL,
  `TRIGGER_NAME`   VARCHAR(200) NOT NULL,
  `TRIGGER_GROUP`  VARCHAR(200) NOT NULL,
  `JOB_NAME`       VARCHAR(200) NOT NULL,
  `JOB_GROUP`      VARCHAR(200) NOT NULL,
  `DESCRIPTION`    VARCHAR(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` BIGINT(13)   DEFAULT NULL,
  `PREV_FIRE_TIME` BIGINT(13)   DEFAULT NULL,
  `PRIORITY`       INT(11)      DEFAULT NULL,
  `TRIGGER_STATE`  VARCHAR(16)  NOT NULL,
  `TRIGGER_TYPE`   VARCHAR(8)   NOT NULL,
  `START_TIME`     BIGINT(13)   NOT NULL,
  `END_TIME`       BIGINT(13)   DEFAULT NULL,
  `CALENDAR_NAME`  VARCHAR(200) DEFAULT NULL,
  `MISFIRE_INSTR`  SMALLINT(2)  DEFAULT NULL,
  `JOB_DATA`       BLOB,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_J` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_T_JG` (`SCHED_NAME`, `JOB_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_T_C` (`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE,
  KEY `IDX_QRTZ_T_G` (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_T_STATE` (`SCHED_NAME`, `TRIGGER_STATE`) USING BTREE,
  KEY `IDX_QRTZ_T_N_STATE` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
  KEY `IDX_QRTZ_T_N_G_STATE` (`SCHED_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
  KEY `IDX_QRTZ_T_NEXT_FIRE_TIME` (`SCHED_NAME`, `NEXT_FIRE_TIME`) USING BTREE,
  KEY `IDX_QRTZ_T_NFT_ST` (`SCHED_NAME`, `TRIGGER_STATE`, `NEXT_FIRE_TIME`) USING BTREE,
  KEY `IDX_QRTZ_T_NFT_MISFIRE` (`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`) USING BTREE,
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE` (`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_STATE`) USING BTREE,
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP` (`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers` (
  `SCHED_NAME`    VARCHAR(120) NOT NULL,
  `TRIGGER_NAME`  VARCHAR(200) NOT NULL,
  `TRIGGER_GROUP` VARCHAR(200) NOT NULL,
  `BLOB_DATA`     BLOB,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers` (
  `SCHED_NAME`      VARCHAR(120) NOT NULL,
  `TRIGGER_NAME`    VARCHAR(200) NOT NULL,
  `TRIGGER_GROUP`   VARCHAR(200) NOT NULL,
  `CRON_EXPRESSION` VARCHAR(120) NOT NULL,
  `TIME_ZONE_ID`    VARCHAR(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`),
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers` (
  `SCHED_NAME`        VARCHAR(120) NOT NULL,
  `ENTRY_ID`          VARCHAR(95)  NOT NULL,
  `TRIGGER_NAME`      VARCHAR(200) NOT NULL,
  `TRIGGER_GROUP`     VARCHAR(200) NOT NULL,
  `INSTANCE_NAME`     VARCHAR(200) NOT NULL,
  `FIRED_TIME`        BIGINT(13)   NOT NULL,
  `SCHED_TIME`        BIGINT(13)   NOT NULL,
  `PRIORITY`          INT(11)      NOT NULL,
  `STATE`             VARCHAR(16)  NOT NULL,
  `JOB_NAME`          VARCHAR(200) DEFAULT NULL,
  `JOB_GROUP`         VARCHAR(200) DEFAULT NULL,
  `IS_NONCONCURRENT`  VARCHAR(1)   DEFAULT NULL,
  `REQUESTS_RECOVERY` VARCHAR(1)   DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `ENTRY_ID`),
  KEY `IDX_QRTZ_FT_TRIG_INST_NAME` (`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE,
  KEY `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME`, `INSTANCE_NAME`, `REQUESTS_RECOVERY`) USING BTREE,
  KEY `IDX_QRTZ_FT_J_G` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_FT_JG` (`SCHED_NAME`, `JOB_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_FT_T_G` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_FT_TG` (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------



-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks` (
  `SCHED_NAME` VARCHAR(120) NOT NULL,
  `LOCK_NAME`  VARCHAR(40)  NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------
INSERT INTO `qrtz_locks` VALUES ('schedulerFactory', 'STATE_ACCESS');
INSERT INTO `qrtz_locks` VALUES ('schedulerFactory', 'TRIGGER_ACCESS');

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps` (
  `SCHED_NAME`    VARCHAR(120) NOT NULL,
  `TRIGGER_GROUP` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of qrtz_paused_trigger_grps
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state` (
  `SCHED_NAME`        VARCHAR(120) NOT NULL,
  `INSTANCE_NAME`     VARCHAR(200) NOT NULL,
  `LAST_CHECKIN_TIME` BIGINT(13)   NOT NULL,
  `CHECKIN_INTERVAL`  BIGINT(13)   NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------
INSERT INTO `qrtz_scheduler_state`
VALUES ('schedulerFactory', 'NON_CLUSTERED', '1461575735340', '7500');

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers` (
  `SCHED_NAME`      VARCHAR(120) NOT NULL,
  `TRIGGER_NAME`    VARCHAR(200) NOT NULL,
  `TRIGGER_GROUP`   VARCHAR(200) NOT NULL,
  `REPEAT_COUNT`    BIGINT(7)    NOT NULL,
  `REPEAT_INTERVAL` BIGINT(12)   NOT NULL,
  `TIMES_TRIGGERED` BIGINT(10)   NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of qrtz_simple_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers` (
  `SCHED_NAME`    VARCHAR(120) NOT NULL,
  `TRIGGER_NAME`  VARCHAR(200) NOT NULL,
  `TRIGGER_GROUP` VARCHAR(200) NOT NULL,
  `STR_PROP_1`    VARCHAR(512)   DEFAULT NULL,
  `STR_PROP_2`    VARCHAR(512)   DEFAULT NULL,
  `STR_PROP_3`    VARCHAR(512)   DEFAULT NULL,
  `INT_PROP_1`    INT(11)        DEFAULT NULL,
  `INT_PROP_2`    INT(11)        DEFAULT NULL,
  `LONG_PROP_1`   BIGINT(20)     DEFAULT NULL,
  `LONG_PROP_2`   BIGINT(20)     DEFAULT NULL,
  `DEC_PROP_1`    DECIMAL(13, 4) DEFAULT NULL,
  `DEC_PROP_2`    DECIMAL(13, 4) DEFAULT NULL,
  `BOOL_PROP_1`   VARCHAR(1)     DEFAULT NULL,
  `BOOL_PROP_2`   VARCHAR(1)     DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of qrtz_simprop_triggers
-- ----------------------------


-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `role_id`      BIGINT(18)  NOT NULL AUTO_INCREMENT
  COMMENT '角色ID',
  `name`         VARCHAR(20) NOT NULL
  COMMENT '角色名称',
  `code`         BIGINT(18)  NOT NULL
  COMMENT '角色代码，必须唯一',
  `description`  VARCHAR(255)         DEFAULT NULL
  COMMENT '描述',
  `create_time`  DATETIME    NOT NULL
  COMMENT '创建时间',
  `creator`      BIGINT(18)  NOT NULL
  COMMENT '创建者id',
  `update_user`  BIGINT(18)           DEFAULT NULL
  COMMENT '更新者id',
  `update_time`  DATETIME             DEFAULT NULL
  COMMENT '更新时间',
  `delete_flag`  INT(1)      NOT NULL DEFAULT '0'
  COMMENT '删除flag，0:未删除；1：已删除',
  `ROLE_STATUS`  INT(1)      NOT NULL
  COMMENT '角色状态，0:启用；1：禁用',
  `canBeDeleted` INT(1)      NOT NULL DEFAULT '1'
  COMMENT '该角色是否可被删除，0代表不可删除，1代表可删除',
  PRIMARY KEY (`role_id`),
  KEY `canBeDeleted_index` (`canBeDeleted`) USING BTREE,
  KEY `name_index` (`name`, `delete_flag`) USING BTREE,
  KEY `code_index` (`code`, `delete_flag`) USING BTREE,
  KEY `index2` (`role_id`, `delete_flag`, `ROLE_STATUS`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 8
  DEFAULT CHARSET = utf8
  COMMENT = '角色表';

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role`
VALUES
  ('1', '超级管理员', '1', '', '2015-01-27 18:55:53', '1', '0', '2015-03-02 14:46:06', '0', '0', '0');
INSERT INTO `role`
VALUES
  ('2', '客户经理', '2', '', '2016-01-26 17:32:40', '1', NULL, '2016-01-26 17:32:40', '0', '0', '0');
INSERT INTO `role`
VALUES
  ('3', '企业管理员', '3', '', '2016-01-26 17:33:42', '1', NULL, '2016-02-26 10:11:08', '0', '0', '0');
INSERT INTO `role`
VALUES
  ('6', '省级管理员', '6', '', '2016-02-25 14:03:44', '1', NULL, '2016-02-25 14:03:44', '0', '0', '0');
INSERT INTO `role`
VALUES
  ('7', '市级管理员', '7', '', '2016-02-29 19:56:52', '1', NULL, '2016-02-29 19:56:52', '0', '0', '0');

-- ----------------------------
-- Table structure for role_authority
-- ----------------------------
DROP TABLE IF EXISTS `role_authority`;
CREATE TABLE `role_authority` (
  `role_id`      BIGINT(18) NOT NULL
  COMMENT '角色id',
  `authority_id` BIGINT(18) NOT NULL
  COMMENT '权限id',
  KEY `role_index` (`role_id`) USING BTREE,
  KEY `authority_index` (`authority_id`) USING BTREE,
  KEY `role_author` (`role_id`, `authority_id`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of role_authority
-- ----------------------------
INSERT INTO `role_authority` VALUES ('1', '1');
INSERT INTO `role_authority` VALUES ('1', '2');
INSERT INTO `role_authority` VALUES ('1', '4');
INSERT INTO `role_authority` VALUES ('1', '397');
INSERT INTO `role_authority` VALUES ('2', '1');
INSERT INTO `role_authority` VALUES ('2', '5');
INSERT INTO `role_authority` VALUES ('2', '6');
INSERT INTO `role_authority` VALUES ('2', '7');
INSERT INTO `role_authority` VALUES ('2', '395');
INSERT INTO `role_authority` VALUES ('2', '397');
INSERT INTO `role_authority` VALUES ('2', '398');
INSERT INTO `role_authority` VALUES ('2', '404');
INSERT INTO `role_authority` VALUES ('2', '500');
INSERT INTO `role_authority` VALUES ('3', '5');
INSERT INTO `role_authority` VALUES ('3', '8');
INSERT INTO `role_authority` VALUES ('3', '40');
INSERT INTO `role_authority` VALUES ('3', '360');
INSERT INTO `role_authority` VALUES ('3', '392');
INSERT INTO `role_authority` VALUES ('3', '395');
INSERT INTO `role_authority` VALUES ('3', '397');
INSERT INTO `role_authority` VALUES ('3', '398');
INSERT INTO `role_authority` VALUES ('6', '1');
INSERT INTO `role_authority` VALUES ('6', '5');
INSERT INTO `role_authority` VALUES ('6', '6');
INSERT INTO `role_authority` VALUES ('6', '394');
INSERT INTO `role_authority` VALUES ('6', '395');
INSERT INTO `role_authority` VALUES ('6', '397');
INSERT INTO `role_authority` VALUES ('6', '398');
INSERT INTO `role_authority` VALUES ('6', '402');
INSERT INTO `role_authority` VALUES ('6', '403');
INSERT INTO `role_authority` VALUES ('6', '404');
INSERT INTO `role_authority` VALUES ('6', '501');
INSERT INTO `role_authority` VALUES ('7', '1');
INSERT INTO `role_authority` VALUES ('7', '5');
INSERT INTO `role_authority` VALUES ('7', '6');
INSERT INTO `role_authority` VALUES ('7', '394');
INSERT INTO `role_authority` VALUES ('7', '395');
INSERT INTO `role_authority` VALUES ('7', '397');
INSERT INTO `role_authority` VALUES ('7', '398');
INSERT INTO `role_authority` VALUES ('7', '401');
INSERT INTO `role_authority` VALUES ('7', '404');
INSERT INTO `role_authority` VALUES ('7', '501');
INSERT INTO `role_authority` VALUES ('8', '1');
INSERT INTO `role_authority` VALUES ('8', '392');
INSERT INTO `role_authority` VALUES ('9', '1');
INSERT INTO `role_authority` VALUES ('10', '1');

-- ----------------------------
-- Table structure for role_create
-- ----------------------------
DROP TABLE IF EXISTS `role_create`;
CREATE TABLE `role_create` (
  `role_id`        BIGINT(18) NOT NULL,
  `create_role_id` BIGINT(18) NOT NULL
  COMMENT '可创建的角色ID'
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of role_create
-- ----------------------------
INSERT INTO `role_create` VALUES ('1', '6');
INSERT INTO `role_create` VALUES ('6', '7');
INSERT INTO `role_create` VALUES ('7', '2');

-- ----------------------------
-- Table structure for rule_sms_template
-- ----------------------------
DROP TABLE IF EXISTS `rule_sms_template`;
CREATE TABLE `rule_sms_template` (
  `id`          BIGINT(18)    NOT NULL AUTO_INCREMENT,
  `name`        VARCHAR(100)  NOT NULL,
  `type`        VARCHAR(6)             DEFAULT NULL
  COMMENT '短信类型',
  `create_time` DATETIME      NOT NULL,
  `update_time` DATETIME      NOT NULL,
  `delete_flag` INT(2)        NOT NULL,
  `content`     VARCHAR(1000) NOT NULL,
  `creator_id`  BIGINT(18)    NOT NULL,
  `role_id`     BIGINT(18)    NOT NULL
  COMMENT '角色id',
  `type_name`   VARCHAR(10)            DEFAULT NULL
  COMMENT '短信类型名称',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of rule_sms_template
-- ----------------------------

-- ----------------------------
-- Table structure for rule_template
-- ----------------------------
DROP TABLE IF EXISTS `rule_template`;
CREATE TABLE `rule_template` (
  `id`          BIGINT(18)   NOT NULL AUTO_INCREMENT,
  `name`        VARCHAR(100) NOT NULL,
  `image`       VARCHAR(255)          DEFAULT NULL,
  `creator_id`  BIGINT(18)   NOT NULL,
  `role_id`     BIGINT(18)   NOT NULL
  COMMENT '色角id',
  `create_time` DATETIME     NOT NULL,
  `update_time` DATETIME     NOT NULL,
  `image_cnt`   INT(11)               DEFAULT NULL,
  `delete_flag` INT(1)       NOT NULL,
  `status`      INT(1)       NOT NULL
  COMMENT ' 0为下架, 1为上架, 2为全部',
  `title`       VARCHAR(100) NOT NULL
  COMMENT '活动主题',
  `description` TEXT         NOT NULL
  COMMENT '红包描述',
  `people`      VARCHAR(255) NOT NULL
  COMMENT '活动对象',
  `activityDes` VARCHAR(255)          DEFAULT NULL
  COMMENT '活动说明(活动规则)',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 10
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of rule_template
-- ----------------------------

-- ----------------------------
-- Table structure for rule_type
-- ----------------------------
DROP TABLE IF EXISTS `rule_type`;
CREATE TABLE `rule_type` (
  `id`   BIGINT(18)   NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL
  COMMENT '规则名称',
  `type` VARCHAR(4)   NOT NULL
  COMMENT '活动类型：R：红包流量赠送，redPacketTask；M：包月赠送，monthlyTask；F：流量卡充值业务，flowCardTask；G：转盘游戏充值业务，gameTask\r\n',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of rule_type
-- ----------------------------

-- ----------------------------
-- Table structure for send_msg
-- ----------------------------
DROP TABLE IF EXISTS `send_msg`;
CREATE TABLE `send_msg` (
  `id`          BIGINT(18)   NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  `mobile`      VARCHAR(18)  NOT NULL
  COMMENT '手机号码',
  `content`     VARCHAR(300) NOT NULL
  COMMENT '短信内容',
  `type`        INT(1)       NOT NULL
  COMMENT '短信类型 0为登陆随机验证码',
  `status`      INT(1)       NOT NULL
  COMMENT '0为已发送 1为已发送成功 2为发送失败',
  `send_time`   DATETIME     NOT NULL
  COMMENT '发送时间',
  `delete_flag` INT(1)       NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_all` (`mobile`, `type`, `status`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of send_msg
-- ----------------------------

-- ----------------------------
-- Table structure for sms_record
-- ----------------------------
DROP TABLE IF EXISTS `sms_record`;
CREATE TABLE `sms_record` (
  `id`          BIGINT(18)   NOT NULL AUTO_INCREMENT,
  `mobile`      VARCHAR(11)  NOT NULL
  COMMENT '手机号码',
  `content`     VARCHAR(200) NOT NULL
  COMMENT '验证码',
  `create_time` DATETIME     NOT NULL
  COMMENT '创建时间',
  `update_time` DATETIME              DEFAULT NULL
  COMMENT '更新时间',
  `delete_flag` INT(1)       NOT NULL DEFAULT '0'
  COMMENT '删除标记， 0:未删除；1：已删除',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 313885
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of sms_record
-- ----------------------------

-- ----------------------------
-- Table structure for sms_template
-- ----------------------------
DROP TABLE IF EXISTS `sms_template`;
CREATE TABLE `sms_template` (
  `id`          BIGINT(18)   NOT NULL AUTO_INCREMENT,
  `name`        VARCHAR(50)  NOT NULL,
  `content`     VARCHAR(400) NOT NULL,
  `create_time` DATETIME     NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of sms_template
-- ----------------------------
INSERT INTO `sms_template`
VALUES ('1', '20158149', '尊敬的用户，您已获得{0}MB国内流量，当月有效。感谢您的使用，如有疑问详询10086', '2016-03-27 10:16:49');
INSERT INTO `sms_template`
VALUES ('2', '20158150', '尊敬的用户，{1}向您赠送了{0}MB国内流量，当月有效，感谢您的支持！', '2016-03-27 10:17:13');
INSERT INTO `sms_template`
VALUES ('3', '20160145', '尊敬的用户，您已经获赠{0}MB国内流量，当月有效，感谢您的支持！', '2016-03-27 10:17:53');

-- ----------------------------
-- Table structure for userbalance_record
-- ----------------------------
DROP TABLE IF EXISTS `userbalance_record`;
CREATE TABLE `userbalance_record` (
  `id`             BIGINT(18)  NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  `mobile`         VARCHAR(12) NOT NULL
  COMMENT '待充值用户手机号',
  `creator_id`     BIGINT(18)  NOT NULL
  COMMENT '创建该条记录的管理员id',
  `enter_id`       BIGINT(18)           DEFAULT NULL,
  `pro_id`         BIGINT(18)  NOT NULL
  COMMENT '充值产品id',
  `validality`     DATETIME    NOT NULL
  COMMENT '有效期',
  `chargeMobile`   VARCHAR(12)          DEFAULT NULL
  COMMENT '充值的手机号码',
  `status`         INT(4)      NOT NULL
  COMMENT '状态  0:未充值 1:充值中 2：充值成功 3：充值失败 4：已过期',
  `charge_time`    DATETIME             DEFAULT NULL
  COMMENT '充值时间',
  `faileur_reason` VARCHAR(100)         DEFAULT NULL
  COMMENT '充值错误原因',
  `create_time`    DATETIME    NOT NULL
  COMMENT '创建时间',
  `update_time`    DATETIME             DEFAULT NULL
  COMMENT '更新时间',
  `delete_flag`    INT(1)      NOT NULL DEFAULT '0'
  COMMENT '删除Flag，0:未删除；1：已删除',
  `version`        INT(4)               DEFAULT '0'
  COMMENT '版本号',
  `owner_mobile`   VARCHAR(12)          DEFAULT NULL,
  `sys_serial_num` VARCHAR(50)          DEFAULT NULL
  COMMENT '系统流水号',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of userbalance_record
-- ----------------------------

-- ----------------------------
-- Table structure for user_gain_record
-- ----------------------------
DROP TABLE IF EXISTS `user_gain_record`;
CREATE TABLE `user_gain_record` (
  `id`           BIGINT(18)  NOT NULL AUTO_INCREMENT
  COMMENT '赚流量接口记录ID',
  `ent_id`       BIGINT(18)  NOT NULL
  COMMENT '企业标识， 指向enterprise的id字段',
  `user_id`      BIGINT(18)  NOT NULL
  COMMENT '用户标识，指向user_info的id字段',
  `prd_id`       BIGINT(18)           DEFAULT NULL
  COMMENT '产品ID，指向product的id字段, 为空时代表没有中奖',
  `status`       INT(4)      NOT NULL
  COMMENT '充值状态, 0代表成功， 1代表失败, 2代表待充值',
  `extend_param` VARCHAR(50)          DEFAULT NULL
  COMMENT '游戏的附加参数',
  `source_name`  VARCHAR(50) NOT NULL
  COMMENT '来源名称',
  `create_time`  DATETIME    NOT NULL
  COMMENT '创建时间',
  `update_time`  DATETIME             DEFAULT NULL
  COMMENT '更新时间',
  `delete_flag`  INT(1)      NOT NULL
  COMMENT '删除Flag，0:未删除；1：已删除',
  PRIMARY KEY (`id`),
  KEY `ent_id_index` (`ent_id`, `delete_flag`) USING BTREE,
  KEY `mobile_index` (`user_id`, `delete_flag`) USING BTREE,
  KEY `get_index` (`user_id`, `source_name`, `delete_flag`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of user_gain_record
-- ----------------------------

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `id`          BIGINT(18)  NOT NULL AUTO_INCREMENT
  COMMENT '移动用户ID',
  `mobile`      VARCHAR(11) NOT NULL
  COMMENT '手机号码',
  `create_time` DATETIME    NOT NULL
  COMMENT '创建时间',
  `update_time` DATETIME             DEFAULT NULL
  COMMENT '更新时间',
  `delete_flag` INT(1)      NOT NULL DEFAULT '0'
  COMMENT '删除标记， 0:未删除；1：已删除',
  PRIMARY KEY (`id`),
  KEY `openid_index` (`delete_flag`) USING BTREE,
  KEY `mobile_phone_index` (`mobile`, `delete_flag`) USING BTREE,
  KEY `mobile_index2` (`mobile`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of user_info
-- ----------------------------

-- ----------------------------
-- Table structure for white_list
-- ----------------------------
DROP TABLE IF EXISTS `white_list`;
CREATE TABLE `white_list` (
  `id`          BIGINT(18)  NOT NULL AUTO_INCREMENT
  COMMENT 'ID',
  `mobile`      VARCHAR(11) NOT NULL
  COMMENT '手机号',
  `create_time` DATETIME    NOT NULL
  COMMENT '创建时间',
  `update_time` DATETIME             DEFAULT NULL
  COMMENT '更新时间',
  `delete_flag` INT(1)      NOT NULL DEFAULT '0'
  COMMENT '删除标记， 0:未删除；1：已删除',
  PRIMARY KEY (`id`),
  KEY `modile_index` (`mobile`, `delete_flag`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of white_list
-- ----------------------------


-- ----------------------------
-- 20160426 modify table product adding default tag
-- ----------------------------
ALTER TABLE `product` ADD `defaultValue` INT(8) DEFAULT '0'
COMMENT '1-默认企业创建时就有该产品，0-默认企业创建时无该产品';

-- ----------------------------
-- 20160426 modify table ent_product
-- ----------------------------
ALTER TABLE `ent_product` DROP `amount`;
ALTER TABLE `ent_product` DROP `version`;

-- ----------------------------
-- 20160428 add authority for product change
-- ----------------------------
INSERT INTO `authority` VALUES
  ('502', NULL, '产品变更', 'ROLE_ENTERPRISE_PRODUCTCHANGE', '103010', NULL, '2016-04-28 08:53:17',
          NULL, NULL,
          '2016-04-28 08:53:20', '0');
INSERT INTO `authority` VALUES
  ('503', NULL, '产品变更审批', 'ROLE_ENTERPRISE_PRODUCTCHANGE_APPROVAL', '103011', NULL,
          '2016-04-28 08:53:17', NULL, NULL,
          '2016-04-28 08:53:20', '0');
INSERT INTO `role_authority` VALUES ('2', '502');
INSERT INTO `role_authority` VALUES ('6', '503');
INSERT INTO `role_authority` VALUES ('7', '503');
-- ------------------------
-- 20160429 add type for account
-- ------------------------
ALTER TABLE `account`
ADD COLUMN `type` INTEGER(8) NOT NULL
COMMENT '-1为企业帐户，0为红包帐户，1为大转盘帐户，2为流量卡帐户，3为营销卡帐户'
AFTER `product_id`;

-- -------------------------
-- 20160505 add type for golden_ball
-- ------------------------

DROP TABLE IF EXISTS `goldenball_activity`;
CREATE TABLE `goldenball_activity` (
  `id`               BIGINT(18)   NOT NULL AUTO_INCREMENT,
  `activity_id`      VARCHAR(225) NOT NULL
  COMMENT '活动ID',
  `name`             VARCHAR(64)           DEFAULT NULL
  COMMENT '活动名称',
  `charge_type`      INT(11)               DEFAULT NULL
  COMMENT '充值方式,是否延后充值:0立即充值；1延后充值',
  `charge_url`       VARCHAR(255)          DEFAULT NULL
  COMMENT '充值接口地址',
  `flow_type`        INT(2)                DEFAULT NULL
  COMMENT '流量类型，0:流量包',
  `start_time`       DATETIME              DEFAULT NULL
  COMMENT '活动开始时间',
  `end_time`         DATETIME              DEFAULT NULL
  COMMENT '活动结束时间',
  `max_play_number`  INT(18)               DEFAULT NULL
  COMMENT '抽奖次数',
  `gived_number`     INT(18)               DEFAULT NULL
  COMMENT '中奖次数',
  `lottery_type`     INT(2)                DEFAULT NULL
  COMMENT '抽奖类型：0:活动期间只能抽lottery_count次；1:每天能抽取lottery_count次',
  `probability_type` INT(2)                DEFAULT NULL
  COMMENT '中奖概率:0固定概率，奖项概率需手动设置；1非固定概率，活动期间奖项概率自动调整，无需设置',
  `creator_id`       BIGINT(18)            DEFAULT NULL
  COMMENT '动活创建者',
  `create_time`      DATETIME              DEFAULT NULL,
  `update_time`      DATETIME              DEFAULT NULL,
  `status`           INT(4)                DEFAULT NULL
  COMMENT '活动状态1:未开始；2进行中；3已结束；4已删除',
  `url`              VARCHAR(255)          DEFAULT NULL
  COMMENT '砸金蛋地址',
  `code`             VARCHAR(10)           DEFAULT NULL
  COMMENT '返回代码；200正确；其余错误',
  `rule`             VARCHAR(255)          DEFAULT NULL,
  `delete_flag`      INT(1)                DEFAULT NULL
  COMMENT '删除标记:0未删除；1已删除',
  `ent_id`           BIGINT(18)            DEFAULT NULL
  COMMENT '活动绑定的企业ID',
  `check_type`       INT(18)               DEFAULT NULL
  COMMENT '鉴权方式',
  `check_url`        VARCHAR(255)          DEFAULT NULL
  COMMENT '鉴权url',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `goldenball_record`;
CREATE TABLE `goldenball_record` (
  `activity_id`     VARCHAR(50)  NOT NULL
  COMMENT '活动ID',
  `id`              BIGINT(18)   NOT NULL AUTO_INCREMENT
  COMMENT '记录ID',
  `product_code`    VARCHAR(50)  NOT NULL
  COMMENT '品产编码',
  `enterprise_code` VARCHAR(50)  NOT NULL
  COMMENT '企业编码',
  `mobile`          VARCHAR(15)  NOT NULL
  COMMENT '奖中手机号码',
  `product_name`    VARCHAR(255) NOT NULL
  COMMENT '产品名称',
  `win_time`        DATETIME     NOT NULL
  COMMENT '中奖时间',
  `create_time`     DATETIME     NOT NULL,
  `rank_name`       VARCHAR(20)  NOT NULL
  COMMENT '几等奖',
  `status`          INT(4)       NOT NULL
  COMMENT '充值状态',
  `reason`          VARCHAR(100)          DEFAULT NULL
  COMMENT '败失原因',
  `delete_flag`     INT(1)                DEFAULT NULL
  COMMENT '除删标识：0未删除；1已删除',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

INSERT INTO `authority` VALUES
  ('504', NULL, '砸金蛋', 'ROLE_GOLDENBALL', '104006', NULL, '2016-05-05 10:28:14', NULL, NULL,
          '2016-05-05 10:28:18',
          '0');
INSERT INTO `role_authority` VALUES ('3', '504');

-- -----------------------------------
-- 20160506 add serialNum in charge_record
-- -----------------------------------
ALTER TABLE `charge_record`
ADD COLUMN `serial_num` VARCHAR(50) COMMENT 'EC接口企业序列号'
AFTER `aName`;

-- ---------------------------------------
-- 20160516 add tables of flowcard
-- ---------------------------------------
DROP TABLE IF EXISTS `mdrc_batch_config`;
CREATE TABLE `mdrc_batch_config` (
  `id`             BIGINT(18)  NOT NULL AUTO_INCREMENT
  COMMENT '标识',
  `config_name`    VARCHAR(18) NOT NULL
  COMMENT '配置名称',
  `manager_id`     BIGINT(18)  NOT NULL
  COMMENT '客户经理标识',
  `cardmaker_id`   BIGINT(18)  NOT NULL,
  `product_id`     BIGINT(18)  NOT NULL
  COMMENT '产品ID',
  `amount`         BIGINT(18)  NOT NULL
  COMMENT '生成卡记录条数',
  `province_code`  VARCHAR(2)  NOT NULL
  COMMENT '省份编码',
  `this_year`      VARCHAR(4)  NOT NULL
  COMMENT '当前年份',
  `serial_number`  INT(8)      NOT NULL
  COMMENT '批号',
  `create_time`    DATETIME    NOT NULL
  COMMENT '记录创建时间',
  `creator_id`     BIGINT(18)  NOT NULL
  COMMENT '创建者标识',
  `status`         TINYINT(1)  NOT NULL
  COMMENT '规则状态，1已生成卡数据记录， 2卡密码生成完毕，可下载 3已下载',
  `download_time`  DATETIME             DEFAULT NULL
  COMMENT '下载时间',
  `download_ip`    VARCHAR(18)          DEFAULT NULL
  COMMENT '下载者ip地址',
  `excel_password` VARCHAR(32)          DEFAULT NULL
  COMMENT ' excel密码',
  `noti_flag`      INT(1)      NOT NULL
  COMMENT '是否下发通知短信：0未下发通知短信，1已下发通知短信',
  `noti_time`      DATETIME             DEFAULT NULL
  COMMENT '下发通知短信时间',
  PRIMARY KEY (`id`),
  KEY `province_year` (`province_code`, `this_year`) USING BTREE,
  KEY `creator_status` (`creator_id`, `status`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '营销卡规则表';

-- ----------------------------
-- Table structure for `mdrc_card_info`
-- ----------------------------
DROP TABLE IF EXISTS `mdrc_card_info`;
CREATE TABLE `mdrc_card_info` (
  `id`             BIGINT(18)  NOT NULL AUTO_INCREMENT
  COMMENT '标识',
  `config_id`      BIGINT(18)  NOT NULL
  COMMENT '生成规则标识',
  `card_number`    VARCHAR(27) NOT NULL
  COMMENT '序列号',
  `card_password`  VARCHAR(32)          DEFAULT NULL
  COMMENT '密码',
  `status`         TINYINT(1)  NOT NULL
  COMMENT '卡状态，1新制卡, 2已入库、3已激活、4已绑定、5已使用、6已过期、7已锁定',
  `user_mobile`    VARCHAR(11)          DEFAULT NULL
  COMMENT '使用者手机号',
  `user_ip`        VARCHAR(15)          DEFAULT NULL
  COMMENT '使用者IP地址',
  `deadline`       DATETIME             DEFAULT NULL
  COMMENT '失效日期',
  `create_time`    DATETIME    NOT NULL
  COMMENT '记录创建时间',
  `stored_time`    DATETIME             DEFAULT NULL
  COMMENT '入库时间',
  `activated_time` DATETIME             DEFAULT NULL
  COMMENT '激活时间',
  `bound_time`     DATETIME             DEFAULT NULL
  COMMENT '绑定时间',
  `used_time`      DATETIME             DEFAULT NULL
  COMMENT '使用时间',
  `locked_time`    DATETIME             DEFAULT NULL
  COMMENT '锁定时间',
  `enter_id`       BIGINT(18)           DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `configId_status` (`config_id`, `status`) USING BTREE,
  KEY `cardnumber_index` (`card_number`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '营销卡记录表';

-- ----------------------------
-- Table structure for `mdrc_cardmaker`
-- ----------------------------
DROP TABLE IF EXISTS `mdrc_cardmaker`;
CREATE TABLE `mdrc_cardmaker` (
  `id`            BIGINT(18)   NOT NULL AUTO_INCREMENT
  COMMENT '标识，主键',
  `name`          VARCHAR(100) NOT NULL
  COMMENT '制卡商名称',
  `serial_number` VARCHAR(2)   NOT NULL
  COMMENT '制卡商编号',
  `create_time`   DATETIME     NOT NULL
  COMMENT '记录创建时间',
  `operator_id`   BIGINT(20)            DEFAULT NULL
  COMMENT '专员标识',
  `delete_flag`   TINYINT(4)   NOT NULL
  COMMENT '删除标记位',
  `public_key`    TEXT COMMENT '公钥',
  `private_key`   TEXT COMMENT '私钥',
  `creator_id`    BIGINT(18)   NOT NULL
  COMMENT '创建者标识',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '营销卡制卡商表';

-- ------------------------------------
-- 20160517 modify comment by wujiamin
-- ------------------------------------
ALTER TABLE `account_change_request` MODIFY COLUMN `status` INT COMMENT '0为已保存， 1为待市级管理员审批，2为待省级管理员1审批，3为待省级管理员2审批，4为审批通过，5为审批驳回，6为取消申请';

-- ------------------------------------
-- 20160518 add account change record by wujiamin
-- ------------------------------------
INSERT INTO `authority` VALUES
  ('505', NULL, '企业充值记录', 'ROLE_ACCOUNT_CHANGE_RECORD', '100602', NULL, '2016-05-18 10:28:14', NULL,
          NULL,
          '2016-05-18 10:28:18', '0');
INSERT INTO `role_authority` VALUES ('2', '505');

-- -----------------------------------
-- 20160519 add last_operator_id in account_change_request by wujiamin
-- -----------------------------------
ALTER TABLE `account_change_request`
ADD COLUMN `last_operator_id` BIGINT(18) DEFAULT NULL
COMMENT '最后一个操作该申请的用户id';

-- 20160524 add system_num and boss_num in charge_record by lilin
ALTER TABLE `charge_record`
ADD COLUMN `system_num` VARCHAR(50)
CHARACTER SET utf8
COLLATE utf8_general_ci NULL DEFAULT NULL
COMMENT '流量平侧流水号'
AFTER `aName`,
ADD COLUMN `boss_num` VARCHAR(50)
CHARACTER SET utf8
COLLATE utf8_general_ci NULL DEFAULT NULL
COMMENT 'boss侧流水号'
AFTER `serial_num`;

-- ----------------------------
-- 增加product表的主键索引, 2016/05/30, by sunyiwei
-- ----------------------------
ALTER TABLE `product`
ADD INDEX `id_index` (`id`) USING BTREE;

-- ----------------------------
-- 增加enterprises表的appkey索引, 2016/05/30, by sunyiwei
-- ----------------------------
ALTER TABLE `enterprises`
ADD INDEX `appkey_index` (`app_key`) USING BTREE;

-- -------------------------------
-- 20160530 add status for enterprise by wujiamin
-- -------------------------------
ALTER TABLE `enterprises`
ADD COLUMN `status` TINYINT NULL
COMMENT '1-体验企业，2-认证企业，3-合作企业'
AFTER `update_time`;

-- --------------------------------
-- 20160530 add new enterprise_approval_record by wujiamin
-- --------------------------------
DROP TABLE IF EXISTS `enterprise_approval_record`;
CREATE TABLE `enterprise_approval_record` (
  `id`               BIGINT(18) NOT NULL AUTO_INCREMENT,
  `ent_id`           BIGINT(18) NOT NULL,
  `type`             TINYINT(4) NOT NULL
  COMMENT '流程类型：1-体验企业到认证企业；2-认证企业到合作企业',
  `operator_id`      BIGINT(18) NOT NULL,
  `operator_comment` VARCHAR(310)        DEFAULT NULL,
  `serial_number`    VARCHAR(50)         DEFAULT NULL,
  `origin_status`    INT(10)    NOT NULL
  COMMENT '记录原企业状态',
  `new_status`       INT(10)    NOT NULL
  COMMENT '记录新企业状态',
  `create_time`      DATETIME   NOT NULL,
  `update_time`      DATETIME            DEFAULT NULL,
  `isNew`            TINYINT(4) NOT NULL
  COMMENT '是否是最新的一条记录，最新记录标识为1，不是最新记录标识为0',
  `delete_flag`      TINYINT(4) NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ---------------------------------
-- 20160530 modify enterprise_file by wujiamin
-- ---------------------------------
ALTER TABLE `enterprise_file`
MODIFY COLUMN `customerfile_name` VARCHAR(500)
CHARACTER SET utf8
COLLATE utf8_general_ci NULL DEFAULT NULL
COMMENT '客户说明附件'
AFTER `ent_id`,
MODIFY COLUMN `image_name` VARCHAR(500)
CHARACTER SET utf8
COLLATE utf8_general_ci NULL DEFAULT NULL
COMMENT '审批截图'
AFTER `customerfile_name`,
MODIFY COLUMN `contract_name` VARCHAR(500)
CHARACTER SET utf8
COLLATE utf8_general_ci NULL DEFAULT NULL
COMMENT '合作协议文件'
AFTER `image_name`,
ADD COLUMN `business_licence` VARCHAR(500) NULL
COMMENT '企业工商营业执照'
AFTER `contract_name`,
ADD COLUMN `authorization_certificate` VARCHAR(500) NULL
COMMENT '企业管理员授权证明'
AFTER `business_licence`,
ADD COLUMN `identification_card` VARCHAR(500) NULL
COMMENT '企业管理员身份证'
AFTER `authorization_certificate`,
ADD COLUMN `customerfile_key` VARCHAR(50) NULL
AFTER `identification_card`,
ADD COLUMN `image_key` VARCHAR(50) NULL
AFTER `customerfile_key`,
ADD COLUMN `contract_key` VARCHAR(50) NULL
AFTER `image_key`,
ADD COLUMN `licence_key` VARCHAR(50) NULL
AFTER `contract_key`,
ADD COLUMN `authorization_key` VARCHAR(50) NULL
AFTER `licence_key`,
ADD COLUMN `identification_key` VARCHAR(50) NULL
AFTER `authorization_key`;

-- ----------------------------
-- 增加流量卡相关的数据库表, 2016.05.31, by sunyiwei
-- Table structure for `mdrc_batch_config`
-- ----------------------------
DROP TABLE IF EXISTS `mdrc_batch_config`;
CREATE TABLE `mdrc_batch_config` (
  `id`             BIGINT(18)  NOT NULL AUTO_INCREMENT
  COMMENT '标识',
  `config_name`    VARCHAR(18) NOT NULL
  COMMENT '配置名称',
  `manager_id`     BIGINT(18)  NOT NULL
  COMMENT '客户经理标识',
  `cardmaker_id`   BIGINT(18)  NOT NULL,
  `product_id`     BIGINT(18)  NOT NULL
  COMMENT '产品ID',
  `amount`         BIGINT(18)  NOT NULL
  COMMENT '生成卡记录条数',
  `province_code`  VARCHAR(2)  NOT NULL
  COMMENT '省份编码',
  `this_year`      VARCHAR(4)  NOT NULL
  COMMENT '当前年份',
  `serial_number`  INT(8)      NOT NULL
  COMMENT '批号',
  `create_time`    DATETIME    NOT NULL
  COMMENT '记录创建时间',
  `creator_id`     BIGINT(18)  NOT NULL
  COMMENT '创建者标识',
  `status`         TINYINT(1)  NOT NULL
  COMMENT '规则状态，1已生成卡数据记录， 2卡密码生成完毕，可下载 3已下载',
  `download_time`  DATETIME             DEFAULT NULL
  COMMENT '下载时间',
  `download_ip`    VARCHAR(18)          DEFAULT NULL
  COMMENT '下载者ip地址',
  `excel_password` VARCHAR(32)          DEFAULT NULL
  COMMENT ' excel密码',
  `noti_flag`      INT(1)      NOT NULL
  COMMENT '是否下发通知短信：0未下发通知短信，1已下发通知短信',
  `noti_time`      DATETIME             DEFAULT NULL
  COMMENT '下发通知短信时间',
  PRIMARY KEY (`id`),
  KEY `province_year` (`province_code`, `this_year`) USING BTREE,
  KEY `creator_status` (`creator_id`, `status`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 10
  DEFAULT CHARSET = utf8
  COMMENT = '营销卡规则表';

-- ----------------------------
-- Table structure for `mdrc_card_info`
-- ----------------------------
DROP TABLE IF EXISTS `mdrc_card_info_16`;
CREATE TABLE `mdrc_card_info_16` (
  `id`             BIGINT(18)  NOT NULL AUTO_INCREMENT
  COMMENT '标识',
  `config_id`      BIGINT(18)  NOT NULL
  COMMENT '生成规则标识',
  `card_number`    VARCHAR(27) NOT NULL
  COMMENT '序列号',
  `card_password`  VARCHAR(32)          DEFAULT NULL
  COMMENT '密码',
  `status`         TINYINT(1)  NOT NULL
  COMMENT '卡状态，1新制卡, 2已入库、3已激活、4已绑定、5已使用、6已过期、7已锁定',
  `user_mobile`    VARCHAR(11)          DEFAULT NULL
  COMMENT '使用者手机号',
  `user_ip`        VARCHAR(15)          DEFAULT NULL
  COMMENT '使用者IP地址',
  `deadline`       DATETIME             DEFAULT NULL
  COMMENT '失效日期',
  `create_time`    DATETIME    NOT NULL
  COMMENT '记录创建时间',
  `stored_time`    DATETIME             DEFAULT NULL
  COMMENT '入库时间',
  `activated_time` DATETIME             DEFAULT NULL
  COMMENT '激活时间',
  `bound_time`     DATETIME             DEFAULT NULL
  COMMENT '绑定时间',
  `used_time`      DATETIME             DEFAULT NULL
  COMMENT '使用时间',
  `locked_time`    DATETIME             DEFAULT NULL
  COMMENT '锁定时间',
  `enter_id`       BIGINT(18)           DEFAULT NULL,
  `product_id`     BIGINT(18)           DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `configId_status` (`config_id`, `status`) USING BTREE,
  KEY `cardnumber_index` (`card_number`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '营销卡记录表';

-- ----------------------------
-- Records of mdrc_card_info
-- ----------------------------

-- ----------------------------
-- Table structure for `mdrc_card_info_17`
-- ----------------------------
DROP TABLE IF EXISTS `mdrc_card_info_17`;
CREATE TABLE `mdrc_card_info_17` (
  `id`             BIGINT(18)  NOT NULL AUTO_INCREMENT,
  `config_id`      BIGINT(18)  NOT NULL,
  `card_number`    VARCHAR(27) NOT NULL,
  `card_password`  VARCHAR(32)          DEFAULT NULL,
  `status`         TINYINT(1)  NOT NULL,
  `user_mobile`    VARCHAR(11)          DEFAULT NULL,
  `user_ip`        VARCHAR(15)          DEFAULT NULL,
  `deadline`       DATETIME             DEFAULT NULL,
  `create_time`    DATETIME    NOT NULL,
  `stored_time`    DATETIME             DEFAULT NULL,
  `activated_time` DATETIME             DEFAULT NULL,
  `bound_time`     DATETIME             DEFAULT NULL,
  `used_time`      DATETIME             DEFAULT NULL,
  `locked_time`    DATETIME             DEFAULT NULL,
  `enter_id`       BIGINT(18)           DEFAULT NULL,
  `product_id`     BIGINT(18)           DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `configId_status` (`config_id`, `status`) USING BTREE,
  KEY `cardnumber_index` (`card_number`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '营销卡记录表';

-- ----------------------------
-- Table structure for `mdrc_card_info_18`
-- ----------------------------
DROP TABLE IF EXISTS `mdrc_card_info_18`;
CREATE TABLE `mdrc_card_info_18` (
  `id`             BIGINT(18)  NOT NULL AUTO_INCREMENT
  COMMENT '标识',
  `config_id`      BIGINT(18)  NOT NULL
  COMMENT '生成规则标识',
  `card_number`    VARCHAR(27) NOT NULL
  COMMENT '序列号',
  `card_password`  VARCHAR(32)          DEFAULT NULL
  COMMENT '密码',
  `status`         TINYINT(1)  NOT NULL
  COMMENT '卡状态，1新制卡, 2已入库、3已激活、4已绑定、5已使用、6已过期、7已锁定',
  `user_mobile`    VARCHAR(11)          DEFAULT NULL
  COMMENT '使用者手机号',
  `user_ip`        VARCHAR(15)          DEFAULT NULL
  COMMENT '使用者IP地址',
  `deadline`       DATETIME             DEFAULT NULL
  COMMENT '失效日期',
  `create_time`    DATETIME    NOT NULL
  COMMENT '记录创建时间',
  `stored_time`    DATETIME             DEFAULT NULL
  COMMENT '入库时间',
  `activated_time` DATETIME             DEFAULT NULL
  COMMENT '激活时间',
  `bound_time`     DATETIME             DEFAULT NULL
  COMMENT '绑定时间',
  `used_time`      DATETIME             DEFAULT NULL
  COMMENT '使用时间',
  `locked_time`    DATETIME             DEFAULT NULL
  COMMENT '锁定时间',
  `enter_id`       BIGINT(18)           DEFAULT NULL,
  `product_id`     BIGINT(18)           DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `configId_status` (`config_id`, `status`) USING BTREE,
  KEY `cardnumber_index` (`card_number`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '营销卡记录表';

-- ----------------------------
-- Records of mdrc_card_info_15
-- ----------------------------

-- ----------------------------
-- Table structure for `mdrc_card_info_19`
-- ----------------------------
DROP TABLE IF EXISTS `mdrc_card_info_19`;
CREATE TABLE `mdrc_card_info_19` (
  `id`             BIGINT(18)  NOT NULL AUTO_INCREMENT
  COMMENT '标识',
  `config_id`      BIGINT(18)  NOT NULL
  COMMENT '生成规则标识',
  `card_number`    VARCHAR(27) NOT NULL
  COMMENT '序列号',
  `card_password`  VARCHAR(32)          DEFAULT NULL
  COMMENT '密码',
  `status`         TINYINT(1)  NOT NULL
  COMMENT '卡状态，1新制卡, 2已入库、3已激活、4已绑定、5已使用、6已过期、7已锁定',
  `user_mobile`    VARCHAR(11)          DEFAULT NULL
  COMMENT '使用者手机号',
  `user_ip`        VARCHAR(15)          DEFAULT NULL
  COMMENT '使用者IP地址',
  `deadline`       DATETIME             DEFAULT NULL
  COMMENT '失效日期',
  `create_time`    DATETIME    NOT NULL
  COMMENT '记录创建时间',
  `stored_time`    DATETIME             DEFAULT NULL
  COMMENT '入库时间',
  `activated_time` DATETIME             DEFAULT NULL
  COMMENT '激活时间',
  `bound_time`     DATETIME             DEFAULT NULL
  COMMENT '绑定时间',
  `used_time`      DATETIME             DEFAULT NULL
  COMMENT '使用时间',
  `locked_time`    DATETIME             DEFAULT NULL
  COMMENT '锁定时间',
  `enter_id`       BIGINT(18)           DEFAULT NULL,
  `product_id`     BIGINT(18)           DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `configId_status` (`config_id`, `status`) USING BTREE,
  KEY `cardnumber_index` (`card_number`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 18001
  DEFAULT CHARSET = utf8
  COMMENT = '营销卡记录表';

-- ----------------------------
-- Table structure for `mdrc_cardmaker`
-- ----------------------------
DROP TABLE IF EXISTS `mdrc_cardmaker`;
CREATE TABLE `mdrc_cardmaker` (
  `id`            BIGINT(18)   NOT NULL AUTO_INCREMENT
  COMMENT '标识，主键',
  `name`          VARCHAR(100) NOT NULL
  COMMENT '制卡商名称',
  `serial_number` VARCHAR(2)   NOT NULL
  COMMENT '制卡商编号',
  `create_time`   DATETIME     NOT NULL
  COMMENT '记录创建时间',
  `operator_id`   BIGINT(20)            DEFAULT NULL
  COMMENT '专员标识',
  `delete_flag`   TINYINT(4)   NOT NULL
  COMMENT '删除标记位',
  `public_key`    TEXT COMMENT '公钥',
  `private_key`   TEXT COMMENT '私钥',
  `creator_id`    BIGINT(18)   NOT NULL
  COMMENT '创建者标识',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = utf8
  COMMENT = '营销卡制卡商表';

-- ----------------------------
-- Table structure for `mdrc_template`
-- ----------------------------
DROP TABLE IF EXISTS `mdrc_template`;
CREATE TABLE `mdrc_template` (
  `id`              BIGINT(18)   NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  `name`            VARCHAR(100) NOT NULL
  COMMENT '模板名称',
  `front_image`     VARCHAR(255)          DEFAULT NULL
  COMMENT '正面预览图',
  `rear_image`      VARCHAR(255)          DEFAULT NULL
  COMMENT '背面预览图',
  `product_id`      BIGINT(18)            DEFAULT NULL
  COMMENT '产品标识',
  `create_time`     DATETIME     NOT NULL
  COMMENT '记录创建时间',
  `delete_flag`     INT(1)       NOT NULL
  COMMENT '删除标记位',
  `delete_time`     DATETIME              DEFAULT NULL
  COMMENT '删除时间',
  `creator_id`      BIGINT(18)   NOT NULL
  COMMENT '创建者ID',
  `theme`           VARCHAR(100)          DEFAULT NULL
  COMMENT '模板主题',
  `resources_count` INT(11)      NOT NULL
  COMMENT '资源文件个数',
  `product_size`    BIGINT(18)            DEFAULT '0'
  COMMENT '产品流量包大小',
  `pro_size`        VARCHAR(100) NOT NULL
  COMMENT '流量包大小（字符型）',
  PRIMARY KEY (`id`),
  KEY `theme_index` (`theme`) USING BTREE,
  KEY `name_index` (`name`, `delete_flag`) USING BTREE,
  KEY `delete_index` (`delete_flag`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;

-- ----------------------------------
-- 20160531 charge for test by wujiamin
-- ----------------------------------
-- INSERT INTO `boss_product_map` VALUES (1, 344, '1', '2016-4-4 18:25:37', '2016-4-18 18:25:43', 0);
-- INSERT INTO `boss_product_info` VALUES (1, 'GUID-PROD-06b436fc0cbee1516fe1b1290ed96ca76b836268', '2016-5-26 16:39:28', '2016-5-26 16:39:30', 0);


-- ----------------------------------
-- 20160531 add licence time for enterprise by wujiamin
-- ----------------------------------
ALTER TABLE `enterprises`
ADD COLUMN `licence_start_time` DATETIME NULL
COMMENT '工商营业执照开始时间'
AFTER `app_key`,
ADD COLUMN `licence_end_time` DATETIME NULL
COMMENT '工商营业执照结束时间'
AFTER `licence_start_time`;

-- ---------------------------
-- 20160531 modify enterprises by wujiamin
-- 去掉必填的企业编码和企业联系电话
-- ----------------------------
ALTER TABLE `enterprises`
MODIFY COLUMN `code` VARCHAR(20)
CHARACTER SET utf8
COLLATE utf8_general_ci NULL
COMMENT '企业代码'
AFTER `name`,
MODIFY COLUMN `phone` VARCHAR(50)
CHARACTER SET utf8
COLLATE utf8_general_ci NULL
COMMENT '企业手机号码'
AFTER `code`;

-- ----------------------------
-- 增加卡的运营状态，by sunyiwei, 2016.06.01
-- ----------------------------
ALTER TABLE `mdrc_card_info_16`
ADD COLUMN `op_status` TINYINT(1) NOT NULL DEFAULT 7
COMMENT '运营状态，7为正常，8为锁定，9为销卡，这个状态值比status有更高的优先级'
AFTER `status`;
ALTER TABLE `mdrc_card_info_17`
ADD COLUMN `op_status` TINYINT(1) NOT NULL DEFAULT 7
COMMENT '运营状态，7为正常，8为锁定，9为销卡，这个状态值比status有更高的优先级'
AFTER `status`;
ALTER TABLE `mdrc_card_info_18`
ADD COLUMN `op_status` TINYINT(1) NOT NULL DEFAULT 7
COMMENT '运营状态，7为正常，8为锁定，9为销卡，这个状态值比status有更高的优先级'
AFTER `status`;
ALTER TABLE `mdrc_card_info_19`
ADD COLUMN `op_status` TINYINT(1) NOT NULL DEFAULT 7
COMMENT '运营状态，7为正常，8为锁定，9为销卡，这个状态值比status有更高的优先级'
AFTER `status`;

-- ----------------------------
-- 增加卡的解锁时间，销卡时间，延期时间，by sunyiwei, 2016.06.01
-- ----------------------------
ALTER TABLE `mdrc_card_info_16`
ADD COLUMN `unlock_time` DATETIME NULL
COMMENT '解锁时间'
AFTER `locked_time`,
ADD COLUMN `delete_time` DATETIME NULL
COMMENT '销卡时间'
AFTER `unlock_time`,
ADD COLUMN `extend_time` DATETIME NULL
COMMENT '延期时间'
AFTER `delete_time`,
ADD COLUMN `deactivate_time` DATETIME NULL
COMMENT '去激活时间'
AFTER `activated_time`;

ALTER TABLE `mdrc_card_info_17`
ADD COLUMN `unlock_time` DATETIME NULL
COMMENT '解锁时间'
AFTER `locked_time`,
ADD COLUMN `delete_time` DATETIME NULL
COMMENT '销卡时间'
AFTER `unlock_time`,
ADD COLUMN `extend_time` DATETIME NULL
COMMENT '延期时间'
AFTER `delete_time`,
ADD COLUMN `deactivate_time` DATETIME NULL
COMMENT '去激活时间'
AFTER `activated_time`;

ALTER TABLE `mdrc_card_info_18`
ADD COLUMN `unlock_time` DATETIME NULL
COMMENT '解锁时间'
AFTER `locked_time`,
ADD COLUMN `delete_time` DATETIME NULL
COMMENT '销卡时间'
AFTER `unlock_time`,
ADD COLUMN `extend_time` DATETIME NULL
COMMENT '延期时间'
AFTER `delete_time`,
ADD COLUMN `deactivate_time` DATETIME NULL
COMMENT '去激活时间'
AFTER `activated_time`;

ALTER TABLE `mdrc_card_info_19`
ADD COLUMN `unlock_time` DATETIME NULL
COMMENT '解锁时间'
AFTER `locked_time`,
ADD COLUMN `delete_time` DATETIME NULL
COMMENT '销卡时间'
AFTER `unlock_time`,
ADD COLUMN `extend_time` DATETIME NULL
COMMENT '延期时间'
AFTER `delete_time`,
ADD COLUMN `deactivate_time` DATETIME NULL
COMMENT '去激活时间'
AFTER `activated_time`;

-- ----------------------------
-- 增加制卡的相关权限，以及mdrc_batch_config的调整，by 调整, 2016.06.01
-- ----------------------------
INSERT INTO `authority`
VALUES (NULL, NULL, '营销卡数据下载', 'ROLE_MDRC_DATADL', '107002', NULL, now(), NULL, NULL, now(), '0');
INSERT INTO `authority`
VALUES
  (NULL, NULL, '营销卡制卡商管理', 'ROLE_MDRC_CARDMAKER_MGMT', '107003', NULL, now(), NULL, NULL, now(),
         '0');
INSERT INTO `authority`
VALUES (NULL, NULL, '营销卡制卡管理', 'ROLE_MDRC_CFG', '107001', NULL, now(), NULL, NULL, now(), '0');
INSERT INTO `authority`
VALUES (NULL, NULL, '营销卡模板管理', 'ROLE_MDRC_TEMPLATE', '107004', NULL, now(), NULL, NULL, now(), '0');


ALTER TABLE `mdrc_batch_config`
CHANGE COLUMN `product_id` `template_id` BIGINT(18) NOT NULL
COMMENT '产品ID'
AFTER `cardmaker_id`;

-- -----------------------------
-- account金额，数据类型修改, 20160601, by wujiamin
-- -----------------------------
ALTER TABLE `account`
MODIFY COLUMN `count` DOUBLE(18, 2) NOT NULL
AFTER `type`;

ALTER TABLE `account_change_request`
MODIFY COLUMN `count` DOUBLE(18, 2) NOT NULL
AFTER `id`;

ALTER TABLE `account_record`
MODIFY COLUMN `count` DOUBLE(18, 2) NOT NULL
COMMENT '变化数量'
AFTER `serial_num`;

-- -----------------------------
-- 20160605, enterprise_approval_record表，增加description, by wujiamin
-- -----------------------------
ALTER TABLE `enterprise_approval_record`
ADD COLUMN `description` VARCHAR(50) NULL
AFTER `delete_flag`;

-- -----------------------------
-- 20160607, 企业表属性取消必填及默认值
-- -----------------------------
ALTER TABLE `enterprises`
MODIFY COLUMN `discount` BIGINT(18) NULL DEFAULT NULL
COMMENT '折扣信息关联到折扣表id'
AFTER `benefit_grade_id`,
MODIFY COLUMN `interface` INT(10) NULL DEFAULT NULL
COMMENT '是否开通接口调用：1-是，0-否'
AFTER `pay_type_id`;

-- -----------------------------
-- 20160613, 供应商信息表
-- -----------------------------
DROP TABLE IF EXISTS `supplier`;
CREATE TABLE `supplier` (
  `id`          BIGINT(18)              NOT NULL AUTO_INCREMENT
  COMMENT '供应商标识',
  `name`        VARCHAR(64)
                CHARACTER SET utf8
                COLLATE utf8_general_ci NOT NULL
  COMMENT '供应商名称',
  `isp`         VARCHAR(2)
                CHARACTER SET utf8
                COLLATE utf8_general_ci DEFAULT NULL
  COMMENT '供应商类型，M：移动；T：电信；U：联通; A: 三网',
  `create_time` DATETIME                DEFAULT NULL
  COMMENT '创建时间',
  `update_time` DATETIME                NULL     DEFAULT NULL
  COMMENT '更新时间',
  `delete_flag` INT(1)                  NOT NULL DEFAULT 0
  COMMENT '删除标记， 0:未删除；1：已删除',
  PRIMARY KEY (`id`)
)
  COMMENT = '供应商信息';

-- -----------------------------
-- 20160613, 供应商产品信息表
-- -----------------------------
DROP TABLE IF EXISTS `supplier_product`;
CREATE TABLE `supplier_product` (
  `id`               BIGINT(18)  NOT NULL AUTO_INCREMENT
  COMMENT '供应商产品ID',
  `supplier_id`      BIGINT(18)  NOT NULL
  COMMENT '提供该产品的供应商ID',
  `code`             VARCHAR(32) NOT NULL
  COMMENT '供应商产品编码',
  `size`             BIGINT(18)  NOT NULL
  COMMENT '流量包大小,以KB为单位',
  `ownership_region` VARCHAR(2)  NOT NULL
  COMMENT 'CN全国，各省用两字母缩写，表示该产品能为哪些区域的手机号码充值',
  `roaming_region`   VARCHAR(2)  NOT NULL
  COMMENT 'CN全国，各省用两字母缩写，表示充值的流量可在哪些区域进行使用',
  `price`            INT(10)     NOT NULL
  COMMENT '买入价格，以分为单位',
  `feature`          VARCHAR(256)         DEFAULT NULL
  COMMENT '供应商定义的产品相关的信息，用JSON字符串表达',
  `create_time`      DATETIME    NOT NULL
  COMMENT '创建时间',
  `update_time`      DATETIME             DEFAULT NULL
  COMMENT '更新时间',
  `delete_flag`      INT(1)      NOT NULL DEFAULT '0'
  COMMENT '删除标记， 0:未删除；1：已删除',
  PRIMARY KEY (`id`),
  KEY `id` (`id`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '供应商定义的产品信息，通过supplier_product_map关联到平台定义的产品，在充值时，只提供平台的产品ID，通过这种关联获取到相应的BOSS产品信息，完成充值';

-- -----------------------------
-- 20160613, 供应商产品与平台产品的对应表
-- -----------------------------
DROP TABLE IF EXISTS `supplier_product_map`;
CREATE TABLE `supplier_product_map` (
  `id`                  BIGINT(18) NOT NULL AUTO_INCREMENT,
  `platform_product_id` BIGINT(18) NOT NULL
  COMMENT '平台定义的产品ID',
  `supplier_product_id` BIGINT(18) NOT NULL
  COMMENT 'BOSS侧定义的产品信息ID',
  `create_time`         DATETIME   NOT NULL
  COMMENT '创建时间',
  `update_time`         DATETIME            DEFAULT NULL
  COMMENT '更新时间',
  `delete_flag`         INT(1)     NOT NULL DEFAULT '0'
  COMMENT '删除标记， 0:未删除；1：已删除',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '平台的产品与BOSS侧定义的产品关联关系表';

-- ----------------------------
-- 20160608 身份证复印件反面文件 wujiamin
-- ----------------------------
ALTER TABLE `enterprise_file`
ADD COLUMN `identification_back` VARCHAR(500) NULL
AFTER `identification_card`,
ADD COLUMN `identification_back_key` VARCHAR(50) NULL
AFTER `identification_key`;

-- -----------------------------
-- 20160612, 接口调用记录表增加fingerprint列， by xujue
-- -----------------------------
ALTER TABLE `interface_record`
ADD COLUMN `fingerprint` VARCHAR(100) DEFAULT NULL
COMMENT '双创接口鉴权成功后返回的企业标识'
AFTER `boss_serial_num`;

-- -----------------------------
-- 20160612, 接口调用记录表增加fingerprint列， by xujue
-- -----------------------------
ALTER TABLE `charge_record`
ADD COLUMN `fingerprint` VARCHAR(100) DEFAULT NULL
COMMENT '双创接口鉴权成功后返回的企业标识'
AFTER `boss_num`;

-- ----------------------------
-- 20160614 审核记录表增加字段
-- ----------------------------
ALTER TABLE `enterprise_approval_record`
ADD COLUMN `is_cooperate` BIGINT(18) NULL
AFTER `description`;
ALTER TABLE `enterprise_approval_record`
ADD COLUMN `creator_id` BIGINT(18) NULL
AFTER `is_cooperate`;

-- ------------------------
-- 20160616 扩大企业表的code字段
-- ------------------------
ALTER TABLE `enterprises`
MODIFY COLUMN `code` VARCHAR(32)
CHARACTER SET utf8
COLLATE utf8_general_ci NULL DEFAULT NULL
COMMENT '企业代码'
AFTER `name`;

-- ------------------------
-- 20160616 默认插入数据库时EC接口字段为0，即不开通EC接口
-- ------------------------
ALTER TABLE `enterprises`
MODIFY COLUMN `interface` INT(10) NOT NULL DEFAULT 0
COMMENT '是否开通接口调用：1-是，0-否'
AFTER `pay_type_id`;

-- ------------------------
-- 20160617 企业折扣默认为无折扣，与discount表中id关联   by xujue
-- ------------------------
ALTER TABLE `enterprises`
MODIFY COLUMN `discount` BIGINT(18) DEFAULT 1
COMMENT '折扣信息关联到折扣表id';

-- -------------------------------
-- --20150613,供应商产品，增加供应商产品名称,wujiamin
-- ------------------------------
ALTER TABLE `supplier_product`
ADD COLUMN `name` VARCHAR(64) NOT NULL
AFTER `id`,
ADD COLUMN `isp` VARCHAR(2) NOT NULL
AFTER `name`,
ADD COLUMN `status` INT(1) NOT NULL
COMMENT '上架-1；下架-0'
AFTER `feature`;

-- -------------------------------
-- -20160614,漫游范围和使用范围，用中文存储,wujiamin
-- -------------------------------
ALTER TABLE `supplier_product`
MODIFY COLUMN `ownership_region` VARCHAR(8)
CHARACTER SET utf8
COLLATE utf8_general_ci NOT NULL
COMMENT '中文显示'
AFTER `size`,
MODIFY COLUMN `roaming_region` VARCHAR(8)
CHARACTER SET utf8
COLLATE utf8_general_ci NOT NULL
COMMENT '中文显示'
AFTER `ownership_region`;

-- ------------------------------
-- 20160614,供应商表增加是否有同步产品接口标识,wujiamin
-- ----------------------------
ALTER TABLE `supplier`
ADD COLUMN `sync` INT(1) DEFAULT NULL
COMMENT '是否有同步产品接口：1-有；0-无'
AFTER `delete_flag`;

-- ----------------------------
-- 20160615 修改目前已有的产品数据结构, by sunyiwei
-- ----------------------------
ALTER TABLE `product`
MODIFY COLUMN `price` INT(10) NOT NULL  DEFAULT 1
AFTER `delete_flag`,
ADD COLUMN `isp` VARCHAR(2)
CHARACTER SET utf8
COLLATE utf8_general_ci NOT NULL
COMMENT '运营商标识，M：移动；T：电信；U：联通'
AFTER `defaultValue`,
ADD COLUMN `ownership_region` VARCHAR(8)
CHARACTER SET utf8
COLLATE utf8_general_ci NOT NULL
COMMENT '中文显示， 表示该产品能为哪些区域的手机号码充值'
AFTER `isp`,
ADD COLUMN `roaming_region` VARCHAR(8)
CHARACTER SET utf8
COLLATE utf8_general_ci NOT NULL
COMMENT '中文显示，表示充值的流量可在哪些区域进行使用'
AFTER `ownership_region`,
MODIFY COLUMN `product_size` BIGINT(18) NOT NULL
COMMENT '流量包大小,以KB为单位'
AFTER `roaming_region`,
MODIFY COLUMN `product_code` VARCHAR(64)
CHARACTER SET utf8
COLLATE utf8_general_ci NOT NULL
AFTER `id`;

DROP TABLE IF EXISTS `platform_product`;

-- 20160615,权限表增加BOSS产品管理权限,wujiamin
-- --------------------------------
INSERT INTO `authority`
VALUES (NULL, NULL, 'BOSS产品管理', 'ROLE_PRODUCT_BOSS', '102003', NULL, now(), NULL, NULL, now(), '0');

-- ------------------------------
-- 20160617, sunyiwei, 增加供应商指纹字段，同时bossService也提供相应的指纹信息，如果两者匹配，说明该bossService能够为该供应商提供的产品进行充值
-- --------------------------------
ALTER TABLE `supplier`
ADD COLUMN `fingerprint` VARCHAR(32)
CHARACTER SET utf8
COLLATE utf8_general_ci NOT NULL
COMMENT '供应商的指纹特征，用于匹配相应的BossService，任何能够提供该指纹数据的BossService就被认为可以为这家供应商产品进行充值'
AFTER `create_time`;

-- ----------------------------
-- 20160615 add authority for supplier, by xujue
-- ----------------------------
INSERT INTO `authority`
VALUES (NULL, NULL, '供应商', 'ROLE_PRODUCT_SUPPLIER', '102005', NULL, now(), NULL, NULL, now(), '0');

-- ----------------------------
-- 20160615 supplier表增加企业名称，企业编码，状态字段, by xujue
-- ----------------------------
ALTER TABLE `supplier`
ADD COLUMN `enter_name` VARCHAR(255) DEFAULT NULL DEFAULT '杭研'
COMMENT '企业名称'
AFTER `isp`,
ADD COLUMN `enter_code` VARCHAR(20) DEFAULT NULL DEFAULT 'hy'
COMMENT '企业编码'
AFTER `enter_name`;

-- -------------------------------
-- 20160616 add cash product by luozuwu
-- ----------------------------
INSERT INTO `product`
VALUES (NULL, "wqe1212", 0, 'sadadas', 1, now(), now(), 0, 1, 0, 'M', '全国', '全国', 1);

-- -------------------------------
-- 20160616,权限表增加平台产品管理权限,wujiamin
-- --------------------------------
INSERT INTO `authority`
VALUES
  (NULL, NULL, '平台产品管理', 'ROLE_PRODUCT_PLATFORM', '102004', NULL, now(), NULL, NULL, now(), '0');

-- ------------------------
-- 20160616 扩大企业表的code字段
-- ------------------------
ALTER TABLE `enterprises`
MODIFY COLUMN `code` VARCHAR(32)
CHARACTER SET utf8
COLLATE utf8_general_ci NULL DEFAULT NULL
COMMENT '企业代码'
AFTER `name`;

-- ------------------------
-- 20160616 默认插入数据库时EC接口字段为0，即不开通EC接口
-- ------------------------
ALTER TABLE `enterprises`
MODIFY COLUMN `interface` INT(10) NOT NULL DEFAULT 0
COMMENT '是否开通接口调用：1-是，0-否'
AFTER `pay_type_id`;

-- ------------------------
-- 20160617 企业折扣默认为无折扣，与discount表中id关联   by xujue
-- ------------------------
ALTER TABLE `enterprises`
MODIFY COLUMN `discount` BIGINT(18) DEFAULT 1
COMMENT '折扣信息关联到折扣表id';

-- ----------------------------
-- 20160621 审核记录表增加字段
-- ----------------------------
ALTER TABLE `enterprise_approval_record`
ADD COLUMN `version` INT(10) NOT NULL
AFTER `creator_id`;

-- ---------------------
-- 20160622 统一设置为InnoDB
-- ---------------------
ALTER TABLE `activity_prize`
ENGINE = InnoDB;

ALTER TABLE `app_info`
ENGINE = InnoDB;

ALTER TABLE `black_and_white_list`
ENGINE = InnoDB;

ALTER TABLE `interface_record`
ENGINE = InnoDB;

ALTER TABLE `lottery_activity`
ENGINE = InnoDB;

ALTER TABLE `send_msg`
ENGINE = InnoDB;

ALTER TABLE `sms_record`
ENGINE = InnoDB;

ALTER TABLE `white_list`
ENGINE = InnoDB;

-- -----------------------------------------------------
-- 20160628 红包，大转盘，砸金蛋分别增加是否微信鉴权字段
-- -----------------------------------------------------
ALTER TABLE `lottery_activity`
ADD COLUMN `wechatAuth` INT(2) NULL DEFAULT 0
COMMENT '0 普通用户  1 微信用户'
AFTER `check_url`;

ALTER TABLE `goldenball_activity`
ADD COLUMN `wechatAuth` INT(2) NULL DEFAULT 0
COMMENT '0 普通用户  1 微信用户'
AFTER `check_url`;

ALTER TABLE `ent_redpacket`
ADD COLUMN `wechatAuth` INT(2) NULL DEFAULT 0
COMMENT '0 普通用户  1 微信用户'
AFTER `probability_type`;

-- ---------------------
-- 20160624 修改supplier_product的code长度为64, by sunyiwei
-- ---------------------
ALTER TABLE `supplier_product`
MODIFY COLUMN `code` VARCHAR(64)
CHARACTER SET utf8
COLLATE utf8_general_ci NOT NULL
COMMENT '供应商产品编码'
AFTER `supplier_id`;

-- ---------------------
-- 20160624 修改enterprises的code长度为64, by sunyiwei
-- ---------------------
ALTER TABLE `enterprises`
MODIFY COLUMN `code` VARCHAR(64)
CHARACTER SET utf8
COLLATE utf8_general_ci NULL DEFAULT NULL
COMMENT '企业代码'
AFTER `name`;

-- ---------------------
-- 20160628 将过期状态当作运营状态，而不是普通卡状态
-- ---------------------
ALTER TABLE `mdrc_card_info_16`
MODIFY COLUMN `status` TINYINT(1) NOT NULL
COMMENT '卡状态，1新制卡, 2已入库、3已激活、4已绑定、5已使用'
AFTER `card_password`,
MODIFY COLUMN `op_status` TINYINT(1) NOT NULL DEFAULT 7
COMMENT '运营状态，6已过期, 7为正常，8为锁定，9为销卡，这个状态值比status有更高的优先级'
AFTER `status`;

ALTER TABLE `mdrc_card_info_17`
MODIFY COLUMN `status` TINYINT(1) NOT NULL
COMMENT '卡状态，1新制卡, 2已入库、3已激活、4已绑定、5已使用'
AFTER `card_password`,
MODIFY COLUMN `op_status` TINYINT(1) NOT NULL DEFAULT 7
COMMENT '运营状态，6已过期, 7为正常，8为锁定，9为销卡，这个状态值比status有更高的优先级'
AFTER `status`;

ALTER TABLE `mdrc_card_info_18`
MODIFY COLUMN `status` TINYINT(1) NOT NULL
COMMENT '卡状态，1新制卡, 2已入库、3已激活、4已绑定、5已使用'
AFTER `card_password`,
MODIFY COLUMN `op_status` TINYINT(1) NOT NULL DEFAULT 7
COMMENT '运营状态，6已过期, 7为正常，8为锁定，9为销卡，这个状态值比status有更高的优先级'
AFTER `status`;

ALTER TABLE `mdrc_card_info_19`
MODIFY COLUMN `status` TINYINT(1) NOT NULL
COMMENT '卡状态，1新制卡, 2已入库、3已激活、4已绑定、5已使用'
AFTER `card_password`,
MODIFY COLUMN `op_status` TINYINT(1) NOT NULL DEFAULT 7
COMMENT '运营状态，6已过期, 7为正常，8为锁定，9为销卡，这个状态值比status有更高的优先级'
AFTER `status`;

-- ---------------------
-- 20160628 修改interface_record的编码长度
-- ---------------------
ALTER TABLE `interface_record`
MODIFY COLUMN `enterprise_code` VARCHAR(64)
CHARACTER SET utf8
COLLATE utf8_general_ci NOT NULL
COMMENT '企业编码'
AFTER `id`,
MODIFY COLUMN `product_code` VARCHAR(64)
CHARACTER SET utf8
COLLATE utf8_general_ci NOT NULL
COMMENT '产品编码'
AFTER `enterprise_code`;

-- -----------------------------------------------------
-- 20160630 ent_product增加字段 by qinqinyan
-- -----------------------------------------------------
ALTER TABLE `ent_product`
ADD COLUMN `discount` INT(5) DEFAULT NULL
COMMENT '折扣，取出使用时要除以100'
AFTER `delete_flag`;

ALTER TABLE `ent_product`
ADD COLUMN `create_time` DATETIME NOT NULL
AFTER `discount`;

ALTER TABLE `ent_product`
ADD COLUMN `update_time` DATETIME NOT NULL
AFTER `create_time`;

-- ---------------------
-- 20160630 增加动态验证码查询权限项
-- ---------------------
INSERT INTO `authority` (`AUTHORITY_ID`, `PARENT_ID`, `NAME`, `AUTHORITY_NAME`, `CODE`, `AUTHORITY_URL`, `CREATE_TIME`, `CREATOR`, `UPDATE_USER`, `UPDATE_TIME`, `DELETE_FLAG`)
VALUES
  ('517', NULL, '动态验证码查询 ', 'ROLE_QUERY_VERIFY_CODE', '108001', NULL, '2016-06-30 10:10:07', NULL,
          NULL,
          '2016-06-30 10:10:10', '0');

-- -------------------------------
-- 20160630 product code长度修改为64位
-- -------------------------------
ALTER TABLE `product`
MODIFY COLUMN `product_code` VARCHAR(64)
CHARACTER SET utf8
COLLATE utf8_general_ci NOT NULL
COMMENT '供应商产品编码'
AFTER `id`;

-- -------------------------------
-- 20160704 account min_count 修改为double类型
-- -------------------------------
ALTER TABLE `account`
MODIFY COLUMN `min_count` DOUBLE(18, 2) NOT NULL
AFTER `count`;

-- ---------------------
-- 20160706 增加企业异步充值回调地址
-- ---------------------
DROP TABLE IF EXISTS `ent_callback_addr`;
CREATE TABLE `ent_callback_addr` (
  `id`            BIGINT(18)   NOT NULL AUTO_INCREMENT
  COMMENT 'ID',
  `ent_id`        BIGINT(18)   NOT NULL
  COMMENT '手机号',
  `callback_addr` VARCHAR(255) NOT NULL
  COMMENT '创建时间',
  `create_time`   DATETIME     NOT NULL
  COMMENT '创建时间',
  `update_time`   DATETIME              DEFAULT NULL
  COMMENT '更新时间',
  `delete_flag`   INT(1)       NOT NULL DEFAULT '0'
  COMMENT '删除标记， 0:未删除；1：已删除',
  PRIMARY KEY (`id`),
  KEY `ent_id_index` (`ent_id`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '企业异步充值回调地址';

-- -------------------------------
-- 20160707 增加每笔交易的价格和供应商产品id, wujiamin
-- -------------------------------
ALTER TABLE `charge_record`
ADD COLUMN `price` BIGINT(7) NULL
AFTER `fingerprint`,
ADD COLUMN `supplier_product_id` BIGINT(18) NULL
AFTER `price`;

-- -------------------------------
-- 20160711 营销卡记录增加充值请求序列号和响应序列号, luozuwu
-- -------------------------------
ALTER TABLE `mdrc_card_info_16`
ADD COLUMN `request_serial_number` VARCHAR(64) NULL
AFTER `product_id`,
ADD COLUMN `reponse_serial_number` VARCHAR(64) NULL
AFTER `request_serial_number`;


ALTER TABLE `mdrc_card_info_17`
ADD COLUMN `request_serial_number` VARCHAR(64) NULL
AFTER `product_id`,
ADD COLUMN `reponse_serial_number` VARCHAR(64) NULL
AFTER `request_serial_number`;


ALTER TABLE `mdrc_card_info_18`
ADD COLUMN `request_serial_number` VARCHAR(64) NULL
AFTER `product_id`,
ADD COLUMN `reponse_serial_number` VARCHAR(64) NULL
AFTER `request_serial_number`;


ALTER TABLE `mdrc_card_info_19`
ADD COLUMN `request_serial_number` VARCHAR(64) NULL
AFTER `product_id`,
ADD COLUMN `reponse_serial_number` VARCHAR(64) NULL
AFTER `request_serial_number`;
-- --------------------------------
-- 20160713 增加充值统计日表、月表、年表
-- --------------------------------
DROP TABLE IF EXISTS `day_charge_statistic`;
CREATE TABLE `day_charge_statistic` (
  `id`       BIGINT(18)     NOT NULL AUTO_INCREMENT,
  `enter_id` BIGINT(18)     NOT NULL,
  `number`   BIGINT(20)     NOT NULL
  COMMENT '流量交易次数',
  `capacity` BIGINT(20)     NOT NULL
  COMMENT '流量总M数',
  `money`    DECIMAL(18, 2) NOT NULL
  COMMENT '流量交易金额',
  `date`     DATE           NOT NULL
  COMMENT '流量交易日期',
  PRIMARY KEY (`id`),
  KEY `idx_charge_date` (`date`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


DROP TABLE IF EXISTS `month_charge_statistic`;
CREATE TABLE `month_charge_statistic` (
  `id`       BIGINT(18)     NOT NULL AUTO_INCREMENT,
  `enter_id` BIGINT(18)     NOT NULL,
  `number`   BIGINT(20)     NOT NULL
  COMMENT '流量交易次数',
  `capacity` BIGINT(20)     NOT NULL
  COMMENT '流量总M数',
  `money`    DECIMAL(18, 2) NOT NULL
  COMMENT '流量交易金额',
  `date`     DATE           NOT NULL
  COMMENT '流量交易日期',
  PRIMARY KEY (`id`),
  KEY `idx_charge_date` (`date`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


DROP TABLE IF EXISTS `year_charge_statistic`;
CREATE TABLE `year_charge_statistic` (
  `id`       BIGINT(18)     NOT NULL AUTO_INCREMENT,
  `enter_id` BIGINT(18)     NOT NULL,
  `number`   BIGINT(20)     NOT NULL
  COMMENT '流量交易次数',
  `capacity` BIGINT(20)     NOT NULL
  COMMENT '流量总M数',
  `money`    DECIMAL(18, 2) NOT NULL
  COMMENT '流量交易金额',
  `date`     DATE           NOT NULL
  COMMENT '流量交易日期',
  PRIMARY KEY (`id`),
  KEY `idx_charge_date` (`date`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------
-- 20160714 增加一步创建企业权限， wujiamin
-- 20160721 增加全局配置权限， sunyiwei
-- --------------------------
INSERT INTO `authority`
VALUES
  (NULL, NULL, '一步创建企业', 'ROLE_ENTERPRISE_ONE_PROCESS', '103012', '', now(), '', '', now(), '0');

INSERT INTO `authority` (`PARENT_ID`, `NAME`, `AUTHORITY_NAME`, `CODE`, `AUTHORITY_URL`, `CREATE_TIME`, `CREATOR`, `UPDATE_USER`, `UPDATE_TIME`, `DELETE_FLAG`)
VALUES
  (NULL, '全局配置管理', 'ROLE_GLOBAL_CONFIG', '109001', NULL, '2016-07-21 11:32:55', NULL, NULL,
   '2016-07-21 11:32:57', '0');

-- --------------------------
-- 20160726 修改全局配置中的value长度， sunyiwei
-- --------------------------
ALTER TABLE `global_config`
MODIFY COLUMN `config_value` VARCHAR(1024)
CHARACTER SET utf8
COLLATE utf8_general_ci NOT NULL
COMMENT '配置VALUE值'
AFTER `config_key`;

-- --------------------------
-- 20160727 增加全局配置权限， sunyiwei
-- --------------------------
INSERT INTO `global_config` VALUES
  ('49', '漫道短信请求地址', '漫道短信请求地址', 'MD_URL', 'http://sdk2.entinfo.cn:8061/mdsmssend.ashx',
         '2016-07-26 16:49:34',
         '2016-07-26 16:52:18', '1', '1', '0', NULL);
INSERT INTO `global_config` VALUES
  ('50', '漫道短信请求序列号', '漫道短信请求序列号', 'MD_SN', 'SDK-BBX-010-22482', '2016-07-26 17:11:01',
         '2016-07-26 17:11:01', '1', '1',
         '0', NULL);
INSERT INTO `global_config` VALUES
  ('51', '漫道短信请求密码', '漫道短信请求密码', 'MD_PWD', 'c5@e520@', '2016-07-26 17:11:35', '2016-07-26 17:11:35',
         '1', '1', '0',
         NULL);
INSERT INTO `global_config` VALUES
  ('52', '漫道短信签名', '漫道短信签名', 'MD_SIGN', '【中移流量PLUS】', '2016-07-26 17:12:08', '2016-07-26 17:12:08',
         '1', '1', '0',
         NULL);
INSERT INTO `global_config` VALUES
  ('53', '四川短信模板1', '四川短信模板1: 尊敬的用户，您已获得XXXMB国内流量，当月有效。感谢您的使用，如有疑问详询10086。', 'SC_MSG_TEMPLATE_ID1',
         '20158149',
         '2016-07-26 17:12:59', '2016-07-26 17:12:59', '1', '1', '0', NULL);
INSERT INTO `global_config` VALUES
  ('54', '四川短信模板2', '四川短信模板2：尊敬的用户，XXX（公司简称）向您赠送了XXXMB国内流量，当月有效，感谢您的支持！', 'SC_MSG_TEMPLATE_ID2',
         '20158150',
         '2016-07-26 17:14:02', '2016-07-26 17:14:02', '1', '1', '0', NULL);
INSERT INTO `global_config` VALUES
  ('55', '四川短信模板3', '四川短信模板3：尊敬的用户，您已经获赠XXXMB国内流量，当月有效，感谢您的支持！', 'SC_MSG_TEMPLATE_ID3', '20160145',
         '2016-07-26 17:14:41', '2016-07-26 17:14:41', '1', '1', '0', NULL);
INSERT INTO `global_config` VALUES
  ('56', '四川短信发送URL', '四川短信发送URL', 'SC_MSG_SEND_URL',
         'http://218.205.252.26:18051/openApi/auth/contract/sendInfoNTK',
         '2016-07-26 17:16:26', '2016-07-26 17:19:15', '1', '1', '0', NULL);
INSERT INTO `global_config` VALUES
  ('57', '四川短信AppId', '四川短信AppId', 'SC_MSG_APPID', '10000231', '2016-07-26 17:17:29',
         '2016-07-26 17:17:29', '1', '1',
         '0', NULL);
INSERT INTO `global_config` VALUES
  ('58', '四川短信appKey', '四川短信appKey', 'SC_MSG_APPKEY', '5%s20y%3478p', '2016-07-26 17:18:24',
         '2016-07-26 17:18:24', '1',
         '1', '0', NULL);
INSERT INTO `global_config` VALUES ('59', '四川短信私钥', '四川短信私钥', 'SC_MSG_PRIVATE_KEY',
                                          'MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAKthgW4zlK2+U8houM/XHtazSkIgcF/ejilI7Vef2m/M3Nh0AikizaqDaT/dKwibY6k1YP3xXZUd03iMUjNmfVHJb/XBoqk34Pa+fw1G2VdalIZsB5zaIqR+suMQ2XN98AMVxlAVsVFtuehVXMgSdH7XT4Jgzik1JlNFr7F7lh+HAgMBAAECgYEAlPZXjfX/kVURdeywVtdg0rVcIkYX5vyHDJN62OL09qBWhT2wcxjxMviuDviqYvzNj1H6UD/vW6FlIi61dD/tRC3UqbqG0uFqjNFSDRDGbhxfDzufsbnHsqBbYiBCqQqNQG47ghKMaws6M+sHLcUkIiltRJIDb4IF+xiVxLiIv6ECQQDxNWOeZhwJJBbQTBWTWPzh4E7uB2u8WHhANcAHOs1p2TrSPjR42qr4G6HaLLOUHIr8ZaWsVqD/GjDWAK5z3BHbAkEAtePtXu7n2pEFRItPaaOgumbnEoHrpkJXo3hvFCsSz7uhE+5ImRaVq9Q+cLYyh8pf50ikWkMSC+wdHyn/5FfGxQJAV1DoWvLDu1thFs40ET7pbCry55+wFGJCRZwvg2555ZNJg8oY3JbrxRzbnksIRtl+RpfVPWmupioo+48Ll81WeQJBALWEAoysqiCKfNFMnTF1I58hthPYJ8zBhCgUtfVQjvNT8YmsUDLGQRM7OhzNFlxA77gl3C5fpJDVTrKc/Uto9WkCQQDUEC2qV/dddFH5ff1v5eJdmYnCIpVyoucnQR/nNd9Sg0nBrUgQ2UUJTDh3LaXolqdXLW49FU7A9tfDWCJdAwSw',
                                          '2016-07-26 17:23:16', '2016-07-26 17:23:16', '1', '1',
                                          '0', NULL);
INSERT INTO `global_config` VALUES
  ('60', '四川短信方法名', '四川短信方法名', 'SC_MSG_METHOD', 'sendInfoNTK', '2016-07-26 17:23:52',
         '2016-07-26 17:23:52', '1', '1',
         '0', NULL);
INSERT INTO `global_config` VALUES
  ('61', '四川短信协议版本', '四川短信协议版本', 'SC_MSG_VERSION', '1.0', '2016-07-26 17:24:15',
         '2016-07-26 17:24:15', '1', '1', '0',
         NULL);
INSERT INTO `global_config` VALUES
  ('62', '四川短信环境', '正式环境和沙箱环境路由值，2：表示沙箱环境，1表示正式环境', 'SC_MSG_ENV', '2', '2016-07-26 17:24:55',
         '2016-07-26 17:24:55',
         '1', '1', '0', NULL);
INSERT INTO `global_config` VALUES
  ('63', '四川短信密钥', '四川短信密钥', 'SC_MSG_SECRET',
         '5452AF5199EDAF7DD7543481FCE8D095D343E90BBBDFD25B7B1FD703DF79B727',
         '2016-07-26 17:25:21', '2016-07-26 17:25:21', '1', '1', '0', NULL);
INSERT INTO `global_config` VALUES
  ('64', '内蒙古BOSS的appId', '内蒙古BOSS的appId', 'BOSS_NM_APPID', '1688684079A4F48F',
         '2016-07-26 17:47:44',
         '2016-07-26 17:47:44', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES ('65', '内蒙古BOSS的pubKey', '内蒙古BOSS的pubKey', 'BOSS_NM_PUBKEY',
                                          'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDGb6kLVoFWDdQ3EaiWOVNuFt8ywkwkyDxOrterYeEv3hLn9eWvSmT3RKI+mh3zAwUoC8Bbps/e9tuZNSky0op5zntjduC6GIoL2k/MI0JVJ7A6BJI7lpwqUg6cWrQZDTnDveV1lz0ktvFt57D8yFr9OPY2FaIr9jzRdRCDIKfFUQIDAQAB',
                                          '2016-07-26 17:47:44', '2016-07-26 17:47:44', '1', '1',
                                          '0', '0');
INSERT INTO `global_config` VALUES
  ('66', '内蒙古BOSS的busiCodeCharge', '内蒙古BOSS的busiCodeCharge', 'BOSS_NM_BUSICODE_CHARGE',
         'OP_AcceptGroupProd',
         '2016-07-26 17:47:44', '2016-07-26 17:47:44', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('67', '内蒙古BOSS的busiCodeSms', '内蒙古BOSS的busiCodeSms', 'BOSS_NM_BUSICODE_SMS',
         'OP_SendShortMessage',
         '2016-07-26 17:47:44', '2016-07-26 17:47:44', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('68', '内蒙古BOSS的busiCodeQuery', '内蒙古BOSS的busiCodeQuery', 'BOSS_NM_BUSICODE_QUERY',
         'OP_GetGroupInfo',
         '2016-07-26 17:47:44', '2016-07-26 17:47:44', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('69', '内蒙古BOSS的clientIP', '内蒙古BOSS的clientIP', 'BOSS_NM_CLIENT_IP', '10.220.4.164',
         '2016-07-26 17:47:44',
         '2016-07-26 17:47:44', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('70', '内蒙古BOSS的countryCode', '内蒙古BOSS的countryCode', 'BOSS_NM_COUNTRY_CODE', '7100',
         '2016-07-26 17:47:44',
         '2016-07-26 17:47:44', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('71', '内蒙古BOSS的regionCode', '内蒙古BOSS的regionCode', 'BOSS_NM_REGION_CODE', '471',
         '2016-07-26 17:47:44',
         '2016-07-26 17:47:44', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('72', '内蒙古BOSS的actType', '内蒙古BOSS的actType', 'BOSS_NM_ACT_TYPE', '1', '2016-07-26 17:47:45',
         '2016-07-26 17:47:45',
         '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('73', '内蒙古BOSS的bossUrl', '内蒙古BOSS的bossUrl', 'BOSS_NM_BOSS_URL',
         'http://10.220.3.16:9211/odp/v1/trans?',
         '2016-07-26 17:47:45', '2016-07-26 17:47:45', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('74', '湖南BOSS请求URL', '湖南BOSS请求URL', 'BOSS_HUNAN_URL', 'https://www.hn.10086.cn/open/router',
         '2016-07-26 17:52:36',
         '2016-07-26 17:52:36', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('75', '湖南BOSS的appId', '湖南BOSS的appId', 'BOSS_HUNAN_APPID', '20160128174400',
         '2016-07-26 17:52:36',
         '2016-07-26 17:52:36', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('76', '湖南BOSS的secret', '湖南BOSS的secret', 'BOSS_HUNAN_SECRET', '4D59FD528A8CD1E2763BB9AA98D4D8D4',
         '2016-07-26 17:52:36', '2016-07-26 17:52:36', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('77', '湖南BOSS的格式', '湖南BOSS的格式', 'BOSS_HUNAN_FORMAT', 'json', '2016-07-26 17:52:36',
         '2016-07-26 17:52:36', '1', '1',
         '0', '0');
INSERT INTO `global_config` VALUES
  ('78', '湖南BOSS的版本号', '湖南BOSS的版本号', 'BOSS_HUNAN_VERSION', '1.0', '2016-07-26 17:52:36',
         '2016-07-26 17:52:36', '1',
         '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('79', '湖南BOSS端的产品编码', '湖南BOSS端的产品编码', 'BOSS_HUNAN_PRODUCT_ID', '22000774', '2016-07-26 17:52:36',
         '2016-07-26 17:52:36', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('80', '湖南BOSS的本地化代码', '湖南BOSS的本地化代码', 'BOSS_HUNAN_LOCALE', 'zh_CN', '2016-07-26 17:52:36',
         '2016-07-26 17:52:36',
         '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('81', '湖南BOSS的环境', '湖南BOSS的环境', 'BOSS_HUNAN_ENV', '1', '2016-07-26 17:52:36',
         '2016-07-26 17:52:36', '1', '1', '0',
         '0');
INSERT INTO `global_config` VALUES
  ('82', '湖南BOSS的充值method', '湖南BOSS的充值method', 'BOSS_HUNAN_CHARGE_METHOD',
         'group.opertion.sendflow',
         '2016-07-26 17:52:36', '2016-07-26 17:52:36', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('83', '湖南BOSS的查询企业账户method', '湖南BOSS的查询企业账户method', 'BOSS_HUNAN_QUERY_GROUP_ACCOUNT_METHOD',
         'group.query.acct',
         '2016-07-26 17:52:36', '2016-07-26 17:52:36', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('84', '湖南BOSS的查询企业产品method', '湖南BOSS的查询企业产品method', 'BOSS_HUNAN_QUERY_GROUP_PRODUCT_METHOD',
         'group.query.product',
         '2016-07-26 17:52:36', '2016-07-26 17:52:36', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('85', '对账日类型',
         '对账日类型0-表示没有出账日不充值的情况；1-表示月末checkAccountStartDay天到下月初checkAccountEndDay天；2-表示checkAccountStartDay号到checkAccountEndDay号对账',
         'CHECK_ACCOUNT_DATE_TYPE', '0', '2016-07-26 17:52:36', '2016-07-26 17:52:36', '1', '1',
         '0',
         '0');
INSERT INTO `global_config` VALUES
  ('86', '对账日开始日期', '对账日开始日期', 'CHECK_ACCOUNT_START_DAY', '1', '2016-07-26 17:52:36',
         '2016-07-26 17:52:36', '1', '1',
         '0', '0');
INSERT INTO `global_config` VALUES
  ('87', '对账日开始时间', '对账日开始时间', 'CHECK_ACCOUNT_START_TIME', '21:00:00', '2016-07-26 17:52:37',
         '2016-07-26 17:52:37',
         '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('88', '对账日结束日期', '对账日结束日期', 'CHECK_ACCOUNT_END_DAY', '1', '2016-07-26 17:52:37',
         '2016-07-26 17:52:37', '1', '1',
         '0', '0');
INSERT INTO `global_config` VALUES
  ('89', '对账日结束时间', '对账日结束时间', 'CHECK_ACCOUNT_END_TIME', '12:00:00', '2016-07-26 17:52:37',
         '2016-07-26 17:52:37', '1',
         '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('90', 'JWT_TOKEN生成签名时的前缀', 'JWT_TOKEN生成签名时的前缀', 'JWT_SIGN_PREFIX', 'sparta',
         '2016-07-26 17:52:37',
         '2016-07-26 17:52:37', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('91', 'JWT_TOKEN生成签名时的后缀', 'JWT_TOKEN生成签名时的后缀', 'JWT_SIGN_SUFFIX', 'warrior',
         '2016-07-26 17:52:37',
         '2016-07-26 17:52:37', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('92', '接口认证的有效时间，以分钟为单位', '接口认证的有效时间，以分钟为单位', 'TOKEN_VALID_PERIOD', '30', '2016-07-26 18:00:43',
         '2016-07-26 18:00:43', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('93', '重庆BOSS地址', '重庆BOSS地址', 'BOSS_CQ_HOST', '10.191.133.248', '2016-07-26 18:00:43',
         '2016-07-26 18:00:43', '1',
         '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('94', '重庆BOSS端口', '重庆BOSS端口', 'BOSS_CQ_PORT', '8135', '2016-07-26 18:00:43',
         '2016-07-26 18:00:43', '1', '1', '0',
         '0');
INSERT INTO `global_config` VALUES
  ('95', '山东BOSS的appkey', '山东BOSS的appkey', 'BOSS_SD_APPKEY', '5388018197438', '2016-07-26 18:00:43',
         '2016-07-26 18:00:43', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('96', '山东BOSS的appsecret', '山东BOSS的appsecret', 'BOSS_SD_APPSECRET',
         'd7274673cd294466bae7179e2c44e87a',
         '2016-07-26 18:00:43', '2016-07-26 18:00:43', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('97', '山东BOSS的userId', '山东BOSS的userId', 'BOSS_SD_USERID', '5388022162385', '2016-07-26 18:00:43',
         '2016-07-26 18:00:43', '1', '1', '0', '0');
INSERT INTO `global_config`
VALUES ('98', '山东BOSS的appsecret', '山东BOSS的appsecret', 'BOSS_SD_ORDER_URL',
              'https://shandong.4ggogo.com/sd-web/open/ChargeFlow', '2016-07-26 18:00:43',
              '2016-07-26 18:00:43', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('99', '河南BOSS的GBILL_ID', '河南BOSS的GBILL_ID', 'BOSS_HENAN_GBILL_ID', '93713620147',
         '2016-07-26 18:00:43',
         '2016-07-26 18:00:43', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('100', '河南BOSS的smsTemplate', '河南BOSS的smsTemplate', 'BOSS_HENAN_SMS_TEMPLATE', '0',
          '2016-07-26 18:00:43',
          '2016-07-26 18:00:43', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('101', '河南BOSS的validType', '河南BOSS的validType', 'BOSS_HENAN_VALID_TYPE', '1',
          '2016-07-26 18:00:43',
          '2016-07-26 18:00:43', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('102', '河南BOSS的validMonth', '河南BOSS的validMonth', 'BOSS_HENAN_VALID_MONTH', '3',
          '2016-07-26 18:00:43',
          '2016-07-26 18:00:43', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('103', '河南BOSS的appId', '河南BOSS的appId', 'BOSS_HENAN_APPID', '505060', '2016-07-26 18:00:43',
          '2016-07-26 18:00:43',
          '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('104', '河南BOSS的appKey', '河南BOSS的appKey', 'BOSS_HENAN_APPKEY', '1c21366415bc6146ad30f2295d55b81c',
          '2016-07-26 18:00:43', '2016-07-26 18:00:43', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('105', '河南BOSS的ChargeDomain', '河南BOSS的ChargeDomain', 'BOSS_HENAN_CHARGE_DOMAIN',
          'http://211.138.30.208:20110/oppf',
          '2016-07-26 18:00:43', '2016-07-26 18:00:43', '1', '1', '0', '0');
INSERT INTO `global_config`
VALUES ('106', '河南BOSS的authDomain', '河南BOSS的authDomain', 'BOSS_HENAN_AUTH_DOMAIN',
               'http://211.138.30.208:20200/aopoauth/oauth/token', '2016-07-26 18:00:43',
               '2016-07-26 18:00:43', '1', '1', '0', '0');
INSERT INTO `global_config`
VALUES ('107', '黑龙江BOSS的WSDL地址', '黑龙江BOSS的WSDL地址', 'BOSS_HLJ_WSDL_ADDRESS',
               'http://10.110.0.100:51000/esbWS/services/sWebOpMsgCfmLLWS?wsdl',
               '2016-07-26 18:00:43', '2016-07-26 18:00:43', '1', '1', '0', '0');
INSERT INTO `global_config`
VALUES ('108', '黑龙江BOSS的充值地址', '黑龙江BOSS的充值地址', 'BOSS_HLJ_CHARGE_ADDRESS',
               'http://10.110.0.100:51000/esbWS/services/sWebOpMsgCfmLLWS/callService',
               '2016-07-26 18:00:43', '2016-07-26 18:00:43', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('109', '黑龙江BOSS的渠道标识', '黑龙江BOSS的渠道标识', 'BOSS_HLJ_CHANNEL_SOURCE', '37', '2016-07-26 18:00:44',
          '2016-07-26 18:00:44',
          '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('110', '黑龙江BOSS的操作代码', '黑龙江BOSS的操作代码', 'BOSS_HLJ_OP_CODE', 'g685', '2016-07-26 18:00:44',
          '2016-07-26 18:00:44', '1',
          '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('111', '黑龙江BOSS的操作工号', '黑龙江BOSS的操作工号', 'BOSS_HLJ_LOGIN_NO', 'llzcpt', '2016-07-26 18:00:44',
          '2016-07-26 18:00:44',
          '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('112', '黑龙江BOSS的操作密码', '黑龙江BOSS的操作密码', 'BOSS_HLJ_LOGIN_PWD', 'EIGBDHBHPHHPMJCE',
          '2016-07-26 18:00:44',
          '2016-07-26 18:00:44', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('113', '黑龙江BOSS的号码', '黑龙江BOSS的号码', 'BOSS_HLJ_PHONE_NO', '1390450015', '2016-07-26 18:00:44',
          '2016-07-26 18:00:44',
          '1', '1', '0', '0');
INSERT INTO `global_config` VALUES ('114', '甘肃BOSS产品受理URL', '甘肃BOSS产品受理URL', 'BOSS_GS_PRODUCT_URL',
                                           'https://partner.cmccgs.cn/openapi/V1/partner/ability/production/productChangePlanProd',
                                           '2016-07-26 18:00:44', '2016-07-26 18:00:44', '1', '1',
                                           '0', '0');
INSERT INTO `global_config`
VALUES ('115', '甘肃BOSS动态鉴权服务URL', '甘肃BOSS动态鉴权服务URL', 'BOSS_GS_DYNAMIC_TOKEN_URL',
               'https://partner.cmccgs.cn/openapi/V1/partner/ability/dynamicToken',
               '2016-07-26 18:00:44', '2016-07-26 18:00:44', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('116', '甘肃BOSS能力平台Version', '甘肃BOSS能力平台Version', 'BOSS_GS_VERSION', '1.0', '2016-07-26 18:00:44',
          '2016-07-26 18:00:44', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('117', '甘肃BOSS测试标识', '甘肃BOSS测试标识', 'BOSS_GS_TEST_FLAG', '1', '2016-07-26 18:00:44',
          '2016-07-26 18:00:44', '1', '1',
          '0', '0');
INSERT INTO `global_config` VALUES
  ('118', '甘肃BOSS的appId', '甘肃BOSS的appId', 'BOSS_GS_APPID', 'A2016051015330700015',
          '2016-07-26 18:00:44',
          '2016-07-26 18:00:44', '1', '1', '0', '0');
INSERT INTO `global_config`
VALUES ('119', '甘肃BOSS的accessToken', '甘肃BOSS的accessToken', 'BOSS_GS_ACCESS_TOKEN',
               '03747d5150753a3b032bada8cbbe46e060a0f884', '2016-07-26 18:00:44',
               '2016-07-26 18:00:44', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('120', '甘肃BOSS的userAuthorizationCode', '甘肃BOSS的userAuthorizationCode',
          'BOSS_GS_USER_AUTHORIZATION_CODE', '123456',
          '2016-07-26 18:00:44', '2016-07-26 18:00:44', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('121', '甘肃BOSS的validType', '甘肃BOSS的validType', 'BOSS_GS_VALID_TYPE', '1', '2016-07-26 18:00:44',
          '2016-07-26 18:00:44', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('122', '甘肃BOSS的newPlanId', '甘肃BOSS的newPlanId', 'BOSS_GS_NEW_PLAN_ID', '1', '2016-07-26 18:00:44',
          '2016-07-26 18:00:44', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('123', '甘肃BOSS的newBrand', '甘肃BOSS的newBrand', 'BOSS_GS_NEW_BRAND', '1', '2016-07-26 18:00:44',
          '2016-07-26 18:00:44',
          '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('124', '甘肃BOSS的prodType', '甘肃BOSS的prodType', 'BOSS_GS_PROD_TYPE', '1', '2016-07-26 18:00:44',
          '2016-07-26 18:00:44',
          '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('125', '甘肃BOSS的actionType', '甘肃BOSS的actionType', 'BOSS_GS_ACTION_TYPE', '1',
          '2016-07-26 18:00:44',
          '2016-07-26 18:00:44', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('126', '甘肃BOSS的busType', '甘肃BOSS的busType', 'BOSS_GS_BUS_TYPE', '1', '2016-07-26 18:00:44',
          '2016-07-26 18:00:44',
          '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('127', '甘肃BOSS的serviceId', '甘肃BOSS的serviceId', 'BOSS_GS_SERVICE_ID', '1', '2016-07-26 18:00:44',
          '2016-07-26 18:00:44', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('128', '甘肃BOSS的preProdId', '甘肃BOSS的preProdId', 'BOSS_GS_PRE_PRODID', '1', '2016-07-26 18:00:44',
          '2016-07-26 18:00:44', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('129', '四川BOSS增加成员URL', '四川BOSS增加成员URL', 'BOSS_SC_ADD_MEMBER_URL',
          'http://218.205.252.26:33000/rest/1.0/sAddMember',
          '2016-07-26 18:03:46', '2016-07-26 18:03:46', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES ('130', '四川BOSS流量统付开户URL', '四川BOSS流量统付开户URL', 'BOSS_SC_OPEN_URL',
                                           'http://218.205.252.26:33000/rest/1.0/sLocalLLTFGrpOpen',
                                           '2016-07-26 18:03:46', '2016-07-26 18:03:46', '1', '1',
                                           '0', '0');
INSERT INTO `global_config` VALUES
  ('131', '四川BOSS私钥路径', '四川BOSS私钥路径', 'BOSS_SC_PRIVATE_KEY_PATH',
          '/etc/pdata/conf/private_key_dev.txt',
          '2016-07-26 18:03:46', '2016-07-26 18:03:46', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('132', '四川BOSS用户名', '四川BOSS用户名', 'BOSS_SC_USER_NAME', 'wujiamin', '2016-07-26 18:03:46',
          '2016-07-26 18:03:46', '1',
          '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('133', '四川BOSS的appKey', '四川BOSS的appKey', 'BOSS_SC_APP_KEY', '21000030', '2016-07-26 18:03:46',
          '2016-07-26 18:03:46',
          '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('134', '四川BOSS登录账号', '四川BOSS登录账号', 'BOSS_SC_LOGIN_NO', 'oc0025', '2016-07-26 18:03:46',
          '2016-07-26 18:03:46', '1',
          '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('135', '四川BOSS的regionId', '四川BOSS的regionId', 'BOSS_SC_OPEN_REGIONID', '11',
          '2016-07-26 18:03:46',
          '2016-07-26 18:03:46', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('136', '四川BOSS的opCode', '四川BOSS的opCode', 'BOSS_SC_OPCODE', '4317', '2016-07-26 18:03:46',
          '2016-07-26 18:03:46', '1',
          '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('137', '四川BOSS的prcId', '四川BOSS的prcId', 'BOSS_SC_OPEN_PRCID', 'ACCZ65711', '2016-07-26 18:03:46',
          '2016-07-26 18:03:46', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('138', '四川BOSS的expireRule', '四川BOSS的expireRule', 'BOSS_SC_EXPIRE_RULE', '0',
          '2016-07-26 18:03:46',
          '2016-07-26 18:03:46', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('139', '四川BOSS的产品注销URL', '四川BOSS的产品注销URL', 'BOSS_SC_CANCEL_URL',
          'http://218.205.252.26:33000/rest/1.0/sCancleGrp',
          '2016-07-26 18:03:46', '2016-07-26 18:03:46', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('140', '四川BOSS的产品注销操作代码', '四川BOSS的产品注销操作代码', 'BOSS_SC_CANCEL_OPCODE', '1052',
          '2016-07-26 18:03:46',
          '2016-07-26 18:03:46', '1', '1', '0', '0');
INSERT INTO `global_config`
VALUES ('141', '四川BOSS的成员删除URL', '四川BOSS的成员删除URL', 'BOSS_SC_DEL_MEMBER_URL',
               'http://218.205.252.26:33000/rest/1.0/sDelMember', '2016-07-26 18:03:46',
               '2016-07-26 18:03:46', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('142', '超级管理员角色ID', '超级管理员角色ID', 'SUPER_ADMIN_ROLE_ID', '1', '2016-07-26 18:06:54',
          '2016-07-26 18:06:54', '1', '1',
          '0', '0');
INSERT INTO `global_config` VALUES
  ('143', '客户经理角色ID', '客户经理角色ID', 'ACCOUNT_MANAGER_ROLE_ID', '2', '2016-07-26 18:06:54',
          '2016-07-26 18:06:54', '1',
          '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('144', '企业关键人角色ID', '企业关键人角色ID', 'ENTERPRISE_CONTACTOR_ROLE_ID', '3', '2016-07-26 18:06:54',
          '2016-07-26 18:06:54',
          '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('145', '流量卡关键人角色ID', '流量卡关键人角色ID', 'FLOW_CARD_MANAGER_ROLE_ID', '5', '2016-07-26 18:06:54',
          '2016-07-26 18:06:54',
          '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('146', '制卡商角色ID', '制卡商角色ID', 'CARD_MAKER_ROLE_ID', '8', '2016-07-26 18:06:54',
          '2016-07-26 18:06:54', '1', '1', '0',
          '0');
INSERT INTO `global_config` VALUES
  ('147', '省级管理员角色ID', '省级管理员角色ID', 'PROVINCE_MANAGER_ROLE_ID', '6', '2016-07-26 18:06:54',
          '2016-07-26 18:06:54', '1',
          '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('148', '市级管理员角色ID', '市级管理员角色ID', 'CITY_MANAGER_ROLE_ID', '7', '2016-07-26 18:06:54',
          '2016-07-26 18:06:54', '1', '1',
          '0', '0');
INSERT INTO `global_config` VALUES
  ('149', '四川BOSS的余额查询URL', '四川BOSS的余额查询URL', 'BOSS_SC_BALANCE_URL',
          'http://218.205.252.26:33000/rest/1.0/sPFeeQuery',
          '2016-07-27 17:36:00', '2016-07-27 17:36:00', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('150', '四川BOSS余额查询下发短信标识', '四川BOSS余额查询下发短信标识', 'BOSS_SC_BALANCE_MSG_FLAG', '0',
          '2016-07-27 17:36:00',
          '2016-07-27 17:36:00', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('151', '自营平台的appKey', '自营平台的appKey', 'BOSS_ZY_APP_KEY',
          '56a73801d6364e30d580a13d25e26afc0667448d',
          '2016-07-27 17:36:00', '2016-07-27 17:36:00', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('152', '自营平台的appSecret', '自营平台的appSecret', 'BOSS_ZY_APP_SECRET',
          '571d185a54be1f22e8cb9e016884e567eded7b46',
          '2016-07-27 17:36:00', '2016-07-27 17:36:00', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('153', '自营平台的authUrl', '自营平台的authUrl', 'BOSS_ZY_AUTH_URL', 'http://core.4ggogo.com/auth',
          '2016-07-27 17:36:00',
          '2016-07-27 17:36:00', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('154', '自营平台的chargeUrl', '自营平台的chargeUrl', 'BOSS_ZY_CHARGE_URL',
          'http://core.4ggogo.com/boss/charge',
          '2016-07-27 17:36:00', '2016-07-27 17:36:00', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('155', '双创接口的请求URL', '双创接口的请求URL', 'SHUANCHANG_URL',
          'http://192.168.13.4:8081/open/nettraffic/athentication',
          '2016-07-27 17:36:01', '2016-07-27 17:36:01', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('156', '双创接口的请求CODE', '双创接口的请求CODE', 'SHUANCHANG_CODE', '280280280', '2016-07-27 17:36:01',
          '2016-07-27 17:36:01',
          '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('157', '模板文件名称', '模板文件名称', 'TEMPLATE_FILE_NAME', '企业管理员授权证明.doc', '2016-07-27 17:36:01',
          '2016-07-27 17:36:01', '1',
          '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('158', '模板文件KEY', '模板文件KEY', 'TEMPLATE_FILE_KEY', 'pdata_template_flie', '2016-07-27 17:36:01',
          '2016-07-27 17:36:01', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('159', '首页的LOGO名称', '首页的LOGO名称', 'LOGO_NAME', '干得好流量平台', '2016-07-27 17:36:01',
          '2016-07-27 17:36:01', '1', '1', '0',
          '0');
INSERT INTO `global_config` VALUES
  ('160', '手机号段查询服务URL', '手机号段查询服务URL', 'PHONE_REGION_QUERY_URL',
          'http://fgsyw.4ggogo.com/phone-region/query',
          '2016-07-27 17:36:01', '2016-07-27 17:36:01', '1', '1', '0', '0');
INSERT INTO `global_config`
VALUES
  ('161', '省份标识', '省份标识', 'PROVINCE_FLAG', 'sc', '2016-07-27 17:36:01', '2016-07-27 17:36:01', '1',
          '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('162', 'S3的accessKey', 'S3的accessKey', 'S3_AWS_ACCESS_KEY', '5HGRT33XIGUFYJ5ESHQF',
          '2016-07-27 17:36:01',
          '2016-07-27 17:36:01', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('163', 'S3的accessSecret', 'S3的accessSecret', 'S3_AWS_ACCESS_SECRET',
          '7kQwI60Phm2M3hXOJoM1go3rzHF6KcC5N6KXCFlX',
          '2016-07-27 17:36:01', '2016-07-27 17:36:01', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('164', 'S3的bucketName', 'S3的bucketName', 'S3_BUCKET_NAME', 'sichuan-test', '2016-07-27 17:36:01',
          '2016-07-27 17:36:01', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('165', 'S3的HOST', 'S3的HOST', 'S3_HOST', '192.168.32.103:7480', '2016-07-27 17:36:01',
          '2016-07-27 17:36:01', '1',
          '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('166', '营销卡制卡权限代码', '营销卡制卡权限代码', 'MDRC_GENERATE_AUTH_CODE', '107001', '2016-07-27 17:36:01',
          '2016-07-27 17:36:01',
          '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('167', '营销卡下载权限代码', '营销卡下载权限代码', 'MDRC_DOWNLOAD_AUTH_CODE', '107002', '2016-07-27 17:36:01',
          '2016-07-27 17:36:01',
          '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('168', '营销卡制卡商权限代码', '营销卡制卡商权限代码', 'MDRC_CARDMAKER_AUTH_CODE', '107003', '2016-07-27 17:36:01',
          '2016-07-27 17:36:01', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('169', '营销卡模板权限代码', '营销卡模板权限代码', 'MDRC_TEMPLATE_AUTH_CODE', '107004', '2016-07-27 17:36:01',
          '2016-07-27 17:36:01',
          '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('170', '营销卡省份代码', '营销卡省份代码', 'MDRC_PROVINCE_CODE', '23', '2016-07-27 17:36:01',
          '2016-07-27 17:36:01', '1', '1', '0',
          '0');
INSERT INTO `global_config` VALUES
  ('171', '营销卡模板文件存储目录', '营销卡模板文件存储目录', 'MDRC_TEMPLATE_FILE_PATH', '/srv/appdata/data/template',
          '2016-07-27 17:36:01',
          '2016-07-27 17:36:01', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('172', '营销卡数据临时存储目录', '营销卡数据临时存储目录', 'MDRC_DATA_FILE_PATH', '/srv/appdata/data/mdrc/data',
          '2016-07-27 17:36:01',
          '2016-07-27 17:36:01', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('173', '营销卡打包文件临时存储目录', '营销卡打包文件临时存储目录', 'MDRC_ZIP_FILE_PATH', '/srv/appdata/zip',
          '2016-07-27 17:36:01',
          '2016-07-27 17:36:01', '1', '1', '0', '0');
INSERT INTO `global_config`
VALUES ('174', '判断活动是否授权的URL', '判断活动是否授权的URL', 'ACTIVITY_IS_AUTHORIZED_URL',
               'http://wxthird.4ggogo.com/cgi-bin/component/is_authorized?enterprise_access_token=sgs_sctestdev',
               '2016-07-27 17:36:01', '2016-07-27 17:36:01', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES ('175', '活动授权的URL', '活动授权的URL', 'ACTIVITY_AUTHORIZED_URL',
                                           'http://wxexp.4ggogo.com/auth/wxAuth/pre.html?enterpriseId=sgs_sctestdev',
                                           '2016-07-27 17:36:01', '2016-07-27 17:36:01', '1', '1',
                                           '0', '0');
INSERT INTO `global_config` VALUES ('176', '生成活动的URL', '生成活动的URL', 'ACTIVITY_GENERATE_URL',
                                           'http://activitytest.4ggogo.com/template/service/autoGenerate/make',
                                           '2016-07-27 17:36:01', '2016-07-27 17:36:01', '1', '1',
                                           '0', '0');
INSERT INTO `global_config` VALUES
  ('177', '生成活动的平台标识', '生成活动的平台标识', 'ACTIVITY_PLATFORM_NAME', 'sgs_sctestdev',
          '2016-07-27 17:36:01',
          '2016-07-27 17:36:01', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('178', '活动的回调地址', '活动的回调地址', 'ACTIVITY_CALLBACK_URL',
          'http://localhost:8080/web-in/api/charge.html',
          '2016-07-27 17:36:01', '2016-07-27 17:36:01', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('179', '活动图片存储目录', '活动图片存储目录', 'ACTIVITY_IMAGE_PATH', '', '2016-07-27 17:36:01',
          '2016-07-27 17:36:01', '1', '1',
          '0', '0');
INSERT INTO `global_config` VALUES
  ('180', '卡模板文件存放目录', '卡模板文件存放目录', 'ACTIVITY_RULE_TEMPLATE_FILE_PATH',
          '/srv/appdata/activity/template',
          '2016-07-27 17:36:01', '2016-07-27 17:36:01', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('181', '账单存储的目录', '账单存储的目录', 'ACCOUNT_PATH', '/srv/sftpd/checkaccount/user',
          '2016-07-27 17:36:01',
          '2016-07-27 17:36:01', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('182', '存储账单差异的目录', '存储账单差异的目录', 'ACCOUNT_DIFF_PATH', '/srv/sftpd/checkaccount/diff',
          '2016-07-27 17:36:01',
          '2016-07-27 17:36:01', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('183', 'PLUS自营平台的appKey', 'PLUS自营平台的appKey', 'BOSS_EC_PLUS_APP_KEY',
          '63949039ad7e358bf67179c3359327f7',
          '2016-07-27 17:36:00', '2016-07-27 17:36:00', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('184', 'PLUS自营平台的appSecret', 'PLUS自营平台的appSecret', 'BOSS_EC_PLUS_APP_SECRET',
          '7fb8e2104badd44a825f265536de9f37',
          '2016-07-27 17:36:00', '2016-07-27 17:36:00', '1', '1', '0', '0');
INSERT INTO `global_config` VALUES
  ('185', 'PLUS自营平台的authUrl', 'PLUS自营平台的authUrl', 'BOSS_EC_PLUS_AUTH_URL',
          'http://localhost:8080/web-in/auth.html',
          '2016-07-27 17:36:00', '2016-07-27 17:36:00', '1', '1', '0', '0');
INSERT INTO `global_config`
VALUES ('186', 'PLUS自营平台的chargeUrl', 'PLUS自营平台的chargeUrl', 'BOSS_EC_PLUS_CHARGE_URL',
               'http://localhost:8080/web-in/boss/charge.html', '2016-07-27 17:36:00',
               '2016-07-27 17:36:00', '1', '1', '0', '0');

-- --------------------------------
-- 20160715 创建管理员表, wujiamin
-- --------------------------------
DROP TABLE IF EXISTS `manager`;
CREATE TABLE `manager` (
  `id`          BIGINT(18)  NOT NULL AUTO_INCREMENT,
  `role_id`     BIGINT(18)  NOT NULL,
  `name`        VARCHAR(64) NOT NULL,
  `parent_id`   BIGINT(18)  NOT NULL,
  `create_time` DATETIME    NOT NULL,
  `update_time` DATETIME             DEFAULT NULL,
  `delete_flag` TINYINT(2)  NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `PARENT_ID` (`parent_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

INSERT INTO `manager` VALUES ('1', '1', '平台超级管理员', '1', now(), now(), '0');

-- --------------------------------
-- 20160715 创建用户-管理员表, wujiamin
-- --------------------------------
DROP TABLE IF EXISTS `admin_manager`;
CREATE TABLE `admin_manager` (
  `id`          BIGINT(18) NOT NULL AUTO_INCREMENT
  COMMENT '创建这个关联的adminid',
  `admin_id`    BIGINT(18) NOT NULL,
  `manager_id`  BIGINT(18) NOT NULL,
  `create_time` DATETIME   NOT NULL,
  `update_time` DATETIME   NOT NULL,
  `delete_flag` TINYINT(4) NOT NULL DEFAULT '0',
  `creator_id`  BIGINT(18) NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

INSERT INTO `admin_manager` VALUES ('1', '1', '1', now(), now(), '0', '1');

-- --------------------------------
-- 20160715 增加创建管理员的权限项， wujiamin
-- --------------------------------
INSERT INTO `authority`
VALUES (NULL, NULL, '职位管理 ', 'ROLE_CREATE_MANAGER', '101004', NULL, now(), NULL, NULL, now(), '0');

-- --------------------------------
-- 20160719 创建企业-管理员表, wujiamin
-- --------------------------------
DROP TABLE IF EXISTS `ent_manager`;
CREATE TABLE `ent_manager` (
  `id`          BIGINT(18) NOT NULL AUTO_INCREMENT
  COMMENT '创建这个关联的adminid',
  `enter_id`    BIGINT(18) NOT NULL,
  `manager_id`  BIGINT(18) NOT NULL,
  `create_time` DATETIME   NOT NULL,
  `update_time` DATETIME   NOT NULL,
  `delete_flag` TINYINT(4) NOT NULL DEFAULT '0',
  `creator_id`  BIGINT(18) NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ---------------------
-- 20160720 企业管理员关联的角色, wujiamin
-- ---------------------
INSERT INTO `authority`
VALUES (NULL, NULL, '企业管理员关联的角色', 'ROLE_ENTERPRISE_SET_MANAGER', '103013', NULL, now(), NULL, NULL,
              now(), '0');

-- ---------------------
-- 20160721 企业管理员父节点的角色, wujiamin
-- ---------------------
INSERT INTO `authority`
VALUES
  (NULL, NULL, '企业管理员父节点的角色', 'ROLE_ENTERPRISE_MANAGER_PARENT', '103014', NULL, now(), NULL, NULL,
         now(), '0');

-- --------------------------------
-- 20160721 创建企业-产品操作记录表, xujue
-- --------------------------------
DROP TABLE IF EXISTS `product_change_operator`;
CREATE TABLE `product_change_operator` (
  `id`          BIGINT(18) NOT NULL AUTO_INCREMENT
  COMMENT '操作记录',
  `enter_id`    BIGINT(18) NOT NULL,
  `prd_id`      BIGINT(18) NOT NULL,
  `operator`    INT(1)     NOT NULL
  COMMENT '操作：0-删除;1-增加;2-变更折扣',
  `discount`    INT(3)     NOT NULL
  COMMENT '折扣',
  `delete_flag` TINYINT(1) NOT NULL DEFAULT '0'
  COMMENT '删除Flag，0:未删除；1：已删除;',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- -------------------------------
-- 20160714 充值记录增加相应索引, sunyiwei
-- --------------------------------
ALTER TABLE charge_record ADD INDEX idx_charge_time(charge_time);
ALTER TABLE charge_record ADD INDEX idx_charge_record_enter_id(enter_id);
ALTER TABLE charge_record ADD INDEX idx_system_num(system_num);
ALTER TABLE charge_record ADD INDEX idx_record_id(record_id);
-- ----------------------------

-- -------------------------------
-- 20160718 增加数据库相应索引, sunyiwei
-- --------------------------------
ALTER TABLE `enterprise_sms_template`
ADD INDEX `idx_enterprise_sms_template_enter_id` (`enter_id`) USING BTREE;
-- -------------------------------
-- 20160718 增加数据库相应索引, sunyiwei
-- --------------------------------
ALTER TABLE `supplier_product_map`
ADD INDEX `idx_plt_product_id` (`platform_product_id`) USING BTREE;

-- -------------------------------
-- 20160728 审核流程定义表  by qinqinyan
-- --------------------------------
DROP TABLE IF EXISTS `approval_process_definition`;
CREATE TABLE `approval_process_definition` (
  `id`             BIGINT(18) NOT NULL AUTO_INCREMENT,
  `origin_role_id` BIGINT(18) NOT NULL
  COMMENT '审批流程发起角色id',
  `stage`          INT(1)     NOT NULL
  COMMENT '审批层级，0表示无审批流程，最高至5级审批',
  `target_status`  INT(5)     NOT NULL
  COMMENT '审批流程目标状态，0：无审批流程；1:一级审批；3：二级审批；7：三级审批；15：四级审批；31：五级审批',
  `create_time`    DATETIME   NOT NULL
  COMMENT '创建时间',
  `update_time`    DATETIME   NOT NULL
  COMMENT '更新时间',
  `delete_flag`    INT(1)     NOT NULL,
  `type`           INT(1)     NOT NULL
  COMMENT '审批类型：0，企业开户，1产品变更，2账户变更',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 26
  DEFAULT CHARSET = utf8;

-- -------------------------------
-- 20160728 审核流程明细表  by qinqinyan
-- --------------------------------
DROP TABLE IF EXISTS `approval_detail_definition`;
CREATE TABLE `approval_detail_definition` (
  `id`            BIGINT(18) NOT NULL AUTO_INCREMENT,
  `process_id`    BIGINT(18) NOT NULL
  COMMENT '审批流程id',
  `role_id`       BIGINT(18) NOT NULL
  COMMENT '角色id',
  `approval_code` INT(2)     NOT NULL
  COMMENT '审批编号：1，2,4,8,16',
  `precondition`  INT(2)     NOT NULL
  COMMENT '前置状态，用于与当前状态做对比',
  `delete_flag`   INT(1)              DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 47
  DEFAULT CHARSET = utf8;

-- ------------------------------------------
-- 20160728 审批历史记录表  by qinqinyan
-- ------------------------------------------
DROP TABLE IF EXISTS `approval_record`;
CREATE TABLE `approval_record` (
  `id`          BIGINT(18)  NOT NULL AUTO_INCREMENT,
  `request_id`  BIGINT(18)  NOT NULL
  COMMENT '请求审批记录id',
  `creator_id`  BIGINT(18)  NOT NULL,
  `operator_id` BIGINT(18)           DEFAULT NULL
  COMMENT '审批人id',
  `comment`     VARCHAR(350)         DEFAULT NULL
  COMMENT '审批意见',
  `description` VARCHAR(50) NOT NULL,
  `create_time` DATETIME    NOT NULL,
  `update_time` DATETIME             DEFAULT NULL,
  `is_new`      INT(1)      NOT NULL
  COMMENT '待审批记录标示，0已完成审核，1未完成审核',
  `delete_flag` INT(1)      NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 107
  DEFAULT CHARSET = utf8;

-- ------------------------------------------
-- 20160728 审批请求记录表  by qinqinyan
-- ------------------------------------------
DROP TABLE IF EXISTS `approval_request`;
CREATE TABLE `approval_request` (
  `id`          BIGINT(18) NOT NULL AUTO_INCREMENT,
  `process_id`  BIGINT(18) NOT NULL
  COMMENT '审批流程id',
  `ent_id`      BIGINT(18) NOT NULL,
  `creator_id`  BIGINT(18) NOT NULL
  COMMENT '流程发起用户id',
  `status`      INT(2)     NOT NULL DEFAULT '0'
  COMMENT '当前审核状态，提交审批时status默认为0',
  `create_time` DATETIME   NOT NULL,
  `update_time` DATETIME   NOT NULL,
  `delete_flag` INT(1)     NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 80
  DEFAULT CHARSET = utf8;

-- -----------------------------------
-- 20160728 产品变更明细表  by qinqinyan
-- ------------------------------------
DROP TABLE IF EXISTS `product_change_detail`;
CREATE TABLE `product_change_detail` (
  `id`          BIGINT(18) NOT NULL AUTO_INCREMENT,
  `request_id`  BIGINT(18) NOT NULL
  COMMENT '请求审批id',
  `product_id`  BIGINT(18) NOT NULL,
  `operate`     INT(1)     NOT NULL
  COMMENT '0删除、1增加、2变更折扣',
  `discount`    INT(3)     NOT NULL
  COMMENT '折扣',
  `delete_flag` INT(1)     NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 81
  DEFAULT CHARSET = utf8;

-- ------------------------------------------
-- 20160728 产品变更临时操作明细表  by qinqinyan
-- -------------------------------------------
DROP TABLE IF EXISTS `product_change_operator`;
CREATE TABLE `product_change_operator` (
  `id`          BIGINT(18) NOT NULL AUTO_INCREMENT
  COMMENT '操作记录',
  `enter_id`    BIGINT(18) NOT NULL,
  `prd_id`      BIGINT(18) NOT NULL,
  `operator`    INT(1)     NOT NULL
  COMMENT '操作：0-删除;1-增加;2-变更折扣',
  `discount`    INT(3)     NOT NULL
  COMMENT '折扣',
  `delete_flag` TINYINT(1) NOT NULL DEFAULT '0'
  COMMENT '删除Flag，0:未删除；1：已删除;',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 132
  DEFAULT CHARSET = utf8;

-- ------------------------------------------
-- 20160728 账户变更临时操作明细表  by qinqinyan
-- -------------------------------------------
DROP TABLE IF EXISTS `account_change_operator`;
CREATE TABLE `account_change_operator` (
  `id`          BIGINT(18)    NOT NULL AUTO_INCREMENT,
  `account_id`  BIGINT(18)    NOT NULL,
  `prd_id`      BIGINT(18)    NOT NULL,
  `count`       DOUBLE(18, 0) NOT NULL,
  `ent_id`      BIGINT(18)    NOT NULL,
  `serial_num`  VARCHAR(64)   NOT NULL,
  `delete_flag` INT(1)        NOT NULL,
  `create_time` DATETIME      NOT NULL
  COMMENT '创建时间',
  `update_time` DATETIME      NOT NULL
  COMMENT '更新时间',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 46
  DEFAULT CHARSET = utf8;

-- ------------------------------------------
-- 20160728 账户变更明细表  by qinqinyan
-- -------------------------------------------
DROP TABLE IF EXISTS `account_change_detail`;
CREATE TABLE `account_change_detail` (
  `id`          BIGINT(18)    NOT NULL AUTO_INCREMENT,
  `request_id`  BIGINT(18)    NOT NULL,
  `account_id`  BIGINT(18)    NOT NULL,
  `count`       DOUBLE(18, 0) NOT NULL,
  `serial_num`  VARCHAR(64)   NOT NULL,
  `product_id`  BIGINT(18)    NOT NULL,
  `delete_flag` INT(1)        NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 23
  DEFAULT CHARSET = utf8;

-- -------------------------------
-- 20160803 权限项增加 by wujiamin
-- -------------------------------
INSERT INTO `authority` (`AUTHORITY_ID`, `PARENT_ID`, `NAME`, `AUTHORITY_NAME`, `CODE`, `AUTHORITY_URL`, `CREATE_TIME`, `CREATOR`, `UPDATE_USER`, `UPDATE_TIME`, `DELETE_FLAG`)
VALUES
  (NULL, NULL, '审批流程管理', 'ROLE_APPOVAL_PROCESS_SET', '108001', NULL, now(), NULL, NULL, now(), '0');

-- -------------------------------
-- 20160808 增加流量卡名称字段的长度，wujiamin
-- -------------------------------
ALTER TABLE `mdrc_batch_config`
MODIFY COLUMN `config_name` VARCHAR(64)
CHARACTER SET utf8
COLLATE utf8_general_ci NOT NULL
COMMENT '配置名称'
AFTER `id`;

-- -----------------------
-- 20160808，企业联系方式增加为64个字符，wujiamin
-- -----------------------
ALTER TABLE `enterprises`
MODIFY COLUMN `phone` VARCHAR(64)
CHARACTER SET utf8
COLLATE utf8_general_ci NULL DEFAULT NULL
COMMENT '企业手机号码'
AFTER `code`;

-- -------------------------------
-- 20160718 增加根据企业信息展示相应的流量卡统计功能, xujue
-- -------------------------------
INSERT INTO `authority`
VALUES
  (NULL, NULL, '营销卡统计(企业)', 'ROLE_MDRC_DATA_STAT', '107005', NULL, now(), NULL, NULL, now(), '0');

-- -------------------------------
-- 20160808 企业表中增加客户经理邮箱和客户经理手机号，wujiamin
-- -------------------------------
ALTER TABLE `enterprises`
ADD COLUMN `cm_email` VARCHAR(100) NULL
COMMENT '客户经理邮箱'
AFTER `licence_end_time`,
ADD COLUMN `cm_phone` VARCHAR(11) NULL
COMMENT '客户经理手机'
AFTER `cm_email`;

-- --------------------------------
-- 20160808 用户角色增加默认的普通用户角色，wujiamin
-- --------------------------------
INSERT INTO `role` (`role_id`, `name`, `code`, `description`, `create_time`, `creator`, `update_user`, `update_time`, `delete_flag`, `ROLE_STATUS`, `canBeDeleted`)
VALUES ('-1', '普通用户', '0', NULL, now(), '1', NULL, now(), '0', '0', '0');
INSERT INTO `manager` (`id`, `role_id`, `name`, `parent_id`, `create_time`, `update_time`, `delete_flag`)
VALUES ('-1', '-1', '无角色', '-1', now(), now(), '0');

-- --------------------------------
-- 20160809 增加流量卡充值结果状态和信息，wujiamin
-- --------------------------------
ALTER TABLE `mdrc_card_info`
ADD COLUMN `charge_status` INT(2) NULL
AFTER `enter_id`,
ADD COLUMN `charge_msg` VARCHAR(100) NULL
AFTER `charge_status`;

ALTER TABLE `mdrc_card_info_16`
ADD COLUMN `charge_status` INT(2) NULL
AFTER `enter_id`,
ADD COLUMN `charge_msg` VARCHAR(100) NULL
AFTER `charge_status`;

ALTER TABLE `mdrc_card_info_17`
ADD COLUMN `charge_status` INT(2) NULL
AFTER `enter_id`,
ADD COLUMN `charge_msg` VARCHAR(100) NULL
AFTER `charge_status`;

ALTER TABLE `mdrc_card_info_18`
ADD COLUMN `charge_status` INT(2) NULL
AFTER `enter_id`,
ADD COLUMN `charge_msg` VARCHAR(100) NULL
AFTER `charge_status`;

ALTER TABLE `mdrc_card_info_19`
ADD COLUMN `charge_status` INT(2) NULL
AFTER `enter_id`,
ADD COLUMN `charge_msg` VARCHAR(100) NULL
AFTER `charge_status`;

-- -------------------------------
-- 20160809 增加序列号表，sunyiwei
-- -------------------------------
CREATE TABLE `serial_num` (
  `id`                   BIGINT(18)  NOT NULL AUTO_INCREMENT,
  `platform_serial_num`  VARCHAR(64) NOT NULL
  COMMENT '平台内部的序列号',
  `ec_serial_num`        VARCHAR(64)          DEFAULT NULL
  COMMENT '企业向平台请求时的序列号',
  `boss_req_serial_num`  VARCHAR(64)          DEFAULT NULL
  COMMENT '平台向BOSS请求时的序列号，它与platform_serial_num不一定一样',
  `boss_resp_serial_num` VARCHAR(64)          DEFAULT NULL
  COMMENT '平台请求BOSS时，boss侧返回的序列号',
  `create_time`          DATETIME    NOT NULL
  COMMENT '创建时间',
  `update_time`          DATETIME             DEFAULT NULL
  COMMENT '更新时间',
  `delete_flag`          INT(1)      NOT NULL DEFAULT '0'
  COMMENT '删除标记， 0:未删除；1：已删除',
  PRIMARY KEY (`id`),
  KEY `idxPltSerialNum` (`platform_serial_num`) USING BTREE,
  KEY `idxEcSerialNum` (`ec_serial_num`) USING BTREE,
  KEY `idxBossReqSerialNum` (`boss_req_serial_num`) USING BTREE,
  KEY `idxBossRespSerialNum` (`boss_resp_serial_num`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- -------------------------------
-- 20160816，给用户密码增加哈希盐， sunyiwei
-- -------------------------------
ALTER TABLE `administer`
ADD COLUMN `password_new` VARCHAR(64)
CHARACTER SET utf8
COLLATE utf8_general_ci NULL DEFAULT NULL
COMMENT '新的密码，加盐哈希'
AFTER `password`,
ADD COLUMN `salt` VARCHAR(64)
CHARACTER SET utf8
COLLATE utf8_general_ci NULL DEFAULT NULL
COMMENT '密码盐'
AFTER `password_new`;

-- -------------------------------
-- 20160816，在authority表中增加权限管理， xujue
-- -------------------------------
INSERT INTO `authority`
VALUES (NULL, NULL, '权限管理', 'ROLE_AUTHORITY', '109002', '', NOW(), '', '', NOW(), '0');

-- ------------------------------------
-- 20160828 二维码、流量券等活动统一增加表,wujiamin
-- ------------------------------------
DROP TABLE IF EXISTS `activities`;
CREATE TABLE `activities` (
  `id`          BIGINT(18)   NOT NULL AUTO_INCREMENT,
  `activity_id` VARCHAR(225) NOT NULL
  COMMENT 'uuid',
  `type`        INT(1)       NOT NULL
  COMMENT '活动类型：1：流量卡券，2：二维码',
  `ent_id`      BIGINT(18)   NOT NULL,
  `name`        VARCHAR(100) NOT NULL
  COMMENT '活动名称',
  `start_time`  DATETIME     NOT NULL
  COMMENT '活动开始时间',
  `end_time`    DATETIME     NOT NULL,
  `creator_id`  BIGINT(18)   NOT NULL,
  `status`      INT(1)       NOT NULL
  COMMENT 'SAVED(0, "已保存"), ON(1, "已上架"),PROCESSING(2, "活动进行中"),DOWN(3, "下架"),END(4, "活动已结束");',
  `delete_flag` INT(1)       NOT NULL,
  `create_time` DATETIME     NOT NULL,
  `update_time` DATETIME     NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_activity_id` (`activity_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 47
  DEFAULT CHARSET = utf8;


DROP TABLE IF EXISTS `activity_black_and_white`;
CREATE TABLE `activity_black_and_white` (
  `id`          BIGINT(18)   NOT NULL AUTO_INCREMENT,
  `activity_id` VARCHAR(225) NOT NULL
  COMMENT '活动uuid',
  `mobile`      VARCHAR(15)  NOT NULL,
  `is_white`    INT(1)       NOT NULL
  COMMENT '1:白名单，2黑名单',
  `delete_flag` INT(1)       NOT NULL,
  `create_time` DATETIME     NOT NULL,
  `update_time` DATETIME     NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 10
  DEFAULT CHARSET = utf8;


DROP TABLE IF EXISTS `activity_info`;
CREATE TABLE `activity_info` (
  `id`                 BIGINT(18)           NOT NULL AUTO_INCREMENT,
  `activity_id`        VARCHAR(225)
                       CHARACTER SET latin1 NOT NULL,
  `prize_count`        BIGINT(18)                    DEFAULT NULL
  COMMENT '活动预计产品总数量',
  `user_count`         BIGINT(18)                    DEFAULT NULL
  COMMENT '活动预计用户数，手机号去重预估',
  `total_product_size` BIGINT(18)                    DEFAULT NULL
  COMMENT '活动预计消费总流量大小',
  `price`              BIGINT(18)                    DEFAULT NULL
  COMMENT '活动预计花费',
  `create_time`        DATETIME             NOT NULL,
  `update_time`        DATETIME             NOT NULL,
  `delete_flag`        INT(1)               NOT NULL,
  `has_white_or_black` INT(1)                        DEFAULT NULL
  COMMENT '是否有黑白名单。0无，1白名单，2黑名单',
  `qrcode_size`        INT(1)                        DEFAULT NULL
  COMMENT '1:8cmm; 2:12cm; 3:15cm; 4:30cm; 5:50cm',
  `download`           INT(1)                        DEFAULT NULL
  COMMENT '已下载，为1，其他为空',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 26
  DEFAULT CHARSET = utf8;


ALTER TABLE `activity_prize`
ADD COLUMN `size` BIGINT(18) NULL
AFTER `prize_name`,
ADD COLUMN `type` INT(1) NULL
AFTER `size`;


DROP TABLE IF EXISTS `activity_win_record`;
CREATE TABLE `activity_win_record` (
  `id`            BIGINT(18)           NOT NULL AUTO_INCREMENT,
  `record_id`     VARCHAR(225)
                  CHARACTER SET latin1 NOT NULL
  COMMENT '记录uuid',
  `activity_id`   VARCHAR(225)
                  CHARACTER SET latin1 NOT NULL
  COMMENT '活动id',
  `own_mobile`    VARCHAR(11)
                  CHARACTER SET latin1          DEFAULT NULL
  COMMENT '中奖用户（砸金蛋、红包、大转盘）、赠送用户（流量券，二维码）',
  `charge_mobile` VARCHAR(11)
                  CHARACTER SET latin1          DEFAULT NULL
  COMMENT '实际充值用户（大转盘、砸金蛋、红包与own_mobile相同；流量卡券存在赠送，）',
  `prize_id`      BIGINT(18)                    DEFAULT NULL,
  `isp`           VARCHAR(2)
                  CHARACTER SET latin1          DEFAULT NULL
  COMMENT '运营商：M移动，U联通，T电信',
  `win_time`      DATETIME                      DEFAULT NULL
  COMMENT '中奖时间',
  `charge_time`   DATETIME                      DEFAULT NULL
  COMMENT '充值时间',
  `status`        INT(1)                        DEFAULT NULL
  COMMENT '充值情况，待充值/充值成功/充值失败',
  `reason`        VARCHAR(200)                  DEFAULT NULL
  COMMENT '充值成功，充值失败原因',
  `delete_flag`   INT(1)               NOT NULL,
  `create_time`   DATETIME             NOT NULL,
  `update_time`   DATETIME             NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_record_id` (`record_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 72
  DEFAULT CHARSET = utf8;


INSERT INTO `authority` (`AUTHORITY_ID`, `PARENT_ID`, `NAME`, `AUTHORITY_NAME`, `CODE`, `AUTHORITY_URL`, `CREATE_TIME`, `CREATOR`, `UPDATE_USER`, `UPDATE_TIME`, `DELETE_FLAG`)
VALUES (NULL, NULL, '流量券', 'ROLE_FLOW_CARD', '104007', NULL, now(), NULL, NULL, now(), '0');
INSERT INTO `authority` (`AUTHORITY_ID`, `PARENT_ID`, `NAME`, `AUTHORITY_NAME`, `CODE`, `AUTHORITY_URL`, `CREATE_TIME`, `CREATOR`, `UPDATE_USER`, `UPDATE_TIME`, `DELETE_FLAG`)
VALUES
  (NULL, NULL, '营销卡数据下载(采购)', 'ROLE_MDRC_DATADL_CAIGOU', '107005', NULL, now(), NULL, NULL, now(),
         '0');
INSERT INTO `authority` (`AUTHORITY_ID`, `PARENT_ID`, `NAME`, `AUTHORITY_NAME`, `CODE`, `AUTHORITY_URL`, `CREATE_TIME`, `CREATOR`, `UPDATE_USER`, `UPDATE_TIME`, `DELETE_FLAG`)
VALUES (NULL, NULL, '二维码', 'ROLE_QRCODE', '104008', NULL, now(), NULL, NULL, now(), '0');


INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (NULL, 'URL加密秘钥', '营销活动链接加密秘钥', 'ACTIVITY_URL_KEY', 'this is test', now(), now(), '1', '1', '0',
         NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (NULL, '流量券充值url', '流量券充值url', 'LOTTERY_FLOWCARD_URL',
         'http://localhost:8080/web-in/manage/flowcard/charge/', now(),
         now(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES (NULL, '营销卡采购数据临时存储目录', '营销卡采购数据临时存储目录', 'MDRC_PURCHASE_DATA_FILE_PATH',
              '/srv/appdata/data/mdrc/cgdata', now(),
              now(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (NULL, '二维码手机号下载临时存储目录', '二维码手机号下载临时存储目录', 'LOTTERY_QRCODE_PATH',
         '/srv/appdata/data/lottety/qrcode', now(), now(),
         '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (NULL, '二维码文件下载临时存储目录', '二维码文件下载临时存储目录', 'QRCODE_DOWNLOAD_PATH', '/srv/appdata/data/qrcode',
         now(), now(), '1', '1',
         '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES (NULL, '二维码充值url', '二维码充值url', 'LOTTERY_QRCODE_URL',
              'http://localhost:8080/web-in/manage/qrcode/charge/index.html?token=', now(), now(),
              '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (NULL, '移动运营商号段前三位', '移动运营商号段,前三位', 'CMCC_MOBILE_FLAG',
         '134,135,136,137,138,139,147,150,151,152,157,158,159,187,188',
         now(), now(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (NULL, '联通运营商号段前三位', '联通运营商号段前三位', 'UNICOM_MOBILE_FLAG', '130,131,132,155,156,185,186', now(),
         now(), '1', '1', '0',
         NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (NULL, '电信手机号号段前三位', '电信手机号号段前三位', 'TELECOM_MOBILE_FLAG', '133,153,180,189', now(), now(), '1',
         '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (NULL, '批量赠送块大小', '批量赠送块大小', 'BATCH_PRESENT_BLOCK_SIZE', '2000', '2016-08-30 15:41:58',
         '2016-08-30 15:42:00', '1',
         '1', '0', '0');

-- -------------------------------------------
-- 20160908 短信模板默认,并将模板配置与原代码中的硬编码保持一致
-- 如平台需要特殊的短信配置，可以修改update语句
-- -------------------------------------------
ALTER TABLE `sms_template`
ADD COLUMN `default_have` INT(1) NULL DEFAULT 0
COMMENT '是否为企业开户时默认的可选短信模板：0-否，1-是'
AFTER `create_time`,
ADD COLUMN `default_use` INT(1) NULL DEFAULT 0
COMMENT '是否为企业开户时默认发送短信模板：0-否，1-是'
AFTER `default_have`;
UPDATE sms_template
SET default_have = 1, default_use = 0
WHERE id = 2;
UPDATE sms_template
SET default_have = 1, default_use = 1
WHERE id = 3;

-- ------------------------------------
-- 20160908 增加甘肃BOS参数veType,xujue
-- ------------------------------------
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (NULL, '甘肃BOSS的veType', '甘肃BOSS的veType', 'BOSS_GS_VETYPE', '3', now(), now(), '1', '1', '0', '0');

-- -------------------------------------------
-- by sunyiwei, 20160908, 增加索引
-- -------------------------------------------
ALTER TABLE `account_record`
ADD INDEX `serial_num_idx` (`serial_num`) USING BTREE;

ALTER TABLE `account_record`
ADD INDEX `enter_id_idx` (`enter_id`) USING BTREE;

-- ------------------------------------
-- 20160909 活动下架url配置项增加，wujiamin
-- ------------------------------------
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES (NULL, '活动下架URL', '通知模板侧进行活动下架的URL', 'ACTIVITY_OFFLINE_URL',
              'http://activitytest.4ggogo.com/template/close/index.html', now(), now(), '1', '1',
              '0', NULL);

-- ---------------------------------------
-- -lilin 9/11 add table sh_boss_product
-- ---------------------------------------
DROP TABLE IF EXISTS `sh_boss_product`;
CREATE TABLE `sh_boss_product` (
  `id`                     BIGINT(18) NOT NULL AUTO_INCREMENT,
  `supplier_product_id`    BIGINT(18) NOT NULL,
  `supplier_product_price` INT(10)    NULL,
  `supplier_product_size`  BIGINT(18) NOT NULL,
  `create_time`            DATETIME   NOT NULL,
  `update_time`            DATETIME   NOT NULL,
  `delete_flag`            INT        NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_shboss_supplier_product_id` (`supplier_product_id`) USING BTREE
);

INSERT INTO `sh_boss_product` (`id`, `supplier_product_id`, `supplier_product_price`, `supplier_product_size`, `create_time`, `update_time`, `delete_flag`)
VALUES ('1', '10', '300', '10240', '2016-09-11 17:16:20', '2016-09-11 17:16:22', '0');
INSERT INTO `sh_boss_product` (`id`, `supplier_product_id`, `supplier_product_price`, `supplier_product_size`, `create_time`, `update_time`, `delete_flag`)
VALUES ('2', '11', '500', '30720', '2016-09-11 17:17:34', '2016-09-11 17:17:37', '0');
INSERT INTO `sh_boss_product` (`id`, `supplier_product_id`, `supplier_product_price`, `supplier_product_size`, `create_time`, `update_time`, `delete_flag`)
VALUES ('3', '12', '10000', '71680', '2016-09-11 17:18:18', '2016-09-11 17:18:20', '0');
INSERT INTO `sh_boss_product` (`id`, `supplier_product_id`, `supplier_product_price`, `supplier_product_size`, `create_time`, `update_time`, `delete_flag`)
VALUES ('4', '25', '10000', '102400', '2016-09-11 17:19:22', '2016-09-11 17:19:24', '0');
INSERT INTO `sh_boss_product` (`id`, `supplier_product_id`, `supplier_product_price`, `supplier_product_size`, `create_time`, `update_time`, `delete_flag`)
VALUES ('5', '13', '20000', '153600', '2016-09-11 17:20:54', '2016-09-11 17:20:57', '0');
INSERT INTO `sh_boss_product` (`id`, `supplier_product_id`, `supplier_product_price`, `supplier_product_size`, `create_time`, `update_time`, `delete_flag`)
VALUES ('6', '26', '20000', '307200', '2016-09-11 17:22:51', '2016-09-11 17:22:54', '0');
INSERT INTO `sh_boss_product` (`id`, `supplier_product_id`, `supplier_product_price`, `supplier_product_size`, `create_time`, `update_time`, `delete_flag`)
VALUES ('7', '18', '30000', '512000', '2016-09-11 17:24:02', '2016-09-11 17:24:05', '0');
INSERT INTO `sh_boss_product` (`id`, `supplier_product_id`, `supplier_product_price`, `supplier_product_size`, `create_time`, `update_time`, `delete_flag`)
VALUES ('8', '19', '50000', '1048576', '2016-09-11 17:25:13', '2016-09-11 17:25:16', '0');
INSERT INTO `sh_boss_product` (`id`, `supplier_product_id`, `supplier_product_price`, `supplier_product_size`, `create_time`, `update_time`, `delete_flag`)
VALUES ('9', '20', '70000', '2097152', '2016-09-11 17:26:27', '2016-09-11 17:26:29', '0');
INSERT INTO `sh_boss_product` (`id`, `supplier_product_id`, `supplier_product_price`, `supplier_product_size`, `create_time`, `update_time`, `delete_flag`)
VALUES ('10', '21', '100000', '3145728', '2016-09-11 17:27:43', '2016-09-11 17:27:46', '0');
INSERT INTO `sh_boss_product` (`id`, `supplier_product_id`, `supplier_product_price`, `supplier_product_size`, `create_time`, `update_time`, `delete_flag`)
VALUES ('11', '22', '130000', '4194304', '2016-09-11 17:28:43', '2016-09-11 17:28:45', '0');
INSERT INTO `sh_boss_product` (`id`, `supplier_product_id`, `supplier_product_price`, `supplier_product_size`, `create_time`, `update_time`, `delete_flag`)
VALUES ('12', '23', '180000', '6291456', '2016-09-11 17:29:27', '2016-09-11 17:29:30', '0');
INSERT INTO `sh_boss_product` (`id`, `supplier_product_id`, `supplier_product_price`, `supplier_product_size`, `create_time`, `update_time`, `delete_flag`)
VALUES ('13', '24', '280000', '11534336', '2016-09-11 17:30:21', '2016-09-11 17:30:23', '0');
INSERT INTO `sh_boss_product` (`id`, `supplier_product_id`, `supplier_product_price`, `supplier_product_size`, `create_time`, `update_time`, `delete_flag`)
VALUES ('14', '5001', '1', '1024', '2016-09-11 17:31:06', '2016-09-11 17:31:10', '0');
INSERT INTO `sh_boss_product` (`id`, `supplier_product_id`, `supplier_product_price`, `supplier_product_size`, `create_time`, `update_time`, `delete_flag`)
VALUES ('15', '5002', '1', '2048', '2016-09-11 17:32:13', '2016-09-11 17:32:15', '0');
INSERT INTO `sh_boss_product` (`id`, `supplier_product_id`, `supplier_product_price`, `supplier_product_size`, `create_time`, `update_time`, `delete_flag`)
VALUES ('16', '5003', '1', '5120', '2016-09-11 17:32:45', '2016-09-11 17:32:47', '0');
INSERT INTO `sh_boss_product` (`id`, `supplier_product_id`, `supplier_product_price`, `supplier_product_size`, `create_time`, `update_time`, `delete_flag`)
VALUES ('17', '5004', '1', '10240', '2016-09-11 17:33:34', '2016-09-11 17:33:44', '0');
INSERT INTO `sh_boss_product` (`id`, `supplier_product_id`, `supplier_product_price`, `supplier_product_size`, `create_time`, `update_time`, `delete_flag`)
VALUES ('18', '5005', '1', '20480', '2016-09-11 17:34:09', '2016-09-11 17:34:12', '0');
INSERT INTO `sh_boss_product` (`id`, `supplier_product_id`, `supplier_product_price`, `supplier_product_size`, `create_time`, `update_time`, `delete_flag`)
VALUES ('19', '5006', '1', '51200', '2016-09-11 17:34:48', '2016-09-11 17:34:53', '0');
INSERT INTO `sh_boss_product` (`id`, `supplier_product_id`, `supplier_product_price`, `supplier_product_size`, `create_time`, `update_time`, `delete_flag`)
VALUES ('20', '5007', '1', '102400', '2016-09-11 17:35:35', '2016-09-11 17:35:37', '0');
INSERT INTO `sh_boss_product` (`id`, `supplier_product_id`, `supplier_product_price`, `supplier_product_size`, `create_time`, `update_time`, `delete_flag`)
VALUES ('21', '5008', '1', '204800', '2016-09-11 17:36:41', '2016-09-11 17:36:44', '0');
INSERT INTO `sh_boss_product` (`id`, `supplier_product_id`, `supplier_product_price`, `supplier_product_size`, `create_time`, `update_time`, `delete_flag`)
VALUES ('22', '5009', '1', '512000', '2016-09-11 17:37:24', '2016-09-11 17:37:26', '0');
INSERT INTO `sh_boss_product` (`id`, `supplier_product_id`, `supplier_product_price`, `supplier_product_size`, `create_time`, `update_time`, `delete_flag`)
VALUES ('23', '5010', '1', '1048576', '2016-09-11 17:38:01', '2016-09-11 17:38:02', '0');

-- ----------
-- 企业产品的折扣默认为100, by sunyiwei, 2016/09/12
-- ---------
ALTER TABLE `ent_product`
MODIFY COLUMN `discount` INT(5) NOT NULL DEFAULT 100
COMMENT '折扣，取出使用时要除以100'
AFTER `delete_flag`;

-- -----------------------------
-- 20160913 清理流量券表， wujiamin
-- -----------------------------
DROP TABLE IF EXISTS `userbalance_record`;
DROP TABLE IF EXISTS `coupon_transfer`;

-- ----------------------------
-- 20160913 清理benefit_grade
-- ----------------------------
DROP TABLE IF EXISTS `benefit_grade`;

-- --------------------
-- 20160913 清理对账表，wujiamin
-- --------------------
DROP TABLE IF EXISTS `flow_account_analysis`;
DROP TABLE IF EXISTS `flow_account`;
DROP TABLE IF EXISTS `flow_account_difference`;

-- ----------------------
-- 20160914 活动表缺少的sql
-- ----------------------
ALTER TABLE `activity_prize`
MODIFY COLUMN `id_prefix` VARCHAR(18)
CHARACTER SET utf8
COLLATE utf8_general_ci NULL DEFAULT NULL
COMMENT '前缀，从0开始计数'
AFTER `id`,
MODIFY COLUMN `rank_name` VARCHAR(64)
CHARACTER SET utf8
COLLATE utf8_general_ci NOT NULL
COMMENT '几等奖'
AFTER `id_prefix`,
MODIFY COLUMN `size` BIGINT(18) NULL DEFAULT NULL
COMMENT '流量大小（流量卡券用到）：count*产品大小（product_id）'
AFTER `prize_name`,
MODIFY COLUMN `type` INT(1) NULL DEFAULT NULL
COMMENT '流量类型（流量卡券用到）：1流量包，2流量池'
AFTER `size`;

-- ----------------------------------------------
-- 20160919 修改lottery_record表中wx_openid可为空
-- ---------------------------------------------
ALTER TABLE `lottery_record`
MODIFY COLUMN `wx_openid` VARCHAR(50)
CHARACTER SET utf8
COLLATE utf8_general_ci NULL
COMMENT '微信openId'
AFTER `id`;

-- --------------------------------------
-- 20160920 审批记录中增加审批操作者当时的职位，wujiamin
-- --------------------------------------
ALTER TABLE `approval_record`
ADD COLUMN `manager_id` BIGINT(18) NULL
COMMENT '操作者实时的职位'
AFTER `operator_id`;
-- --------------------------------
-- 20160920 更新原始审批记录中的操作者manager_id,wujiamin
-- --------------------------------
UPDATE approval_record ar
SET ar.manager_id = (SELECT m.id
                     FROM manager m LEFT JOIN admin_manager am ON am.manager_id = m.id
                     WHERE am.delete_flag = 0 AND am.admin_id = ar.operator_id)
WHERE ar.manager_id IS NULL;

-- -------------------------------
-- 20160921 删除一步创建企业权限项
-- -------------------------------
DELETE FROM `authority`
WHERE NAME = '一步创建企业' AND AUTHORITY_NAME = 'ROLE_ENTERPRISE_ONE_PROCESS';

-- --------------------------------
-- 20160921 增加全局配置中各省BOSS渠道的配置信息，sunyiwei
-- --------------------------------
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('湖南BOSS免费充值method名称', '湖南BOSS免费充值method名称', 'BOSS_HUNAN_FREE_OF_CHARGE_METHOD',
        'user.busi.operation',
        '2016-09-21 10:46:58', '2016-09-21 10:46:58', '1120', '1120', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('游客登录开关控制', '游客登录的开关控制（0-允许游客登录，1-不允许游客登录，允许平台无角色用户登录，2-不允许游客和平台无角色用户登录)', 'GUEST_LOGIN', '0',
   '2016-09-21 09:31:05',
   '2016-09-21 10:06:01', '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('北京BOSS渠道PASSWORD', '北京BOSS渠道PASSWORD', 'BOSS_BJ_PASSWORD', 'zM7KKA3Q', '2016-09-20 20:11:16',
   '2016-09-20 20:11:16',
   '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('北京BOSS渠道CA', '北京BOSS渠道CA', 'BOSS_BJ_CA', '19XWBQP7', '2016-09-20 20:10:48',
   '2016-09-20 20:10:48', '1', '1', '0',
   NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('北京BOSS渠道ADMINNAME', '北京BOSS渠道ADMINNAME', 'BOSS_BJ_ADMIN_NAME', 'admin', '2016-09-20 20:10:28',
   '2016-09-20 20:10:28', '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('卓望BOSS的testFlag', '卓望BOSS的testFlag', 'BOSS_ZW_TEST_FLAG', '0', '2016-09-20 17:46:25',
   '2016-09-20 17:46:25', '1120',
   '1120', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('卓望BOSS的productId', '卓望BOSS的productId', 'BOSS_ZW_PRODUCT_ID', '9003419034',
        '2016-09-20 17:46:00',
        '2016-09-20 17:46:00', '1120', '1120', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('浙江外围编码BIZCODE', '浙江外围编码BIZCODE', 'BOSS_ZJ_CHARGE_BIZCODE', 'ZYHZ', '2016-09-20 14:10:15',
   '2016-09-20 14:10:15',
   '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('浙江APPSECRET', '浙江APPSECRET', 'BOSS_ZH_CHARGE_APPSECRET', '7a61bbefed5b8406',
        '2016-09-20 14:08:21',
        '2016-09-20 14:08:21', '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('浙江APPKEY', '浙江APPKEY', 'BOSS_ZJ_CHARGE_APPKEY', '6a02f088b86d4d50a446109890f47f6f',
        '2016-09-20 14:05:49',
        '2016-09-20 14:05:49', '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('浙江充值URL', '浙江充值URL', 'BOSS_ZJ_CHARGE_URL',
        'http://211.138.118.15:1100/mbb/presentDataTraffic/v1',
        '2016-09-20 14:04:58', '2016-09-20 14:04:58', '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('山东授权URL', '山东授权URL', 'BOSS_SD_EC_AUTH_URL', 'https://shandong.4ggogo.com/sd-web-in/auth.html',
   '2016-09-20 12:01:24', '2016-09-20 12:01:24', '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('山东APPSECRET', '山东APPSECRET', 'BOSS_SD_EC_APPSECRET', 'ce41ef3ff1614cbe8a38620bc3e28ad0',
        '2016-09-20 11:54:15',
        '2016-09-20 11:54:15', '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('山东充值APPKEY', '山东充值APPKEY', 'BOSS_SD_EC_APPKEY', '17ad66800e44478bbbed6df5437b9ae0',
        '2016-09-20 11:52:52',
        '2016-09-20 11:52:52', '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('广东BOSS通道CHARGEURL', '广东BOSS通道CHARGEURL', 'BOSS_GUANGDONG_CHARGE_URL',
        'http://221.179.7.247:8201/NGADCInterface/NGADCServicesForEC.svc/NGADCServicesForEC',
        '2016-09-20 09:33:07',
        '2016-09-20 09:33:07', '1120', '1120', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('广东BOSS通道PRDORDNUM', '广东BOSS通道PRDORDNUM', 'BOSS_GUANGDONG_PRDORDNUM', '51515000024',
        '2016-09-20 09:32:42',
        '2016-09-20 09:32:42', '1120', '1120', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('广东BOSS通道ECPASSWORD', '广东BOSS通道ECPASSWORD', 'BOSS_GUANGDONG_ECPASSWORD',
        'RO3IYqIfAHBREHIZiXCuOYdKHnL1eYEt',
        '2016-09-20 09:32:20', '2016-09-20 09:32:20', '1120', '1120', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('广东BOSS通道ECUSERNAME', '广东BOSS通道ECUSERNAME', 'BOSS_GUANGDONG_ECUSERNAME', 'admin',
        '2016-09-20 09:32:05',
        '2016-09-20 12:00:04', '1120', '1120', '1', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('广东BOSS通道ECCODE', '广东BOSS通道ECCODE', 'BOSS_GUANGDONG_ECCODE', '2001510788', '2016-09-20 09:31:26',
   '2016-09-20 09:31:26', '1120', '1120', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('卓望BOSS的充值URL', '卓望BOSS的充值URL', 'BOSS_ZW_URL', 'http://zwinterface/ec_serv_intf/forec',
        '2016-09-19 17:47:54',
        '2016-09-19 18:00:22', '1120', '1120', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('卓望BOSS的appSecret', '卓望BOSS的appSecret', 'BOSS_ZW_APP_SECRET', 'MsRer6Ik8RQCUCga1EAl2M9K',
        '2016-09-19 17:47:28',
        '2016-09-19 18:00:34', '1120', '1120', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('卓望BOSS的appKey', '卓望BOSS的appKey', 'BOSS_ZW_APP_KEY', 'HANGYAN@ZJ', '2016-09-19 17:47:08',
        '2016-09-19 18:00:43',
        '1120', '1120', '0', NULL);

-- --------------------------------------------------------------------
-- 20160920 structure of activity approval detail,  create by qinqinyan
-- --------------------------------------------------------------------
DROP TABLE IF EXISTS `activity_approval_detail`;
CREATE TABLE `activity_approval_detail` (
  `id`            BIGINT(18)   NOT NULL AUTO_INCREMENT,
  `request_id`    BIGINT(18)   NOT NULL
  COMMENT '审核请求id',
  `activity_type` INT(2)       NOT NULL
  COMMENT '活动类型',
  `activity_id`   VARCHAR(50)  NOT NULL
  COMMENT '活动uuid',
  `activity_name` VARCHAR(225) NOT NULL
  COMMENT '活动名称',
  `delete_flag`   INT(1)       NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 35
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------------------
-- 20160920 record of authority,  insert by qinqinyan
-- --------------------------------------------------------------------
INSERT INTO `authority` VALUES
  (NULL, NULL, '活动审核', 'ROLE_ACTIVITY_APPROVAL', '104009', NULL, '2016-09-18 17:15:05', NULL, NULL,
         '2016-09-18 17:15:10', '0');

-- ------------------------------------
-- 20160920 新增企业流控变更记录表,xujue
-- ------------------------------------

DROP TABLE IF EXISTS `ent_flowcontrol_record`;
CREATE TABLE `ent_flowcontrol_record` (
  `id`          BIGINT(18) NOT NULL AUTO_INCREMENT
  COMMENT '企业流控变更记录标识',
  `enter_id`    BIGINT(18) NOT NULL,
  `type`        TINYINT(4) NOT NULL
  COMMENT '操作类型，0：日上限金额，1：追加金额，2：充值金额',
  `count`       BIGINT(18) NOT NULL
  COMMENT '金额',
  `create_time` TIMESTAMP  NULL     DEFAULT NULL
  COMMENT '创建时间',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ------------------------------------
-- 20160920 新增企业流控变更记录表,xujue
-- ------------------------------------

DROP TABLE IF EXISTS `ent_flowcontrol`;
CREATE TABLE `ent_flowcontrol` (
  `id`          BIGINT(18) NOT NULL AUTO_INCREMENT
  COMMENT '企业流控信息标示',
  `enter_id`    BIGINT(18) NOT NULL,
  `count_upper` BIGINT(18) NOT NULL
  COMMENT '日上线额度',
  `creator_id`  BIGINT(18)          DEFAULT NULL
  COMMENT '创建者ID',
  `create_time` TIMESTAMP  NULL     DEFAULT NULL
  COMMENT '创建时间',
  `updator_id`  BIGINT(18)          DEFAULT NULL
  COMMENT '更新者ID',
  `update_time` TIMESTAMP  NULL     DEFAULT NULL
  COMMENT '更新时间',
  `delete_flag` INT(1)     NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ------------------------------------
-- 20160920 新增权限项企业流控,xujue
-- ------------------------------------
INSERT INTO `authority` (`AUTHORITY_ID`, `PARENT_ID`, `NAME`, `AUTHORITY_NAME`, `CODE`, `AUTHORITY_URL`, `CREATE_TIME`, `CREATOR`, `UPDATE_USER`, `UPDATE_TIME`, `DELETE_FLAG`)
VALUES (NULL, NULL, '企业流控', 'ROLE_ENTERPRISE_FLOWCONTROL', '103015', NULL, now(), NULL, NULL, now(),
              '0');

-- ------------------------------------
-- 20160920 企业表新增列 是否下发企业流控短信,xujue
-- ------------------------------------
ALTER TABLE enterprises
ADD COLUMN `fcsms_flag` INT(1) NOT NULL DEFAULT 0
COMMENT '是否下发企业流控短信，0:不发短信；1：下发短信';

-- ------------------------------------
-- 20161008 增加配置赠送流量币兑换网址,xujue
-- ------------------------------------
INSERT INTO `global_config`
VALUES
  (NULL, '赠送流量币兑换网址', '赠送流量币兑换网址', 'FLOWCOIN_PRESENT_PATH', 'http://xxx', now(), now(), '1', '1',
         '0', '0');

-- ----------------------------------
-- 20161009 集中化平台sql修改，wujiamin
-- ----------------------------------
INSERT INTO `authority` (`AUTHORITY_ID`, `PARENT_ID`, `NAME`, `AUTHORITY_NAME`, `CODE`, `AUTHORITY_URL`, `CREATE_TIME`, `CREATOR`, `UPDATE_USER`, `UPDATE_TIME`, `DELETE_FLAG`)
VALUES (NULL, NULL, '个人-业务查询-流量余额', 'ROLE_INDIVIDUAL_QUERY_FLOW', '110001', NULL, now(), NULL, NULL,
              now(), '0');
INSERT INTO `authority` (`AUTHORITY_ID`, `PARENT_ID`, `NAME`, `AUTHORITY_NAME`, `CODE`, `AUTHORITY_URL`, `CREATE_TIME`, `CREATOR`, `UPDATE_USER`, `UPDATE_TIME`, `DELETE_FLAG`)
VALUES
  (NULL, NULL, '个人-业务查询-流量币余额', 'ROLE_INDIVIDUAL_QUERY_FLOWCOIN', '110002', NULL, now(), NULL, NULL,
         now(), '0');
INSERT INTO `authority` (`AUTHORITY_ID`, `PARENT_ID`, `NAME`, `AUTHORITY_NAME`, `CODE`, `AUTHORITY_URL`, `CREATE_TIME`, `CREATOR`, `UPDATE_USER`, `UPDATE_TIME`, `DELETE_FLAG`)
VALUES
  (NULL, NULL, '个人-业务办理-流量币购买', 'ROLE_INDIVIDUAL_FLOWCOIN_PURCHASE', '110003', NULL, now(), NULL,
         NULL, now(), '0');
INSERT INTO `authority` (`AUTHORITY_ID`, `PARENT_ID`, `NAME`, `AUTHORITY_NAME`, `CODE`, `AUTHORITY_URL`, `CREATE_TIME`, `CREATOR`, `UPDATE_USER`, `UPDATE_TIME`, `DELETE_FLAG`)
VALUES
  (NULL, NULL, '个人-业务办理-红包', 'ROLE_INDIVIDUAL_REDPACKAGE', '110004', NULL, now(), NULL, NULL, now(),
         '0');
INSERT INTO `authority` (`AUTHORITY_ID`, `PARENT_ID`, `NAME`, `AUTHORITY_NAME`, `CODE`, `AUTHORITY_URL`, `CREATE_TIME`, `CREATOR`, `UPDATE_USER`, `UPDATE_TIME`, `DELETE_FLAG`)
VALUES
  (NULL, NULL, '个人-业务办理-赠送', 'ROLE_INDIVIDUAL_FLOWCOIN_PRESENT', '110005', NULL, now(), NULL, NULL,
         now(), '0');
INSERT INTO `authority` (`AUTHORITY_ID`, `PARENT_ID`, `NAME`, `AUTHORITY_NAME`, `CODE`, `AUTHORITY_URL`, `CREATE_TIME`, `CREATOR`, `UPDATE_USER`, `UPDATE_TIME`, `DELETE_FLAG`)
VALUES
  (NULL, NULL, '个人-业务办理-兑换', 'ROLE_INDIVIDUAL_EXCHANGE', '110006', NULL, now(), NULL, NULL, now(),
         '0');
INSERT INTO `authority` (`AUTHORITY_ID`, `PARENT_ID`, `NAME`, `AUTHORITY_NAME`, `CODE`, `AUTHORITY_URL`, `CREATE_TIME`, `CREATOR`, `UPDATE_USER`, `UPDATE_TIME`, `DELETE_FLAG`)
VALUES
  (NULL, NULL, '个人-账户管理', 'ROLE_INDIVIDUAL_ACCOUNT', '110007', NULL, now(), NULL, NULL, now(), '0');


ALTER TABLE `activity_info`
ADD COLUMN `code` VARCHAR(10) DEFAULT NULL
COMMENT '营销模板返回代码',
ADD COLUMN `url` VARCHAR(255) DEFAULT NULL
COMMENT '活动url';


ALTER TABLE `activity_prize`
MODIFY COLUMN `rank_name` VARCHAR(64) DEFAULT NULL
COMMENT '几等奖',
MODIFY COLUMN `product_id` BIGINT(18) DEFAULT NULL
COMMENT '产品ID';

-- ----------------------------
-- Table structure for individual_account
-- ----------------------------
DROP TABLE IF EXISTS `individual_account`;
CREATE TABLE `individual_account` (
  `id`                    BIGINT(18)     NOT NULL AUTO_INCREMENT,
  `admin_id`              BIGINT(18)     NOT NULL
  COMMENT '用户ID',
  `owner_id`              BIGINT(18)     NOT NULL
  COMMENT '账户拥有者ID（活动账户为活动id）',
  `individual_product_id` BIGINT(18)     NOT NULL
  COMMENT '账户所对应的个人产品id（话费、流量币、兑换的某个流量包）',
  `type`                  INT(8)         NOT NULL
  COMMENT '-1为个人帐户(话费，流量包)，0为活动冻结账户，1为兑换冻结账户，2为购买冻结账户',
  `count`                 DECIMAL(18, 2) NOT NULL,
  `create_time`           DATETIME       NOT NULL
  COMMENT '创建时间',
  `update_time`           DATETIME                DEFAULT NULL
  COMMENT '更新时间',
  `delete_flag`           INT(1)         NOT NULL DEFAULT '0'
  COMMENT '删除标记， 0:未删除；1：已删除',
  `version`               INT(8)                  DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_owner_id` (`owner_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 570
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for individual_account_record
-- ----------------------------
DROP TABLE IF EXISTS `individual_account_record`;
CREATE TABLE `individual_account_record` (
  `id`            BIGINT(18)     NOT NULL AUTO_INCREMENT,
  `admin_id`      BIGINT(18)     NOT NULL
  COMMENT '用户ID',
  `owner_id`      BIGINT(18)     NOT NULL
  COMMENT '账户拥有者ID（活动账户为活动id）',
  `account_id`    BIGINT(18)     NOT NULL
  COMMENT '账户ID',
  `type`          INT(1)         NOT NULL
  COMMENT '操作类型，0代表收入，1代表支出',
  `status`        INT(1)         NOT NULL DEFAULT '0',
  `error_msg`     VARCHAR(200)            DEFAULT NULL
  COMMENT '失败的原因',
  `serial_num`    VARCHAR(64)    NOT NULL
  COMMENT '操作流水号',
  `count`         DECIMAL(18, 2) NOT NULL
  COMMENT '变化数量',
  `description`   VARCHAR(255)            DEFAULT NULL
  COMMENT '描述',
  `create_time`   DATETIME                DEFAULT NULL
  COMMENT '创建时间',
  `update_time`   DATETIME                DEFAULT NULL
  COMMENT '更新时间',
  `delete_flag`   INT(1)                  DEFAULT NULL
  COMMENT '删除标记，0为未删除，1为已删除',
  `activity_type` INT(4)         NOT NULL DEFAULT '-1'
  COMMENT '7, "个人普通红包"；8, "拼手气红包"；9,"个人流量币兑换"；10,"个人流量币购买"；',
  `back`          INT(1)         NOT NULL DEFAULT '0'
  COMMENT '0-不是回退操作，1-回退操作',
  PRIMARY KEY (`id`),
  KEY `guid` (`admin_id`) USING BTREE,
  KEY `user_guid` (`owner_id`) USING BTREE,
  KEY `account_guid` (`account_id`) USING BTREE,
  KEY `serial_num_idx` (`serial_num`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 300
  DEFAULT CHARSET = utf8
  COMMENT = '用户的现金帐户变化记录表';

-- ----------------------------
-- Table structure for individual_flowcoin_exchange
-- ----------------------------
DROP TABLE IF EXISTS `individual_flowcoin_exchange`;
CREATE TABLE `individual_flowcoin_exchange` (
  `id`                    BIGINT(18)  NOT NULL AUTO_INCREMENT,
  `admin_id`              BIGINT(18)  NOT NULL
  COMMENT '购买用户id',
  `count`                 INT(10)     NOT NULL DEFAULT '0'
  COMMENT '流量币个数',
  `mobile`                VARCHAR(11) NOT NULL
  COMMENT '兑换手机号',
  `individual_product_id` BIGINT(18)  NOT NULL,
  `status`                INT(1)      NOT NULL DEFAULT '0'
  COMMENT '状态：0-兑换中，1-兑换成功，2-兑换失败',
  `create_time`           DATETIME    NOT NULL,
  `update_time`           DATETIME             DEFAULT NULL,
  `delete_flag`           INT(11)     NOT NULL DEFAULT '0',
  `version`               BIGINT(18)  NOT NULL DEFAULT '0',
  `system_serial`         VARCHAR(64) NOT NULL
  COMMENT '订单号(系统序列号）',
  PRIMARY KEY (`id`),
  KEY `idx_system_serial` (`system_serial`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 33
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for individual_flowcoin_purchase
-- ----------------------------
DROP TABLE IF EXISTS `individual_flowcoin_purchase`;
CREATE TABLE `individual_flowcoin_purchase` (
  `id`            BIGINT(18)     NOT NULL AUTO_INCREMENT,
  `admin_id`      BIGINT(18)     NOT NULL
  COMMENT '购买用户id',
  `count`         INT(10)        NOT NULL DEFAULT '0'
  COMMENT '流量币个数',
  `price`         DECIMAL(18, 2) NOT NULL DEFAULT '0.00'
  COMMENT '流量币总价',
  `status`        INT(1)         NOT NULL DEFAULT '0'
  COMMENT '状态：0-待支付，1-支付中，2-支付成功，3-支付失败，4-取消支付',
  `create_time`   DATETIME       NOT NULL,
  `update_time`   DATETIME                DEFAULT NULL,
  `expire_time`   DATETIME                DEFAULT NULL
  COMMENT '过期时间',
  `delete_flag`   INT(11)        NOT NULL DEFAULT '0',
  `version`       BIGINT(18)     NOT NULL DEFAULT '0',
  `system_serial` VARCHAR(64)    NOT NULL
  COMMENT '订单号(系统序列号）',
  PRIMARY KEY (`id`),
  KEY `idx_system_serial` (`system_serial`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 44
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for individual_flowcoin_record
-- ----------------------------
DROP TABLE IF EXISTS `individual_flowcoin_record`;
CREATE TABLE `individual_flowcoin_record` (
  `id`                           BIGINT(18) NOT NULL AUTO_INCREMENT,
  `admin_id`                     BIGINT(18) NOT NULL
  COMMENT '购买用户id',
  `individual_account_record_id` BIGINT(18) NOT NULL,
  `count`                        INT(10)    NOT NULL DEFAULT '0'
  COMMENT '流量币个数',
  `type`                         INT(1)     NOT NULL
  COMMENT '收入，支出类型',
  `create_time`                  DATETIME   NOT NULL,
  `update_time`                  DATETIME            DEFAULT NULL,
  `expire_time`                  DATETIME            DEFAULT NULL
  COMMENT '过期时间',
  `delete_flag`                  INT(1)     NOT NULL DEFAULT '0'
  COMMENT '支出的手机号',
  `description`                  VARCHAR(64)         DEFAULT NULL
  COMMENT '描述(）',
  `mobile`                       VARCHAR(11)         DEFAULT NULL,
  `activity_type`                INT(2)              DEFAULT NULL
  COMMENT '活动类型',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 47
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for individual_product
-- ----------------------------
DROP TABLE IF EXISTS `individual_product`;
CREATE TABLE `individual_product` (
  `id`            BIGINT(18)   NOT NULL AUTO_INCREMENT
  COMMENT '自增ID，主键',
  `product_code`  VARCHAR(64)           DEFAULT NULL,
  `type`          INT(8)       NOT NULL DEFAULT '2'
  COMMENT '产品类型，0为话费，1为流量币产品，2为流量包产品',
  `name`          VARCHAR(128) NOT NULL,
  `create_time`   DATETIME     NOT NULL
  COMMENT '创建时间',
  `update_time`   DATETIME              DEFAULT NULL
  COMMENT '更新时间',
  `delete_flag`   INT(1)       NOT NULL DEFAULT '0'
  COMMENT '删除Flag，0:未删除；1：已删除',
  `price`         INT(10)      NOT NULL DEFAULT '1',
  `product_size`  BIGINT(18)            DEFAULT NULL
  COMMENT '流量包大小,以KB为单位',
  `default_value` INT(1)                DEFAULT '0'
  COMMENT '个人用户一开始就默认拥有该产品：1-默认拥有；0-默认不有用',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for individual_product_map
-- ----------------------------
DROP TABLE IF EXISTS `individual_product_map`;
CREATE TABLE `individual_product_map` (
  `id`                    BIGINT(18) NOT NULL AUTO_INCREMENT,
  `admin_id`              BIGINT(18) NOT NULL,
  `individual_product_id` BIGINT(18) NOT NULL,
  `create_time`           DATETIME   NOT NULL,
  `update_time`           DATETIME            DEFAULT NULL,
  `delete_flag`           INT(1)     NOT NULL DEFAULT '0',
  `price`                 INT(10)             DEFAULT NULL,
  `discount`              INT(4)     NOT NULL DEFAULT '100',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 36
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- 初始化个人集中化平台产品
-- ----------------------------
INSERT INTO `individual_product`
VALUES (NULL, '1', '0', '个人通信账户话费', now(), now(), '0', '0', '0', '1');
INSERT INTO `individual_product` VALUES (NULL, '2', '1', '流量币', now(), now(), '0', '10', '0', '1');
INSERT INTO `individual_product`
VALUES (NULL, 'ACAZ25208', '2', '本地流量统付3元10M', now(), now(), '0', '300', '10240', '1');
INSERT INTO `individual_product`
VALUES (NULL, 'ACAZ25197', '2', '本地流量统付5元30M', now(), now(), '0', '500', '30720', '1');
INSERT INTO `individual_product`
VALUES (NULL, 'ACAZ25198', '2', '本地流量统付10元70M', now(), now(), '0', '1000', '71680', '1');
INSERT INTO `individual_product`
VALUES (NULL, 'ACAZ25199', '2', '本地流量统付20元150M', now(), now(), '0', '2000', '153600', '1');
INSERT INTO `individual_product`
VALUES (NULL, 'ACAZ25200', '2', '本地流量统付30元500M', now(), now(), '0', '3000', '512000', '1');
INSERT INTO `individual_product`
VALUES (NULL, 'ACAZ25202', '2', '本地流量统付50元1G', now(), now(), '0', '5000', '1048576', '1');
INSERT INTO `individual_product`
VALUES (NULL, 'ACAZ25203', '2', '本地流量统付70元2G', now(), now(), '0', '7000', '2097152', '1');
INSERT INTO `individual_product`
VALUES (NULL, 'ACAZ25204', '2', '本地流量统付100元3G', now(), now(), '0', '10000', '3145728', '1');
INSERT INTO `individual_product`
VALUES (NULL, 'ACAZ25205', '2', '本地流量统付130元4G', now(), now(), '0', '13000', '4194304', '1');
INSERT INTO `individual_product`
VALUES (NULL, 'ACAZ25206', '2', '本地流量统付180元6G', now(), now(), '0', '18000', '6291456', '1');
INSERT INTO `individual_product`
VALUES (NULL, 'ACAZ25207', '2', '本地流量统付280元11G', now(), now(), '0', '28000', '11534336', '1');

-- ----------------------------
-- Table structure for label_config
-- ----------------------------
DROP TABLE IF EXISTS `label_config`;
CREATE TABLE `label_config` (
  `id`            BIGINT(18)   NOT NULL AUTO_INCREMENT
  COMMENT '标识',
  `name`          VARCHAR(64)  NOT NULL
  COMMENT 'Key值名称',
  `description`   VARCHAR(100)          DEFAULT NULL
  COMMENT '配置描述',
  `default_value` VARCHAR(100) NOT NULL
  COMMENT '配置默认值',
  `create_time`   DATETIME     NOT NULL,
  `update_time`   DATETIME              DEFAULT NULL,
  `creator_id`    BIGINT(18)   NOT NULL,
  `updater_id`    BIGINT(18)   NOT NULL,
  `delete_flag`   INT(1)       NOT NULL,
  `config_update` INT(1)                DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `key_index` (`name`) USING BTREE,
  KEY `id_index` (`id`, `delete_flag`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

-- --------------------------
-- 20161011 增加标签配置权限， panxin
-- --------------------------
INSERT INTO `authority` (`PARENT_ID`, `NAME`, `AUTHORITY_NAME`, `CODE`, `AUTHORITY_URL`, `CREATE_TIME`, `CREATOR`, `UPDATE_USER`, `UPDATE_TIME`, `DELETE_FLAG`)
VALUES
  (NULL, '标签配置管理', 'ROLE_LABEL_CONFIG', '109002', NULL, '2016-10-11 11:32:55', NULL, NULL,
   '2016-10-11 11:32:57', '0');
-- ----------------------------
-- Table structure for history_enterprises and history_enterprise_file by qinqinyan in 12/10/2016
-- ----------------------------
DROP TABLE IF EXISTS `history_enterprises`;
CREATE TABLE `history_enterprises` (
  `id`                 BIGINT(18)   NOT NULL AUTO_INCREMENT
  COMMENT '变更记录ID',
  `request_id`         BIGINT(18)   NOT NULL
  COMMENT '请求审核ID',
  `ent_id`             BIGINT(18)   NOT NULL
  COMMENT '企业ID',
  `name`               VARCHAR(255) NOT NULL
  COMMENT '企业名称',
  `code`               VARCHAR(64)           DEFAULT NULL
  COMMENT '企业代码',
  `phone`              VARCHAR(64)           DEFAULT NULL
  COMMENT '企业手机号码',
  `email`              VARCHAR(100)          DEFAULT NULL,
  `create_time`        DATETIME     NOT NULL
  COMMENT '创建时间',
  `update_time`        DATETIME              DEFAULT NULL
  COMMENT '更新时间',
  `status`             TINYINT(4)            DEFAULT NULL
  COMMENT '1-体验企业，2-认证企业，3-合作企业',
  `delete_flag`        INT(1)       NOT NULL DEFAULT '0'
  COMMENT '删除Flag，0:未删除；1：已删除; 2:暂停;3-下线(BOSS关停)；4-等待市级管员审核；5-等待省级管理员审核',
  `creator_id`         BIGINT(18)            DEFAULT NULL,
  `app_secret`         VARCHAR(64)           DEFAULT '',
  `ent_name`           VARCHAR(255)          DEFAULT NULL
  COMMENT '企业品牌',
  `district_id`        BIGINT(18)            DEFAULT NULL
  COMMENT '企业所属地区id',
  `customer_type_id`   BIGINT(18)            DEFAULT NULL
  COMMENT '客户分类id',
  `benefit_grade_id`   BIGINT(18)            DEFAULT NULL
  COMMENT '效益标识id',
  `discount`           BIGINT(18)            DEFAULT '1'
  COMMENT '折扣信息关联到折扣表id',
  `business_type_id`   BIGINT(18)            DEFAULT NULL
  COMMENT '业务类别',
  `pay_type_id`        BIGINT(18)            DEFAULT NULL
  COMMENT '支付方式',
  `interface_flag`     INT(10)      NOT NULL DEFAULT '0'
  COMMENT '是否开通接口调用：1-是，0-否',
  `start_time`         DATETIME              DEFAULT NULL
  COMMENT '合作开始时间',
  `end_time`           DATETIME              DEFAULT NULL
  COMMENT '合作结束时间',
  `app_key`            VARCHAR(64)           DEFAULT NULL
  COMMENT '企业appKey',
  `licence_start_time` DATETIME              DEFAULT NULL
  COMMENT '工商营业执照开始时间',
  `licence_end_time`   DATETIME              DEFAULT NULL
  COMMENT '工商营业执照结束时间',
  `cm_email`           VARCHAR(100)          DEFAULT NULL
  COMMENT '客户经理邮箱',
  `cm_phone`           VARCHAR(11)           DEFAULT NULL
  COMMENT '客户经理手机',
  `fcsms_flag`         INT(1)       NOT NULL DEFAULT '0'
  COMMENT '是否下发企业流控短信，0:不发短信；1：下发短信',
  `comment`            VARCHAR(300) NOT NULL
  COMMENT '修改备注，必填',
  PRIMARY KEY (`id`),
  KEY `name_index` (`name`(191), `delete_flag`) USING BTREE,
  KEY `code_index` (`code`, `delete_flag`) USING BTREE,
  KEY `request_id` (`request_id`) USING BTREE,
  KEY `ent_id` (`ent_id`) USING BTREE,
  KEY `createor_index` (`delete_flag`, `creator_id`) USING BTREE,
  KEY `appkey_index` (`app_key`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `history_enterprise_file`;
CREATE TABLE `history_enterprise_file` (
  `id`                        BIGINT(18) NOT NULL AUTO_INCREMENT
  COMMENT '变更记录ID',
  `request_id`                BIGINT(18) NOT NULL
  COMMENT '请求审核ID',
  `ent_id`                    BIGINT(18) NOT NULL,
  `customerfile_name`         VARCHAR(500)        DEFAULT NULL
  COMMENT '客户说明附件',
  `image_name`                VARCHAR(500)        DEFAULT NULL
  COMMENT '审批截图',
  `contract_name`             VARCHAR(500)        DEFAULT NULL
  COMMENT '合作协议文件',
  `business_licence`          VARCHAR(500)        DEFAULT NULL
  COMMENT '企业工商营业执照',
  `authorization_certificate` VARCHAR(500)        DEFAULT NULL
  COMMENT '企业管理员授权证明',
  `identification_card`       VARCHAR(500)        DEFAULT NULL
  COMMENT '企业管理员身份证',
  `identification_back`       VARCHAR(500)        DEFAULT NULL,
  `customerfile_key`          VARCHAR(50)         DEFAULT NULL,
  `image_key`                 VARCHAR(50)         DEFAULT NULL,
  `contract_key`              VARCHAR(50)         DEFAULT NULL,
  `licence_key`               VARCHAR(50)         DEFAULT NULL,
  `authorization_key`         VARCHAR(50)         DEFAULT NULL,
  `identification_key`        VARCHAR(50)         DEFAULT NULL,
  `identification_back_key`   VARCHAR(50)         DEFAULT NULL,
  `create_time`               DATETIME   NOT NULL,
  `update_time`               DATETIME   NOT NULL,
  PRIMARY KEY (`id`),
  KEY `request_id` (`request_id`) USING BTREE,
  KEY `ent_id` (`ent_id`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------
-- 20161012 增加信息变更审核权限， xujue
-- --------------------------
INSERT INTO `authority` (`PARENT_ID`, `NAME`, `AUTHORITY_NAME`, `CODE`, `AUTHORITY_URL`, `CREATE_TIME`, `CREATOR`, `UPDATE_USER`, `UPDATE_TIME`, `DELETE_FLAG`)
VALUES
  (NULL, '信息变更审核', 'ROLE_ENT_INFOMATION_CHANGE_APPROVAL', '103016', NULL, now(), NULL, NULL, now(),
   '0');

-- --------------------------
-- 20161013 增加信息变更历史表存送字段， xujue
-- --------------------------
ALTER TABLE `history_enterprises`
ADD `give_money_id` BIGINT(18) NOT NULL DEFAULT '1'
COMMENT '存送Id';

-- --------------------------
-- 20161012 增加unionFlow的全局配置项， sunyiwei
-- --------------------------
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('Unionflow的充值地址', 'Unionflow的充值地址', 'BOSS_UNIONFLOW_CHARGEURL', 'NULL', NOW(), NOW(), '1', '1',
   '0', '0');

INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('Unionflow的AppKey', 'Unionflow的AppKey', 'BOSS_UNIONFLOW_APPKEY', 'NULL', NOW(), NOW(), '1', '1',
   '0', '0');

INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('Unionflow的AppSecret', 'Unionflow的AppSecret', 'BOSS_UNIONFLOW_APPSECRET', 'NULL', NOW(), NOW(),
   '1', '1', '0', '0');

-- ----------------------------------
-- 20161012 charge_record price 默认为0
-- ----------------------------------
ALTER TABLE `charge_record`
MODIFY COLUMN `price` BIGINT(7) NULL DEFAULT 0
AFTER `fingerprint`;

-- -----------------------------------
-- 20161014 administer delete code, add password_update_time,wujiamin
-- -----------------------------------
ALTER TABLE `administer`
ADD COLUMN `password_update_time` DATETIME NULL
AFTER `salt`;

ALTER TABLE `administer`
DROP COLUMN `code`;

-- -----------------------------
-- 20161014 增加密码有效天数配置项, wujiamin
-- -----------------------------
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (NULL, '密码有效期时间', '密码有效期时间(填写天数，-1为长期有效)', 'PASSWORD_EXPIRE_TIME', '-1', NOW(), NOW(), '1', '1',
         '0', NULL);

-- ----------------------------------
-- 20161014 增加全局变量配置，平台登录URL by qinqinyan
-- ----------------------------------
INSERT INTO `global_config` VALUES
  (NULL, '平台登录URL', '平台登录URL', 'LOGIN_URL', 'httphttp://localhost:8080/web-in/manage/index.html',
         '2016-10-14 17:37:06',
         '2016-10-14 17:37:06', '1', '1', '0', NULL);

-- ----------------------------------
-- 20161017 activity_win_record增加列size by qinqinyan
-- ----------------------------------
ALTER TABLE `activity_win_record`
ADD COLUMN `size` VARCHAR(18) DEFAULT NULL
COMMENT '奖品大小';

-- ----------------------------------
-- 20161017 label_config增加活动提示语 by panxin
-- ----------------------------------
INSERT INTO `label_config` VALUES
  (NULL, 'LOTTERY_ACTIVITYRULE_RED_WARNING', '转盘活动规则提示语', '禁止发布、复制、传播违法敏感信息', '2016-10-12 13:18:47',
   '2016-10-12 13:52:40', 1, 1, 0, NULL);
INSERT INTO `label_config` VALUES
  (NULL, 'REDPACKET_DESCRIPTION_RED_WARNING', '红包活动描述提示语', '禁止发布、复制、传播违法敏感信息',
   '2016-10-12 13:30:43',
   '2016-10-12 13:52:36', 1, 1, 0, NULL);
INSERT INTO `label_config` VALUES
  (NULL, 'REDPACKET_PEOPLE_RED_WARNING', '红包活动对象提示语', '禁止发布、复制、传播违法敏感信息', '2016-10-12 13:32:09',
   '2016-10-12 13:52:33',
   1, 1, 0, NULL);
INSERT INTO `label_config` VALUES
  (NULL, 'REDPACKET_ACTIVITYDES_RED_WARNING', '红包活动规则提示语', '禁止发布、复制、传播违法敏感信息',
   '2016-10-12 13:32:33',
   '2016-10-12 13:52:29', 1, 1, 0, NULL);
INSERT INTO `label_config` VALUES
  (NULL, 'GOLDENBALL_ACTIVITYRULE_RED_WARNING', '砸金蛋活动规则提示语', '禁止发布、复制、传播违法敏感信息',
   '2016-10-12 13:41:59',
   '2016-10-12 13:52:26', 1, 1, 0, NULL);

-- ------------------------------
-- 20161018 企业EC信息, wujiamin
-- ------------------------------
INSERT INTO `authority` (`PARENT_ID`, `NAME`, `AUTHORITY_NAME`, `CODE`, `AUTHORITY_URL`, `CREATE_TIME`, `CREATOR`, `UPDATE_USER`, `UPDATE_TIME`, `DELETE_FLAG`)
VALUES
  (NULL, '企业EC信息审核', 'ROLE_ENTERPRISE_EC_APPROVAL', '103018', NULL, now(), NULL, NULL, now(), '0');

ALTER TABLE `enterprises`
MODIFY COLUMN `interface` INT(10) NOT NULL DEFAULT 2
COMMENT '是否开通接口调用：1-是,0-否,2-未申请(初始化),3-申请中(初始化),4-已驳回(初始化)'
AFTER `pay_type_id`;

ALTER TABLE `enterprises`
ADD COLUMN `interface_approval_status` INT(2) NULL
COMMENT '接口信息变更的审核状态：0-申请中；1-已通过；2-已驳回；'
AFTER `interface`;

ALTER TABLE `enterprises`
ADD COLUMN `interface_expire_time` DATETIME DEFAULT NULL
COMMENT '接口appkey及appsecret过期时间'
AFTER `interface`;

INSERT INTO `global_config` VALUES
  (NULL, '接口appkey及appsecret有效期时间', '-1长期有效，其他则为有效天数', 'INTERFACE_EXPIRE_TIME', '90', now(), now(),
         '1', '1', '0',
         NULL);

DROP TABLE IF EXISTS `ec_approval_detail`;
CREATE TABLE `ec_approval_detail` (
  `id`           BIGINT(18)   NOT NULL AUTO_INCREMENT,
  `request_id`   BIGINT(18)   NOT NULL,
  `ent_id`       BIGINT(18)   NOT NULL,
  `ip1`          VARCHAR(20)  NOT NULL,
  `ip2`          VARCHAR(20)           DEFAULT NULL,
  `ip3`          VARCHAR(20)           DEFAULT NULL,
  `callback_url` VARCHAR(510) NOT NULL,
  `delete_flag`  INT(2)       NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ------------------------------
-- 20161020 四川平台企业关停的boss余额阈值
-- ------------------------------
INSERT INTO `global_config` VALUES
  (NULL, '四川平台企业关停的boss余额阈值', '四川平台企业关停的boss余额阈值', 'SC_ACCOUNT_BALANCE', '5000', NOW(), NOW(), '1',
         '1', '0', NULL);

-- ---------------------
-- 20161024 邮件提醒的邮箱配置
-- ---------------------
INSERT INTO `global_config`
VALUES (NULL, '发送邮箱地址', '平台发送提醒邮件的邮箱地址', 'MAIL_ADDRESS', 'XXX', NOW(), NOW(), '1', '1', '0', NULL);

-- ------------------------------
-- 20161024 approval_request表中增加 result字段   xujue
-- ------------------------------
ALTER TABLE `approval_request`
ADD COLUMN `result` INT(2) DEFAULT NULL
COMMENT '审核状态，审核中：0, 审核通过：1, 审核驳回：2 '
AFTER `delete_flag`;

-- -------------------------
-- 20161025 营销卡密码数据库密文存储
-- -------------------------
ALTER TABLE `mdrc_card_info_16`
MODIFY COLUMN `card_password` VARCHAR(64)
CHARACTER SET utf8
COLLATE utf8_general_ci NULL DEFAULT NULL
COMMENT '密码'
AFTER `card_number`,
ADD COLUMN `salt` VARCHAR(64) NULL
AFTER `card_password`;
ALTER TABLE `mdrc_card_info_17`
MODIFY COLUMN `card_password` VARCHAR(64)
CHARACTER SET utf8
COLLATE utf8_general_ci NULL DEFAULT NULL
COMMENT '密码'
AFTER `card_number`,
ADD COLUMN `salt` VARCHAR(64) NULL
AFTER `card_password`;
ALTER TABLE `mdrc_card_info_18`
MODIFY COLUMN `card_password` VARCHAR(64)
CHARACTER SET utf8
COLLATE utf8_general_ci NULL DEFAULT NULL
COMMENT '密码'
AFTER `card_number`,
ADD COLUMN `salt` VARCHAR(64) NULL
AFTER `card_password`;
ALTER TABLE `mdrc_card_info_19`
MODIFY COLUMN `card_password` VARCHAR(64)
CHARACTER SET utf8
COLLATE utf8_general_ci NULL DEFAULT NULL
COMMENT '密码'
AFTER `card_number`,
ADD COLUMN `salt` VARCHAR(64) NULL
AFTER `card_password`;

-- ----------------------
-- 20161023 企业ip白名单
-- ----------------------
DROP TABLE IF EXISTS `ent_white_list`;
CREATE TABLE `ent_white_list` (
  `id`          BIGINT(18)  NOT NULL AUTO_INCREMENT
  COMMENT 'ID',
  `ent_id`      BIGINT(18)  NOT NULL
  COMMENT '企业ID',
  `ip_address`  VARCHAR(64) NOT NULL
  COMMENT 'ip地址',
  `create_time` DATETIME    NOT NULL
  COMMENT '创建时间',
  `update_time` DATETIME             DEFAULT NULL
  COMMENT '更新时间',
  `delete_flag` INT(1)      NOT NULL DEFAULT '0'
  COMMENT '删除标记， 0:未删除；1：已删除',
  PRIMARY KEY (`id`),
  KEY `ent_id_index` (`ent_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 16
  DEFAULT CHARSET = utf8;

-- ---------------------
-- 20161030 修改ec详情中回调地址可以为空
-- ---------------------
ALTER TABLE `ec_approval_detail`
MODIFY COLUMN `callback_url` VARCHAR(510)
CHARACTER SET utf8
COLLATE utf8_general_ci NULL
AFTER `ip3`;
-- -------------------------------------
-- -20161028  增加流量卡起始日期 BY LILIN
-- -------------------------------------
ALTER TABLE `mdrc_card_info`
ADD COLUMN `start_time` DATETIME NULL DEFAULT NULL
COMMENT '生效日期'
AFTER `user_ip`;

-- -------------------------------------
-- -20161031  增加信息变更权限 BY qinqinyan
-- -------------------------------------
INSERT INTO `authority` VALUES
  (NULL, NULL, '企业信息变更', 'ROLE_ENTERPRISE_CHANGE', '103017', NULL, '2016-10-17 20:33:02', NULL,
         NULL,
         '2016-10-17 20:33:05', '0');

-- -------------------------------------
-- -20161031  增加流量卡起始日期 BY LILIN
-- -------------------------------------
ALTER TABLE `mdrc_card_info_16`
ADD COLUMN `start_time` DATETIME NULL DEFAULT NULL
COMMENT '生效日期'
AFTER `user_ip`;
ALTER TABLE `mdrc_card_info_17`
ADD COLUMN `start_time` DATETIME NULL DEFAULT NULL
COMMENT '生效时间'
AFTER `user_ip`;
ALTER TABLE `mdrc_card_info_18`
ADD COLUMN `start_time` DATETIME NULL DEFAULT NULL
COMMENT '生效时间'
AFTER `reponse_serial_number`;
ALTER TABLE `mdrc_card_info_19`
ADD COLUMN `start_time` DATETIME NULL DEFAULT NULL
COMMENT '生效时间'
AFTER `reponse_serial_number`;

-- ------------------------------------
-- -20161031  增加批量赠送块流水号与订单流水号的关联表, by sunyiwei
-- -------------------------------------
CREATE TABLE `present_serial_num` (
  `id`                  BIGINT(18)  NOT NULL AUTO_INCREMENT,
  `block_serial_num`    VARCHAR(64) NOT NULL
  COMMENT '批流水号',
  `platform_serial_num` VARCHAR(64) NOT NULL
  COMMENT '订单的平台流水号',
  `create_time`         DATETIME    NOT NULL
  COMMENT '创建时间',
  `update_time`         DATETIME             DEFAULT NULL
  COMMENT '更新时间',
  `delete_flag`         INT(1)      NOT NULL DEFAULT '0'
  COMMENT '删除标记， 0:未删除；1：已删除',
  PRIMARY KEY (`id`),
  KEY `idx_block_serial_num` (`block_serial_num`) USING BTREE,
  KEY `idx_plt_serial_num` (`platform_serial_num`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = '批量赠送时，块处理有个流水号，块中的每个订单也都有个流水号，需要进行关联';

-- ------------------------
-- 20161103 回调地址长度增加
-- ------------------------
ALTER TABLE `ent_callback_addr`
MODIFY COLUMN `callback_addr` VARCHAR(510)
CHARACTER SET utf8
COLLATE utf8_general_ci NOT NULL
COMMENT '创建时间'
AFTER `ent_id`;

-- ----------------------
-- 20161109 增加索引，解决充值报表导出很慢
-- ----------------------
ALTER TABLE ent_manager ADD INDEX idx_enter_id(enter_id) USING BTREE;

-- ------------------------------------
-- 20161114 全局配置项，EC接口ip白名单开关 
-- ------------------------------------
INSERT INTO `global_config` VALUES
  (NULL, 'EC接口ip白名单开关', 'EC接口ip白名单开关(0-不检验ip；1-需校验ip)', 'EC_IP_WHITELIST', '0', NOW(), NOW(), '1',
         '1', '0', NULL);

-- ----------------------------
-- 20161115 Table structure for `activity_template` by qinqinyan
-- ----------------------------
DROP TABLE IF EXISTS `activity_template`;
CREATE TABLE `activity_template` (
  `id`                BIGINT(18)   NOT NULL AUTO_INCREMENT,
  `activity_id`       VARCHAR(225) NOT NULL
  COMMENT '活动ID',
  `user_type`         INT(2)       NOT NULL
  COMMENT '用户类型。0:平台无关，以手机号为key;1:微信授权,以openid为key',
  `gived_number`      INT(18)               DEFAULT NULL
  COMMENT '用户可中最大数量；不填：无限',
  `daily`             INT(1)       NOT NULL
  COMMENT '用户次数每天是否重置,红包是false,true:每天刷新次数;false:互动期间不刷新次数',
  `max_play_number`   INT(18)               DEFAULT NULL
  COMMENT '用户最大可玩次数,红包填-1，其他活动必须填写大于等于0的数字',
  `check_type`        INT(2)       NOT NULL
  COMMENT '鉴权方式,0:不做鉴权;1:白名单;2:黑名单',
  `check_url`         VARCHAR(255)          DEFAULT NULL
  COMMENT '鉴权url,如果check_type为0,则为空',
  `fixed_probability` INT(2)       NOT NULL
  COMMENT '中奖概率:0:不固定概率;1:固定概率',
  `description`       VARCHAR(602)          DEFAULT NULL
  COMMENT '活动描述',
  `object`            VARCHAR(602)          DEFAULT NULL
  COMMENT '活动对象',
  `rules`             VARCHAR(602)          DEFAULT NULL
  COMMENT '活动规则',
  `create_time`       DATETIME     NOT NULL,
  `update_time`       DATETIME     NOT NULL,
  `delete_flag`       INT(1)       NOT NULL
  COMMENT '删除标记:0未删除；1已删除',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

-- ------------------------------------
-- 20161117 全局配置项，各种频率限制
-- ------------------------------------
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('登录成功的频率限制开关', '登录成功的频率限制开关', 'LOGIN_SUCCESS_LIMIT', 'ON', '2016-11-17 15:41:18',
        '2016-11-17 15:41:18', '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('登录成功的频率限制的时间范围', '登录成功的频率限制的时间范围，即以多长时间为限制', 'LOGIN_SUCCESS_LIMIT_RANGE', '300',
        '2016-11-17 15:41:37', '2016-11-17 15:41:37', '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('登录成功的频率限制的次数', '登录成功的频率限制的次数', 'LOGIN_SUCCESS_LIMIT_COUNT', '2', '2016-11-17 15:41:53',
        '2016-11-17 15:41:53', '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('登录失败的频率限制', '登录失败的频率限制，ON为开启，其它为关闭', 'LOGIN_FAIL_LIMIT', 'ON', '2016-11-17 16:03:09',
        '2016-11-17 16:03:09', '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('登录失败的频率限制的时间范围', '登录失败的频率限制的时间范围，即以多长时间为限制', 'LOGIN_FAIL_LIMIT_RANGE', '300',
        '2016-11-17 16:03:24', '2016-11-17 16:03:24', '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('登录失败的频率限制的次数', '登录失败的频率限制的次数', 'LOGIN_FAIL_LIMIT_COUNT', '2', '2016-11-17 16:03:34',
        '2016-11-17 16:03:34', '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('普通赠送的频率限制', '普通赠送的频率限制，ON为开启，其它为关闭', 'PRESENT_RATE_LIMIT', 'ON', '2016-11-17 17:21:55',
        '2016-11-17 17:21:55', '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('普通赠送的频率限制的时间范围', '普通赠送的频率限制的时间范围，即以多长时间为限制', 'PRESENT_RATE_LIMIT_RANGE', '300',
        '2016-11-17 17:22:10', '2016-11-17 17:22:10', '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('普通赠送的频率限制的次数', '普通赠送的频率限制的次数', 'PRESENT_RATE_LIMIT_COUNT', '2', '2016-11-17 17:22:19',
        '2016-11-17 17:22:19', '1', '1', '0', NULL);

-- ------------------------------------
-- 20161121 暂停值，告警值配置
-- ------------------------------------
ALTER TABLE `account`
ADD COLUMN `alert_count` DOUBLE(18, 2) NOT NULL DEFAULT 0.00
AFTER `min_count`;
ALTER TABLE `account`
ADD COLUMN `stop_count` DOUBLE(18, 2) NOT NULL DEFAULT 0.00
AFTER `alert_count`;

-- ------------------------------------
-- 20161121 全局配置项，账号余额短信通知
-- ------------------------------------
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('账户余额不足短信通知开关控制', '现金账户余额不足，短信通知的开关控制（OK为启用，其它为不启用)', 'ACCOUNT_ALERT_MSG', 'OK',
   '2016-11-19 09:31:05',
   '2016-11-19 10:06:01', '1', '1', '0', NULL);

-- 20161114 全局配置项，企业流控开关
-- ------------------------------------
INSERT INTO `global_config` VALUES
  (NULL, '企业流控控制开关', '企业流控控制开关(OK-开启; 其余为关闭)', 'ENTERPRISE_FLOW_CONTROL', 'OK', NOW(), NOW(), '1',
         '1', '0', NULL);
INSERT INTO `global_config` VALUES
  (NULL, '企业流控短信开关(客户经理)', '企业流控短信开关(客户经理)(OK-开启; 其余为关闭)', 'ENT_FLOW_CONTROL_SMS_CMANAGER', 'OK',
         NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` VALUES
  (NULL, '企业流控控制开关(企业管理员)', '企业流控控制开关(企业管理员)(OK-开启; 其余为关闭)', 'ENT_FLOW_CONTROL_SMS_ADMINFORENTER',
         'OK', NOW(), NOW(), '1', '1', '0', NULL);

-- ----------------------------
-- 20161122 增加present_record,charge_record,interface_record 状态码字段
-- ----------------------------
ALTER TABLE `present_record` ADD `status_code` VARCHAR(50) DEFAULT NULL
COMMENT '状态码'
AFTER `status`;
ALTER TABLE `charge_record` ADD `status_code` VARCHAR(50) DEFAULT NULL
COMMENT '状态码'
AFTER `status`;
ALTER TABLE `interface_record` ADD `status_code` VARCHAR(50) DEFAULT NULL
COMMENT '状态码'
AFTER `status`;

-- ----------------------------
-- 20161123 增加discount_record,sd_dailystatistic 山东对账使用的表
-- ----------------------------
DROP TABLE IF EXISTS `discount_record`;
CREATE TABLE `discount_record` (
  `id`          BIGINT(18)                NOT NULL AUTO_INCREMENT,
  `user_id`     VARCHAR(64)
                CHARACTER SET utf8
                COLLATE utf8_general_ci   NOT NULL,
  `prd_code`    VARCHAR(70)
                CHARACTER SET utf8
                COLLATE utf8_general_ci   NOT NULL,
  `discount`    INT(5)                    NOT NULL,
  `create_time` DATETIME                  NOT NULL,
  `update_time` DATETIME                  NOT NULL,
  `delete_flag` INT(18) UNSIGNED ZEROFILL NOT NULL
  COMMENT '删除标识位：0未删除；1已删除',
  PRIMARY KEY (`id`),
  KEY `userPrdTime_index` (`user_id`, `prd_code`, `create_time`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  AUTO_INCREMENT = 1;

DROP TABLE IF EXISTS `sd_dailystatistic`;
CREATE TABLE `sd_dailystatistic` (
  `id`                        BIGINT(18)              NOT NULL AUTO_INCREMENT,
  `date`                      VARCHAR(10)
                              CHARACTER SET utf8
                              COLLATE utf8_general_ci NOT NULL
  COMMENT '格式',
  `database_count`            INT(10)                 NOT NULL,
  `database_originprice`      DOUBLE(10, 2)           NOT NULL,
  `database_discountprice`    DOUBLE(10, 2)           NOT NULL,
  `bill_count`                INT(10)                 NOT NULL,
  `bil_originprice`           DOUBLE(10, 2)           NOT NULL,
  `bill_discountprice`        DOUBLE(10, 2)           NOT NULL,
  `huadanone_count`           INT(10)                 NOT NULL,
  `huadanone_originprice`     DOUBLE(10, 0)           NOT NULL,
  `huadanone_discountprice`   DOUBLE(10, 2)           NOT NULL,
  `huadanthree_count`         INT(10)                 NOT NULL,
  `huadanthree_originprice`   DOUBLE(10, 2)           NOT NULL,
  `huadanthree_discountprice` DOUBLE(10, 2)           NOT NULL,
  PRIMARY KEY (`id`),
  KEY `date_index` (`date`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  AUTO_INCREMENT = 1;

-- ----------------------------
-- 20161123 扩展supplier_product的feature字段的长度，山东需求
-- ----------------------------
ALTER TABLE `supplier_product`
MODIFY COLUMN `feature` VARCHAR(1024)
CHARACTER SET utf8
COLLATE utf8_general_ci NULL DEFAULT NULL
COMMENT '供应商定义的产品相关的信息，用JSON字符串表达'
AFTER `price`;

-- ----------------------------
-- 20161123 global_config山东相关 包括 山东boss充值，山东ftp，山东对账存放路径，山东发送邮件的配置
-- ----------------------------
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('山东云平台充值URL地址', '山东云平台充值URL地址', 'SD_CLOUD_URL', 'http://211.137.190.207:8089', NOW(), NOW(), '1',
   '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('山东云平台充值platformid', '山东云平台充值platformid', 'SD_CLOUD_PLATFORMID', 'SDLLPT_20161018', NOW(), NOW(),
   '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('山东充值参数feetype', '山东充值参数feetype', 'SD_BOSS_FEETYPE', '1', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('山东充值参数factfee', '山东充值参数factfee', 'SD_BOSS_FACTFEE', '0', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('山东充值参数limitflow', '山东充值参数limitflow', 'SD_BOSS_LIMITFLOW', '0', NOW(), NOW(), '1', '1', '0',
        NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('山东充值参数itemid', '山东充值参数itemid', 'SD_BOSS_ITEMID', '0', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('山东充值参数status', '山东充值参数status', 'SD_BOSS_STATUS', '01', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('山东云平台ftp上传url', '山东云平台ftp上传url', 'SD_FTP_URL', '223.99.248.117', NOW(), NOW(), '1', '1', '0',
   NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('山东云平台ftp登录用户名', '山东云平台ftp登录用户名', 'SD_FTP_LOGINNAME', 'llftp', NOW(), NOW(), '1', '1', '0',
        NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('山东云平台ftp登录密码', '山东云平台ftp登录密码', 'SD_FTP_LOGINPASS', '123', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('山东云平台ftp文件路径', '山东云平台ftp文件路径', 'SD_FTP_FILEPATH', '/home/llftp/', NOW(), NOW(), '1', '1', '0',
   NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('山东对账存放账单路径', '山东对账存放账单路径', 'SD_RECONCILE_BILL_PATH',
        '/usr/share/tomcat/shandongfile/shandongBill/bill', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('山东对账存放话单路径', '山东对账存放话单路径', 'SD_RECONCILE_HUADAN_PATH',
        '/usr/share/tomcat/shandongfile/shandongBill/huadan', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('山东对账存放差异记录路径', '山东对账存放差异记录路径', 'SD_RECONCILE_CHANGERECORD_PATH',
        '/usr/share/tomcat/shandongfile/shandongBill/changeRecords', NOW(), NOW(), '1', '1', '0',
        NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('云平台上传通知路径', '云平台上传通知路径', 'SD_RECONCILE_NOTICE_URL',
        'http://127.0.0.1:8091/bizplatform/ftpTranFile/dev/ftpTranFile', NOW(), NOW(), '1', '1',
        '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('email服务器地址', 'email服务器地址', 'SD_EMAIL_HOST', 'smtp.chinamobile.com', NOW(), NOW(), '1', '1', '0',
   NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('email发送者', 'email发送者', 'SD_EMAIL_SENDER', 'luozuwu@chinamobile.com', NOW(), NOW(), '1', '1',
   '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('email接收者', 'email接收者', 'SD_EMAIL_RECEIVER', 'luozuwu@chinamobile.com', NOW(), NOW(), '1', '1',
   '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('email登录用户名', 'email登录用户名', 'SD_EMAIL_USERNAME', 'luozuwu', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('email登录用户密码', 'email登录用户密码', 'SD_EMAIL_PASSWORD', 'luozuwu123', NOW(), NOW(), '1', '1', '0',
   NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('email服务器端口', 'email服务器端口', 'SD_EMAIL_PORT', '25', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('是否上传下载到ftp参数', '不使用ftp上传下载文件,ok为不使用', 'SD_NOUSE_FTP', 'ok', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('山东方便测试组测试的定时任务参数', '山东方便测试组测试的定时任务参数,ON为开启,对账频率为5分钟', 'SD_RECONCILE_FOR_TEST', 'ON', NOW(),
        NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('SD_RECONCILE_EMAIL', '山东对账完成是否发邮件的功能,ON为开启', 'SD_RECONCILE_EMAIL', 'NO', NOW(), NOW(), '1', '1',
   '0', NULL);

-- ----------------------------
-- 20161125 增加山东是否判断从云平台登录的配置项
-- ----------------------------
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('山东客户经理和企业关键人是否从云平台登录', '山东客户经理和企业关键人是否从云平台登录，on为开启', 'SD_LOGIN_ON_CLOUDPLATFORM', 'NO', NOW(),
   NOW(), '1', '1', '0', NULL);

-- ----------------------------
-- 20161128 增加山东云平台的充值url
-- ----------------------------
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('山东云平台充值完整URL地址', '山东云平台充值完整URL地址', 'SD_CLOUD_CHARGE_URL',
        'http://211.137.190.207:8089/sdkService/restWs/llpt/updateProductMemberFlowSystemPay',
        NOW(), NOW(), '1', '1', '0', NULL);

-- ------------------------------------
-- 20161201 产品同步信息表 重庆平台 ， by linguangkuo
-- ------------------------------------
DROP TABLE IF EXISTS `ent_sync_list`;
CREATE TABLE `ent_sync_list` (
  `id`                 BIGINT(18) NOT NULL AUTO_INCREMENT,
  `enter_id`           BIGINT(18) NOT NULL
  COMMENT '企业ID',
  `enter_product_code` VARCHAR(128)        DEFAULT NULL
  COMMENT '集团产品编码（订单号）',
  `status`             INT(1)     NOT NULL
  COMMENT '同步状态：1成功 0失败',
  `sync_Info`          VARCHAR(64)         DEFAULT NULL
  COMMENT 'status=1 sync_info="成功"; status=0 sync_info={失败原因}',
  `create_time`        DATETIME   NOT NULL
  COMMENT '创建时间',
  `update_time`        DATETIME            DEFAULT NULL
  COMMENT '更新时间',
  `delete_flag`        INT(1)     NOT NULL DEFAULT '0'
  COMMENT '删除标记， 0:未删除；1：已删除',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

-- ------------------------------------
-- 20161201 集团产品编码账户 ， by linguangkuo
-- ------------------------------------
DROP TABLE IF EXISTS `supplier_product_account`;
CREATE TABLE `supplier_product_account` (
  `id`                  BIGINT(18)    NOT NULL AUTO_INCREMENT,
  `supplier_product_id` BIGINT(18)    NOT NULL
  COMMENT 'BOSS侧定义的产品信息ID',
  `ent_sync_list_id`    BIGINT(18)    NOT NULL,
  `count`               DOUBLE(18, 2) NOT NULL
  COMMENT '产品余额',
  `min_count`           DOUBLE(18, 2) NOT NULL DEFAULT '0.00',
  `create_time`         DATETIME      NOT NULL
  COMMENT '创建时间',
  `update_time`         DATETIME               DEFAULT NULL
  COMMENT '更新时间',
  `delete_flag`         INT(1)        NOT NULL DEFAULT '0'
  COMMENT '删除标记， 0:未删除；1：已删除',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

-- -----------------------------
-- 20161124 四川平台全省运营人员充值报表功能, wujiamin
-- -----------------------------
INSERT INTO `authority`
VALUES
  (NULL, NULL, '充值报表（全省运营人员）', 'ROLE_CHARGE_TABLE_PROVINCE', '105006', '', NOW(), '', '', NOW(),
         '0');

-- ----------------------------
-- 20161130 增加分发队列的消费者个数的配置项 by xujue
-- ----------------------------
INSERT INTO `global_config` VALUES
  (NULL, '分发队列的消费者个数', '分发队列的消费者个数', 'DELIVERQUEUE_CONSUMER_COUNT', '1', NOW(), NOW(), '1', '1',
         '0', NULL);

-- 20161123 企业扩展信息字段
-- ------------------------------------
CREATE TABLE `enterprises_ext_info` (
  `id`          BIGINT(18) NOT NULL AUTO_INCREMENT,
  `enter_id`    BIGINT(18) NOT NULL
  COMMENT '企业ID',
  `ec_code`     VARCHAR(32)         DEFAULT NULL
  COMMENT '集团编码',
  `ec_prd_code` VARCHAR(32)         DEFAULT NULL
  COMMENT '集团产品号码',
  `feature`     VARCHAR(255)        DEFAULT NULL
  COMMENT '企业的扩展信息字段',
  `create_time` DATETIME   NOT NULL
  COMMENT '创建时间',
  `update_time` DATETIME            DEFAULT NULL
  COMMENT '更新时间',
  `delete_flag` INT(1)     NOT NULL DEFAULT '0'
  COMMENT '删除标记， 0:未删除；1：已删除',
  PRIMARY KEY (`id`),
  KEY `idxEnterId` (`enter_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 808
  DEFAULT CHARSET = utf8
  COMMENT = '企业扩展信息';
-- -----------------------------
-- 20161129 产品中增加平台产品是否是流量池转化用产品的标识
-- -----------------------------
ALTER TABLE `product`
ADD COLUMN `flow_account_flag` INT(2) NULL DEFAULT 0
COMMENT '流量池转化用的产品：1-是流量池转化用的产品；2-普通产品'
AFTER `product_size`;

-- -----------------------------
-- 20161202 流量卡激活申请，激活审批，制卡申请，制卡审批权限 by qinqinyan
-- -----------------------------
INSERT INTO `authority` VALUES
  (NULL, NULL, '营销卡激活审核', 'ROLE_MDRC_ACTIVE_APPROVAL', '107007', NULL, '2016-11-28 15:25:05', NULL,
         NULL, '2016-11-28 15:25:32', '0');
INSERT INTO `authority` VALUES
  (NULL, NULL, '营销卡激活申请', 'ROLE_MDRC_ACTIVE_REQUEST', '107008', NULL, '2016-11-29 11:17:39', NULL,
         NULL, '2016-11-29 11:17:42', '0');
INSERT INTO `authority` VALUES
  (NULL, NULL, '营销卡制卡审核', 'ROLE_MDRC_CARDMAKE_APPROVAL', '107009', NULL, '2016-11-30 16:39:07',
         NULL, NULL, '2016-11-30 16:39:10', '0');
INSERT INTO `authority` VALUES
  (NULL, NULL, '营销卡制卡申请', 'ROLE_MDRC_CARDMAKE_REQUEST', '107010', NULL, '2016-12-01 15:09:35', NULL,
         NULL, '2016-12-01 15:09:38', '0');

-- ----------------------------
-- Table structure for `mdrc_cardmake_detail` by qinqinyan
-- ----------------------------
DROP TABLE IF EXISTS `mdrc_cardmake_detail`;
CREATE TABLE `mdrc_cardmake_detail` (
  `id`              BIGINT(18)  NOT NULL AUTO_INCREMENT,
  `request_id`      BIGINT(18)  NOT NULL
  COMMENT '请求id',
  `config_name`     VARCHAR(64) NOT NULL
  COMMENT '卡名称',
  `cardmaker_id`    BIGINT(18)  NOT NULL
  COMMENT '制卡商id',
  `template_id`     BIGINT(18)  NOT NULL
  COMMENT '模板id',
  `amount`          BIGINT(18)  NOT NULL
  COMMENT '生成卡记录条数',
  `start_time`      DATETIME    NOT NULL
  COMMENT '有效期起始时间',
  `end_time`        DATETIME    NOT NULL
  COMMENT '有效期终止时间',
  `delete_flag`     BIGINT(1)   NOT NULL DEFAULT '0',
  `cardmake_status` INT(1)               DEFAULT '0'
  COMMENT '0:未制卡，1：已制卡',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for `mdrc_makecard_request_config` by qinqinyan
-- ----------------------------
DROP TABLE IF EXISTS `mdrc_makecard_request_config`;
CREATE TABLE `mdrc_makecard_request_config` (
  `id`          BIGINT(18) NOT NULL AUTO_INCREMENT,
  `config_id`   BIGINT(18) NOT NULL,
  `request_id`  BIGINT(18) NOT NULL,
  `create_time` DATETIME   NOT NULL,
  `delete_flag` INT(11)    NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for `mdrc_active_detail` by qinqinyan
-- ----------------------------
DROP TABLE IF EXISTS `mdrc_active_detail`;
CREATE TABLE `mdrc_active_detail` (
  `id`                BIGINT(18) NOT NULL AUTO_INCREMENT,
  `request_id`        BIGINT(18)          DEFAULT NULL
  COMMENT '请求id',
  `config_id`         BIGINT(18)          DEFAULT NULL,
  `start_card_number` VARCHAR(27)         DEFAULT NULL,
  `end_card_number`   VARCHAR(27)         DEFAULT NULL,
  `count`             INT(10)    NOT NULL
  COMMENT '激活数量',
  `image`             VARCHAR(64)         DEFAULT NULL
  COMMENT '企业签收图片名称',
  `image_key`         VARCHAR(64)         DEFAULT NULL
  COMMENT '企业签收图片key',
  `delete_flag`       INT(1)              DEFAULT '0',
  `ent_id`            BIGINT(18)          DEFAULT NULL
  COMMENT '企业id',
  `template_id`       BIGINT(18)          DEFAULT NULL
  COMMENT '营销卡模板id',
  `product_id`        BIGINT(18)          DEFAULT NULL
  COMMENT '产品id',
  `active_status`     INT(1)              DEFAULT '0'
  COMMENT '0，未激活；1，已激活（即已经处理该请求）',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for `mdrc_active_request_config` by qinqinyan
-- ----------------------------
DROP TABLE IF EXISTS `mdrc_active_request_config`;
CREATE TABLE `mdrc_active_request_config` (
  `id`           BIGINT(18)   NOT NULL AUTO_INCREMENT,
  `request_id`   BIGINT(18)   NOT NULL
  COMMENT '请求激活审批id',
  `config_id`    BIGINT(18)   NOT NULL
  COMMENT '营销卡批次id',
  `start_serial` VARCHAR(200) NOT NULL
  COMMENT '起始序列号',
  `end_serial`   VARCHAR(200) NOT NULL
  COMMENT '终止序列号',
  `create_time`  DATETIME     NOT NULL,
  `delete_flag`  INT(1)       NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

-- -----------------------
-- 20161205 增加流量池转换产品关联的流量池产品ID
-- -----------------------
ALTER TABLE `product`
ADD COLUMN `flow_account_product_id` BIGINT(18) NULL
COMMENT '流量池产品的id（该流量池转换产品关联的是哪个流量池）'
AFTER `flow_account_flag`;

-- -----------------------
-- 20161205 增加状态码 by qinqinyan
-- -----------------------
ALTER TABLE activity_win_record ADD COLUMN status_code VARCHAR(50) DEFAULT NULL
COMMENT '状态码';

-- -----------------------
-- 20161206 增加新疆boss的地址 by qihang
-- -----------------------
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('BOSS_XJ_URL', '新疆boss地址', 'BOSS_XJ_URL',
        'http://10.238.159.118:42000/uip_inws/services/UIPSOAP', NOW(), NOW(), '1', '1', '0', NULL);

-- -----------------------
-- 20161206 重庆平台
-- -----------------------
INSERT INTO `authority` VALUES
  (NULL, NULL, '产品同步', 'ROLE_PRODUCT_SYNC', '107011', NULL, '2016-04-20 08:53:17', NULL, NULL,
         '2016-04-20 08:53:20', '0');
INSERT INTO `authority` VALUES
  (NULL, NULL, '产品余额', 'ROLE_PRODUCT_REMAIN', '107012', NULL, '2016-04-20 08:53:17', NULL, NULL,
         '2016-04-20 08:53:20', '0');
INSERT INTO `authority` VALUES
  (NULL, NULL, '订购查询', 'ROLE_ORDER_QUERY', '107013', NULL, '2016-04-20 08:53:17', NULL, NULL,
         '2016-04-20 08:53:20', '0');

-- ------------------
-- 20161206 增加平台序列号唯一索引，【部分平台已增加了索引，执行可能报错】
-- ------------------
ALTER TABLE `serial_num`
ADD UNIQUE INDEX `uniq_idx_platform_serial_num` (`platform_serial_num`) USING BTREE;

-- 20161206 增加流量池话单路径
-- -----------------------
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('山东对账存放账单路径', '山东对账存放账单路径', 'SD_RECONCILE_FLOWBILL_PATH',
        '/usr/share/tomcat/shandongfile/shandongFlow/billCell', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('山东对账存放话单路径', '山东对账存放话单路径', 'SD_RECONCILE_FLOWHUADAN_PATH',
        '/usr/share/tomcat/shandongfile/shandongFlow/huadanCell', NOW(), NOW(), '1', '1', '0',
        NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('山东对账存放差异记录路径', '山东对账存放差异记录路径', 'SD_RECONCILE_FLOWCHANGERECORD_PATH',
        '/usr/share/tomcat/shandongfile/shandongFlow/changeRecordsCell', NOW(), NOW(), '1', '1',
        '0', NULL);

-- -----------------------
-- 20161207 增加根据手机号统计流量池使用量的表
-- -----------------------
CREATE TABLE `pool_used_statistic` (
  `id`           BIGINT(18)  NOT NULL AUTO_INCREMENT,
  `date`         VARCHAR(10) NOT NULL
  COMMENT '日期，比如20161206',
  `mobile`       VARCHAR(12) NOT NULL
  COMMENT '手机号',
  `enter_code`   VARCHAR(20) NOT NULL
  COMMENT '企业编码，山东是userId',
  `product_code` VARCHAR(20) NOT NULL
  COMMENT '使用的流量池编码',
  `used_amount`  BIGINT(18)  NOT NULL DEFAULT '0'
  COMMENT '使用的总流量',
  PRIMARY KEY (`id`),
  KEY `search_index` (`mobile`, `enter_code`, `product_code`) USING BTREE,
  KEY `date_index` (`date`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------------------
-- 默认产品表中的flow_account_flag为2(普通产品) DATE :2016/12/13 BY llilin
-- --------------------------------------------------------------------
ALTER TABLE `product`
MODIFY COLUMN `flow_account_flag` INT(2) NULL DEFAULT 2
COMMENT '流量池转化用的产品：1-是流量池转化用的产品；2-普通产品'
AFTER `product_size`;

-- --------------------------------------------------------------
-- 产品表中的增加标识产品是否可以配置数量 DATE :2016/12/13 BY llilin
-- --------------------------------------------------------------
ALTER TABLE `product`
ADD COLUMN `configurable_num_flag` INT(1) NOT NULL DEFAULT 1
COMMENT '充值业务中产品可以配置数量标志位，0:可配置数量，1:不可配置数量'
AFTER `flow_account_product_id`;

-- -----------------------
-- 20161209 增加流量池话单路径
-- -----------------------
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('山东流量池boss对账存放账单路径', '山东流量池boss对账存放账单路径', 'SD_RECONCILE_FLOWBOSSBILL_PATH',
        '/usr/share/tomcat/shandongfile/shandongFlow/billCellBoss', NOW(), NOW(), '1', '1', '0',
        NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('山东流量池boss对账存放话单路径', '山东流量池boss对账存放话单路径', 'SD_RECONCILE_FLOWBOSSHUADAN_PATH',
        '/usr/share/tomcat/shandongfile/shandongFlow/huadanCellBoss', NOW(), NOW(), '1', '1', '0',
        NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('山东流量池boss对账存放差异记录路径', '山东流量池boss对账存放差异记录路径', 'SD_RECONCILE_FLOWBOSSCHANGERECORD_PATH',
        '/usr/share/tomcat/shandongfile/shandongFlow/changeRecordsCellBoss', NOW(), NOW(), '1', '1',
        '0', NULL);

-- -----------------------
-- 20161209 增加山东dynamictoken_utl地址
-- -----------------------
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('山东dynamictoken_utl地址', '山东dynamictoken_utl地址', 'SD_DYNAMICTOKEN_UTL',
        'http://211.137.190.207:8089/sdkService/restWs/commonService/dynamicToken', NOW(), NOW(),
        '1', '1', '0', NULL);

-- -----------------------
-- 20161210 增加全局配置项，向上渠道相关参数
-- -----------------------
INSERT INTO `global_config` (
  `id`,
  `name`,
  `description`,
  `config_key`,
  `config_value`,
  `create_time`,
  `update_time`,
  `creator_id`,
  `updater_id`,
  `delete_flag`,
  `config_update`
)
VALUES
  (
    NULL,
    '平台侧往向上公司请求时的加密私钥',
    '平台侧往向上公司请求时的加密私钥',
    'BOSS_XS_PRIVATE_KEY',
    'private_key',
    '2016-12-10 20:31:31',
    '2016-12-10 20:31:31',
    '1',
    '1',
    '0',
    NULL
  );

INSERT INTO `global_config` (
  `id`,
  `name`,
  `description`,
  `config_key`,
  `config_value`,
  `create_time`,
  `update_time`,
  `creator_id`,
  `updater_id`,
  `delete_flag`,
  `config_update`
)
VALUES
  (
    NULL,
    '平台侧接收向上公司响应时的解密公钥',
    '平台侧接收向上公司响应时的解密公钥',
    'BOSS_XS_PUBLIC_KEY',
    'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDG78YogpvISlW/mvP0cIVBbrVu1OuhRuyaMGgo00CZmn2556T0n0rmNMBFMdah//lfYvlRxZQk1x6luoP1w7p8P+V9aIvVJ6eaBflzRTVkODB+TI9nt4fL5WsHS6gaLc73lIpvbCywYNfyltKyTSOBHzT3WUoWPblFHTFciJE76wIDAQAB',
    '2016-12-10 20:31:31',
    '2016-12-10 20:31:31',
    '1',
    '1',
    '0',
    NULL
  );

INSERT INTO `global_config` (
  `id`,
  `name`,
  `description`,
  `config_key`,
  `config_value`,
  `create_time`,
  `update_time`,
  `creator_id`,
  `updater_id`,
  `delete_flag`,
  `config_update`
)
VALUES
  (
    NULL,
    '向上渠道充值地址',
    '向上渠道充值地址',
    'BOSS_XS_CHARGE_URL',
    'http://shop.test.bolext.cn:81/shop/buyunit',
    '2016-12-10 20:31:31',
    '2016-12-10 20:31:31',
    '1',
    '1',
    '0',
    NULL
  );

-- -----------------------
-- 20161213 营销卡必须的配置项
-- -----------------------
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES (NULL, '新建营销卡数量的最小值', '新建营销卡数量的最小值', 'MDRC_MINIMUN_NUM', '2345', '2016-10-26 15:13:40',
              '2016-10-26 15:13:41', '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES (NULL, '新建营销卡数量的最大值', '新建营销卡数量的最大值', 'MDRC_MAXIMUM_NUM', '99999', '2016-10-26 15:14:19',
              '2016-10-26 15:14:21', '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES (NULL, '营销卡有效期配置最大天数', '营销卡有效期配置最大天数', 'MDRC_VALID_MAX_DAYS', '100', '2016-10-26 17:06:25',
              '2016-10-26 17:06:27', '1', '1', '0', NULL);

-- -----------------------
-- 20161214 陕西BOSS渠道充值地址
-- -----------------------
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES (NULL, '陕西BOSS渠道充值地址', '陕西BOSS渠道充值地址', 'BOSS_SHANXI_CHARGE_URL',
              'http://www.sn.10086.cn/test_uip_sx', '2016-10-26 17:06:25', '2016-10-26 17:06:27',
              '1', '1', '0', NULL);

-- ----------------------
-- 20161222 每条充值记录中增加财务状态， wujiamin
-- ----------------------
ALTER TABLE `charge_record`
ADD COLUMN `update_charge_time` DATETIME NULL
COMMENT '充值结果更新时间'
AFTER `supplier_product_id`,
ADD COLUMN `finance_status` INT(2) NULL DEFAULT 0
COMMENT '财务状态：FinanceStatus'
AFTER `update_charge_time`,
ADD COLUMN `change_account_status` INT(2) NULL DEFAULT 0
COMMENT '账户状态：0-可以被调账；1-调账中；2-已结账'
AFTER `finance_status`;

-- -----------------
-- 20161227 企业账单权限，wujiamin
-- -----------------
INSERT INTO `authority`
VALUES
  (NULL, NULL, '企业账单(客户经理)', 'ROLE_ENTERPRISE_BILL_KHJL', '103019', '', NOW(), '', '', NOW(), '0');

-- ------------------
-- 20161230 增加boss充值时间，wujiamin
-- ------------------
ALTER TABLE `charge_record`
ADD COLUMN `boss_charge_time` DATETIME NULL
COMMENT '向boss发起充值的时间'
AFTER `supplier_product_id`;

-- ----------------------------
-- Table structure for `activity_payment_info` by qinqinyan on 2017/1/6 广东流量众筹支付记录表
-- ----------------------------
DROP TABLE IF EXISTS `activity_payment_info`;
CREATE TABLE `activity_payment_info` (
  `id`                   BIGINT(18)   NOT NULL AUTO_INCREMENT,
  `win_record_id`        VARCHAR(225) NOT NULL
  COMMENT '中奖记录uuid（record_id），即平台侧提供的订单编号',
  `sys_serial_num`       VARCHAR(225) NOT NULL
  COMMENT '系统支付流水号（平台侧,满足支付平台要求）',
  `charge_type`          VARCHAR(200)          DEFAULT NULL
  COMMENT '支付方式，记录用户支付渠道，允许为空',
  `charge_time`          DATETIME              DEFAULT NULL
  COMMENT '支付时间，允许为空',
  `charge_update_time`   DATETIME              DEFAULT NULL
  COMMENT '返回支付结果时间，允许为空',
  `refund_time`          DATETIME              DEFAULT NULL
  COMMENT '如果发生退款，返回退款结果时间，允许为空',
  `status`               INT(1)                DEFAULT NULL
  COMMENT '支付状态。默认0：表示未支付；1：支付中；2：支付成功； 3：支付失败， 4，退款成功 5：退款失败：6未知异常',
  `return_serial_num`    VARCHAR(225)          DEFAULT NULL
  COMMENT ' 记录第三方返回的支付流水号，允许为空（对于广东流量众筹平台，就是ADC订单流水号）',
  `return_pay_num`       VARCHAR(225)          DEFAULT NULL
  COMMENT '第三方返回的支付单号，用于订购流量，允许为空',
  `return_pay_status`    INT(2)                DEFAULT NULL
  COMMENT 'Category等于1或2时为消息关联的支付单状态，支付单状态定义如下：（1）1--等待付款；（2）2--成功；（3）3--已取消；（4）4--超时；（5）5--失败；（6）6--部分退款',
  `return_pay_time`      DATETIME              DEFAULT NULL
  COMMENT '第三方返回的订单时间，允许为空',
  `return_pay_amount`    BIGINT(18)            DEFAULT NULL
  COMMENT '第三方返回的支付金额，允许为空',
  `return_category`      INT(5)                DEFAULT NULL
  COMMENT '第三方返回的退款状态，允许为空（对于流量众筹平台：消息类型（1）1--支付完成通知；（2）2--冲正完成通知（退款）；（3）3--严密完成通知；（4）101--文件就绪通知；（5）102--支付机构划扣支持银行列表文件就绪通知；（6）200--退款确认通知',
  `return_refund_amount` BIGINT(18)            DEFAULT NULL
  COMMENT '第三方返回的退款金额，允许为空',
  `error_message`        VARCHAR(500)          DEFAULT NULL
  COMMENT '记录第三方返回的支付结果信息message，允许为空（预留字段）',
  `delete_flag`          INT(1)                DEFAULT '0'
  COMMENT '默认0,1是逻辑删除',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for `crowdfunding_activity_detail` by qinqinyan on 2017/1/6  广东流量众筹活动详情
-- ----------------------------
DROP TABLE IF EXISTS `crowdfunding_activity_detail`;
CREATE TABLE `crowdfunding_activity_detail` (
  `id`                 BIGINT(18)   NOT NULL AUTO_INCREMENT,
  `activity_id`        VARCHAR(225) NOT NULL
  COMMENT '活动uuid，活动唯一标识',
  `current_count`      BIGINT(18)   NOT NULL DEFAULT '0'
  COMMENT '当前参加众筹人数',
  `target_count`       BIGINT(18)   NOT NULL
  COMMENT '目标众筹人数',
  `rules`              VARCHAR(602) NOT NULL
  COMMENT '活动规则',
  `result`             INT(1)       NOT NULL
  COMMENT '众筹活动结果，默认0，众筹中；1：众筹成功；2失败',
  `banner`             VARCHAR(225) NOT NULL
  COMMENT '活动banner，存图片名称',
  `logo`               VARCHAR(225) NOT NULL
  COMMENT '企业logo，存图片名称',
  `charge_type`        INT(1)       NOT NULL DEFAULT '0'
  COMMENT '充值类型，0默认立即生效，1，次月生效',
  `appendix`           VARCHAR(225)          DEFAULT NULL
  COMMENT '审批附件，存附件名称',
  `has_white_or_black` INT(1)       NOT NULL
  COMMENT '是否有黑白名单。0无，1白名单，2黑名单',
  `join_type`          INT(2)       NOT NULL
  COMMENT '报名方式；1：企业报名；2：用户报名',
  `url`                VARCHAR(600)          DEFAULT NULL
  COMMENT '活动url',
  `delete_flag`        INT(1)       NOT NULL DEFAULT '0'
  COMMENT '默认0，1逻辑删除',
  `version`            BIGINT(18)   NOT NULL
  COMMENT '乐观锁',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- add column 'discount' in 'activity_prize' by qinqinyan on 2017/1/6  广东流量众筹增加折扣字段
-- ----------------------------
ALTER TABLE `activity_prize`
ADD COLUMN `discount` INT(5) DEFAULT NULL DEFAULT -1
COMMENT '设置默认值为-1，即对于其他活动不需要该字段；存储0到100的正整数，100即为无折扣'
AFTER `type`;

-- ----------------------------
-- add column in 'activity_win_record' by qinqinyan on 2017/1/6  广东流量众筹增加字段
-- ----------------------------
ALTER TABLE `activity_win_record`
ADD COLUMN `pay_result` INT(5) DEFAULT NULL DEFAULT -1
COMMENT '支付结果，默认为-1，表示该活动不需要支付，0：未支付；1：已发送支付请求（支付中）；2：支付成功； 3：支付失败；4，退款成功 5：退款失败：6未知异常'
AFTER `status_code`;

ALTER TABLE `activity_win_record`
ADD COLUMN `wx_openid` VARCHAR(500) DEFAULT NULL
COMMENT '微信openId,允许为空'
AFTER `pay_result`;

-- -----------------------
-- 20161220 产品禁止转换表 by:qihang
-- -----------------------
CREATE TABLE `product_converter` (
  `id`            BIGINT(18) NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  `source_prd_id` BIGINT(18) NOT NULL
  COMMENT '禁止转换的源产品的Id',
  `dest_prd_id`   BIGINT(18) NOT NULL
  COMMENT '禁止转换的目标转换产品的Id',
  `create_time`   DATETIME   NOT NULL
  COMMENT '创建时间',
  `update_time`   DATETIME   NOT NULL
  COMMENT '修改时间',
  `delete_flag`   INT(2)     NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `source_index` (`source_prd_id`) USING BTREE,
  INDEX `dest_index` (`dest_prd_id`) USING BTREE,
  INDEX `source_dest_index` (`source_prd_id`, `dest_prd_id`, `delete_flag`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

INSERT INTO `authority` VALUES
  (NULL, NULL, '产品转换关系管理', 'ROLE_PRODUCT_CONVERTER', '109005', NULL, NOW(), NULL, NULL,
         NOW(), '0');

-- -----------------------
-- 20161223 增加转化类型  by:qihang
-- -----------------------
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('产品转化类型设置', 'black为黑名单，white为白名单，其它为不启用关系,默认为不启用', 'PRODUCT_CONVERTER_TYPE', 'DEFAULT', NOW(),
   NOW(), '1', '1', '0', NULL);

-- -----------------------
-- 20161222 虚拟产品单笔充值最大值限制
-- -----------------------
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES (NULL, '花费赠送单笔最大值(分)', '花费赠送单笔最大值(分)', 'MAX_SIZE_VIRTRUAL_MOBILE_FEE', '50000',
              '2016-12-22 17:06:27', '2016-12-22 17:06:27', '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES (NULL, '流量池单笔赠送最大值(M)', '流量池单笔赠送最大值(M)', 'MAX_SIZE_VIRTRUAL_FLOW_PRODUCT', '1024',
              '2016-12-22 17:06:27', '2016-12-22 17:06:27', '1', '1', '0', NULL);
-- -----------------------
-- 20161223 敏感词库 linguangkuo
-- -----------------------
CREATE TABLE `sensitive_words` (
  `id`          BIGINT(18)  NOT NULL AUTO_INCREMENT,
  `name`        VARCHAR(64) NOT NULL
  COMMENT '敏感词',
  `creator_id`  BIGINT(18)  NOT NULL,
  `create_time` DATETIME    NOT NULL,
  `update_time` DATETIME    NOT NULL,
  `delete_flag` INT(1)      NOT NULL DEFAULT '0'
  COMMENT '删除Flag，0:未删除；1：已删除',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 17
  DEFAULT CHARSET = utf8;

INSERT INTO `authority` VALUES
  (NULL, NULL, '敏感词词库', 'ROLE_SENSITIVE_WORDS', '104010', NULL, '2016-04-20 08:53:17', NULL, NULL,
         '2016-04-20 08:53:20', '0');

-- -----------------------
-- 20170104 authorrity产品关联改名  by:qihang
-- -----------------------          
UPDATE authority
SET `NAME` = "产品关联"
WHERE NAME = "产品转换关系管理";

-- -----------------------
-- 20170105 authorrity产品关联改名  by:qihang
-- -----------------------          
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('山东充值接口异步设置', 'on为启用，其余为不启用，即同步', 'SD_CHARGE_OPEN_ASYC', 'OFF', NOW(), NOW(), '1', '1', '0',
        NULL);

-- -----------------------
-- 20170123 陕西boss参数  by:linguangkuo
-- -----------------------          
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('陕西BOSS工号', '陕西BOSS工号', 'BOSS_SHANXI_TRADESTAFFID', 'ITFBWJW1', NOW(), NOW(), '1', '1', '0',
        NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('陕西 BOSS接入方式', '陕西 BOSS接入方式', 'BOSS_SHANXI_INMODECODE', 'g', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('陕西营销活动BOSS工号', '陕西营销活动BOSS工号', 'BOSS_SHANXI_ACTIVITY_TRADESTAFFID', 'ITFBHSH1', NOW(), NOW(),
   '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('陕西 营销活动BOSS接入方式', '陕西 营销活动BOSS接入方式', 'BOSS_SHANXI_ACTIVITY_INMODECODE', 'Y', NOW(), NOW(), '1',
   '1', '0', NULL);

-- -----------------------
-- 20170208 天津boss参数  by:linguangkuo
-- -----------------------          
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('天津接口URL地址', '天津接口URL地址', 'TIANJIN_BOSS_URL', 'http://211.103.90.123:80/oppf', NOW(), NOW(), '1',
   '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('天津接口APPID', '天津接口APPID', 'TIANJIN_BOSS_APPID', '505386', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('天津接口STATUS', '天津接口STATUS,0:沙箱环境  1:为正式环境', 'TIANJIN_BOSS_STATUS', '1', NOW(), NOW(), '1', '1',
   '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('天津接口APPKEY', '天津接口APPKEY', 'TIANJIN_BOSS_APPKEY', 'd82ab7f18cf511977a42551e4f784ac4', NOW(),
   NOW(), '1', '1', '0', NULL);

INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES ('天津接口公钥', '天津接口公钥', 'TIANJIN_BOSS_PUBLICKEY',
        'MGcwDQYJKoZIhvcNAQEBBQADVgAwUwJMAMRdM2AsG5vcvr+miFJEscDecDBKGYlXDL2RWDe6hGVTXjdKM73gv+hv2Enj3N6vUEFzrrSI6Tv97k4nfuNMd8YXq70IF5Tg899rnwIDAQAB',
        NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('天津接口工号部门编码', '天津接口工号部门编码', 'TIANJIN_BOSS_TRADEDEPARTID', '30824', NOW(), NOW(), '1', '1', '0',
   NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('天津接口交易工号编码', '天津接口交易工号编码', 'TIANJIN_BOSS_TRADESTAFFID', 'KVZY0001', NOW(), NOW(), '1', '1', '0',
   NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('天津接口部门编码', '天津接口部门编码', 'TIANJIN_BOSS_DEPARTID', '30824', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  ('天津接口接入工号密码', '天津接口接入工号密码', 'TIANJIN_BOSS_TRADEDEPARTPASSWD', 'lc1234', NOW(), NOW(), '1', '1',
   '0', NULL);

-- -----------------------
-- 20170212 开放平台短信通道参数  by:sunyiwei
-- -----------------------
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (NULL, '开放平台短信通道请求地址', '开放平台短信通道请求地址', 'OPT_MSG_REQ_ADDR',
         'http://www.openservice.com.cn:8001/open/sms/send', '2017-02-13 16:29:14',
         '2017-02-13 16:29:14', 1, 1, 0, NULL),
  (NULL, '开放平台短信通道请求appkey', '开放平台短信通道请求appkey', 'OPT_MSG_APP_KEY',
         '23c041f978224fbcaa45b21b6a74c960', '2017-02-13 16:29:14', '2017-02-13 16:29:14', 1, 1, 0,
         NULL),
  (NULL, '开放平台短信通道请求appsecret', '开放平台短信通道请求appsecret', 'OPT_MSG_SECRET_KEY', 'AAAAAAAA',
         '2017-02-13 16:29:14', '2017-02-13 16:29:14', 1, 1, 0, NULL),
  (NULL, '开放平台短信通道查询地址', '开放平台短信通道查询地址', 'OPT_MSG_QUERY_ADDR',
         'http://www.openservice.com.cn:8001/open/sms/deliveryStatus', '2017-02-13 16:29:14',
         '2017-02-13 16:29:14', 1, 1, 0, NULL);

-- -----------------------
-- 20170213 增加单个赠送频率限制, wujiamin
-- ----------------------- 
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (NULL, '单个赠送的频率限制', '单个赠送的频率限制，ON为开启，其它为关闭', 'PRESENT_SINGLE_RATE_LIMIT', 'OFF', NOW(), NOW(),
         '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (NULL, '单个赠送的频率限制的时间范围', '单个赠送的频率限制的时间范围，即以多长时间为限制', 'PRESENT_SINGLE_RATE_LIMIT_RANGE', '180',
         NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (NULL, '单个赠送的频率限制的次数', '单个赠送的频率限制的次数', 'PRESENT_SINGLE_RATE_LIMIT_COUNT', '2', NOW(), NOW(), '1',
         '1', '0', NULL);
-- -------------------------------
-- 20170112 增加表url_map BY leelyn
-- -------------------------------
CREATE TABLE `url_map` (
  `id`          BIGINT(18)   NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  `uuid`        VARCHAR(64)  NOT NULL
  COMMENT '唯一标识中奖URL的ID',
  `real_url`    VARCHAR(255) NOT NULL
  COMMENT '真实的中奖URL',
  `type`        INT(8)       NOT NULL
  COMMENT '营销活动类型 0：流量券',
  `create_time` DATETIME     NOT NULL
  COMMENT '创建时间',
  `update_time` DATETIME     NULL
  COMMENT '更新时间',
  PRIMARY KEY (`id`)
);

-- -------------------------------
-- 20170217 增加企业开户字段的默认信息
-- -------------------------------
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (NULL, '企业开户所需要的字段', '企业开户所需要的字段', 'ENT_PROPS_INFO',
         '{\"name\":{\"name\":\"企业名称\",\"description\":\"\"},\"cooperationTime\":{\"name\":\"合作时间\",\"description\":\"\"},\"code\":{\"name\":\"企业编码\",\"description\":\"客户集团编码(280)\"},\"enterpriseManagerName\":{\"name\":\"企业管理员姓名\",\"description\":\"\"},\"enterpriseManagerPhone\":{\"name\":\"企业管理员手机号码\",\"description\":\"\"},\"customerManagerPhone\":{\"name\":\"客户经理手机号码\",\"description\":\"\"},\"customerManagerName\":{\"name\":\"客户经理姓名\",\"description\":\"\"},\"district\":{\"name\":\"地区\",\"description\":\"\"}}',
         '2017-02-16 11:26:25', '2017-02-16 11:26:25', 1, 1, 0, NULL);

-- --20170207 增加activity_payment_info索引
-- -----------------------
ALTER TABLE `activity_payment_info`
ADD UNIQUE INDEX `idx_sys_serial_num` (`sys_serial_num`) USING BTREE;
-- -----------------------
-- --20170207 增加支付金额记录
-- -----------------------
ALTER TABLE `activity_payment_info`
ADD COLUMN `pay_amount` BIGINT NULL
COMMENT '支付金额（分为单位）'
AFTER `charge_update_time`;

-- -----------------------
-- --20170217 add data by qinqinyan
-- -----------------------
INSERT INTO `global_config` VALUES
  (NULL, '广东众筹文件存储路径', '广东众筹文件存储路径', 'CROWD_FUNDING_PATH', '/srv/appdata/data/lottery/crowdfunding',
         '2017-01-22 13:43:45', '2017-01-22 13:46:35', '1', '1', '0', NULL);
INSERT INTO `authority` VALUES
  (NULL, NULL, '流量众筹', 'ROLE_FLOW_CROWD_FUNDING', '104010', NULL, '2017-01-12 09:40:24', NULL, NULL,
         '2017-01-12 09:40:29', '0');

-- -----------------------
-- --20170219 add data by qinqinyan
-- -----------------------
INSERT INTO `global_config` VALUES
  (NULL, '广东众筹领取页面URL', '广东众筹领取页面URL', 'CROWD_FUNDING_GETFLOW_PAGE',
         'https://gdzc.4ggogo.com/web-in/manage/crowdFunding/getFlow.html', '2017-02-19 01:25:34',
         '2017-02-19 01:25:34', '1', '1', '0', NULL);
INSERT INTO `global_config` VALUES
  (NULL, '广东众筹微信公众号appID', '广东众筹微信公众号appID', 'CROWD_FUNDING_APP_ID', 'wx68bf2f552fc8e2fb',
         '2017-02-19 01:29:09', '2017-02-19 01:38:02', '1', '1', '0', NULL);
INSERT INTO `global_config` VALUES (NULL, '广东众筹鉴权URL', '广东众筹鉴权URL', 'CROWD_FUNDING_AUTH_URL',
                                          'https://lljyz.4ggogo.com/wechat/user/auth.html',
                                          '2017-02-20 01:29:57', '2017-02-19 01:29:57', '1', '1',
                                          '0', NULL);
INSERT INTO `global_config` VALUES
  (NULL, '广东众筹引导关注公众号URL', '广东众筹引导关注公众号URL', 'CROWD_FUNDING_FOCUS_URL',
         'https://lljyz.4ggogo.com/wechat/welcome/focus.html', '2017-02-19 01:30:49',
         '2017-02-19 01:30:49', '1', '1', '0', NULL);
INSERT INTO `global_config` VALUES
  (NULL, '广东众筹公众号绑定手机号URL', '广东众筹公众号绑定手机号URL', 'CROWD_FUNDING_BINDED_URL',
         'https://lljyz.4ggogo.com/wechat/welcome/bind.html', '2017-02-19 01:31:18',
         '2017-02-19 01:31:18', '1', '1', '0', NULL);

-- -----------------------
-- --20170222 weixin config, wujiamin
-- -----------------------
INSERT INTO `global_config` VALUES (null, '平台域名', '平台域名', 'PLATFORM_URL', 'http://localhost:8080', NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '微信公众号获取code接口', '微信公众号获取code接口', 'WEIXIN_CODE_URL', 'http://lljyz.4ggogo.com/wechat/async/getCode.html?platformUrl={0}', NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '微信公众号获取用户类型及跳转链接接口', '微信公众号获取用户类型及跳转链接接口', 'WEIXIN_USERTYPE_URL', 'http://lljyz.4ggogo.com/wechat/async/getUserType.html?code={0}&state={1}', NOW(), NOW(), '1', '1', '0', null);

-- -----------------------
-- --20170224 weixin url, wujiamin
-- -----------------------
INSERT INTO `global_config` VALUES (null, '微信公众号获取邀请二维码接口', '微信公众号获取邀请二维码接口', 'WEIXIN_INVITE_QRCODE_URL', 'http://lljyz.4ggogo.com/wechat/async/getQrcode.html', NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '微信公众号获取用户信息接口', '微信公众号获取用户信息接口', 'WEIXIN_USERINFO_URL', 'http://lljyz.4ggogo.com/wechat/async/getWxUserInfo/{0}.html', NOW(), NOW(), '1', '1', '0', null);

-- ----------------------
-- --20170301 众筹微信公众号，配置
-- ----------------------
INSERT INTO `global_config` VALUES (null, '每日签到最大可获取的积分', '每日签到最大可获取的积分', 'MAX_POINT_PER_DAY', '5', NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '邀请好友可获得的积分', '邀请好友可获得的积分', 'INVITE_POINT', '10', NOW(), NOW(), '1', '1', '0', null);

-- -----------------------
-- --20170222 增加企业开户的权限项, sunyiwei
-- -----------------------
INSERT INTO `authority` (`AUTHORITY_ID`, `PARENT_ID`, `NAME`, `AUTHORITY_NAME`, `CODE`, `AUTHORITY_URL`, `CREATE_TIME`, `CREATOR`, `UPDATE_USER`, `UPDATE_TIME`, `DELETE_FLAG`)
VALUES
  (NULL, NULL, '企业开户字段', 'ROLE_ENT_PROPS_INFO', '109010', NULL, '2017-02-20 11:07:35', NULL, NULL,
         '2017-02-20 11:07:35', 0);

-- -----------------------
-- 20170223 增加字段 by qinqinyan
-- -----------------------
ALTER TABLE crowdfunding_activity_detail ADD COLUMN banner_key VARCHAR(255) DEFAULT NULL
COMMENT 'banner存储key值';
ALTER TABLE crowdfunding_activity_detail ADD COLUMN logo_key VARCHAR(255) DEFAULT NULL
COMMENT 'logo存储key值';
ALTER TABLE crowdfunding_activity_detail ADD COLUMN appendix_key VARCHAR(255) DEFAULT NULL
COMMENT 'appendix存储key值';

-- -----------------------
-- 20170228 增加内蒙古流量平台参数 by linguangkuo
-- -----------------------
INSERT INTO `global_config` VALUES
  (NULL, '内蒙古BOSS的EC接口appkey', '内蒙古BOSS的EC接口appkey', 'BOSS_NM_EC_APPKEY',
         '7c615e762d2446038afd415acd52639d', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` VALUES
  (NULL, '内蒙古BOSS的EC接口appsecret', '内蒙古BOSS的EC接口appsecret', 'BOSS_NM_EC_APPSECRET',
         '0e58cdb3fc15455e8f85455772b788aa', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` VALUES
  (NULL, '内蒙古BOSS的EC接口CHARGE_URL', '内蒙古BOSS的EC接口CHARGE_URL', 'BOSS_NM_EC_CHARGE_URL',
         'http://www.nm.10086.cn/flowplat/boss/charge.html', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` VALUES
  (NULL, '内蒙古BOSS的EC接口充值结果查询QUERY_URL', '内蒙古BOSS的EC接口充值结果查询QUERY_URL', 'BOSS_NM_EC_QUERY_URL',
         'http://www.nm.10086.cn/flowplat/chargeRecords', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` VALUES
  (NULL, '内蒙古BOSS的EC接口AUTH_URL', '内蒙古BOSS的EC接口AUTH_URL', 'BOSS_NM_EC_AUTH_URL',
         'http://www.nm.10086.cn/flowplat/auth.html', NOW(), NOW(), '1', '1', '0', NULL);

-- -----------------------
-- 20170301 权限用于控制企业是有有扩展字段，目前用于广东流量卡和众筹   by qinqinyan
-- -----------------------
INSERT INTO `global_config` VALUES

  (NULL, '是否需要扩展的企业信息字段', '是否需要扩展的企业信息字段, 目前广东众筹和流量卡平台使用, YES为需要，其它为不需要', 'EXT_ENTERPRISE_INFO', 'NO',
         '2016-11-23 11:06:40', '2016-11-23 11:06:41', '1', '1', '0', NULL);

-- -----------------------
-- 20170302 微信table, wujiamin
-- -----------------------
DROP TABLE IF EXISTS `wx_grade`;
CREATE TABLE `wx_grade` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '等级的名称',
  `grade` int(11) NOT NULL COMMENT '等级的数值，从1开始，每一级+1',
  `points` bigint(18) NOT NULL COMMENT '达到该等级所需的累积积分',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
INSERT INTO `wx_grade` (`id`, `name`, `grade`, `points`) VALUES ('1', '普通会员', '1', '0');
INSERT INTO `wx_grade` (`id`, `name`, `grade`, `points`) VALUES ('2', '青铜会员', '2', '200');
INSERT INTO `wx_grade` (`id`, `name`, `grade`, `points`) VALUES ('3', '白银会员', '3', '800');
INSERT INTO `wx_grade` (`id`, `name`, `grade`, `points`) VALUES ('4', '黄金会员', '4', '2000');

DROP TABLE IF EXISTS `wx_invite_qrcode`;
CREATE TABLE `wx_invite_qrcode` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `admin_id` bigint(18) NOT NULL,
  `openid` varchar(255) NOT NULL,
  `ticket` varchar(255) NOT NULL,
  `url` varchar(500) NOT NULL COMMENT '二维码对应的url',
  `expire_seconds` int(11) NOT NULL COMMENT 'ticket过期时间（秒）',
  `expire_time` datetime NOT NULL COMMENT 'ticket过期时间（具体时间）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` int(2) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_admin_id` (`admin_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `wx_invite_record`;
CREATE TABLE `wx_invite_record` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `invite_serial` varchar(100) NOT NULL COMMENT '邀请序列号(平台侧生成）',
  `invite_admin_id` bigint(18) NOT NULL COMMENT '邀请者的adminId',
  `invited_admin_id` bigint(18) NOT NULL COMMENT '被邀请者adminId',
  `invited_openid` varchar(100) NOT NULL COMMENT '被邀请者openid',
  `ticket` varchar(500) NOT NULL COMMENT '邀请二维码的ticket',
  `invite_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_invited_admin_id` (`invited_admin_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- -----------------------
-- -个人集中化能力平台，红包，20170112
-- -----------------------
CREATE TABLE `individual_flow_order` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `mobile` varchar(11) NOT NULL COMMENT '订购者手机号码',
  `prd_id` bigint(18) NOT NULL,
  `size` bigint(18) NOT NULL COMMENT '流量大小，以M为单位',
  `system_num` varchar(100) NOT NULL COMMENT '系统序列号',
  `ec_serial_num` varchar(100) DEFAULT NULL COMMENT 'ec侧发送的序列号',
  `boss_serial_num` varchar(100) DEFAULT NULL COMMENT 'boss侧返回的序列号',
  `status` int(2) NOT NULL COMMENT '订购状态，3-订购成功，4-订购失败',
  `error_msg` varchar(500) DEFAULT NULL COMMENT '失败原因',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_flag` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_system_num` (`system_num`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `individual_activity_serial_num` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `system_num` varchar(100) NOT NULL COMMENT '平台的活动序列号',
  `ec_serial_num` varchar(100) DEFAULT NULL COMMENT 'EC平台请求生成活动的序列号',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_flag` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_idx_system_num` (`system_num`) USING BTREE,
  KEY `idx_ec_serial_num` (`ec_serial_num`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE `activity_info`
ADD INDEX `idx_activity_id` (`activity_id`) USING BTREE ;

ALTER TABLE `activity_win_record`
ADD INDEX `idx_activity_id` (`activity_id`) USING BTREE ;

ALTER TABLE `activity_prize`
ADD INDEX `idx_activity_id` (`activity_id`) USING BTREE ;

INSERT INTO `global_config` VALUES (NULL, '个人集中化平台校验号码url', '个人集中化平台校验号码url', 'JIZHONG_ACTIVITY_CHECK_URL', 'http://localhost:8080/web-in/api/phoneQuery/sichuan.html', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` VALUES (NULL, '四川BOSS红包落地URL', '四川BOSS红包落地URL', 'BOSS_SC_FLOWREDPACKET_URL', 'http://218.205.252.26:33000/rest/1.0/RedPacket', NOW(), NOW(), '1', '1', '0', '0');
INSERT INTO `global_config` VALUES (NULL, '四川BOSS红包落地资费代码', '四川BOSS红包落地资费代码', 'BOSS_SC_FLOWREDPACKET_PRCID', 'ACAZ27212', NOW(), NOW(), '1', '1', '0', '0');
INSERT INTO `global_config` VALUES (NULL, '四川BOSS红包订购URL', '四川BOSS红包订购URL', 'BOSS_SC_ORDERFLOW_URL', 'http://218.205.252.26:33000/rest/1.0/sShortAddMode', NOW(), NOW(), '1', '1', '0', '0');

-- -----------------------
-- 20170303  修改global_config的config_value字段的长度
-- -----------------------
ALTER TABLE global_config MODIFY COLUMN config_value VARCHAR(4096) NOT NULL DEFAULT ''
COMMENT '配置VALUE值';

-- -------------------------------
-- --20170302 湖南BOSS接口改造，新增参数
-- -------------------------------
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES ( NULL, '湖南BOSS省别编码', '湖南BOSS省别编码', 'BOSS_HUNAN_PROVINCE_CODE', 'HNAN', NOW(), NOW(), '1', '1', '0', NULL );
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (NULL, '湖南BOSS接入方式', '湖南BOSS接入方式', 'BOSS_HUNAN_IN_MODE_CODE', '9', NOW(), NOW(), '1', '1', '0',
         NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (NULL, '湖南BOSS地州编码', '湖南BOSS地州编码', 'BOSS_HUNAN_TRADE_EPARCHY_CODE', '0731', NOW(), NOW(), '1',
         '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (NULL, '湖南BOSS业务区编码', '湖南BOSS业务区编码', 'BOSS_HUNAN_TRADE_CITY_CODE', 'XXXG', NOW(), NOW(), '1', '1',
         '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (NULL, '湖南BOSS员工所在的部门', '湖南BOSS员工所在的部门', 'BOSS_HUNAN_TRADE_DEPART_ID', 'C0ZZC', NOW(), NOW(), '1',
         '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (NULL, '湖南BOSS员工编码', '湖南BOSS员工编码', 'BOSS_HUNAN_TRADE_STAFF_ID', 'ITFHYPT1', NOW(), NOW(), '1',
         '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (NULL, '湖南BOSS渠道接入密码', '湖南BOSS渠道接入密码', 'BOSS_HUNAN_TRADE_DEPART_PASSWD', '348688', NOW(), NOW(),
         '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (NULL, '湖南BOSS接入IP', '湖南BOSS接入IP', 'BOSS_HUNAN_TRADE_TERMINAL_ID', '120.76.211.39', NOW(), NOW(),
         '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (NULL, '湖南BOSS路由地州编码', '湖南BOSS路由地州编码', 'BOSS_HUNAN_ROUTE_EPARCHY_CODE', '0731', NOW(), NOW(), '1',
         '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (NULL, '湖南BOSS查询类型', '湖南BOSS查询企业产品列表时固定参数,TYPE=00', 'BOSS_HUNAN_QUERY_TYPE', '00', NOW(), NOW(),
         '1', '1', '0', NULL);

-- -------------------------------
-- --20170315 辽宁boss参数
-- -------------------------------
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (NULL, '辽宁渠道appId', '辽宁渠道appId', 'BOSS_LN_APP_ID', '501800', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (NULL, '辽宁渠道appKey', '辽宁渠道appKey', 'BOSS_LN_APP_KEY', '09b149b082d320cb54cd7385b129371f', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (NULL, '辽宁渠道客户编码', '辽宁渠道客户编码', 'BOSS_LN_CUST_ID', '100016261826', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (NULL, '辽宁渠道集团编码', '辽宁渠道集团编码', 'BOSS_LN_BUSI_CODE', 'BOSS_LN_BUSI_CODE', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (NULL, '辽宁流量统付生效方式', '辽宁流量统付生效方式', 'BOSS_LN_EFFECTIVE_WAY', '0', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (NULL, '辽宁渠道是否发送短信', '辽宁渠道是否发送短信', 'BOSS_LN_SEND_MSG', '0', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (NULL, '辽宁渠道赠送月份', '辽宁渠道赠送月份', 'BOSS_LN_GIVE_MONTH', '1', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (NULL, '辽宁渠道Openid', '辽宁渠道Openid', 'BOSS_LN_OPENID', 'e2f94078-4c61-4d25-96ec-9a5aa82cb063', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (NULL, '辽宁渠道URL', '辽宁渠道URL', 'BOSS_LN_URL', 'http://221.180.247.69:5291/oppf', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (NULL, '辽宁渠道AccessToken', '辽宁渠道AccessToken', 'BOSS_LN_ACCESSTOKEN', '58d238d7-45ff-4f02-bc94-6cd93c5fff2c', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (NULL, '辽宁渠道OperId', '辽宁渠道OperId', 'BOSS_LN_OPERID', '100004638522', NOW(), NOW(), '1', '1', '0', NULL);

-- -----------------------
-- 20170303 修改个人账户结构，增加来源账户id，wujiamin
-- -----------------------
ALTER TABLE `individual_account`
MODIFY COLUMN `type`  int(8) NOT NULL COMMENT '-1为个人帐户(话费，流量包)，0为活动冻结账户，1为兑换冻结账户，2为购买冻结账户，3流量订购账户' AFTER `individual_product_id`,
ADD COLUMN `source_account_id`  bigint(18) NULL COMMENT '来源账户，用户流量红包活动，记录该活动账户来源于哪个订购账户' AFTER `count`;

-- --------------------
-- 20170303 四川红包修改，wujiamin
-- --------------------
ALTER TABLE `individual_account`
ADD COLUMN `expire_time` datetime NULL DEFAULT NULL COMMENT '四川红包流量账户过期时间' AFTER `version`;

INSERT INTO `global_config` VALUES (NULL, '四川个人流量红包活动规则', '四川个人流量红包活动规则', 'SC_REDPACKET_RULE', '1、每次活动同一个手机号只能抢一次，红包有效期24个小时。输入手机号参与抽奖即可参与活动，充值结果以到账短信为准。<br>2、抢到的流量立即充入手机账户，流量当月有效。违规行为一经发现，用户违规行为一经发现将不能再购买、参与红包活动。', NOW(), NOW(), '1', '1', '0', '0');
INSERT INTO `global_config` VALUES (NULL, '四川个人流量红包活动名称', '四川个人流量红包活动名称', 'SC_REDPACKET_NAME', '四川流量红包', NOW(), NOW(), '1', '1', '0', '0');
INSERT INTO `global_config` VALUES (NULL, '四川个人流量红包活动对象', '四川个人流量红包活动对象', 'SC_REDPACKET_OBJECT', '四川移动用户', NOW(), NOW(), '1', '1', '0', '0');
INSERT INTO `global_config` VALUES (NULL, '四川个人流量红包每天最多订购次数', '四川个人流量红包每天最多订购次数', 'SC_FLOW_ORDER_DAILY_LIMIT', '10', NOW(), NOW(), '1', '1', '0', '0');
INSERT INTO `global_config` VALUES (NULL, '四川个人流量红包每月最多订购次数', '四川个人流量红包每月最多订购次数', 'SC_FLOW_ORDER_MONTHLY_LIMIT', '50', NOW(), NOW(), '1', '1', '0', '0');

-- --------------------
-- 20170313 山东对账是否使用流水号，qihang
-- --------------------
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES ('山东对账是否使用流水号', '山东对账是否使用流水号,ON为开启', 'SD_RECONCILE_USE_SEQ', 'ON', NOW(),NOW(), '1', '1', '0', null);

-- --------------------
-- 20170323 上海月呈渠道充值的相应参数, sunyiwei
-- --------------------
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (null, '上海月呈充值地址', '上海月呈充值地址', 'SHYC_CHARGE_UR', 'http://域名:端口/api/CallApi/Index', '2017-03-23 11:50:00', '2017-03-23 11:50:00', 1, 1, 0, NULL),
  (null, '上海月呈签名时使用的apikey', '上海月呈签名时使用的apikey', 'SHYC_API_KEY', '7f4cc16ea94fab234ff20d5586585c61', '2017-03-23 11:50:35', '2017-03-23 11:50:35', 1, 1, 0, NULL),
  (null, '上海月呈代理商编码', '上海月呈代理商编码', 'SHYC_SUPPLIER_CODE', 'D307', '2017-03-23 11:51:07', '2017-03-23 11:51:07', 1, 1, 0, NULL);

-- -------------------------------
-- --20170321 营销活动中是否允许设置三网产品
-- -------------------------------
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (NULL, '营销活动中是否允许三网产品', '营销活动中是否允许三网产品，存在且value值为YES时允许（不区分大小写），不存在或值不为YES为不允许',
         'ALL_PLATFORM_PRODUCT', 'YES', '2017-03-21 14:20:13', '2017-03-21 15:46:12', 1, 1, 0, NULL);

-- --------------------------
-- 20170315 微信公众号积分兑换规则，wujiamin
-- --------------------------
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES ('微信公众号积分兑换规则', '微信公众号积分兑换规则,1个积分可以兑换流量M数', 'WEIXIN_SCORE_EXCHANGE_RULE', '10', NOW(),NOW(), '1', '1', '0', null);


-- ------------------------
-- 20170317 个人产品增加扩展属性字段，wujiamin
-- ------------------------
ALTER TABLE `individual_product`
ADD COLUMN `feature`  varchar(500) NULL COMMENT '产品的扩展属性' AFTER `default_value`;

-- -------------------------
-- 20170317 增加微信兑换表，wujiamin
-- -------------------------
CREATE TABLE `wx_exchange_record` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `admin_id` bigint(18) NOT NULL COMMENT '发起积分兑换的用户Id',
  `mobile` varchar(11) NOT NULL COMMENT '充值的手机号码',
  `individual_product_id` bigint(18) NOT NULL COMMENT '充值的产品id（individual_product）',
  `system_num` varchar(64) NOT NULL COMMENT '平台兑换系统序列号（individual_account_record对应）',
  `boss_req_serial_num` varchar(64) DEFAULT NULL COMMENT '请求boss的流水号',
  `boss_resp_serial_num` varchar(64) DEFAULT NULL COMMENT 'boss返回的流水号',
  `status` int(2) DEFAULT NULL COMMENT '1-待充值，2-已发送充值请求,3-充值成功，4-充值失败',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_flag` int(2) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_system_num` (`system_num`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- -----------------------
-- 20170317 增加四川验证码下发验证url
-- -----------------------
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES ('四川BOSS随机码下发验证URL', '四川BOSS随机码下发验证URL', 'BOSS_SC_QRY_RANDPASS_URL', 'http://218.205.252.26:33000/rest/1.0/sQryRandPass', NOW(),NOW(), '1', '1', '0', null);

-- ----------------------------------
-- 20170327 游客登录错误信息，sunyiwei
-- ---------------------------------------
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (null, '游客登录错误信息', '游客登录错误信息', 'GUEST_LOGIN_MSG', '当前账户平台不存在，游客不能登录，请联系相关客户经理', '2016-09-21 09:31:05', '2016-09-21 10:06:01', 1, 1, 0, NULL);

-- ---------------------------------------
-- 20170330 广东众筹开户增加字段 by qinqinyan
-- ---------------------------------------
INSERT INTO `global_config` VALUES (null, '是广东众筹平台', '广东众筹和广东流量卡省份标示相同，采用了该标示区分企业开户差异。“YES”是广东众筹平台，为空或者其他是广东流量卡平台', 'IS_CROWDFUNDING_PLATFORM', 'NO', '2017-03-29 09:58:38', '2017-03-29 09:58:38', '1', '1', '0', null);

ALTER TABLE `enterprises_ext_info`
ADD COLUMN `join_type`  int(2) NULL DEFAULT NULL COMMENT '报名方式；1：大规模企业；2：中小规模企业' AFTER `delete_flag`;
ALTER TABLE `enterprises_ext_info`
ADD COLUMN `callback_url`  varchar(300) NULL DEFAULT NULL COMMENT '接口回调地址' AFTER `join_type`;

-- -----------------------
-- 20170331 增加微信公众号发送模板消息url
-- -----------------------
INSERT INTO `global_config` VALUES (null, '微信公众号发送模板消息接口', '微信公众号发送模板消息接口', 'WEIXIN_SEND_TEMPLATE_URL', 'http://lljyz.4ggogo.com/wechat/async/sendWxTemplateMsg.html', NOW(), NOW(), '1', '1', '0', null);

-- -----------------------
-- 20170406 广东积分兑换参数   linguangkuo
-- -----------------------
INSERT INTO `global_config` VALUES (null, '广东流量币BOSS通道ECCODE', '广东流量币BOSS通道ECCODE', 'BOSS_GUANGDONG_FLOWCOIN_ECCODE', '2000188888', NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '广东流量币BOSS通道ECUSERNAME', '广东流量币BOSS通道ECUSERNAME', 'BOSS_GUANGDONG_FLOWCOIN_ECUSERNAME', 'admin', NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '广东流量币BOSS通道ECPASSWORD', '广东流量币BOSS通道ECPASSWORD', 'BOSS_GUANGDONG_FLOWCOIN_ECPASSWORD', 'TneHRkPUuqKvpxEJNUSCguMaIoR413Jf', NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '广东流量币BOSS通道PRDORDNUM', '广东流量币BOSS通道PRDORDNUM', 'BOSS_GUANGDONG_FLOWCOIN_PRDORDNUM', '50115100100', NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '广东流量币BOSS通道CHARGEURL', '广东流量币BOSS通道CHARGEURL', 'BOSS_GUANGDONG_FLOWCOIN_CHARGE_URL', 'http://221.179.7.247:8201/NGADCInterface/NGADCServicesForEC.svc/NGADCServicesForEC', NOW(), NOW(), '1', '1', '0', null);

-- ------------------------
-- 20170406 兑换表中增加兑换消耗的流量币个数
-- ------------------------
ALTER TABLE `wx_exchange_record`
ADD COLUMN `count`  int NOT NULL DEFAULT 0 COMMENT '兑换消耗的流量币个数' AFTER `individual_product_id`;
-- 每月每个账户流量币兑换的流量总额限制
INSERT INTO `global_config` VALUES (null, '微信公众号积分兑换账户每月兑换流量总额限制', '微信公众号积分兑换账户每月兑换流量总额限制（MB）', 'WEIXIN_EXCHANGE_MOTHLY_LIMIT', '1000', NOW(), NOW(), '1', '1', '0', null);

-- --------------------------------
-- 20170406 北京云漫渠道的相关参数 sunyiwei
-- -------------------------------
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (null, '北京云漫渠道的partyId', '北京云漫渠道的partyId', 'BJYM_PARTY_ID', '10002062', NOW(), NOW(), 1, 1, 0, NULL);

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (null, '北京云漫渠道的key', '北京云漫渠道的key', 'BJYM_KEY', 'bea8b0cdf1aa5bdba7fb1118daef17a7', NOW(), NOW(), 1, 1, 0, NULL);

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (null, '北京云漫渠道的充值请求地址', '北京云漫渠道的充值请求地址', 'BJYM_CHARGE_URL', 'http://114.55.79.251:16838/HandleSubmit/submit', NOW(), NOW(), 1, 1, 0, NULL);

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (null, '北京云漫渠道的用户名', '北京云漫渠道的用户名', 'BJYM_USER_NAME', 'zhongyixx', NOW(), NOW(), 1, 1, 0, NULL);

-- ----------------------------------
-- 20170425 增加广东微信大转盘活动所属企业id的配置项
-- ----------------------------------
INSERT INTO `global_config` VALUES (null, '广东微信大转盘活动所属企业id', '广东微信大转盘活动所属企业id', 'WEIXIN_LOTTERY_ENTER_ID', '创建大转盘的企业id', NOW(), NOW(), '1', '1', '0', null);

-- -----------------------------------------
-- 20170412 众筹平台增加微信侧后台管理列表权限，wujiamin
-- -----------------------------------------
INSERT INTO `authority` VALUES (NULL, NULL, '会员列表(众筹)', 'ROLE_MEMBERSHIP_LIST', '111001', NULL, NOW(), NULL, NULL, NOW(), '0');
INSERT INTO `authority` VALUES (NULL, NULL, '兑换报表(众筹)', 'ROLE_MEMBERSHIP_EXCHANGE_LIST', '111002', NULL, NOW(), NULL, NULL, NOW(), '0');

-- 兑换记录增加充值信息字段，wujiamin
ALTER TABLE `wx_exchange_record`
ADD COLUMN `message`  varchar(500) NULL COMMENT '充值信息' AFTER `status`;
  
-- -----------------------
-- 20160410 增加生效方式
-- -----------------------
ALTER TABLE `charge_record`
ADD COLUMN `effect_type` int(2) DEFAULT 1
COMMENT '生效方式，立即生效1，次月生效2'
AFTER `change_account_status`;
ALTER TABLE `present_record`
ADD COLUMN `effect_type` int(2) DEFAULT 1
COMMENT '生效方式，立即生效1，次月生效2'
AFTER `status`;

-- --------------------------------------
-- 20170420 四川红包单点登录配置项，wujiamin
-- --------------------------------------
INSERT INTO `global_config` VALUES (NULL, '四川和生活合作者ID', '四川和生活合作者ID', 'SC_HSH_PARTNERID', '10000044', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` VALUES (NULL, '四川和生活单点登录接口URL', '四川和生活单点登录接口URL', 'SC_HSH_SSO_URL', 'https://hbx.139sc.com:8080/har-hbx-third-front/hbxBoss/v1/HTF10001', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` VALUES (NULL, '四川和生活APPID', '四川和生活APPID', 'SC_HSH_APPID', '10070', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` VALUES (NULL, '四川和生活的公钥路径', '四川和生活的公钥路径', 'SC_HSH_PUBLIC_KEY', '/etc/pdata/conf/scredpacket_public_key_hsh.txt', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` VALUES (NULL, '四川和生活的私钥路径', '四川和生活的私钥路径', 'SC_HSH_PRIVATE_KEY', '/etc/pdata/conf/scredpacket_private_key.txt', NOW(), NOW(), '1', '1', '0', NULL);


-- -----------------------------------------
-- 20170420 四川红包增加后台管理报表权限，wujiamin
-- -----------------------------------------
INSERT INTO `authority` VALUES (NULL, NULL, '订购报表(四川红包)', 'ROLE_SCREDPACKET_ORDER_LIST', '112001', NULL, NOW(), NULL, NULL, NOW(), '0');
INSERT INTO `authority` VALUES (NULL, NULL, '抢红包列表(四川红包)', 'ROLE_SCREDPACKET_REDPACKET_LIST', '112002', NULL, NOW(), NULL, NULL, NOW(), '0');

-- --------------------------------
-- 20170421 充值短信提醒，linguangkuo
-- --------------------------------
INSERT INTO `global_config` VALUES (NULL, '充值成功短信提醒', '充值成功短信提醒', 'CHARGE_SUCCESS_NOTICE', 'false', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` VALUES (NULL, '回调可以重复的次数', '回调可以重复的次数', 'CALLBACK_RETRY_TIMES', '0', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` VALUES (NULL, '回调重试中间隔的时间种子', '回调重试中间隔的时间种子', 'CALLBACK_RETRY_SECONDS', '1', NOW(), NOW(), '1', '1', '0', NULL);

-- --------------------------------
-- 20170421 登录配置，linguangkuo
-- --------------------------------
INSERT INTO `global_config` VALUES (NULL, '登录方式：1静态密码，2短信验证码，3静态密码或短信验证码，4静态密码和短信验证码', '登录方式：1静态密码，2短信验证码，3静态密码或短信验证码，4静态密码和短信验证码', 'LOGIN_TYPE', '3', NOW(), NOW(), '1', '1', '0', NULL);

-- --------------------------------
-- 20170424 吉林boss配置，linguangkuo
-- --------------------------------
INSERT INTO `global_config` VALUES (NULL, '吉林boss渠道充值地址', '吉林boss渠道充值地址', 'BOSS_JILIN_CHARGE_URL', 'http://211.141.63.140:18080/rest/1.0/orderPrcSubmit', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` VALUES (NULL, '吉林boss渠道余额查询地址', '吉林boss渠道余额查询地址', 'BOSS_JILIN_OWEPAY_URL', 'http://211.141.63.140:18080/rest/1.0/owePayQry', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` VALUES (NULL, '吉林boss渠道APPKEY', '吉林boss渠道APPKEY', 'BOSS_JILIN_APPKEY', '10001020', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` VALUES (NULL, '吉林boss渠道STATUS', '吉林boss渠道STATUS', 'BOSS_JILIN_STATUS', '1', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` VALUES (NULL, '吉林boss渠道USERNAME', '吉林boss渠道USERNAME', 'BOSS_JILIN_USERNAME', 'linguangkuo', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` VALUES (NULL, '吉林boss渠道私钥文件', '吉林boss渠道私钥文件', 'BOSS_JILIN_PRIVATEKEY_FILE', '/etc/pdata/conf/airreCharge_private_key.pem', NOW(), NOW(), '1', '1', '0', NULL);

-- --------------------------------
-- 20170428 用户变更相关表,qihang
-- --------------------------------

-- 用户变更相关表,保存临时信息表
CREATE TABLE `admin_change_detail` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `request_id` bigint(18) NOT NULL,
  `enter_id` bigint(18) NOT NULL,
  `admin_id` bigint(18) NOT NULL,
  `src_name` varchar(20) NOT NULL COMMENT '源姓名',
  `src_phone` varchar(20) NOT NULL COMMENT '源手机号',
  `dest_name` varchar(20) NOT NULL COMMENT '目标姓名',
  `dest_phone` varchar(20) NOT NULL COMMENT '目标手机号',
  `comment` varchar(500) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_flag` int(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 用户变更相关表,保存提交审核之后的信息表
CREATE TABLE `admin_change_operator` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `enter_id` bigint(18) NOT NULL,
  `admin_id` bigint(18) NOT NULL,
  `dest_name` varchar(20) NOT NULL COMMENT '源手机号',
  `dest_phone` varchar(20) NOT NULL COMMENT '目标手机号',
  `comment` varchar(500) DEFAULT NULL,
  `delete_flag` int(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

INSERT INTO `global_config` VALUES (NULL, '客户经理用户管理使用企业列表中的用户管理', '客户经理用户管理使用企业列表中的用户管理,true为使用', 'ADMIN_CHANGE_USE_ENTERLIST', 'true', NOW(), NOW(), '1', '1', '0', NULL);

INSERT INTO `authority` VALUES (NULL, NULL, '用户修改审核', 'ROLE_ADMINCHANGE_APPROVAL', '109011', NULL, NOW(), NULL, NULL, NOW(), '0');

-- ----------------------
-- 20170322 增加账户余额和流量订购记录的关系，增加订购过期时间
-- ----------------------
ALTER TABLE `individual_account`
ADD COLUMN `current_order_id`  bigint(18) NULL COMMENT '当前账户中的流量来源于哪次订购，存储订购表中的id' AFTER `expire_time`;

ALTER TABLE `individual_flow_order`
ADD COLUMN `expire_time`  datetime NULL AFTER `update_time`;

CREATE TABLE `individual_activity_order` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `activity_id` varchar(64) NOT NULL,
  `order_id` bigint(18) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_activity_id` (`activity_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- -------------------------------------------
-- 20170322 删除个人账户中之前添加的source_account_id，修改过四川红包的逻辑，该字段没有用处
-- -------------------------------------------
ALTER TABLE `individual_account`
DROP COLUMN `source_account_id`;

-- -----------------------
-- 20170503 產品組功能表結構變更  by qinqinyan
-- -----------------------
-- 下载供应商列表文件临时目录
INSERT INTO `global_config` VALUES (null, '下载供应商列表文件临时目录', '下载供应商列表文件临时目录', 'SUPPLIER_PATH', '/srv/appdata/data/supplier/', '2017-03-02 15:40:55', '2017-03-02 15:41:11', '1', '1', '0', null);

-- 供應商增加上下架字段
ALTER TABLE `supplier`
ADD COLUMN `status`  int(1) NOT NULL COMMENT '0, 下架; 1,上架' AFTER `sync`;

-- 供應商產品增加説明字段
ALTER TABLE `supplier_product`
ADD COLUMN `illustration`  varchar(600) NULL COMMENT '说明' AFTER `delete_flag`;

-- Table structure for `product_template_enterprise_map`
DROP TABLE IF EXISTS `product_template_enterprise_map`;
CREATE TABLE `product_template_enterprise_map` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `enterprise_id` bigint(18) NOT NULL COMMENT '企业ID',
  `product_template_id` bigint(18) NOT NULL COMMENT '产品模板ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `delete_flag` int(1) NOT NULL COMMENT '1:已删除；0未删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- Table structure for `product_template`
DROP TABLE IF EXISTS `product_template`;
CREATE TABLE `product_template` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL COMMENT '模板名称',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `delete_flag` int(1) NOT NULL COMMENT '0,未删除，1逻辑删除',
  `default_flag` int(1) NOT NULL COMMENT '1:企业默认关联的产品组；0非企业默认的关联组',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

INSERT INTO `product_template` VALUES ('1', '默认模板', '2017-03-15 15:50:09', '2017-03-15 15:50:14', '0', '1');

-- 增加字段
ALTER TABLE `product_change_operator`
ADD COLUMN `old_product_template_id`  bigint(18) NULL COMMENT '企业原产品模板' AFTER `delete_flag`;

ALTER TABLE `product_change_operator`
ADD COLUMN `new_product_template_id`  bigint(18) NULL COMMENT '变更后的产品模板' AFTER `old_product_template_id`;

ALTER TABLE `product_change_operator`
ADD COLUMN `create_time`  datetime(0) NULL AFTER `new_product_template_id`;

ALTER TABLE `product_change_operator`
ADD COLUMN `update_time`  datetime(0) NULL AFTER `create_time`;

-- 增加字段
ALTER TABLE `product_change_detail`
ADD COLUMN `old_product_template_id`  bigint(18) NULL COMMENT '企业原产品模板' AFTER `delete_flag`;

ALTER TABLE `product_change_detail`
ADD COLUMN `new_product_template_id`  bigint(18) NULL COMMENT '变更后的产品模板' AFTER `old_product_template_id`;

-- Table structure for `platform_product_template_map`
DROP TABLE IF EXISTS `platform_product_template_map`;
CREATE TABLE `platform_product_template_map` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `product_template_id` bigint(18) NOT NULL COMMENT '产品模板ID',
  `platform_product_id` bigint(18) NOT NULL COMMENT '平台产品ID',
  `create_time` datetime NOT NULL COMMENT '添加时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `delete_flag` int(1) NOT NULL COMMENT '0,未删除，1已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 增加字段
ALTER TABLE `product`
ADD COLUMN `illustration`  varchar(300) NOT NULL COMMENT '说明' AFTER `configurable_num_flag`;

INSERT INTO `global_config` VALUES (null, '是否使用产品模板', '是否使用产品模板，YES是使用，不设置改配置项或者值为其他则不使用该配置项', 'USE_PRODUCT_TEMPLATE', 'NO', '2017-03-24 09:18:57', '2017-05-03 10:57:36', '1', '1', '0', null);

-- -----------------------
-- 20170416 本地文件存储的配置 sunyiwei
-- -----------------------
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (null, '本地文件存储的目录', '本地文件存储的目录', 'LOCAL_FILESYSTEM_STORE_PATH', '/tmp/pdata/', NOW(), NOW(), 1, 1, 0, NULL);

-- --------------------------------
-- 20170503 企业状态、EC操作记录，罗祖武
-- --------------------------------
DROP TABLE IF EXISTS `ent_ec_record`;
CREATE TABLE `ent_ec_record` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `operator_id` bigint(18) DEFAULT NULL COMMENT '操作者ID',
  `operator_name` varchar(18) DEFAULT NULL COMMENT '操作者的姓名',
  `operator_mobile` varchar(18) DEFAULT NULL COMMENT '操作者的手机号码',
  `operator_role` varchar(18) DEFAULT NULL COMMENT '操作者的角色',
  `ent_id` bigint(18) DEFAULT NULL COMMENT '企业ID',
  `op_type` int(18) DEFAULT NULL COMMENT '操作类型',
  `op_desc` varchar(500) DEFAULT NULL COMMENT '操作附加说明',
  `pre_status` int(18) DEFAULT NULL COMMENT '更新前的状态',
  `now_status` int(18) DEFAULT NULL COMMENT '更新后的状态',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `delete_flag` int(11) DEFAULT NULL COMMENT '删除标识位：0未删除；1已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `ent_status_record`;
CREATE TABLE `ent_status_record` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `operator_id` bigint(18) DEFAULT NULL COMMENT '操作者ID',
  `operator_name` varchar(18) DEFAULT NULL COMMENT '操作者姓名',
  `operator_mobile` varchar(18) DEFAULT NULL COMMENT '操作者手机号码',
  `operator_role` varchar(18) DEFAULT NULL COMMENT '操作者角色',
  `ent_id` bigint(18) DEFAULT NULL COMMENT '企业ID',
  `op_type` int(18) DEFAULT NULL COMMENT '操作类型',
  `op_desc` varchar(255) DEFAULT NULL COMMENT '操作附加说明',
  `pre_status` int(18) DEFAULT NULL COMMENT '更新前的状态',
  `now_status` int(18) DEFAULT NULL COMMENT '更新后的状态',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `delete_flag` int(11) DEFAULT NULL COMMENT '删除标识位：0未删除；1已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


-- ------------------------------
-- 20170503 云企信，折扣表变更，wujiamin
-- ------------------------------
ALTER TABLE `discount`
ADD COLUMN `customer_type_id`  int(18) NULL DEFAULT 1 COMMENT '该折扣对应的客户类型' AFTER `discount`;
ALTER TABLE `discount`
MODIFY COLUMN `id`  bigint(18) NOT NULL AUTO_INCREMENT FIRST ;

-- --------------------------------
-- 20170504 众筹活动用户列表查询方式 linguangkuo
-- --------------------------------
ALTER TABLE `crowdfunding_activity_detail`
ADD COLUMN `user_list`  int(2) NULL COMMENT '用户列表查询方式：1txt上传；2ADC查询；3企业接口查询' AFTER `join_type`;

DROP TABLE IF EXISTS `crowdfunding_query_url`;
CREATE TABLE `crowdfunding_query_url` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `crowdfunding_activity_detail_id` bigint(18) DEFAULT NULL COMMENT '众筹活动ID',
  `query_url` varchar(255) DEFAULT NULL COMMENT '企业查询接口',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `delete_flag` int(11) DEFAULT NULL COMMENT '删除标识位：0未删除；1已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ------------------------------
-- 20170508 随机红包 by qinqinyan
-- ------------------------------
ALTER TABLE `activity_info`
ADD COLUMN `gived_user_count`  bigint(18) NULL DEFAULT 0 COMMENT '(随机红包需求定义)已发数量：已发数量为一共有多少用户抽奖。（开发理解）已发用户数量（一个红包活动只能中奖一次，实质就是已发奖品数量）'
AFTER `url`;
ALTER TABLE `activity_info`
ADD COLUMN `used_product_size`  bigint(18) NULL DEFAULT 0 COMMENT '（随机红包需求定义）已发额度：显示当前有多少产品被抢走，流量池产品单位为MB，话费产品单位为元。（开发理解）发送流量总大小，以KB形式存储，如果后续做话费，以分存储'
AFTER `gived_user_count`;

-- ------------------------------
-- 20170509 大转盘流量币充值 by qinqinyan
-- ------------------------------
ALTER TABLE `individual_account_record`
MODIFY COLUMN `description` varchar(700) DEFAULT NULL COMMENT '描述'
AFTER `count`;

-- --------------------------
-- 20170505 个人产品表增加云企信产品注释，wujiamin
-- --------------------------
ALTER TABLE `individual_product`
MODIFY COLUMN `type`  int(8) NOT NULL DEFAULT 2 COMMENT '产品类型，0为话费，1为流量币产品，2为流量包产品，3、默认流量（四川红包），4、积分（广东微信），5、云企信订购产品' AFTER `product_code`;

-- -----------------------
-- 20170504 省份校验和生效方式  linguangkuo
-- -----------------------
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (null, '赠送页面是否需要生效方式', '赠送页面是否需要生效方式', 'EFFECT_TYPE', 'false', NOW(), NOW(), 1, 1, 0, NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (null, '是否校验管理员归属地', '是否校验管理员归属地', 'NEED_CHECK_PHONE_REGION', 'false', NOW(), NOW(), 1, 1, 0, NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (null, '号码归属地', '号码归属地', 'PHONE_REGION', 'ZJ', NOW(), NOW(), 1, 1, 0, NULL);

-- ---------------------------
-- 20170504 产品组补充sql by qinqinyan
-- ---------------------------
-- 产品权限
INSERT INTO `authority` VALUES (null, null, '产品模板', 'ROLE_PRODUCT_TEMPLATE', '102006', null, '2017-03-10 10:46:55', '1', '1', '2017-03-10 10:46:55', '0');

-- 存在线上supplier表一些字段默认不为空，这里统一设置为空
ALTER TABLE `supplier` MODIFY COLUMN `isp` VARCHAR(2) CHARACTER SET utf8 COLLATE utf8_general_ci default NULL
COMMENT '供应商类型，M：移动；T：电信；U：联通; A: 三网' AFTER `name`;

ALTER TABLE `supplier` MODIFY COLUMN `sync` int(1)  default 0
COMMENT '是否有同步产品接口：1-有；0-无' AFTER `delete_flag`;

-- --------------------------------
-- 20170509 修改企业状态操作记录描述信息长度，罗祖武
-- --------------------------------
ALTER TABLE `ent_status_record`
MODIFY COLUMN `op_desc`  varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作附加说明' AFTER `op_type`;

-- --------------------------------
-- 20170510 甘肃验证码失效时间，linguangkuo
-- --------------------------------
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
  (null, '甘肃BOSS的验证码失效时间', '甘肃BOSS的验证码失效时间', 'BOSS_GS_EXPIRED_TIME', '5', NOW(), NOW(), 1, 1, 0, NULL);


-- -------------------------
-- 20170511 云企信数据库，wujiamin
-- -------------------------
-- 云企信新增的数据库表
CREATE TABLE `yqx_charge_info` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `serial_num` varchar(255) NOT NULL COMMENT '充值的流水号（关联支付transactionId）',
  `return_system_num` varchar(255) DEFAULT NULL COMMENT '充值时上游返回的序列号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_serial_num` (`serial_num`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `yqx_order_record` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `mobile` varchar(11) NOT NULL COMMENT '订购产品的用户手机号码',
  `individual_product_id` bigint(18) NOT NULL COMMENT '订购的V网产品id（V网产品关联的是individual_product表）',
  `vpmn_time` datetime NOT NULL COMMENT 'V网入网时间',
  `discount` int(11) NOT NULL COMMENT '折扣，1-100的值',
  `pay_price` bigint(20) NOT NULL COMMENT '实际支付价格',
  `serial_num` varchar(255) NOT NULL COMMENT '我们平台的订单号',
  `trade_status` int(11) NOT NULL COMMENT '交易状态：PROCESSING(0, "交易中"),CLOSE(1, "交易关闭")，SUCCESS(2, "交易成功"),FAIL(3, "交易失败");',
  `pay_status` int(11) NOT NULL COMMENT '支付状态：WAIT(0, "待支付"),NO(1, "未支付"),SUCCESS(2, "支付成功"),FAIL(3, "支付失败");',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '0',
  `charge_status` int(2) DEFAULT NULL COMMENT '充值状态：1-待充值；2-已发送充值请求；3-充值成功；4-充值失败',
  `charge_time` datetime DEFAULT NULL COMMENT '发起充值的时间',
  `charge_msg` varchar(255) DEFAULT NULL COMMENT '充值详情',
  `charge_return_time` datetime DEFAULT NULL COMMENT '充值返回时间',
  `refund_status` int(11) NOT NULL DEFAULT '0' COMMENT '退款状态：0-未申请退款；1-退款处理中；2-退款成功；3-退款失败',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_serial_num` (`serial_num`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `yqx_pay_record` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `order_serial_num` varchar(255) NOT NULL COMMENT '我们平台的订单号',
  `pay_order_id` varchar(255) NOT NULL COMMENT '128位带订单号的订单编号（发给支付平台的）',
  `pay_transaction_id` varchar(255) NOT NULL COMMENT '交易流水（我们平台发给支付平台的，唯一）',
  `pay_type` int(2) NOT NULL DEFAULT '2' COMMENT '支付类型：1-微信，2-支付宝',
  `status` int(2) NOT NULL COMMENT '支付状态 0 成功 1 失败 2 等待支付(支付平台返回)  3 等待支付平台返回',
  `status_info` varchar(255) DEFAULT NULL COMMENT '支付失败时返回信息',
  `done_code` varchar(255) DEFAULT NULL COMMENT '支付平台成功时返回的流水号',
  `result_return_time` datetime DEFAULT NULL COMMENT '支付结果返回时间',
  `create_time` datetime NOT NULL COMMENT '支付创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '记录更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_pay_transaction_id` (`pay_transaction_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `yqx_refund_record` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `pay_serial_num` varchar(255) NOT NULL COMMENT '支付流水号',
  `refund_serial_num` varchar(255) NOT NULL COMMENT '我们平台发起的退款流水号',
  `status` int(11) NOT NULL COMMENT '退款记录状态',
  `create_time` datetime NOT NULL COMMENT '退款创建时间',
  `result_return_time` datetime DEFAULT NULL COMMENT '支付平台返回退款结果的时间',
  `update_time` datetime DEFAULT NULL COMMENT '退款记录更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `yqx_vpmn_discount` (
  `start` int(11) NOT NULL COMMENT '该折扣时间段的起点，天',
  `end` int(11) NOT NULL COMMENT '该折扣时间段的终点',
  `discount` int(11) NOT NULL COMMENT '折扣，1-100'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- globalConfig增加云企信配置（上线时要调整成生产参数）
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '云企信的appKey', '云企信的appKey', 'YQX_EC_APP_KEY', '1879e0a5a1fb4d52a8e166305c8f9f7d', NOW(), NOW(), '1', '1', '0', NULL);
    
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '云企信的appSecret', '云企信的appSecret', 'YQX_EC_APP_SECRET', 'd67d013773a64aefb6f8f1344dd2e447', NOW(), NOW(), '1', '1', '0', NULL);

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '云企信的authUrl', '云企信的authUrl', 'YQX_EC_AUTH_URL', 'http://localhost:8080/web-in/auth.html', NOW(), NOW(), '1', '1', '0', NULL);

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '云企信的chargeUrl', '云企信的chargeUrl', 'YQX_EC_CHARGE_URL', 'http://localhost:8080/web-in/boss/charge.html', NOW(), NOW(), '1', '1', '0', NULL);

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '云企信订购用户每月订购次数限制', '云企信订购用户每月订购次数限制', 'YQX_ORDER_MONTH_LIMIT', '10', NOW(), NOW(), '1', '1', '0', NULL);

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '云企信四川厂商标识', '云企信四川厂商标识', 'YQX_SC_ORIGIN_ID', 'zyscyqx', NOW(), NOW(), '1', '1', '0', NULL);

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '云企信128位订单号企业编码', '云企信128位订单号企业编码', 'YQX_ORDERID_ENTER_CODE', '2801104057', NOW(), NOW(), '1', '1', '0', NULL);

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '云企信128位订单号省份编码', '云企信128位订单号省份编码', 'YQX_ORDERID_PROVINCE_CODE', '51', NOW(), NOW(), '1', '1', '0', NULL);

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '云企信128位订单号收款方', '云企信128位订单号收款方', 'YQX_ORDERID_RECEIVE_MONEY', 'shoukuanfang', NOW(), NOW(), '1', '1', '0', NULL);

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '云企信128位订单号地市编码', '云企信128位订单号地市编码', 'YQX_ORDERID_CITY_CODE', '01', NOW(), NOW(), '1', '1', '0', NULL);

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '云企信128位订单号运营商编码', '云企信128位订单号运营商编码', 'YQX_ORDERID_ISP_CODE', '01', NOW(), NOW(), '1', '1', '0', NULL);

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '云企信128位订单号供应商编码', '云企信128位订单号供应商编码', 'YQX_ORDERID_SUPPLIER_CODE', '5101', NOW(), NOW(), '1', '1', '0', NULL);

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '云企信128位订单号应用编码', '云企信128位订单号应用编码', 'YQX_ORDERID_APP_CODE', 'yunqixin', NOW(), NOW(), '1', '1', '0', NULL);


-- 支付配置
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '支付平台的ORIGINID', '支付平台的ORIGINID', 'PAY_ORIGINID', '3003', NOW(), NOW(), '1', '1', '0', NULL);

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '支付平台的ACCOUNTCODE', '支付平台的ACCOUNTCODE', 'PAY_ACCOUNTCODE', '18867102087', NOW(), NOW(), '1', '1', '0', NULL);

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '支付平台的MERCHANTID', '支付平台的MERCHANTID', 'PAY_MERCHANTID', '100000002', NOW(), NOW(), '1', '1', '0', NULL);

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '支付平台的PRODUCTID', '支付平台的PRODUCTID', 'PAY_PRODUCTID', '100000001', NOW(), NOW(), '1', '1', '0', NULL);

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '支付平台的同步通知地址', '支付平台的同步通知地址', 'PAY_PAYNOTIFYINTURL', 'http://xj-qa.4ggogo.com/web-in/manage/payplatform/payCallbackSync.html', NOW(), NOW(), '1', '1', '0', NULL);

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '支付平台的异步通知地址', '支付平台的异步通知地址', 'PAY_PAYNOTIFYPAGEURL', 'http://172.23.27.212:8080/web-in/manage/payplatform/payCallbackAsync.html', NOW(), NOW(), '1', '1', '0', NULL);

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '支付平台的支付时间', '支付平台的支付时间,5m代表5分钟，90m代表90分钟', 'PAY_PAYPERIOD', '5m', NOW(), NOW(), '1', '1', '0', NULL);

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '支付平台的加解密秘钥', '支付平台的加解密秘钥', 'PAY_ENCODESECRET', 'vv5my48k9hhvij2fbxzkmm48c7iyxkhr', NOW(), NOW(), '1', '1', '0', NULL);

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '支付平台发送地址', '支付平台发送地址', 'PAY_PAYPLATFORMURL', 'https://paypre.4ggogo.com/core/pay/ask-for.do?xml=', NOW(), NOW(), '1', '1', '0', NULL);

-- 云企信对接地址
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '向云企信发送的请求地址', '向云企信发送的请求地址', 'YQX_CONNECT_REQUEST_URL', 'http://stage.scyunqixin.com/neas_sc/v1/users/check-token', NOW(), NOW(), '1', '1', '0', NULL);

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '给云企信返回的地址', '给云企信返回的地址', 'YQX_CONNECT_RESPONSE_URL', 'http://xj-qa.4ggogo.com/web-in/yqx/order/index.html', NOW(), NOW(), '1', '1', '0', NULL);

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '四川BOSSv网成员查询URL', '四川BOSSv网成员查询URL', 'BOSS_SC_MEMBER_INQUIRY_URL', 'http://218.205.252.26:33000/rest/1.0/MenberInquiry', NOW(), NOW(), '1', '1', '0', NULL);
 
-- -------------------------
-- 20170516 四川流量平台增加索引 zhangyin
-- -------------------------
ALTER TABLE `yqx_pay_record`
ADD INDEX `idx_order_serial_num` (`order_serial_num`) USING BTREE ;
ALTER TABLE `interface_record`
ADD INDEX `idx_serial_num` (`serial_num`) USING BTREE ;

-- --------------------------
-- 20170517 云企信订购表V网入网时间设置成可以为空，wujiamin
-- --------------------------
ALTER TABLE `yqx_order_record`
MODIFY COLUMN `vpmn_time`  datetime NULL COMMENT 'V网入网时间' AFTER `individual_product_id`;

-- ----------------------------
-- 20170517 重庆云企信配置，wujiamin
-- ----------------------------
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
    VALUES (NULL, '云企信重庆厂商标识', '云企信重庆厂商标识', 'YQX_CQ_ORIGIN_ID', 'zycqyqx', NOW(), NOW(), '1', '1', '0', NULL);
    
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
    VALUES (NULL, '重庆云企信产品个数限制', '重庆云企信产品个数限制', 'YQX_CQ_PRODUCT_NUM_LIMIT', '100', NOW(), NOW(), '1', '1', '0', NULL);  

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
    VALUES (NULL, '云企信订购页面展示的文案', '云企信订购页面展示的文案', 'YQX_ORDER_PAGE_TEXT', '已根据您的网龄进行优惠调整，具体详情见购买说明，如有疑问请致电：158-8233-8533。', NOW(), NOW(), '1', '1', '0', NULL);

-- ----------------------------
-- 20170518 退款页面权限，qihang（该权限现在已经不需要了，wujiamin，20170706）
-- ----------------------------
-- INSERT INTO `authority` VALUES (null, null, '退款管理（云企信）', 'ROLE_YQX_REFUND', '113002', null, NOW(), '1', '1', NOW(), '0');

-- ----------------------------
-- 20170518 云企信返回地址，qihang
-- ----------------------------
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '重庆给云企信返回的地址', '重庆给云企信返回的地址', 'YQX_CQ_CONNECT_RESPONSE_URL', 'http://xj-qa.4ggogo.com/web-in/yqx/order/index.html', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '四川给云企信返回的地址', '四川给云企信返回的地址', 'YQX_SC_CONNECT_RESPONSE_URL', 'http://xj-qa.4ggogo.com/web-in/yqx/order/notice.html', NOW(), NOW(), '1', '1', '0', NULL);


-- ----------------------------
-- 20170519 云企信访问地址，qihang
-- ----------------------------
-- 云企信对接地址
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '向四川云企信发送的请求地址', '向四川云企信发送的请求地址', 'YQX_SC_CONNECT_REQUEST_URL', 'http://stage.scyunqixin.com/neas_sc/v1/users/check-token', NOW(), NOW(), '1', '1', '0', NULL);

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '向重庆云企信发送的请求地址', '向重庆云企信发送的请求地址', 'YQX_CQ_CONNECT_REQUEST_URL', 'http://172.23.28.70:9109/front/users/checkToken', NOW(), NOW(), '1', '1', '0', NULL);

-- 退款设置
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '支付平台退款发送地址', '支付平台退款发送地址', 'PAY_FORMREFUNDURL', 'https://paypre.4ggogo.com/admin/api/refundService.html?xml=', NOW(), NOW(), '1', '1', '0', NULL);

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '支付平台退款回调地址', '支付平台退款回调地址', 'PAY_FORMREFUNDCALLBACKURL', 'http://172.23.27.212:8080/web-in/manage/payplatform/refundAsync.html', NOW(), NOW(), '1', '1', '0', NULL);

-- ----------------------------
-- 20170519 增加云企信后台管理-订单管理权限，wujiamin
-- ----------------------------
INSERT INTO `authority` (`AUTHORITY_ID`, `PARENT_ID`, `NAME`, `AUTHORITY_NAME`, `CODE`, `AUTHORITY_URL`, `CREATE_TIME`, `CREATOR`, `UPDATE_USER`, `UPDATE_TIME`, `DELETE_FLAG`) VALUES (NULL, NULL, '订单管理（云企信）', 'ROLE_YQX_ORDER_MANAGE', '113001', NULL, NOW(), NULL, NULL, NOW(), '0');
INSERT INTO `authority` (`AUTHORITY_ID`, `PARENT_ID`, `NAME`, `AUTHORITY_NAME`, `CODE`, `AUTHORITY_URL`, `CREATE_TIME`, `CREATOR`, `UPDATE_USER`, `UPDATE_TIME`, `DELETE_FLAG`) VALUES (NULL, NULL, '异常订单（云企信）', 'ROLE_YQX_ORDER_ERROR_MANAGE', '113002', NULL, NOW(), NULL, NULL, NOW(), '0');
    
-- -----------------------------
-- 20170519 增加申请退款时间，修改退款状态的注释，wujiamin
-- -----------------------------
ALTER TABLE `yqx_order_record`
ADD COLUMN `refund_approval_time`  datetime NULL COMMENT '退款申请时间' AFTER `refund_status`;

ALTER TABLE `yqx_order_record`
MODIFY COLUMN `refund_status`  int(11) NOT NULL DEFAULT 0 COMMENT '退款状态：0-未申请退款；1-退款受理中；2-退款处理中（已发送退款申请）；3-退款成功；4-退款失败' AFTER `charge_return_time`; 
-- -----------------------------
-- 20170519 增加订购记录充值对应的支付流水号，wujiamin
-- -----------------------------
ALTER TABLE `yqx_order_record`
ADD COLUMN `pay_transaction_id`  varchar(255) NULL COMMENT '支付流水号' AFTER `refund_approval_time`;

-- -----------------------------
-- 20170522 增加云企信重庆移动号段的校验，qh
-- -----------------------------
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '云企信重庆移动校验', '云企信重庆移动校验', 'YQX_CQ_PHONECHECK', 'true', NOW(), NOW(), '1', '1', '0', NULL);

-- -------------------------------
-- 20170522 供应商管理  by qinqinyan
-- -------------------------------
-- 增加供应商付费方式和合同编号
ALTER TABLE `supplier`
ADD COLUMN `pay_type` varchar(32) NULL DEFAULT NULL COMMENT '付款方式'
AFTER `status`;
ALTER TABLE `supplier`
ADD COLUMN `contract_code`  varchar(32) NULL DEFAULT NULL COMMENT '合同编码'
AFTER `pay_type`;

-- 供应商限额
ALTER TABLE `supplier`
ADD COLUMN `limit_money` double(18,2) NULL DEFAULT -1.00 COMMENT '全量每日限额（开发理解：就是该供应商每日充值限制额度）,单位分，-1则不限额'
AFTER `contract_code`;
ALTER TABLE `supplier`
ADD COLUMN `limit_money_flag`  int(1) NULL DEFAULT 0 COMMENT '全量限额标志位，0：不开启；1:开启'
AFTER `limit_money`;

-- 供应商产品限额
ALTER TABLE `supplier_product`
ADD COLUMN `limit_money`  double(18,2) NULL DEFAULT -1.00 COMMENT '限额,单位分，-1则不限额'
AFTER `illustration`;
ALTER TABLE `supplier_product`
ADD COLUMN `limit_money_flag`  int(1) NULL DEFAULT 0 COMMENT '全量限额标志位，0：不开启；1:开启'
AFTER `limit_money`;

-- 供应商余额不足提醒相关全局变量配置
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '供应商余额低于最小值时,是否开启短信通知', '供应商余额低于最小值时,是否开启短信通知,YES:开启;其余关闭功能', 'NOTICE_BALANCE_NOT_ENOUGH','NO', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '供应商余额最低值', '供应商余额最低值,单位:万元', 'SUPPLIER_MIN_BALANCE','5', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) 
VALUES (NULL, '供应商余额不足,通知指定角色管理员', '供应商余额不足,通知指定角色管理员。1:超级管理员;2:客户经理;3:企业管理员;6:省级管理员;7:市级管理员', 'NOTICE_ROLE_ID','2', NOW(), NOW(), '1', '1', '0', NULL);

-- 供应商财务记录表和供应商付款记录表
DROP TABLE IF EXISTS `supplier_finance_record`;
CREATE TABLE `supplier_finance_record` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `supplier_id` bigint(18) NOT NULL COMMENT '供应商id',
  `total_money` double(18,2) NOT NULL DEFAULT '0.00' COMMENT '付款总金额（单位分）',
  `used_money` double(18,2) NOT NULL DEFAULT '0.00' COMMENT '使用总金额（单位分）',
  `balance` double(18,2) NOT NULL DEFAULT '0.00' COMMENT '余额（单位分）',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `delete_flag` int(1) NOT NULL COMMENT '逻辑删除标志位',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `supplier_pay_record`;
CREATE TABLE `supplier_pay_record` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `supplier_id` bigint(18) NOT NULL COMMENT '供应商id',
  `pay_money` double(18,2) NOT NULL DEFAULT '0.00' COMMENT '付款金额(单位分)',
  `pay_time` datetime NOT NULL COMMENT '付款金额',
  `note` varchar(24) NOT NULL COMMENT '付款备注',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `delete_flag` int(1) NOT NULL COMMENT '逻辑删除标志位',
  `operator_id` bigint(18) NOT NULL COMMENT '操作人员id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 供应商和供应商产品限额变更记录表
DROP TABLE IF EXISTS `supplier_modify_limit_record`;
CREATE TABLE `supplier_modify_limit_record` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `supplier_id` bigint(18) NOT NULL COMMENT '供应商id',
  `limit_money` double(18,2) NOT NULL COMMENT '供应商充值限制额度（以已发送充值请求来统计），单位分,-1表示设置为不需要限额',
  `operate_type` int(1) NOT NULL COMMENT '1：设置限额；2：取消限额；3：变更限制额度',
  `operate_id` bigint(18) NOT NULL COMMENT '操作用户id',
  `create_time` datetime NOT NULL,
  `delete_flag` int(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `supplier_prod_modify_limit_record`;
CREATE TABLE `supplier_prod_modify_limit_record` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `supplier_product_id` bigint(18) NOT NULL COMMENT '供应商id',
  `limit_money` double(18,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '供应商充值限制额度（以已发送充值请求来统计），单位分,-1表示设置为不需要限额',
  `operate_type` int(1) NOT NULL COMMENT '1：设置限额；2：取消限额；3：变更限制额度',
  `operate_id` bigint(18) NOT NULL COMMENT '操作用户id',
  `create_time` datetime NOT NULL,
  `delete_flag` int(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 对于限额的供应商和供应商产品每日充值统计表（以发送充值请求为统计依据）
DROP TABLE IF EXISTS `supplier_prod_req_use_per_day`;
CREATE TABLE `supplier_prod_req_use_per_day` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `supplier_product_id` bigint(18) NOT NULL COMMENT '供应商产品id',
  `use_money` double(18,2) NOT NULL DEFAULT '0.00' COMMENT '每天成功充值金额，单位分，（定时任务跑脚本插入数据库）',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `delete_flag` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `supplier_req_use_per_day`;
CREATE TABLE `supplier_req_use_per_day` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `supplier_id` bigint(18) NOT NULL COMMENT '供应商id',
  `used_money` double(18,2) NOT NULL DEFAULT '0.00' COMMENT '供应商每天发送充值请求的金额，单位分',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `delete_flag` int(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 供应商和供应商产品成功充值的总数
DROP TABLE IF EXISTS `supplier_prod_success_total_use`;
CREATE TABLE `supplier_prod_success_total_use` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `supplier_product_id` bigint(18) NOT NULL,
  `total_use` double(18,2) NOT NULL DEFAULT '0.00' COMMENT '供应商产品总成功充值金额，单位分(由定时任务跑脚本更新)，单位分',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `delete_flag` int(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `supplier_success_total_use`;
CREATE TABLE `supplier_success_total_use` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `supplier_id` bigint(18) NOT NULL COMMENT '供应商id',
  `total_use_money` double(18,2) NOT NULL DEFAULT '0.00' COMMENT '供应商总成功充值金额，单位分.（由定时任务脚本更新数值）',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `delete_flag` int(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 供应商和供应商产品每天充值记录表
DROP TABLE IF EXISTS `supplier_prod_success_use_per_day`;
CREATE TABLE `supplier_prod_success_use_per_day` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `supplier_product_id` bigint(18) NOT NULL COMMENT '供应商产品id',
  `use_money` double(18,2) NOT NULL DEFAULT '0.00' COMMENT '每天成功充值金额，单位分，（定时任务跑脚本插入数据库）',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `delete_flag` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `supplier_success_use_per_day`;
CREATE TABLE `supplier_success_use_per_day` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `supplier_id` bigint(18) NOT NULL COMMENT '供应商id',
  `used_money` double(18,2) NOT NULL DEFAULT '0.00' COMMENT '供应商每天成功充值金额，单位分,(由定时任务脚本插入该数值)',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `delete_flag` int(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  -- --------------------------------
-- 20170515 修改默认值，允许为null,罗祖武
-- --------------------------------  
ALTER TABLE `product`
MODIFY COLUMN `illustration`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '说明' AFTER `configurable_num_flag`;   

  -- --------------------------------
-- 20170516 用户变更流程相关表长度增加qh
-- --------------------------------   
ALTER TABLE `admin_change_detail`
MODIFY COLUMN `src_name`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '源姓名' AFTER `admin_id`,
MODIFY COLUMN `dest_name`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '目标姓名' AFTER `src_phone`;


ALTER TABLE `admin_change_operator`
MODIFY COLUMN `dest_name`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '目标姓名' AFTER `admin_id`;

-- --------------------------------
-- 20170522 sunyiwei  增加全局配置项,是否给企业关键人显示充值失败原因
-- --------------------------------
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`)
VALUES
	(null, '是否给企业关键人显示充值失败原因', '是否给企业关键人显示充值失败原因, YES(忽略大小写)为显示, 此项缺失或设置为其它为不显示',
	'SHOW_FAIL_REASON_TO_ENT_MANAGER', 'YES',
	NOW(), NOW(), 1, 1, 0, NULL);

-- ----------------------------------
-- 20170524 修改广东众筹、流量卡企业扩展信息表字段长度，wujiamin
-- ----------------------------------
ALTER TABLE `enterprises_ext_info`
MODIFY COLUMN `ec_code`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '集团编码' AFTER `enter_id`,
MODIFY COLUMN `ec_prd_code`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '集团产品号码' AFTER `ec_code`;

-- --------------------------------
-- 20170524 qh  云企信退款表重构
-- --------------------------------
DROP TABLE IF EXISTS `yqx_refund_record`;
CREATE TABLE `yqx_refund_record` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `done_code` varchar(255) NOT NULL COMMENT '支付平台返回的支付流水号',
  `reason` varchar(255) DEFAULT NULL COMMENT '退款原因',
  `refund_amount` double(10,2) DEFAULT NULL COMMENT '退款金额单位为元，为空时默认为支付金额 ',
  `refund_type` int(2) NOT NULL COMMENT '退款类型1.业务退款2.对账退款',
  `refund_serial_num` varchar(255) NOT NULL COMMENT '我们平台发起的退款流水号',
  `status` int(11) NOT NULL COMMENT '退款记录状态 0.待发送 1.受理成功 2.受理失败 3.退款成功 4.退款失败',
  `msg` varchar(100) DEFAULT NULL COMMENT '支付相关信息',
  `create_time` datetime NOT NULL COMMENT '退款创建时间',
  `result_return_time` datetime DEFAULT NULL COMMENT '支付平台返回退款结果的时间',
  `update_time` datetime DEFAULT NULL COMMENT '退款记录更新时间',
  PRIMARY KEY (`id`),
  KEY `doneCode_index` (`done_code`) USING BTREE,
  KEY `doneCode_status_index` (`done_code`,`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- -----------------------
-- 20170525 湖南BOSS免费流量池充值接口新增参数 by luozuwu
-- -----------------------
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES ('湖南BOSS免费流量池充值接口根据手机号码获取地市信息method', '湖南BOSS免费流量池充值接口根据手机号码获取地市信息method', 'BOSS_HUNAN_FREE_OF_DISCNT_METHOD', 'DQ_HQ_HNAN_PMphoneAddress', NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES ('湖南BOSS免费流量池充值接口选择变更标记', '湖南BOSS免费流量池充值接口选择变更标记', 'BOSS_HUNAN_FREE_OF_MODIFY_TAG', '0', NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES ('湖南BOSS免费流量池充值接口元素类型', '湖南BOSS免费流量池充值接口元素类型', 'BOSS_HUNAN_FREE_OF_ELEMENT_TYPE_CODE', 'D', NOW(), NOW(), '1', '1', '0', null);


-- --------------------------------
-- 20170525 luozuwu  湖南流量平台使用：话费充值接口，增加地市编码与资费编码之间的映射表
-- --------------------------------	
CREATE TABLE `city_fee` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键，自增ID',
  `city_code` varchar(32) DEFAULT NULL COMMENT '湖南地市编码',
  `pre_fee_code` varchar(32) DEFAULT NULL COMMENT '资费编码编码前导字符，最终资费编码：前导编码+资费编码后缀',
  `fee_code` varchar(32) DEFAULT NULL COMMENT '湖南资费编码后缀',
  `description` varchar(64) DEFAULT NULL COMMENT '描述信息',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `delete_flag` int(10) unsigned zerofill DEFAULT '0000000000' COMMENT '删除标记：0未删除；1已删除',
  PRIMARY KEY (`id`),
  KEY `index_city_code` (`city_code`,`fee_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
INSERT INTO `city_fee` VALUES ('309', '0731', '31', '537949', '赠送国内通用流量10MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('310', '0732', '32', '537949', '赠送国内通用流量10MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('311', '0733', '33', '537949', '赠送国内通用流量10MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('312', '0734', '34', '537949', '赠送国内通用流量10MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('313', '0735', '35', '537949', '赠送国内通用流量10MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('314', '0736', '36', '537949', '赠送国内通用流量10MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('315', '0737', '37', '537949', '赠送国内通用流量10MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('316', '0738', '38', '537949', '赠送国内通用流量10MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('317', '0739', '39', '537949', '赠送国内通用流量10MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('318', '0730', '30', '537949', '赠送国内通用流量10MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('319', '0743', '43', '537949', '赠送国内通用流量10MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('320', '0744', '44', '537949', '赠送国内通用流量10MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('321', '0745', '45', '537949', '赠送国内通用流量10MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('322', '0746', '46', '537949', '赠送国内通用流量10MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('323', '0731', '31', '537950', '赠送国内通用流量30MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('324', '0732', '32', '537950', '赠送国内通用流量30MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('325', '0733', '33', '537950', '赠送国内通用流量30MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('326', '0734', '34', '537950', '赠送国内通用流量30MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('327', '0735', '35', '537950', '赠送国内通用流量30MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('328', '0736', '36', '537950', '赠送国内通用流量30MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('329', '0737', '37', '537950', '赠送国内通用流量30MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('330', '0738', '38', '537950', '赠送国内通用流量30MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('331', '0739', '39', '537950', '赠送国内通用流量30MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('332', '0730', '30', '537950', '赠送国内通用流量30MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('333', '0743', '43', '537950', '赠送国内通用流量30MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('334', '0744', '44', '537950', '赠送国内通用流量30MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('335', '0745', '45', '537950', '赠送国内通用流量30MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('336', '0746', '46', '537950', '赠送国内通用流量30MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('337', '0731', '31', '537951', '赠送国内通用流量100MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('338', '0732', '32', '537951', '赠送国内通用流量100MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('339', '0733', '33', '537951', '赠送国内通用流量100MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('340', '0734', '34', '537951', '赠送国内通用流量100MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('341', '0735', '35', '537951', '赠送国内通用流量100MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('342', '0736', '36', '537951', '赠送国内通用流量100MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('343', '0737', '37', '537951', '赠送国内通用流量100MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('344', '0738', '38', '537951', '赠送国内通用流量100MB', '2017-05-25 16:54:51', '2017-05-25 16:54:51', '0000000000');
INSERT INTO `city_fee` VALUES ('345', '0739', '39', '537951', '赠送国内通用流量100MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('346', '0730', '30', '537951', '赠送国内通用流量100MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('347', '0743', '43', '537951', '赠送国内通用流量100MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('348', '0744', '44', '537951', '赠送国内通用流量100MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('349', '0745', '45', '537951', '赠送国内通用流量100MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('350', '0746', '46', '537951', '赠送国内通用流量100MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('351', '0731', '31', '537952', '赠送国内通用流量300MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('352', '0732', '32', '537952', '赠送国内通用流量300MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('353', '0733', '33', '537952', '赠送国内通用流量300MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('354', '0734', '34', '537952', '赠送国内通用流量300MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('355', '0735', '35', '537952', '赠送国内通用流量300MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('356', '0736', '36', '537952', '赠送国内通用流量300MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('357', '0737', '37', '537952', '赠送国内通用流量300MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('358', '0738', '38', '537952', '赠送国内通用流量300MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('359', '0739', '39', '537952', '赠送国内通用流量300MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('360', '0730', '30', '537952', '赠送国内通用流量300MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('361', '0743', '43', '537952', '赠送国内通用流量300MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('362', '0744', '44', '537952', '赠送国内通用流量300MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('363', '0745', '45', '537952', '赠送国内通用流量300MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('364', '0746', '46', '537952', '赠送国内通用流量300MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('365', '0731', '31', '537953', '赠送国内通用流量500MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('366', '0732', '32', '537953', '赠送国内通用流量500MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('367', '0733', '33', '537953', '赠送国内通用流量500MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('368', '0734', '34', '537953', '赠送国内通用流量500MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('369', '0735', '35', '537953', '赠送国内通用流量500MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('370', '0736', '36', '537953', '赠送国内通用流量500MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('371', '0737', '37', '537953', '赠送国内通用流量500MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('372', '0738', '38', '537953', '赠送国内通用流量500MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('373', '0739', '39', '537953', '赠送国内通用流量500MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('374', '0730', '30', '537953', '赠送国内通用流量500MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('375', '0743', '43', '537953', '赠送国内通用流量500MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('376', '0744', '44', '537953', '赠送国内通用流量500MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('377', '0745', '45', '537953', '赠送国内通用流量500MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('378', '0746', '46', '537953', '赠送国内通用流量500MB', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('379', '0731', '31', '537954', '赠送国内通用流量1G', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('380', '0732', '32', '537954', '赠送国内通用流量1G', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('381', '0733', '33', '537954', '赠送国内通用流量1G', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('382', '0734', '34', '537954', '赠送国内通用流量1G', '2017-05-25 16:54:52', '2017-05-25 16:54:52', '0000000000');
INSERT INTO `city_fee` VALUES ('383', '0735', '35', '537954', '赠送国内通用流量1G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('384', '0736', '36', '537954', '赠送国内通用流量1G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('385', '0737', '37', '537954', '赠送国内通用流量1G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('386', '0738', '38', '537954', '赠送国内通用流量1G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('387', '0739', '39', '537954', '赠送国内通用流量1G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('388', '0730', '30', '537954', '赠送国内通用流量1G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('389', '0743', '43', '537954', '赠送国内通用流量1G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('390', '0744', '44', '537954', '赠送国内通用流量1G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('391', '0745', '45', '537954', '赠送国内通用流量1G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('392', '0746', '46', '537954', '赠送国内通用流量1G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('393', '0731', '31', '537955', '赠送国内通用流量2G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('394', '0732', '32', '537955', '赠送国内通用流量2G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('395', '0733', '33', '537955', '赠送国内通用流量2G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('396', '0734', '34', '537955', '赠送国内通用流量2G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('397', '0735', '35', '537955', '赠送国内通用流量2G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('398', '0736', '36', '537955', '赠送国内通用流量2G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('399', '0737', '37', '537955', '赠送国内通用流量2G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('400', '0738', '38', '537955', '赠送国内通用流量2G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('401', '0739', '39', '537955', '赠送国内通用流量2G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('402', '0730', '30', '537955', '赠送国内通用流量2G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('403', '0743', '43', '537955', '赠送国内通用流量2G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('404', '0744', '44', '537955', '赠送国内通用流量2G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('405', '0745', '45', '537955', '赠送国内通用流量2G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('406', '0746', '46', '537955', '赠送国内通用流量2G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('407', '0731', '31', '537956', '赠送国内通用流量3G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('408', '0732', '32', '537956', '赠送国内通用流量3G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('409', '0733', '33', '537956', '赠送国内通用流量3G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('410', '0734', '34', '537956', '赠送国内通用流量3G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('411', '0735', '35', '537956', '赠送国内通用流量3G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('412', '0736', '36', '537956', '赠送国内通用流量3G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('413', '0737', '37', '537956', '赠送国内通用流量3G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('414', '0738', '38', '537956', '赠送国内通用流量3G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('415', '0739', '39', '537956', '赠送国内通用流量3G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('416', '0730', '30', '537956', '赠送国内通用流量3G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('417', '0743', '43', '537956', '赠送国内通用流量3G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('418', '0744', '44', '537956', '赠送国内通用流量3G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('419', '0745', '45', '537956', '赠送国内通用流量3G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('420', '0746', '46', '537956', '赠送国内通用流量3G', '2017-05-25 16:54:53', '2017-05-25 16:54:53', '0000000000');
INSERT INTO `city_fee` VALUES ('421', '0731', '31', '537957', '赠送国内通用流量4G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('422', '0732', '32', '537957', '赠送国内通用流量4G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('423', '0733', '33', '537957', '赠送国内通用流量4G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('424', '0734', '34', '537957', '赠送国内通用流量4G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('425', '0735', '35', '537957', '赠送国内通用流量4G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('426', '0736', '36', '537957', '赠送国内通用流量4G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('427', '0737', '37', '537957', '赠送国内通用流量4G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('428', '0738', '38', '537957', '赠送国内通用流量4G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('429', '0739', '39', '537957', '赠送国内通用流量4G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('430', '0730', '30', '537957', '赠送国内通用流量4G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('431', '0743', '43', '537957', '赠送国内通用流量4G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('432', '0744', '44', '537957', '赠送国内通用流量4G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('433', '0745', '45', '537957', '赠送国内通用流量4G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('434', '0746', '46', '537957', '赠送国内通用流量4G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('435', '0731', '31', '537958', '赠送国内通用流量6G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('436', '0732', '32', '537958', '赠送国内通用流量6G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('437', '0733', '33', '537958', '赠送国内通用流量6G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('438', '0734', '34', '537958', '赠送国内通用流量6G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('439', '0735', '35', '537958', '赠送国内通用流量6G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('440', '0736', '36', '537958', '赠送国内通用流量6G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('441', '0737', '37', '537958', '赠送国内通用流量6G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('442', '0738', '38', '537958', '赠送国内通用流量6G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('443', '0739', '39', '537958', '赠送国内通用流量6G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('444', '0730', '30', '537958', '赠送国内通用流量6G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('445', '0743', '43', '537958', '赠送国内通用流量6G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('446', '0744', '44', '537958', '赠送国内通用流量6G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('447', '0745', '45', '537958', '赠送国内通用流量6G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('448', '0746', '46', '537958', '赠送国内通用流量6G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('449', '0731', '31', '537959', '赠送国内通用流量11G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('450', '0732', '32', '537959', '赠送国内通用流量11G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('451', '0733', '33', '537959', '赠送国内通用流量11G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('452', '0734', '34', '537959', '赠送国内通用流量11G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('453', '0735', '35', '537959', '赠送国内通用流量11G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('454', '0736', '36', '537959', '赠送国内通用流量11G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('455', '0737', '37', '537959', '赠送国内通用流量11G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('456', '0738', '38', '537959', '赠送国内通用流量11G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('457', '0739', '39', '537959', '赠送国内通用流量11G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('458', '0730', '30', '537959', '赠送国内通用流量11G', '2017-05-25 16:54:54', '2017-05-25 16:54:54', '0000000000');
INSERT INTO `city_fee` VALUES ('459', '0743', '43', '537959', '赠送国内通用流量11G', '2017-05-25 16:54:55', '2017-05-25 16:54:55', '0000000000');
INSERT INTO `city_fee` VALUES ('460', '0744', '44', '537959', '赠送国内通用流量11G', '2017-05-25 16:54:55', '2017-05-25 16:54:55', '0000000000');
INSERT INTO `city_fee` VALUES ('461', '0745', '45', '537959', '赠送国内通用流量11G', '2017-05-25 16:54:55', '2017-05-25 16:54:55', '0000000000');
INSERT INTO `city_fee` VALUES ('462', '0746', '46', '537959', '赠送国内通用流量11G', '2017-05-25 16:54:55', '2017-05-25 16:54:55', '0000000000');



-- -------------------------------------
-- 20170527 增加云企信月末最后一天是否可以订购的的标识
-- -------------------------------------
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES ('云企信月末最后一天是否可以订购的标识', '云企信月末最后一天是否可以订购的标识（true-可以订购，false-不可以订购）', 'YQX_MONTH_LAST_DAY_ORDER', 'true', NOW(), NOW(), '1', '1', '0', null);


-- --------------------------------
-- 20170526 luozuwu  山东流量平台使用,订购接口增加操作说明字段:01订购；02删除操作；03变更操作；04暂停操作；05恢复操作
-- --------------------------------	
ALTER TABLE `supplier_product`
ADD COLUMN `op_type`  varchar(8) NULL DEFAULT '0' COMMENT '山东流量平台操作说明：01订购；02删除操作；03变更操作；04暂停操作；05恢复操作' AFTER `illustration`;

ALTER TABLE `supplier_product`
ADD COLUMN `op_status`  varchar(8) NULL COMMENT '山东流量平台操作发生后订单状态：0正常；1删除；2暂停' AFTER `op_type`;

ALTER TABLE `supplier_product`
MODIFY COLUMN `op_type`  varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '01' COMMENT '山东流量平台操作说明：01订购；02删除操作；03变更操作；04暂停操作；05恢复操作' AFTER `illustration`,
MODIFY COLUMN `op_status`  varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '操作发生后订单状态：0正常；1删除；2暂停' AFTER `op_type`;


-- --------------------------------------------
-- 20170531 供应商和供应商产品限额表结构变更 by qinqinyan 
-- --------------------------------------------
ALTER TABLE `supplier_success_use_per_day`
ADD COLUMN `date`  date NOT NULL AFTER `supplier_id`;
ALTER TABLE `supplier_success_use_per_day`
ADD UNIQUE INDEX `idx_supplier_date` (`supplier_id`, `date`) USING BTREE ;

ALTER TABLE `supplier_success_total_use`
ADD UNIQUE INDEX `idx_supplier_id` (`supplier_id`) USING BTREE ;


ALTER TABLE `supplier_prod_success_use_per_day`
ADD COLUMN `date`  date NOT NULL AFTER `supplier_product_id`;
ALTER TABLE `supplier_prod_success_use_per_day`
ADD UNIQUE INDEX `idx_supplier_product_date` (`supplier_product_id`, `date`) USING BTREE ;

ALTER TABLE `supplier_prod_success_total_use`
ADD UNIQUE INDEX `idx_supplier_product_id` (`supplier_product_id`) USING BTREE ;


-- -----------------------
-- 20170531 增加权限项 by qinqinyan
-- -----------------------
INSERT INTO `authority` VALUES (null, null, '供应商财务记录', 'ROLE_SUPPLIER_FINANCE', '102007', null, '2017-05-12 16:47:41', '1', '1', '2017-05-12 16:47:41', '0');
INSERT INTO `authority` VALUES (null, null, '供应商限额管理', 'ROLE_SUPPLIER_LIMIT_MANAGE', '102008', null, '2017-05-12 16:47:41', '1', '1', '2017-05-12 16:47:41', '0');


-- ------------------------------------------------
-- 20170602 增加mock的V网网龄配置，测试用，生产环境可以不添加，wujiamin
-- ------------------------------------------------
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES ('MOCK的V网入网时间', '测试用，V网入网时间（yyyyMMddHHmmss格式）', 'MOCK_VPMNTIME', '20170601120000', NOW(), NOW(), '1', '1', '0', null);


-- -----------------------------------------------
-- 20170605 众筹活动默认当前全部活动的用户列表校验方式为1，wujiamin
-- -----------------------------------------------
update crowdfunding_activity_detail set user_list=1 where user_list is NULL;

-- ------------------------------------------------
-- 20170606 增加广东众筹支付配置项，wujiamin
-- ------------------------------------------------
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES ('广东众筹支付数字摘要的密钥', '广东众筹支付数字摘要的密钥', 'GUANGDONG_ZHONGCHOU_PAY_SECRET', 'U2FsdGVkX18MBAY8UFe5IkRU', NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES ('广东众筹支付URL', '广东众筹支付URL', 'GUANGDONG_ZHONGCHOU_PAY_URL', 'http://adc.ewaytec2000.cn/NGADCABInterface/TrafficZC/PmpOnePhaseSubmit.aspx', NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES ('广东众筹支付完成页面通知地址', '广东众筹支付完成页面通知地址', 'GUANGDONG_ZHONGCHOU_PAY_BACK_URL', 'https://域名/web-in/gdzc/payBack.html', NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES ('广东众筹支付完成后台通知地址', '广东众筹支付完成后台通知地址', 'GUANGDONG_ZHONGCHOU_PAY_NOTIFY_URL', 'https://域名/web-in/gdzc/notify.html', NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES ('广东众筹支付结果查询URL', '广东众筹支付结果查询URL', 'GUANGDONG_ZHONGCHOU_GETPAYMENTORDER_URL', 'http://221.179.7.247/NGADCABInterface/TrafficZC/PmpServicesRec.aspx', NOW(), NOW(), '1', '1', '0', null);

INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES ('广东众筹大企业订购通知URL', '广东众筹大企业订购通知URL', 'GUANGDONG_ZHONGCHOU_LEC_ORDERRELATION_URL', 'http://221.179.7.247/NGADCABInterface/TrafficZC/PmpServicesRec.aspx', NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES ('广东众筹充值结果查询URL', '广东众筹充值结果查询URL', 'GUANGDONG_ZHONGCHOU_ORDERINFO_URL', 'http://221.179.7.247/NGADCABInterface/TrafficZC/PmpServicesRec.aspx', NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES ('广东众筹订购操作类型:0-开通', '广东众筹订购操作类型:0-开通', 'GUANGDONG_ZHONGCHOU_CHARGE_OPTTYPE', '0', NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES ('广东众筹订购生效方式', '广东众筹订购生效方式', 'GUANGDONG_ZHONGCHOU_CHARGE_EFFTYPE', '2', NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES ('广东众筹订购业务URL', '广东众筹订购业务URL', 'GUANGDONG_ZHONGCHOU_CHARGE_URL', 'http://221.179.7.247/NGADCABInterface/TrafficZC/PmpServicesRec.aspx', NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES ('广东众筹订购使用的月数, 8585和8584产品必填', '广东众筹订购使用的月数, 8585和8584产品必填', 'GUANGDONG_ZHONGCHOU_CHARGE_USECYCLE', '1', NOW(), NOW(), '1', '1', '0', null);
-- -----------------------------------
-- 20170607 广东众筹支付记录增加状态，wujiamin
-- -----------------------------------
ALTER TABLE `activity_payment_info`
MODIFY COLUMN `status`  int(1) NULL DEFAULT NULL COMMENT '支付状态。默认0：表示未支付；1：支付中；2：支付成功； 3：支付失败， 4，退款成功 5：退款失败：6未知异常 7等待付款, 8已取消, 9超时，10关闭' AFTER `refund_time`;

-- -----------------------------------
-- 20170607 增加操作监控时间
-- -----------------------------------
ALTER TABLE `supplier`
ADD COLUMN `limit_update_time` datetime NULL DEFAULT NULL COMMENT '操作时间，包括供应商限额或者供应商产品限额修改' 
AFTER `limit_money_flag`;

ALTER TABLE `supplier_finance_record`
ADD COLUMN `operate_time` datetime NULL DEFAULT NULL COMMENT '记录操作增加付款记录或者废弃付款记录的操作时间，这个是产品要求的' 
AFTER `delete_flag`;

-- -----------------------------------
-- 20170608 云企信支付对账前台及权限
-- -----------------------------------
ALTER TABLE `yqx_pay_record`
ADD COLUMN `reconcile_status`  int(2) NOT NULL DEFAULT 0 COMMENT '对账状态 0 未对账 1 对账成功 2 对账失败' AFTER `update_time`,
ADD COLUMN `reconcile_msg`  varchar(255) NULL DEFAULT '未对账' COMMENT '对账信息' AFTER `reconcile_status`;

INSERT INTO `authority` VALUES (null, null, '支付对账（云企信）', 'ROLE_YQX_PAYRECONCILE', '120001', null, NOW(), '1', '1', NOW(), '0');



-- -----------------------------------
-- 20170610 供应商产品增加字段 by qinqinyan for function v1.10.1
-- -----------------------------------
ALTER TABLE `supplier_product`
ADD COLUMN `type`  int(1) NULL DEFAULT 2 COMMENT '产品类型，0为现金产品，1为流量池产品，2为流量包产品，3为话费产品，4虚拟币产品' AFTER `limit_money_flag`;

-- -----------------------------------
-- 20170608 江苏boss
-- -----------------------------------
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES ('江苏boss渠道appid', '江苏boss渠道appid', 'JS_BOSS_APPID', '109000000067', NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES ('江苏boss渠道accessToken', '江苏boss渠道accessToken', 'JS_BOSS_ACCESS_TOKEN', 'ABCDE12345ABCDE12410', NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES ('江苏boss渠道operatorId', '江苏boss渠道operatorId', 'JS_BOSS_OPERATOR_ID', '17777704', NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES ('江苏boss渠道充值url', '江苏boss渠道充值url', 'JS_BOSS_CHARGE_URL', 'http://10.33.254.120:8002/BDS_ESB', NOW(), NOW(), '1', '1', '0', null);

-- -----------------------------------
-- 20170609 山东专用 山东流量平台互换code与phone字段 罗祖武 update enterprises as a, enterprises as b set a.code=b.phone, a.phone=b.code where a.id=b.id;
-- -----------------------------------

-- -----------------------------------
-- 20170609 山东专用 山东流量平台更换现金产品名称 罗祖武 update product set name = '现金产品' where name = 'sadadas';
-- -----------------------------------

-- -------------------
-- 20170610 变更供应商指纹的字段长度 by qinqinyan
-- -------------------
ALTER TABLE `supplier`
MODIFY COLUMN `fingerprint`  varchar(202) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL 
COMMENT '供应商的指纹特征，用于匹配相应的BossService，任何能够提供该指纹数据的BossService就被认为可以为这家供应商产品进行充值' AFTER `create_time`;

-- -------------------
-- 20170612 虚拟币最多赠送个数限制 by qinqinyan for v1.10.1
-- -------------------
INSERT INTO `global_config` VALUES (null, '虚拟币最多赠送个数限制', '单位个，用于普通赠送虚拟币最多赠送个数限制。如果不设置，则默认为999999,该功能只适用广东众筹平台，只有这个平台有个人虚拟账户', 'GIVE_VIRTUAL_COIN_MAX', '5000', '2017-06-11 16:14:25', '2017-06-11 16:14:25', '1', '1', '0', null);

-- -----------------------------------
-- 20170614 虚拟充值供应商，仅用于广东众筹平台 by qinqinyan for function v1.10.1
-- -----------------------------------
INSERT INTO `supplier` VALUES (null, '虚拟充值供应商', null, '杭研', 'hy', '2017-06-06 10:30:57', 'virtualcharge', '2017-06-06 10:30:57', '0', '0', '1', '', '', '-1.00', '0', null);
-- -----------------------------------
-- 201706013 新疆boss by qihang
-- -----------------------------------
update global_config set config_value = 'http://10.238.98.31:7001/oppf?' where config_key = 'BOSS_XJ_URL';
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES ('新疆boss渠道appid', '新疆boss渠道appid', 'BOSS_XJ_APPID', '510607', NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES ('新疆boss渠道appsecret', '新疆boss渠道appsecret', 'BOSS_XJ_APPSECRET', '61b8a845738bc9690910bb7e787f21b2', NOW(), NOW(), '1', '1', '0', null);


-- ----------------------------
-- 20170616 广东众筹更换手机号 by qinqinyan for v1.10.1
-- ----------------------------
-- Table structure for `change_mobile_record` 
DROP TABLE IF EXISTS `change_mobile_record`;
CREATE TABLE `change_mobile_record` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `admin_id` bigint(18) NOT NULL COMMENT '用户id',
  `old_mobile` varchar(12) NOT NULL COMMENT '旧手机号',
  `new_mobile` varchar(12) NOT NULL COMMENT '新手机号',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `delete_flag` int(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- 广东众筹全局变量配置
INSERT INTO `global_config` VALUES (null, '判断新手机号在微信侧是否已经被绑定', '判断新手机号在微信侧是否已经被绑定', 'IS_BINDED_URL_IN_WEIXIN', 'http://lljyz.4ggogo.com/wechat/api/checkMobile.html?newMobile=', '2017-06-15 14:16:58', '2017-06-15 14:34:44', '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '修改微信侧手机号url', '修改微信侧手机号url', 'MODIFY_MOBILE_URL_FOR_WEIXIN', 'http://lljyz.4ggogo.com/wechat/api/modifyMobile.html', '2017-06-15 14:19:19', '2017-06-15 14:35:10', '1', '1', '0', null);


-- -----------------------------------------
-- 20170621 增加活动记录对应的支付记录序列号,wujiamin
-- -----------------------------------------
ALTER TABLE `activity_win_record`
ADD COLUMN `pay_serial_num`  varchar(100) NULL COMMENT '对应的支付记录序列号' AFTER `wx_openid`;
-- 填充新增字段的值，广东众筹平台升级时执行一次即可
-- update activity_win_record w set w.pay_serial_num = (select sys_serial_num from activity_payment_info WHERE win_record_id=w.record_id ORDER BY charge_time DESC LIMIT 0,1);

-- -------------------------------------
-- 20170621 增加广东ADC查询url配置，wujiamin
-- -------------------------------------
INSERT INTO `global_config` VALUES (null, '广东众筹验证手机号码的URL', '广东众筹验证手机号码的URL', 'GUANGDONG_ZHONGCHOU_CHECK_URL', 'http://221.179.7.247/NGADCABInterface/TrafficZC/PmpServicesRec.aspx?svc_cat=GetTx&svc_code=CheckMobile', NOW(), NOW(), '1', '1', '0', null);


-- --------------------------------
-- 20170616 退款记录表增加退款操作者信息，wujiamin， v1.11.0
-- --------------------------------
ALTER TABLE `yqx_refund_record`
ADD COLUMN `operator_id`  bigint NULL COMMENT '退款操作者id' AFTER `reason`,
ADD COLUMN `operator_name`  varchar(255) NULL COMMENT '记录操作者当时的姓名，以防用户修改姓名' AFTER `operator_id`,
ADD COLUMN `operator_mobile`  varchar(11) NULL COMMENT '记录操作者当时的手机号，以防用户修改手机号' AFTER `operator_name`;

-- ----------------------------------
-- 20170616 退款列表字符长度修改为300，wujiamin，v1.11.0
-- ----------------------------------
ALTER TABLE `yqx_refund_record`
MODIFY COLUMN `reason`  varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退款原因' AFTER `done_code`;

-- ----------------------------------
-- 20170621 江西newboss linguangkuo
-- ----------------------------------
update global_config set config_value='CC933BE6EDC6CF9C' where config_key='BOSS_JIANGXI_APPSECRET';
update global_config set config_value='ekndnuRS' where config_key='BOSS_JIANGXI_SERVICEID';
update global_config set config_value='256' where config_key='BOSS_JIANGXI_ECCODE';
INSERT INTO `global_config` VALUES (null, '江西供应商id', '江西供应商id', 'BOSS_JIANGXI_SUPPLIERID', 'plus线上id', '2017-06-15 14:19:19', '2017-06-15 14:35:10', '1', '1', '0', null);

-- ----------------------------------
-- 20170621 云企信对账 qihang
-- ----------------------------------
INSERT INTO `global_config` VALUES (null, '云企信是否使用对账', '云企信是否使用对账,true为使用,清缓存重启tomcat后生效', 'YQX_RECONCILE_IS_USE', 'false',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '云企信是否测试服使用', '云企信是否测试服使用,true为使用，其它为生产环境使用，优先级在YQX_RECONCILE_IS_USE之后,清缓存重启tomcat后生效', 'YQX_RECONCILE_IS_TEST', 'false',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '云企信对账ftp的url', '云企信对账ftp的url', 'YQX_RECONCILE_FTP_URL', '127.0.0.1',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '云企信对账ftp的port', '云企信对账ftp的port', 'YQX_RECONCILE_FTP_PORT', '21',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '云企信对账ftp的username', '云企信对账ftp的username', 'YQX_RECONCILE_FTP_LOGINNAME', 'qihang',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '云企信对账ftp的pass', '云企信对账ftp的pass', 'YQX_RECONCILE_FTP_LOGINPASS', 'xiaoqi160',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '云企信对账ftp的本地账单保存路径', '云企信对账ftp的本地账单保存路径', 'YQX_RECONCILE_FTP_LOCALPATH', '/usr/share/tomcat/yqxpay/bill',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '云企信对账ftp的目标文件保存路径', '云企信对账ftp的目标文件保存路径', 'YQX_RECONCILE_FTP_TARGETPATH', '/usr/share/tomcat/yqxpay/target',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, 'email服务器', 'email服务器', 'YQX_RECONCILE_EMAIL_HOST', 'smtp.chinamobile.com',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, 'email端口', 'email端口', 'YQX_RECONCILE_EMAIL_PORT', '25',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, 'email发送邮箱', 'email发送邮箱', 'YQX_RECONCILE_EMAIL_SENDER', 'luozuwu@cmhi.chinamobile.com',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, 'email接收邮箱', 'email接收邮箱', 'YQX_RECONCILE_EMAIL_RECEIVER', 'luozuwu@cmhi.chinamobile.com',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, 'email用户名', 'email用户名', 'YQX_RECONCILE_EMAIL_USERNAME', 'luozuwu',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, 'email用户密码', 'email用户密码', 'YQX_RECONCILE_EMAIL_PASS', 'luozuwu123',NOW(), NOW(), '1', '1', '0', null);

-- ----------------------------------
-- 20170621 江苏对账相关配置 qihang
-- ----------------------------------
INSERT INTO `global_config` VALUES (null, '江苏是否使用对账', '江苏是否使用对账,true为使用,清缓存重启tomcat后生效', 'JS_RECONCILE_IS_USE', 'false',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '江苏是否测试服使用', '江苏是否测试服使用,true为使用，其它为生产环境使用，优先级在JS_RECONCILE_IS_USE之后,清缓存重启tomcat后生效', 'JS_RECONCILE_IS_TEST', 'false',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '江苏对账ftp的url', '江苏对账ftp的url', 'JS_RECONCILE_FTP_URL', '127.0.0.1',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '江苏对账ftp的port', '江苏对账ftp的port', 'JS_RECONCILE_FTP_PORT', '21',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '江苏对账ftp的username', '江苏对账ftp的username', 'JS_RECONCILE_FTP_LOGINNAME', 'qihang',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '江苏对账ftp的pass', '江苏对账ftp的pass', 'JS_RECONCILE_FTP_LOGINPASS', 'xiaoqi160',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '江苏对账ftp的远程路径', '江苏对账ftp的远程路径', 'JS_RECONCILE_FTP_REMOTEPATH', 'aaa',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '江苏对账ftp的本地账单保存路径', '江苏对账ftp的本地账单保存路径', 'JS_RECONCILE_FTP_LOCALPATH', '/usr/share/tomcat/jspay/bill',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '江苏对账ftp的目标文件保存路径', '江苏对账ftp的目标文件保存路径', 'JS_RECONCILE_FTP_TARGETPATH', '/usr/share/tomcat/jspay/target',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '江苏email服务器', '江苏email服务器', 'JS_RECONCILE_EMAIL_HOST', 'smtp.chinamobile.com',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '江苏email端口', '江苏email端口', 'JS_RECONCILE_EMAIL_PORT', '25',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '江苏email发送邮箱', '江苏email发送邮箱', 'JS_RECONCILE_EMAIL_SENDER', 'luozuwu@cmhi.chinamobile.com',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '江苏email接收邮箱', '江苏email接收邮箱', 'JS_RECONCILE_EMAIL_RECEIVER', 'luozuwu@cmhi.chinamobile.com',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '江苏email用户名', '江苏email用户名', 'JS_RECONCILE_EMAIL_USERNAME', 'luozuwu',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '江苏email用户密码', '江苏email用户密码', 'JS_RECONCILE_EMAIL_PASS', 'luozuwu123',NOW(), NOW(), '1', '1', '0', null);

-- -----------------------------------
-- 201706016 上海订购组 by linguangkuo
-- -----------------------------------
INSERT INTO `authority` VALUES (null, null, '订购组列表', 'ROLE_PRODUCT_ORDER_LIST', '102009', null, NOW(), '1', '1', NOW(), '0');
INSERT INTO `authority` VALUES (null, null, '企业订购组', 'ROLE_ENT_PRODUCT_ORDER_LIST', '102010', null, NOW(), '1', '1', NOW(), '0');

DROP TABLE IF EXISTS `sh_order_list`;
CREATE TABLE `sh_order_list` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `enter_id` bigint(18) NOT NULL COMMENT '企业id',
  `main_bill_id` varchar(64) NOT NULL COMMENT '订购组计费号',
  `order_name` varchar(64) NOT NULL,
  `offer_id` varchar(64) NOT NULL COMMENT '策划编号',
  `role_id` varchar(64) NOT NULL COMMENT '角色编号',
  `acc_id` varchar(64) NOT NULL COMMENT '账户编号',
  `order_type` varchar(16) NOT NULL COMMENT '订购类型01全网订购，02本地订购',
  `count` double(18,2) NOT NULL DEFAULT '0.00' COMMENT '订购组余额',
  `alert_count` double(18,2) NOT NULL DEFAULT '0.00' COMMENT '预警值',
  `stop_count` double(18,2) NOT NULL DEFAULT '0.00' COMMENT '暂停值',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_flag` int(1) NOT NULL DEFAULT '0' COMMENT '删除标记， 0:未删除；1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sh_order_product_map`;
CREATE TABLE `sh_order_product_map` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `product_id` bigint(18) NOT NULL COMMENT '平台产品ID',
  `order_list_id` bigint(18) NOT NULL COMMENT 'd订购组列表id',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_flag` int(1) NOT NULL DEFAULT '0' COMMENT '删除标记， 0:未删除；1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- -------------------
-- 20170623 上海流量券增加了渠道字段 by qinqinyan for function v1.11.0
-- -------------------
ALTER TABLE `activity_win_record`
ADD COLUMN `channel` varchar(100) NULL DEFAULT NULL COMMENT '充值渠道，目前用于上海流量券' 
AFTER `pay_serial_num`;

-- -------------------------------
-- 20170623 云企信订购表增加用户是否提交过退款申请的标记，wujiamin
-- -------------------------------
ALTER TABLE `yqx_order_record`
ADD COLUMN `approval_refund`  int NULL DEFAULT 0 COMMENT '用户是否提交过退款申请：0-没有提交过申请，1-提交过退款申请' AFTER `pay_transaction_id`;
-- --增加该字段后，历史数据更新
update yqx_order_record set approval_refund=1 where refund_status!=0;

-- -----------------
-- 20170627 统计参加营销活动访问人次，中奖人次，玩人次 by qinqinyan for function v1.11.0
-- -----------------
-- 增加字段
ALTER TABLE `activity_info`
ADD COLUMN `visit_count` bigint(18) NULL DEFAULT 0 COMMENT '访问游戏次数' 
AFTER `used_product_size`;
ALTER TABLE `activity_info`
ADD COLUMN `play_count` bigint(18) NULL DEFAULT 0 COMMENT '玩游戏人次' 
AFTER `visit_count`;
ALTER TABLE `activity_info`
MODIFY COLUMN `gived_user_count`  bigint(18) NULL DEFAULT 0 COMMENT '中奖人次。(随机红包需求定义)已发数量：已发数量为一共有多少用户抽奖。（开发理解）已发用户数量（一个红包活动只能中奖一次，实质就是已发奖品数量）'
AFTER `url`;

-- 增加权限项
INSERT INTO `global_config` VALUES (null, '向营销模板请求营销数据url', '向营销模板请求营销数据url', 'ACTIVITY_GET_DATA_URL', 'http://activitytest.4ggogo.com/lottery/data/getCount.html', '2017-06-27 08:46:07', '2017-06-27 08:46:07', '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '删除营销模板侧运营数据url', '删除营销模板侧运营数据url', 'ACTIVITY_DEl_DATA_URL', 'http://activitytest.4ggogo.com/lottery/data/delCount.html', '2017-06-27 08:47:44', '2017-06-27 08:47:44', '1', '1', '0', null);

-- -------------------------------
-- 20170628 上海产品 linguangkuo
-- -------------------------------
ALTER TABLE `sh_boss_product`
ADD COLUMN `supplier_product_name`  varchar(255)  NOT NULL COMMENT '供应商产品名称' AFTER `supplier_product_id`,
ADD COLUMN `order_type`  varchar(255) NOT NULL COMMENT '订购类型01全网订购，02本地订购' AFTER `supplier_product_name`;

-- --------------------
-- 20170628 增加云企信账单表,qh
-- --------------------
CREATE TABLE `yqx_pay_bill` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `date` varchar(20) NOT NULL COMMENT '日期',
  `originId` varchar(255) DEFAULT NULL,
  `merchantId` varchar(255) DEFAULT NULL,
  `productId` varchar(255) DEFAULT NULL,
  `orderid` varchar(255) DEFAULT NULL,
  `payAmount` varchar(255) DEFAULT NULL,
  `tradeNo` varchar(255) DEFAULT NULL,
  `thirdOrderid` varchar(255) DEFAULT NULL,
  `transactionId` varchar(255) DEFAULT NULL,
  `transactionDate` varchar(255) DEFAULT NULL,
  `payInfo` varchar(255) DEFAULT NULL,
  `accountCode` varchar(255) DEFAULT NULL,
  `accountType` varchar(255) DEFAULT NULL,
  `payPeriod` varchar(255) DEFAULT NULL,
  `payTime` varchar(255) DEFAULT NULL,
  `proCount` varchar(255) DEFAULT NULL,
  `productName` varchar(255) DEFAULT NULL,
  `orderType` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `thirdAmount` varchar(255) DEFAULT NULL,
  `integralNum` varchar(255) DEFAULT NULL,
  `hemiNum` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- --------------------
-- 20170703 短信黑名单,linguangkuo
-- --------------------
INSERT INTO `authority` VALUES (null, null, '短信黑名单', 'ROLE_SMS_BLACK_LIST', '109011', null, NOW(), '1', '1', NOW(), '0');
DROP TABLE IF EXISTS `mobile_black_list`;
CREATE TABLE `mobile_black_list` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `mobile` varchar(11) NOT NULL,
  `type` varchar(50) NOT NULL COMMENT '1、开户审批；2、Ec审批；3、平台短信验证码；4、充值到账；5、预警提醒；6、暂停值提醒；7、营销活动；8、初始化静态密码',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_flag` int(1) NOT NULL DEFAULT '0' COMMENT '删除标记， 0:未删除；1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;


-- --------------------
-- 20170703 充值记录增加供应商价格,linguangkuo
-- --------------------
ALTER TABLE `charge_record`
ADD COLUMN `supplier_product_price` bigint(7)  COMMENT '供应商产品价格' DEFAULT '0' AFTER supplier_product_id;

-- -------------------------------
-- 20170630 上海接口  linguangkuo
-- -------------------------------
INSERT INTO `global_config` VALUES (null, '上海boss企业编码', '上海boss企业编码', 'SH_BOSS_ENTER_CODE', 'xxxxx', '2017-06-27 08:46:07', '2017-06-27 08:46:07', '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '上海全网供应商id', '上海全网供应商id', 'SH_OLD_SUPPLIER_ID', 'xxxxx', '2017-06-27 08:46:07', '2017-06-27 08:46:07', '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '上海本地供应商id', '上海本地供应商id', 'SH_NEW_SUPPLIER_ID', 'xxxxx', '2017-06-27 08:46:07', '2017-06-27 08:46:07', '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '上海boss预警值', '上海boss预警值', 'SH_BOSS_ALERT_COUNT', '0', '2017-06-27 08:46:07', '2017-06-27 08:46:07', '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '上海新boss渠道APPCODE', '上海新boss渠道APPCODE', 'BOSS_SHANGHAI_NEW_APPCODE', 'A0002019', '2017-06-27 08:46:07', '2017-06-27 08:46:07', '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '上海新boss渠道APK', '上海新boss渠道APK', 'BOSS_SHANGHAI_NEW_APK', 'hgH7terOSW5uYQvHUY/jrXZKcAjsyF3q', '2017-06-27 08:46:07', '2017-06-27 08:46:07', '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '上海新boss渠道securityUrl', '上海新boss渠道securityUrl', 'BOSS_SHANGHAI_NEW_SECURITY_URL', 'http://211.136.164.123/open/security', '2017-06-27 08:46:07', '2017-06-27 08:46:07', '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '上海新boss渠道openapiUrl', '上海新boss渠道openapiUrl', 'BOSS_SHANGHAI_NEW_OPENAPI_URL', 'http://211.136.164.123/open/service', '2017-06-27 08:46:07', '2017-06-27 08:46:07', '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '上海boss是否需要mock', '上海boss是否需要mock', 'BOSS_SHANGHAI_NEED_MOCK', 'false', '2017-06-27 08:46:07', '2017-06-27 08:46:07', '1', '1', '0', null);

-- -----------------------------
-- 20170703 云企信支付记录增加充值状态，充值时间， wujiamin
-- -----------------------------
ALTER TABLE `yqx_pay_record`
ADD COLUMN `charge_status`  int(2) NULL DEFAULT NULL COMMENT '1-待充值；2-已发送充值请求；3-充值成功；4-充值失败' AFTER `reconcile_msg`,
ADD COLUMN `charge_time`  datetime NULL COMMENT '充值时间，云企信向平台发起EC充值的时间' AFTER `charge_status`;
-- 云企信平台（四川、重庆）更新V1.11.0版本时需要更新历史支付记录的充值状态和充值时间（只需升级时执行一次）
-- update yqx_pay_record a left join interface_record b on a.pay_transaction_id=b.serial_num set a.charge_status=b.status, a.charge_time=b.create_time;

-- --------------------------
-- 20170706 广东众筹新签到 by qinqinyan 
-- --------------------------
-- 统计连续签到记录表
DROP TABLE IF EXISTS `wx_serial_sign_record`;
CREATE TABLE `wx_serial_sign_record` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `admin_id` bigint(18) NOT NULL COMMENT '签到用户id',
  `start_time` datetime NOT NULL COMMENT '此次连续签到的起始日期',
  `count` int(5) NOT NULL COMMENT '连续签到天数',
  `update_time` datetime NOT NULL,
  `delete_flag` int(1) NOT NULL,
  `serial_flag` int(1) NOT NULL DEFAULT '0' COMMENT '0：连续；1：不连续',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 每天签到详细记录表
DROP TABLE IF EXISTS `wx_sign_detail_record`;
CREATE TABLE `wx_sign_detail_record` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `admin_id` bigint(18) NOT NULL COMMENT '签到用户id',
  `sign_time` datetime NOT NULL COMMENT '签到时间',
  `coin_count` int(5) NOT NULL COMMENT '此次签到活动的流量币',
  `serail_sign_id` bigint(18) NOT NULL COMMENT '此次签到对应的连续签到记录的id',
  `update_time` datetime NOT NULL,
  `delete_flag` int(1) NOT NULL,
  `serial_num` varchar(255) NOT NULL COMMENT '流水号',
  `type` int(1) NOT NULL DEFAULT '0' COMMENT '0：签到；1：签到奖励',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 全局变量配置
INSERT INTO `global_config` VALUES (null, '广东众筹禁止签到的起始时间', '广东众筹禁止签到的起始时间', 'START_TIME_OF_FORBID_TO_SIGN_IN', '00:00:00', '2017-07-05 09:47:04', '2017-07-05 09:47:04', '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '广东众筹禁止签到的结束始时间', '广东众筹禁止签到的结束始时间', 'END_TIME_OF_FORBID_TO_SIGN_IN', '07:00:00', '2017-07-05 09:47:51', '2017-07-05 11:05:43', '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '超过最大连续签到次数则不赠送流量币', '超过最大连续签到次数则不赠送流量币', 'MAX_SERIAL_SIGN_DAY', '21', '2017-07-05 09:48:24', '2017-07-06 14:14:39', '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '每天签到赠送流量币个数', '每天签到赠送流量币个数', 'SIGN_GIVE_COUNT_PER_DAY', '1', '2017-07-05 09:49:13', '2017-07-05 09:49:13', '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '连续签到达到指定天数后奖励流量币个数', '连续签到达到指定天数后奖励流量币个数', 'SIGN_AWARD_COUNT', '10', '2017-07-05 09:50:07', '2017-07-06 11:44:06', '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '每天签到能获得流量币最大签到人数', '每天签到能获得流量币最大签到人数', 'SIGN_MAX_COUNT_PER_DAY', '10000', '2017-07-05 09:50:53', '2017-07-05 09:50:53', '1', '1', '0', null);

-- ec短信通知
INSERT INTO `global_config` VALUES (null, 'ec审批通知', 'ec审批通知', 'EC_APPROVAL_NOTICE', 'false', '2017-07-05 09:50:53', '2017-07-05 09:50:53', '1', '1', '0', null);


-- -------------------------------
-- 20170707 开放平台短信接口  qihang
-- -------------------------------
INSERT INTO `global_config` VALUES (null, '开放平台短信APIKEY', '开放平台短信APIKEY', 'OPEN_PLATFORM_SMS_APIKEY', '9b3a7827ece947238efdf1f08d98c4dd',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '开放平台短信APPSECRET', '开放平台短信APPSECRET', 'OPEN_PLATFORM_SMS_APPSECRET', '2124e16970354338a94c4492d4ecf534',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '开放平台短信短信内容签名', '开放平台短信短信内容签名', 'OPEN_PLATFORM_SMS_MESSAGESIGN', '和力云平台',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '开放平台短信地址', '开放平台短信地址', 'OPEN_PLATFORM_SMS_SENDURL', 'http://www.cmccheli.com/api/v1/sms/send',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '开放平台短信模板Id', '开放平台短信模板Id', 'OPEN_PLATFORM_SMS_TEMPLATEID', '29',NOW(), NOW(), '1', '1', '0', null);

-- -------------------------------
-- 20170712 云企信对账增加字段
-- -------------------------------
ALTER TABLE `yqx_pay_bill`
ADD COLUMN `subOrderid`  varchar(255) NULL AFTER `create_time`,
ADD COLUMN `payOrderid`  varchar(255) NULL AFTER `subOrderid`,
ADD COLUMN `subPayOrderid`  varchar(255) NULL AFTER `payOrderid`;

-- ------------------------
-- 20170716 V1.11.0缺少的sql补充，author wuguoping， added by wujiamin
-- ------------------------
ALTER TABLE `account_change_detail`
MODIFY COLUMN `count`  double(18,2) NOT NULL AFTER `account_id`;

ALTER TABLE `account_change_operator`
MODIFY COLUMN `count`  double(18,2) NOT NULL AFTER `prd_id`;

-- -------------------------------
-- 20170714 企业充值支持两位小数  wuguoping
-- -------------------------------
ALTER TABLE `account_change_detail` MODIFY COLUMN `count`  double(18,2) NOT NULL AFTER `account_id`;
ALTER TABLE `account_change_operator` MODIFY COLUMN `count`  double(18,2) NOT NULL AFTER `prd_id`;


-- ----------------------------
-- v1.11.1 向公众号请求手机号功能  by qinqinyan on 2017/07/17 
-- 仅广东众筹需要这两个
-- ----------------------------
INSERT INTO `global_config` VALUES (null, '营销活动向公众号请求用户手机号标志位', '营销活动向公众号请求用户手机号标志位，广东众筹平台，标志位设置为true', 'ACTIVITY_GET_MOBILE_FROM_WX_FLAG', 'false', '2017-07-17 09:14:08', '2017-07-17 09:14:08', '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '营销活动向公众号请求用户手机号URL', '营销活动向公众号请求用户手机号URL', 'ACTIVITY_GET_MOBILE_FROM_WX_URL', 'https://lljyz.4ggogo.com/wechat/async/getByOpenid.html', '2017-07-17 09:15:28', '2017-07-17 13:47:01', '1', '1', '0', null);

-- -------------------
-- 流量券中奖纪录查询异常:原因是字段编码设置不对，这里是修正sql by qinqinyan on 2017/07/24
-- -------------------
ALTER TABLE activity_win_record CHANGE own_mobile own_mobile VARCHAR(11) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL 
COMMENT '中奖用户（砸金蛋、红包、大转盘）、赠送用户（流量券，二维码）';
ALTER TABLE activity_win_record CHANGE charge_mobile charge_mobile VARCHAR(11) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL 
COMMENT '实际充值用户（大转盘、砸金蛋、红包与own_mobile相同；流量卡券存在赠送，）';



-- --------------------------------------
-- activity_win_record表增加索引, 20170725, wujiamin
-- --------------------------------------
ALTER TABLE activity_win_record ADD INDEX idx_own_mobile(own_mobile);
ALTER TABLE activity_win_record ADD INDEX idx_charge_mobile(charge_mobile);

-- ----------------------------
-- ----------------------------
-- 重构包月赠送记录表，luozuwu 20170718, 注意：如果之前有包月赠送记录的请备份
-- ----------------------------
DROP TABLE IF EXISTS `monthly_present_record`;
CREATE TABLE `monthly_present_record` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '标识',
  `rule_id` bigint(18) NOT NULL COMMENT '规则标识',
  `prd_id` bigint(25) NOT NULL,
  `mobile` varchar(15) NOT NULL COMMENT '手机号码',
  `status` tinyint(4) NOT NULL COMMENT '充值状态',
  `effect_type` int(2) DEFAULT '1' COMMENT '生效方式，立即生效1，次月生效2',
  `status_code` varchar(50) DEFAULT NULL COMMENT '状态码',
  `error_message` varchar(200) DEFAULT NULL COMMENT '充值失败信息',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  `sys_serial_num` varchar(50) DEFAULT NULL COMMENT '系统流水号',
  `boss_serial_num` varchar(50) DEFAULT NULL COMMENT 'BOSS流水号',
  PRIMARY KEY (`id`),
  KEY `rule_status` (`rule_id`,`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- 重构包月赠送规则表，luozuwu 20170718，注意：如果之前有包月赠送记录的请备份
-- ----------------------------
DROP TABLE IF EXISTS `monthly_present_rule`;
CREATE TABLE `monthly_present_rule` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '标识',
  `ent_id` bigint(18) NOT NULL COMMENT '企业标识',
  `total` int(18) NOT NULL COMMENT '被赠送人总数',
  `status` int(18) NOT NULL COMMENT '状态',
  `month_count` int(18) NOT NULL DEFAULT '1' COMMENT '赠送总月数',
  `start_time` datetime NOT NULL COMMENT '赠送开始时间',
  `end_time` datetime NOT NULL COMMENT '赠送结束时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `creator_id` bigint(18) NOT NULL COMMENT '创建者标识',
  `updater_id` bigint(18) NOT NULL COMMENT '最近一次修改者标识',
  `delete_flag` int(18) NOT NULL COMMENT '删除标记，0未删除， 1已删除',
  `version` int(18) DEFAULT NULL COMMENT '更新版本号',
  `use_sms` int(18) DEFAULT '0' COMMENT '是否使用短信模板：0不使用；1使用',
  `sms_template_id` bigint(18) DEFAULT NULL COMMENT '短信模板ID',
  `activity_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- -----------------------------
-- 20170719 充值记录表增加count字段 qh
-- -----------------------------
ALTER TABLE `charge_record`
ADD COLUMN `count`  int(10) NOT NULL DEFAULT 1 COMMENT '赠送的个数，默认值所有功能为1，山东包月需要填写个数' AFTER `effect_type`;

-- -----------------------------
-- 20170720 企业黑名单     wuguoping
-- -----------------------------
DROP TABLE IF EXISTS `enterprise_blacklist`;
CREATE TABLE `enterprise_blacklist` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `enterprise_name` varchar(64) NOT NULL COMMENT '企业名称',
  `key_name` varchar(64) NOT NULL COMMENT '关键词名称',
  `creator_id` bigint(18) NOT NULL COMMENT '创建者id',
  `creater_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '关键词更新时间',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

-- -----------------------------
-- 20170720 企业黑名单     wuguoping
-- 目前重庆需要
-- -----------------------------
INSERT INTO `authority` VALUES
  (NULL, NULL, '企业黑名单', 'ROLE_ENTERPRISE_BLACKLIST', '100086', NULL, '2017-07-13 14:17:32', NULL, NULL,
         '2017-07-13 14:26:48', '0');
         

-- -------------
-- v1.12.0 上海流量券短信模板，其他平台不要插入该条数据  by qinqinyan on 2017/07/24        
-- --------------  
INSERT INTO `sms_template` VALUES (null, '流量券短信模板', '您好!{0}向您赠送{1}流量券{2},请到上海移动政企微厅微信公众号或和教授app http://dx.10086.cn/profZQ 查收,谢谢!', '2017-07-24 15:50:11', '1', '1');       


-- -----------------
-- 生成活动接口采用新接口（新接口于就接口区别：字段校验更加严格，平台是1.5版本之前的都采用老接口，即不执行该条语句） by qinqinyan on 2017/07/24
-- ----------------
UPDATE `global_config` SET config_value = 'http://activity.4ggogo.com/template/service/autoGenerateNew/make',
 update_time = NOW() WHERE config_key = 'ACTIVITY_GENERATE_URL' AND delete_flag = 0;

-- --------------------------
-- 优化活动鉴权配置项 by qinqinyan on 2017/07/26
-- --------------------------
UPDATE global_config SET config_value = 'http://wxexp.4ggogo.com/auth/wxAuth/pre.html?enterpriseId=' WHERE config_key = 'ACTIVITY_AUTHORIZED_URL';
UPDATE global_config SET config_value = 'http://wxthird.4ggogo.com/cgi-bin/component/is_authorized?enterprise_access_token=' WHERE config_key = 'ACTIVITY_IS_AUTHORIZED_URL';

-- -----------------------------
-- 20170802 广东单点登录配置 ,请根据实际情况配置地址    qihang
-- -----------------------------
INSERT INTO `global_config` VALUES (null, '广东是否使用单点登录', '广东是否使用单点登录,true为使用,请同时检查province_flag是否为广东环境', 'GD_SSO_IS_USE', 'true',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '广东单点登录检测token的地址', '广东单点登录检测token的地址', 'GD_SSO_CHECKTOKEN_URL', 'http://localhost:8090/gdsso/auth/checktoken.html',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '广东sso首页的url', '广东sso首页的url', 'GD_SSO_FIRSTPAGE_URL', 'http://localhost:8090/gdsso/user/main.html',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '广东本机登录的url', '广东部署流量平台，流量卡平台的url，请只替换到j_spring_security_check前为止', 'GD_SSO_LOGAL_LOGIN_URL', 'http://localhost:8080/web-in/j_spring_security_check',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '广东单点登录与登录平台共用加密key', '广东单点登录与登录平台共用加密key,16位长度', 'GD_SSO_PUBLIC_KEY', 'gdllpt_share_aes',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '广东单点登录加密登录地址使用私钥', '广东单点登录加密登录地址使用私钥,16位长度', 'GD_SSO_PRIVATE_KEY', 'login_privatekey',NOW(), NOW(), '1', '1', '0', null);

-- --------------
-- v1.12.1 营销模板改造 by qinqinyan on 2017/08/07
-- --------------
DROP TABLE IF EXISTS `mdrc_bacth_config_info`;
CREATE TABLE `mdrc_bacth_config_info` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `is_free` int(1) DEFAULT '0' COMMENT '是否减免费用。0：不减肥；1：减免',
  `name` varchar(64) DEFAULT NULL COMMENT '收件人姓名',
  `mobile` varchar(11) DEFAULT NULL COMMENT '收件人手机号',
  `address` varchar(300) DEFAULT NULL COMMENT '收件人详细地址',
  `postcode` varchar(6) DEFAULT NULL COMMENT '邮编',
  `certificate_key` varchar(300) DEFAULT NULL COMMENT '向s3获取缴费凭证的key',
  `certificate_name` varchar(300) DEFAULT NULL COMMENT '缴费凭证图片的名字',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_flag` int(1) DEFAULT NULL,
  `qrcode_key` varchar(300) DEFAULT NULL COMMENT '企业宣传二维码key',
  `qrcode_name` varchar(300) DEFAULT NULL COMMENT '企业宣传二维码名称',
  `customer_service_phone` varchar(20) DEFAULT NULL COMMENT '客服电话',
  `template_front_key` varchar(300) DEFAULT NULL COMMENT '自定义模板正面key',
  `template_front_name` varchar(300) DEFAULT NULL COMMENT '自定义模板正面名称',
  `template_back_key` varchar(300) DEFAULT NULL COMMENT '自定义模板反面key',
  `template_back_name` varchar(300) DEFAULT NULL COMMENT '自定义模板反面名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

ALTER TABLE `mdrc_cardmake_detail`
ADD COLUMN `enterprise_id`  bigint(18) NULL AFTER `cardmake_status`,
ADD COLUMN `product_id`  bigint(18) NULL AFTER `enterprise_id`,
ADD COLUMN `config_info_id`  bigint(18) NULL AFTER `product_id`;

ALTER TABLE `mdrc_batch_config`
ADD COLUMN `enterprise_id`  bigint(18) NULL AFTER `noti_time`,
ADD COLUMN `product_id`  bigint(18) NULL AFTER `enterprise_id`,
ADD COLUMN `config_info_id`  bigint(18) NULL AFTER `product_id`;


-- --------------
-- v1.12.1 营销模板改造 by qinqinyan on 2017/08/09
-- --------------
ALTER TABLE `mdrc_cardmake_detail`
ADD COLUMN `file_name`  varchar(600) NULL COMMENT '待下载文件的文件名' AFTER `config_info_id`;

ALTER TABLE `mdrc_batch_config`
MODIFY COLUMN `config_name`  varchar(64) NULL default null AFTER `id`;

-- --------------
-- v1.12.1 营销模板改造 by qinqinyan on 2017/08/09
-- --------------
ALTER TABLE `mdrc_template`
ADD COLUMN `type`  int(1) NULL default 0 COMMENT '模板类型：0:通用模板；1：自定义模板' AFTER `pro_size`;

-- --------------
-- v1.12.1 广东接口 by linguangkuo on 2017/08/09
-- --------------
CREATE TABLE `ec_sync_info` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `ec_code` varchar(18) NOT NULL COMMENT 'Ec企业代码',
  `ec_name` varchar(128) NOT NULL COMMENT '企业名称',
  `region` varchar(54) NOT NULL COMMENT '工单地区',
  `legal_person` varchar(128) NOT NULL COMMENT '企业法人',
  `ent_permit` varchar(50) NOT NULL COMMENT '营业执照号码',
  `user_name` varchar(54) NOT NULL COMMENT '联系人姓名',
  `mobile` varchar(54) NOT NULL COMMENT '联系人手机号码',
  `email` varchar(255) NOT NULL COMMENT '联系人邮箱',
  `main_contact` int(2) NOT NULL COMMENT '是否主联系人',
  `ec_level` varchar(20) NOT NULL COMMENT '集团等级',
  `unit_kind` varchar(20) NOT NULL COMMENT '集团机构类型',
  `district` varchar(20) NOT NULL COMMENT '所属城市',
  `innet_date` datetime NOT NULL COMMENT '集团开户时间',
  `vip_type` varchar(20) NOT NULL COMMENT '集团转态',
  `vip_type_state_date` datetime NOT NULL COMMENT '集团状态更新时间',
  `credit_level` varchar(10) NOT NULL COMMENT '集团信控等级',
  `dev_channel` varchar(20) NOT NULL COMMENT '集团客户发展渠道',
  `dev_user_id` varchar(50) NOT NULL COMMENT '集团客户发展人工号',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_flag` int(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 众筹蒙版
CREATE TABLE `masking` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `admin_id` bigint(18) NOT NULL,
  `crowdfunding_mask` int(1) DEFAULT '1' COMMENT '众筹蒙版1：仍然提醒；0：不再提醒',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- --------------
-- v1.12.1 营销模板改造 by qinqinyan on 2017/08/10
-- --------------
ALTER TABLE `mdrc_cardmake_detail`
ADD COLUMN `tracking_number`  varchar(400) NULL COMMENT '快递单号' AFTER `file_name`;


-- --------------
-- v1.12.1 营销模板改造 by qinqinyan on 2017/08/15
-- --------------
INSERT INTO `authority` VALUES (null, null, '营销卡充值记录', 'ROLE_MDRC_CHARGE', '107012', null, '2017-08-15 09:43:11', '1', '1', '2017-08-15 09:43:11', '0');

ALTER TABLE `masking`
ADD COLUMN `mdrc_mask`  int(1) NULL default 1 COMMENT '营销模板：1：仍然提醒；0：不再提醒' AFTER `update_time`;

-- --------------
-- v1.12.1 营销模板改造 by qinqinyan on 2017/08/10
-- --------------
ALTER TABLE `mdrc_cardmake_detail`
ADD COLUMN `cost`  bigint(18) NULL COMMENT '制卡成本' AFTER `tracking_number`;

-- --------------
-- v1.12.1 接口参数 by linguangkuo on 2017/08/16
-- --------------
INSERT INTO `global_config` VALUES (null, '广东查询产品订购关系接口url', '广东查询产品订购关系接口url', 'GD_QRY_ENT_SRV_REG_URL', 'http://221.179.7.250/GDADC_W/NGADCABInterface/TrafficZC/PmpServicesRec.aspx?svc_cat=GetTx&svc_code=QryEntSrvReg',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '广东查询集团信息接口url', '广东查询集团信息接口url', 'GD_QRY_EC_SYNC_INFO_URL', 'http://221.179.7.250/GDADC_W/NGADCABInterface/TrafficZC/PmpServicesRec.aspx?svc_cat=GetTx&svc_code=QryECSyncInfo',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '广东查询产品账户余额接口url', '广东查询产品账户余额接口url', 'GD_QRY_PRODUCT_FEE_URL', 'http://221.179.7.250/GDADC_W/NGADCABInterface/TrafficZC/PmpServicesRec.aspx?svc_cat=GetTx&svc_code=QryProductFee',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '平台名称', '平台名称，8590众筹；8588流量卡', 'GD_PLATFORM', '8588',NOW(), NOW(), '1', '1', '0', null);

-- --------------
-- v1.12.1 营销模板修改字段 by qinqinyan on 2017/08/16
-- --------------
ALTER TABLE `mdrc_batch_config`
MODIFY COLUMN `config_name`  varchar(64) NULL COMMENT '卡名称' AFTER `id`;


-- --------------
-- v1.12.1 营销卡配置规则增加生效日期和失效日期 by 罗祖武 on 2017/08/10
-- --------------
ALTER TABLE `mdrc_batch_config`
ADD COLUMN `effective_time`  datetime NULL COMMENT '生效日期' AFTER `create_time`,
ADD COLUMN `expiry_time`  datetime NULL COMMENT '失效日期' AFTER `effective_time`;
-- --------------
-- v1.12.1 营销卡配置规则信息表增加物流公司名称和快递单号 by 罗祖武 on 2017/08/10
-- --------------
ALTER TABLE `mdrc_bacth_config_info`
ADD COLUMN `express_ent_name`  varchar(300) NULL COMMENT '物流公司名称' AFTER `template_back_name`,
ADD COLUMN `express_number`  varchar(64) NULL COMMENT '快递单号' AFTER `express_ent_name`,
ADD COLUMN `receive_key`  varchar(300) NULL COMMENT '签收凭证' AFTER `express_number`,
ADD COLUMN `receiver_name`  varchar(64) NULL COMMENT '签收人姓名' AFTER `receive_key`,
ADD COLUMN `receiver_mobile`  varchar(11) NULL COMMENT '签收人手机号码' AFTER `receiver_name`;


-- --------------
-- v1.12.1 营销卡规则状态变更表 by 罗祖武 on 2017/08/15
-- --------------
CREATE TABLE `mdrc_batch_config_status_record` (
`id`  bigint(18) NOT NULL COMMENT '自增ID' ,
`config_id`  bigint(18) NULL ,
`pre_status`  int(18) NULL COMMENT '之前状态' ,
`now_status`  int(18) NULL COMMENT '当前状态' ,
`create_time`  datetime NULL COMMENT '创建时间' ,
`update_time`  datetime NULL COMMENT '更新时间' ,
`updator_id`  bigint(18) NULL ,
`delete_flag`  int(18) NULL COMMENT '除删标识' ,
PRIMARY KEY (`id`)
);

ALTER TABLE `mdrc_batch_config_status_record`
MODIFY COLUMN `id`  bigint(18) NOT NULL AUTO_INCREMENT COMMENT '自增ID' FIRST ;

ALTER TABLE `mdrc_bacth_config_info`
ADD COLUMN `receive_file_name`  varchar(300) NULL COMMENT '签收凭证文件名' AFTER `receive_key`;

-- --------------
-- v1.12.1 手机号码单月的MDRC充值成功次数限制,按需添加，始于广东营销卡平台  by 罗祖武 on 2017/08/18
-- INSERT INTO `global_config` VALUES (null, '手机号码单月的MDRC充值成功次数限制', '营销卡一个手机号码单月的充值成功次数限制', 'MAX_SIZE_CHARGE_SUCCESS_PER_MONTH', '100',NOW(), NOW(), '1', '1', '0', null);
-- --------------

-- -------------
-- v1.12.1 增加字段 by qinqinyan on 2017/08/21
-- -------------
ALTER TABLE `mdrc_template`
ADD COLUMN `front_image_name`  varchar(300) NULL COMMENT '模板正面名称' AFTER `type`,
ADD COLUMN `rear_image_name`  varchar(300) NULL COMMENT '模板反面名称' AFTER `front_image_name`;

-- -------------
-- v1.12.1 增加营销卡签收权限 by luozuwu on 2017/08/22
-- -------------
INSERT INTO `authority` VALUES (null, null, '营销卡签收', 'ROLE_MDRC_ACK_RECEIVE', '108013', null, NOW(), '1', '1',NOW(), '0');

-- --------------
-- v1.12.1 营销卡修改字段 by qinqinyan on 2017/08/22
-- --------------
ALTER TABLE `mdrc_cardmake_detail`
MODIFY COLUMN `config_name`  varchar(64) NULL COMMENT '卡名称' AFTER `request_id`;

-- --------------
-- v1.12.1 单点登录增加登出请求url by qinqinyan on 2017/08/22
-- --------------
INSERT INTO `global_config` VALUES (null, '广东单点登录登出时发给gdsso的url', '广东单点登录登出时发给gdsso的url', 'GD_SSO_LOGOUT_URL', 'http://localhost:8090/gdsso/gdlogout/req.html',NOW(), NOW(), '1', '1', '0', null);
update global_config set config_value='http://localhost:8090/gdsso/user/login.html' where config_key = 'GD_SSO_FIRSTPAGE_URL';

-- --------------
-- v1.12.1 暂停关闭企业增加原因 by linguangkuo on 2017/08/30
-- --------------
ALTER TABLE `ent_status_record`
ADD COLUMN `op_reason` varchar(200) NULL COMMENT '操作原因' AFTER `delete_flag`;

-- --------------
-- v1.12.1 存在转义，因此修改字段长度 qinqinyan on 2017/09/06
-- --------------
ALTER TABLE `mdrc_batch_config`
MODIFY COLUMN `config_name`  varchar(400) CHARACTER SET utf8
                COLLATE utf8_general_ci default NULL COMMENT '卡名称' AFTER `id`;

ALTER TABLE `mdrc_cardmake_detail`
MODIFY COLUMN `config_name`  varchar(400) CHARACTER SET utf8
                COLLATE utf8_general_ci default NULL COMMENT '卡名称' AFTER `request_id`;

ALTER TABLE `mdrc_bacth_config_info`
MODIFY COLUMN `customer_service_phone`  varchar(400) CHARACTER SET utf8
                COLLATE utf8_general_ci default NULL COMMENT '客服电话' AFTER `qrcode_name`;
                
ALTER TABLE `mdrc_bacth_config_info`
MODIFY COLUMN `name`  varchar(400) CHARACTER SET utf8
                COLLATE utf8_general_ci default NULL COMMENT '收件人姓名' AFTER `is_free`;

ALTER TABLE `mdrc_bacth_config_info`
MODIFY COLUMN `address`  varchar(1500) CHARACTER SET utf8
                COLLATE utf8_general_ci default NULL COMMENT '收件人详细地址' AFTER `mobile`;

-- ----------------------------
-- v1.13.0 充值结果查询策略变更  by linguangkuo on 2017/07/19 
-- ----------------------------
INSERT INTO `global_config` VALUES (null, '充值结果查询次数', '充值结果查询次数', 'BOSS_QUERY_TIME', '10', NOW(), NOW(), '1', '1', '0', null);
ALTER TABLE `supplier`
ADD COLUMN `is_query_charge_result` int(1) NULL DEFAULT 0 COMMENT '是否有充值结果查询接口' AFTER `limit_update_time`;
ALTER TABLE `charge_record`
ADD COLUMN `query_time` datetime DEFAULT NULL COMMENT '手动查询时间' AFTER `count`;

INSERT INTO `global_config` VALUES (null, '上海全网boss渠道openapiUrl', '上海全网boss渠道openapiUrl', 'BOSS_SHANGHAI_OLD_OPENAPI_URL', 'http://211.136.111.133/open/service', NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '上海全网boss渠道securityUrl', '上海全网boss渠道securityUrl', 'BOSS_SHANGHAI_OLD_SECURITY_URL', 'http://211.136.111.133/open/security', NOW(), NOW(), '1', '1', '0', null);


-- ----------------------------
-- v1.13.0 产品表增加产品分类，wujiamin，20170830
-- ----------------------------
ALTER TABLE `product`
ADD COLUMN `product_customer_type`  bigint NULL DEFAULT 1 COMMENT '与customer_type表关联，表示产品分类' AFTER `illustration`;

ALTER TABLE `enterprises`
MODIFY COLUMN `customer_type_id`  bigint(18) NULL DEFAULT 1 COMMENT '客户分类id' AFTER `district_id`;

-- ----------------------------
-- v1.13.0企业开户增加产品分类需要的sql，可具体咨询wujiamin，20170830
-- ----------------------------
-- 1、检查customer_type表，enterprise表
-- select * from customer_type;
-- 如果customer_type记录不是1条，“1-行业”，请进行特殊处理
-- 如果是1条“1-行业”，则执行下面的update
-- update customer_type set name="无" where id=1;
-- 2、检查企业表
-- select * from enterprises where customer_type_id!=1 or customer_type_id is null;
-- 如果上面筛选出customer_type_id都为空，把空值的customer_type_id都设为1，可执行下面的update
-- update enterprises set customer_type_id = 1 where customer_type_id is null;
-- 如果上面筛选出customer_type_id除了空值，还有其他不为1的值，则要特殊处理，处理逻辑咨询wujiamin

-- ----------------------------
-- v1.13.0重庆闲时流量包 by linguangkuo，20170830
-- ----------------------------
INSERT INTO `global_config` VALUES (null, '重庆闲时流量包充值URL', '重庆闲时流量包充值URL', 'CQ_NEW_BOSS_URL', 'https://183.230.30.244:7101/openapi/httpService/OrderService',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '重庆闲时流量包充值应用id', '重庆闲时流量包充值应用id', 'CQ_NEW_BOSS_CLIENT_ID', '20170726000098025',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '重庆闲时流量包充值应用秘钥', '重庆闲时流量包充值应用秘钥', 'CQ_NEW_BOSS_CLIENT_SECRET', '5ce4bb6ba1c8f3c1d3b304656cacbcb8',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '重庆闲时流量包充值获取token地址', '重庆闲时流量包充值获取token地址', 'CQ_NEW_BOSS_TOKEN_URL', 'https://183.230.30.244:7101/OAuth/restOauth2Server/access_token',NOW(), NOW(), '1', '1', '0', null);

-- -----------------------
-- v1.13.0 山东BOSS产品 luozuwu 20170831
-- -----------------------
CREATE TABLE `sd_boss_product` (
`id`  bigint(18) NULL AUTO_INCREMENT COMMENT '自增ID' ,
`name`  varchar(255) NULL COMMENT '产品名称' ,
`code`  varchar(255) NULL COMMENT '产品编码' ,
`price`  bigint(18) NULL COMMENT '产品价格，单位分' ,
`size`  bigint(18) NULL COMMENT '产品大小，单位KB' ,
`type`  int(18) NULL COMMENT '产品类型：0 现金产品；1流量池产品；2流量包产品；3话费产品；4虚拟币产品；5预付费资金产品；6预付费流量包产品' ,
`create_time`  datetime NULL COMMENT '创建时间' ,
`update_time`  datetime NULL COMMENT '更新时间' ,
`delete_flag`  int(18) NULL COMMENT '删除标记：0未删除；1已删除' ,
PRIMARY KEY (`id`)
);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('全省流量统付3元包','109201',300,10240, 2,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('全省流量统付5元包','109202',500,30720, 2,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('全省流量统付10元包','109203',1000,71680, 2,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('全省流量统付20元包','109204',2000,153600, 2,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('全省流量统付30元包','109205',3000,512000, 2,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('全省流量统付50元包','109206',5000,1048576, 2,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('全省流量统付70元包','109207',7000,2097152, 2,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('全省流量统付100元包','109208',10000,3145728, 2,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('全省流量统付130元包','109209',13000,4194304, 2,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('全省流量统付180元包','109210',18000,6291456, 2,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('全省流量统付280元包','109211',28000,11534336, 2,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('流量统付3元包','108701',300,10240, 2,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('流量统付5元包','108702',500,30720, 2,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('流量统付10元包','108703',1000,71680, 2,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('流量统付20元包','108704',2000,153600, 2,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('流量统付30元包','108705',3000,512000, 2,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('流量统付50元包','108706',5000,1048576, 2,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('流量统付70元包','108707',7000,2097152, 2,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('流量统付100元包','108708',10000,3145728, 2,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('流量统付130元包','108709',13000,4194304, 2,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('流量统付180元包','108710',18000,6291456, 2,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('流量统付280元包','108711',28000,11534336, 2,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('流量统付10元包100M','108712',1000,102400, 2,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('流量统付20元包300M','108713',2000,307200, 2,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('全省流量统付10元包100M','109212',1000,102400, 2,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('全省流量统付20元包300M','109213',2000,307200, 2,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('流量池产品','109901',0,1024, 1,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('GPRS-3元包','110501',300,10240, 6,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('GPRS-5元包','110502',500,30720, 6,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('GPRS-10元包','110503',1000,71680, 6,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('GPRS-20元包','110504',2000,153600, 6,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('GPRS-30元包','110505',3000,512000, 6,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('GPRS-50元包','110506',5000,1048576, 6,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('GPRS-70元包','110507',7000,2097152, 6,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('GPRS-100元包','110508',10000,3145728, 6,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('GPRS-130元包','110509',13000,4194304, 6,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('GPRS-180元包','110510',18000,6291456, 6,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('GPRS-280元包','110511',28000,11534336, 6,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('10元包100M','110512',1000,102400, 6,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('20元包300M','110513',2000,307200, 6,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('5元包1GB省内通用流量单天包','109214',500,1048576, 2,NOW(),NOW(),0);
-- ----------------------------
-- v1.13.0山东流量平台预付费产品 by luozuwu，20170831
-- ----------------------------
INSERT INTO `product` (product_code, type, name, status, create_time, update_time, delete_flag, price, defaultValue, isp, ownership_region, roaming_region, product_size, flow_account_flag) VALUES ("prepay", 5, '预付费资金产品', 1, now(), now(), 0, 1, 1, 'M', '全国', '全国', 1, 2);
-- ----------------------------
-- v1.13.0山东流量平台折扣生效时间 by luozuwu，20170831
-- ----------------------------
ALTER TABLE `discount_record`
ADD COLUMN `validate_time`  datetime NULL COMMENT '扣折生效时间' AFTER `delete_flag`;

-- ----------------------------
-- v1.13.0甘肃账户余额变更 by linguangkuo，20170901
-- ----------------------------
ALTER TABLE `account_change_detail`
ADD COLUMN `discount_type`  int(2) DEFAULT NULL COMMENT '优惠类型' AFTER `delete_flag`;
ALTER TABLE `account_change_detail`
ADD COLUMN `discount_value` int(8) DEFAULT NULL COMMENT '优惠值' AFTER `discount_type`;
ALTER TABLE `account_change_operator`
ADD COLUMN `discount_type`  int(2) DEFAULT NULL COMMENT '优惠类型' AFTER `update_time`;
ALTER TABLE `account_change_operator`
ADD COLUMN `discount_value` int(8) DEFAULT NULL COMMENT '优惠值' AFTER `discount_type`;


-- ---------------------------
-- v1.13.0 山东账户充值记录（预付产品账户） by luozuwu，20170831
-- ----------------------------
CREATE TABLE `sd_account_charge_record` (
`id`  bigint(18) NULL AUTO_INCREMENT COMMENT '自增ID' ,
`opr`  varchar(255) NULL COMMENT '操作：默认为pay' ,
`type`  varchar(255) NULL COMMENT '操作类型' ,
`pkg_seq`  varchar(255) NULL COMMENT '发起方交易包流水号' ,
`ecid`  varchar(255) NULL COMMENT 'bossId' ,
`acct_id`  varchar(255) NULL COMMENT '账户编码--计费账户标志' ,
`user_id`  varchar(255) NULL COMMENT '集团用户ID (产品实例)' ,
`product_id`  varchar(255) NULL COMMENT '主产品ID ' ,
`pay`  bigint(18) NULL COMMENT '充值金额，单位分' ,
`opr_eff_time`  datetime NULL COMMENT '[充值时间], 必填' ,
`acct_seq`  varchar(255) NULL COMMENT '[充值流水号]' ,
`acct_no`  varchar(255) NULL COMMENT '充值序列号',
`param_name`  varchar(255) NULL COMMENT '预留参数名' ,
`param_value`  varchar(255) NULL COMMENT '留预参数值' ,
`request_body`  varchar(1024) NULL COMMENT '完整请求报文' ,
`create_time`  datetime NULL COMMENT '记录创建时间' ,
`update_time`  datetime NULL COMMENT '记录更新时间' ,
`plat_account_id`  bigint NULL COMMENT '平台账户ID' ,
`status`  bigint NULL COMMENT '操作结果：1、成功；2、失败' ,
`message`  varchar NULL COMMENT '操作结果说明，如果失败则表示失败原因' ,
`delete_flag`  int NULL COMMENT '删除标记：0未删除；1已删除',
PRIMARY KEY (`id`)
);

-- ---------------------------
-- v1.13.0 山东预付费产品 by luozuwu，20170904
-- ----------------------------
ALTER TABLE `approval_request`
MODIFY COLUMN `creator_id`  bigint(18) NULL COMMENT '流程发起用户id' AFTER `ent_id`;


-- ----------------------------
-- v1.13.0 Table structure for `activity_config` by qinqinyan on 2017/09/05
-- ----------------------------
DROP TABLE IF EXISTS `activity_config`;
CREATE TABLE `activity_config` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `province` varchar(300) NOT NULL COMMENT '中奖号码归属地范围',
  `isp` varchar(50) NOT NULL COMMENT '中奖号码运营商范围：M移动；U联通；T电信；A三网',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `delete_flag` int(1) NOT NULL COMMENT '逻辑删除标志位。1删除；0未删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


-- ---------------------------
-- v1.13.0 江苏开放平台对账相关 20170906
-- ----------------------------
INSERT INTO `global_config` VALUES (null, '江苏开放平台对账生成文件前缀', '江苏开放平台对账生成文件前缀', 'JS_OPENPLATFORM_FILE_PREFIX', 'llpt2hly',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '江苏开放平台对账ftp的url', '江苏开放平台对账ftp的url', 'JS_OPENPLATFORM_FTP_URL', '127.0.0.1',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '江苏开放平台对账ftp的port', '江苏开放平台对账ftp的port', 'JS_OPENPLATFORM_FTP_PORT', '21',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '江苏开放平台对账ftp的LOGINNAME', '江苏开放平台对账ftp的LOGINNAME', 'JS_OPENPLATFORM_FTP_LOGINNAME', 'qihang',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '江苏开放平台对账ftp的LOGINPASS', '江苏开放平台对账ftp的LOGINPASS', 'JS_OPENPLATFORM_FTP_LOGINPASS', 'xiaoqi160',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '江苏开放平台对账ftp的本地账单保存路径', '江苏开放平台对账ftp的本地账单保存路径', 'JS_OPENPLATFORM_FTP_LOCALPATH', '/usr/share/tomcat/jsData',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '江苏开放平台对账ftp的目标文件保存路径', '江苏开放平台对账ftp的目标文件保存路径', 'JS_OPENPLATFORM_FTP_TARGETPATH', '/aaa',NOW(), NOW(), '1', '1', '0', null);



-- ---------------------------
-- v1.13.0 权限配置 201709011
-- ----------------------------
INSERT INTO `authority` VALUES (null, null, '中奖配置', 'ROLE_ACTIVITY_SCOPE_CONFIG', '109004', null, '2017-08-30 16:54:44', '1', '1', '2017-08-30 16:54:44', '0');

-- ---------------------------
-- v1.13.0 sd_account_charge_record 重置,qihang
-- ----------------------------
DROP TABLE IF EXISTS `sd_account_charge_record`;
CREATE TABLE `sd_account_charge_record` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `opr` varchar(255) DEFAULT NULL COMMENT '操作：默认为pay',
  `type` varchar(255) DEFAULT NULL COMMENT '操作类型',
  `pkg_seq` varchar(255) DEFAULT NULL COMMENT '发起方交易包流水号',
  `ecid` varchar(255) DEFAULT NULL COMMENT 'bossId',
  `acct_id` varchar(255) DEFAULT NULL COMMENT '账户编码--计费账户标志',
  `user_id` varchar(255) DEFAULT NULL COMMENT '集团用户ID (产品实例)',
  `product_id` varchar(255) DEFAULT NULL COMMENT '主产品ID ',
  `pay` bigint(18) DEFAULT NULL COMMENT '充值金额，单位分',
  `opr_eff_time` datetime DEFAULT NULL COMMENT '[充值时间], 必填',
  `acct_seq` varchar(255) DEFAULT NULL COMMENT '[充值流水号]',
  `acct_no` varchar(255) DEFAULT NULL,
  `param_name` varchar(255) DEFAULT NULL COMMENT '预留参数名',
  `param_value` varchar(255) DEFAULT NULL COMMENT '留预参数值',
  `request_body` varchar(1024) DEFAULT NULL COMMENT '完整请求报文',
  `create_time` datetime DEFAULT NULL COMMENT '记录创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '记录更新时间',
  `plat_account_id` bigint(20) DEFAULT NULL COMMENT '平台账户ID',
  `status` int(20) DEFAULT NULL COMMENT '操作结果：0、同步中； 1、成功；2、失败',
  `message` varchar(255) DEFAULT NULL COMMENT '操作结果说明，如果失败则表示失败原因',
  `delete_flag` int(11) DEFAULT NULL COMMENT '删除标记：0未删除；1已删除',
  `change_detail_id` bigint(18) DEFAULT NULL COMMENT 'change_detail表主键ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;


-- ---------------------------
-- add product type comment
-- ----------------------------
ALTER TABLE `product`
MODIFY COLUMN `type`  int(8) NOT NULL DEFAULT 2 COMMENT '产品类型，0为现金产品，1为流量池产品，2为流量包产品 3话费产品,4虚拟币,5预付费资金产品,6预付费流量包产品' AFTER `product_code`;

-- --------------
-- v1.12.3 更改表名  by qinqinyan on 2017/09/15
-- --------------
rename table mdrc_bacth_config_info to mdrc_batch_config_info;



-- ---------------------------
-- v1.13.1 众筹能力配置 20170919
-- ----------------------------
INSERT INTO `authority` VALUES (null, null, '众筹能力配置', 'ROLE_CROWDFUNDING_ABILITY_CONFIG', '109005', null, '2017-08-30 16:54:44', '1', '1', '2017-08-30 16:54:44', '0');
ALTER TABLE `enterprises_ext_info`
ADD COLUMN `ability_config`  int(2) NULL DEFAULT NULL COMMENT '能力配置 1有企业查询接口；0或null无查询接口' AFTER `callback_url`;

-- ----------------------------
-- v1.13.1 Table structure for `wx_administer` by qinqinyan on 2017/09/19
-- ----------------------------
DROP TABLE IF EXISTS `wx_administer`;
CREATE TABLE `wx_administer` (
  `user_name` varchar(64) DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) DEFAULT NULL COMMENT '用户密码',
  `password_new` varchar(64) DEFAULT NULL COMMENT '新的密码，加盐哈希',
  `salt` varchar(64) DEFAULT NULL COMMENT '密码盐',
  `password_update_time` datetime DEFAULT NULL,
  `mobile_phone` varchar(11) NOT NULL COMMENT '用户手机号码',
  `creator_id` bigint(18) DEFAULT NULL COMMENT '创建者ID， admin时为null， 其它不能为null',
  `pic_url` varchar(128) DEFAULT '' COMMENT '头像url',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱地址',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` int(1) NOT NULL DEFAULT '0' COMMENT '删除标记， 0:未删除；1：已删除',
  `citys` varchar(30) DEFAULT NULL COMMENT '本项目中该字段无效',
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_uuid` varchar(70) DEFAULT NULL COMMENT '用户唯一标示,uuid',
  PRIMARY KEY (`id`),
  KEY `name_index` (`delete_flag`,`user_name`) USING BTREE,
  KEY `mobile_phone_index` (`mobile_phone`,`delete_flag`) USING BTREE,
  KEY `all_index` (`delete_flag`) USING BTREE,
  KEY `id_phone_index` (`mobile_phone`,`delete_flag`,`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


-- ---------------------------
-- v1.13.1 流量币兑换规则 20170921
-- ----------------------------
INSERT INTO `global_config` VALUES (null, '微信公众号积分兑换账户每天兑换次数限制', '微信公众号积分兑换账户每天兑换次数限制', 'WEIXIN_EXCHANGE_NUM_LIMIT', '10',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '微信公众号积分兑换产品兑换总额度', '微信公众号积分兑换产品兑换总额度', 'WEIXIN_EXCHANGE_PRODUCT_NUM_LIMIT', '{10240:50000,30720:5000,71680:5000,153600:5000}',NOW(), NOW(), '1', '1', '0', null);

INSERT INTO `global_config` VALUES (null, '微信公众号积分兑换每月限制开始日期', '微信公众号积分兑换每月限制开始日期', 'WEIXIN_EXCHANGE_START_DATE', '1',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '微信公众号积分兑换每月限制结束日期', '微信公众号积分兑换每月限制结束日期', 'WEIXIN_EXCHANGE_END_DATE', '0',NOW(), NOW(), '1', '1', '0', null);

INSERT INTO `global_config` VALUES (null, '微信公众号积分兑换账户每天兑换流量总额限制', '微信公众号积分兑换账户每天兑换流量总额限制', 'WEIXIN_EXCHANGE_DAY_LIMIT', '10000',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '微信公众号积分兑换每月兑换总额度', '微信公众号积分兑换每月兑换总额度', 'WEIXIN_EXCHANGE_MOTHLY_ALL_LIMIT', '1750000',NOW(), NOW(), '1', '1', '0', null);

-- ---------------------------
-- v1.13.2 显示时模糊处理 罗祖武 20170927 for chognqing
-- ----------------------------
INSERT INTO `global_config` VALUES (null, '企业名称模糊显示', '企业名称模糊显示:取值YES时，是；其他否', 'BLUR_ENT_NAME', 'NO',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '企业编码模糊显示', '企业编码模糊显示:取值YES时，是；其他否', 'BLUR_ENT_CODE', 'NO',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '用户名称模糊显示', '用户名称模糊显示:取值YES时，是；其他否', 'BLUR_USER_NAME', 'NO',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '手机号码模糊显示', '手机号码模糊显示:取值YES时，是；其他否', 'BLUR_USER_MOBILE', 'NO',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '邮箱模糊显示', '邮箱模糊显示:取值YES时，是；其他否', 'BLUR_USER_EMAIL', 'NO',NOW(), NOW(), '1', '1', '0', null);

-- ----------------------------
-- v1.14.0 日统计报表 20171010 linguangkuo
-- ----------------------------
DROP TABLE IF EXISTS `daily_statistic`;
CREATE TABLE `daily_statistic` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `enter_id` bigint(18) NOT NULL COMMENT '企业id ',
  `city` varchar(50) NOT NULL COMMENT '企业所属地市,如济南市',
  `prd_id` bigint(18) NOT NULL COMMENT '产品id',
  `charge_type` varchar(50) NOT NULL COMMENT '充值类型',
  `success_count` bigint(20) NOT NULL COMMENT '充值成功个数',
  `total_count` bigint(20) NOT NULL COMMENT '产品充值总数',
  `capacity` bigint(20) NOT NULL COMMENT '流量总kb数',
  `money` bigint(18) NOT NULL COMMENT '流量交易金额(分)',
  `date` datetime NOT NULL COMMENT '流量交易日期',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_flag` int(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- ----------------------------
-- v1.14.0 增加企业所属地市字段 罗祖武 20171011
-- ----------------------------
ALTER TABLE `daily_statistic`
ADD COLUMN `city`  varchar(50) NOT NULL COMMENT '企业所属地市,如济南市' AFTER `enter_id`;


-- -----------------------
-- 20171017 v1.14.0黑龙江三代接口--思特奇配置相关 luozuwu 特别说明：该配置已在黑龙江线上版本执行过
-- -----------------------
/*
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江BOSS充值接口是否直接对接BOSS接口', '黑龙江BOSS充值接口是否直接对接BOSS接口', 'BOSS_HLJ_USE_BOSS_INTERFACE', 'YES', NOW(),  NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江BOSS充值接口单个充值接口or批量充值接口', '黑龙江BOSS充值接口单个充值接口or批量充值接口', 'BOSS_HLJ_CHARGE_SINGLE_OR_BATCH', 'SINGLE', NOW(), NOW(), '1', '1', '0', NULL);

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江BOSS充值结果查询之WSDL地址', '黑龙江BOSS充值结果查询之WSDL地址', 'BOSS_HLJ_QUERY_CHARGE_RESULT_WSDL', 'http://10.110.26.27:51000/esbWS/services/sHyLLPhoneQryWS?wsdl', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江BOSS充值结果查询之查询接口地址', '黑龙江BOSS充值结果查询之查询接口地址', 'BOSS_HLJ_QUERY_CHARGE_RESULT_ADDRESS', 'http://10.110.26.27:51000/esbWS/services/sHyLLPhoneQryWS/callService', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江BOSS充值结果查询之查询行数', '黑龙江BOSS充值结果查询之查询行数', 'BOSS_HLJ_QUERY_CHARGE_RESULT_ROWNUM', '1', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江BOSS充值结果查询之每行展示的个数', '黑龙江BOSS充值结果查询之每行展示的个数', 'BOSS_HLJ_QUERY_CHARGE_RESULT_ROWCOUNT', '5', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江BOSS充值结果查询之操作员手机号', '黑龙江BOSS充值结果查询之操作员手机号', 'BOSS_HLJ_QUERY_CHARGE_RESULT_IOPPHONE', '', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江BOSS充值结果查询之用户密码', '黑龙江BOSS充值结果查询之用户密码', 'BOSS_HLJ_QUERY_CHARGE_RESULT_IUSERPWD', '', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江BOSS充值结果查询之操作代码', '黑龙江BOSS充值结果查询之操作代码', 'BOSS_HLJ_QUERY_CHARGE_RESULT_IOPCODE', 'g686', NOW(), NOW(), '1', '1', '0', NULL);

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '思特奇流量卡充值URL', '思特奇流量卡充值URL', 'BOSS_HLJ_MDRC_CHARGE_URL', 'http://10.110.2.230:10094/adapter', NOW(), NOW(), '1', '1', '0', NULL);

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇集团可赠送流量查询接口之URL', '黑龙江斯特奇集团可赠送流量查询接口之URL','BOSS_HLJ_SITECH_QUERY_GROUP_ACCOUNT_URL', 'http://10.149.31.102:53000/esbWS/rest/bsp_accept_v1_batch_queryAvailableRev', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇集团可赠送流量查询接口之操作工号', '黑龙江斯特奇集团可赠送流量查询接口之操作工号', 'BOSS_HLJ_SITECH_QUERY_GROUP_ACCOUNT_OPRID', 'llzcpt', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇集团可赠送流量查询接口之工号角色', '黑龙江斯特奇集团可赠送流量查询接口之工号角色', 'BOSS_HLJ_SITECH_QUERY_GROUP_ACCOUNT_OPRROLE', '3', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇集团可赠送流量查询接口之工号密码', '黑龙江BOSS充值接口是否直接对接BOSS接口', 'BOSS_HLJ_SITECH_QUERY_GROUP_ACCOUNT_PASSWORD', 'BYVJFNRZMCEFBREK', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇集团可赠送流量查询接口之集团联系人联系电话', '黑龙江斯特奇集团可赠送流量查询接口之集团联系人联系电话', 'BOSS_HLJ_SITECH_QUERY_GROUP_ACCOUNT_PHONENUM', '13605310531', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇集团可赠送流量查询接口之工号归属', '黑龙江斯特奇集团可赠送流量查询接口之工号归属', 'BOSS_HLJ_SITECH_QUERY_GROUP_ACCOUNT_GROUPID', '11', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇集团可赠送流量查询接口之工号', '黑龙江斯特奇集团可赠送流量查询接口之工号', 'BOSS_HLJ_SITECH_QUERY_GROUP_ACCOUNT_LOGINNO', 'llzcpt', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇集团可赠送流量查询接口之操作代码', '黑龙江斯特奇集团可赠送流量查询接口之操作代码', 'BOSS_HLJ_SITECH_QUERY_GROUP_ACCOUNT_OPCODE', '0000', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇集团可赠送流量查询接口之省份代码', '黑龙江斯特奇集团可赠送流量查询接口之省份代码', 'BOSS_HLJ_SITECH_QUERY_GROUP_ACCOUNT_PROVINCEID', '230000', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇集团可赠送流量查询接口之渠道标识', '黑龙江斯特奇集团可赠送流量查询接口之渠道标识', 'BOSS_HLJ_SITECH_QUERY_GROUP_ACCOUNT_CHANNELID', '11', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇集团可赠送流量查询接口之省份组', '黑龙江斯特奇集团可赠送流量查询接口之省份组', 'BOSS_HLJ_SITECH_QUERY_GROUP_ACCOUNT_PROVINCEGROUP', 'HLJ', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇集团可赠送流量查询接口之路由类型', '黑龙江斯特奇集团可赠送流量查询接口之路由类型', 'BOSS_HLJ_SITECH_QUERY_GROUP_ACCOUNT_ROUTEKEY', '11', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇集团可赠送流量查询接口之路由值', '黑龙江斯特奇集团可赠送流量查询接口之路由值', 'BOSS_HLJ_SITECH_QUERY_GROUP_ACCOUNT_ROUTEVALUE', '4510208711', NOW(), NOW(), '1', '1', '0', NULL);

INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇行业应用流量包实时赠送接口是否是异步方式', '黑龙江斯特奇行业应用流量包实时赠送接口是否是异步方式:true异步；其他同步', 'BOSS_HLJ_SITECH_CHARGE_IS_ASYC', 'true', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇行业应用流量包实时赠送接口之URL', '黑龙江斯特奇行业应用流量包实时赠送接口之URL', 'BOSS_HLJ_SITECH_CHARGE_URL', 'http://10.149.31.102:53000/esbWS/rest/bsp_accept_v1_batch_realPresentlRev', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇行业应用流量包实时赠送接口之赠送方式', '黑龙江斯特奇行业应用流量包实时赠送接口之赠送方式', 'BOSS_HLJ_SITECH_CHARGE_ACCESSTYPE', '37', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇行业应用流量包实时赠送接口之生效规则', '黑龙江斯特奇行业应用流量包实时赠送接口之生效规则', 'BOSS_HLJ_SITECH_CHARGE_EFFRULE', '1001',NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇行业应用流量包实时赠送接口之生效时间', '黑龙江斯特奇行业应用流量包实时赠送接口之生效时间', 'BOSS_HLJ_SITECH_CHARGE_EFFTIME', '', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇行业应用流量包实时赠送接口之失效规则', '黑龙江斯特奇行业应用流量包实时赠送接口之失效规则', 'BOSS_HLJ_SITECH_CHARGE_EXPRULE', '2001', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇行业应用流量包实时赠送接口之失效时间', '黑龙江斯特奇行业应用流量包实时赠送接口之失效时间', 'BOSS_HLJ_SITECH_CHARGE_EXPTIME', '', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇行业应用流量包实时赠送接口之PAY_MODE', '黑龙江斯特奇行业应用流量包实时赠送接口之PAY_MODE', 'BOSS_HLJ_SITECH_CHARGE_PAYMODE', '0', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇行业应用流量包实时赠送接口之SALE_NUM', '黑龙江斯特奇行业应用流量包实时赠送接口之SALE_NUM', 'BOSS_HLJ_SITECH_CHARGE_SALENUM', '1', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇行业应用流量包实时赠送接口之SALE_PRICE', '黑龙江斯特奇行业应用流量包实时赠送接口之SALE_PRICE', 'BOSS_HLJ_SITECH_CHARGE_SALEPRICE', '0', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇行业应用流量包实时赠送接口之SALE_SIZE', '黑龙江斯特奇行业应用流量包实时赠送接口之SALE_SIZE', 'BOSS_HLJ_SITECH_CHARGE_SALESIZE', '', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇行业应用流量包实时赠送接口之OPER_CODE', '黑龙江斯特奇行业应用流量包实时赠送接口之OPER_CODE', 'BOSS_HLJ_SITECH_CHARGE_OPERCODE', '4', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇行业应用流量包实时赠送接口之OPR_ID', '黑龙江斯特奇行业应用流量包实时赠送接口之OPR_ID', 'BOSS_HLJ_SITECH_CHARGE_OPRID', 'llzcpt', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇行业应用流量包实时赠送接口之OPR_ROLE', '黑龙江斯特奇行业应用流量包实时赠送接口之OPR_ROLE', 'BOSS_HLJ_SITECH_CHARGE_OPRROLE', '3', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇行业应用流量包实时赠送接口之PASS_WORD', '黑龙江斯特奇行业应用流量包实时赠送接口之PASS_WORD', 'BOSS_HLJ_SITECH_CHARGE_PASSWORD', 'BYVJFNRZMCEFBREK', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇行业应用流量包实时赠送接口之PHONE_NUM', '黑龙江斯特奇行业应用流量包实时赠送接口之PHONE_NUM', 'BOSS_HLJ_SITECH_CHARGE_PHONENUM', '13504831117', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇行业应用流量包实时赠送接口之UNIT_ID_NO', '黑龙江斯特奇行业应用流量包实时赠送接口之UNIT_ID_NO', 'BOSS_HLJ_SITECH_CHARGE_UNITIDNO', '', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇行业应用流量包实时赠送接口之REPEAT_FLAG', '黑龙江斯特奇行业应用流量包实时赠送接口之REPEAT_FLAG', 'BOSS_HLJ_SITECH_CHARGE_REPEATEFLAG', '0', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇集团可赠送流量查询接口之省份组', '黑龙江斯特奇集团可赠送流量查询接口之省份组', 'BOSS_HLJ_SITECH_CHARGE_GROUPID', '4510208711', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇行业应用流量包实时赠送接口之LOGIN_NO', '黑龙江斯特奇行业应用流量包实时赠送接口之LOGIN_NO', 'BOSS_HLJ_SITECH_CHARGE_LOGINNO', 'llzcpt', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇行业应用流量包实时赠送接口之OP_CODE', '黑龙江斯特奇行业应用流量包实时赠送接口之OP_CODE', 'BOSS_HLJ_SITECH_CHARGE_OPCODE', '10001', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇行业应用流量包实时赠送接口之PROVINCE_ID', '黑龙江斯特奇行业应用流量包实时赠送接口之PROVINCE_ID', 'BOSS_HLJ_SITECH_CHARGE_PROVINCEID', '230000', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇行业应用流量包实时赠送接口之PROVINCE_GROUP', '黑龙江斯特奇行业应用流量包实时赠送接口之PROVINCE_GROUP', 'BOSS_HLJ_SITECH_CHARGE_PROVICEGROUP', 'HLJ', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇行业应用流量包实时赠送接口之CHANNEL_ID', '黑龙江斯特奇行业应用流量包实时赠送接口之CHANNEL_ID', 'BOSS_HLJ_SITECH_CHARGE_CHANNELID', '37', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇行业应用流量包实时赠送接口之ROUTE_KEY', '黑龙江斯特奇行业应用流量包实时赠送接口之ROUTE_KEY', 'BOSS_HLJ_SITECH_CHARGE_ROUTEKEY', '11', NOW(), NOW(), '1', '1', '0', NULL);
INSERT INTO `global_config` (`id`, `name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES (null, '黑龙江斯特奇行业应用流量包实时赠送接口之ROUTE_VALUE', '黑龙江斯特奇行业应用流量包实时赠送接口之ROUTE_VALUE', 'BOSS_HLJ_SITECH_CHARGE_ROUTEVALUE', '4510208711', NOW(), NOW(), '1', '1', '0', NULL);

insert into enterprises (id,name,code,phone,email,create_time,update_time,status,delete_flag,creator_id,app_secret,ent_name,district_id,customer_type_id,benefit_grade_id,discount,business_type_id,pay_type_id,interface,interface_expire_time,interface_approval_status,start_time,end_time,app_key,licence_start_time,licence_end_time,cm_email,cm_phone,fcsms_flag)values(null,'黑龙集思特奇平台','9999','20170920','luozuwu@chinamobile.com',now(),now(),3,0,null,'8e817c26a353444996a1585b8892569f','黑龙江思特奇',null,null,null,100,null,null,1,'2199-01-30 00:00:00',1,'2016-12-01 01:06:32','2199-01-30 00:00:00','eee69a4e53054e6eb7e443f92031de5f','2016-12-01 01:06:32','2199-01-30 00:00:00',null,null,0);
*/
-- ----------------------------
-- 山东流量平台新增三种类型流量包产品 20170927 罗祖武
-- ----------------------------
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('8元包1G省内流量7天包','109215',800,1024 * 1024, 2,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('12元包3G省内流量7天包','109216',1200,3072 * 1024, 2,NOW(),NOW(),0);
insert into sd_boss_product(name,code,price,size,type,create_time,update_time,delete_flag) values('16元包10G省内流量7天包','109217',1600,10240 * 1024, 2,NOW(),NOW(),0);

-- ----------------------------
-- v1.14.0江苏欧飞接口对接 by linguangkuo，20171016
-- ----------------------------
INSERT INTO `global_config` VALUES (null, '江苏欧飞SP编码', '江苏欧飞SP编码', 'BOSS_JSOF_USERID', 'A08566',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '江苏欧飞接入密码', '江苏欧飞接入密码', 'BOSS_JSOF_USERPWS', 'of111111',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '江苏欧飞回调地址', '江苏欧飞回调地址', 'BOSS_JSOF_RETURL', 'http://test',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '江苏欧飞秘钥', '江苏欧飞秘钥', 'BOSS_JSOF_KEYSTRING', 'OFCARD',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '江苏欧飞充值请求URL', '江苏欧飞充值请求URL', 'BOSS_JSOF_CHARGE_URL', 'http://apitest.ofpay.com/flowOrder.do',NOW(), NOW(), '1', '1', '0', null);

-- ----------------------------
-- v1.14.0 充值分析 by qihang
-- ----------------------------
INSERT INTO `authority` VALUES (null, null, '充值分析', 'ROLE_CHARGE_ANALYSE', '109010', null, NOW(), '1', '1', NOW(), '0');
INSERT INTO `authority` VALUES (null, null, '平台报表', 'ROLE_CHARGE_STATISTICS_FORM', '109011', null, NOW(), '1', '1', NOW(), '0');

-- ----------------------------
-- v1.14.0 审核流程是否发送短信 by qihang
-- ----------------------------
ALTER TABLE `approval_process_definition`
ADD COLUMN `msg`  int(1) NOT NULL COMMENT '流程发起人是否收到短信' DEFAULT 0 AFTER `type`;

ALTER TABLE `approval_process_definition`
ADD COLUMN `recvmsg`  int(1) NOT NULL DEFAULT 0 COMMENT '相关管理员是否收到短信' AFTER `msg`;

-- ----------------------------
-- v1.14.0 审核流程是否发送短信 by qihang
-- ----------------------------
INSERT INTO `global_config` VALUES
  (null, 'MOCK四川返回的余额值', 'MOCK四川返回的余额值，单位是分，SC_ACCOUNT_BALANCE的100倍', 'SC_MOCK_ACCOUNT_BALANCE', '10000', NOW(),
         NOW(),'1', '1', '0', '0');

INSERT INTO `global_config` VALUES
  (null, '是否MOCK四川boss', '是否MOCK四川boss,true为是，充值自动成功，查询余额自动返回SC_MOCK_ACCOUNT_BALANCE值单位为分','SC_MOCK_BOSS',  'false', NOW(),
         NOW(),'1', '1', '0', '0');
         
         
-- ----------------------------
-- v1.14.0 重庆个人套餐查询 by 林广阔
-- ----------------------------       
INSERT INTO `global_config` VALUES (null, '重庆套餐剩余量信查询URL', '重庆套餐剩余量信查询URL', 'QRY_LEFT_TRAFFIC_URL', 'https://183.230.30.244:7101/openapi/httpService/UserQryService',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '重庆套餐剩余量信查询应用id', '重庆套餐剩余量信查询应用id', 'QRY_LEFT_TRAFFIC_CLIENT_ID', '20170726000098025',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '重庆套餐剩余量信查询应用秘钥', '重庆套餐剩余量信查询应用秘钥', 'QRY_LEFT_TRAFFIC_CLIENT_SECRET', '5ce4bb6ba1c8f3c1d3b304656cacbcb8',NOW(), NOW(), '1', '1', '0', null);
INSERT INTO `global_config` VALUES (null, '重庆套餐剩余量信查询获取token地址', '重庆套餐剩余量信查询获取token地址', 'QRY_LEFT_TRAFFIC_TOKEN_URL', 'https://183.230.30.244:7101/OAuth/restOauth2Server/access_token',NOW(), NOW(), '1', '1', '0', null);

-- ----------------------------
-- v1.15.0 活动创建者表  by qihang 20171113
-- ----------------------------
CREATE TABLE activity_creator (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`activity_type`  int(5) NOT NULL COMMENT 'ActivityType类，赠送为1，红包为2，转盘为3，砸金蛋为4，二维码为6，包月为18' ,
`activity_id`  bigint(20) NOT NULL COMMENT '活动id' ,
`admin_id`  bigint(20) NOT NULL COMMENT '创建者Id' ,
`user_name`  varchar(64) NOT NULL COMMENT '用户名' ,
`mobile_phone`  varchar(11) NOT NULL ,
`create_time`  datetime NOT NULL ,
`update_time`  datetime NULL ,
`delete_flag`  int(1) NOT NULL ,
PRIMARY KEY (`id`)
)
;

-- ----------------------------
-- v1.15.0 允许负数充值
-- ----------------------------
INSERT INTO `global_config` VALUES (null, '允许企业负数充值', '允许企业负数充值,true为是，其它为否', 'ACCOUNT_CHARGE_ALLOW_NEGATIVE', 'false',NOW(), NOW(), '1', '1', '0', null);

-- ----------------------------
-- v1.15.0 包月赠送  by 林广阔 20171116
-- ----------------------------
ALTER TABLE `monthly_present_rule`
ADD COLUMN `prd_id` bigint(18) NOT NULL COMMENT '产品编码' AFTER `id`;

ALTER TABLE `monthly_present_rule`
ADD COLUMN `given_count` int(18) DEFAULT '0' COMMENT '已赠送月数' AFTER `activity_name`;

ALTER TABLE `monthly_present_record`
ADD COLUMN `give_month` int(18) DEFAULT '1' COMMENT '赠送月数' AFTER `boss_serial_num`;

-- ----------------------------
-- Table structure for monthly_present_record_copy
-- ----------------------------
DROP TABLE IF EXISTS `monthly_present_record_copy`;
CREATE TABLE `monthly_present_record_copy` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '标识',
  `rule_id` bigint(18) NOT NULL COMMENT '规则标识',
  `prd_id` bigint(25) NOT NULL,
  `mobile` varchar(15) NOT NULL COMMENT '手机号码',
  `status` tinyint(4) NOT NULL COMMENT '充值状态',
  `effect_type` int(2) DEFAULT '1' COMMENT '生效方式，立即生效1，次月生效2',
  `status_code` varchar(50) DEFAULT NULL COMMENT '状态码',
  `error_message` varchar(200) DEFAULT NULL COMMENT '充值失败信息',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  `sys_serial_num` varchar(50) DEFAULT NULL COMMENT '系统流水号',
  `boss_serial_num` varchar(50) DEFAULT NULL COMMENT 'BOSS流水号',
  PRIMARY KEY (`id`),
  KEY `rule_status` (`rule_id`,`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- v1.15.0 账户变更增加类型字段 luozuwu
-- ----------------------------
ALTER TABLE `account_change_detail`
ADD COLUMN `change_type`  int(18) NULL DEFAULT 2 COMMENT '账户变更类型：2、账户余额变更；9、账户最小额度变更；10、账户预警值变更；11、账户暂停值变更' AFTER `discount_value`;

ALTER TABLE `account_change_operator`
ADD COLUMN `change_type`  int(18) NULL DEFAULT 2 COMMENT '账户变更类型：2、账户余额变更；9、账户最小额度变更；10、账户预警值变更；11、账户暂停值变更' AFTER `discount_value`;

INSERT INTO `authority` VALUES (null, NULL, '企业最小值变更审批', 'ROLE_ACCOUNT_MIN_CHNAGE_APPROVAL', '201001', null,  NOW(), '1', '1', NOW(), '0');
INSERT INTO `authority` VALUES (null, NULL, '企业预警值变更审批', 'ROLE_ACCOUNT_ALERT_CHNAGE_APPROVAL', '201002', null,  NOW(), '1', '1', NOW(), '0');
INSERT INTO `authority` VALUES (null, NULL, '企业暂停值变更审批', 'ROLE_ACCOUNT_STOP_CHNAGE_APPROVAL', '201003', null,  NOW(), '1', '1', NOW(), '0');

INSERT INTO `authority` VALUES (null, NULL, '企业最小额度变更记录', 'ROLE_ACCOUNT_MIN_CHANGE_RECORD', '201004', null,  NOW(), '1', '1', NOW(), '0');
INSERT INTO `authority` VALUES (null, NULL, '企业最小额度变更记录', 'ROLE_ACCOUNT_ALERT_CHANGE_RECORD', '201005', null,  NOW(), '1', '1', NOW(), '0');
INSERT INTO `authority` VALUES (null, NULL, '企业暂停值变更记录', 'ROLE_ACCOUNT_STOP_CHANGE_RECORD', '201006', null,  NOW(), '1', '1', NOW(), '0');

-- ----------------------------
-- v1.15.0 渠道优先选择 林广阔
-- ----------------------------
ALTER TABLE `supplier_product_map`
ADD COLUMN `prior_flag`  int(1) DEFAULT '0' COMMENT '是否优先选择的供应商产品， 0:否；1：是' AFTER `delete_flag`;
