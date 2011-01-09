-- MySQL dump 10.11
--
-- Host: localhost    Database: wisematches
-- ------------------------------------------------------
-- Server version	5.0.75-0ubuntu10.2

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
-- Table structure for table `lock_account`
--

DROP TABLE IF EXISTS `lock_account`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lock_account` (
  `playerId` bigint(20) unsigned NOT NULL,
  `publicReason` varchar(255) NOT NULL,
  `privateReason` varchar(255) NOT NULL,
  `lockDate` timestamp NOT NULL default '0000-00-00 00:00:00',
  `unlockDate` timestamp NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (`playerId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `lock_username`
--

DROP TABLE IF EXISTS `lock_username`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lock_username` (
  `username` varchar(100) NOT NULL,
  `reason` varchar(255) NOT NULL,
  PRIMARY KEY  (`username`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rp_problems`
--

DROP TABLE IF EXISTS `rp_problems`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rp_problems` (
  `id` bigint(20) NOT NULL auto_increment,
  `username` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `account` varchar(100) NOT NULL,
  `os` tinyint(4) default NULL,
  `browser` tinyint(4) default NULL,
  `subject` varchar(255) NOT NULL,
  `message` longtext NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=206 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `scribble_board`
--

DROP TABLE IF EXISTS `scribble_board`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `scribble_board` (
  `boardId` bigint(20) NOT NULL auto_increment,
  `title` varchar(255) NOT NULL,
  `createDate` bigint(20) default NULL,
  `daysPerMove` tinyint(4) default NULL,
  `maxPlayers` tinyint(4) NOT NULL,
  `minRating` int(11) default NULL,
  `maxRating` int(11) default NULL,
  `language` char(2) NOT NULL,
  `passesCount` tinyint(4) NOT NULL,
  `lastMoveTime` bigint(20) NOT NULL,
  `gameState` tinyint(4) NOT NULL,
  `boardTiles` tinyblob NOT NULL,
  `handTiles` tinyblob NOT NULL,
  `moves` blob NOT NULL,
  `currentPlayerIndex` tinyint(4) NOT NULL,
  `redifinitions` tinyblob NOT NULL,
  `startedDate` bigint(20) default NULL,
  `finishedDate` bigint(20) default NULL,
  `rated` tinyint(3) default '1',
  PRIMARY KEY  (`boardId`),
  KEY `state` (`gameState`)
) ENGINE=MyISAM AUTO_INCREMENT=459 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `scribble_memory`
--

DROP TABLE IF EXISTS `scribble_memory`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `scribble_memory` (
  `boardId` bigint(20) NOT NULL COMMENT 'The id of board',
  `playerId` bigint(20) NOT NULL COMMENT
          'The id of player who made a word',
  `wordNumber` int(11) NOT NULL COMMENT
          'The number of word on the board',
  `row` tinyint(4) NOT NULL default '0' COMMENT 'Row position of the word',
  `col` tinyint(4) NOT NULL default '0' COMMENT 'Column position of the word',
  `direction` tinyint(1) NOT NULL default '0' COMMENT
          'Direction of word (vertical and horizontal). Boolean value.',
  `word` varchar(104) default NULL COMMENT 'The made word. This field contains maximum 15 tiles in following format: \nTileNumberCharCost. Where TileNumber - 1 or two chars,\nChar - the letter,\nCost - the cost (one char).',
  PRIMARY KEY  (`boardId`,`playerId`,`wordNumber`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `scribble_player`
--

DROP TABLE IF EXISTS `scribble_player`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `scribble_player` (
  `playerId` bigint(20) NOT NULL,
  `playerIndex` tinyint(4) NOT NULL,
  `points` smallint(6) NOT NULL,
  `boardId` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL auto_increment,
  `ratingDelta` smallint(6) NOT NULL,
  `previousRating` smallint(6) NOT NULL,
  PRIMARY KEY  USING BTREE (`id`),
  KEY `PlayerIndex` (`playerId`),
  KEY `BoardIndex` (`boardId`)
) ENGINE=MyISAM AUTO_INCREMENT=1185 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `server_properties`
--

DROP TABLE IF EXISTS `server_properties`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `server_properties` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(100) NOT NULL,
  `value` blob,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `stats_info`
--

DROP TABLE IF EXISTS `stats_info`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `stats_info` (
  `playerId` bigint(20) NOT NULL,
  `updateTime` bigint(20) NOT NULL,
  `lastCleanupTime` bigint(20) NOT NULL,
  `activeGames` int(11) NOT NULL,
  `wonGames` int(11) NOT NULL,
  `lostGames` int(11) NOT NULL,
  `drawGames` int(11) NOT NULL,
  `timeouts` int(11) NOT NULL,
  `turnsCount` int(11) NOT NULL,
  `averageTurnTime` int(11) NOT NULL,
  `lastMoveTime` bigint(20) NOT NULL,
  `thirtyAverageRating` int(11) NOT NULL,
  `thirtyHighestRating` int(11) NOT NULL,
  `thirtyLowestRating` int(11) NOT NULL,
  `thirtyAverageOpponentRating` int(11) NOT NULL,
  `thirtyHighestWonOpponent` int(11) NOT NULL,
  `thirtyHighestWonOpponentId` bigint(20) NOT NULL,
  `thirtyLowestLostOpponent` int(11) NOT NULL,
  `thirtyLowestLostOpponentId` bigint(20) NOT NULL,
  `thirtyAverageMovesPerGame` int(11) NOT NULL,
  `ninetyAverageRating` int(11) NOT NULL,
  `ninetyHighestRating` int(11) NOT NULL,
  `ninetyLowestRating` int(11) NOT NULL,
  `ninetyAverageOpponentRating` int(11) NOT NULL,
  `ninetyHighestWonOpponent` int(11) NOT NULL,
  `ninetyHighestWonOpponentId` bigint(20) NOT NULL,
  `ninetyLowestLostOpponent` int(11) NOT NULL,
  `ninetyLowestLostOpponentId` bigint(20) NOT NULL,
  `ninetyAverageMovesPerGame` int(11) NOT NULL,
  `yearAverageRating` int(11) NOT NULL,
  `yearHighestRating` int(11) NOT NULL,
  `yearLowestRating` int(11) NOT NULL,
  `yearAverageOpponentRating` int(11) NOT NULL,
  `yearHighestWonOpponent` int(11) NOT NULL,
  `yearHighestWonOpponentId` bigint(20) NOT NULL,
  `yearLowestLostOpponent` int(11) NOT NULL,
  `yearLowestLostOpponentId` bigint(20) NOT NULL,
  `yearAverageMovesPerGame` int(11) NOT NULL,
  `allAverageRating` int(11) NOT NULL,
  `allHighestRating` int(11) NOT NULL,
  `allLowestRating` int(11) NOT NULL,
  `allAverageOpponentRating` int(11) NOT NULL,
  `allHighestWonOpponent` int(11) NOT NULL,
  `allHighestWonOpponentId` bigint(20) NOT NULL,
  `allLowestLostOpponent` int(11) NOT NULL,
  `allLowestLostOpponentId` bigint(20) NOT NULL,
  `allAverageMovesPerGame` int(11) NOT NULL,
  PRIMARY KEY  (`playerId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `sys_version`
--

DROP TABLE IF EXISTS `sys_version`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `sys_version` (
  `db_version` int(11) NOT NULL,
  `script_name` varchar(200) default NULL,
  `description` varchar(250) default NULL,
  `update_date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  USING BTREE (`db_version`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `tn_remember`
--

DROP TABLE IF EXISTS `tn_remember`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tn_remember` (
  `playerId` bigint(20) NOT NULL,
  `address` int(11) NOT NULL,
  `token` varchar(40) NOT NULL,
  `date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`playerId`,`address`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `tn_restore`
--

DROP TABLE IF EXISTS `tn_restore`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tn_restore` (
  `playerId` bigint(20) NOT NULL,
  `token` varchar(40) NOT NULL,
  `date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`playerId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `user_notification`
--

DROP TABLE IF EXISTS `user_notification`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `user_notification` (
  `playerId` bigint(20) NOT NULL COMMENT 'The id of player',
  `notifications` text NOT NULL COMMENT
          'The string that contains information about disabled notifications.\n',
  PRIMARY KEY  (`playerId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8
COMMENT='User notifications setting table.';
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `user_profile`
--

DROP TABLE IF EXISTS `user_profile`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `user_profile` (
  `playerId` bigint(20) NOT NULL,
  `realName` varchar(150) default NULL COMMENT 'Real name of player',
  `countryCode` char(2) default NULL COMMENT 'two chars country code',
  `city` varchar(100) default NULL COMMENT 'name of city',
  `timezone` int(11) NOT NULL default '0' COMMENT
          'unsigned TimeZone offset where 0 means no timezone, 1 - GMT-11, 2 - GMT-10 and so on.',
  `dateOfBirth` datetime default NULL COMMENT 'date of birth',
  `gender` int(11) default NULL COMMENT
          'gender: 0 - not specified, 1 - male, 2 - female.',
  `homepage` varchar(200) default NULL,
  `additionalInfo` text,
  PRIMARY KEY  USING BTREE (`playerId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `user_settings`
--

DROP TABLE IF EXISTS `user_settings`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `user_settings` (
  `playerId` bigint(20) NOT NULL,
  `profile` varchar(250) default NULL,
  `dashboard` varchar(250) default NULL,
  `gameboard` varchar(250) default NULL,
  `playboard` varchar(250) default NULL,
  `logging` varchar(250) default NULL,
  PRIMARY KEY  (`playerId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `user_user`
--

DROP TABLE IF EXISTS `user_user`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `user_user` (
  `id` bigint(20) unsigned NOT NULL auto_increment COMMENT 'id of user',
  `username` varchar(50) NOT NULL COMMENT 'User name. Max length is 50',
  `password` varchar(100) NOT NULL COMMENT
          'Password of user. Max length is 100.',
  `email` varchar(150) NOT NULL COMMENT 'EMail address of the user.',
  `creationDate` timestamp NOT NULL default '0000-00-00 00:00:00',
  `lastSigninDate` timestamp NOT NULL default '0000-00-00 00:00:00',
  `rating` int(11) NOT NULL default '1000',          `locale` varchar(10) default NULL,
  PRIMARY KEY  (`id`),
  KEY `id` (`id`),
  KEY `login_info` (`password`,`username`)
) ENGINE=MyISAM AUTO_INCREMENT=3007 DEFAULT CHARSET=utf8
COMMENT='User id, name and password table';
SET character_set_client = @saved_cs_client;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2009-06-27 18:58:15
