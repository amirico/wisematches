ALTER TABLE `system` RENAME TO `sys_version`;

ALTER TABLE `sys_version` DROP COLUMN `id`,
 MODIFY COLUMN `db_version` INTEGER  NOT NULL,
 ADD COLUMN `script_name` VARCHAR(200)  DEFAULT NULL AFTER `db_version`,
 ADD COLUMN `description` VARCHAR(250)  DEFAULT NULL AFTER `script_name`,
 ADD COLUMN `update_date` TIMESTAMP  NOT NULL AFTER `description`,
 DROP PRIMARY KEY,
 ADD PRIMARY KEY  USING BTREE(`db_version`);

DELETE FROM sys_version where db_version = 1; 

INSERT INTO sys_version(db_version, script_name, description) VALUES (1, 'path_from_0_to_1.sql', 'Adds rated field to scrabble_board. Creates system table.');

INSERT INTO sys_version(db_version, script_name, description) VALUES (2, 'path_from_1_to_2.sql', 'Updates system table to sys_version and adds more fields');