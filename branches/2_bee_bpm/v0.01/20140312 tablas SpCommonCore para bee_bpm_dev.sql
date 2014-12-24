CREATE DATABASE  IF NOT EXISTS `bee_bpm_dev` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `bee_bpm_dev`;
-- MySQL dump 10.13  Distrib 5.5.32, for debian-linux-gnu (i686)
--
-- Host: localhost    Database: gespro_anii
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
-- Table structure for table `persona`
--

DROP TABLE IF EXISTS `persona`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `persona` (
  `id_persona` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Identificador único (PK) de una Persona',
  `id_tipo_tratamiento_persona` int(11) DEFAULT NULL COMMENT 'Indica como se debe tratar a una persona (Sr. Sra. Dr......)',
  `persona_nombre` varchar(50) DEFAULT NULL COMMENT 'Nombre de la Persona',
  `persona_apellido1` varchar(50) DEFAULT NULL COMMENT 'Apellido uno de la Persona',
  `persona_apellido2` varchar(50) DEFAULT NULL COMMENT 'Apellido 2 de la Persona',
  `persona_apellidos` varchar(105) DEFAULT '' COMMENT 'Apellidos concatenados autoamticamente por TR',
  `persona_foto` blob COMMENT 'Foto carnet de la persona',
  `persona_hobby` varchar(50) DEFAULT '',
  `persona_email` varchar(150) DEFAULT NULL COMMENT 'Una persona puede tener varios eMail en cuyo caso se deben separar por ",". Queda pendiente probar en outloook cual es el separador por defecto.',
  `persona_telefono_domicilio` varchar(100) DEFAULT NULL,
  `persona_telefono_despacho` varchar(50) DEFAULT NULL,
  `persona_telefono_movil` varchar(50) DEFAULT NULL,
  `persona_fax` varchar(100) DEFAULT NULL,
  `id_tipo_documento_persona_fisica` int(11) DEFAULT NULL COMMENT 'Identificador único (PK) del Tipo de Documento',
  `persona_documento_numero` varchar(50) DEFAULT NULL COMMENT 'Número de Documento de la Persona',
  `persona_documento_comentarios` varchar(100) DEFAULT NULL COMMENT 'Campo opcional para agregar algún comentario relacionado con el documento de la persona.',
  `id_pais_documento` varchar(2) DEFAULT NULL COMMENT 'Identificador del Pais que emite el Documento de la Persona',
  `persona_comentarios` varchar(320) DEFAULT NULL,
  `persona_sexo` bit(1) NOT NULL DEFAULT b'0',
  `persona_estado_civil` varchar(10) DEFAULT 'OTROS',
  `persona_fecha_nacimiento` date DEFAULT NULL,
  `persona_fecha_fallecimiento` date DEFAULT NULL,
  `fecha_alta` timestamp NOT NULL DEFAULT '1970-01-02 00:00:02' COMMENT 'Fecha de Alta de la persona en la BBDD. Dato autogenerado.',
  `usuario_alta` varchar(80) DEFAULT NULL,
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `usuario_modificacion` varchar(50) DEFAULT '',
  `persona_estado` int(11) DEFAULT '1' COMMENT '1=Activo   2=Inactivo',
  `id_segmento` int(10) unsigned DEFAULT NULL,
  `id_subsegmento` int(10) unsigned DEFAULT NULL,
  `id_direccion_domicilio` int(10) unsigned DEFAULT NULL,
  `PE_ID` varchar(32) DEFAULT NULL COMMENT 'temporal para carga LN luego se puede eliminar',
  `persona_origen` varchar(30) DEFAULT NULL COMMENT 'Para dejar anotado de donde salio esa persona ( si se ingresa por el sistema, si se importa, etc)',
  `id_situacion_laboral` int(3) DEFAULT NULL COMMENT 'Para indicar la sit laboral de la persona ( Activo, Jubilado, Estudiante, etc )',
  PRIMARY KEY (`id_persona`),
  KEY `FK_id_tipo_tratamiento_persona` (`id_tipo_tratamiento_persona`),
  KEY `Index_3` (`PE_ID`),
  KEY `Index_apellidos` (`persona_apellidos`),
  KEY `FK_id_segmento` (`id_segmento`),
  KEY `FK_id_subsegmento` (`id_subsegmento`),
  CONSTRAINT `FK_id_segmento` FOREIGN KEY (`id_segmento`) REFERENCES `segmento` (`id_segmento`) ON DELETE SET NULL ON UPDATE NO ACTION,
  CONSTRAINT `FK_id_subsegmento` FOREIGN KEY (`id_subsegmento`) REFERENCES `subsegmento` (`id_subsegmento`) ON DELETE SET NULL,
  CONSTRAINT `FK_id_tipo_tratamiento` FOREIGN KEY (`id_tipo_tratamiento_persona`) REFERENCES `tipo_tratamiento_persona` (`id_tipo_tratamiento_persona`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='Maestro de Personas';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `persona`
--

--
-- Table structure for table `login_control`
--

DROP TABLE IF EXISTS `login_control`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `login_control` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `date` timestamp NOT NULL DEFAULT '1970-01-01 00:01:00',
  `ip_address` varchar(45) DEFAULT NULL,
  `origen` varchar(45) DEFAULT NULL,
  `correct_login` bit(1) DEFAULT NULL,
  `login_name` varchar(45) DEFAULT NULL,
  `browser_locale` varchar(10) DEFAULT NULL,
  `user_time_zone` varchar(6) DEFAULT NULL,
  `user_agent` varchar(512) DEFAULT NULL,
  `add_date` datetime NOT NULL DEFAULT '1970-01-01 01:01:00',
  `add_user` int(11) DEFAULT NULL,
  `mod_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mod_user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `email_template`
--

DROP TABLE IF EXISTS `email_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `email_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `email_theme_id` int(11) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `txt_template` text COMMENT 'html email template',
  `html_template` text COMMENT 'txt email template',
  `mobile_template` text COMMENT 'email template for mobile devices',
  `template_file_name` varchar(100) DEFAULT NULL,
  `add_date` datetime NOT NULL DEFAULT '1970-01-01 01:01:00',
  `add_user` int(11) DEFAULT NULL,
  `mod_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mod_user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_email_template_theme` (`email_theme_id`),
  CONSTRAINT `fk_email_template_group` FOREIGN KEY (`email_theme_id`) REFERENCES `email_template_theme` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='email templates to communitate with users and admins';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `email_template_theme`
--

DROP TABLE IF EXISTS `email_template_theme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `email_template_theme` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT 'group name',
  `add_date` datetime NOT NULL DEFAULT '1970-01-01 01:01:00',
  `add_user` int(11) DEFAULT NULL,
  `mod_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mod_user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='email template grouping';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  `active` bit(1) NOT NULL DEFAULT b'0',
  `add_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `add_user` int(11) DEFAULT NULL,
  `mod_date` datetime NOT NULL DEFAULT '1970-01-01 01:01:00',
  `mod_user` int(11) DEFAULT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `cur_role` (`role_id`),
  KEY `cur_user` (`user_id`),
  CONSTRAINT `fk_ur_role_constraint` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `fk_ur_user_constraint` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_settings`
--

DROP TABLE IF EXISTS `user_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_settings` (
  `id` int(11) NOT NULL,
  `notify_new_email_reception` bit(1) NOT NULL DEFAULT b'0',
  `notify_new_ticket_assignation` bit(1) NOT NULL DEFAULT b'0',
  `notify_new_assignment_assignation` bit(1) NOT NULL DEFAULT b'0',
  `forward_received_email` bit(1) NOT NULL DEFAULT b'0',
  `forward_to_personal_email_account` bit(1) NOT NULL DEFAULT b'0',
  `forward_account` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_settings`
--

LOCK TABLES `user_settings` WRITE;
/*!40000 ALTER TABLE `user_settings` DISABLE KEYS */;
INSERT INTO `user_settings` VALUES (666,'\0','\0','\0','\0','',''),(999,'\0','\0','\0','\0','\0',NULL),(1000,'','\0','\0','\0','\0',''),(1001,'\0','\0','\0','\0','\0',NULL),(1018,'\0','\0','\0','\0','\0',''),(1019,'\0','\0','\0','\0','\0',''),(1020,'\0','\0','\0','\0','\0',''),(1021,'\0','\0','\0','\0','\0','');
/*!40000 ALTER TABLE `user_settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` int(3) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `add_date` datetime NOT NULL DEFAULT '1970-01-01 01:01:00',
  `add_user` int(11) DEFAULT NULL,
  `mod_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mod_user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'admin','Administrador','2012-05-30 11:17:46',1000,'2013-06-07 05:04:48',1000),(3,'user','Normal user','2012-05-30 11:18:05',1000,'1970-01-01 09:19:14',1000);
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `time_unit`
--

DROP TABLE IF EXISTS `time_unit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `time_unit` (
  `id` int(2) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `conversion` int(11) NOT NULL COMMENT 'the value to convert into seconds',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `time_unit`
--

LOCK TABLES `time_unit` WRITE;
/*!40000 ALTER TABLE `time_unit` DISABLE KEYS */;
INSERT INTO `time_unit` VALUES (1,'second',1),(2,'minute',60),(3,'hour',3600),(4,'day',86400),(5,'week',604800),(6,'month',2592000);
/*!40000 ALTER TABLE `time_unit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_calendar`
--

DROP TABLE IF EXISTS `business_calendar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_calendar` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `from_date` datetime DEFAULT NULL,
  `to_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_calendar`
--

LOCK TABLES `business_calendar` WRITE;
/*!40000 ALTER TABLE `business_calendar` DISABLE KEYS */;
INSERT INTO `business_calendar` VALUES (1,'Softpoint','2011-07-01 00:00:00','2013-12-31 00:00:00'),(2,'Softpoint2',NULL,NULL);
/*!40000 ALTER TABLE `business_calendar` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_persona` int(11) DEFAULT NULL,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `login_id` varchar(45) NOT NULL,
  `gender` int(1) DEFAULT NULL,
  `time_zone_name` varchar(50) NOT NULL DEFAULT 'Europe/Madrid',
  `password` varchar(45) NOT NULL,
  `admon` varchar(1) NOT NULL DEFAULT 'N',
  `su_lectura` varchar(1) NOT NULL DEFAULT 'N',
  `id_depto` int(11) DEFAULT NULL,
  `last_login_date` datetime DEFAULT NULL COMMENT 'Fecha de ultimo login',
  `enabled` varchar(1) DEFAULT NULL,
  `department_chief` varchar(1) DEFAULT NULL COMMENT 'Para determinar los permisos sobre un departamento en particular',
  `user_photo` blob COMMENT 'De momento en la tabla pero no mapeado',
  `calendar_id` int(11) DEFAULT NULL,
  `country_code` varchar(5) DEFAULT NULL,
  `region_code` varchar(5) DEFAULT NULL,
  `startup_page` varchar(150) DEFAULT NULL,
  `add_date` timestamp NOT NULL DEFAULT '1970-01-01 00:01:01',
  `add_user` int(11) DEFAULT NULL,
  `mod_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mod_user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_id_UNIQUE` (`login_id`),
  KEY `FK_ID_DPTO` (`id_depto`),
  KEY `idx_login_id` (`login_id`),
  KEY `persona` (`id_persona`),
  CONSTRAINT `depto` FOREIGN KEY (`id_depto`) REFERENCES `departamento` (`id_departamento`) ON UPDATE NO ACTION,
  CONSTRAINT `persona` FOREIGN KEY (`id_persona`) REFERENCES `persona` (`id_persona`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2129 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (666,NULL,'daemon','daemon','davidmuleirolopez@yahoo.es','daemon',1,'Europe/Brussels','Yswti0vy2HKBINBSFjp33w==','N','N',NULL,NULL,'S',NULL,NULL,NULL,'','','','1970-01-01 01:01:01',1000,'2012-11-29 16:30:37',1000),(999,NULL,'system','administrator','demo@softpoint.org','sysadmin',0,'Europe/Madrid','Yswti0vy2HKBINBSFjp33w==','S','S',NULL,NULL,'S',NULL,NULL,NULL,'','','','1970-01-01 01:01:01',1000,'2013-04-15 13:39:21',1000),(1000,NULL,'Demo','demo','demo@softpoint.org','demo',0,'Europe/Madrid','Yswti0vy2HKBINBSFjp33w==','S','S',NULL,'2010-09-08 00:00:00','S','N',NULL,1,'es','',NULL,'1970-01-01 01:01:01',NULL,'2014-02-21 11:43:43',1000),(1001,NULL,'Nestor','Demo','ndubikin@softpoint.org','demones',0,'Europe/Madrid','Yswti0vy2HKBINBSFjp33w==','S','S',NULL,NULL,'S','N',NULL,1,'es',NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-20 17:13:59',1001),(1005,NULL,'Milton','Bianchi','mbianchi@softpoint.org','mbianchi',0,'America/Montevideo','Yswti0vy2HKBINBSFjp33w==','S','N',NULL,NULL,'S',NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 11:42:40',NULL),(1018,NULL,'usuario OPERACIONES','demo','demo@softpoint.org','usuarioOP',1,'America/Montevideo','Yswti0vy2HKBINBSFjp33w==','N','N',3,NULL,'S','S',NULL,NULL,'','','','1970-01-01 01:01:01',NULL,'2012-10-18 14:28:00',1000),(1021,NULL,'david','muleiro','demo@softpoint.org','davidmuleiro',1,'Europe/Madrid','Yswti0vy2HKBINBSFjp33w==','S','S',NULL,NULL,'N',NULL,NULL,1,'','','/paginasInicio/paginaInicioDirectorProyecto.xhtml','1970-01-01 01:01:01',NULL,'2012-10-18 14:28:27',1000),(1028,NULL,'usuario FINANZAS','demo','demo@softpoint.org','usuarioFI',1,'America/Montevideo','Yswti0vy2HKBINBSFjp33w==','S','N',4,NULL,'S','S',NULL,NULL,'','','','1970-01-01 01:01:01',NULL,'2012-10-18 14:28:08',1000),(1029,NULL,'Director OP','demo','directorOP@softpoint.org','directorOP',1,'America/Montevideo','Yswti0vy2HKBINBSFjp33w==','N','N',3,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2014-01-07 17:25:49',NULL),(1031,NULL,'Director FINANZAS','demo','directorFI@softpoint.org','directorFI',1,'America/Montevideo','Yswti0vy2HKBINBSFjp33w==','N','N',4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2014-01-07 17:25:50',NULL),(2001,NULL,'Amalia','Quirici','aquirici@anii.org.uy','aquirici',0,'America/Montevideo','Yswti0vy2HKBINBSFjp33w==','N','N',NULL,NULL,'S',NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 01:01:01',NULL,'2013-11-16 13:12:49',NULL),(2004,NULL,'Federico','Secondo','fsecondo@anii.org.uy','fsecondo',0,'America/Montevideo','Yswti0vy2HKBINBSFjp33w==','N','N',NULL,NULL,'S',NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 01:01:01',NULL,'2013-11-16 13:12:49',NULL),(2005,NULL,'Gaston','Nuñez','gnuñez@anii.org.uy','gnunez',0,'America/Montevideo','Yswti0vy2HKBINBSFjp33w==','N','N',NULL,NULL,'S',NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 01:01:01',NULL,'2013-11-16 13:12:49',NULL),(2008,NULL,'Luciana','Balseiro','lbalseiro@anii.org.uy','lbalseiro',0,'America/Montevideo','Yswti0vy2HKBINBSFjp33w==','N','N',NULL,NULL,'S',NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 01:01:01',NULL,'2013-11-16 13:12:49',NULL),(2009,NULL,'Laura','Barreiro','lbarreiro@anii.org.uy','lbarreiro',1,'America/Montevideo','Yswti0vy2HKBINBSFjp33w==','N','N',NULL,NULL,'S',NULL,NULL,NULL,'','','','1970-01-01 01:01:01',NULL,'2013-11-16 15:02:11',1000),(2013,NULL,'Laura','Marquez','lmarquez@anii.org.uy','lmarquez',0,'America/Montevideo','G90HfY32X7cY9pZ1Z3AQCA==','N','N',NULL,NULL,'S',NULL,NULL,NULL,'','','','1970-01-01 01:01:01',NULL,'2014-01-30 11:47:40',1000),(2016,NULL,'Marcelo','Batto','mbatto@anii.org.uy','mbatto',0,'America/Montevideo','Yswti0vy2HKBINBSFjp33w==','N','N',NULL,NULL,'S',NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 01:01:01',NULL,'2013-11-16 13:12:49',NULL),(2017,NULL,'Martin','Salgueiro','msalgueiro@anii.org.uy','msalgueiro',0,'America/Montevideo','Yswti0vy2HKBINBSFjp33w==','N','N',NULL,NULL,'S',NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 01:01:01',NULL,'2013-11-16 13:12:49',NULL),(2018,NULL,'Nancy','Ghan','nghan@anii.org.uy','nghan',0,'America/Montevideo','Yswti0vy2HKBINBSFjp33w==','N','N',NULL,NULL,'S',NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 01:01:01',NULL,'2013-11-16 13:12:49',NULL),(2019,NULL,'Valentina','Gomez','vgomez@anii.org.uy','vgomez',0,'America/Montevideo','Yswti0vy2HKBINBSFjp33w==','N','N',NULL,NULL,'S',NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 01:01:01',NULL,'2013-11-16 13:12:49',NULL),(2021,NULL,'Ximena','Camaño','demo@softpoint.org','xcamano',0,'America/Montevideo','Yswti0vy2HKBINBSFjp33w==','N','N',NULL,NULL,'S',NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 01:01:01',NULL,'2013-11-16 13:12:49',NULL),(2022,NULL,'Agustin','Miranda','amiranda@anii.org.uy','amiranda',0,'America/Montevideo','Yswti0vy2HKBINBSFjp33w==','N','N',NULL,NULL,'S',NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 01:01:01',NULL,'2013-11-16 13:12:49',NULL),(2024,NULL,'Gaston','Gonzalez','ggonzalez@anii.org.uy','ggonzalez',0,'America/Montevideo','Yswti0vy2HKBINBSFjp33w==','N','N',NULL,NULL,'S',NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 01:01:01',NULL,'2013-11-16 13:12:49',NULL),(2027,NULL,'Cinthia','Alvarez','calvarez@anii.org.uy','calvarez',0,'America/Montevideo','Yswti0vy2HKBINBSFjp33w==','N','N',NULL,NULL,'S',NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 01:01:01',NULL,'2013-11-16 13:12:49',NULL),(2033,NULL,'Georgina','Lazarini','glazarini@anii.org.uy','glazarini',0,'America/Montevideo','Yswti0vy2HKBINBSFjp33w==','N','N',NULL,NULL,'S',NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 01:01:01',NULL,'2013-11-16 13:12:49',NULL),(2036,NULL,'Micaela','Araújo','maraújo@anii.org.uy','maraujo',0,'America/Montevideo','Yswti0vy2HKBINBSFjp33w==','N','N',NULL,NULL,'S',NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 01:01:01',NULL,'2013-11-16 13:12:49',NULL),(2037,NULL,'Laura','Di Giovanni','ldigiovanni@anii.org.uy','ldigiovanni',0,'America/Montevideo','Yswti0vy2HKBINBSFjp33w==','N','N',NULL,NULL,'S',NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 01:01:01',NULL,'2013-11-16 13:12:49',NULL),(2038,NULL,'Sara','Goldberg','demo@softpoint.org','sgoldberg',1,'America/Montevideo','Yswti0vy2HKBINBSFjp33w==','N','N',NULL,NULL,'S',NULL,NULL,NULL,'UY','','','1970-01-01 01:01:01',NULL,'2013-11-16 14:58:02',1000),(2039,NULL,'Fabio','Bonanno','fbonanno@anii.org.uy','fbonanno',0,'America/Montevideo','Yswti0vy2HKBINBSFjp33w==','N','N',NULL,NULL,'S',NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 01:01:01',NULL,'2013-11-16 13:12:49',NULL),(2043,NULL,'Martin','Peralta','mperalta@anii.org.uy','mperalta',0,'America/Montevideo','Yswti0vy2HKBINBSFjp33w==','N','N',NULL,NULL,'S',NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 01:01:01',NULL,'2013-11-16 13:12:49',NULL),(2045,NULL,'Maria Ines','Quintana','mquintana@anii.org.uy','mquintana',0,'America/Montevideo','Yswti0vy2HKBINBSFjp33w==','N','N',NULL,NULL,'S',NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 01:01:01',NULL,'2013-11-16 13:12:49',NULL),(2048,NULL,'Miguel','Helou','mhelou@anii.org.uy','mhelou',0,'America/Montevideo','Yswti0vy2HKBINBSFjp33w==','N','N',NULL,NULL,'S',NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 01:01:01',NULL,'2013-11-16 13:12:49',NULL),(2049,NULL,'Sofia','Lopez','slopez@anii.org.uy','slopez',0,'America/Montevideo','Yswti0vy2HKBINBSFjp33w==','N','N',NULL,NULL,'S',NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 01:01:01',NULL,'2013-11-16 13:12:49',NULL),(2081,NULL,'Geraldine','Gaillot-Novak','ggaillot@anii.org.uy','ggaillot',NULL,'America/Montevideo','','N','N',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2082,NULL,'Dolores','Traibel','dtraibel@anii.org.uy','dtraibel',NULL,'America/Montevideo','','N','N',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2083,NULL,'Paula','Valente','pvalente@anii.org.uy','pvalente',NULL,'America/Montevideo','','N','N',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2084,NULL,'Rafael','Saa','rsaa@anii.org.uy','rsaa',NULL,'America/Montevideo','','N','N',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2085,NULL,'Tomás','Parodi','tparodi@anii.org.uy','tparodi',NULL,'America/Montevideo','','N','N',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2086,NULL,'Cecilia','Ragni','cragni@anii.org.uy','cragni',NULL,'America/Montevideo','','N','N',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2087,NULL,'Jesus','Aumente','jaumente@anii.org.uy','jaumente',NULL,'America/Montevideo','','N','N',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2088,NULL,'Diego','Ghirardo','dghirardo@anii.org.uy','dghirardo',NULL,'America/Montevideo','','N','N',4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2089,NULL,'Silvina','Costa','scosta@anii.org.uy','scosta',NULL,'America/Montevideo','','N','N',4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2090,NULL,'Gonzalo','Taborda','gtaborda@anii.org.uy','gtaborda',NULL,'America/Montevideo','','N','N',4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2091,NULL,'Silvina','Regueiro','sregueiro@anii.org.uy','sregueiro',NULL,'America/Montevideo','','N','N',4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2092,NULL,'Gonzalo','Pose','gpose@anii.org.uy','gpose',NULL,'America/Montevideo','','N','N',4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2093,NULL,'Leticia','Ruiz','lruiz@anii.org.uy','lruiz',NULL,'America/Montevideo','','N','N',4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2094,NULL,'Maria Magdalena','Giuria','mgiuria@anii.org.uy','mgiuria',NULL,'America/Montevideo','','N','N',4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2095,NULL,'Mariana','Rodriguez','mrodriguez@anii.org.uy','mrodriguez',NULL,'America/Montevideo','','N','N',4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2096,NULL,'Patricia','Fernandez','pfernandez@anii.org.uy','pfernandez',NULL,'America/Montevideo','','N','N',4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2097,NULL,'Leticia','Correa','lcorrea@anii.org.uy','lcorrea',NULL,'America/Montevideo','','N','N',4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2098,NULL,'Leticia','Furtado','lfurtado@anii.org.uy','lfurtado',NULL,'America/Montevideo','','N','N',3,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2099,NULL,'Camilo','Diaz','cdiaz@anii.org.uy','cdiaz',NULL,'America/Montevideo','','N','N',4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2100,NULL,'Leandro ','Souza','lsouza@anii.org.uy','lsouza',NULL,'America/Montevideo','','N','N',4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2101,NULL,'Irene','Suffia','isuffia@anii.org.uy','isuffia',NULL,'America/Montevideo','','N','N',4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2102,NULL,'Aldo ','Luissi','aluissi@anii.org.uy','aluissi',NULL,'America/Montevideo','','N','N',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2103,NULL,'Florencia','Montado','fmontado@anii.org.uy','fmontado',NULL,'America/Montevideo','','N','N',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2104,NULL,'Camilo','Rousserie','crousserie@anii.org.uy','crousserie',NULL,'America/Montevideo','','N','N',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2105,NULL,'Melisa','Bustamante','mbustamante@anii.org.uy','mbustamante',NULL,'America/Montevideo','','N','N',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2106,NULL,'Daniel','Bukstein','dbukstein@anii.org.uy','dbukstein',NULL,'America/Montevideo','','N','N',12,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2107,NULL,'Elisa','Hernandez','ehernandez@anii.org.uy','ehernandez',NULL,'America/Montevideo','','N','N',12,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2108,NULL,'Ximena','Usher','xusher@anii.org.uy','xusher',NULL,'America/Montevideo','','N','N',12,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2109,NULL,'Ruth','Bernheim','rbernheim@anii.org.uy','rbernheim',NULL,'America/Montevideo','','N','N',12,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2110,NULL,'Maria Eugenia','Sotelo','msotelo@anii.org.uy','msotelo',NULL,'America/Montevideo','','N','N',12,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2111,NULL,'Emiliano','Cardona','ecardona@anii.org.uy','ecardona',NULL,'America/Montevideo','','N','N',12,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2112,NULL,'Nicolás','Costa','ncosta@anii.org.uy','ncosta',NULL,'America/Montevideo','','N','N',12,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2113,NULL,'Lucia','Soca','lsoca@anii.org.uy','lsoca',NULL,'America/Montevideo','','N','N',12,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2114,NULL,'Beatriz','Prandi','bprandi@anii.org.uy','bprandi',NULL,'America/Montevideo','','N','N',1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2115,NULL,'Omar','Berreneche','oberreneche@anii.org.uy','oberreneche',NULL,'America/Montevideo','','N','N',1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2116,NULL,'Nicolas','Caitan','ncaitan@anii.org.uy','ncaitan',NULL,'America/Montevideo','','N','N',13,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2117,NULL,'Rafael','Bustelo','rbustelo@anii.org.uy','rbustelo',NULL,'America/Montevideo','','N','N',13,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2118,NULL,'Juan Bautista','Possamay','jpossamay@anii.org.uy','jpossamay',NULL,'America/Montevideo','','N','N',13,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2119,NULL,'Marcela ','Mercapidez','mmercapidez@anii.org.uy','mmercapidez',NULL,'America/Montevideo','','N','N',13,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2120,NULL,'Diego','Carrion','dcarrion@anii.org.uy','dcarrion',NULL,'America/Montevideo','','N','N',13,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2121,NULL,'Matias','Burchio','mburchio@anii.org.uy','mburchio',NULL,'America/Montevideo','','N','N',13,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2122,NULL,'Laura ','Fernandez','lfernandez@anii.org.uy','lfernandez',NULL,'America/Montevideo','','N','N',3,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2123,NULL,'Elena','Perez','eperez@anii.org.uy','eperez',NULL,'America/Montevideo','','N','N',3,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2124,NULL,'Valentin','Balderrin','vbalderrin@anii.org.uy','vbalderrin',NULL,'America/Montevideo','','N','N',3,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2125,NULL,'Veronica','Suarez','vsuarez@anii.org.uy','vsuarez',NULL,'America/Montevideo','','N','N',3,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2126,NULL,'Clauida','Perrone','cperrone@anii.org.uy','cperrone',NULL,'America/Montevideo','','N','N',11,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2127,NULL,'Lila ','Gutierrez','lgutierrez@anii.org.uy','lgutierrez',NULL,'America/Montevideo','','N','N',11,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL),(2128,NULL,'Maria','Fernandez','mfernandez@anii.org.uy','mfernandez',NULL,'America/Montevideo','','N','N',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1970-01-01 00:01:01',NULL,'2013-12-19 19:54:36',NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-03-12 15:26:06