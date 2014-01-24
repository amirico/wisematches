ALTER TABLE `wisematches`.`account_membership`
ADD INDEX `TYPE_INDEX` (`membership` ASC);

ALTER TABLE `wisematches`.`award_chronicle`
ADD INDEX `recipient_idx` (`recipient` ASC),
ADD INDEX `code_index` (`code` ASC),
ADD INDEX `weight_index` (`weight` ASC);
ALTER TABLE `wisematches`.`award_chronicle`
ADD CONSTRAINT `recipient`
FOREIGN KEY (`recipient`)
REFERENCES `wisematches`.`account_personality` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `wisematches`.`dictionary_reclaim`
ADD CONSTRAINT `requestor`
FOREIGN KEY (`requester`)
REFERENCES `wisematches`.`account_personality` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `wisematches`.`player_message`
ADD INDEX `rec_idx` (`recipient` ASC);
ALTER TABLE `wisematches`.`player_message`
ADD CONSTRAINT `rec`
FOREIGN KEY (`recipient`)
REFERENCES `wisematches`.`account_personality` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `wisematches`.`player_message`
ADD INDEX `state_index` (`state` ASC),
ADD INDEX `original_index` (`original` ASC),
ADD INDEX `sender_index` (`sender` ASC);

ALTER TABLE `wisematches`.`scribble_board`
ADD INDEX `currentPlayer_index` (`currentPlayerIndex` ASC),
ADD INDEX `resolution_index` (`resolution` ASC),
ADD INDEX `language_index` (`language` ASC),
ADD INDEX `days_index` (`daysPerMove` ASC),
ADD INDEX `last_move_index` (`lastMoveTime` ASC);

ALTER TABLE `wisematches`.`scribble_comment`
ADD INDEX `person_idx` (`person` ASC),
ADD INDEX `read_index` (`read` ASC),
ADD INDEX `board_idx` (`board` ASC);
ALTER TABLE `wisematches`.`scribble_comment`
ADD CONSTRAINT `person`
FOREIGN KEY (`person`)
REFERENCES `wisematches`.`account_personality` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
ADD CONSTRAINT `board`
FOREIGN KEY (`board`)
REFERENCES `wisematches`.`scribble_board` (`boardId`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `wisematches`.`scribble_memory`
ADD INDEX `person_idx` (`playerId` ASC);
ALTER TABLE `wisematches`.`scribble_memory`
ADD CONSTRAINT `person`
FOREIGN KEY (`playerId`)
REFERENCES `wisematches`.`account_personality` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `wisematches`.`scribble_statistic`
ADD INDEX `rating` (`rating` ASC);

ALTER TABLE `wisematches`.`tourney_regular_division`
ADD CONSTRAINT `tourney`
FOREIGN KEY (`tourneyId`)
REFERENCES `wisematches`.`tourney_regular` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

ALTER TABLE `wisematches`.`tourney_regular_division`
ADD INDEX `language_index` (`language` ASC);
