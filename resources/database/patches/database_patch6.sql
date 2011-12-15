ALTER TABLE `wisematches`.`scribble_statistic` CHANGE COLUMN `updateTime` `updateTime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  ;

ALTER TABLE `wisematches`.`player_message` CHANGE COLUMN `created` `created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE `wisematches`.`scribble_comment` CHANGE COLUMN `creationDate` `created` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP  AFTER `person` ;

ALTER TABLE `wisematches`.`player_friends` CHANGE COLUMN `registered` `registered` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP  ;
