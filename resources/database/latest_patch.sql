ALTER TABLE `wisematches`.`tourney_regular_round` ADD COLUMN `groupsCount` INT(11) NULL DEFAULT NULL
AFTER `roundNumber`;

UPDATE tourney_regular_round AS r
SET groupsCount=(SELECT
                   count(*)
                 FROM tourney_regular_group g
                 WHERE g.roundId = r.id
                 GROUP BY roundId);