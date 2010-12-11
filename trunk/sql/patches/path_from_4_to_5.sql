ALTER TABLE `user_settings` ADD COLUMN `logging` VARCHAR(250)  AFTER `playboard`;

CREATE TABLE  `user_notification` (
`playerId` bigint(20) NOT NULL COMMENT 'The id of player',
`notifications` text NOT NULL COMMENT 'The string that contains information about disabled notifications.\n',
PRIMARY KEY  (`playerId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='User notifications setting table.'

INSERT INTO sys_version(db_version, script_name, description) VALUES (5, 'path_from_4_to_5.sql', 'Logging settings column added. User notifications table added');