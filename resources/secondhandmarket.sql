/*
Navicat MySQL Data Transfer

Source Server         : 119.29.194.197_3306
Source Server Version : 50639
Source Host           : 119.29.194.197:3306
Source Database       : secondhandmarket

Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2018-04-28 00:46:50
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for collect
-- ----------------------------
DROP TABLE IF EXISTS `collect`;
CREATE TABLE `collect` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'collect收藏夹表',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `goods_id` int(11) NOT NULL COMMENT '商品id',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `goods_id` (`goods_id`),
  CONSTRAINT `collect_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `collect_ibfk_2` FOREIGN KEY (`goods_id`) REFERENCES `goods` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of collect
-- ----------------------------
INSERT INTO `collect` VALUES ('3', '1', '3');
INSERT INTO `collect` VALUES ('4', '3', '3');

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '商品表',
  `title` varchar(255) NOT NULL COMMENT '文章标题',
  `picture` varchar(255) NOT NULL COMMENT '一张图片',
  `address` varchar(255) NOT NULL COMMENT '交易地址',
  `type_id` int(11) NOT NULL COMMENT '商品类别',
  `price` decimal(10,0) NOT NULL COMMENT '价格',
  `detail` varchar(255) NOT NULL COMMENT '详情描述',
  `detail_img` varchar(255) DEFAULT NULL COMMENT '商品详细图片',
  `num` int(11) NOT NULL COMMENT '商品数量',
  `link_way` varchar(255) NOT NULL COMMENT '联系方式',
  `link_people` varchar(255) NOT NULL COMMENT '联系人',
  `create_time` datetime NOT NULL COMMENT '生成时间',
  `check_result` varchar(255) NOT NULL COMMENT '审核结果',
  `check_status` int(11) NOT NULL COMMENT '审核状态',
  `is_del` int(11) NOT NULL COMMENT '删除状态',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `view_count` int(11) DEFAULT NULL COMMENT '浏览次数',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `type_id` (`type_id`),
  CONSTRAINT `goods_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` VALUES ('3', 'test', 'test', 'test', '1', '1', 'test', 'test', '1', 'test', 'test', '1899-12-30 01:00:00', 'test', '1', '0', '1', '1');

-- ----------------------------
-- Table structure for manager
-- ----------------------------
DROP TABLE IF EXISTS `manager`;
CREATE TABLE `manager` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '管理员表',
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of manager
-- ----------------------------
INSERT INTO `manager` VALUES ('1', 'zhangweip', '123456');
INSERT INTO `manager` VALUES ('2', 'zwp', '123456');

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户订单表',
  `user_id` int(11) NOT NULL COMMENT '买方id',
  `user_name` varchar(255) NOT NULL COMMENT '买方用户名',
  `link_phone` varchar(255) NOT NULL COMMENT '买方联系方式',
  `seller_id` int(11) NOT NULL COMMENT '卖方用户id',
  `goods_id` int(11) NOT NULL COMMENT '商品id',
  `goods_num` int(11) NOT NULL COMMENT '商品数量',
  `goods_price` decimal(10,0) NOT NULL COMMENT '商品单价',
  `goods_total` decimal(10,0) NOT NULL COMMENT '商品总价',
  `message` varchar(255) NOT NULL COMMENT '备注留言',
  `creat_time` datetime NOT NULL COMMENT '订单时间',
  `orders_status` int(11) NOT NULL COMMENT '订单状态',
  `del_status` int(11) NOT NULL DEFAULT '0' COMMENT '删除状态：0存在、1已删除',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `seller_id` (`seller_id`),
  KEY `goods_id` (`goods_id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`seller_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `orders_ibfk_3` FOREIGN KEY (`goods_id`) REFERENCES `goods` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES ('5', '1', 'string', 'string', '2', '3', '1', '1', '1', 'string', '2018-04-26 21:23:43', '1', '0');

-- ----------------------------
-- Table structure for type
-- ----------------------------
DROP TABLE IF EXISTS `type`;
CREATE TABLE `type` (
  `id` int(11) NOT NULL COMMENT '类别表',
  `parent_id` int(11) NOT NULL COMMENT '父节点id',
  `name` varchar(255) NOT NULL COMMENT '类别名',
  `del_status` int(11) NOT NULL COMMENT '删除状态',
  `is_parent` int(11) NOT NULL COMMENT '是否是父节点',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of type
-- ----------------------------
INSERT INTO `type` VALUES ('1', '0', '图书、音像、电子书刊', '0', '1', '1899-12-30 01:00:00', '1899-12-30 01:00:00');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户表',
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `mobile` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `identity` varchar(255) DEFAULT NULL COMMENT '身份证',
  `headPic` varchar(255) DEFAULT NULL COMMENT '头像',
  `salt` varchar(255) DEFAULT NULL COMMENT '密码加盐',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'test', 'd67c5cbf5b01c9f91932e3b8def5e5f8', 'test', 'test@test.com', 'test', null, 'string');
INSERT INTO `user` VALUES ('2', '123', '7c9c0b787d24816fe630fc8619564306', '123', '123@123.com', '123', null, '123');
INSERT INTO `user` VALUES ('3', '111', '7fa8282ad93047a4d6fe6111c93b308a', '111', '111@111.com', '111', null, '111');
INSERT INTO `user` VALUES ('4', 'zwp', '4297f44b13955235245b2497399d7a93', '13420118171', '745233700', '11111111111111', 'http:123456.com', '123');

-- ----------------------------
-- Table structure for want
-- ----------------------------
DROP TABLE IF EXISTS `want`;
CREATE TABLE `want` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '需求表',
  `title` varchar(255) NOT NULL COMMENT '需求标题',
  `address` varchar(255) NOT NULL COMMENT '交易地址',
  `type_id` int(11) NOT NULL COMMENT '商品类别',
  `price` decimal(10,0) NOT NULL COMMENT '价格',
  `detail` varchar(255) NOT NULL COMMENT '需求细节',
  `link_people` varchar(255) NOT NULL COMMENT '联系人',
  `link_way` varchar(255) NOT NULL COMMENT '联系方式',
  `num` int(11) NOT NULL COMMENT '数量',
  `creat_time` datetime NOT NULL COMMENT '生成时间',
  `is_del` int(11) NOT NULL COMMENT '删除状态',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `type_id` (`type_id`),
  CONSTRAINT `want_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of want
-- ----------------------------
