-- MySQL dump 10.13  Distrib 8.0.32, for macos13.0 (arm64)
--
-- Host: localhost    Database: datacap
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `audit_plugin`
--

DROP TABLE IF EXISTS `audit_plugin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audit_plugin` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `state` varchar(255) DEFAULT NULL,
  `create_time` mediumtext,
  `end_time` mediumtext,
  `plugin_id` bigint NOT NULL,
  `content` text,
  `message` text,
  `elapsed` bigint DEFAULT '0',
  `user_id` bigint NOT NULL,
  `count` bigint DEFAULT '0',
  PRIMARY KEY (`id`),
  FULLTEXT KEY `full_text_index_for_content` (`content`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_plugin`
--

LOCK TABLES `audit_plugin` WRITE;
/*!40000 ALTER TABLE `audit_plugin` DISABLE KEYS */;
/*!40000 ALTER TABLE `audit_plugin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `datacap_chat`
--

DROP TABLE IF EXISTS `datacap_chat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `datacap_chat` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `active` tinyint(1) DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `avatar` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `datacap_chat`
--

LOCK TABLES `datacap_chat` WRITE;
/*!40000 ALTER TABLE `datacap_chat` DISABLE KEYS */;
/*!40000 ALTER TABLE `datacap_chat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `datacap_chat_user_relation`
--

DROP TABLE IF EXISTS `datacap_chat_user_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `datacap_chat_user_relation` (
  `chat_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `datacap_chat_user_relation`
--

LOCK TABLES `datacap_chat_user_relation` WRITE;
/*!40000 ALTER TABLE `datacap_chat_user_relation` DISABLE KEYS */;
/*!40000 ALTER TABLE `datacap_chat_user_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `datacap_message`
--

DROP TABLE IF EXISTS `datacap_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `datacap_message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `active` tinyint(1) DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `content` text,
  `model` varchar(255) DEFAULT NULL,
  `type` varchar(100) DEFAULT NULL,
  `prompt_tokens` bigint DEFAULT '0',
  `completion_tokens` bigint DEFAULT '0',
  `total_tokens` bigint DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `datacap_message`
--

LOCK TABLES `datacap_message` WRITE;
/*!40000 ALTER TABLE `datacap_message` DISABLE KEYS */;
/*!40000 ALTER TABLE `datacap_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `datacap_message_chat_relation`
--

DROP TABLE IF EXISTS `datacap_message_chat_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `datacap_message_chat_relation` (
  `message_id` bigint DEFAULT NULL,
  `chat_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `datacap_message_chat_relation`
--

LOCK TABLES `datacap_message_chat_relation` WRITE;
/*!40000 ALTER TABLE `datacap_message_chat_relation` DISABLE KEYS */;
/*!40000 ALTER TABLE `datacap_message_chat_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `datacap_message_user_relation`
--

DROP TABLE IF EXISTS `datacap_message_user_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `datacap_message_user_relation` (
  `message_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `datacap_message_user_relation`
--

LOCK TABLES `datacap_message_user_relation` WRITE;
/*!40000 ALTER TABLE `datacap_message_user_relation` DISABLE KEYS */;
/*!40000 ALTER TABLE `datacap_message_user_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `datacap_scheduled`
--

DROP TABLE IF EXISTS `datacap_scheduled`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `datacap_scheduled` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` text NOT NULL,
  `expression` varchar(100) DEFAULT NULL,
  `active` tinyint(1) DEFAULT '1',
  `is_system` tinyint(1) DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `datacap_scheduled`
--

LOCK TABLES `datacap_scheduled` WRITE;
/*!40000 ALTER TABLE `datacap_scheduled` DISABLE KEYS */;
INSERT INTO `datacap_scheduled` VALUES (1,'Synchronize table structure','Synchronize the table structure of the data source library at 1 am every day','0 20 * * * ?',1,1,'2023-07-04 21:47:24','2023-08-08 11:58:25','SOURCE_SYNCHRONIZE'),(2,'Check source available','Check the availability of the data source every 1 hour','0 0 * * * ?',1,1,'2023-08-08 11:54:01','2023-08-08 15:36:31','SOURCE_CHECK');
/*!40000 ALTER TABLE `datacap_scheduled` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `datacap_user`
--

DROP TABLE IF EXISTS `datacap_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `datacap_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL COMMENT ' ',
  `password` varchar(255) DEFAULT NULL COMMENT ' ',
  `create_time` datetime(5) DEFAULT CURRENT_TIMESTAMP(5),
  `chat_configure` text,
  `is_system` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `datacap_user`
--

LOCK TABLES `datacap_user` WRITE;
/*!40000 ALTER TABLE `datacap_user` DISABLE KEYS */;
INSERT INTO `datacap_user` VALUES (1,'admin','$2a$10$ee2yg.Te14GpHppDUROAi.HzYR5Q.q2/5vrZvAr4TFY3J2iT663JG','2023-07-04 21:47:24.57480','',0),(2,'datacap','$2a$10$bZ4XBRlYUjKfkBovWT9TuuXlEF7lpRxVrXS8iqyCjCHUqy4RPTL8.','2023-07-04 21:47:24.57542','',0),(10000,'Ai',NULL,'2023-07-10 19:08:25.95841',NULL,0);
/*!40000 ALTER TABLE `datacap_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `functions`
--

DROP TABLE IF EXISTS `functions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `functions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT 'Function name',
  `content` varchar(255) DEFAULT NULL COMMENT 'Expression of function',
  `description` text COMMENT 'Function description',
  `plugin` varchar(255) DEFAULT NULL COMMENT 'Trial plug-in, multiple according to, split',
  `example` text COMMENT 'Function Usage Example',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `type` varchar(20) DEFAULT 'KEYWORDS',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Plug-in correlation function';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `functions`
--

LOCK TABLES `functions` WRITE;
/*!40000 ALTER TABLE `functions` DISABLE KEYS */;
/*!40000 ALTER TABLE `functions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menus`
--

DROP TABLE IF EXISTS `menus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menus` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `url` varchar(255) NOT NULL,
  `group_name` varchar(255) DEFAULT NULL,
  `sorted` int DEFAULT '0',
  `type` varchar(10) DEFAULT 'VIEW',
  `parent` bigint DEFAULT '0',
  `active` tinyint(1) DEFAULT '1',
  `i18n_key` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `redirect` bigint DEFAULT '0',
  `is_new` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menus`
--

LOCK TABLES `menus` WRITE;
/*!40000 ALTER TABLE `menus` DISABLE KEYS */;
INSERT INTO `menus` VALUES (1,'全局 - 首页','HOME','全局路由：所有用户都可以访问','/dashboard/index',NULL,1,'VIEW',0,1,'common.home','ios-navigate','2023-07-04 21:47:24','2023-07-04 21:47:24',0,0),(2,'全局 - 查询','QUERY','全局路由：所有用户都可以访问','/console/index',NULL,2,'VIEW',0,1,'common.query','md-browsers','2023-07-04 21:47:24','2023-07-04 21:47:24',0,0),(3,'全局 - 管理主菜单','MANAGEMENT','全局：所有用户都可以访问\n位置：顶部管理主菜单','/admin',NULL,3,'VIEW',0,1,'common.admin','ios-hammer','2023-07-04 21:47:24','2023-07-04 21:47:24',0,0),(4,'全局 - 管理 - 数据源','DATASOURCE','全局：所有用户都可以访问\n位置：顶部管理一级子菜单','/admin/source','default',1,'VIEW',3,1,'common.source','md-appstore','2023-07-04 21:47:24','2023-07-04 21:47:24',0,0),(5,'全局 - 管理 - 片段','SNIPPET','全局：所有用户都可以访问\n位置：顶部管理一级子菜单','/admin/snippet',NULL,2,'VIEW',3,1,'common.snippet','ios-barcode','2023-07-04 21:47:24','2023-07-04 21:47:24',0,0),(6,'全局 - 管理 - 查询历史','HISTORY','全局：所有用户都可以访问\n位置：顶部管理一级子菜单','/admin/history',NULL,3,'VIEW',3,1,'common.history','ios-book','2023-07-04 21:47:24','2023-07-04 21:47:24',0,0),(7,'全局 - 管理 - 流水线','PIPELINE','全局：所有用户都可以访问\n位置：顶部管理一级子菜单','/admin/pipeline',NULL,4,'VIEW',3,1,'common.pipeline','md-list-box','2023-07-04 21:47:24','2023-07-04 21:47:24',0,0),(8,'管理员 - 系统主菜单','SYSTEM','管理员：管理员权限用户可以访问\n位置：顶部管理一级子菜单','/system',NULL,4,'VIEW',0,1,'common.system','md-cog','2023-07-04 21:47:24','2023-07-04 21:47:24',0,0),(9,'管理员 - 系统 - 函数','FUNCTION','管理员：管理员权限用户可以访问\n位置：顶部管理一级子菜单','/system/function',NULL,1,'VIEW',8,1,'common.function','ios-basket','2023-07-04 21:47:24','2023-07-04 21:47:24',0,0),(10,'管理员 - 系统 - 定时任务','SCHEDULE','管理员：管理员权限用户可以访问\n位置：顶部管理一级子菜单','/system/schedule',NULL,2,'VIEW',8,1,'common.schedule','md-timer','2023-07-04 21:47:24','2023-07-04 21:47:24',0,0),(11,'管理员 - 系统 - 模版','TEMPLATE','管理员：管理员权限用户可以访问\n位置：顶部管理一级子菜单','/system/template',NULL,3,'VIEW',8,1,'common.template','md-browsers','2023-07-04 21:47:24','2023-07-04 21:47:24',0,0),(12,'管理员 - 系统 - 权限','ROLE','管理员：管理员权限用户可以访问\n位置：顶部管理一级子菜单','/system/role',NULL,4,'VIEW',8,1,'common.authority','md-flag','2023-07-04 21:47:24','2023-07-04 21:47:24',0,0),(13,'管理员 - 系统 - 菜单','MENU','管理员：管理员权限用户可以访问\n位置：顶部管理一级子菜单','/system/menu',NULL,5,'VIEW',8,1,'common.menu','md-menu','2023-07-04 21:47:24','2023-07-04 21:47:24',0,0),(14,'管理员 - 系统 - 用户','USERS','管理员：管理员权限用户可以访问\n位置：顶部管理一级子菜单','/system/user',NULL,6,'VIEW',8,1,'common.user','ios-man','2023-07-04 21:47:24','2023-07-04 21:47:24',0,0);
/*!40000 ALTER TABLE `menus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pipeline`
--

DROP TABLE IF EXISTS `pipeline`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pipeline` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `content` text NOT NULL,
  `state` varchar(100) DEFAULT NULL,
  `message` text,
  `work` text,
  `start_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `end_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `elapsed` bigint DEFAULT NULL,
  `user_id` int NOT NULL,
  `from_id` int NOT NULL,
  `from_configures` text,
  `to_id` int NOT NULL,
  `to_configures` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pipeline`
--

LOCK TABLES `pipeline` WRITE;
/*!40000 ALTER TABLE `pipeline` DISABLE KEYS */;
/*!40000 ALTER TABLE `pipeline` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT ' ',
  `description` varchar(255) DEFAULT NULL COMMENT ' ',
  `create_time` datetime(5) DEFAULT CURRENT_TIMESTAMP(5),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'Admin','Admin role','2023-07-04 21:47:24.48680'),(2,'User','User role','2023-07-04 21:47:24.48914');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_menu_relation`
--

DROP TABLE IF EXISTS `role_menu_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role_menu_relation` (
  `role_id` mediumtext,
  `menu_id` mediumtext
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_menu_relation`
--

LOCK TABLES `role_menu_relation` WRITE;
/*!40000 ALTER TABLE `role_menu_relation` DISABLE KEYS */;
INSERT INTO `role_menu_relation` VALUES ('2','7'),('2','1'),('2','5'),('2','2'),('2','3'),('2','4'),('2','6'),('1','12'),('1','7'),('1','1'),('1','10'),('1','8'),('1','5'),('1','4'),('1','9'),('1','13'),('1','2'),('1','3'),('1','6'),('1','11'),('1','14');
/*!40000 ALTER TABLE `role_menu_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `snippet`
--

DROP TABLE IF EXISTS `snippet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `snippet` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT ' ',
  `description` varchar(255) DEFAULT NULL COMMENT ' ',
  `code` text COMMENT ' ',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  FULLTEXT KEY `full_text_index_for_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `snippet`
--

LOCK TABLES `snippet` WRITE;
/*!40000 ALTER TABLE `snippet` DISABLE KEYS */;
/*!40000 ALTER TABLE `snippet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `source`
--

DROP TABLE IF EXISTS `source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `source` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `_catalog` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `_database` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `host` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `port` bigint NOT NULL,
  `protocol` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `_type` varchar(100) NOT NULL,
  `ssl` tinyint(1) DEFAULT '0',
  `_ssl` tinyint(1) DEFAULT '0',
  `publish` tinyint(1) DEFAULT '0',
  `public` tinyint(1) DEFAULT '0',
  `user_id` bigint DEFAULT NULL,
  `configure` text,
  `used_config` tinyint(1) DEFAULT '0',
  `version` varchar(255) DEFAULT NULL,
  `available` tinyint(1) DEFAULT '1',
  `message` text,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='The storage is used to query the data connection source';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `source`
--

LOCK TABLES `source` WRITE;
/*!40000 ALTER TABLE `source` DISABLE KEYS */;
/*!40000 ALTER TABLE `source` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `template_sql`
--

DROP TABLE IF EXISTS `template_sql`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `template_sql` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT 'Name of template',
  `content` text,
  `description` text,
  `plugin` varchar(50) DEFAULT NULL COMMENT 'Using plug-ins',
  `configure` text COMMENT 'The template must use the configuration information in the key->value format',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `system` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='The system preset SQL template table';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `template_sql`
--

LOCK TABLES `template_sql` WRITE;
/*!40000 ALTER TABLE `template_sql` DISABLE KEYS */;
INSERT INTO `template_sql` VALUES (1,'getAllDatabase','SHOW DATABASES','Gets a list of all databases','ClickHouse,MySQL,H2','[]','2022-12-08 10:38:39','2022-12-08 10:38:39',1),(2,'getAllTablesFromDatabase','SHOW TABLES FROM ${database:String}','Get the data table from the database','ClickHouse,MySQL,H2','[{\"column\":\"database\",\"type\":\"String\",\"expression\":\"${database:String}\"}]','2022-12-08 11:25:31','2022-12-08 11:25:31',1),(3,'getAllDatabaseAndTable','SELECT database as tableSchema, name as tableName\nFROM system.tables\nWHERE database NOT IN (\'system\')\nORDER BY tableSchema, tableName','Get all databases (including all tables)','ClickHouse','[]','2022-12-08 14:52:59','2022-12-08 14:52:59',1),(4,'getAllDatabaseAndTable','SELECT t.table_schema AS tableSchema, t.table_name AS tableName\nFROM INFORMATION_SCHEMA.TABLES t\nORDER BY t.table_schema, t.table_name','Get all databases (including all tables)','MySQL','[]','2022-12-08 15:04:45','2022-12-08 15:04:45',1),(5,'getAllDatabaseAndTables','show tables','show tables','ClickHouse','[]','2022-12-27 10:51:02','2022-12-27 10:51:02',0),(6,'getAllRunningProcess','SELECT\n  query_id AS id,\n  query AS content,\n  toUInt64(toUInt64(read_rows) + toUInt64(written_rows)) AS rows,\n  round(elapsed, 1) AS elapsed,\n  formatReadableSize(toUInt64(read_bytes) + toUInt64(written_bytes)) AS bytes,\n  formatReadableSize(memory_usage) AS memory,\n  formatReadableSize(read_bytes) AS bytesRead,\n  formatReadableSize(written_bytes) AS bytesWritten\nFROM\n  system.processes\nWHERE\n  round(elapsed, 1) > 0','Get a running list of all currently specified server nodes','ClickHouse','[]','2022-12-29 08:40:11','2022-12-29 08:40:11',0),(7,'getAllRunningProcess','SELECT\n  id AS id,\n  info AS content,\n  \' -\' AS rows,\n  time AS elapsed,\n  \' -\' AS bytes,\n  \' -\' AS memory,\n  \' -\' AS bytesRead,\n  \' -\' AS bytesWritten\nFROM\n  information_schema.PROCESSLIST','Get a running list of all currently specified server nodes','MySQL','[]','2022-12-29 11:33:04','2022-12-29 11:33:04',0),(8,'getAllColumnsFromDatabaseAndTable','DESC ${table:String}','Get the data column from the database and table','MySQL,ClickHouse','[{\"column\":\"table\",\"type\":\"String\",\"expression\":\"${table:String}\"}]','2023-01-10 03:59:23','2023-01-10 03:59:23',0),(9,'getDataFromDatabaseAndTableLimited','SELECT *\nFROM ${table:String}\nLIMIT ${size:Integer}\nOFFSET ${page:Integer}','Get all data from table by limited','MySQL,ClickHouse','[{\"column\":\"table\",\"type\":\"String\",\"expression\":\"${table:String}\"},{\"column\":\"size\",\"type\":\"Integer\",\"expression\":\"${size:Integer}\"},{\"column\":\"page\",\"type\":\"Integer\",\"expression\":\"${page:Integer}\"}]','2023-01-10 05:31:10','2023-01-10 05:31:10',0),(10,'getAllColumnsFromDatabaseAndTable','SHOW COLUMNS FROM ${table:String}','Get the data column from the database and table','H2','[{\"column\":\"table\",\"type\":\"String\",\"expression\":\"${table:String}\"}]','2023-07-04 13:47:24','2023-07-04 13:47:24',1),(11,'FindTableTypeByDatabase','SELECT\n  CASE\n    INFORMATION_SCHEMA.TABLES.TABLE_TYPE\n    WHEN \'VIEW\' THEN \'view\'\n    ELSE \'table\'\n  END AS table_type\nFROM\n  INFORMATION_SCHEMA.TABLES\nWHERE\n  INFORMATION_SCHEMA.TABLES.TABLE_SCHEMA = \'${database:String}\'\nGROUP BY\n  table_type','Finds all table types under the database according to the database','H2','[{\"column\":\"database\",\"type\":\"String\",\"expression\":\"${database:String}\"}]','2023-07-04 13:47:24','2023-07-04 13:47:24',1),(12,'FindTableByDatabaseAndType','SELECT\n  TABLE_CATALOG,\n  TABLE_NAME\nFROM\n  (\n    SELECT\n      TABLE_SCHEMA AS TABLE_CATALOG,\n      TABLE_NAME AS TABLE_NAME\n    FROM\n      INFORMATION_SCHEMA.TABLES\n    WHERE\n      INFORMATION_SCHEMA.TABLES.TABLE_SCHEMA = \'${database:String}\'\n      AND CASE\n        INFORMATION_SCHEMA.TABLES.TABLE_TYPE\n        WHEN \'VIEW\' THEN \'view\'\n        ELSE \'table\'\n      END = \'${type:String}\'\n    GROUP BY\n      TABLE_NAME,\n      TABLE_SCHEMA\n  )','Gets a collection of related data based on the specified database and data type','H2','[{\"column\":\"database\",\"type\":\"String\",\"expression\":\"${database:String}\"},{\"column\":\"type\",\"type\":\"String\",\"expression\":\"${type:String}\"}]','2023-07-04 13:47:24','2023-07-04 13:47:24',1),(13,'FindColumnTypeByDatabaseAndTable','SELECT\n  \'${database:String}\' AS TABLE_CATALOG,\n  COLUMN_TYPE\nFROM\n  (\n    SELECT\n      CASE\n        WHEN (\n          icl.IS_INDEX = \'Y\'\n          AND col.IS_IDENTITY = \'YES\'\n        ) THEN \'index,primaryKey\'\n        WHEN col.IS_IDENTITY = \'YES\' THEN \'primaryKey\'\n        WHEN icl.IS_INDEX = \'Y\' THEN \'index\'\n        ELSE \'column\'\n      END AS COLUMN_TYPE\n    FROM\n      INFORMATION_SCHEMA.COLUMNS col\n      LEFT JOIN (\n        SELECT\n          TABLE_SCHEMA,\n          TABLE_NAME,\n          COLUMN_NAME,\n          \'Y\' AS IS_KEY\n        FROM\n          INFORMATION_SCHEMA.KEY_COLUMN_USAGE\n      ) kcu ON kcu.TABLE_SCHEMA = col.TABLE_SCHEMA\n      AND kcu.TABLE_NAME = col.TABLE_NAME\n      AND kcu.COLUMN_NAME = col.COLUMN_NAME\n      LEFT JOIN (\n        SELECT\n          ic.TABLE_SCHEMA AS TABLE_SCHEMA,\n          ic.TABLE_NAME AS TABLE_NAME,\n          ic.COLUMN_NAME AS COLUMN_NAME,\n          \'Y\' AS IS_INDEX\n        FROM\n          INFORMATION_SCHEMA.INDEXES i,\n          INFORMATION_SCHEMA.INDEX_COLUMNS ic\n        WHERE\n          i.INDEX_NAME = ic.INDEX_NAME\n      ) icl ON icl.TABLE_SCHEMA = col.TABLE_SCHEMA\n      AND icl.TABLE_NAME = col.TABLE_NAME\n      AND icl.COLUMN_NAME = col.COLUMN_NAME\n    WHERE\n      col.TABLE_SCHEMA = \'${database:String}\'\n      AND col.TABLE_NAME = \'${table:String}\'\n    ORDER BY\n      col.COLUMN_NAME\n  )\nGROUP BY\n  COLUMN_TYPE','Gets the data column classification collection based on the provided database and data table','H2','[{\"column\":\"database\",\"type\":\"String\",\"expression\":\"${database:String}\"},{\"column\":\"table\",\"type\":\"String\",\"expression\":\"${table:String}\"}]','2023-07-04 13:47:24','2023-07-04 13:47:24',1),(14,'FindColumnByDatabaseAndTableAndType','SELECT\n  \'${database:String}\' AS TABLE_CATALOG,\n  \'${table:String}\' AS TABLE_NAME,\n  COLUMN_NAME,\n  COLUMN_TYPE,\n  DATA_TYPE\nFROM\n  (\n    SELECT\n      col.COLUMN_NAME AS COLUMN_NAME,\n      col.DATA_TYPE AS DATA_TYPE,\n      CASE\n        WHEN (\n          icl.IS_INDEX = \'Y\'\n          AND col.IS_IDENTITY = \'YES\'\n        ) THEN \'index,primaryKey\'\n        WHEN col.IS_IDENTITY = \'YES\' THEN \'primaryKey\'\n        WHEN icl.IS_INDEX = \'Y\' THEN \'index\'\n        ELSE \'column\'\n      END AS COLUMN_TYPE\n    FROM\n      INFORMATION_SCHEMA.COLUMNS col\n      LEFT JOIN (\n        SELECT\n          TABLE_SCHEMA,\n          TABLE_NAME,\n          COLUMN_NAME,\n          \'Y\' AS IS_KEY\n        FROM\n          INFORMATION_SCHEMA.KEY_COLUMN_USAGE\n      ) kcu ON kcu.TABLE_SCHEMA = col.TABLE_SCHEMA\n      AND kcu.TABLE_NAME = col.TABLE_NAME\n      AND kcu.COLUMN_NAME = col.COLUMN_NAME\n      LEFT JOIN (\n        SELECT\n          ic.TABLE_SCHEMA AS TABLE_SCHEMA,\n          ic.TABLE_NAME AS TABLE_NAME,\n          ic.COLUMN_NAME AS COLUMN_NAME,\n          \'Y\' AS IS_INDEX\n        FROM\n          INFORMATION_SCHEMA.INDEXES i,\n          INFORMATION_SCHEMA.INDEX_COLUMNS ic\n        WHERE\n          i.INDEX_NAME = ic.INDEX_NAME\n      ) icl ON icl.TABLE_SCHEMA = col.TABLE_SCHEMA\n      AND icl.TABLE_NAME = col.TABLE_NAME\n      AND icl.COLUMN_NAME = col.COLUMN_NAME\n    WHERE\n      col.TABLE_SCHEMA = \'${database:String}\'\n      AND col.TABLE_NAME = \'${table:String}\'\n    ORDER BY\n      col.COLUMN_NAME\n  )\nWHERE\n  COLUMN_TYPE LIKE \'%${type:String}%\'\nGROUP BY\n  COLUMN_NAME\nORDER BY\n  COLUMN_NAME','Gets a collection of related data based on the specified database, table, and data type','H2','[{\"column\":\"database\",\"type\":\"String\",\"expression\":\"${database:String}\"},{\"column\":\"table\",\"type\":\"String\",\"expression\":\"${table:String}\"},{\"column\":\"type\",\"type\":\"String\",\"expression\":\"${type:String}\"}]','2023-07-04 13:47:24','2023-07-04 13:47:24',1),(15,'FindTableTypeByDatabase','SELECT\n  CASE\n    INFORMATION_SCHEMA.TABLES.TABLE_TYPE\n    WHEN \'VIEW\' THEN \'view\'\n    ELSE \'table\'\n  END AS table_type\nFROM\n  INFORMATION_SCHEMA.TABLES\nWHERE\n  INFORMATION_SCHEMA.TABLES.TABLE_SCHEMA = \'${database:String}\'\nGROUP BY\n  table_type','Finds all table types under the database according to the database','MySQL','[{\"column\":\"database\",\"type\":\"String\",\"expression\":\"${database:String}\"}]','2023-07-04 13:47:24','2023-07-04 13:47:24',1),(16,'FindTableByDatabaseAndType','SELECT\n  TABLE_SCHEMA AS TABLE_CATALOG,\n  TABLE_NAME AS TABLE_NAME\nFROM\n  INFORMATION_SCHEMA.TABLES\nWHERE\n  INFORMATION_SCHEMA.TABLES.TABLE_SCHEMA = \'${database:String}\'\n  AND CASE\n    INFORMATION_SCHEMA.TABLES.TABLE_TYPE\n    WHEN \'VIEW\' THEN \'view\'\n    ELSE \'table\'\n  END = \'${type:String}\'\nGROUP BY\n  TABLE_NAME,\n  TABLE_SCHEMA','Gets a collection of related data based on the specified database and data type','MySQL','[{\"column\":\"database\",\"type\":\"String\",\"expression\":\"${database:String}\"},{\"column\":\"type\",\"type\":\"String\",\"expression\":\"${type:String}\"}]','2023-07-04 13:47:24','2023-07-04 13:47:24',1),(17,'FindColumnTypeByDatabaseAndTable','SELECT\n  \'${database:String}\' AS TABLE_CATALOG,\n  COLUMN_TYPE\nFROM\n  (\n    SELECT\n      CASE\n        WHEN (\n          icl.IS_INDEX = \'Y\'\n          AND col.COLUMN_KEY = \'PRI\'\n        ) THEN \'index,primaryKey\'\n        WHEN col.COLUMN_KEY = \'PRI\' THEN \'primaryKey\'\n        WHEN icl.IS_INDEX = \'Y\' THEN \'index\'\n        ELSE \'column\'\n      END AS COLUMN_TYPE\n    FROM\n      INFORMATION_SCHEMA.COLUMNS col\n      LEFT JOIN (\n        SELECT\n          TABLE_SCHEMA,\n          TABLE_NAME,\n          COLUMN_NAME,\n          \'Y\' AS IS_KEY\n        FROM\n          INFORMATION_SCHEMA.KEY_COLUMN_USAGE\n      ) kcu ON kcu.TABLE_SCHEMA = col.TABLE_SCHEMA\n      AND kcu.TABLE_NAME = col.TABLE_NAME\n      AND kcu.COLUMN_NAME = col.COLUMN_NAME\n      LEFT JOIN (\n        SELECT\n          TABLE_SCHEMA AS TABLE_SCHEMA,\n          TABLE_NAME AS TABLE_NAME,\n          COLUMN_NAME AS COLUMN_NAME,\n          \'Y\' AS IS_INDEX\n        FROM\n          INFORMATION_SCHEMA.`STATISTICS`\n        WHERE\n          TABLE_CATALOG = \'${database:String}\'\n          AND TABLE_NAME = \'${table:String}\'\n      ) icl ON icl.TABLE_SCHEMA = col.TABLE_SCHEMA\n      AND icl.TABLE_NAME = col.TABLE_NAME\n      AND icl.COLUMN_NAME = col.COLUMN_NAME\n    WHERE\n      col.TABLE_SCHEMA = \'${database:String}\'\n      AND col.TABLE_NAME = \'${table:String}\'\n    ORDER BY\n      col.COLUMN_NAME\n  ) AS tmp\nGROUP BY\n  COLUMN_TYPE','Gets the data column classification collection based on the provided database and data table','MySQL','[{\"column\":\"database\",\"type\":\"String\",\"expression\":\"${database:String}\"},{\"column\":\"table\",\"type\":\"String\",\"expression\":\"${table:String}\"}]','2023-07-04 13:47:24','2023-07-04 13:47:24',1),(18,'FindColumnByDatabaseAndTableAndType','SELECT\n  \'${database:String}\' AS TABLE_CATALOG,\n  \'${table:String}\' AS TABLE_NAME,\n  COLUMN_NAME,\n  COLUMN_TYPE,\n  DATA_TYPE\nFROM\n  (\n    SELECT\n      col.COLUMN_NAME AS COLUMN_NAME,\n      col.DATA_TYPE AS DATA_TYPE,\n      CASE\n        WHEN (\n          icl.IS_INDEX = \'Y\'\n          AND col.COLUMN_KEY = \'PRI\'\n        ) THEN \'index,primaryKey\'\n        WHEN col.COLUMN_KEY = \'PRI\' THEN \'primaryKey\'\n        WHEN icl.IS_INDEX = \'Y\' THEN \'index\'\n        ELSE \'column\'\n      END AS COLUMN_TYPE\n    FROM\n      INFORMATION_SCHEMA.COLUMNS col\n      LEFT JOIN (\n        SELECT\n          TABLE_SCHEMA,\n          TABLE_NAME,\n          COLUMN_NAME,\n          \'Y\' AS IS_KEY\n        FROM\n          INFORMATION_SCHEMA.KEY_COLUMN_USAGE\n      ) kcu ON kcu.TABLE_SCHEMA = col.TABLE_SCHEMA\n      AND kcu.TABLE_NAME = col.TABLE_NAME\n      AND kcu.COLUMN_NAME = col.COLUMN_NAME\n      LEFT JOIN (\n        SELECT\n          TABLE_SCHEMA AS TABLE_SCHEMA,\n          TABLE_NAME AS TABLE_NAME,\n          COLUMN_NAME AS COLUMN_NAME,\n          \'Y\' AS IS_INDEX\n        FROM\n          INFORMATION_SCHEMA.`STATISTICS`\n        WHERE\n          TABLE_CATALOG = \'${database:String}\'\n          AND TABLE_NAME = \'${table:String}\'\n      ) icl ON icl.TABLE_SCHEMA = col.TABLE_SCHEMA\n      AND icl.TABLE_NAME = col.TABLE_NAME\n      AND icl.COLUMN_NAME = col.COLUMN_NAME\n    WHERE\n      col.TABLE_SCHEMA = \'${database:String}\'\n      AND col.TABLE_NAME = \'${table:String}\'\n    ORDER BY\n      col.COLUMN_NAME\n  ) AS tmp\nWHERE\n  COLUMN_TYPE LIKE \'%${type:String}%\'\nGROUP BY\n  TABLE_CATALOG,\n  TABLE_NAME,\n  COLUMN_NAME,\n  COLUMN_TYPE,\n  DATA_TYPE\nORDER BY\n  COLUMN_NAME','Gets a collection of related data based on the specified database, table, and data type','MySQL','[{\"column\":\"database\",\"type\":\"String\",\"expression\":\"${database:String}\"},{\"column\":\"table\",\"type\":\"String\",\"expression\":\"${table:String}\"},{\"column\":\"type\",\"type\":\"String\",\"expression\":\"${type:String}\"}]','2023-07-04 13:47:24','2023-07-04 13:47:24',1),(19,'FindTableTypeByDatabase','SELECT\n  multiIf(\n    startsWith(engine, \'System\'),\n    \'system\',\n    endsWith(engine, \'View\'),\n    \'view\',\n    startsWith(engine, \'Kafka\'),\n    \'kafka\',\n    endsWith(engine, \'Log\'),\n    \'log\',\n    \'table\'\n  ) AS TABLE_TYPE\nFROM\n  system.tables\nWHERE\n  database = \'${database:String}\'\nGROUP BY\n  TABLE_TYPE','Finds all table types under the database according to the database','ClickHouse','[{\"column\":\"database\",\"type\":\"String\",\"expression\":\"${database:String}\"}]','2023-07-04 13:47:24','2023-07-04 13:47:24',1),(20,'FindTableByDatabaseAndType','SELECT\n  `database` AS TABLE_CATALOG,\n  name AS TABLE_NAME\nFROM\n  system.tables\nWHERE\n  `database` = \'${database:String}\'\n  AND multiIf(\n    startsWith(engine, \'System\'),\n    \'system\',\n    endsWith(engine, \'View\'),\n    \'view\',\n    startsWith(engine, \'Kafka\'),\n    \'kafka\',\n    endsWith(engine, \'Log\'),\n    \'log\',\n    \'table\'\n  ) = \'${type:String}\'\nGROUP BY\n  TABLE_NAME,\n  `database`','Gets a collection of related data based on the specified database and data type','ClickHouse','[{\"column\":\"database\",\"type\":\"String\",\"expression\":\"${database:String}\"},{\"column\":\"type\",\"type\":\"String\",\"expression\":\"${type:String}\"}]','2023-07-04 13:47:24','2023-07-04 13:47:24',1),(21,'FindColumnTypeByDatabaseAndTable','SELECT\n  \'${database:String}\' AS TABLE_CATALOG,\n  COLUMN_TYPE\nFROM\n  (\n    SELECT\n      CASE\n        WHEN (\n          is_in_primary_key = \'1\'\n          AND is_in_partition_key = \'1\'\n        ) THEN \'index,primaryKey\'\n        WHEN is_in_primary_key = \'1\' THEN \'primaryKey\'\n        WHEN is_in_primary_key = \'1\' THEN \'index\'\n        ELSE \'column\'\n      END AS COLUMN_TYPE\n    FROM\n      system.columns col\n    WHERE\n      `database` = \'${database:String}\'\n      AND `table` = \'${table:String}\'\n    ORDER BY\n      `name`\n  ) AS tmp\nGROUP BY\n  COLUMN_TYPE','Gets the data column classification collection based on the provided database and data table','ClickHouse','[{\"column\":\"database\",\"type\":\"String\",\"expression\":\"${database:String}\"},{\"column\":\"table\",\"type\":\"String\",\"expression\":\"${table:String}\"}]','2023-07-04 13:47:24','2023-07-04 13:47:24',1),(22,'FindColumnByDatabaseAndTableAndType','SELECT\n  \'${database:String}\' AS TABLE_CATALOG,\n  \'${table:String}\' AS TABLE_NAME,\n  COLUMN_NAME,\n  COLUMN_TYPE,\n  DATA_TYPE\nFROM\n  (\n    SELECT\n      `name` AS COLUMN_NAME,\n      `type` AS DATA_TYPE,\n      CASE\n        WHEN (\n          is_in_primary_key = \'1\'\n          AND is_in_partition_key = \'1\'\n        ) THEN \'index,primaryKey\'\n        WHEN is_in_primary_key = \'1\' THEN \'primaryKey\'\n        WHEN is_in_primary_key = \'1\' THEN \'index\'\n        ELSE \'column\'\n      END AS COLUMN_TYPE\n    FROM\n      system.columns col\n    WHERE\n      `database` = \'${database:String}\'\n      AND `table` = \'${table:String}\'\n    ORDER BY\n      `name`\n  ) AS tmp\nWHERE\n  COLUMN_TYPE LIKE \'%${type:String}%\'\nGROUP BY\n  TABLE_CATALOG,\n  TABLE_NAME,\n  COLUMN_NAME,\n  COLUMN_TYPE,\n  DATA_TYPE\nORDER BY\n  COLUMN_NAME','Gets a collection of related data based on the specified database, table, and data type','ClickHouse','[{\"column\":\"database\",\"type\":\"String\",\"expression\":\"${database:String}\"},{\"column\":\"table\",\"type\":\"String\",\"expression\":\"${table:String}\"},{\"column\":\"type\",\"type\":\"String\",\"expression\":\"${type:String}\"}]','2023-07-04 13:47:24','2023-07-04 13:47:24',1),(23,'getAllDatabase','SELECT keyspace_name AS name\nFROM system_schema.keyspaces','Gets a list of all databases','Cassandra','[]','2023-07-04 13:47:24','2023-07-04 13:47:24',1),(24,'getAllTablesFromDatabase','SELECT\n  table_name AS name\nFROM\n  system_schema.tables\nWHERE\n  keyspace_name = \'${database:String}\'\nGROUP BY\n  table_name','Get the data table from the database','Cassandra','[{\"column\":\"database\",\"type\":\"String\",\"expression\":\"${database:String}\"}]','2023-07-04 13:47:24','2023-07-04 13:47:24',1),(25,'getAllColumnsFromDatabaseAndTable','SELECT\n  column_name\nFROM\n  system_schema.columns\nWHERE\n  keyspace_name = \'${database:String}\'\n  and table_name = \'${table:String}\'','Get the data column from the database and table','Cassandra','[{\"column\":\"database\",\"type\":\"String\",\"expression\":\"${database:String}\"},{\"column\":\"table\",\"type\":\"String\",\"expression\":\"${table:String}\"}]','2023-07-04 13:47:24','2023-07-04 13:47:24',1);
/*!40000 ALTER TABLE `template_sql` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_log`
--

DROP TABLE IF EXISTS `user_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `device` varchar(255) DEFAULT NULL COMMENT 'Login device',
  `client` varchar(255) DEFAULT NULL COMMENT 'Login client',
  `ip` varchar(100) DEFAULT NULL COMMENT 'Login ip',
  `message` varchar(225) DEFAULT NULL COMMENT 'Error message',
  `state` varchar(20) DEFAULT NULL COMMENT 'Login state',
  `ua` varchar(255) DEFAULT NULL COMMENT 'Trial plug-in, multiple according to, split',
  `user_id` bigint NOT NULL,
  `create_time` datetime(5) DEFAULT CURRENT_TIMESTAMP(5),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='User login log';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_log`
--

LOCK TABLES `user_log` WRITE;
/*!40000 ALTER TABLE `user_log` DISABLE KEYS */;
INSERT INTO `user_log` VALUES (1,'Computer','Chrome 11','0:0:0:0:0:0:0:1',NULL,'SUCCESS','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36 Edg/115.0.1901.188',2,'2023-08-09 15:22:27.62800');
/*!40000 ALTER TABLE `user_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_roles` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
INSERT INTO `user_roles` VALUES (1,1),(2,2);
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-09 15:22:41
