CREATE TABLE `system` (
  `id` int  NOT NULL AUTO_INCREMENT,
  `db_version` int  NOT NULL,
  PRIMARY KEY (`id`)
)
ENGINE = MyISAM;

insert into system(id, db_version) values (1, 1);

ALTER TABLE `scrabble_board` ADD COLUMN `rated` TINYINT(3)  DEFAULT 1 AFTER `finishedDate`;