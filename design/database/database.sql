SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `wisematches` DEFAULT CHARACTER SET utf8 ;
USE `wisematches` ;

-- -----------------------------------------------------
-- Table `wisematches`.`account_personality`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS
        `wisematches`.`account_personality` (
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
  UNIQUE INDEX `email_UNIQUE` (`email` ASC)
)
  ENGINE = InnoDB
  COMMENT = 'The base table that contains information about a player';

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
  ON UPDATE NO ACTION
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `wisematches`.`account_blacknames`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS
        `wisematches`.`account_blacknames` (
  `username` VARCHAR(100) NOT NULL ,
  `reason` VARCHAR(255) NOT NULL ,
  PRIMARY KEY (`username`)
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `wisematches`.`persistent_logins`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS
        `wisematches`.`persistent_logins` (
  `series` VARCHAR(64) NOT NULL ,
  `username` VARCHAR(150) NOT NULL ,
  `token` VARCHAR(64) NOT NULL ,
  `last_used` TIMESTAMP NOT NULL ,
  PRIMARY KEY (`series`)
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `wisematches`.`persistent_recoveries`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS
        `wisematches`.`persistent_recoveries` (
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
  ON UPDATE NO ACTION
)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wisematches`.`scribble_board`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `wisematches`.`scribble_board` (
  `boardId` BIGINT(20)  NOT NULL AUTO_INCREMENT ,
  `title` VARCHAR(150) NOT NULL ,
  `rated` TINYINT(2) NULL DEFAULT 1 ,
  `daysPerMove` TINYINT(4) NULL ,
  `language` CHAR(2) NOT NULL ,
  `resolution` TINYINT(4) NULL ,
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
  UNIQUE INDEX `boardId_UNIQUE` (`boardId` ASC)
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `wisematches`.`scribble_player`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS
        `wisematches`.`scribble_player` (
  `playerId` BIGINT(20)  NOT NULL ,
  `boardId` BIGINT(20)  NOT NULL ,
  `playerIndex` TINYINT(4) NOT NULL ,
  `points` SMALLINT(6) NOT NULL ,
  INDEX `INDEX` (`playerId` ASC, `boardId` ASC) ,
  UNIQUE INDEX `UNIQUE` (`playerId` ASC, `boardId` ASC) ,
  INDEX `boardId` (`boardId` ASC) ,
  CONSTRAINT `boardId`
  FOREIGN KEY (`boardId` )
  REFERENCES `wisematches`.`scribble_board` (`boardId` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  COMMENT = 'There is no constrain between playerId and account_personality because system players don\'t have account.';

-- -----------------------------------------------------
-- Table `wisematches`.`rating_history`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `wisematches`.`rating_history` (
  `playerId` BIGINT(20)  NOT NULL ,
  `boardId` BIGINT(20)  NOT NULL ,
  `time` TIMESTAMP NOT NULL ,
  `oldRating` SMALLINT NOT NULL ,
  `newRating` SMALLINT NOT NULL ,
  `points` SMALLINT NOT NULL ,
  UNIQUE INDEX `history_UNIQUE` (`playerId` ASC, `boardId` ASC)
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `wisematches`.`scribble_statistic`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS
        `wisematches`.`scribble_statistic` (
  `playerId` BIGINT(20)  NOT NULL ,
  `rating` SMALLINT NULL ,
  `updateTime` TIMESTAMP NULL ,
  `wins` INT NULL ,
  `loses` INT NULL ,
  `draws` INT NULL ,
  `timeouts` INT NULL ,
  `active` INT NULL ,
  `unrated` INT NULL ,
  `finished` INT NULL ,
  `aRating` SMALLINT NULL ,
  `hRating` SMALLINT NULL ,
  `lRating` SMALLINT NULL ,
  `aoRating` SMALLINT NULL ,
  `hoRating` SMALLINT NULL ,
  `hoId` BIGINT(20)  NULL ,
  `loRating` SMALLINT NULL ,
  `loId` BIGINT(20)  NULL ,
  `lastMoveTime` TIMESTAMP NULL ,
  `avgMoveTime` INT NULL ,
  `avgGameMoves` INT NULL ,
  `turns` INT NULL ,
  `passes` INT NULL ,
  `lPoints` SMALLINT NULL ,
  `aPoints` SMALLINT NULL ,
  `hPoints` SMALLINT NULL ,
  `words` INT NULL ,
  `exchanges` INT NULL ,
  `aWord` BIGINT(20)  NULL ,
  `longestWord` VARCHAR(104) NULL ,
  `valuableWord` VARCHAR(104) NULL ,
  PRIMARY KEY (`playerId`) ,
  INDEX
          `fk_rating_statistic_account_personality1` (`playerId` ASC) ,
  CONSTRAINT
          `fk_rating_statistic_account_personality1`
  FOREIGN KEY (`playerId` )
  REFERENCES `wisematches`.`account_personality` (`id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `wisematches`.`scribble_memory`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS
        `wisematches`.`scribble_memory` (
  `boardId` BIGINT(20)  NOT NULL COMMENT 'The id of board' ,
  `playerId` BIGINT(20)  NOT NULL COMMENT
          'The id of player who made a word' ,
  `wordNumber` INT NOT NULL COMMENT
          'he number of word on the board' ,
  `row` TINYINT NULL COMMENT 'Row position of the word' ,
  `col` TINYINT NULL COMMENT 'Column position of the word' ,
  `direction` TINYINT(1)  NULL COMMENT
          'Direction of word (vertical and horizontal). Boolean value.' ,
  `word` VARCHAR(104) NULL COMMENT 'The made word. This field contains maximum 15 tiles in following format: \\nTileNumberCharCost. Where TileNumber - 1 or two chars,\\nChar - the letter,\\nCost - the cost (one char).' ,
  PRIMARY KEY (`boardId`, `playerId`, `wordNumber`)
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `wisematches`.`account_profile`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS
        `wisematches`.`account_profile` (
  `playerId` BIGINT(20)  NOT NULL ,
  `created` DATE NOT NULL ,
  `realName` VARCHAR(100) NULL ,
  `countryCode` CHAR(2) NULL ,
  `birthday` DATE NULL ,
  `gender` TINYINT NULL ,
  `language` CHAR(2) NULL ,
  `comments` TEXT NULL ,
  PRIMARY KEY (`playerId`) ,
  INDEX
          `fk_account_profile_account_personality1` (`playerId` ASC) ,
  CONSTRAINT
          `fk_account_profile_account_personality1`
  FOREIGN KEY (`playerId` )
  REFERENCES `wisematches`.`account_personality` (`id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
)
  ENGINE = InnoDB;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

ALTER TABLE account_personality AUTO_INCREMENT = 1001;