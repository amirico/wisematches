ALTER TABLE `scrabble_memory` MODIFY COLUMN `word` VARCHAR(104)  CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'The made word. This field contains maximum 15 tiles in following format: 
TileNumberCharCost. Where TileNumber - 1 or two chars,
Char - the letter,
Cost - the cost (one char).';

DELETE FROM scrabble_memory;

INSERT INTO sys_version(db_version, script_name, description) VALUES (4, 'path_from_3_to_4.sql', 'Size of letters in Words Memory table increated. All memory words removed.');