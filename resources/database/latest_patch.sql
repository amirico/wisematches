ALTER TABLE `wisematches`.`award_chronicle` ADD COLUMN `uuid` INT(11) NOT NULL DEFAULT 0
AFTER `recipient`, CHANGE COLUMN `player` `recipient` BIGINT(20) NOT NULL, CHANGE COLUMN `code` `code` VARCHAR(40) NULL DEFAULT NULL;

UPDATE award_chronicle
SET uuid = 3100
WHERE code = 'robot.conqueror';
UPDATE award_chronicle
SET uuid = 1100
WHERE code = 'tourney.winner';
UPDATE award_chronicle
SET uuid = 2100
WHERE code = 'dictionary.editor';

ALTER TABLE `wisematches`.`award_chronicle` DROP COLUMN `code`, CHANGE COLUMN `awardedDate` `awarded` DATETIME NOT NULL;

ALTER TABLE `wisematches`.`award_chronicle` CHANGE COLUMN `uuid` `code` INT(11) NOT NULL DEFAULT 0
AFTER `id`, CHANGE COLUMN `weight` `weight` INT(11) NULL DEFAULT NULL
AFTER `recipient`;
