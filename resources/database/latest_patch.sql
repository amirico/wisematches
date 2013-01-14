ALTER TABLE `wisematches`.`notification_settings` CHANGE COLUMN `playground.tourney.finished` `playground.award.granted` TINYINT(4) NULL DEFAULT NULL;

UPDATE notification_settings
SET `playground.award.granted` = 0
WHERE `playground.award.granted` = 2;
