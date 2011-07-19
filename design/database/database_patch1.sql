SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE  TABLE IF NOT EXISTS `wisematches`.`player_message` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `created` TIMESTAMP NOT NULL ,
  `recipient` BIGINT(20) NOT NULL ,
  `text` TEXT NOT NULL ,
  `sender` BIGINT(20) NOT NULL DEFAULT 0 ,
  `notification` TINYINT(4) NOT NULL ,
  `original` BIGINT(20) NOT NULL DEFAULT 0 ,
  `board` BIGINT(20) NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE  TABLE IF NOT EXISTS `wisematches`.`player_blacklist` (
  `person` BIGINT(20) NOT NULL ,
  `whom` BIGINT(20) NOT NULL ,
  `since` TIMESTAMP NOT NULL ,
  `message` VARCHAR(254) NULL DEFAULT NULL ,
  PRIMARY KEY (`person`, `whom`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE  TABLE IF NOT EXISTS `wisematches`.`player_activity` (
  `pid` BIGINT(20) NOT NULL ,
  `last_messages_check` TIMESTAMP NULL DEFAULT NULL ,
  PRIMARY KEY (`pid`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

DROP TABLE IF EXISTS `wisematches`.`message` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
