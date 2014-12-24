CREATE DATABASE  IF NOT EXISTS `bee_bpm_dev` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `bee_bpm_dev`;
-- MySQL dump 10.13  Distrib 5.5.32, for debian-linux-gnu (i686)
--
-- Host: localhost    Database: incidencias_sp
-- ------------------------------------------------------
-- Server version	5.5.32-0ubuntu0.12.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `rel_channel_notification_event`
--

DROP TABLE IF EXISTS `rel_channel_notification_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rel_channel_notification_event` (
  `notification_event_id` int(11) NOT NULL,
  `channel_id` int(11) NOT NULL,
  `add_date` timestamp NOT NULL DEFAULT '1970-01-01 00:01:00',
  `add_user` int(11) DEFAULT NULL,
  `mod_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mod_user` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notification_event`
--

DROP TABLE IF EXISTS `notification_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notification_event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `system_object_id` int(11) NOT NULL,
  `object_method` varchar(255) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `label_user_readable` varchar(255) NOT NULL,
  `email_template_id` int(11) DEFAULT NULL,
  `add_date` datetime NOT NULL DEFAULT '1970-01-01 01:01:00',
  `add_user` int(11) DEFAULT NULL,
  `mod_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mod_user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rel_channel_r_u_n_e`
--

DROP TABLE IF EXISTS `rel_channel_r_u_n_e`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rel_channel_r_u_n_e` (
  `user_notification_event_id` int(11) NOT NULL,
  `channel_id` int(11) NOT NULL,
  `add_date` timestamp NOT NULL DEFAULT '1970-01-01 00:01:00',
  `add_user` int(11) DEFAULT NULL,
  `mod_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mod_user` int(11) DEFAULT NULL,
  PRIMARY KEY (`user_notification_event_id`,`channel_id`),
  KEY `fk_une_constraint` (`user_notification_event_id`),
  KEY `fk_ch_constraint` (`channel_id`),
  CONSTRAINT `fk_ch_constraint` FOREIGN KEY (`channel_id`) REFERENCES `channel` (`id`),
  CONSTRAINT `fk_une_constraint` FOREIGN KEY (`user_notification_event_id`) REFERENCES `rel_user_notification_event` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `channel`
--

DROP TABLE IF EXISTS `channel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `channel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(120) DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `icon` varchar(120) DEFAULT NULL,
  `add_date` datetime NOT NULL DEFAULT '1970-01-01 01:01:00',
  `add_user` int(11) DEFAULT NULL,
  `mod_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mod_user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `channel`
--

LOCK TABLES `channel` WRITE;
/*!40000 ALTER TABLE `channel` DISABLE KEYS */;
INSERT INTO `channel` VALUES (5,'email','email','/images/icons/channels/email.png','2012-12-19 09:55:04',1000,'2012-12-19 08:55:09',1000),(10,'mobile phone','cel','/images/icons/channels/cel.png','2012-12-19 09:54:09',1000,'2012-12-19 08:55:20',1000),(11,'chat','chat','/images/icons/channels/chat.png','2012-12-19 09:54:17',1000,'1970-01-01 09:19:14',1000),(12,'facebook','face','/images/icons/channels/face.png','2012-12-19 09:54:27',1000,'2012-12-27 12:41:41',1000),(13,'phone','phone','/images/icons/channels/phone.png','2012-12-19 09:54:43',1000,'1970-01-01 09:19:14',1000),(15,'twitter','twitter','/images/icons/channels/twitter.png','2012-12-19 09:55:04',1000,'2012-12-19 08:55:09',1000);
/*!40000 ALTER TABLE `channel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rel_user_notification_event`
--

DROP TABLE IF EXISTS `rel_user_notification_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rel_user_notification_event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `notification_event_id` int(11) DEFAULT NULL,
  `enabled` bit(1) NOT NULL DEFAULT b'0',
  `add_date` timestamp NOT NULL DEFAULT '1970-01-01 00:01:00',
  `add_user` int(11) DEFAULT NULL,
  `mod_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mod_user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_ur_ne_constraint` (`notification_event_id`),
  KEY `fk_ur_user_ne_constraint` (`user_id`),
  CONSTRAINT `fk_ur_ne_constraint` FOREIGN KEY (`notification_event_id`) REFERENCES `notification_event` (`id`),
  CONSTRAINT `fk_ur_user_ne_constraint` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

/*!40000 ALTER TABLE `rel_user_notification_event` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-02-18 11:19:31