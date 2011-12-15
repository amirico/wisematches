SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

ALTER TABLE `wisematches`.`scribble_player` ADD COLUMN `oldRating` SMALLINT(6) NULL DEFAULT NULL  AFTER `points` , ADD COLUMN `newRating` SMALLINT(6) NULL DEFAULT NULL  AFTER `oldRating`
, ADD PRIMARY KEY (`playerIndex`, `boardId`) ;

UPDATE scribble_player as p LEFT JOIN rating_history as r
ON r.playerId=p.playerId and r.boardId=p.boardId
SET p.oldRating = r.oldRating, p.newRating=r.newRating;

UPDATE scribble_player as p SET p.oldRating = 0, p.newRating=0 WHERE p.oldRating is null;

DROP TABLE rating_history;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
