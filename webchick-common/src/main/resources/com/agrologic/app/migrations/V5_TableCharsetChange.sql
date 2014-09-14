--
-- Charset in all database tables must be utf8
--
ALTER DATABASE agrodb
CHARSET =utf8;
ALTER DATABASE agrodb
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`databylanguage` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`controllers` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`users` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`workers` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`workers` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`transactions` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`tabledata` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`tablebylanguage` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`systemstatenames` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`systemstatebylanguage` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`spread` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`systemstatebylanguage` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`specialdatalabels` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`special` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`screentable` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`screens` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`screenbylanguage` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`relaynames` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`relaybylanguage` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`programsysstates` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`programs` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`programrelays` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`programalarms` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`programactionset` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`newcontrollerdata` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`medicine` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`languages` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`labor` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`graph24hours` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`hits` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`gas` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`fuel` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`flocks` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`flockhistory` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`flockhistory24` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`feedtypes` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`eggs` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`domains` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`distribute` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`datatable` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`currency` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`controllerdata` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`controll` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`alarms` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`alarmnames` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`alarmbylanguage` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`alarmbylanguage` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`actionsetbylanguage` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;
ALTER TABLE `agrodb`.`actionset` CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;


# SELECT
#  `tables`.`TABLE_NAME`,
#  `collations`.`character_set_name`
# FROM
#   `information_schema`.`TABLES` AS `tables`,
#   `information_schema`.`COLLATION_CHARACTER_SET_APPLICABILITY` AS `collations`
# WHERE
#   `tables`.`table_schema` = DATABASE()
#   AND `collations`.`collation_name` = `tables`.`table_collation`
# ;
# show variables like 'char%';
# show variables like 'colla%';










