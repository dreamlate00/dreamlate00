DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `firstname` varchar(50) DEFAULT NULL,
  `lastpasswordresetdate` datetime DEFAULT NULL,
  `lastname` varchar(50) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
 
INSERT INTO `user` VALUES (1,'admin@admin.com',b'1','admin',NULL,'admin','123','sjy'),(2,'enabled@user.com',b'1','user',NULL,'user','123','user'),(3,'disabled@user.com',b'0','user',NULL,'user','123','test');
 
 
 
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
 
INSERT INTO `role` VALUES (1,'ROLE_ADMIN'),(2,'ROLE_USER'),(3,'ROLE_TEST');
 
 
DROP TABLE IF EXISTS `authority`;
CREATE TABLE `authority` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `pid` int(11) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
 
INSERT INTO `authority` VALUES (1,'admin',0,'/index1','管理'),(2,'user',0,'/index2','用戶'),(3,'test',0,'/index3','測試');
 
 
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  KEY `FKa68196081fvovjhkek5m97n3y` (`role_id`),
  KEY `FK859n2jvi8ivhui0rl0esws6o` (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
 
 
INSERT INTO `user_role` VALUES (1,1),(2,2),(3,3),(1,2);
 
DROP TABLE IF EXISTS `role_auth`;
CREATE TABLE `role_auth` (
  `auth_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  KEY `FKrcrl3r3y8xexlss4vuah9h5pj` (`role_id`),
  KEY `FKdclw9v91w4q8voap572llxar1` (`auth_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
 
 
INSERT INTO `role_auth` VALUES (1,1),(2,3),(2,3),(2,2),(3,3);