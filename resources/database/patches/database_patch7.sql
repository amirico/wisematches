ALTER TABLE `wisematches`.`settings_notice` CHANGE COLUMN `game.started` `game.state.started` INT(1) NULL DEFAULT NULL;

ALTER TABLE `wisematches`.`settings_notice` CHANGE COLUMN `game.finished` `game.state.finished` INT(1) NULL DEFAULT NULL AFTER `game.state.started`;

ALTER TABLE `wisematches`.`settings_notice` CHANGE COLUMN `game.challenge` `game.challenge.received` INT(1) NULL DEFAULT 1 AFTER `game.timeout.hour`;

ALTER TABLE `wisematches`.`settings_notice` ADD COLUMN `game.challenge.rejected` INT(1) NULL DEFAULT 0 AFTER `game.challenge.received`;

ALTER TABLE `wisematches`.`scribble_board` ADD COLUMN `playersCount` TINYINT(4) NOT NULL DEFAULT 2 AFTER `language`;

ALTER TABLE `wisematches`.`scribble_board` CHANGE COLUMN `daysPerMove` `daysPerMove` TINYINT(4) NOT NULL;

ALTER TABLE `wisematches`.`scribble_comment` ADD COLUMN `read` INT(11) NOT NULL DEFAULT 0;

