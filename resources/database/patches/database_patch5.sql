ALTER TABLE `wisematches`.`settings_notice` ADD COLUMN `game.challenge` INT(1) NULL DEFAULT 1  AFTER `game.message`;

update `wisematches`.`settings_notice` set `game.challenge`=1 where `game.challenge` is NULL;