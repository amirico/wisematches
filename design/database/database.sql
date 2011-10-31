SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `wisematches` DEFAULT CHARACTER SET utf8 ;
USE `wisematches` ;

-- -----------------------------------------------------
-- Table `wisematches`.`account_personality`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `wisematches`.`account_personality` (
  `id` BIGINT(20)  NOT NULL AUTO_INCREMENT ,
  `nickname` VARCHAR(100) NOT NULL ,
  `password` VARCHAR(100) NOT NULL ,
  `email` VARCHAR(150) NOT NULL ,
  `language` CHAR(2) NOT NULL ,
  `timezone` VARCHAR(45) NULL ,
  `membership` VARCHAR(10) NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) ,
  UNIQUE INDEX `username_UNIQUE` (`nickname` ASC) ,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'The base table that contains information about a player' ;


-- -----------------------------------------------------
-- Table `wisematches`.`account_lock`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `wisematches`.`account_lock` (
  `playerId` BIGINT(20)  NOT NULL ,
  `publicReason` VARCHAR(145) NOT NULL ,
  `privateReason` VARCHAR(145) NOT NULL ,
  `lockDate` DATETIME NOT NULL ,
  `unlockDate` DATETIME NOT NULL ,
  PRIMARY KEY (`playerId`) ,
  UNIQUE INDEX `userId_UNIQUE` (`playerId` ASC) ,
  INDEX `fk_user_lock_user_player` (`playerId` ASC) ,
  CONSTRAINT `fk_user_lock_user_player`
    FOREIGN KEY (`playerId` )
    REFERENCES `wisematches`.`account_personality` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `wisematches`.`account_blacknames`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `wisematches`.`account_blacknames` (
  `username` VARCHAR(100) NOT NULL ,
  `reason` VARCHAR(255) NOT NULL ,
  PRIMARY KEY (`username`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `wisematches`.`persistent_logins`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `wisematches`.`persistent_logins` (
  `series` VARCHAR(64) NOT NULL ,
  `username` VARCHAR(150) NOT NULL ,
  `token` VARCHAR(64) NOT NULL ,
  `last_used` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`series`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `wisematches`.`persistent_recoveries`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `wisematches`.`persistent_recoveries` (
  `playerId` BIGINT(20)  NOT NULL ,
  `token` VARCHAR(45) NOT NULL ,
  `date` DATETIME NOT NULL ,
  PRIMARY KEY (`playerId`) ,
  UNIQUE INDEX `playerId_UNIQUE` (`playerId` ASC) ,
  INDEX `id` (`playerId` ASC) ,
  CONSTRAINT `id`
    FOREIGN KEY (`playerId` )
    REFERENCES `wisematches`.`account_personality` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `wisematches`.`scribble_board`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `wisematches`.`scribble_board` (
  `boardId` BIGINT(20)  NOT NULL AUTO_INCREMENT ,
  `title` VARCHAR(150) NOT NULL ,
  `rated` TINYINT(2) NULL DEFAULT 1 ,
  `daysPerMove` TINYINT(4) NOT NULL ,
  `language` CHAR(2) NOT NULL ,
  `playersCount` TINYINT(4) NOT NULL DEFAULT 2 ,
  `resolution` TINYINT(4) NULL ,
  `movesCount` TINYINT(4) NOT NULL ,
  `passesCount` TINYINT(4) NOT NULL ,
  `currentPlayerIndex` TINYINT(4) NULL ,
  `redefinitions` TINYBLOB NULL ,
  `boardTiles` TINYBLOB NOT NULL ,
  `handTiles` TINYBLOB NOT NULL ,
  `moves` BLOB NOT NULL ,
  `startedDate` DATETIME NULL ,
  `finishedDate` DATETIME NULL ,
  `lastMoveTime` DATETIME NULL ,
  PRIMARY KEY (`boardId`) ,
  UNIQUE INDEX `boardId_UNIQUE` (`boardId` ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `wisematches`.`scribble_player`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `wisematches`.`scribble_player` (
  `playerId` BIGINT(20)  NOT NULL ,
  `boardId` BIGINT(20)  NOT NULL ,
  `playerIndex` TINYINT(4) NOT NULL ,
  `points` SMALLINT(6) NOT NULL ,
  `oldRating` SMALLINT(6) NULL ,
  `newRating` SMALLINT(6) NULL ,
  INDEX `INDEX` (`playerId` ASC, `boardId` ASC) ,
  UNIQUE INDEX `UNIQUE` (`playerId` ASC, `boardId` ASC) ,
  INDEX `boardId` (`boardId` ASC) ,
  PRIMARY KEY (`playerIndex`, `boardId`) ,
  CONSTRAINT `boardId`
    FOREIGN KEY (`boardId` )
    REFERENCES `wisematches`.`scribble_board` (`boardId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8, 
COMMENT = 'There is no constrain between playerId and account_personali' /* comment truncated */ ;


-- -----------------------------------------------------
-- Table `wisematches`.`scribble_statistic`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `wisematches`.`scribble_statistic` (
  `playerId` BIGINT(20) NOT NULL ,
  `rating` SMALLINT(6) NULL DEFAULT NULL ,
  `updateTime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP ,
  `wins` INT(11) NULL DEFAULT NULL ,
  `loses` INT(11) NULL DEFAULT NULL ,
  `draws` INT(11) NULL DEFAULT NULL ,
  `timeouts` INT(11) NULL DEFAULT NULL ,
  `active` INT(11) NULL DEFAULT NULL ,
  `unrated` INT(11) NULL DEFAULT NULL ,
  `finished` INT(11) NULL DEFAULT NULL ,
  `aRating` SMALLINT(6) NULL DEFAULT NULL ,
  `hRating` SMALLINT(6) NULL DEFAULT NULL ,
  `lRating` SMALLINT(6) NULL DEFAULT NULL ,
  `aoRating` SMALLINT(6) NULL DEFAULT NULL ,
  `hoRating` SMALLINT(6) NULL DEFAULT NULL ,
  `hoId` BIGINT(20) NULL DEFAULT NULL ,
  `loRating` SMALLINT(6) NULL DEFAULT NULL ,
  `loId` BIGINT(20) NULL DEFAULT NULL ,
  `lastMoveTime` TIMESTAMP NULL DEFAULT NULL ,
  `avgMoveTime` INT(11) NULL DEFAULT NULL ,
  `avgGameMoves` INT(11) NULL DEFAULT NULL ,
  `turns` INT(11) NULL DEFAULT NULL ,
  `passes` INT(11) NULL DEFAULT NULL ,
  `lPoints` SMALLINT(6) NULL DEFAULT NULL ,
  `aPoints` SMALLINT(6) NULL DEFAULT NULL ,
  `hPoints` SMALLINT(6) NULL DEFAULT NULL ,
  `words` INT(11) NULL DEFAULT NULL ,
  `exchanges` INT(11) NULL DEFAULT NULL ,
  `aWord` BIGINT(20) NULL DEFAULT NULL ,
  `longestWord` VARCHAR(255) NULL DEFAULT NULL ,
  `valuableWord` VARCHAR(255) NULL DEFAULT NULL ,
  `allHandBonuses` INT(11) NULL DEFAULT 0 ,
  PRIMARY KEY (`playerId`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;


-- -----------------------------------------------------
-- Table `wisematches`.`scribble_memory`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `wisematches`.`scribble_memory` (
  `boardId` BIGINT(20)  NOT NULL COMMENT 'The id of board' ,
  `playerId` BIGINT(20)  NOT NULL COMMENT 'The id of player who made a word' ,
  `wordNumber` INT NOT NULL COMMENT 'he number of word on the board' ,
  `row` TINYINT NULL COMMENT 'Row position of the word' ,
  `col` TINYINT NULL COMMENT 'Column position of the word' ,
  `direction` TINYINT(1)  NULL COMMENT 'Direction of word (vertical and horizontal). Boolean value.' ,
  `word` VARCHAR(104) NULL COMMENT 'The made word. This field contains maximum 15 tiles in following format: \\nTileNumberCharCost. Where TileNumber - 1 or two chars,\\nChar - the letter,\\nCost - the cost (one char).' ,
  PRIMARY KEY (`boardId`, `playerId`, `wordNumber`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `wisematches`.`account_profile`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `wisematches`.`account_profile` (
  `playerId` BIGINT(20)  NOT NULL ,
  `created` DATE NOT NULL ,
  `realName` VARCHAR(100) NULL ,
  `countryCode` CHAR(2) NULL ,
  `birthday` DATE NULL ,
  `gender` TINYINT NULL ,
  `language` CHAR(2) NULL ,
  `comments` TEXT NULL ,
  PRIMARY KEY (`playerId`) ,
  INDEX `fk_account_profile_account_personality1` (`playerId` ASC) ,
  CONSTRAINT `fk_account_profile_account_personality1`
    FOREIGN KEY (`playerId` )
    REFERENCES `wisematches`.`account_personality` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `wisematches`.`settings_notice`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `wisematches`.`settings_notice` (
  `pid` BIGINT(20) NOT NULL ,
  `game.state.started` INT(1) NULL DEFAULT NULL ,
  `game.state.finished` INT(1) NULL DEFAULT NULL ,
  `game.move.your` INT(1) NULL DEFAULT NULL ,
  `game.move.opponent` INT(1) NULL DEFAULT NULL ,
  `game.timeout.day` INT(1) NULL DEFAULT NULL ,
  `game.timeout.half` INT(1) NULL DEFAULT NULL ,
  `game.timeout.hour` INT(1) NULL DEFAULT NULL ,
  `game.challenge.received` INT(1) NULL DEFAULT '1' ,
  `game.challenge.rejected` INT(1) NULL DEFAULT '0' ,
  `game.message` INT(1) NULL DEFAULT NULL ,
  PRIMARY KEY (`pid`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;


-- -----------------------------------------------------
-- Table `wisematches`.`player_message`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `wisematches`.`player_message` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `recipient` BIGINT(20) NOT NULL ,
  `text` TEXT NOT NULL ,
  `sender` BIGINT(20) NOT NULL DEFAULT '0' ,
  `notification` TINYINT(4) NOT NULL ,
  `original` BIGINT(20) NOT NULL DEFAULT '0' ,
  `state` TINYINT(4) NULL DEFAULT '0' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;


-- -----------------------------------------------------
-- Table `wisematches`.`player_blacklist`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `wisematches`.`player_blacklist` (
  `person` BIGINT(20)  NOT NULL ,
  `whom` BIGINT(20)  NOT NULL ,
  `since` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `message` VARCHAR(254) NULL ,
  PRIMARY KEY (`person`, `whom`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wisematches`.`player_activity`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `wisematches`.`player_activity` (
  `pid` BIGINT(20)  NOT NULL ,
  `last_messages_check` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`pid`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `wisematches`.`scribble_comment`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `wisematches`.`scribble_comment` (
  `id` BIGINT(20)  NOT NULL AUTO_INCREMENT ,
  `board` BIGINT(20)  NULL ,
  `person` BIGINT(20)  NULL ,
  `created` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ,
  `text` VARCHAR(254) NULL ,
  `read` INT NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wisematches`.`player_friends`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `wisematches`.`player_friends` (
  `person` BIGINT(20)  NOT NULL ,
  `friend` BIGINT(20)  NOT NULL ,
  `registered` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ,
  `comment` VARCHAR(254) NULL ,
  PRIMARY KEY (`person`, `friend`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wisematches`.`scribble_settings`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `wisematches`.`scribble_settings` (
  `playerId` BIGINT(20)  NOT NULL ,
  `cleanMemory` INT NULL DEFAULT 1 ,
  `checkWords` TINYINT(4)  NULL DEFAULT 1 ,
  `tilesClass` VARCHAR(45) NULL ,
  PRIMARY KEY (`playerId`) )
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
