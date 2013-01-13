CREATE TABLE IF NOT EXISTS `wisematches`.`award_chronicle` (
  `id`               BIGINT(20)  NOT NULL AUTO_INCREMENT,
  `player`           BIGINT(20)  NOT NULL,
  `code`             VARCHAR(40) NOT NULL,
  `awardedDate`      DATETIME    NOT NULL,
  `weight`           INT         NULL,
  `relationshipCode` INT         NULL,
  `relationshipId`   BIGINT(20)  NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;

ALTER TABLE `wisematches`.`scribble_statistic` ADD COLUMN `winsRD` INT(11) NULL DEFAULT 0
AFTER `allHandBonuses`, ADD COLUMN `winsRT` INT(11) NULL DEFAULT 0
AFTER `winsRD`, ADD COLUMN `winsRE` INT(11) NULL DEFAULT 0
AFTER `winsRT`, ADD COLUMN `winsTF` INT(11) NULL DEFAULT 0
AFTER `winsRE`, ADD COLUMN `winsTS` INT(11) NULL DEFAULT 0
AFTER `winsTF`, ADD COLUMN `winsTT` INT(11) NULL DEFAULT 0
AFTER `winsTS`;


SELECT
  p.playerId,
  r.playerId,
  count(*)
FROM
    scribble_player p
    LEFT JOIN
    scribble_player r
      ON p.boardId = r.boardId AND p.playerId != r.playerId
    LEFT JOIN
    scribble_board b
      ON b.boardId = p.boardId
WHERE p.playerId > 100 AND r.playerId < 100 AND p.points > r.points AND p.newRating > p.oldRating
      AND r.playerId = 3
GROUP BY p.playerId, r.playerId
HAVING count(*) >= 5;

UPDATE scribble_statistic AS s LEFT JOIN (SELECT
                                            p.playerId AS pid,
                                            count(*)   AS cnt
                                          FROM
                                              scribble_player p
                                              LEFT JOIN
                                              scribble_player r
                                                ON p.boardId = r.boardId AND p.playerId != r.playerId
                                              LEFT JOIN
                                              scribble_board b
                                                ON b.boardId = p.boardId
                                          WHERE p.playerId > 100 AND r.playerId < 100 AND p.points > r.points AND
                                                p.newRating > p.oldRating
                                                AND r.playerId = 2
                                          GROUP BY p.playerId) AS w
    ON pid = s.playerId
SET winsRD = cnt;

UPDATE scribble_statistic AS s LEFT JOIN (SELECT
                                            p.playerId AS pid,
                                            count(*)   AS cnt
                                          FROM
                                              scribble_player p
                                              LEFT JOIN
                                              scribble_player r
                                                ON p.boardId = r.boardId AND p.playerId != r.playerId
                                              LEFT JOIN
                                              scribble_board b
                                                ON b.boardId = p.boardId
                                          WHERE p.playerId > 100 AND r.playerId < 100 AND p.points > r.points AND
                                                p.newRating > p.oldRating
                                                AND r.playerId = 3
                                          GROUP BY p.playerId) AS w
    ON pid = s.playerId
SET winsRT = cnt;

UPDATE scribble_statistic AS s LEFT JOIN (SELECT
                                            p.playerId AS pid,
                                            count(*)   AS cnt
                                          FROM
                                              scribble_player p
                                              LEFT JOIN
                                              scribble_player r
                                                ON p.boardId = r.boardId AND p.playerId != r.playerId
                                              LEFT JOIN
                                              scribble_board b
                                                ON b.boardId = p.boardId
                                          WHERE p.playerId > 100 AND r.playerId < 100 AND p.points > r.points AND
                                                p.newRating > p.oldRating
                                                AND r.playerId = 4
                                          GROUP BY p.playerId) AS w
    ON pid = s.playerId
SET winsRE = cnt;

UPDATE scribble_statistic
SET winsRE = 0
WHERE winsRE IS null;
UPDATE scribble_statistic
SET winsRT = 0
WHERE winsRT IS null;
UPDATE scribble_statistic
SET winsRD = 0
WHERE winsRD IS null;


