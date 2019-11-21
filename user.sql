/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50724
Source Host           : localhost:3306
Source Database       : db_student_ssm

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2019-11-21 10:40:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) NOT NULL,
  `password` varchar(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'admin', '21232f297a57a5a743894a0e4a801fc3');
INSERT INTO `user` VALUES ('2', '小小菜鸟', '123456');
INSERT INTO `user` VALUES ('32', 'leo', 'c4ca4238a0b923820dcc509a6f75849b');
INSERT INTO `user` VALUES ('33', 'test', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `user` VALUES ('34', 'test1', 'c16a5320fa475530d9583c34fd356ef5');
INSERT INTO `user` VALUES ('35', '1', 'c4ca4238a0b923820dcc509a6f75849b');
INSERT INTO `user` VALUES ('36', '2', 'c81e728d9d4c2f636f067f89cc14862c');
INSERT INTO `user` VALUES ('37', '3', 'eccbc87e4b5ce2fe28308fd9f2a7baf3');
INSERT INTO `user` VALUES ('38', '4', 'a87ff679a2f3e71d9181a67b7542122c');
INSERT INTO `user` VALUES ('39', '5', 'e4da3b7fbbce2345d7772b0674a318d5');
INSERT INTO `user` VALUES ('40', '6', '1679091c5a880faf6fb5e6087eb1b2dc');
