DELETE p1, p2
FROM
    scribble_board b LEFT JOIN
    (scribble_player p1 LEFT JOIN scribble_player p2
         ON p1.boardId = p2.boardId AND p1.playerId != p2.playerId)
      ON p1.boardId = b.boardId
WHERE p1.playerId < 1000 AND p2.playerId < 1000 AND b.playersCount = 2 AND b.resolution IS NOT null;

DELETE b FROM scribble_board b LEFT JOIN scribble_player p
    ON p.boardId = b.boardId
WHERE p.playerId IS null;
