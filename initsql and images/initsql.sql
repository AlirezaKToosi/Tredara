-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: localhost    Database: tradera
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bid`
--

DROP TABLE IF EXISTS `bid`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bid` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` double DEFAULT NULL,
  `bid_time` datetime(6) DEFAULT NULL,
  `item_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9qeb5m2ef85uovk0nhso3vi48` (`item_id`),
  KEY `FKi1pwg1muxilapowsmifod8jtf` (`user_id`),
  CONSTRAINT `FK9qeb5m2ef85uovk0nhso3vi48` FOREIGN KEY (`item_id`) REFERENCES `item` (`id`),
  CONSTRAINT `FKi1pwg1muxilapowsmifod8jtf` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bid`
--

LOCK TABLES `bid` WRITE;
/*!40000 ALTER TABLE `bid` DISABLE KEYS */;
INSERT INTO `bid` VALUES (1,2100,'2023-10-10 03:00:00.000000',22,1),(2,2200,'2023-10-10 04:00:00.000000',22,1),(4,2400,'2023-10-09 21:23:43.549000',22,5),(5,2600,'2023-10-09 21:39:11.951000',22,5),(6,1001,'2023-10-09 22:55:52.526000',23,4),(7,501,'2023-10-09 23:43:02.681000',21,4),(8,510,'2023-10-09 23:43:38.637000',21,4),(9,511,'2023-10-09 23:43:57.986000',21,4),(10,511.5,'2023-10-09 23:45:33.133000',21,4),(11,2600.51,'2023-10-09 23:50:43.691000',22,4),(12,512,'2023-10-09 23:59:12.500000',21,4),(13,2601,'2023-10-10 00:09:08.173000',22,7),(14,3000,'2023-10-11 02:02:31.239000',31,3),(15,3100,'2023-10-11 02:04:47.918000',31,8),(16,3200,'2023-10-11 02:28:38.181000',32,8),(17,3300,'2023-10-11 02:29:08.373000',32,3),(18,3300,'2023-10-11 02:35:15.027000',34,8);
/*!40000 ALTER TABLE `bid` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `end_time` datetime(6) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `start_time` datetime(6) DEFAULT NULL,
  `start_price` double DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `seller_user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5yrcj8ste0ot08nfn08nl3x12` (`seller_user_id`),
  CONSTRAINT `FK5yrcj8ste0ot08nfn08nl3x12` FOREIGN KEY (`seller_user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `item_chk_1` CHECK ((`status` between 0 and 1))
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item`
--

LOCK TABLES `item` WRITE;
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
INSERT INTO `item` VALUES (20,'Antique radio','2023-10-10 18:48:00.000000','http://localhost:8080/files/1696594688192.png','2023-10-09 02:00:00.000000',800,NULL,'Radio',1),(21,'Golden candlestick','2023-10-10 02:00:00.000000','http://localhost:8080/files/1696595609916.png','2023-10-08 02:00:00.000000',500,NULL,'candlestick',1),(22,'Iphone 11','2023-10-11 02:00:00.000000','http://localhost:8080/files/1696763309297.png','2023-10-08 04:00:00.000000',2000,NULL,'Iphone',1),(23,'Black Shoes','2023-10-11 01:00:00.000000','http://localhost:8080/files/1696595619004.png','2023-10-08 02:00:00.000000',1000,NULL,'Shoes',1),(24,'Iphone 11','2023-10-10 00:00:00.000000','http://localhost:8080/files/1696595619004.png','2023-10-08 02:00:00.000000',2000,NULL,'Iphone2',1),(26,'Iphone 11','2023-10-10 02:00:00.000000','http://localhost:8080/files/1696978825109.png','2023-10-08 02:00:00.000000',2000,NULL,'Iphone3',1),(27,'Iphone 11','2023-10-10 02:00:00.000000','http://localhost:8080/files/1696979043048.png','2023-10-08 02:00:00.000000',2000,NULL,'Iphone4',1),(28,'Iphone 11','2023-10-10 02:00:00.000000','http://localhost:8080/files/1696979308957.png','2023-10-08 02:00:00.000000',2000,NULL,'Iphone4',1),(29,'Iphone 11','2023-10-10 02:00:00.000000','http://localhost:8080/files/1696979331268.png','2023-10-08 02:00:00.000000',2000,NULL,'Iphone5',1),(30,'Iphone 11','2023-10-10 02:00:00.000000','http://localhost:8080/files/1696982247123.png','2023-10-08 02:00:00.000000',2000,NULL,'Iphone6',1),(31,'Iphone 11','2023-10-13 02:00:00.000000','http://localhost:8080/files/1696982535799.png','2023-10-08 02:00:00.000000',2000,NULL,'Iphone6',1),(32,'Iphone 11','2023-10-13 02:00:00.000000','http://localhost:8080/files/1696984103936.png','2023-10-08 02:00:00.000000',2000,NULL,'Iphone8',1),(33,'Iphone 11','2023-10-13 02:00:00.000000','http://localhost:8080/files/1696984482372.png','2023-10-08 02:00:00.000000',2000,NULL,'Iphone9',1),(34,'Iphone 11','2023-10-13 02:00:00.000000','http://localhost:8080/files/1696984497976.png','2023-10-08 02:00:00.000000',2000,NULL,'Iphone10',1);
/*!40000 ALTER TABLE `item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log`
--

DROP TABLE IF EXISTS `log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `action_details` varchar(255) DEFAULT NULL,
  `action_type` tinyint DEFAULT NULL,
  `time_stamp` datetime(6) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `item_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKefuitn33qkpy6nonixjyyu3u0` (`user_id`),
  KEY `FKs2eorr9f8yn1hw6u4tnaukls0` (`item_id`),
  CONSTRAINT `FKefuitn33qkpy6nonixjyyu3u0` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKs2eorr9f8yn1hw6u4tnaukls0` FOREIGN KEY (`item_id`) REFERENCES `item` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log`
--

LOCK TABLES `log` WRITE;
/*!40000 ALTER TABLE `log` DISABLE KEYS */;
INSERT INTO `log` VALUES (2,'User logged in successfully.',0,'2023-10-09 03:18:04.266000',2,NULL),(3,'User login failed for email: Ali@gmail.com',1,'2023-10-09 03:18:15.214000',2,NULL),(4,'User login failed for email: Ali@gmail.com',1,'2023-10-09 10:27:00.557000',2,NULL),(5,'User login failed for email: Ali@gmail.com',1,'2023-10-09 12:42:20.452000',2,NULL),(6,'User logged in successfully.',0,'2023-10-09 12:42:30.450000',2,NULL),(7,'User logged in successfully.',0,'2023-10-09 15:30:55.925000',1,NULL),(8,'User logged in successfully.',0,'2023-10-09 15:38:33.291000',3,NULL),(9,'User logged in successfully.',0,'2023-10-09 15:44:28.746000',1,NULL),(10,'User logged in successfully.',0,'2023-10-09 15:48:09.010000',1,NULL),(11,'User login failed for email: Ali2@gmail.com',1,'2023-10-09 16:16:11.836000',3,NULL),(12,'User login failed for email: Ali21111@gmail.com',1,'2023-10-09 16:16:27.543000',NULL,NULL),(13,'User logged in successfully.',0,'2023-10-09 19:31:02.430000',1,NULL),(14,'User logged in successfully.',0,'2023-10-09 19:32:24.008000',1,NULL),(15,'User login failed for email: Ali@ali.com',1,'2023-10-09 19:35:15.758000',NULL,NULL),(16,'User logged in successfully.',0,'2023-10-09 19:36:24.403000',4,NULL),(17,'User logged in successfully.',0,'2023-10-09 19:37:47.395000',4,NULL),(18,'User login failed for email: Ali@ali.com',1,'2023-10-09 22:53:51.310000',NULL,NULL),(19,'User logged in successfully.',0,'2023-10-09 22:53:58.832000',1,NULL),(20,'User logged in successfully.',0,'2023-10-09 22:54:40.584000',4,NULL),(21,'User logged in successfully.',0,'2023-10-09 23:42:31.789000',4,NULL),(22,'User logged in successfully.',0,'2023-10-09 23:43:32.209000',4,NULL),(23,'User logged in successfully.',0,'2023-10-09 23:45:12.199000',4,NULL),(24,'User logged in successfully.',0,'2023-10-09 23:50:33.770000',4,NULL),(25,'User logged in successfully.',0,'2023-10-09 23:59:06.542000',4,NULL),(26,'User logged in successfully.',0,'2023-10-10 00:11:32.730000',7,NULL),(27,'User login failed for email: Ali21111@gmail.com',1,'2023-10-11 00:59:12.821000',NULL,NULL),(28,'User login failed for email: Ali2@gmail.com',1,'2023-10-11 00:59:28.650000',3,NULL),(29,'User logged in successfully.',0,'2023-10-11 00:59:44.722000',3,NULL),(30,'User Created new item with email: Ali2@gmail.com and for item:26',2,'2023-10-11 01:00:25.238000',3,26),(31,'User logged in successfully.',0,'2023-10-11 01:03:54.861000',8,NULL),(32,'User logged in successfully.',0,'2023-10-11 01:03:56.808000',8,NULL),(33,'User Created new item with email: Ali3@gmail.com and for item:27',2,'2023-10-11 01:04:03.064000',8,27),(34,'User logged in successfully.',0,'2023-10-11 01:05:27.899000',8,NULL),(35,'User logged in successfully.',0,'2023-10-11 01:08:25.127000',8,NULL),(36,'User Created new item with email: Ali3@gmail.com and for item:28',2,'2023-10-11 01:08:29.104000',8,28),(37,'User logged in successfully.',0,'2023-10-11 01:08:44.736000',3,NULL),(38,'User Created new item with email: Ali2@gmail.com and for item:29',2,'2023-10-11 01:08:51.305000',3,29),(39,'User logged in successfully.',0,'2023-10-11 01:57:16.477000',3,NULL),(40,'User Created new item with email: Ali2@gmail.com and for item:30',2,'2023-10-11 01:57:27.260000',3,30),(41,'User Created new item with email: Ali2@gmail.com and for item:31',2,'2023-10-11 02:02:15.833000',3,31),(42,'User Created new item with email: Ali2@gmail.com and for item:31',3,'2023-10-11 02:02:31.320000',3,31),(43,'User logged in successfully.',0,'2023-10-11 02:04:22.427000',8,NULL),(44,'User Created new item with email: Ali3@gmail.com and for item:31',3,'2023-10-11 02:04:47.930000',8,31),(45,'User logged in successfully.',0,'2023-10-11 02:28:13.839000',8,NULL),(46,'User Created new item with email: Ali3@gmail.com and for item:32',2,'2023-10-11 02:28:24.040000',8,32),(47,'User Created new item with email: Ali3@gmail.com and for item:32',3,'2023-10-11 02:28:38.221000',8,32),(48,'User logged in successfully.',0,'2023-10-11 02:28:59.118000',3,NULL),(49,'User Created new item with email: Ali2@gmail.com and for item:32',3,'2023-10-11 02:29:08.386000',3,32),(50,'User Created new item with email: Ali2@gmail.com and for item:33',2,'2023-10-11 02:34:42.555000',3,33),(51,'User logged in successfully.',0,'2023-10-11 02:34:52.079000',8,NULL),(52,'User Created new item with email: Ali3@gmail.com and for item:34',2,'2023-10-11 02:34:58.005000',8,34),(53,'User Added new bid with email: Ali3@gmail.com and for item:34',3,'2023-10-11 02:35:15.055000',8,34);
/*!40000 ALTER TABLE `log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `notification_id` bigint NOT NULL AUTO_INCREMENT,
  `status` tinyint DEFAULT NULL,
  `time_stamp` datetime(6) DEFAULT NULL,
  `bid_id` bigint DEFAULT NULL,
  `item_id` bigint DEFAULT NULL,
  PRIMARY KEY (`notification_id`),
  KEY `FKfcmgmhqujsnnxliv3l1apphr6` (`bid_id`),
  KEY `FKc1cu9t44n9goseax32ybpwlrb` (`item_id`),
  CONSTRAINT `FKc1cu9t44n9goseax32ybpwlrb` FOREIGN KEY (`item_id`) REFERENCES `item` (`id`),
  CONSTRAINT `FKfcmgmhqujsnnxliv3l1apphr6` FOREIGN KEY (`bid_id`) REFERENCES `bid` (`id`),
  CONSTRAINT `notification_chk_1` CHECK ((`status` between 0 and 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` tinyint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
  CONSTRAINT `users_chk_1` CHECK ((`role` between 0 and 1))
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Alireza@ali.com','Alireza','$2a$10$xf5BY.QgoHq2Rjkg8yHhi.PoKBA8LQsKFsJnBRmkRDfyMZgZz80Iu',1),(2,'Ali@gmail.com','Ali','$2a$10$KMLmV6DLdOHRKoTgZy/uiOqt2yD/DnWjt0z6STEjklXx/2xDYiEb.',1),(3,'Ali2@gmail.com','Ali2','$2a$10$RYYv3m1rE0K5vDYsamEPfOUgIcgcuPmjeA1bczklT3Kggb87mbfwS',1),(4,'Bahareh@gmail.com','Bahareh','$2a$10$bdMeftWfgZaNcKvDHYnV1ea.GDsy7nPfFz9rGDjBmJJV46a0NWs02',1),(5,'Hamid@gmail.com','Hamid','$2a$10$LNdcNboGXfE2ocD3fvxYUO2m7fQCVvjynOMA38Gxd2J5A5.6im15G',1),(6,'Ziba@gmail.com','Ziba','$2a$10$RtSG7xXUMhNlb961rHe4g.2CSx./CWScnItqAykq8zKyNgjv9/jGS',1),(7,'fariba@gmail.com','fariba','$2a$10$viubfyoP6CMHXvl.OUgR5.JifBliTWAv/sq4aAQwnLYEQ97Pg/jMO',1),(8,'Ali3@gmail.com','Ali3','$2a$10$fqYL570NLsruVNWzDsbcSOxy4KJuBDPb4BcORT/ChD2C8aVev9WYm',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-11  9:59:17
