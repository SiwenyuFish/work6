CREATE DATABASE /*!32312 IF NOT EXISTS*/`memo-transaction` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `memo-transaction`;

/*Table structure for table `things` */

DROP TABLE IF EXISTS `things`;

CREATE TABLE `things` (
                          `id` bigint NOT NULL COMMENT '事务id',
                          `userid` bigint NOT NULL COMMENT '用户id',
                          `content` varchar(255) NOT NULL COMMENT '事项内容',
                          `status` int NOT NULL DEFAULT '0' COMMENT '状态',
                          `created_at` datetime NOT NULL COMMENT '创建时间',
                          `updated_at` datetime NOT NULL COMMENT '更新时间',
                          PRIMARY KEY (`id`),
                          KEY `things_userid_index` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

/*Data for the table `things` */

insert  into `things`(`id`,`userid`,`content`,`status`,`created_at`,`updated_at`) values
                                                                                      (270279476280168448,270035912765870080,'待办事项',1,'2024-05-09 12:40:15','2024-05-09 20:41:25'),
                                                                                      (270279658044526592,270035912765870080,'待办事项2',1,'2024-05-09 12:40:59','2024-05-09 20:41:36'),
                                                                                      (270279711173775360,270035912765870080,'待办事项3',1,'2024-05-09 12:41:11','2024-05-09 20:41:36'),
                                                                                      (270287599686520832,270035912765870080,'待办事项4',1,'2024-05-09 13:12:32','2024-05-09 20:41:36'),
                                                                                      (270288023625797632,270035912765870080,'待办事项5',1,'2024-05-09 13:14:13','2024-05-09 20:41:36'),
                                                                                      (270996874666840064,270035912765870080,'事项测试',1,'2024-05-11 12:10:54','2024-05-11 15:53:33'),
                                                                                      (270996880538865664,270035912765870080,'事项测试',1,'2024-05-11 12:10:56','2024-05-11 15:53:33'),
                                                                                      (270996884301156352,270035912765870080,'事项测试',1,'2024-05-11 12:10:57','2024-05-11 15:53:33'),
                                                                                      (270996888000532480,270035912765870080,'事项测试',1,'2024-05-11 12:10:57','2024-05-11 15:53:33'),
                                                                                      (270996891414695936,270035912765870080,'事项测试',1,'2024-05-11 12:10:58','2024-05-11 15:53:33'),
                                                                                      (270996894778527744,270035912765870080,'事项测试',1,'2024-05-11 12:10:59','2024-05-11 15:53:33'),
                                                                                      (270996898012336128,270035912765870080,'事项测试',1,'2024-05-11 12:11:00','2024-05-11 15:53:33'),
                                                                                      (270996901309059072,270035912765870080,'事项测试',1,'2024-05-11 12:11:01','2024-05-11 15:53:33'),
                                                                                      (270996904404455424,270035912765870080,'事项测试',1,'2024-05-11 12:11:01','2024-05-11 15:53:33'),
                                                                                      (270996907797647360,270035912765870080,'事项测试',1,'2024-05-11 12:11:02','2024-05-11 15:53:33'),
                                                                                      (270996911744487424,270035912765870080,'事项测试',1,'2024-05-11 12:11:03','2024-05-11 15:53:33'),
                                                                                      (271052632678862848,270035912765870080,'事项测试',1,'2024-05-11 15:52:32','2024-05-11 15:53:33'),
                                                                                      (271133443767799808,270035912765870080,'事项测试',0,'2024-05-11 21:13:42','2024-05-11 21:13:42'),
                                                                                      (271134179658436608,270035912765870080,'事项测试',0,'2024-05-11 21:16:32','2024-05-11 21:16:32'),
                                                                                      (272197051599687680,270035912765870080,'事项测试',0,'2024-05-14 19:40:04','2024-05-14 19:40:04'),
                                                                                      (272199186986962944,270035912765870080,'事项测试',0,'2024-05-14 19:48:29','2024-05-14 19:48:29'),
                                                                                      (272199458488455168,270035912765870080,'事项测试',0,'2024-05-14 19:49:34','2024-05-14 19:49:34'),
                                                                                      (272201079951855616,270035912765870080,'事项测试',0,'2024-05-14 19:56:01','2024-05-14 19:56:01'),
                                                                                      (272202039289843712,270035912765870080,'事项测试',0,'2024-05-14 19:59:49','2024-05-14 19:59:49'),
                                                                                      (272202466160939008,270035912765870080,'事项测试',0,'2024-05-14 20:01:34','2024-05-14 20:01:34'),
                                                                                      (272203453621735424,270035912765870080,'事项测试',0,'2024-05-14 20:05:27','2024-05-14 20:05:27'),
                                                                                      (272204272916107264,270035912765870080,'事项测试',0,'2024-05-14 20:08:43','2024-05-14 20:08:43'),
                                                                                      (272204561568108544,270035912765870080,'事项测试',0,'2024-05-14 20:09:51','2024-05-14 20:09:51'),
                                                                                      (272205732580036608,270035912765870080,'事项测试',0,'2024-05-14 20:14:30','2024-05-14 20:14:30'),
                                                                                      (272205792617304064,270035912765870080,'事项测试',0,'2024-05-14 20:14:44','2024-05-14 20:14:44'),
                                                                                      (272212111772487680,270035912765870080,'事项测试',0,'2024-05-14 20:39:51','2024-05-14 20:39:51'),
                                                                                      (272212291720712192,270035912765870080,'事项测试',0,'2024-05-14 20:40:34','2024-05-14 20:40:34'),
                                                                                      (272212307558404096,270035912765870080,'事项测试',0,'2024-05-14 20:40:38','2024-05-14 20:40:38'),
                                                                                      (272212353037242368,270035912765870080,'事项测试',0,'2024-05-14 20:40:48','2024-05-14 20:40:48'),
                                                                                      (272212377926242304,270035912765870080,'事项测试',0,'2024-05-14 20:40:54','2024-05-14 20:40:54'),
                                                                                      (273872555549528064,270035912765870080,'事项测试',0,'2024-05-19 10:37:55','2024-05-19 10:37:55'),
                                                                                      (273873696211472384,270035912765870080,'事项测试',0,'2024-05-19 10:42:27','2024-05-19 10:42:27'),
                                                                                      (273873868597366784,270035912765870080,'事项测试',0,'2024-05-19 10:43:08','2024-05-19 10:43:08');

CREATE DATABASE /*!32312 IF NOT EXISTS*/`memo-user` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `memo-user`;

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
                        `id` bigint NOT NULL COMMENT '用户id',
                        `username` varchar(50) NOT NULL COMMENT '用户名',
                        `password` varchar(50) NOT NULL COMMENT '密码',
                        `count` int NOT NULL DEFAULT '0',
                        PRIMARY KEY (`id`),
                        KEY `user_id_username_index` (`id`,`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

/*Data for the table `user` */

insert  into `user`(`id`,`username`,`password`,`count`) values
                                                            (270035912765870080,'Pink','e10adc3949ba59abbe56e057f20f883e',38),
                                                            (270059538252697600,'Water','e10adc3949ba59abbe56e057f20f883e',0),
                                                            (270253443090878464,'Floyd','21218cca77804d2ba1922c33e0151105',0);