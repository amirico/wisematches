ALTER TABLE `wisematches`.`account_personality` DROP COLUMN `membership`;

ALTER TABLE `wisematches`.`persistent_logins` CHANGE COLUMN `last_used` `last_used` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

CREATE TABLE IF NOT EXISTS `wisematches`.`account_recovery` (
  `account`   BIGINT(20)  NOT NULL,
  `token`     VARCHAR(45) NOT NULL,
  `generated` DATETIME    NOT NULL,
  PRIMARY KEY (`account`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

ALTER TABLE `wisematches`.`scribble_board` CHANGE COLUMN `movesCount` `movesCount` SMALLINT(6) NOT NULL;

ALTER TABLE `wisematches`.`scribble_player` ADD COLUMN `winner` TINYINT(1) NULL DEFAULT NULL
AFTER `points`
, DROP PRIMARY KEY
, ADD PRIMARY KEY (`boardId`, `playerIndex`);

ALTER TABLE `wisematches`.`scribble_statistic` CHANGE COLUMN `updateTime` `updateTime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

ALTER TABLE `wisematches`.`player_message` DROP COLUMN `notification`;

ALTER TABLE `wisematches`.`player_blacklist` CHANGE COLUMN `since` `since` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE `wisematches`.`player_activity` CHANGE COLUMN `last_messages_check` `last_messages_check` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE `wisematches`.`player_friends` COLLATE = utf8_general_ci, CHANGE COLUMN `comment` `comment` VARCHAR(254) NULL DEFAULT NULL;

ALTER TABLE `wisematches`.`scribble_settings` COLLATE = utf8_general_ci, CHANGE COLUMN `cleanMemory` `cleanMemory` TINYINT(4) NULL DEFAULT 1, CHANGE COLUMN `tilesClass` `tilesClass` VARCHAR(45) NULL DEFAULT NULL;

ALTER TABLE `wisematches`.`tourney_regular` DROP COLUMN `lastChange`;

ALTER TABLE `wisematches`.`tourney_regular_round` DROP COLUMN `lastChange`;

ALTER TABLE `wisematches`.`tourney_regular_group` DROP COLUMN `lastChange`, CHANGE COLUMN `game1` `game1` BIGINT(20) NULL DEFAULT NULL
AFTER `player4`, CHANGE COLUMN `game2` `game2` BIGINT(20) NULL DEFAULT NULL
AFTER `game1`, CHANGE COLUMN `game3` `game3` BIGINT(20) NULL DEFAULT NULL
AFTER `game2`, CHANGE COLUMN `game4` `game4` BIGINT(20) NULL DEFAULT NULL
AFTER `game3`, CHANGE COLUMN `game5` `game5` BIGINT(20) NULL DEFAULT NULL
AFTER `game4`, CHANGE COLUMN `game6` `game6` BIGINT(20) NULL DEFAULT NULL
AFTER `game5`;

ALTER TABLE `wisematches`.`dictionary_changes` ADD COLUMN `commentary` VARCHAR(255) NULL DEFAULT NULL
AFTER `definition`, CHANGE COLUMN `definition` `definition` TEXT NULL DEFAULT NULL;

CREATE TABLE IF NOT EXISTS `wisematches`.`account_membership` (
  `pid`        BIGINT(20) NOT NULL,
  `membership` INT(11)    NOT NULL,
  `expiration` DATE       NOT NULL,
  PRIMARY KEY (`pid`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8
  COLLATE = utf8_general_ci;

DROP TABLE IF EXISTS `wisematches`.`tourney_regular_config`;

DROP TABLE IF EXISTS `wisematches`.`persistent_recoveries`;

UPDATE scribble_player
SET playerId = 100
WHERE playerId = 2;
UPDATE scribble_player
SET playerId = 101
WHERE playerId = 3;
UPDATE scribble_player
SET playerId = 102
WHERE playerId = 4;
UPDATE scribble_player
SET playerId = 200
WHERE playerId = 1;

UPDATE scribble_statistic
SET hoId = 100
WHERE hoId = 2;
UPDATE scribble_statistic
SET loId = 100
WHERE loId = 2;
UPDATE scribble_statistic
SET hoId = 101
WHERE hoId = 3;
UPDATE scribble_statistic
SET loId = 101
WHERE loId = 3;
UPDATE scribble_statistic
SET hoId = 102
WHERE hoId = 4;
UPDATE scribble_statistic
SET loId = 102
WHERE loId = 4;
UPDATE scribble_statistic
SET hoId = 200
WHERE hoId = 1;
UPDATE scribble_statistic
SET loId = 200
WHERE loId = 1;

UPDATE scribble_player
SET winner = FALSE;

# Ratings check query for player 1352
# select * from
# scribble_board as b left join scribble_player as l on  b.boardId = l.boardId
# left join scribble_player as r on l.boardId=r.boardId and l.playerId != r.playerId
# where l.playerId=1352 order by b.finishedDate;