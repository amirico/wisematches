# Select number of wins

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

# Update player wins
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