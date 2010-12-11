CREATE TABLE `scrabble_memory` (
`boardId` bigint(20) NOT NULL COMMENT 'The id of board',
`playerId` bigint(20) NOT NULL COMMENT 'The id of player who made a word',
`wordNumber` int(11) NOT NULL COMMENT 'The number of word on the board',
`row` tinyint(4) NOT NULL default '0' COMMENT 'Row position of the word',
`col` tinyint(4) NOT NULL default '0' COMMENT 'Column position of the word',
`direction` tinyint(1) NOT NULL default '0' COMMENT 'Direction of word (vertical and horizontal). Boolean value.',
`word` varchar(75) default NULL COMMENT 'The made word. This field contains maximum 15 tiles in following format: \nTileNumberCharCost. Where TileNumber - 1 or two chars, \nChar - the letter,\nCost - the cost (one char).',
PRIMARY KEY  (`boardId`,`playerId`,`wordNumber`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

INSERT INTO sys_version(db_version, script_name, description) VALUES (3, 'path_from_2_to_3.sql', 'Memory Words table added.');