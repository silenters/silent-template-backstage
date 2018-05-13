/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.28
Source Server Version : 50556
Source Host           : 192.168.1.28:3306
Source Database       : template_backstage

Target Server Type    : MYSQL
Target Server Version : 50556
File Encoding         : 65001

Date: 2018-04-11 15:25:35
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for auth_page
-- ----------------------------
DROP TABLE IF EXISTS `auth_page`;
CREATE TABLE `auth_page` (
  `id` varchar(50) NOT NULL,
  `name` varchar(30) DEFAULT NULL,
  `url` varchar(40) DEFAULT NULL,
  `description` varchar(60) DEFAULT NULL,
  `parent_id` varchar(50) DEFAULT NULL,
  `level` int(5) DEFAULT NULL,
  `sequence` int(5) DEFAULT NULL,
  `visible` tinyint(5) DEFAULT NULL,
  `time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of auth_page
-- ----------------------------
INSERT INTO `auth_page` VALUES ('15195380-315f-42d3-bcaf-3a59862a08ac', '添加角色', 'admin/authority/role/add', null, '32077fc4-7368-476f-a33d-b23bdbfd5c95', '0', '1', '1', '1514882644798');
INSERT INTO `auth_page` VALUES ('22cbecbd-a28c-4b4d-993c-553d231dd313', '删除角色', 'admin/authority/role/del', null, '32077fc4-7368-476f-a33d-b23bdbfd5c95', '0', '3', '1', '1514882644798');
INSERT INTO `auth_page` VALUES ('274cf5c2-b25c-42ea-9497-2b7c59621863', '删除页面', 'admin/authority/page/del', null, '3cbeda11-9887-46ac-8c75-0799b6110263', '0', '3', '1', '1514882644798');
INSERT INTO `auth_page` VALUES ('32077fc4-7368-476f-a33d-b23bdbfd5c95', '角色管理', 'admin/authority/role/index', null, 'cf6b317e-f217-44b3-9a8c-c0ecf467ce19', '0', '1', '1', '1514882644798');
INSERT INTO `auth_page` VALUES ('3cbeda11-9887-46ac-8c75-0799b6110263', '页面管理', 'admin/authority/page/index', null, 'cf6b317e-f217-44b3-9a8c-c0ecf467ce19', '0', '1', '1', '0');
INSERT INTO `auth_page` VALUES ('6206f1cd-5e1c-4a1b-9cfd-8d1b13437278', '添加用户', 'admin/authority/user/add', null, '90c58a63-c287-47cb-a78a-fe567a632200', '0', '4', '1', '1514882644798');
INSERT INTO `auth_page` VALUES ('6be126a9-2efb-4ec1-8e50-f1b18c70107f', '编辑页面', 'admin/authority/page/edit', null, '3cbeda11-9887-46ac-8c75-0799b6110263', '0', '2', '1', '1514882644798');
INSERT INTO `auth_page` VALUES ('6eab5c46-b355-4273-9e76-2366abe39e9f', '用户管理', '', null, 'd2996d07-eb71-4419-ab0d-7598aa994b8d', '0', '2', '1', '0');
INSERT INTO `auth_page` VALUES ('7f58b6a2-721e-4625-b199-665ad79d64dd', '添加页面', 'admin/authority/page/add', null, '3cbeda11-9887-46ac-8c75-0799b6110263', '0', '1', '1', '1514882644798');
INSERT INTO `auth_page` VALUES ('90c58a63-c287-47cb-a78a-fe567a632200', '用户列表', 'admin/authority/user/index', null, '6eab5c46-b355-4273-9e76-2366abe39e9f', '0', '2', '1', '0');
INSERT INTO `auth_page` VALUES ('92474134-cfc2-410d-afdb-83fe71f4af8d', '删除用户', 'admin/authority/user/del', null, '90c58a63-c287-47cb-a78a-fe567a632200', '0', '2', '1', '1514882644798');
INSERT INTO `auth_page` VALUES ('a9cca4f4-7fa0-4b7a-846c-3da84f1f5020', '编辑用户', 'admin/authority/user/edit', null, '90c58a63-c287-47cb-a78a-fe567a632200', '0', '3', '1', '1514882644798');
INSERT INTO `auth_page` VALUES ('cea6bee2-480f-445f-adc2-7281250b92bf', '编辑角色', 'admin/authority/role/edit', null, '32077fc4-7368-476f-a33d-b23bdbfd5c95', '0', '2', '1', '1514882644798');
INSERT INTO `auth_page` VALUES ('cf6b317e-f217-44b3-9a8c-c0ecf467ce19', '权限管理', null, null, 'd2996d07-eb71-4419-ab0d-7598aa994b8d', '0', '1', '1', '1514882644798');
INSERT INTO `auth_page` VALUES ('d2996d07-eb71-4419-ab0d-7598aa994b8d', '管理员', '', null, '', '0', '5', '1', '1520908559898');
INSERT INTO `auth_page` VALUES ('ffa1ee6a-5be7-456a-beaa-cb30de7ed74e', '个人信息', 'admin/user/info/index', null, 'd2996d07-eb71-4419-ab0d-7598aa994b8d', '0', '3', '1', '1522116262901');

-- ----------------------------
-- Table structure for auth_role
-- ----------------------------
DROP TABLE IF EXISTS `auth_role`;
CREATE TABLE `auth_role` (
  `id` varchar(50) NOT NULL,
  `name` varchar(30) DEFAULT NULL,
  `description` varchar(60) DEFAULT NULL,
  `time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of auth_role
-- ----------------------------
INSERT INTO `auth_role` VALUES ('0eb10c17-3bb0-4823-8be3-1200d9b7314a', '超级管理员', '拥有系统权限', '1521531401717');

-- ----------------------------
-- Table structure for auth_role_page
-- ----------------------------
DROP TABLE IF EXISTS `auth_role_page`;
CREATE TABLE `auth_role_page` (
  `id` varchar(50) NOT NULL,
  `role_id` varchar(50) DEFAULT NULL,
  `page_id` varchar(50) DEFAULT NULL,
  `time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of auth_role_page
-- ----------------------------
INSERT INTO `auth_role_page` VALUES ('1093da6a-e853-4286-8996-6985f2e7caf9', '0eb10c17-3bb0-4823-8be3-1200d9b7314a', 'd2996d07-eb71-4419-ab0d-7598aa994b8d', '1514882644798');
INSERT INTO `auth_role_page` VALUES ('15420b55-45a4-4819-ad61-db1d2fc7ff61', '0eb10c17-3bb0-4823-8be3-1200d9b7314a', '90c58a63-c287-47cb-a78a-fe567a632200', '1514882644798');
INSERT INTO `auth_role_page` VALUES ('1c25e7f5-40ef-4248-90f8-2766a39c33f5', '0eb10c17-3bb0-4823-8be3-1200d9b7314a', '7f58b6a2-721e-4625-b199-665ad79d64dd', '1514882644798');
INSERT INTO `auth_role_page` VALUES ('28346017-d5bf-42cc-b7ff-8aff484a6332', '0eb10c17-3bb0-4823-8be3-1200d9b7314a', 'cea6bee2-480f-445f-adc2-7281250b92bf', '1514882644798');
INSERT INTO `auth_role_page` VALUES ('445c1ca2-a6ad-46c1-9a71-76299bbc63fc\r\n', '0eb10c17-3bb0-4823-8be3-1200d9b7314a', '6be126a9-2efb-4ec1-8e50-f1b18c70107f', '1514882644798');
INSERT INTO `auth_role_page` VALUES ('467ad59c-79b7-42f3-aa77-ecb08f7048c4', '0eb10c17-3bb0-4823-8be3-1200d9b7314a', '15195380-315f-42d3-bcaf-3a59862a08ac', '1514882644798');
INSERT INTO `auth_role_page` VALUES ('512a0a92-a3cb-41b4-a36f-f56bd72950cd', '0eb10c17-3bb0-4823-8be3-1200d9b7314a', '6eab5c46-b355-4273-9e76-2366abe39e9f', '1514882644798');
INSERT INTO `auth_role_page` VALUES ('61ce20a0-23da-4ee8-8d56-710b708caa91', '0eb10c17-3bb0-4823-8be3-1200d9b7314a', 'a9cca4f4-7fa0-4b7a-846c-3da84f1f5020', '1514882644798');
INSERT INTO `auth_role_page` VALUES ('62dd3731-a0ef-4056-b185-c5f2426d7d39', '0eb10c17-3bb0-4823-8be3-1200d9b7314a', '6206f1cd-5e1c-4a1b-9cfd-8d1b13437278', '1514882644798');
INSERT INTO `auth_role_page` VALUES ('6527b2df-898e-48db-866c-7dc585c0474f', '0eb10c17-3bb0-4823-8be3-1200d9b7314a', '22cbecbd-a28c-4b4d-993c-553d231dd313', '1514882644798');
INSERT INTO `auth_role_page` VALUES ('7faa902d-0871-4c1c-9207-959040dea409', '0eb10c17-3bb0-4823-8be3-1200d9b7314a', '92474134-cfc2-410d-afdb-83fe71f4af8d', '1514882644798');
INSERT INTO `auth_role_page` VALUES ('8f256ca6-0ad1-436b-a700-bec71672402e', '0eb10c17-3bb0-4823-8be3-1200d9b7314a', '3cbeda11-9887-46ac-8c75-0799b6110263', '1514882644798');
INSERT INTO `auth_role_page` VALUES ('9558001c-53c4-4834-a4a2-c14fc9054580', '0eb10c17-3bb0-4823-8be3-1200d9b7314a', '32077fc4-7368-476f-a33d-b23bdbfd5c95', '1514882644798');
INSERT INTO `auth_role_page` VALUES ('c7a11092-a7f2-4d82-8aeb-fad74af05dcb', '0eb10c17-3bb0-4823-8be3-1200d9b7314a', 'cf6b317e-f217-44b3-9a8c-c0ecf467ce19', '1514882644798');
INSERT INTO `auth_role_page` VALUES ('e0223512-56d4-4eb7-bf1c-aefa8a73f9d6', '0eb10c17-3bb0-4823-8be3-1200d9b7314a', 'ffa1ee6a-5be7-456a-beaa-cb30de7ed74e', '1514882644798');
INSERT INTO `auth_role_page` VALUES ('f1aca3a1-1c0a-42cb-b44d-01284d89a3ec', '0eb10c17-3bb0-4823-8be3-1200d9b7314a', '274cf5c2-b25c-42ea-9497-2b7c59621863', '1514882644798');

-- ----------------------------
-- Table structure for auth_user
-- ----------------------------
DROP TABLE IF EXISTS `auth_user`;
CREATE TABLE `auth_user` (
  `id` varchar(50) NOT NULL,
  `username` varchar(20) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `realname` varchar(20) DEFAULT NULL,
  `company` varchar(30) DEFAULT NULL,
  `job` varchar(40) DEFAULT NULL,
  `salt` varchar(20) DEFAULT NULL,
  `status` tinyint(2) DEFAULT NULL,
  `time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of auth_user
-- ----------------------------
INSERT INTO `auth_user` VALUES ('9cbd1eeb-09a5-4208-9f7f-d26df6f4522c', 'RR', '2adc10cf3196206310828d346c0f9bda', 'rr', '天津星云时代科技有限公司', '前端开发', 'lx2FjO', '1', '1521189623925');
INSERT INTO `auth_user` VALUES ('a6c5d56b-1470-480c-ad75-8863e78f9f88', 'admin', 'fe94a58d3f389810e9b7ab86ee487f40', 'Admin', '天津星云时代科技有限公司', '大数据研发工程师', 'srOpYg', '1', '1521087394437');

-- ----------------------------
-- Table structure for auth_user_role
-- ----------------------------
DROP TABLE IF EXISTS `auth_user_role`;
CREATE TABLE `auth_user_role` (
  `id` varchar(50) NOT NULL,
  `user_id` varchar(50) DEFAULT NULL,
  `role_id` varchar(50) DEFAULT NULL,
  `time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of auth_user_role
-- ----------------------------
INSERT INTO `auth_user_role` VALUES ('e88a836a-fd3a-4df1-90e1-7d4f07fa8a10', 'a6c5d56b-1470-480c-ad75-8863e78f9f88', '0eb10c17-3bb0-4823-8be3-1200d9b7314a', '1514947486547');
