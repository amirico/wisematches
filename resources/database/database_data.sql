ALTER TABLE `user_user` AUTO_INCREMENT = 1002;

INSERT IGNORE INTO sys_version(db_version, script_name, description)
        VALUES (1, 'path_from_0_to_1.sql',
                'Adds rated field to scribble_board. Creates system table.');

INSERT IGNORE INTO sys_version(db_version, script_name, description)
        VALUES (2, 'path_from_1_to_2.sql',
                'Updates system table to sys_version and adds more fields');

INSERT IGNORE INTO sys_version(db_version, script_name, description)
        VALUES (3, 'path_from_2_to_3.sql', 'Memory Words table added.');

INSERT IGNORE INTO sys_version(db_version, script_name, description)
        VALUES (4, 'path_from_3_to_4.sql',
                'Size of letters in Words Memory table increated. All memory words removed.');

INSERT IGNORE INTO sys_version(db_version, script_name, description)
        VALUES (5, 'path_from_4_to_5.sql',
                'Logging settings column added. User notification table added');

INSERT IGNORE INTO sys_version(db_version, script_name, description)
        VALUES (6, 'path_from_5_to_6.sql', 'Some tables renamed.');