ALTER TABLE `wisematches`.`scribble_board` CHANGE COLUMN `movesCount` `movesCount` SMALLINT(6) NOT NULL;

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
