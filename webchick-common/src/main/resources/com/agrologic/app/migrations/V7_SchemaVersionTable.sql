--
-- New table for schema version
--
DROP TABLE IF EXISTS `agrodb`.`version`;
CREATE TABLE  `agrodb`.`version` (
  `Version` varchar(5) NOT NULL,
  `Description` varchar(45) NOT NULL,
  PRIMARY KEY (`Version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `agrodb`.`version` (`Version`, `Description`)
VALUES ('1.0', 'Complete and fixed database version');