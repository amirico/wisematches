ALTER TABLE `wisematches`.`tourney_regular_round` ADD COLUMN `groupsCount` INT(11) NULL DEFAULT NULL
AFTER `roundNumber`;

UPDATE tourney_regular_round AS r
SET groupsCount=(SELECT
                   count(*)
                 FROM tourney_regular_group g
                 WHERE g.roundId = r.id
                 GROUP BY roundId);

ALTER TABLE `wisematches`.`tourney_regular_division` CHANGE COLUMN `activeRound` `activeRound` INT(11) NULL DEFAULT NULL;

UPDATE tourney_regular_division
SET activeRound=null
WHERE activeRound = 0;

ALTER TABLE `wisematches`.`tourney_regular_division` ADD COLUMN `roundsCount` INT(11) NULL DEFAULT NULL
AFTER `finished`, CHANGE COLUMN `activeRound` `activeRound` BIGINT(20) NULL DEFAULT NULL;

UPDATE tourney_regular_division AS d
SET roundsCount=(SELECT
                   count(*)
                 FROM tourney_regular_round r
                 WHERE r.divisionId = d.id
                 GROUP BY r.divisionId);
