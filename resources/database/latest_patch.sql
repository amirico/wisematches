ALTER TABLE `wisematches`.`notification_settings` ADD COLUMN `playground.award.granted` TINYINT(4) NULL DEFAULT 0
AFTER `playground.dictionary.rejected`;
