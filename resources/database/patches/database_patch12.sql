SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE=''TRADITIONAL'';

ALTER TABLE `wisematches`.`scribble_statistic` CHANGE COLUMN `updateTime` `updateTime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP  ;

ALTER TABLE `wisematches`.`player_activity` ADD COLUMN `last_activity` TIMESTAMP NULL DEFAULT NULL  AFTER `last_messages_check` ;

CREATE  TABLE IF NOT EXISTS `wisematches`.`player_notification` (
  `pid` BIGINT(20) NOT NULL ,
  `playground.game.started` DATETIME NULL DEFAULT NULL ,
  `playground.game.turn` DATETIME NULL DEFAULT NULL ,
  `playground.game.finished` DATETIME NULL DEFAULT NULL ,
  `playground.game.expiration.day` DATETIME NULL DEFAULT NULL ,
  `playground.game.expiration.half` DATETIME NULL DEFAULT NULL ,
  `playground.game.expiration.hour` DATETIME NULL DEFAULT NULL ,
  `playground.challenge.initiated` DATETIME NULL DEFAULT NULL ,
  `playground.challenge.rejected` DATETIME NULL DEFAULT NULL ,
  `playground.challenge.repudiated` DATETIME NULL DEFAULT NULL ,
  `playground.challenge.terminated` DATETIME NULL DEFAULT NULL ,
  `playground.challenge.expiration.days` DATETIME NULL DEFAULT NULL ,
  `playground.challenge.expiration.day` DATETIME NULL DEFAULT NULL ,
  `playground.message.received` DATETIME NULL DEFAULT NULL ,
  PRIMARY KEY (`pid`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

DROP TABLE IF EXISTS `wisematches`.`settings_notice` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
