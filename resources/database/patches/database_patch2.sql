ALTER TABLE `wisematches`.`player_message` ADD COLUMN `state` TINYINT(4) NULL DEFAULT 0  AFTER `board`;

ALTER TABLE `wisematches`.`player_message` DROP COLUMN `board`;