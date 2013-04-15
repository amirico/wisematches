ALTER TABLE `wisematches`.`dictionary_changes` RENAME TO `wisematches`.`dictionary_reclaim`;

ALTER TABLE `wisematches`.`scribble_statistic` ADD COLUMN `reclaimsRaised` INT(11) NULL DEFAULT 0
AFTER `winsTT`, ADD COLUMN `reclaimsApproved` INT(11) NULL DEFAULT 0
AFTER `reclaimsRaised`, ADD COLUMN `reclaimsRejected` INT(11) NULL DEFAULT 0
AFTER `reclaimsApproved`;

ALTER TABLE `wisematches`.`notification_settings` CHANGE COLUMN `playground.dictionary.accepted` `playground.dictionary.approved` TINYINT(4) NULL DEFAULT 0;

ALTER TABLE `wisematches`.`dictionary_reclaim` CHANGE COLUMN `suggestionType` `resolutionType` TINYINT(4) NOT NULL, CHANGE COLUMN `suggestionState` `resolution` TINYINT(4) NOT NULL;

UPDATE scribble_statistic AS s LEFT JOIN
  (SELECT
     d.requester AS pid,
     count(*)    AS cnt
   FROM dictionary_reclaim AS d
   GROUP BY d.requester) AS w
    ON s.playerId = pid
SET s.reclaimsRaised=cnt;

UPDATE scribble_statistic AS s LEFT JOIN
  (SELECT
     d.requester AS pid,
     count(*)    AS cnt
   FROM dictionary_reclaim AS d
   WHERE d.resolutionType = 1
   GROUP BY d.requester) AS w
    ON s.playerId = pid
SET s.reclaimsApproved=cnt;

UPDATE scribble_statistic AS s LEFT JOIN
  (SELECT
     d.requester AS pid,
     count(*)    AS cnt
   FROM dictionary_reclaim AS d
   WHERE d.resolutionType = 2
   GROUP BY d.requester) AS w
    ON s.playerId = pid
SET s.reclaimsRejected=cnt;

UPDATE scribble_statistic
SET reclaimsRaised=0
WHERE reclaimsRaised IS null;
UPDATE scribble_statistic
SET reclaimsApproved=0
WHERE reclaimsApproved IS null;
UPDATE scribble_statistic
SET reclaimsRejected=0
WHERE reclaimsRejected IS null;
